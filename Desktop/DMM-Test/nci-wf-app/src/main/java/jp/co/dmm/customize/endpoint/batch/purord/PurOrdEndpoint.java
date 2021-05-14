package jp.co.dmm.customize.endpoint.batch.purord;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.IRequest;

/**
 * 定期発注バッチ処理Endpoint
 */
@Path("/purord")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class PurOrdEndpoint implements IRequest {

	@Inject private PurOrdService service;

	/**
	 * 処理
	 */
	@POST
	@Path("/process")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PurOrdResponse processStart(PurOrdRequest req) {

		// バッチ処理開始
		return service.process(req);
	}

}
