package jp.co.nci.iwf.endpoint.dc.dc0905;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.document.DocInfoSearchRequest;
import jp.co.nci.iwf.component.document.DocInfoService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書ファイルの移動先選択画面サービス.
 */
@BizLogic
public class Dc0905Service extends BaseService implements CodeMaster, DcCodeBook {

	/** 文書情報サービス */
	@Inject private DocInfoService docInfoService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0905Response init(Dc0905Request req) {
		// 戻り値生成
		final Dc0905Response res = createResponse(Dc0905Response.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 文書情報検索
	 * @param req
	 * @return
	 */
	public List<OptionItem> search(String title, Long excludeDocId) {
		final DocInfoSearchRequest req = new DocInfoSearchRequest();
		req.title = title;
		req.excludeDocId = excludeDocId;
		// とりあえずここでは常に1ページ目でページサイズ20固定
		req.pageNo = 1;
		req.pageSize = 20;
		return docInfoService.getDocInfoOptionList(req, sessionHolder.getLoginInfo().isCorpAdmin());
	}
}
