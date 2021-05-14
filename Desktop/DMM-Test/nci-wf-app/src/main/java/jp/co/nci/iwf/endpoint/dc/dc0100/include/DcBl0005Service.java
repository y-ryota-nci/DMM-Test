package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.DocAttributeExService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書属性(拡張)ブロック：文書属性(拡張)サービス
 */
@BizLogic
public class DcBl0005Service extends BaseService implements CodeMaster {

	/** 文書属性(拡張)情報サービス */
	@Inject protected DocAttributeExService docAttributeExService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		// 文書属性(拡張)一覧の取得
		if (res.contents.docId != null) {
			res.contents.attributeExs = docAttributeExService.getDocAttributeExList(res.contents.docId, null, res.contents.corporationCode);
		} else if (req.copyDocId != null) {
			res.contents.attributeExs = docAttributeExService.getDocAttributeExList(req.copyDocId, null, res.contents.corporationCode);
			res.contents.attributeExs.forEach(e -> {
				e.docMetaId = null;
			});
		}
		// 設定可能なメタテンプレート一覧の取得
		res.contents.metaTemplates = docAttributeExService.getMetaTemplateOptionList(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/**
	 * メタテンプレートIDから文書属性(拡張)一覧取得
	 * @param req
	 * @return
	 */
	public DcBl0005Response getAttributeExListByMetaTemplateId(DcBl0005Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です。");
		}
		final DcBl0005Response res = createResponse(DcBl0005Response.class, req);
		res.attributeExs = docAttributeExService.getDocAttributeExListByMetaTemplateId(req.corporationCode, req.metaTemplateId);
		res.success = true;
		return res;
	}

	/**
	 * 文書属性(拡張)情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		docAttributeExService.saveMwtDocMetaInfo(docId, req.attributeExs);
	}
}
