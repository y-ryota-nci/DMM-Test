package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.io.IOUtils;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.custom.WfLatestHistory;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.FileStreamingOutput;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.WfLatestHistoryInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.TrayEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0000Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0001Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0005Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0006Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0008Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0009Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0011Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Service;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 申請・承認画面サービス
 */
@BizLogic
public class Vd0310Service extends BaseService implements CodeMaster {

	/** ボタンブロックサービス */
	@Inject protected Bl0000Service bl0000Service;
	/** 申請情報サービス */
	@Inject protected Bl0001Service bl0001Service;
	/** 文書内容サービス */
	@Inject protected Bl0002Service bl0002Service;
	/** 添付ファイル情報サービス */
	@Inject protected Bl0003Service bl0003Service;
	/** 関連文書サービス */
	@Inject protected Bl0005Service bl0005Service;
	/** 履歴情報サービス */
	@Inject protected Bl0006Service bl0006Service;
	/** 承認ルート情報サービス */
	@Inject protected Bl0007Service bl0007Service;
	/** デフォルト閲覧者（デフォルト参照者情報）サービス */
	@Inject protected Bl0008Service bl0008Service;
	/** 参照者情報サービス */
	@Inject protected Bl0009Service bl0009Service;
	/** 要説明(掲示板)情報サービス */
	@Inject protected Bl0010Service bl0010Service;
	/** メモ情報サービス */
	@Inject protected Bl0011Service bl0011Service;
	/** 文書ファイル情報サービス */
	@Inject protected Bl0015Service bl0015Service;
	/** ダウンロード通知サービス */
	@Inject private DownloadNotifyService notify;
	/** 申請文書のファイルサイズに関するバリデーター */
	@Inject private Vd0310FileSizeValidator fileSizeValidator;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/** Vd0310Repository */
	@Inject
	protected Vd0310Repository vd0310Repository;
	/** パーツレンダラーFactory */
	@Inject
	protected PartsRenderFactory render;
	/** (画面デザイナー)ユーザデータサービス */
	@Inject
	private UserDataService userDataService;
	/** ルート設定サービス */
	@Inject
	private RouteSettingService routeSettingService;
	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0310InitResponse init(Vd0310InitRequest req) {

		// 起動パラメータのバリデーション
		validateInitParams(req);

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		final Set<String> menuRoleCds = login.getMenuRoleCodes();
		final Vd0310InitResponse res = createResponse(Vd0310InitResponse.class, req);
		res.contents = new Vd0310Contents();

		// トレイ情報を取得
		final TrayEntity trayEntity = vd0310Repository.getTrayEntity(
				req.corporationCode,
				req.processId,
				req.activityId,
				req.screenProcessId,
				menuRoleCds,
				localeCode);
		validateTrayEntity(req, trayEntity, menuRoleCds);
		copyFields(trayEntity, res.contents);
		res.contents.trayType = req.trayType;
		res.contents.proxyUser = req.proxyUser;
		// アクティビティ定義を取得
		res.contents.activityDef = routeSettingService.getActivityDef(trayEntity);

		// 最新の承認履歴を取得する（＝差戻しすると履歴には同一アクティビティが複数出るが、この重複を除去して最新アクティビティだけで構築した履歴情報）
		if (isNotEmpty(res.contents.processId)) {
			// API呼出
			List<WfLatestHistory> latestHistory = bl0000Service.getLatestActivityList(req);
			res.contents.latestHistoryList = latestHistory.stream().map(l -> new WfLatestHistoryInfo(l)).collect(Collectors.toList());
		}

		// トレイタイプ
		res.contents.trayType = req.trayType;
		// 入力者情報を取得
		res.contents.processUserInfo = bl0001Service.getProcessUserInfo(trayEntity);
		// 起案担当者情報を取得
		res.contents.startUserInfo = bl0001Service.getStartUserInfo(trayEntity);
		// プロセス定義のコメント表示フラグ
		res.contents.processCommentDisplayFlag = trayEntity.commentDisplayFlag;
		// 承認者設定画面を使用するか
		res.contents.useApproversSettingScreen = corpProp.getBool(CorporationProperty.USE_APPROVERS_SETTING_SCREEN, true);
		// 旧消費税率(8%)を表示する基準日
		res.contents.oldTaxRateDisplayReferenceDate = corpProp.getString(CorporationProperty.OLD_TAX_RATE_DISPLAY_REFERENCE_DATE);

		// ブロックの初期化
		initBlocks(req, res);

		// 排他
		if (trayEntity.timestampUpdated != null && !eq(trayEntity.timestampUpdated.getTime(), req.timestampUpdated)) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	/** トレイ情報の整合性チェック */
	private void validateTrayEntity(Vd0310InitRequest req, TrayEntity trayEntity, Set<String> menuRoleCds) {
		if (trayEntity == null)
			throw new NotFoundException("トレイ情報が見つかりません ->"
					+ " screenProcessId=" + req.screenProcessId+ " trayType=" + req.trayType
					+ " processId=" + req.processId
					+ " corporationCode=" + req.corporationCode + " activityId=" + req.activityId);
		if (trayEntity.screenProcessId == null)
			throw new NotFoundException("トレイ情報の画面プロセスIDが見つかりません ->"
					+ " trayType=" + req.trayType + " processId=" + req.processId
					+ " corporationCode=" + req.corporationCode + " activityId=" + req.activityId
					+ " screenProcessId=" + req.screenProcessId + " menuRoleCodes=" + Objects.toString(menuRoleCds));
	}

	/** 起動パラメータのバリデーション */
	private void validateInitParams(Vd0310InitRequest req) {
		if (req.trayType == null) {
			throw new BadRequestException("トレイタイプが未指定です");
		}
		else if (req.trayType == TrayType.NEW) {
			// 新規
			if (req.screenProcessId == null)
				throw new BadRequestException("画面プロセスIDが未指定です");
		}
		else {
			// 新規以外
			if (isEmpty(req.corporationCode))
				throw new BadRequestException("企業コードが未指定です");
			if (req.processId == null)
				throw new BadRequestException("プロセスIDが未指定です");
			if (req.activityId == null)
				throw new BadRequestException("アクティビティIDが未指定です");
			if (req.timestampUpdated == null)
				throw new BadRequestException("更新日時が未指定です");

			// 権限チェック
			this.validateWFAuthority(req);
		}
	}

	/** 操作者によるプロセスへの参照権チェック */
	private void validateWFAuthority(Vd0310InitRequest req) {
		// 操作者によるプロセスへの参照権チェック
		if (req.trayType == TrayType.WORKLIST || req.trayType == TrayType.BATCH) {
			GetActivityListInParam in = new GetActivityListInParam();
			in.setProcessId(req.processId);
			WfSearchCondition<Long> cond = new WfSearchConditionImpl<>(WfvTray.ACTIVITY_ID, SearchConditionType.EQUAL, req.activityId);
			List<WfSearchCondition<?>> condList = new ArrayList<>();
			condList.add(cond);
			in.setSearchConditionList(condList);
			in.setMode(GetActivityListInParam.Mode.USER_TRAY);
			in.setSelectMode(GetActivityListInParam.SelectMode.COUNT);
			in.setWfUserRole(bl0000Service.cloneUserRole(sessionHolder.getWfUserRole(), req.proxyUser));
			if (wf.getActivityList(in).getAllCount().signum() == 0)
				throw new ForbiddenException("対象データが存在しないもしくは権限がありません");

		} else if (req.trayType == TrayType.OWN) {
			GetActivityListInParam in = new GetActivityListInParam();
			in.setProcessId(req.processId);
			WfSearchCondition<Long> cond = new WfSearchConditionImpl<>(WfvTray.ACTIVITY_ID, SearchConditionType.EQUAL, req.activityId);
			List<WfSearchCondition<?>> condList = new ArrayList<>();
			condList.add(cond);
			in.setSearchConditionList(condList);
			in.setMode(GetActivityListInParam.Mode.OWN_PROCESS_TRAY);
			in.setSelectMode(GetActivityListInParam.SelectMode.COUNT);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			if (wf.getActivityList(in).getAllCount().signum() == 0)
				throw new ForbiddenException("対象データが存在しないもしくは権限がありません");

		} else if (req.trayType == TrayType.ALL) {
			GetProcessHistoryListInParam in = new GetProcessHistoryListInParam();
			in.setProcessId(req.processId);
			in.setExecuting(true);
			in.setMode(GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORY);
			in.setSelectMode(GetActivityListInParam.SelectMode.COUNT);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			if (wf.getProcessHistoryList(in).getAllCount().signum() == 0)
				throw new ForbiddenException("対象データが存在しないもしくは権限がありません");

		} else if (req.trayType == TrayType.FORCE) {
			if (!sessionHolder.getLoginInfo().isCorpAdmin()) {
				throw new ForbiddenException("権限がありません");
			}
		} else {
			throw new BadRequestException("トレイタイプが不正です");
		}
	}

	/** ブロック情報の初期化 */
	private void initBlocks(final Vd0310InitRequest req, final Vd0310InitResponse res) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		final RuntimeContext ctx = RuntimeContext.newInstance(res.contents, req.viewWidth, null);

		// 画面カスタマイズにはとりあえずNULLインスタンスを設定しておく
		IScreenCustom screenCustom = IScreenCustom.get(null);

		// ブロックの表示非表示情報を取得
		List<BlockDisplayEntity> blockDisplayList = vd0310Repository.getBlockDisplayList(res.contents.corporationCode, res.contents.dcId, res.contents.screenProcessId, localeCode);
		if (blockDisplayList.isEmpty()) {
			throw new NotFoundException(
					"ブロック表示順情報が見つかりません ->"
					+ " screenProcessId=" + res.contents.screenProcessId
					+ " processId=" + res.contents.processId
					+ " corporationCode=" + res.contents.corporationCode);
		}
		res.contents.blockList.clear();
		boolean initButtonBlock = false;
		for (BlockDisplayEntity e : blockDisplayList) {
			// 承認履歴は新規申請から来た場合表示しないようにする
			if (BlockIds.HISTORY == e.blockId
					&& isEmpty(res.contents.processId)) {
				e.displayFlag = DisplayFlag.OFF;
			}
			res.contents.blockList.add(e);

			if (DisplayFlag.OFF.equals(e.displayFlag)) {
				continue;
			}


			switch (e.blockId) {
			case BlockIds.BUTTON1:
			case BlockIds.BUTTON2:
				// アクション情報ブロックは２つあるが、初期化は一回で良い
				if (!initButtonBlock) {
					bl0000Service.init(req, res);
					initButtonBlock = true;
				}
				break;

			case BlockIds.APPLICANT:
				break;

			case BlockIds.CONTENTS:
				// 画面プロセスIDをキーにパーツ定義へユーザデータを読み込んで、実行時インスタンスを生成
				if (req.copyProcessId == null)
					userDataService.loadScreenAndUserData(ctx);
				else
					userDataService.copyScreenAndUserData(ctx, req.copyProcessId);

				// 画面カスタムクラス：未定義ならNULLインスタンスとして初期化されます
				// 必要に応じて、画面カスタムクラスにてデザイン定義を変更
				screenCustom = IScreenCustom.get(ctx.screenCustomClass);
				screenCustom.afterInitLoad(req, res, ctx);

				// スタンプパーツが存在する場合履歴情報を取得する
				if (isNotEmpty(res.contents.latestHistoryList) && existsStampParts(ctx.runtimeMap)) {
					ctx.stampMap.clear();
					res.contents.latestHistoryList
							.stream()
							.filter(l -> isNotEmpty(l.stampCode) && eq(AssignedStatus.END, l.assignedStatus))
							.collect(Collectors.groupingBy(l -> l.stampCode, Collectors.toList()))
							.forEach((k, v) -> {
								ctx.stampMap.put(k, new StampInfo(v.get(0)));
							});
					res.contents.stampMap = ctx.stampMap;
				}
				screenCustom.beforeInitRender(req, res, ctx);

				// パーツのHTML
				res.html = render.renderAll(ctx);

				// 画面デザイナのキー情報を転写
				res.contents.runtimeMap = ctx.runtimeMap;
				res.contents.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
				res.contents.submitFunctions = ctx.submitFunctions;
				res.contents.loadFunctions = ctx.loadFunctions;
				res.contents.changeStartUserFunctions = ctx.changeStartUserFunctions;
				res.contents.javascriptIds = ctx.javascriptIds;
				res.contents.screenCustomClass = ctx.screenCustomClass;

				screenCustom.afterInitRender(req, res, ctx);
				break;

			case BlockIds.ATTACH_FILE:
				if (CommonFlag.ON.equals(e.expansionFlag)) {
					res.contents.attachFileWfList = bl0003Service.getAttachFileWfList(
							res.contents.corporationCode, res.contents.processId);
				}
				break;
			case BlockIds.APPROVAL_RELATION:
				if (CommonFlag.ON.equals(e.expansionFlag)) {
					res.contents.approvalRelationList = bl0005Service.getApprovalRelationList(
							res.contents.corporationCode, res.contents.processId);
				}
				break;
			case BlockIds.HISTORY:
				if (CommonFlag.ON.equals(e.expansionFlag)) {
					res.contents.historyList = bl0006Service.getHistoryList(
							res.contents.corporationCode, res.contents.processId);
				}
				break;
			case BlockIds.APPROVER:
				Bl0007Request bl0007Req = new Bl0007Request();
				bl0007Req.corporationCode = res.contents.corporationCode;
				bl0007Req.processId = res.contents.processId;
				bl0007Req.processDefCode = res.contents.processDefCode;
				bl0007Req.processDefDetailCode = res.contents.processDefDetailCode;
				bl0007Req.activityDefCode = res.contents.activityDefCode;
				bl0007Req.processUserInfo = res.contents.processUserInfo;
				bl0007Req.startUserInfo = res.contents.startUserInfo;
				Bl0007Response bl0007Res = bl0007Service.init(bl0007Req);
				res.contents.routeList = bl0007Res.routeList;
				break;
			case BlockIds.DEFAULT_REFERER:
				if (CommonFlag.ON.equals(e.expansionFlag)) {
					res.contents.informationSharerDefList = bl0008Service.getInformationSharerDefList(
							res.contents.corporationCode,
							res.contents.processDefCode,
							res.contents.processDefDetailCode,
							res.contents.processId
					);
				}
				break;
			case BlockIds.REFERER:
				if (CommonFlag.ON.equals(e.expansionFlag) && isNotEmpty(res.contents.processId)) {
					res.contents.informationSharerList = bl0009Service.getInformationSharerList(
							res.contents.corporationCode, res.contents.processId);
				}
				break;
			case BlockIds.BBS:
				// 要説明ブロックはWFの申請／保存とは無関係にデータの登録が行われる。
				// このため最新データの取得の仕方がblock.js側のブロックの開閉をトリガーに取得するので、ここでは不要
				break;
			case BlockIds.MEMO:
				// メモブロックはWFの申請／保存とは無関係にデータの登録が行われる。
				// このため最新データの取得の仕方がblock.js側のブロックの開閉をトリガーに取得するので、ここでは不要
				break;
			case BlockIds.DOC_MANAGEMENT:
				break;
			case BlockIds.DOC_FILE:
				if (CommonFlag.ON.equals(e.expansionFlag)) {
					res.contents.docFileWfList = bl0015Service.getDocFileWfList(
							res.contents.corporationCode, res.contents.processId);
				}
				break;
			default:
				break;
			}
		}
		screenCustom.beforeInitResponse(req, res, ctx);
	}

