package jp.co.nci.iwf.endpoint.mm.mm0092;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmLookupInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmLookupTypeInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupTypeInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmLookupOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmLookupTypeOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * ルックアップグループ設定サービス
 */
@BizLogic
public class Mm0092Service extends MmBaseService<WfmLookup> {

	@Inject
	private WfmLookupService lookup;
	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0092Response init(Mm0092Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.lookupTypeCode))
			throw new BadRequestException("企業コードまたはルックアップグループIDが未指定です");

		final Mm0092Response res = createResponse(Mm0092Response.class, req);

		SearchWfmLookupTypeInParam inParam = new SearchWfmLookupTypeInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setLookupTypeCode(req.lookupTypeCode);
		SearchWfmLookupTypeOutParam outParam = wf.searchWfmLookupType(inParam);
		final List<WfmLookupType> list = outParam.getWfmLookupTypes();
		res.lookupType = list.get(0);

		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);

		// 排他
		if (req.timestampUpdated != null && !eq(req.timestampUpdated, res.lookupType.getTimestampUpdated().getTime())) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	public Mm0092Response search(Mm0092Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.lookupTypeCode)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		SearchWfmLookupInParam luInParam = new SearchWfmLookupInParam();
		luInParam.setCorporationCode(req.corporationCode);
		luInParam.setLookupTypeCode(req.lookupTypeCode);
		luInParam.setPageSize(req.pageSize);
		luInParam.setPageNo(req.pageNo);
		luInParam.setOrderBy(toOrderBy(req, "A."));
		SearchWfmLookupOutParam luOutParam = wf.searchWfmLookup(luInParam);
		final List<WfmLookup> luList = luOutParam.getWfmLookups();

		int allCount = luOutParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0092Response res = createResponse(Mm0092Response.class, req, allCount);

		SearchWfmLookupTypeInParam inParam = new SearchWfmLookupTypeInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setLookupTypeCode(req.lookupTypeCode);
		SearchWfmLookupTypeOutParam outParam = wf.searchWfmLookupType(inParam);
		final List<WfmLookupType> list = outParam.getWfmLookupTypes();
		res.lookupType = list.get(0);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;
		res.results = luList;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);

		res.success = true;

		return res;
	}

	/**
	 * ルックアップグループの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0092Response update(Mm0092UpdateRequest req) {
		SearchWfmLookupTypeInParam inParam = new SearchWfmLookupTypeInParam();
		inParam.setCorporationCode(req.lookupType.getCorporationCode());
		inParam.setLookupTypeCode(req.lookupType.getLookupTypeCode());
		SearchWfmLookupTypeOutParam outParam = wf.searchWfmLookupType(inParam);
		final List<WfmLookupType> list = outParam.getWfmLookupTypes();
		WfmLookupType lookupType = list.get(0);

		if (lookupType == null) {
			throw new AlreadyUpdatedException();
		}

		lookupType.setLookupTypeName(req.lookupType.getLookupTypeName());
		lookupType.setSortOrder(req.lookupType.getSortOrder());
		lookupType.setDeleteFlag(req.lookupType.getDeleteFlag());

		UpdateWfmLookupTypeInParam updateIn = new UpdateWfmLookupTypeInParam();
		updateIn.setWfmLookupType(req.lookupType);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmLookupType(updateIn);

		final Mm0092Response res = createResponse(Mm0092Response.class, req);

		res.lookupType = lookupType;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.lookupType));
		res.success = true;
		return res;
	}

	/**
	 * ルックアップの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0092Response delete(Mm0092Request req) {
		for (WfmLookup deleteLookup : req.deleteLookups) {
			DeleteWfmLookupInParam deleteIn = new DeleteWfmLookupInParam();
			deleteIn.setWfmLookup(deleteLookup);
			deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
			wf.deleteWfmLookup(deleteIn);
		}
		final Mm0092Response res = createResponse(Mm0092Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.lookup));

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);

		res.success = true;
		return res;
	}

}
