package jp.co.nci.iwf.endpoint.mm.mm0101;

import java.util.Collections;
import java.util.HashSet;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 情報共有者定義作成サービス
 */
@BizLogic
public class Mm0101Service extends MmBaseService<WfmAssignRole> implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private WfmLookupService lookup;
	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0101InitResponse init(Mm0101InitRequest req) {
		// 初期検索
		final Mm0101InitResponse res = createResponse(Mm0101InitResponse.class, req);
		res.informationSharerTypes = lookup.getOptionItems(false, LookupTypeCode.INFORMATION_SHARER_TYPE);
		res.success = true;
		return res;
	}

	/**
	 * 検索処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0101SearchResponse search(Mm0101SearchRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		SearchWfmAssignRoleInParam in = new SearchWfmAssignRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setAssignRoleCode(req.assignRoleCode);
		in.setAssignRoleName(req.assignRoleName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setBelongTypes(new HashSet<>());
		Collections.addAll(
				in.getBelongTypes(),
				BelongType.ORGANIZATION,
				BelongType.ORG_POST,
				BelongType.POST,
				BelongType.USER,
				BelongType.ALL_OF_CORP
		);
		in.setOrderBy(toOrderBy(req, "R."));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);

		SearchWfmAssignRoleOutParam out = wf.searchWfmAssignRole(in);

		final Mm0101SearchResponse res = createResponse(Mm0101SearchResponse.class, req, out.getCount());
		res.results = out.getAssignRoles();
		res.success = true;
		return res;
	}


	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0101SaveResponse create(Mm0101SaveRequest req) {
		final Mm0101SaveResponse res = createResponse(Mm0101SaveResponse.class, req);
		req.informationSharerDefs.stream().forEach(e -> routeSetting.insert(false, e));
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.informationSharingPerson));
		res.success = true;
		return res;
	}

}
