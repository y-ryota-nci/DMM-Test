package jp.co.nci.iwf.endpoint.cm.cm0210;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocInfoSearchRequest;
import jp.co.nci.iwf.component.document.DocInfoSearchResponse;
import jp.co.nci.iwf.component.document.DocInfoService;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書情報選択画面のサービス
 */
@BizLogic
public class Cm0210Service extends CmBaseService<MwvDocFolder> {

	/** 文書情報サービス */
	@Inject private DocInfoService docInfoService;
	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0210InitResponse init(BaseRequest req) {
		final Cm0210InitResponse res = createResponse(Cm0210InitResponse.class, req);
		// コンテンツ種別（文書管理用）の選択肢
		// 「3:ファイル」は除外(「1:業務文書」「2:バインダー」のみ含める)
		res.contentsTypes = lookup.getOptionItems(LookupGroupId.CONTENTS_TYPE, true, DcCodeBook.ContentsType.BIZ_DOC, DcCodeBook.ContentsType.BINDER);
		// 公開／非公開の選択肢
		res.publishFlags = lookup.getOptionItems(LookupGroupId.PUBLISH_FLAG, true);
		// 保存期間区分の選択肢
		res.retentionTermTypes = lookup.getOptionItems(LookupGroupId.RETENTION_TERM_TYPE, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return res
	 */
	public DocInfoSearchResponse search(DocInfoSearchRequest req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		final LoginInfo login = sessionHolder.getLoginInfo();
		final DocInfoSearchResponse res = docInfoService.search(req, login.isCorpAdmin());
		res.success = true;
		return res;
	}
}
