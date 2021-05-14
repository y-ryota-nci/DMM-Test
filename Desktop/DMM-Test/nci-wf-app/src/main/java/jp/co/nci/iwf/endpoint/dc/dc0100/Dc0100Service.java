package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.api.param.input.GetLatestActivityListInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfLatestHistory;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocInfoOperationRequest;
import jp.co.nci.iwf.component.document.DocInfoService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocVersionInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0000Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0001Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0002Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0003Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0004Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0005Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0006Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0007Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0008Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0009Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0010Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0011Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.WfLatestHistoryInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;

/**
 * 業務文書の登録・更新画面サービス.
 */
@BizLogic
public class Dc0100Service extends BaseService implements CodeMaster, DcCodeBook {

	/** ボタンブロックサービス */
	@Inject protected DcBl0000Service bl0000Service;
	/** 業務文書ブロックサービス */
	@Inject protected DcBl0001Service bl0001Service;
	/** 文書内容ブロックサービス */
	@Inject protected DcBl0002Service bl0002Service;
	/** 文書属性ブロックサービス */
	@Inject protected DcBl0003Service bl0003Service;
	/** 権限設定ブロックサービス */
	@Inject protected DcBl0004Service bl0004Service;
	/** 文書属性(拡張)ブロックサービス */
	@Inject protected DcBl0005Service bl0005Service;
	/** 文書ファイルブロックサービス */
	@Inject protected DcBl0006Service bl0006Service;
	/** 更新履歴ブロックサービス */
	@Inject protected DcBl0007Service bl0007Service;
	/** メモ情報ブロックサービス */
	@Inject protected DcBl0008Service bl0008Service;
	/** バインダーブロックサービス */
	@Inject protected DcBl0009Service bl0009Service;
	/** 添付ファイルブロックサービス */
	@Inject protected DcBl0010Service bl0010Service;
	/** WF連携ブロックサービス */
	@Inject protected DcBl0011Service bl0011Service;
	/** WF API */
	@Inject protected WfInstanceWrapper wf;

	/** Dc0100Repository */
	@Inject protected Dc0100Repository repository;

	/** 文書管理サービス */
	@Inject private DocInfoService docService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0100InitResponse init(Dc0100InitRequest req) {

		// 起動パラメータのバリデーション
		validateInitParams(req);

		final LoginInfo login = sessionHolder.getLoginInfo();

		// 戻り値生成
		final Dc0100InitResponse res = createResponse(Dc0100InitResponse.class, req);
		res.contents = new Dc0100Contents();
		// 文書情報を取得
		final DocInfoEntity docInfoEntity = (req.docId != null) ?
				docService.getDocInfoEntity(req.docId, req.corporationCode, req.fromCoopWfFlag) : docService.createNewDocInfoEntity(req.copyDocId, req.corporationCode, req.screenDocId);
		validateDocInfoEntity(req, docInfoEntity, login);
		copyFields(docInfoEntity, res.contents);
		// 文書情報
		final DocInfo docInfo = new DocInfo(docInfoEntity);
		res.contents.docInfo = docInfo;
		res.contents.oldTaxRateDisplayReferenceDate = corpProp.getString(CorporationProperty.OLD_TAX_RATE_DISPLAY_REFERENCE_DATE);	 // 旧消費税率(8%)を表示する基準日

		// トレイタイプ
		// とりあえず初期値は参照モードで設定
		// 画面デザイナー側との兼ね合いより設定値は「TrayType.ALL」
		// 表示条件IDは「102:文書管理(参照時)」に設定
		// なおWF申請中の場合、「TrayType.ALL」で固定
		res.contents.trayType = TrayType.ALL;
		res.contents.dcId = DocDcId.REFERRING;
		if (!eq(CommonFlag.ON, docInfoEntity.wfApplying)) {
			// 文書IDがなければ新規登録
			if (docInfoEntity.docId == null) {
				res.contents.trayType = TrayType.NEW;
				res.contents.dcId = DocDcId.EDITING;
			} else if (eq(CommonFlag.ON, docInfoEntity.ownLockFlag)) {
				res.contents.trayType = TrayType.WORKLIST;
				res.contents.dcId = DocDcId.EDITING;
			}
		}

		// 最新の承認履歴を取得する（＝差戻しすると履歴には同一アクティビティが複数出るが、この重複を除去して最新アクティビティだけで構築した履歴情報）
		if (isNotEmpty(docInfo.processId)) {
			// API呼出
			List<WfLatestHistory> latestHistory = this.getLatestActivityList(docInfo.corporationCode, docInfo.processId);
			res.contents.latestHistoryList = latestHistory.stream().map(l -> new WfLatestHistoryInfo(l)).collect(Collectors.toList());
		}

		// ブロックの初期化
		initBlocks(req, res);

		res.success = true;
		return res;
	}

