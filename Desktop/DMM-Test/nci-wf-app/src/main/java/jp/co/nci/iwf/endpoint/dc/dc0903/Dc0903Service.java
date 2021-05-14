package jp.co.nci.iwf.endpoint.dc.dc0903;

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
public class Dc0903Service extends BaseService implements CodeMaster, DcCodeBook {

	/** 文書フォルダサービス */
	@Inject private DocFolderService docFolderService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0903Response init(Dc0903Request req) {
		// 戻り値生成
		final Dc0903Response res = createResponse(Dc0903Response.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 文書フォルダ検索
	 * @param req
	 * @return
	 */
	public List<OptionItem> search(String folderName, Long exclusionDocFolderId) {
		return docFolderService.getFolderOptionList(folderName, exclusionDocFolderId, sessionHolder.getLoginInfo().isCorpAdmin());
	}
}
