package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.model.custom.WfProcessableActivity;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.AttachFileDocService;
import jp.co.nci.iwf.component.document.BizDocService;
import jp.co.nci.iwf.component.document.DocFileService;
import jp.co.nci.iwf.component.document.DocWfRelationService;
import jp.co.nci.iwf.component.tray.BaseTrayService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocWfRelationType;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * WF連携ブロック：WF連携のサービス
 */
@BizLogic
public class DcBl0011Service extends BaseTrayService {

	/** 文書WF連携サービス. */
	@Inject private DocWfRelationService docWfRelationService;
	/** 業務文書サービス. */
	@Inject BizDocService bizDocService;
	/** 文書ファイルサービス. */
	@Inject DocFileService docFileService;
	/** 文書管理添付ファイルサービス. */
	@Inject AttachFileDocService attachFileDocService;
	/** 申請・承認画面サービス. */
	@Inject private Vd0310Service vd0310Service;
	/** ワークフロー添付ファイルサービス. */
	@Inject private Bl0003Service bl0003Service;
	/** ワークフロー文書ファイルサービス. */
	@Inject private Bl0015Service bl0015Service;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		if (res.contents.docId != null) {
			res.contents.docWfRelations = docWfRelationService.getDocWfInfoList(res.contents.docId);
		}
	}

	/**
	 *
	 * ワークフロー連携された申請書を開く際のアクセス可能なアクティビティ(アクティビティID)を取得.
	 * 常に操作者がアクセス可能な最新のアクティビティの状態で開く
	 * @param req
	 * @return res
	 */
	public DcBl0011Response getAccessibleActivity(DcBl0011Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (req.processId == null) {
			throw new BadRequestException("プロセスIDが未指定です");
		}

		final DcBl0011Response res = createResponse(DcBl0011Response.class, req);

		// トレイタイプは「ALL」で固定
		final List<WfProcessableActivity> list = super.getAccessibleActivity(req.corporationCode, req.processId, TrayType.ALL);
		if (list == null || list.isEmpty()) {
			res.addAlerts("選択した申請書にはアクセスできません。");
		} else {
			// 操作者がアクセス可能なアクティビティの内、最新のアクティビティを返す
			// 最新のアクティビティはlistの先頭(のはず。WfInstance#getWfProcessableActivityListを見る限り。。。)
			// TODO 一応鷲田さんに確認しておこう
			res.activityId = list.get(0).getActivityId();
			res.success = true;
		}
		return res;
	}

	/**
	 * WF連携処理.
	 * @param req
	 * @param res
	 */
	public void applyWf(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// 画面文書定義マスタが持つWF連携先画面プロセス定義コードよりWF連携で使用する画面プロセス定義を取得
		final MwmScreenProcessDef screenProcessDef = docWfRelationService.getMwmScreenProcessDef(req.contents.corporationCode, req.contents.bizDocInfo.screenProcessCode);
		if (screenProcessDef == null) {
			throw new NotFoundException("連携先の画面プロセス定義が見つかりません。screenId=" + req.contents.screenId);
		}

		// 起案処理を実行するため、Vd0310Serviceのexecuteを実行する
		// そのためのダミーリクエストを生成
		final Vd0310ExecuteRequest wfReq = this.createVd0310ExecuteRequest(req, res, screenProcessDef);
		// 起案実行
		BaseResponse res2 = vd0310Service.execute(wfReq);
		// 正常終了したら文書WF連携TBLへデータを登録
		if (res2.success) {
			Vd0310InitResponse wfRes = (Vd0310InitResponse)res2;
			docWfRelationService.saveMwtDocWfRelation(wfRes.contents.corporationCode, wfRes.contents.processId, req.contents.docId, DocWfRelationType.TO_WF);
		}
	}

	/**
	 * WF連携での起案処理実行用のダミーリクエスト生成.
	 * @param req
	 * @param res
	 * @param screenProcessDef
	 * @return
	 */
	private Vd0310ExecuteRequest createVd0310ExecuteRequest(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res, MwmScreenProcessDef screenProcessDef) {
		// 最初にVd0310Serviceのinitを呼び出して処理に必要な情報を取得
		final Vd0310InitRequest initReq = new Vd0310InitRequest();
		initReq.screenProcessId = screenProcessDef.getScreenProcessId();
		initReq.trayType = TrayType.NEW;
		initReq.viewWidth = req.viewWidth;
		final Vd0310InitResponse initRes = vd0310Service.init(initReq);

		final Vd0310ExecuteRequest executeReq = new Vd0310ExecuteRequest();
		executeReq.contents = initRes.contents;
		executeReq.startUserInfo = initRes.contents.startUserInfo;
		// 実行するアクション情報を設定
		// TODO とりあえず「保存」
		final ActionInfo actionInfo = new ActionInfo();
		actionInfo.actionType = ActionType.SAVE;
		executeReq.actionInfo = actionInfo;
		// 文書内容を設定
		// 新しくWF側の文書内容として登録するのでRuntimeIdはクリアしておく
		bizDocService.clearRuntimeId(res.ctx, null);
		executeReq.runtimeMap = res.ctx.runtimeMap;
		// 決裁関連文書の設定（特になし）
		executeReq.approvalRelationList = new ArrayList<>();
		// 添付ファイル情報を設定
		final List<AttachFileDocInfo> attachFileDocs = attachFileDocService.getAttachFileDocInfoList(req.contents.docId);
		executeReq.additionAttachFileWfList = bl0003Service.copyAttachFileDoc2Wf(attachFileDocs);
		// 文書ファイル情報を設定
		final List<DocFileInfo> docFiles = docFileService.getDocFileInfoList(req.contents.docId);
		executeReq.additionDocFileWfList = bl0015Service.copyDocFileDoc2Wf(docFiles);

		return executeReq;
	}
}
