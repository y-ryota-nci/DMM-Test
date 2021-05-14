package jp.co.nci.iwf.endpoint.mm.mm0050;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.endpoint.mm.mm0051.Mm0051Entity;

/**
 * 採番形式一覧のサービス
 */
@BizLogic
public class Mm0050Service extends MmBaseService<Mm0051Entity> {
	@Inject
	private WfmLookupService lookup;

	@Inject
	private Mm0050Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0050Response init(Mm0050Request req) {
		final Mm0050Response res = createResponse(Mm0050Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0050Response search(Mm0050Request req) {
		int allCount = repository.count(req, sessionHolder.getLoginInfo().getLocaleCode());
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0050Response res = createResponse(Mm0050Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		// 結果の抽出
		final List<Mm0051Entity> results;
		if (allCount == 0) {
			results = new ArrayList<>();
		} else {
			results = repository.search(req, sessionHolder.getLoginInfo().getLocaleCode(), true);
		}

		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * 採番形式マスタ追加
	 * @param req
	 * @return
	 */
	public Mm0050Response add(Mm0050Request req) {
		final Mm0051Entity numberingFormat = new Mm0051Entity();
		numberingFormat.corporationCode = req.corporationCode;
		numberingFormat.deleteFlag = DeleteFlag.OFF;

		final Mm0050Response res = createResponse(Mm0050Response.class, req);
		res.numberingFormat = numberingFormat;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
