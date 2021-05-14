package jp.co.dmm.customize.endpoint.mg.mg0221;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 支払業務マスタ編集画面Endpoint
 */
@Path("/mg0221")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0221Endpoint extends BaseEndpoint<Mg0221GetRequest> {

	@Inject private Mg0221Service service;

	/**
	 * 支払業務マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0221GetResponse init(Mg0221GetRequest req) {
		return service.init(req);
	}

	/**
	 * 支払業務マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0221UpdateResponse update(Mg0221UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 支払業務マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0221UpdateResponse insert(Mg0221UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 支払業務マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0221GetRequest req) {
		return service.insertCheck(req);
	}

}
