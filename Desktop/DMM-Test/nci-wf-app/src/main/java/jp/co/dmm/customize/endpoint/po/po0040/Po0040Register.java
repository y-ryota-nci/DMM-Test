package jp.co.dmm.customize.endpoint.po.po0040;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.numbering.PartsNumberingService;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * kintoneデータアップロード画面の登録ロジック
 */
@ApplicationScoped
public class Po0040Register extends MiscUtils {
	@Inject private Logger log;
	@Inject private Po0040Repository repository;
	/** パーツの採番サービス */
	@Inject private PartsNumberingService service;
	/** 申請・承認画面サービス */
	@Inject private Vd0310Service vd0310Service;
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** (画面デザイナー)画面定義読込サービス */
	@Inject private ScreenLoadService screenLoadService;
	/** SQLサービス */
	@Inject private SqlService sqlService;
	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;

	/** ロック実行 */
	@Transactional
	public WfmCorpPropMaster lock() {
		// 他ユーザが同時にアップロードしないよう、排他ロック
		return repository.lock();
	}

	/** ロック解除 */
	@Transactional
	public void unlock(WfmCorpPropMaster entity) {
		repository.unlock(entity);
	}

	/**
	 * DBへの差分登録処理
	 * @param book
	 * @param lotNo ロットNo
	 */
	@Transactional
	public void save(Po0040Book book, final String companyCd, final String lotNo) {
		final long start = System.currentTimeMillis();
		log.debug("save() START");

		// 同じロットNoで作成された旧データを一括削除
		deleteOldData(companyCd, lotNo);

		// kintoneデータを[開催日(月)] + [取引先コード]をキーにしてMapへ変換
		final Map<String, List<Po0040KntnInf>> map = toMap(book.kntnInfs);

		// kitone情報へkintoneデータを登録
		registKntnInf(map, companyCd, lotNo, book.purordNoId, book.rcvinspNoId);

		// 発注情報の一括登録
		repository.bulkInsertPurordInf(companyCd, lotNo);
		// 発注明細情報の一括登録
		repository.bulkInsertPurorddtlInf(companyCd, lotNo);
		// 検収情報の一括登録
		repository.bulkInsertRcvinspInf(companyCd, lotNo);
		// 検収明細情報の一括登録
		repository.bulkInsertRcvinspdtlInf(companyCd, lotNo);
		// 仕入情報の一括登録
		repository.bulkInsertBuyInf(companyCd, lotNo);
		// 仕入明細情報の一括登録
		repository.bulkInsertBuydtlInf(companyCd, lotNo);

		// SS-GL連携
		repository.ssSendRcv(companyCd, lotNo);

		// 発注申請のワークフローを作成（実際にワークフローを処理するのではなく、閲覧可能リスト等で参照できるようにするためのダミーのデータ生成）
		createPurOrdWf(map, companyCd, lotNo);
		// 検収申請のワークフローを作成（実際にワークフローを処理するのではなく、閲覧可能リスト等で参照できるようにするためのダミーのデータ生成）
		createRcvInspWf(map, companyCd, lotNo);
		// 最後に支払依頼申請のワークフローを起票
		applyPayReqWf(map, book.screenProcessId, companyCd, lotNo);

		log.debug("save() END --> {}msec", (System.currentTimeMillis() - start));
	}

	/** kintoneデータを[開催日(月)] + [取引先コード]をキーにしてMapへ変換 */
	private Map<String, List<Po0040KntnInf>> toMap(List<Po0040KntnInf> list) {
		// kintoneデータを[開催日(月)] + [取引先コード]をキーにしてMapへ変換
		// 複合キーを作成
		Function<Po0040KntnInf, String> compositeKey = bean -> {
			// 開催日から月(YYYYMM)を取得
			final String yymm = MiscUtils.toStr(MiscUtils.toDate(bean.exhbDt, MiscUtils.FORMAT_DATE), "yyyyMM");
		    return yymm + "-" + bean.splrCd;
		};
		// 複合キーでグルーピング
		return list.stream().collect(Collectors.groupingBy(compositeKey, LinkedHashMap::new, Collectors.toList()));
	}

	/**
	 * kintone情報への登録処理.
	 * @param map
	 * @param lotNo ロットNo
	 * @param purordNoId 発注Noのパーツ採番形式ID
	 * @param rcvinspNoId 検収Noのパーツ採番形式ID
	 * @param buyNoId 仕入Noのパーツ採番形式ID
	 */
	private void registKntnInf(final Map<String, List<Po0040KntnInf>> map, final String companyCd, final String lotNo, final Long purordNoId, final Long rcvinspNoId) {
		final long start = System.currentTimeMillis();
		log.debug("registKntnInf() START");

		for (String key : map.keySet()) {
			final String purordNo = service.getNumber(purordNoId);
			final String rcvinspNo = service.getNumber(rcvinspNoId);
			final String buyNo = repository.getBuyNo(companyCd);
			// 最初のレコードに対して発注No、検収No、仕入Noを設定しておく
			// 後で行うWF発注情報、WF検収情報の作成時に使用するよ
			List<Po0040KntnInf> datas = map.get(key);
			Po0040KntnInf first = datas.get(0);
			first.purordNo = purordNo;
			first.rcvinspNo = rcvinspNo;
			first.buyNo = buyNo;

			// kintone情報へ一括登録
			repository.bulkInsertKntnInf(datas, companyCd, lotNo, purordNo, rcvinspNo, buyNo);
		}

		log.debug("registKntnInf() END --> {}msec", (System.currentTimeMillis() - start));
	}

