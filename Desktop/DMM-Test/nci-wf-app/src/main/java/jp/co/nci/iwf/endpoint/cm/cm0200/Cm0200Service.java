package jp.co.nci.iwf.endpoint.cm.cm0200;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocFolderSearchRequest;
import jp.co.nci.iwf.component.document.DocFolderSearchResponse;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書フォルダ選択画面のサービス
 */
@BizLogic
public class Cm0200Service extends CmBaseService<MwvDocFolder> {

	/** 文書フォルダサービス */
	@Inject private DocFolderService docFolderService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return res
	 */
	public DocFolderSearchResponse search(DocFolderSearchRequest req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		final LoginInfo login = sessionHolder.getLoginInfo();
		final DocFolderSearchResponse res = docFolderService.search(req, login.isCorpAdmin());
		res.success = true;
		return res;
	}
}
