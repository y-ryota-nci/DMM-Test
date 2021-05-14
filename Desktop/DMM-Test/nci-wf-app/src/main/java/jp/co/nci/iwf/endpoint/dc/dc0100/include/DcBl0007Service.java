package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.DocUpdateLogService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocUpdateType;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.UpdateHistoryInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書・バインダー)ブロック：更新履歴のサービス
 */
@BizLogic
public class DcBl0007Service extends BaseService implements CodeMaster {

	@Inject DocUpdateLogService docUpdateLogService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		// 画面に表示する更新履歴一覧にしてレスポンスに設定
		res.contents.updateLogs = this.getHistoryList(res.contents.docId);
	}

	/**
	 * 更新履歴一覧の取得.
	 * @param docId 文書ID
	 * @return
	 */
	public List<UpdateHistoryInfo> getHistoryList(Long docId) {
		if (docId != null) {
			return docUpdateLogService.getHistoryList(docId);
		}
		return null;
	}

	/**
	 * 文書更新履歴の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		// 更新区分を設定
		DocUpdateType docUpdateType = null;
		if (eq(CommonFlag.ON, req.wfApplying)) {
			docUpdateType = DocUpdateType.WF;
		} else if (req.contents.docId == null) {
			docUpdateType = DocUpdateType.REGIST;
		} else {
			docUpdateType = DocUpdateType.UPDATE;
		}
		// 文書更新履歴の登録
		docUpdateLogService.saveMwtDocUpdateLog(docUpdateType, docId, req.docInfo.title, req.docInfo.contentsType
				,req.docInfo.majorVersion, req.docInfo.minorVersion, req.versionInfo.updateVersionType, null, res.docHistoryId, null);
	}
}