	/**
	 * 新規_支払依頼申請の起票処理.
	 * @param map
	 * @param screenProcessId 画面プロセス定義ID
	 * @param companyCd 企業コード
	 * @param lotNo ロットNo
	 */
	private void applyPayReqWf(final Map<String, List<Po0040KntnInf>> map, final Long screenProcessId, final String companyCd, final String lotNo) {
		final long start = System.currentTimeMillis();
		log.debug("applyPayReqWf() START");

		try {
			// 開催日(年月) + 取引先コード毎に支払依頼申請の起票処理を実行
			for (String key : map.keySet()) {
				// Vd0310Service#initを呼出し、支払依頼申請の初期化処理を実行
				final Vd0310InitRequest initReq = new Vd0310InitRequest();
				initReq.screenProcessId = screenProcessId;
				initReq.trayType = TrayType.NEW;
				initReq.viewWidth = ViewWidth.LG;	// レンダリングはしないから何でもよい
				final Vd0310InitResponse initRes = vd0310Service.init(initReq);
				// 保存のアクションコードを取得
				final ActionInfo saveActionInfo = initRes.contents.actionList.stream()
													.filter(a -> ActionType.SAVE.equals(a.actionType))
													.findFirst().orElse(null);

				// 支払依頼申請の起票処理
				final Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
				execReq.contents = initRes.contents;
				execReq.actionInfo = saveActionInfo;
				execReq.startUserInfo = initRes.contents.startUserInfo;
				execReq.actionComment = null;
				execReq.additionAttachFileWfList = new ArrayList<>();
				execReq.removeAttachFileWfIdList = new ArrayList<>();
				execReq.additionInformationSharerList = new ArrayList<>();
				execReq.removeInformationSharerList = new ArrayList<>();
				execReq.changeRouteList = new ArrayList<>();
				execReq.approvalRelationList = new ArrayList<>();
				// WF支払依頼、WF支払依頼明細のトランザクションテーブルへ書き込むデータを読み込む
				final RuntimeContext ctx = RuntimeContext.newInstance(execReq);
				screenLoadService.loadScreenParts(ctx.screenId, ctx);
				fillUserData(ctx, companyCd, lotNo, key);
				execReq.runtimeMap = ctx.runtimeMap;

				// アクション実行処理(vd0310のアクション実行処理を呼び出す)
				final BaseResponse execRes = vd0310Service.execute(execReq);
				if (execRes instanceof Vd0310ExecuteResponse) {
					final Vd0310ExecuteResponse res = (Vd0310ExecuteResponse)execRes;
					if (res.errors != null && !res.errors.isEmpty()) {
						res.errors.stream().forEach(e -> log.error(e.htmlId + ":" + e.labelText + ":" + e.message));
						throw new InternalServerErrorException("支払依頼申請の起票処理にてエラーが発生したため、kintoneデータ取込処理に失敗しました。");
					}
				}
			}
		} catch (InternalServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerErrorException("支払依頼申請の起票処理にてエラーが発生したため、kintoneデータ取込処理に失敗しました。", e);
		} finally {
			log.debug("applyPayReqWf() END --> {}msec", (System.currentTimeMillis() - start));
		}
	}

	private void fillUserData(final RuntimeContext ctx, final String companyCd, final String lotNo, final String key) {
		// keyを"-"で分割して開催日(年月)と取引先コードを取得
		final String exhbYm = key.split("-")[0];
		final String splrCd = key.split("-")[1];

		// WF支払依頼に設定するデータを読み込み
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		{
			final Object[] params = getMwtPayDataParams(companyCd, lotNo, exhbYm, splrCd);
			final String tableName = "MWT_PAY";	// ヘッダ部のコンテナのテーブル名
			final String sql = sqlService.get("PO0040_18");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// WF支払依頼明細に設定するデータを読み込み
		{
			final Object[] params = getMwtPaydtlDataParams(companyCd, lotNo, exhbYm, splrCd);
			final String tableName = "MWT_PAYDTL";	// 明細部のコンテナのテーブル名
			final String sql = sqlService.get("PO0040_19");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// 読み込んだデータからランタイムMapを生成
		loader.fillUserData(ctx, tables, false);
	}

	private Object[] getMwtPayDataParams(final String companyCd, final String lotNo, final String exhbYm, final String splrCd) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Object[] params = {
			login.getUserCode(), login.getOrganizationCode(), login.getOrganizationName(), login.getOrganizationCodeUp3(),
			login.getUserName(), login.getExtendedInfo01(), login.getSbmtrAddr(),
			companyCd, lotNo, exhbYm, splrCd, exhbYm, login.getCorporationCode(), login.getOrganizationCode()
		};
		return params;
	}

	private Object[] getMwtPaydtlDataParams(final String companyCd, final String lotNo, final String exhbYm, final String splrCd) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Object[] params = { login.getOrganizationCodeUp3(), companyCd, lotNo, exhbYm, splrCd, login.getOrganizationCodeUp3() };
		return params;
	}

