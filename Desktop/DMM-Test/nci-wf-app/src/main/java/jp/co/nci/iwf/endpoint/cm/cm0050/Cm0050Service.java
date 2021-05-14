package jp.co.nci.iwf.endpoint.cm.cm0050;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfvUserBelongOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ユーザ所属選択のサービス
 */
@BizLogic
public class Cm0050Service extends BasePagingService {
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Cm0050Response res = createResponse(Cm0050Response.class, req);
		res.success = true;
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(true);
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0050Response search(Cm0050Request req) {
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(req.corporationCode);
		in.setUserAddedInfo(req.userAddedInfo);
		in.setUserName(req.userName);
		in.setOrganizationCode(req.organizationCode);
		in.setOrganizationName(req.organizationName);
		in.setOrganizationTreeName(req.organizationTreeName);
		in.setPostName(req.postName);
		in.setValidStartDateOrganization(req.validStartDate);
		in.setValidStartDatePost(req.validStartDate);
		in.setValidStartDateUser(req.validStartDate);
		in.setValidStartDateUserBelong(req.validStartDate);
		in.setValidEndDateOrganization(req.validEndDate);
		in.setValidEndDatePost(req.validEndDate);
		in.setValidEndDateUser(req.validEndDate);
		in.setValidEndDateUserBelong(req.validEndDate);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		if (req.sortColumn != null) {
			String[] cols = req.sortColumn.split(",\\s*");
			List<OrderBy> orderBys = new ArrayList<>();
			for (String col : cols) {
				orderBys.add(new OrderBy(req.sortAsc, col));
			}
			in.setOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
		}

		// 所有ロールによる暗黙の絞り込み条件
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (!login.isAspAdmin()) {
			if (isNotEmpty(login.getCorporationGroupCode()))
				// 企業グループに属していれば、グループ内は見える
				in.setCorporationGroupCode(login.getCorporationGroupCode());
			else
				// ASP管理者でも企業グループにも属してなければ、表示可能なのは自社のみ。
				in.setCorporationCode(login.getCorporationCode());
		}

		// 検索
		final SearchWfvUserBelongOutParam out = wf.searchWfvUserBelong(in);

		final Cm0050Response res = createResponse(Cm0050Response.class, req, out.getCount());
		res.results = out.getUserBelongList();
		res.success = true;

		return res;
	}

}