	/** 文書情報の整合性チェック */
	private void validateDocInfoEntity(Dc0100InitRequest req, DocInfoEntity docInfoEntity, LoginInfo login) {
		if (docInfoEntity == null)
			throw new ForbiddenException("文書情報が見つかりません ->"
					+ " corporationCode=" + req.corporationCode
					+ " docId=" + req.docId
					+ " screenDocId=" + req.screenDocId);
		// 一般ユーザ（＝管理者、公開者、文書責任者以外）は公開フラグが「0:非公開」、または公開期間外であれば閲覧不可
		if (docInfoEntity.docId != null
				&& !login.isCorpAdmin()
				&& (!eq(login.getCorporationCode(), docInfoEntity.publishCorporationCode) || !eq(login.getUserCode(), docInfoEntity.publishUserCode))
				&& (!eq(login.getCorporationCode(), docInfoEntity.ownerCorporationCode) || !eq(login.getUserCode(), docInfoEntity.ownerUserCode))) {
			if (eq(CommonFlag.OFF, docInfoEntity.publishFlag))
				throw new ForbiddenException("この文書は非公開のため閲覧できません ->  docId=" + docInfoEntity.docId);
			if (!between(today(), docInfoEntity.publishStartDate, docInfoEntity.publishEndDate))
				throw new ForbiddenException("この文書は公開期間が過ぎているため閲覧できません ->  docId=" + docInfoEntity.docId);
		}
	}

	/** 起動パラメータのバリデーション */
	private void validateInitParams(Dc0100InitRequest req) {
//		if (req.docId == null && req.screenDocId == null) {
//			throw new BadRequestException("文書ID、画面文書IDが未指定です");
//		}
	}

	/** ブロック情報の初期化 */
	private void initBlocks(final Dc0100InitRequest req, final Dc0100InitResponse res) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		RuntimeContext ctx = null;

		// 画面カスタマイズにはとりあえずNULLインスタンスを設定しておく
		IScreenCustom screenCustom = IScreenCustom.get(null);

