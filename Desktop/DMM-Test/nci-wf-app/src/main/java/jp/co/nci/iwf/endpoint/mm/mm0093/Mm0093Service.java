package jp.co.nci.iwf.endpoint.mm.mm0093;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmLookupInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmLookupInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmLookupOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmLookupOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
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
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ルックアップ設定サービス
 */
@BizLogic
public class Mm0093Service extends BaseService {
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private WfmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0093Response init(Mm0093Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.lookupTypeCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0093Response res = createResponse(Mm0093Response.class, req);

		SearchWfmLookupTypeInParam lutInParam = new SearchWfmLookupTypeInParam();
		lutInParam.setCorporationCode(req.corporationCode);
		lutInParam.setLookupTypeCode(req.lookupTypeCode);
		SearchWfmLookupTypeOutParam lutOutParam = wf.searchWfmLookupType(lutInParam);
		final List<WfmLookupType> lutList = lutOutParam.getWfmLookupTypes();

		if (CommonUtil.isEmpty(lutList)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0155, MessageCd.lookupType));
			res.success = false;
			return res;
		}
		res.lookupType = lutList.get(0);

		SearchWfmLookupInParam luInParam = new SearchWfmLookupInParam();
		luInParam.setCorporationCode(req.corporationCode);
		luInParam.setLookupTypeCode(req.lookupTypeCode);
		luInParam.setLookupCode(req.lookupCode);
		SearchWfmLookupOutParam luOutParam = wf.searchWfmLookup(luInParam);
		final List<WfmLookup> luList = luOutParam.getWfmLookups();
		WfmLookup lookup = null;

		if (CommonUtil.isEmpty(luList)) {
			lookup = new WfmLookup();
			lookup.setCorporationCode(req.corporationCode);
			lookup.setLookupTypeCode(req.lookupTypeCode);
			lookup.setDeleteFlag(DeleteFlag.OFF);
		} else {
			lookup = luList.get(0);
		}
//		lookup.setLookupTypeName(lookupType.getLookupTypeName());
		res.lookup = lookup;

		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookupService.getOptionItems(false, LookupTypeCode.UPDATE_FLAG);

		// 排他
		if (!CommonUtil.isEmpty(luList) && !eq(req.timestampUpdated, lookup.getTimestampUpdated().getTime())) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
		} else {
			res.success = true;
		}
		return res;
	}

	/**
	 * ルックアップ登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0093Response insert(Mm0093InsertRequest req) {
		final Mm0093Response res = createResponse(Mm0093Response.class, req);

		SearchWfmLookupInParam luInParam = new SearchWfmLookupInParam();
		luInParam.setCorporationCode(req.lookup.getCorporationCode());
		luInParam.setLookupTypeCode(req.lookup.getLookupTypeCode());
		luInParam.setLookupCode(req.lookup.getLookupCode());
		SearchWfmLookupOutParam luOutParam = wf.searchWfmLookup(luInParam);
		final List<WfmLookup> luList = luOutParam.getWfmLookups();

		if (!CommonUtil.isEmpty(luList)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.lookupCode, req.lookup.getLookupCode()));
			res.success = false;
			return res;
		}

		InsertWfmLookupInParam insertIn = new InsertWfmLookupInParam();
		insertIn.setWfmLookup(req.lookup);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmLookupOutParam insertOut = wf.insertWfmLookup(insertIn);

		try {
			res.lookup = new WfmLookup();
			BeanUtils.copyProperties(res.lookup, insertOut.getWfmLookup());
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}

		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookupService.getOptionItems(false, LookupTypeCode.UPDATE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.lookup));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0093Response update(Mm0093InsertRequest req) {
		final Mm0093Response res = createResponse(Mm0093Response.class, req);

		SearchWfmLookupInParam luInParam = new SearchWfmLookupInParam();
		luInParam.setCorporationCode(req.lookup.getCorporationCode());
		luInParam.setLookupTypeCode(req.lookup.getLookupTypeCode());
		luInParam.setLookupCode(req.lookup.getLookupCode());
		SearchWfmLookupOutParam luOutParam = wf.searchWfmLookup(luInParam);
		final List<WfmLookup> luList = luOutParam.getWfmLookups();

		WfmLookup lookup = luList.get(0);
		lookup.setLookupName(req.lookup.getLookupName());
		lookup.setSortOrder(req.lookup.getSortOrder());
		lookup.setUpdateFlag(req.lookup.getUpdateFlag());
		lookup.setDeleteFlag(req.lookup.getDeleteFlag());
		lookup.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());

		UpdateWfmLookupInParam updateIn = new UpdateWfmLookupInParam();
		updateIn.setWfmLookup(lookup);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmLookupOutParam updateOut = wf.updateWfmLookup(updateIn);

		try {
			res.lookup = new WfmLookup();
			BeanUtils.copyProperties(res.lookup, updateOut.getWfmLookup());
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);
		res.updateFlagList = lookupService.getOptionItems(false, LookupTypeCode.UPDATE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.lookup));
		res.success = true;
		return res;
	}
}
