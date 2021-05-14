package jp.co.nci.iwf.endpoint.gd.gd0010;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.gadget.GadgetCountService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * ガジェット(件数)画面サービス
 */
@BizLogic
public class Gd0010Service extends BasePagingService {

	@Inject private GadgetCountService gadgetCountService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Gd0010Response init(Gd0010Request req) {
		return search(req);
	}

	/**
	 * リフレッシュ
	 * @param req
	 * @return
	 */
	public Gd0010Response refresh(Gd0010Request req) {
		return search(req);
	}

	private Gd0010Response search(Gd0010Request req) {
		Gd0010Response res = createResponse(Gd0010Response.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		login.getAccessibleMenus().forEach(m -> {
			if (isNotEmpty(m.getUrl())) {
				if (m.getUrl().contains("/wl/wl0030.html")) {
					res.worklist = true;
				} else if (m.getUrl().contains("/wl/wl0032.html")) {
					res.ownlist = true;
				} else if (m.getUrl().contains("/na/na0010.html")) {
					res.newApplication = true;
				}
			}
		});
		final WfUserRole userRole = sessionHolder.getWfUserRole().clone(true);

		if (res.worklist) {
			res.applicationPendingCount = getApplicationPendingCount(userRole);
			res.approvalPendingCount = getApprovalPendingCount(userRole);
		}
		if (res.ownlist) {
			res.approvedCount = getApprovedCount(userRole);
		}

		res.success = true;
		return res;
	}

	private int getApplicationPendingCount(WfUserRole userRole) {
		GetActivityListInParam in = gadgetCountService.createInParam(TrayType.WORKLIST.toString());
		gadgetCountService.addApplicationPendingConditions(in.getSearchConditionList());
		return gadgetCountService.getCount(in);
	}

	private int getApprovalPendingCount(WfUserRole userRole) {
		GetActivityListInParam in = gadgetCountService.createInParam(TrayType.WORKLIST.toString());
		gadgetCountService.addApprovalPendingConditions(in.getSearchConditionList());
		return gadgetCountService.getCount(in);
	}

	private int getApprovedCount(WfUserRole userRole) {
		GetActivityListInParam in = gadgetCountService.createInParam(TrayType.OWN.toString());
		gadgetCountService.addApprovedConditions(in.getSearchConditionList());
		return gadgetCountService.getCount(in);
	}

}