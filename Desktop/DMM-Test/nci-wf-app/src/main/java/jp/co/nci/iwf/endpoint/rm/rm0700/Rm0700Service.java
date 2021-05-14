package jp.co.nci.iwf.endpoint.rm.rm0700;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.menu.download.MenuRoleDownloader;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 参加者ロール一覧のサービス【企業管理者】
 */
@BizLogic
public class Rm0700Service extends MmBaseService<WfmMenuRole> {
	@Inject
	private WfmLookupService lookup;
	@Inject
	private MenuRoleDownloader downloader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0700Response init(Rm0700Request req) {
		final Rm0700Response res = createResponse(Rm0700Response.class, req);
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
	public Rm0700Response search(Rm0700Request req) {
		SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setMenuRoleCode(req.menuRoleCode);
		inParam.setMenuRoleName(req.menuRoleName);
		inParam.setMenuRoleType(MenuRoleType.CORPORATION);
		inParam.setValidStartDate(req.validStartDate);
		inParam.setValidEndDate(req.validEndDate);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setPageSize(req.pageSize);
		inParam.setPageNo(req.pageNo);
//		inParam.setOrderBy(toOrderBy(req, ""));
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(inParam);
		final List<WfmMenuRole> list = outParam.getMenuRoles();

		int allCount = outParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Rm0700Response res = createResponse(Rm0700Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = list;
		res.success = true;
		return res;
	}

	/**
	 * 参加者ロールマスタ追加
	 * @param req
	 * @return
	 */
	public Rm0700Response add(Rm0700Request req) {
		final WfmMenuRole menuRole = new WfmMenuRole();
		menuRole.setCorporationCode(req.corporationCode);
		menuRole.setValidStartDate(today());
		menuRole.setValidEndDate(ENDDATE);
		menuRole.setDeleteFlag(DeleteFlag.OFF);

		final Rm0700Response res = createResponse(Rm0700Response.class, req);
		res.menuRole = menuRole;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.menuRole));
		return res;
	}

	/**
	 * 参加者ロール定義をZIPファイルでダウンロード
	 * @return
	 */
	public StreamingOutput downloadZip(String corporationCode, String menuRoleType, Set<String> menuRoleCodes) {
		return downloader.setup(corporationCode, menuRoleType, menuRoleCodes);
	}
}
