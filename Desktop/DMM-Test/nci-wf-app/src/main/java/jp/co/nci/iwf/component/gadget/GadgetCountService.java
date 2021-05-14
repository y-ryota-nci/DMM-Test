package jp.co.nci.iwf.component.gadget;

import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.input.BaseTrayInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.CodeMaster.ProcessStatus;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tray.TrayConditionDef;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.component.tray.TrayService;

@BizLogic
public class GadgetCountService extends TrayService {

	/** 件数カウント用Function */
	private Function<GetActivityListInParam, Integer> funcCount;

	/** サービスの初期化 */
	@PostConstruct
	public void initialize() {
		// IWF APIによる件数カウントのラムダ式
		funcCount = p -> wf.getActivityList(p).getAllCount().intValue();
	}

	protected <P extends BaseTrayInParam<?>> P createSearchInParam(Class<P> clazz, String mode, TraySearchRequest req) {
		final P in = newInstance(clazz);
		in.setMode(mode);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setExecuting(true);
		in.setSelectMode(CodeMaster.SelectMode.COUNT);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);

		// 代理
		final WfUserRole userRole = sessionHolder.getWfUserRole().clone(true);
		final String proxyUser = (String)req.get("proxyUser");
		if (isNotEmpty(proxyUser)) {
			final WfUserRole proxy = new WfUserRoleImpl();
			proxy.setCorporationCode(proxyUser.split("_")[0]);
			proxy.setUserCode(proxyUser.split("_")[1]);
			userRole.setProxyUserRole(proxy);
			userRole.setProxy(true);
		}
		in.setWfUserRole(userRole);

		return in;
	}

	public GetActivityListInParam createInParam(String trayType) {
		if (trayType == null)
			throw new BadRequestException("trayTypeが未指定です");

		// ログイン者の参照可能なトレイ設定を求める
		final TrayConfig config = getAccessibleTrayConfig(trayType);
		if (config == null)
			throw new InternalServerErrorException("トレイ設定が1つもありません");

		final TraySearchRequest req = new TraySearchRequest();

		final long trayConfigId = config.trayConfigId;
		final List<TrayConditionDef> conditions = getTrayConditions(trayConfigId);
		req.put("configConditions", toListMap(conditions));

		String mode = TrayType.OWN.equals(trayType) ? GetActivityListInParam.Mode.OWN_PROCESS_TRAY : GetActivityListInParam.Mode.USER_TRAY;
		return createSearchInParam(GetActivityListInParam.class, mode, req);
	}

	public int getCount(GetActivityListInParam in) {
		return funcCount.apply(in);
	}

	/**
	 *
	 * @param searchConditionList
	 */
	public void addApplicationPendingConditions(List<WfSearchCondition<?>> searchConditionList) {
		{
			WfSearchCondition<String> tc = new WfSearchConditionImpl<String>();
			tc.setColumnName(WfvTray.PROCESS_STATUS);
			tc.setSearchCondtionType(CodeMaster.SearchConditionType.EQUAL);
			tc.setSearchConditionValue(ProcessStatus.START);
			searchConditionList.add(tc);
		}
	}

	public void addApprovalPendingConditions(List<WfSearchCondition<?>> searchConditionList) {
		{
			WfSearchCondition<String> tc = new WfSearchConditionImpl<String>();
			tc.setColumnName(WfvTray.PROCESS_STATUS);
			tc.setSearchCondtionType(CodeMaster.SearchConditionType.EQUAL);
			tc.setSearchConditionValue(ProcessStatus.RUN);
			searchConditionList.add(tc);
		}
	}

	public void addApprovedConditions(List<WfSearchCondition<?>> searchConditionList) {
		{
			WfSearchCondition<String> tc = new WfSearchConditionImpl<String>();
			tc.setColumnName(WfvTray.PROCESS_STATUS);
			tc.setSearchCondtionType(CodeMaster.SearchConditionType.EQUAL);
			tc.setSearchConditionValue(ProcessStatus.RUN);
			searchConditionList.add(tc);
		}
	}
}
