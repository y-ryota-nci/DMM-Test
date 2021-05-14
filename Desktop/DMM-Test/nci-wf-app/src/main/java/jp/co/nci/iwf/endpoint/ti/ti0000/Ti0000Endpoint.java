package jp.co.nci.iwf.endpoint.ti.ti0000;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.tableSearch.TableSearchInitRequest;
import jp.co.nci.iwf.component.tableSearch.TableSearchInitResponse;
import jp.co.nci.iwf.component.tableSearch.TableSearchRequest;
import jp.co.nci.iwf.component.tableSearch.TableSearchResponse;
import jp.co.nci.iwf.component.tableSearch.TableSearchService;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 汎用テーブル検索画面（パーツ呼び出し）Endpoint
 */
@Path("/ti0000")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0000Endpoint extends BaseEndpoint<TableSearchInitRequest> {
	@Inject private TableSearchService service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public TableSearchInitResponse init(TableSearchInitRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TableSearchResponse search(TableSearchRequest req) {
		return service.search(req);
	}
}
