package jp.co.nci.iwf.endpoint.dc.dc0902;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 業務文書の登録・更新画面サービス.
 */
@BizLogic
public class Dc0902Service extends BaseService implements CodeMaster, DcCodeBook {

	/** 文書フォルダサービス */
	@Inject private DocFolderService docFolderService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0902Response init(Dc0902Request req) {
		// 戻り値生成
		final Dc0902Response res = createResponse(Dc0902Response.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 文書フォルダ検索
	 * @param req
	 * @return
	 */
	public List<OptionItem> search(String folderName, Long exclusionDocFolderId) {
//		// 戻り値生成
//		final Dc0902Response res = createResponse(Dc0902Response.class, null);
//		res.folders = docFolderService.getFolderOptionList(folderName, exclusionDocFolderId, sessionHolder.getLoginInfo().isCorpAdmin());
//		res.success = true;
//		return res;
		return docFolderService.getFolderOptionList(folderName, exclusionDocFolderId, sessionHolder.getLoginInfo().isCorpAdmin());
	}
}