		// ブロックの表示非表示情報を取得
		// コンテンツ種別によって取得するブロックを切り替える
		final LookupGroupId lookupGroupId = eq(ContentsType.BIZ_DOC, res.contents.contentsType) ? LookupGroupId.BIZDOC_BLOCK_ID : LookupGroupId.BINDER_BLOCK_ID;
		final List<BlockDisplayEntity> blockDisplayList = repository.getBlockDisplayList(res.contents.corporationCode, lookupGroupId, localeCode);
//		final List<BlockDisplayEntity> blockDisplayList;
//		if (eq(ContentsType.BIZ_DOC, res.contents.contentsType)) {
//			blockDisplayList = repository.getBizdocBlockDisplayList(res.contents.corporationCode, res.contents.dcId, res.contents.screenDocId, localeCode);
//		} else {
//			blockDisplayList = repository.getBinderBlockDisplayList(res.contents.corporationCode, localeCode);
//		}
		if (blockDisplayList.isEmpty()) {
			throw new NotFoundException(
					"ブロック表示順情報が見つかりません ->"
					+ " lookupGroupId=" + LookupGroupId.BIZDOC_BLOCK_ID.toString()
					+ " corporationCode=" + res.contents.corporationCode);
		}
		res.contents.blockList.clear();
		for (BlockDisplayEntity e : blockDisplayList) {
			// 更新履歴は新規の場合表示しないようにする
			// WF連携履歴は新規の場合表示しないようにする
			if ((DocBlockIds.HISTORY == e.blockId || DocBlockIds.WF_RELATION == e.blockId) && isEmpty(res.contents.docId)) {
				e.displayFlag = DisplayFlag.OFF;
			}
			res.contents.blockList.add(e);

			if (DisplayFlag.OFF.equals(e.displayFlag)) {
				continue;
			}
			switch (e.blockId) {
				case DocBlockIds.BUTTON:
					bl0000Service.init(req, res);
					break;

				case DocBlockIds.BIZDOC:
					bl0001Service.init(req, res);
					break;

				case DocBlockIds.CONTENTS:
					ctx = bl0002Service.init(req, res);
					break;

				case DocBlockIds.ATTRIBUTE:
					bl0003Service.init(req, res);
					break;

				case DocBlockIds.AUTHORITY:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0004Service.init(req, res);
					}
					break;
				case DocBlockIds.ATTRIBUTE_EX:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0005Service.init(req, res);
					}
					break;
				case DocBlockIds.DOC_FILE:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0006Service.init(req, res);
					}
					break;
				case DocBlockIds.HISTORY:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0007Service.init(req, res);
					}
					break;
				case DocBlockIds.MEMO:
					// メモブロックはWFの申請／保存とは無関係にデータの登録が行われる。
					// このため最新データの取得の仕方がblock.js側のブロックの開閉をトリガーに取得するので、ここでは不要
					break;
				case DocBlockIds.BINDER:
					bl0009Service.init(req, res);
					break;
				case DocBlockIds.ATTACH_FILE:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0010Service.init(req, res);
					}
					break;
				case DocBlockIds.WF_RELATION:
					if (CommonFlag.ON.equals(e.expansionFlag)) {
						bl0011Service.init(req, res);
					}
					break;
				default:
					break;
			}
		}
		screenCustom.beforeInitResponse(req, res, ctx);
	}

	private Dc0100InitRequest createDc0100InitRequest(String corporationCode, Long docId, Long screenDocId, ViewWidth viewWidth, String fromCoopWfFlag) {
		final Dc0100InitRequest req = new Dc0100InitRequest();
		req.corporationCode = corporationCode;
		req.docId = docId;
		req.screenDocId = screenDocId;
		req.viewWidth = viewWidth;
		req.fromCoopWfFlag = fromCoopWfFlag;
		return req;
	}

	/**
	 * ロック処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse lock(DocInfoOperationRequest req) {
		final BaseResponse res = bl0000Service.lock(req);
		if (res.success) {
			// ダミー初期化リクエストを生成してリロード用レスポンスを生成
			final Dc0100InitRequest dummy = this.createDc0100InitRequest(req.corporationCode, req.docId, null, req.viewWidth, null);
			final Dc0100InitResponse res2 = init(dummy);
			res2.addSuccesses(i18n.getText(MessageCd.MSG0226, res2.contents.docInfo.title));
			return res2;
		}
		return res;
	}

	/**
	 * ロック解除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse unlock(DocInfoOperationRequest req) {
		final BaseResponse res = bl0000Service.unlock(req);
		if (res.success) {
			// ダミー初期化リクエストを生成してリロード用レスポンスを生成
			final Dc0100InitRequest dummy = this.createDc0100InitRequest(req.corporationCode, req.docId, null, req.viewWidth, null);
			final Dc0100InitResponse res2 = init(dummy);
			res2.addSuccesses(i18n.getText(MessageCd.MSG0227, res2.contents.docInfo.title));
			return res2;
		}
		return res;
	}

	/**
	 * アクション実行
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public BaseResponse execute(Dc0100ExecuteRequest req) {
		final Dc0100ExecuteResponse res = createResponse(Dc0100ExecuteResponse.class, req);

		// 新規登録か
		final boolean isNew = (req.contents.docId == null);
		// WF連携を行うか
		final boolean isWfApply = eq(CommonFlag.ON, req.wfApplying);

		// 実行前処理（バリデーション等）
		if (beforeExecute(req, res)) {
			// 実行
			execute(req, res);

			// 実行後処理
			afterExecute(req, res);

			// ダミー初期化リクエストを生成してリロード用レスポンスを生成
			final Dc0100InitRequest dummy = this.createDc0100InitRequest(
					defaults(res.corporationCode, req.contents.corporationCode),
					defaults(res.docId, req.contents.docId),
					req.contents.screenDocId, req.viewWidth, req.fromCoopWfFlag);
			final Dc0100InitResponse res2 = init(dummy);

			// 処理成功ならメッセージを設定
			if (isNew) {
				res2.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.docInfo));
			} else if (isWfApply) {
				res2.addSuccesses(i18n.getText(MessageCd.MSG0228, MessageCd.docInfo));
			} else {
				res2.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.docInfo));
			}
			return res2;
		}
		// バリデーションでエラーありでもレスポンスとしては正常扱いする。
		// 代わりにres.errorsの有無でエラー判定をdc0100.js側で行う
		res.success = true;
		return res;
	}

	/**
	 * API実行前処理（バリデーション等）
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return バリデーションエラーなしならtrue
	 */
	private boolean beforeExecute(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// 文書情報の排他チェック
		final MwtDocInfo docInfo = repository.getMwtDocInfo(req.contents.docId);
		if (docInfo != null && !eq(docInfo.getVersion(), req.contents.version)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
			return false;
		}
		// 問題なければパーツのバリデーション
		else {
			bl0002Service.validate(req, res);
		}

		if (res.errors == null || res.errors.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * API実行
	 * @param req リクエスト
	 * @param res レスポンス
	 */
	private void execute(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// コンテンツ種別
		final String contentsType = req.contents.contentsType;

		// バージョン情報が更新される場合、現在の文書情報・文書権限情報・業務文書情報・バインダー情報を
		// 各履歴テーブルへ移す
		Long docHistoryId = null;
		if (req.contents.docId != null && !eq(UpdateVersionType.DO_NOT_UPDATE, req.versionInfo.updateVersionType)) {
			// 履歴情報の登録
			docHistoryId = saveDocHistory(req.contents.docId);
		}
		res.docHistoryId = docHistoryId;

		// 文書情報にバージョン情報をマージ
		mergeDocInfo(req);
		// 文書情報の差分更新
		bl0000Service.save(req, res);

		// 文書権限情報の差分更新
		bl0004Service.save(req, res);

		// 文書属性(拡張)の差分更新
		bl0005Service.save(req, res);

		// 業務文書情報の差分更新
		if (eq(contentsType, ContentsType.BIZ_DOC)) {
			bl0002Service.save(req, res);
		}
		// バインダー情報の差分更新
		else if (eq(contentsType, ContentsType.BINDER)) {
			bl0009Service.save(req, res);
		}

		// 文書管理項目の差分更新
		bl0000Service.saveDocBizInfo(req, res);

		// 文書ファイル情報の差分更新
		bl0006Service.save(req, res);

		// 添付ファイル情報の差分更新
		bl0010Service.save(req, res);

		// 文書コンテンツ情報の差分更新
		bl0000Service.saveContents(req, res);

		// WF連携を行うならDcBl0011Service#applyWfを実行
		if (eq(CommonFlag.ON, req.wfApplying)) {
			bl0011Service.applyWf(req, res);
		}

		// 文書更新履歴の登録
		bl0007Service.save(req, res);
	}

	/**
	 * 文書情報.
	 * @param req
	 * @return
	 */
	private void mergeDocInfo(Dc0100ExecuteRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final DocInfo docInfo = req.docInfo;
		docInfo.docId = req.contents.docId;
		docInfo.contentsType = req.contents.contentsType;
		// 保存期間の設定
		// 画面の年・月の入力値から月数に変換して設定する
		Integer retentionTerm = null;
		if (isNotEmpty(docInfo.retentionTermYear)) {
			retentionTerm = (docInfo.retentionTermYear * 12);
		}
		if (isNotEmpty(docInfo.retentionTermMonths)) {
			if (retentionTerm == null) {
				retentionTerm = (docInfo.retentionTermMonths);
			} else {
				retentionTerm += (docInfo.retentionTermMonths);
			}
		}
		docInfo.retentionTerm = retentionTerm;
		// 以下、バージョン情報から
		final DocVersionInfo versionInfo = req.versionInfo;
		final boolean isLock = eq(CommonFlag.OFF, versionInfo.unlockFlag);
		docInfo.lockFlag = isLock ? CommonFlag.ON : CommonFlag.OFF;
		docInfo.lockCorporationCode = isLock ? login.getCorporationCode() : null;
		docInfo.lockUserCode = isLock ? login.getUserCode() : null;
		docInfo.lockUserName = isLock ? login.getUserName() : null;
		if (eq(UpdateVersionType.MINOR_VERSION_UP, versionInfo.updateVersionType)) {
			// メジャーバージョンはそのまま
			// マイナーバージョンを1つあげる
			docInfo.majorVersion = versionInfo.majorVersion;
			docInfo.minorVersion = versionInfo.minorVersion + 1;
		} else if (eq(UpdateVersionType.MAJOR_VERSION_UP, versionInfo.updateVersionType)) {
			// メジャーバージョンを1つあげる
			// マイナーバージョンは0固定
			docInfo.majorVersion = versionInfo.majorVersion + 1;
			docInfo.minorVersion = 0;
		} else {
			docInfo.majorVersion = versionInfo.majorVersion;
			docInfo.minorVersion = versionInfo.minorVersion;
		}
		// 更新者名を設定
		docInfo.userNameUpdated = login.getUserName();
		// 初回登録時は公開ユーザ情報、作成者名を設定
		if (docInfo.docId == null) {
			docInfo.publishCorporationCode = login.getCorporationCode();
			docInfo.publishUserCode = login.getUserCode();
			docInfo.publishUserName = login.getUserName();
			docInfo.userNameCreated = login.getUserName();
		}

	}

	/**
	 * 文書情報・文書権限情報・業務文書情報・バインダー情報の履歴情報の登録.
	 * @param docId 文書ID
	 * @return 文書履歴ID
	 */
	private Long saveDocHistory(Long docId) {
		if (docId == null) {
			return null;
		}
		// まず現在の各履歴に登録されている履歴連番をインクリメント
		// こうすることで常に最新の履歴は履歴連番"0"で作成される
		repository.updateHistorySeqNo("MWT_DOC_INFO_HISTORY", docId);
		repository.updateHistorySeqNo("MWT_DOC_ACCESSIBLE_HISTORY", docId);
		repository.updateHistorySeqNo("MWT_BIZ_DOC_INFO_HISTORY", docId);
		repository.updateHistorySeqNo("MWT_BINDER_INFO_HISTORY", docId);

		// 文書情報履歴の登録
		// 登録後、文書履歴IDを取得
		Long docHistoryId = repository.insertMwtDocInfoHistory(docId);
		// 文書権限情報履歴の登録
		repository.insertMwtDocAccessibleHistory(docId, docHistoryId);
		// 業務文書情報履歴の登録
		repository.insertMwtBizDocInfoHistory(docId, docHistoryId);
		// バインダー情報履歴の登録
		repository.insertMwtBinderInfoHistory(docId, docHistoryId);

		return docHistoryId;
	}

	/**
	 * API実行後処理
	 * @param req リクエスト
	 * @param res レスポンス
	 */
	private void afterExecute(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);
		screenCustom.beforeResponse(req, res);
	}

	/** 対象プロセスの最新アクティビティ抽出 */
	private List<WfLatestHistory> getLatestActivityList(String corporationCode, Long processId) {
		GetLatestActivityListInParam in = new GetLatestActivityListInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		in.setMode(GetLatestActivityListInParam.Mode.ASSIGNED);
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole()));

		return wf.getLatestActivityList(in).getWfLatestHistoryList();
	}

	/** 代理情報なしでWF操作者情報インスタンスを生成 */
	private WfUserRole cloneUserRole(WfUserRole src) {
		return cloneUserRole(src, null);
	}

	/** 代理情報ありでWF操作者情報インスタンスを生成 */
	private WfUserRole cloneUserRole(WfUserRole src, String proxyUser) {
		final WfUserRole dest = src.clone(true);
		if (isEmpty(proxyUser)) {
			dest.setProxyUserRole(null);
			dest.setProxy(false);
		}
		else {
			final String[] astr = proxyUser.split("_");
			dest.setProxyUserRole(new WfUserRoleImpl(astr[0], astr[1]));
			dest.setProxy(true);
		}
		return dest;
	}
}
