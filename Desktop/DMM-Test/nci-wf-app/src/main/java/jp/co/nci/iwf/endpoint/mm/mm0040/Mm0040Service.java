package jp.co.nci.iwf.endpoint.mm.mm0040;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 業務管理項目名称サービス
 */
@BizLogic
public class Mm0040Service extends BaseService {
	@Inject
	private Mm0040Repository repository;
	@Inject
	private MwmLookupService lookup;
	@Inject
	private MultilingalService multi;
	@Inject
	private WfmLookupService wfLookup;
	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0040Response init(Mm0040Request req) {
		final Mm0040Response res = this.createResponse(Mm0040Response.class, req);

		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.businessInfoNames = repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode());

		// 選択肢
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
	public Mm0040Response save(Mm0040SaveRequest req) {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		final Map<String, MwmDocBusinessInfoName> orgMap = repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode()).stream()
				.collect(Collectors.toMap(MwmDocBusinessInfoName::getDocBusinessInfoCode, e -> e));
		for (MwmDocBusinessInfoName input : req.businessInfoNameList) {
			repository.update(orgMap.get(input.getDocBusinessInfoCode()), input);
			// 多言語対応
			multi.save("MWM_DOC_BUSINESS_INFO_NAME", input.getDocBusinessInfoNameId(), "DOC_BUSINESS_INFO_NAME", input.getDocBusinessInfoName());
		}

		final Mm0040Response res = init(new Mm0040Request());
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.docBusinessInfo));
		return res;
	}


	/**
	 * リセット
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0040Response reset(BaseRequest req) {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();

		// ASPから初期値を取得
		final Map<String, MwmDocBusinessInfoName> orgMap = repository.getBusinessInfoNames(CorporationCodes.ASP, loginInfo.getLocaleCode()).stream()
				.collect(Collectors.toMap(MwmDocBusinessInfoName::getDocBusinessInfoCode, e -> e));
		// 現在、登録済みの自社のデータを取得し、ASPの初期値にて上書きして更新する
		repository.getBusinessInfoNames(loginInfo.getCorporationCode(), loginInfo.getLocaleCode()).stream().forEach(e -> {
			repository.update(e, orgMap.get(e.getDocBusinessInfoCode()));
			// 多言語対応
			multi.save("MWM_DOC_BUSINESS_INFO_NAME", e.getDocBusinessInfoNameId(), "DOC_BUSINESS_INFO_NAME", e.getDocBusinessInfoName());
		});

		final Mm0040Response res = init(new Mm0040Request());
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.docBusinessInfo));
		return res;
	}
}
