package jp.co.nci.iwf.endpoint.mm.mm0010;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupGroupEx;

/**
 * ルックアップグループ一覧のサービス
 */
@BizLogic
public class Mm0010Service extends MmBaseService<MwmLookupGroupEx> {
	@Inject
	private WfmLookupService lookup;

	@Inject
	private Mm0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0010Response init(Mm0010Request req) {
		final Mm0010Response res = createResponse(Mm0010Response.class, req);
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
	public Mm0010Response search(Mm0010Request req) {
		int allCount = repository.count(req, sessionHolder.getLoginInfo().getLocaleCode());
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0010Response res = createResponse(Mm0010Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		// 結果の抽出
		final List<MwmLookupGroupEx> results;
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
	 * ルックアップグループマスタ追加
	 * @param req
	 * @return
	 */
	public Mm0010Response add(Mm0010Request req) {
		final MwmLookupGroupEx lookupGroup = new MwmLookupGroupEx();
		lookupGroup.setCorporationCode(req.corporationCode);
		lookupGroup.setDeleteFlag(DeleteFlag.OFF);

		final Mm0010Response res = createResponse(Mm0010Response.class, req);
		res.lookupGroup = lookupGroup;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