	private boolean existsStampParts(Map<String, PartsBase<?>> runtimeMap) {
		for (PartsBase<?> p : runtimeMap.values()) {
			if (PartsType.STAMP == p.partsType) return true;
		}
		return false;
	}

	/**
	 * コピー起票可能か.
	 * この関数が呼ばれたということはすでにAuthenticateEndpointInterceptorを経由し、チェック処理済みとなっているため
	 * ここでは単にレスポンスだけを返している
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public BaseResponse canCopy(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * アクション実行
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public BaseResponse execute(Vd0310ExecuteRequest req) {
		final Vd0310ExecuteResponse res = createResponse(Vd0310ExecuteResponse.class, req);

		// API実行前処理（バリデーション等）
		if (beforeExecute(req, res)) {
			// API実行
			final Map<String, Object> handOverParam = execute(req, res);

			// API実行後処理
			afterExecute(req, res, handOverParam);

			// 仮保存ならダミー初期化リクエストを生成してリロード用レスポンスを生成
			if (ActionType.SAVE.equals(req.actionInfo.actionType)) {
				final Vd0310InitRequest dummy = new Vd0310InitRequest();
				dummy.corporationCode = defaults(res.corporationCode, req.contents.corporationCode);
				dummy.processId = defaults(res.processId, req.contents.processId);
				dummy.activityId = defaults(res.activityId, req.contents.activityId);
				dummy.screenProcessId = req.contents.screenProcessId;
				dummy.trayType = req.contents.trayType;
				dummy.viewWidth = req.viewWidth;
				dummy.proxyUser = req.contents.proxyUser;
				if (req.contents.timestampUpdated != null) {
					dummy.timestampUpdated = defaults(res.timestampUpdated, req.contents.timestampUpdated.getTime());
				}
				final Vd0310InitResponse res2 = init(dummy);
				res2.addSuccesses(i18n.getText(MessageCd.MSG0201));
				return res2;
			}
		}
		// バリデーションでエラーありでもレスポンスとしては正常扱いする。
		// 代わりにres.errorsの有無でエラー判定をvd0310.js側で行うはず
		res.success = true;
		return res;
	}

	/**
	 * API実行前処理（バリデーション等）
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return バリデーションエラーなしならtrue
	 */
	private boolean beforeExecute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);

		// パーツのバリデーション
		screenCustom.beforeValidate(req, res);
		Map<String, EvaluateCondition> ecResults = null;
		if (req.runtimeMap != null && !req.runtimeMap.isEmpty()) {
			// パーツ定義の読み込み
			res.ctx = bl0002Service.load(req);

			// 現在のパーツ値による条件判定結果Map
			ecResults = partsCondUtils.createEcResults(res.ctx);


			// バリデーションとその結果の反映（バリデーション結果による親コンテナのページ番号の変更処理）
			res.errors = bl0002Service.validate(res.ctx, ecResults);
			if (!res.errors.isEmpty()) {
				res.html = render.renderAll(res.ctx, ecResults);
				res.runtimeMap = res.ctx.runtimeMap;
			}
		}
		// この申請内容に添付されているファイルや画像のファイルサイズの合計が
		// システムプロパティで決められた上限を超過していないか
		final Vd0310FileSizeValidatorResult r = fileSizeValidator.validate(req, res.ctx);
		if (!r.isOK()) {
			// 申請文書に添付しているファイルの合計サイズが上限を超過しました。添付／画像ファイルを減らしてください。（合計=MB、上限=MB、内訳：パーツのファイルサイズ=MB、添付ファイルブロックのファイルサイズ=MB、文書ファイルのファイルサイズ=MB）
			throw new InvalidUserInputException(r.toErrorMessage());
		}
		screenCustom.afterValidate(req, res);

		if (res.errors == null || res.errors.isEmpty()) {
			// アクション機能のみ実行するなら、以降の更新処理は不要である
			if (eq(ActionType.DOACTION, req.actionInfo.actionType)) {
				return true;
			}

			// プロセスインスタンスが作成されていない場合
			if (isEmpty(req.contents.processId)) {
				bl0000Service.create(req, res);
				req.contents.processId = res.processId;
				req.contents.activityId = res.activityId;
				if (res.ctx != null) {
					res.ctx.processId = res.processId;
				}
			}
			else {
				res.ctx.processId = req.contents.processId;
				res.processId = req.contents.processId;
			}

			// 添付ファイル情報更新
			bl0003Service.execute(req, res);

			// 決裁関連文書更新
			bl0005Service.execute(req, res);

			// 参照者情報更新
			bl0009Service.execute(req, res);

			// ワークフロー文書ファイル情報更新
			bl0015Service.execute(req, res);

			// 画面パーツの更新処理と、パーツに紐付く比較条件変数(業務管理項目)をMap形式で抜き出し
			screenCustom.beforeUpdateUserData(req, res);
			if (req.runtimeMap != null && res.ctx != null) {
				// 更新し、その結果として業務管理項目Mapを取得
				res.bizInfos = bl0002Service.update(req, res, ecResults);
			}
			screenCustom.afterUpdateUserData(req, res);

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
	 * @return WF APIの引き継ぎパラメータMap
	 */
	private Map<String, Object> execute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		try {
			// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
			final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);
			screenCustom.beforeUpdateWF(req, res);

			// 変数情報設定処理
			bl0000Service.setVariable(req, res);

			// 付加情報設定処理
			bl0000Service.setAdditionalInfo(req, res);

			// 承認ルート更新
			if (req.changeRouteList != null && req.changeRouteList.size() > 0) {
				bl0007Service.update(req, res);
			}

			// コールバックファンクション用引き継ぎパラメータ
			final Map<String, Object> handOverParam = createHandOverParam(req, res);

			// 承認ルートの遷移
			switch (req.actionInfo.actionType) {
			case ActionType.NORMAL:
				bl0000Service.move(req, res, handOverParam);
				break;
			case ActionType.PULLBACK:
				bl0000Service.pullback(req, res, handOverParam);
				break;
			case ActionType.PULLFORWARD:
				bl0000Service.pullforward(req, res, handOverParam);
				break;
			case ActionType.SENDBACK:
			case ActionType.SENDBACK_NC:
				bl0000Service.sendback(req, res, handOverParam);
				break;
			case ActionType.CANCEL:
				bl0000Service.cancel(req, res, handOverParam);
				break;
			case ActionType.SAVE:
			case ActionType.DOACTION:
			default:
				bl0000Service.doaction(req, res, handOverParam);
				break;
			}

			// WF API呼び出し後
			InParamCallbackBase in = (InParamCallbackBase)handOverParam.get(InParamCallbackBase.class.getSimpleName());
			OutParamCallbackBase out = (OutParamCallbackBase)handOverParam.get(OutParamCallbackBase.class.getSimpleName());
			screenCustom.afterUpdateWF(req, res, in, out);

			return handOverParam;
		} catch (WfException wex) {
			if (ReturnCode.EXCLUSION.equals(wex.getReturnCode())) {
				throw new AlreadyUpdatedException(wex.getMessage());
			}
		}
		return null;
	}

	/** コールバックファンクション用の引き継ぎパラメータを生成 */
	private Map<String, Object> createHandOverParam(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		final Map<String, Object> handOverParam = new HashMap<>();
		handOverParam.put(Vd0310Contents.class.getSimpleName(), req.contents);
		handOverParam.put(ActionType.class.getSimpleName(), req.actionInfo.actionType);
		handOverParam.put(RuntimeContext.class.getSimpleName(), res.ctx);
		return handOverParam;
	}

	/**
	 * API実行後処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param handOverParam WF APIの引き継ぎパラメータMap
	 */
	private void afterExecute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		InParamCallbackBase in = (InParamCallbackBase)handOverParam.get(InParamCallbackBase.class.getSimpleName());
		OutParamCallbackBase out = (OutParamCallbackBase)handOverParam.get(OutParamCallbackBase.class.getSimpleName());

		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);
		screenCustom.beforeResponse(req, res, in, out);
	}

	/**
	 * （帳票など）コンテンツのダウンロード
	 * @param req
	 * @return
	 */
	public FileStreamingOutput doDownload(Vd0310ExecuteRequest req) {
		try {
			// 処理開始の通知
			notify.begin();
			final Vd0310ExecuteResponse dummy = createResponse(Vd0310ExecuteResponse.class, req);

			// API実行前処理（バリデーション等）
			if (beforeExecute(req, dummy)) {
				final Map<String, Object> handOverParam = createHandOverParam(req, dummy);
				handOverParam.put(Vd0310ExecuteRequest.class.getSimpleName(), req);
				handOverParam.put(Vd0310ExecuteResponse.class.getSimpleName(), dummy);

				// StreamingOutputはJerseyによって遅延実行されるが、それだとファイル名が取得できない。
				// （ファイル名を知っているのはコールバックファンクションで、それが実行されるのはAPI呼出し後だ）
				// よって、いったんTEMPファイルへコンテンツを書き出す
				final File temp = File.createTempFile("doDownload-", ".tmp");
				if (!temp.exists())
					throw new InternalServerErrorException("TEMPファイルを作成できませんでした。");

				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp))) {
					handOverParam.put(OutputStream.class.getSimpleName(), bos);

					// アクション機能実行APIでコールバックファンクション起動～カスタマイズクラスを呼び出す
					// カスタマイズクラス側はTEMPファイルへコンテンツをファイルに書き出し
					bl0000Service.doaction(req, dummy, handOverParam);

					// 画面カスタムクラス側で設定したファイル名
					String fileName = (String)handOverParam.get("fileName");

					// 今度はStreamingOutputとして遅延実行しつつ、コンテンツが書き込まれたTEMPファイルを最終的なoutputへ転写
					return new FileStreamingOutput(fileName) {
						@Override
						public void write(OutputStream output) throws IOException, WebApplicationException {
							try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(temp))){
								IOUtils.copy(bis, output);
							}
							finally {
								if (temp.exists()) temp.delete();
							}
						}
					};
				}
			}
			else {
				PartsValidationResult error = dummy.errors.get(0);
				throw new InvalidUserInputException(error.message + " " + error.htmlId);
			}
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
		finally {
			// 処理終了の通知
			notify.end();
		}
	}
}
