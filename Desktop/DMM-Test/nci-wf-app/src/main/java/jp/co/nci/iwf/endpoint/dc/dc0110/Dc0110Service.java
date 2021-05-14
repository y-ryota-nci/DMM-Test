package jp.co.nci.iwf.endpoint.dc.dc0110;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面文書定義一覧のサービス
 */
@BizLogic
public class Dc0110Service extends BasePagingService {
	/** 画面文書定義一覧のリポジトリ */
	@Inject private Dc0110Repository repository;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;
	/** 画面Javascriptサービス */
	@Inject private JavascriptService jsService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0110InitResponse init(BaseRequest req) {
		final Dc0110InitResponse res = createResponse(Dc0110InitResponse.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Dc0110SearchResponse search(Dc0110SearchRequest req) {
		final int allCount = repository.count(req);
		final Dc0110SearchResponse res = createResponse(Dc0110SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Dc0110DeleteRequest req) {
		@SuppressWarnings("unused")
		int count = 0;
		for (Long screenDocId : req.screenDocIds) {
			count += repository.delete(screenDocId);
			multi.physicalDelete("MWM_SCREEN_DOC_DEF", screenDocId);

			// 画面に紐付くJavascriptキャッシュをクリア
			jsService.clear();
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.screenDocInfo));
		res.success = true;
		return res;
	}

}
