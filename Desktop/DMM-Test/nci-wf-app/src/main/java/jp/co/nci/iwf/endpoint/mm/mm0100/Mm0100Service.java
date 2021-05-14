package jp.co.nci.iwf.endpoint.mm.mm0100;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmInformationSharerDefOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 情報共有設定サービス
 */
@BizLogic
public class Mm0100Service extends MmBaseService<WfmInformationSharerDef> implements CodeMaster {

	@Inject
	private WfmLookupService lookup;
	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0100InitResponse init(Mm0100InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// 起動パラメータのバリデーション
		validateInitParams(req);

		// 初期検索
		final Mm0100InitResponse res = createResponse(Mm0100InitResponse.class, req);

		// プロセス定義取得
		res.processDef = routeSetting.getProcessDef(req.corporationCode, req.processDefCode, req.processDefDetailCode);
		res.assignRoles = getAssignRoles(req.corporationCode);
		res.expressionDefs = routeSetting.getExpressionDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode);
		res.expressionDefs.add(0, new WfmExpressionDef());
		res.expressionInfoSharerTypes = lookup.getOptionItems(
				false,
				corporationCode,
				LookupTypeCode.EXPRESSION_INFO_SHARER_TYPE,
				ExpressionInfoSharerType.TRUE_VALUE,
				ExpressionInfoSharerType.FALSE_VALUE
		);
		res.informationSharerTypes = lookup.getOptionItems(false, LookupTypeCode.INFORMATION_SHARER_TYPE);

		res.success = true;
		return res;
	}

	private void validateInitParams(Mm0100InitRequest req) {
		// 新規以外
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.processDefCode == null)
			throw new BadRequestException("プロセス定義コードが未指定です");
		if (req.processDefDetailCode == null)
			throw new BadRequestException("プロセス定義コード枝番が未指定です");
	}

	private List<WfmAssignRole> getAssignRoles(String corporationCode) {
		SearchWfmAssignRoleInParam in = new SearchWfmAssignRoleInParam();
		//検索条件
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(CodeMaster.DefaultFlag.OFF);
		Set<String> belongTypes = new HashSet<>();
		Collections.addAll(belongTypes, BelongType.ORGANIZATION, BelongType.ORG_POST, BelongType.POST, BelongType.USER, BelongType.ALL_OF_CORP);
		in.setBelongTypes(belongTypes);
		//ソート順
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "R.ASSIGN_ROLE_CODE")});
		return wf.searchWfmAssignRole(in).getAssignRoles();
	}

	public Mm0100SearchResponse search(Mm0100SearchRequest req) {
		SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(req.corporationCode);
		in.setProcessDefCode(req.processDefCode);
		in.setProcessDefDetailCode(req.processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		in.setOrderBy(toOrderBy(req, "S."));

		SearchWfmInformationSharerDefOutParam out = wf.searchWfmInformationSharerDef(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0100SearchResponse res = createResponse(Mm0100SearchResponse.class, req, out.getCount());
		final List<WfmInformationSharerDef> results = out.getInformationSharerDefs();
		res.results = results;
		res.informationSharerDefs = results.stream()
				.collect(Collectors.toMap(WfmInformationSharerDef::getUniqueKey, sd -> sd));

		res.success = true;
		return res;
	}

	/**
	 * 更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0100SaveResponse update(Mm0100SaveRequest req) {
		final Mm0100SaveResponse res = createResponse(Mm0100SaveResponse.class, req);
		routeSetting.update(false, req.informationSharerDef);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.informationSharingPerson));
		res.success = true;
		return res;
	}

	/**
	 * 削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0100SaveResponse remove(Mm0100SaveRequest req) {
		final Mm0100SaveResponse res = createResponse(Mm0100SaveResponse.class, req);
		req.informationSharerDefs.stream().forEach(e -> routeSetting.delete( e));
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.informationSharingPerson));
		res.success = true;
		return res;
	}
}
