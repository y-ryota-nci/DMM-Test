package jp.co.dmm.customize.endpoint.batch.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

public abstract class BatchBaseService extends BaseService {
	@Inject private Logger log;
	/** 申請・承認画面サービス */
	@Inject private Vd0310Service vd0310Service;
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** (画面デザイナー)画面定義読込サービス */
	@Inject private ScreenLoadService screenLoadService;
	/** SQLサービス */
	@Inject private SqlService sqlService;

	/**
	 * プロセス情報作成
	 * @param in INパラメータ
	 * @return OUT
	 */
	public Long createRequest(String companyCd, long screenProcessId, String targetYYYYMM, String splrCd, String comment) {

		final long start = System.currentTimeMillis();
		log.debug("申請起票() START");

		try {
			// Vd0310Service#initを呼出し、支払依頼申請の初期化処理を実行
			final Vd0310InitRequest initReq = new Vd0310InitRequest();
			initReq.screenProcessId = screenProcessId;
			initReq.trayType = TrayType.NEW;
			initReq.viewWidth = ViewWidth.LG;	// レンダリングはしないから何でもよい
			final Vd0310InitResponse initRes = vd0310Service.init(initReq);
			// 申請のアクションコードを取得
			final ActionInfo requstActionInfo = initRes.contents.actionList.stream()
												.filter(a -> ActionType.NORMAL.equals(a.actionType))
												.findFirst().orElse(null);

			// 支払依頼申請の起票処理
			final Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
			execReq.contents = initRes.contents;
			execReq.actionInfo = requstActionInfo;
			execReq.startUserInfo = initRes.contents.startUserInfo;
			execReq.actionComment = comment;
			execReq.additionAttachFileWfList = new ArrayList<>();
			execReq.removeAttachFileWfIdList = new ArrayList<>();
			execReq.additionInformationSharerList = new ArrayList<>();
			execReq.removeInformationSharerList = new ArrayList<>();
			execReq.changeRouteList = new ArrayList<>();
			execReq.approvalRelationList = new ArrayList<>();

			// WF支払依頼、WF支払依頼明細のトランザクションテーブルへ書き込むデータを読み込む
			final RuntimeContext ctx = RuntimeContext.newInstance(execReq);
			screenLoadService.loadScreenParts(ctx.screenId, ctx);
			fillUserData(ctx, companyCd, targetYYYYMM, splrCd);
			execReq.runtimeMap = ctx.runtimeMap;

			// アクション実行処理(vd0310のアクション実行処理を呼び出す)
			final BaseResponse execRes = vd0310Service.execute(execReq);
			if (execRes instanceof Vd0310ExecuteResponse) {
				final Vd0310ExecuteResponse res = (Vd0310ExecuteResponse)execRes;
				if (res.errors != null && !res.errors.isEmpty()) {
					throw new InternalServerErrorException("申請の起票処理にてエラーが発生したため、申請処理に失敗しました。");
				}

				return res.processId;
			}
		} catch (InternalServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerErrorException("申請の起票処理にてエラーが発生したため、申請処理に失敗しました。", e);
		} finally {
			log.debug("申請起票() END --> {}msec", (System.currentTimeMillis() - start));
		}

		return null;

	}

	private void fillUserData(final RuntimeContext ctx, final String companyCd, final String exhbYm, final String splrCd) {

		// WF支払依頼に設定するデータを読み込み
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		{
			final Object[] params = getMwtPayDataParams(companyCd, "K" + exhbYm, exhbYm, splrCd);
			final String tableName = "MWT_PAY";	// ヘッダ部のコンテナのテーブル名
			final String sql = sqlService.get("PO0040_18");
			final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// WF支払依頼明細に設定するデータを読み込み
		{
			final Object[] params = getMwtPaydtlDataParams(companyCd, "K" + exhbYm, exhbYm, splrCd);
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
			login.getUserCode(), login.getOrganizationCode5(), login.getOrganizationName5(), login.getOrganizationCodeUp3(),
			login.getUserName(), login.getExtendedInfo01(), login.getSbmtrAddr(),
			companyCd, lotNo, exhbYm, splrCd, login.getCorporationCode(), login.getOrganizationCode()
		};
		return params;
	}

	private Object[] getMwtPaydtlDataParams(final String companyCd, final String lotNo, final String exhbYm, final String splrCd) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Object[] params = { login.getOrganizationCodeUp3(), companyCd, lotNo, exhbYm, splrCd, login.getOrganizationCodeUp3() };
		return params;
	}

}