	/** 前回の同じロットNoで作成された発注等のデータを削除 */
	private void deleteOldData(final String companyCd, final String lotNo) {
		final long start = System.currentTimeMillis();
		log.debug("deleteOldData() START");

		// 発注データの削除（発注情報、WF発注情報、WF発注申請等）
		// WF発注申請のプロセスインスタンスを先に消すとWF発注情報が削除できなくなので削除の順番は注意すること
		repository.deletePurordInf(companyCd, lotNo);
		repository.deleteOrdDtlInf(companyCd, lotNo);
		repository.deleteWfPurord(companyCd, lotNo);
		repository.deleteWfPurordDtl(companyCd, lotNo);
		repository.deleteWfPurordProcess(companyCd, lotNo);
		// 検収情報、仕入情報を削除する前に検収取消を実行する（プロシージャ内部でSS-GL連携の取消処理までやってくれる）
		repository.cancelRcvInsp(companyCd, lotNo);
		// 検収データの削除（検収情報、WF検収情報、WF検収申請等）
		// WF検収申請のプロセスインスタンスを先に消すとWF検収情報が削除できなくなので削除の順番は注意すること
		repository.deleteRcvInspInf(companyCd, lotNo);
		repository.deleteRcvInspDtlInf(companyCd, lotNo);
		repository.deleteWfRcvInsp(companyCd, lotNo);
		repository.deleteWfRcvInspDtl(companyCd, lotNo);
		repository.deleteWfRcvInspProcess(companyCd, lotNo);
		// 仕入データの削除（仕入情報等）
		repository.deleteBuyInf(companyCd, lotNo);
		repository.deleteBuyDtlInf(companyCd, lotNo);
		// 支払データの削除（WF支払情報、WF支払申請等）
		// 発注／検収と違ってこちらは先にWF支払情報を消すとWF支払申請が削除できなくなるので削除の順番は注意すること
		repository.deleteWftProcess(companyCd, lotNo);
		repository.deleteMwtPaydtl(companyCd, lotNo);
		repository.deleteMwtPay(companyCd, lotNo);
		// KNTN情報の削除
		repository.deleteKntnInf(companyCd, lotNo);

		log.debug("deleteOldData() END --> {}msec", (System.currentTimeMillis() - start));
	}

	/**
	 * WF発注申請データ作成処理.
	 * @param map
	 * @param companyCd 企業コード
	 * @param lotNo ロットNo
	 */
	private void createPurOrdWf(Map<String, List<Po0040KntnInf>> map, String companyCd, String lotNo) {
		final long start = System.currentTimeMillis();
		log.debug("createPurOrdWf() START");

		try {
			// 発注情報毎に発注申請のワークフローデータ作成処理を実行
			for (String key : map.keySet()) {
				repository.createWfPurord(companyCd, map.get(key).get(0).purordNo);
			}
		} catch (InternalServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerErrorException("WF発注申請の作成処理にてエラーが発生したため、kintoneデータ取込処理に失敗しました。", e);
		} finally {
			log.debug("createPurOrdWf() END --> {}msec", (System.currentTimeMillis() - start));
		}
	}

	/**
	 * WF検収申請データ作成処理.
	 * @param map
	 * @param companyCd 企業コード
	 * @param lotNo ロットNo
	 */
	private void createRcvInspWf(Map<String, List<Po0040KntnInf>> map, String companyCd, String lotNo) {
		final long start = System.currentTimeMillis();
		log.debug("createRcvInspWf() START");

		try {
			// 検収情報毎に検収申請のワークフローデータ作成処理を実行
			for (String key : map.keySet()) {
				repository.createWfRcvinsp(companyCd, map.get(key).get(0).rcvinspNo);
			}
		} catch (InternalServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerErrorException("WF検収申請の作成処理にてエラーが発生したため、kintoneデータ取込処理に失敗しました。", e);
		} finally {
			log.debug("createRcvInspWf() END --> {}msec", (System.currentTimeMillis() - start));
		}
	}
}
