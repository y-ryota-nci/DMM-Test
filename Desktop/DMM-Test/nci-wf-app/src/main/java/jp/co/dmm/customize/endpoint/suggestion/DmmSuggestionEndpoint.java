package jp.co.dmm.customize.endpoint.suggestion;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.dmm.customize.endpoint.suggestion.bumon.DmmSuggestionBumonRequest;
import jp.co.dmm.customize.endpoint.suggestion.prdPurord.PrdPurordRequest;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.sandbox.SandboxSuggestionResponse;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * DMM_Suggestionサービス
 */
@Path("/dmm_suggestion")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class DmmSuggestionEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject private DmmSuggestionService service;

	@Override
	public BaseResponse init(BaseRequest req) {
		throw new BadRequestException("このメソッドはダミーなので呼び出せません");
	}

	/**
	 * 部門情報Suggestion
	 * @return
	 */
	@POST
	@Path("/bumon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SandboxSuggestionResponse suggestUser(DmmSuggestionBumonRequest req) {
		return service.suggestBumon(req);
	}

	/**
	 * 定期発注予定マスタの抽出Suggestion
	 * @return
	 */
	@POST
	@Path("/getPrdPurord")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse getPrdPurord(PrdPurordRequest req) {
		return service.getPrdPurord(req);
	}
}
