package jp.co.nci.iwf.endpoint.mm.mm0430;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmPostInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmPostInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmPostOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmPostOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.base.WfmPost;
import jp.co.nci.integrated_workflow.model.base.impl.WfmPostImpl;
import jp.co.nci.integrated_workflow.model.custom.WfcPost;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmPostOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 役職一覧のサービス
 */
@BizLogic
public class Mm0430Service extends MmBaseService<WfcPost> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0430Response init(Mm0430Request req) {
		final Mm0430Response res = createResponse(Mm0430Response.class, req);
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
	public Mm0430Response search(Mm0430Request req) {
		final SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(req.corporationCode);
		in.setDeleteFlag(req.deleteFlag);
		in.setPostCode(req.postCode);
		in.setPostName(req.postName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);
		in.setOrderBy(toOrderBy(req, "A."));
		final SearchWfmPostOutParam out = wf.searchWfmPost(in);
		final List<WfcPost> posts =  out.getPostList();

		int allCount = out.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0430Response res = createResponse(Mm0430Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = posts;
		res.success = true;
		return res;
	}

	/**
	 * 役職追加
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0430Response add(Mm0430Request req) {
		final WfmPost post = new WfmPostImpl();
		post.setCorporationCode(req.corporationCode);
		post.setValidStartDate(today());
		post.setValidEndDate(ENDDATE);
		post.setDeleteFlag(DeleteFlag.OFF);

		final InsertWfmPostInParam in = new InsertWfmPostInParam();
		in.setWfmPost(post);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final InsertWfmPostOutParam out = wf.insertWfmPostDefault(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Mm0430Response res = createResponse(Mm0430Response.class, req);
		res.newPost = out.getWfmPost();
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}

	/**
	 * 役職を削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0430Response delete(Mm0430Request req) {
		for (WfmPost post : req.deletePosts) {
			final DeleteWfmPostInParam in = new DeleteWfmPostInParam();
			in.setWfmPost(post);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final DeleteWfmPostOutParam out = wf.deleteWfmPost(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
		final Mm0430Response res = createResponse(Mm0430Response.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.post));
		return res;
	}
}
