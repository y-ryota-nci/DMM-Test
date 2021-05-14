package jp.co.nci.iwf.endpoint.sandbox.sql;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * サンドボックスSQL画面Endpoint
 */
@Path("/sandboxSql")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class SandboxSqlEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject private SandboxSqlService service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
		return service.init(req);
	}

	@POST
	@Path("/executeSql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse executeSql(SandboSqlRequest req) {
		return service.executeSql(req);
	}

	/**
	 * CSVダウンロード
	 */
	@POST
	@Path("/downloadCsv")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadCsv(SandboSqlRequest req) {
		final String name = "SQL_" + toStr(today(), "yyyyMMdd_hhmmss") + ".csv";
		return DownloadUtils.download(name, service.downloadCsv(req));
	}
}
