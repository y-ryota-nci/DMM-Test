package jp.co.nci.iwf.endpoint.mm.mm0030;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DataType;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;

/**
 * 業務管理項目名称サービス
 */
@BizLogic
public class Mm0030Service extends BaseService {
	@Inject
	private Mm0030Repository repository;
	@Inject
	private MwmLookupService lookup;
	@Inject
	private MultilingalService multi;
	@Inject
	private NumberingService numbering;
	@Inject
	private WfmLookupService wfLookup;
	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0030Response init(BaseRequest req) {
		final Mm0030Response res = this.createResponse(Mm0030Response.class, req);

		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.businessInfoNames = repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode());

		// 選択肢
		res.businessInfoTypes = lookup.getOptionItems(LookupGroupId.BUSINESS_INFO_TYPE, false);
		res.validFlags = lookup.getOptionItems(LookupGroupId.VALID_FLAG, false);
		res.screenPartsInputFlags = lookup.getOptionItems(LookupGroupId.SCREEN_PARTS_INPUT_FLAG, false);
		res.dataTypes = wfLookup.getOptionItems(true, LookupTypeCode.DATA_TYPE);

		res.success = true;
		return res;
	}

	/**
	 * 更新処理
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0030Response save(Mm0030SaveRequest req) {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();

		for (MwmBusinessInfoName input : req.businessInfoNameList) {
			repository.update(input);
			// 多言語対応
			multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
		}

		final Mm0030Response res = createResponse(Mm0030Response.class, req);

		res.businessInfoNames = repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode());
		res.businessInfoTypes = lookup.getOptionItems(LookupGroupId.BUSINESS_INFO_TYPE, true);
		res.validFlags = lookup.getOptionItems(LookupGroupId.VALID_FLAG, false);
		res.screenPartsInputFlags = lookup.getOptionItems(LookupGroupId.SCREEN_PARTS_INPUT_FLAG, false);
		res.dataTypes = wfLookup.getOptionItems(true, LookupTypeCode.DATA_TYPE);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.businessInfoName));
		res.success = true;
		return res;
	}


	/**
	 * リセット
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0030Response reset(BaseRequest req) {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();

		Map<String, MwmBusinessInfoName> before = new HashMap<>();
		repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode()).stream().forEach(v -> {
			before.put(v.getBusinessInfoCode(), v);
		});

		for (int i = 1; i <= 250; i++) {
			if (!before.containsKey("PROCESS_BUSINESS_INFO_" + String.format("%03d", i))) {
				MwmBusinessInfoName input = new MwmBusinessInfoName();
				input.setBusinessInfoNameId(numbering.newPK(MwmBusinessInfoName.class));
				input.setCorporationCode(loginInfo.getCorporationCode());
				input.setBusinessInfoCode("PROCESS_BUSINESS_INFO_" + String.format("%03d", i));
				input.setBusinessInfoName("業務管理項目" + String.format("%03d", i));
				input.setBusinessInfoType("3");	// 両方
				input.setValidFlag(CommonFlag.OFF);
				input.setScreenPartsInputFlag(CommonFlag.ON);
				input.setDataType(DataType.STRING);
				input.setSortOrder(i + 1000L);
				input.setDeleteFlag(DeleteFlag.OFF);
				repository.insert(input);
				// 多言語対応
				multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
			}
		}

		// 件名
		if (!before.containsKey("SUBJECT")) {
			MwmBusinessInfoName input = new MwmBusinessInfoName();
			input.setBusinessInfoNameId(numbering.next("MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID"));
			input.setCorporationCode(loginInfo.getCorporationCode());
			input.setBusinessInfoCode("SUBJECT");
			input.setBusinessInfoName("件名");
			input.setBusinessInfoType("3");	// 両方
			input.setValidFlag(CommonFlag.ON);
			input.setScreenPartsInputFlag(CommonFlag.ON);
			input.setDataType(DataType.STRING);
			input.setSortOrder(1L);
			input.setDeleteFlag(DeleteFlag.OFF);
			repository.insert(input);
			// 多言語対応
			multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
		}
		// 申請番号
		if (!before.containsKey("APPLICATION_NO")) {
			MwmBusinessInfoName input = new MwmBusinessInfoName();
			input.setBusinessInfoNameId(numbering.next("MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID"));
			input.setCorporationCode(loginInfo.getCorporationCode());
			input.setBusinessInfoCode("APPLICATION_NO");
			input.setBusinessInfoName("申請番号");
			input.setBusinessInfoType("3");	// 両方
			input.setValidFlag(CommonFlag.ON);
			input.setScreenPartsInputFlag(CommonFlag.ON);
			input.setDataType(DataType.STRING);
			input.setSortOrder(2L);
			input.setDeleteFlag(DeleteFlag.OFF);
			repository.insert(input);
			// 多言語対応
			multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
		}
		// 決裁番号
		if (!before.containsKey("APPROVAL_NO")) {
			MwmBusinessInfoName input = new MwmBusinessInfoName();
			input.setBusinessInfoNameId(numbering.next("MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID"));
			input.setCorporationCode(loginInfo.getCorporationCode());
			input.setBusinessInfoCode("APPROVAL_NO");
			input.setBusinessInfoName("決裁番号");
			input.setBusinessInfoType("3");	// 両方
			input.setValidFlag(CommonFlag.ON);
			input.setScreenPartsInputFlag(CommonFlag.ON);
			input.setDataType(DataType.STRING);
			input.setSortOrder(3L);
			input.setDeleteFlag(DeleteFlag.OFF);
			repository.insert(input);
			// 多言語対応
			multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
		}
		// 金額
		if (!before.containsKey("AMOUNT")) {
			MwmBusinessInfoName input = new MwmBusinessInfoName();
			input.setBusinessInfoNameId(numbering.next("MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID"));
			input.setCorporationCode(loginInfo.getCorporationCode());
			input.setBusinessInfoCode("AMOUNT");
			input.setBusinessInfoName("金額");
			input.setBusinessInfoType("3");	// 両方
			input.setValidFlag(CommonFlag.ON);
			input.setScreenPartsInputFlag(CommonFlag.ON);
			input.setDataType(DataType.INT);
			input.setSortOrder(4L);
			input.setDeleteFlag(DeleteFlag.OFF);
			repository.insert(input);
			// 多言語対応
			multi.save("MWM_BUSINESS_INFO_NAME", input.getBusinessInfoNameId(), "BUSINESS_INFO_NAME", input.getBusinessInfoName());
		}

		final Mm0030Response res = createResponse(Mm0030Response.class, req);

		res.businessInfoNames = repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode());
		res.businessInfoTypes = lookup.getOptionItems(LookupGroupId.BUSINESS_INFO_TYPE, true);
		res.validFlags = lookup.getOptionItems(LookupGroupId.VALID_FLAG, false);
		res.dataTypes = wfLookup.getOptionItems(true, LookupTypeCode.DATA_TYPE);
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.businessInfoName));
		res.success = true;
		return res;
	}
}
