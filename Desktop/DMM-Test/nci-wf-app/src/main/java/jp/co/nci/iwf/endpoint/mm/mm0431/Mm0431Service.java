package jp.co.nci.iwf.endpoint.mm.mm0431;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmPostInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmPostInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmPostOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmPostOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.base.impl.WfmPostImpl;
import jp.co.nci.integrated_workflow.model.custom.WfcPost;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 役職設定サービス
 */
@BizLogic
public class Mm0431Service extends BaseService {
	@Inject
	private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0431Response init(Mm0431Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.postCode))
			throw new BadRequestException("企業コードまたは役職コードが未指定です");
		if (req.timestampUpdated == null)
			throw new BadRequestException("更新日時が未指定です");

		final Mm0431Response res = createResponse(Mm0431Response.class, req);
		res.post = getWfmPost(req.corporationCode, req.postCode);

		// 排他
		if (res.post != null && !eq(req.timestampUpdated, res.post.getTimestampUpdated().getTime())) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	private WfcPost getWfmPost(String corporationCode, String postCode) {
		final SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(corporationCode);
		in.setPostCode(postCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);

		final List<WfcPost> list = wf.searchWfmPost(in).getPostList();
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	/**
	 * 役職の更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0431Response update(Mm0431UpdateRequest req) {
		final WfcPost post = getWfmPost(req.post.getCorporationCode(), req.post.getPostCode());
		if (post == null)
			throw new AlreadyUpdatedException();

		post.setPostName(req.post.getPostName());
		post.setPostNameAbbr(req.post.getPostNameAbbr());
		post.setPostAddedInfo(req.post.getPostAddedInfo());
		post.setPostLevel(req.post.getPostLevel());
		post.setValidStartDate(req.post.getValidStartDate());
		post.setValidEndDate(req.post.getValidEndDate());
		post.setDeleteFlag(DeleteFlag.OFF);

		final UpdateWfmPostInParam in = new UpdateWfmPostInParam();
		in.setWfmPost(post);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final UpdateWfmPostOutParam out = wf.updateWfmPost(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Mm0431Response res = createResponse(Mm0431Response.class, req);
		res.post = (WfmPostImpl)out.getWfmPost();
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.post));
		res.success = true;
		return res;
	}

	/**
	 * 役職の削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0431Response delete(Mm0431Request req) {
		final WfcPost post = getWfmPost(req.corporationCode, req.postCode);
		if (post == null)
			throw new AlreadyUpdatedException();

		final DeleteWfmPostInParam in = new DeleteWfmPostInParam();
		in.setWfmPost(post);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final DeleteWfmPostOutParam out = wf.deleteWfmPost(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Mm0431Response res = createResponse(Mm0431Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.post));
		res.success = true;
		return res;
	}

}
