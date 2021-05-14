package jp.co.nci.iwf.endpoint.ds.ds0010;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ダッシュボード画面サービス
 */
@BizLogic
public class Ds0010Service extends BaseService {

	@Inject private CorporationPropertyService prop;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ds0010InitResponse init(BaseRequest req) {
		final Ds0010InitResponse res = createResponse(Ds0010InitResponse.class, req);
		res.success = true;
		res.dashboardUrl = prop.getString(CorporationProperty.DASHBOARD_URL);
		return res;
	}

}
