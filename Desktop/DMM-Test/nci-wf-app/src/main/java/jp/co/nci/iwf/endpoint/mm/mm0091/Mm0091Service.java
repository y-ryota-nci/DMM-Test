package jp.co.nci.iwf.endpoint.mm.mm0091;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmLookupTypeInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmLookupTypeOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupTypeInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmLookupTypeOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ルックアップグループ登録サービス
 */
@BizLogic
public class Mm0091Service extends BaseService {
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0091Response init(Mm0091Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0091Response res = createResponse(Mm0091Response.class, req);
		res.lookupType = new WfmLookupType();
		res.lookupType.setCorporationCode(req.corporationCode);
		res.lookupType.setDeleteFlag(DeleteFlag.OFF);

		res.updateFlags = lookup.getOptionItems(true, LookupTypeCode.UPDATE_FLAG);
		res.success = (res.lookupType != null);
		return res;
	}

	/**
	 * ルックアップグループの登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0091Response insert(Mm0091InsertRequest req) {
		final Mm0091Response res = createResponse(Mm0091Response.class, req);

		SearchWfmLookupTypeInParam luInParam = new SearchWfmLookupTypeInParam();
		luInParam.setCorporationCode(req.lookupType.getCorporationCode());
		luInParam.setLookupTypeCode(req.lookupType.getLookupTypeCode());
		SearchWfmLookupTypeOutParam luOutParam = wf.searchWfmLookupType(luInParam);
		final List<WfmLookupType> luList = luOutParam.getWfmLookupTypes();

		if (!CommonUtil.isEmpty(luList)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.lookupTypeCode, req.lookupType.getLookupTypeCode()));
			res.success = false;
			return res;
		}

		WfmLookupType lookupType = new WfmLookupType();
		lookupType.setCorporationCode(req.lookupType.getCorporationCode());
		lookupType.setLookupTypeCode(req.lookupType.getLookupTypeCode());
		lookupType.setLookupTypeName(req.lookupType.getLookupTypeName());
		lookupType.setUpdateFlag(req.lookupType.getUpdateFlag());
		lookupType.setSortOrder(req.lookupType.getSortOrder());
		lookupType.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());
		lookupType.setDeleteFlag(DeleteFlag.OFF);

		InsertWfmLookupTypeInParam insertIn = new InsertWfmLookupTypeInParam();
		insertIn.setWfmLookupType(lookupType);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmLookupTypeOutParam insertOut = wf.insertWfmLookupType(insertIn);

		try {
			res.lookupType = new WfmLookupType();
			BeanUtils.copyProperties(res.lookupType, insertOut.getWfmLookupType());
		} catch (InvocationTargetException ite) {
		} catch (IllegalAccessException iae) {
		}

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.lookupGroup));
		res.success = true;
		return res;
	}
}
