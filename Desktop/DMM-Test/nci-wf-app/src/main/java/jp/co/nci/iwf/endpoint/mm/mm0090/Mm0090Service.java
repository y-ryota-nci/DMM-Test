package jp.co.nci.iwf.endpoint.mm.mm0090;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupTypeInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmLookupTypeOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * ルックアップグループ一覧のサービス
 */
@BizLogic
public class Mm0090Service extends MmBaseService<WfmLookupType> {
	@Inject
	private WfmLookupService lookup;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0090Response init(Mm0090Request req) {
		final Mm0090Response res = createResponse(Mm0090Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.updateFlags = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0090Response search(Mm0090Request req) {
		SearchWfmLookupTypeInParam inParam = new SearchWfmLookupTypeInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setLookupTypeCode(req.lookupTypeCode);
		inParam.setLookupTypeName(req.lookupTypeName);
		inParam.setUpdateFlag(req.updateFlag);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setPageSize(req.pageSize);
		inParam.setPageNo(req.pageNo);
		inParam.setOrderBy(toOrderBy(req, "A."));
		SearchWfmLookupTypeOutParam outParam = wf.searchWfmLookupType(inParam);
		final List<WfmLookupType> results = outParam.getWfmLookupTypes();

		int allCount = outParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0090Response res = createResponse(Mm0090Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * ルックアップグループマスタ追加
	 * @param req
	 * @return
	 */
	public Mm0090Response add(Mm0090Request req) {
		final WfmLookupType lookupType = new WfmLookupType();
		lookupType.setCorporationCode(req.corporationCode);
		lookupType.setDeleteFlag(DeleteFlag.OFF);

		final Mm0090Response res = createResponse(Mm0090Response.class, req);
		res.lookupType = lookupType;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
