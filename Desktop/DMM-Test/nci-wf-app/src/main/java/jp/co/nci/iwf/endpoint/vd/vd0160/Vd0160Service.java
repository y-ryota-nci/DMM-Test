package jp.co.nci.iwf.endpoint.vd.vd0160;

import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.vd.vd0114.Vd0114Repository;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;

/**
 * パーツツリー画面サービス
 */
@BizLogic
public class Vd0160Service extends BaseService {
	@Inject
	private Vd0114Repository vd0114;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0160InitResponse init(BaseRequest req) {
		Vd0160InitResponse res = createResponse(Vd0160InitResponse.class, req);

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 業務管理項目Map
		res.businessInfoCodes = vd0114.getMwmBusinessInfoName(corporationCode, localeCode)
				.stream()
				.collect(Collectors.toMap(
						MwmBusinessInfoName::getBusinessInfoCode, MwmBusinessInfoName::getBusinessInfoName));

		res.success = true;
		return res;
	}

}
