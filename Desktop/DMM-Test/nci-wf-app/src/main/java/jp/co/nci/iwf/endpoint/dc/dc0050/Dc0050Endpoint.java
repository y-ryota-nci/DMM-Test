package jp.co.nci.iwf.endpoint.dc.dc0050;

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
 * 文書フォルダ設定Endpoint
 */
@Path("/dc0050")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0050Endpoint extends BaseEndpoint<Dc0050InitRequest> {

	@Inject
	private Dc0050Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0050InitResponse init(Dc0050InitRequest req) {
		return service.init(req);
	}

//	@GET
//	@Path("/getTreeItems")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<DocFolderTreeItem> getTreeItems(@QueryParam("nodeId") String nodeId) {
//		return service.getTreeItems(nodeId);
//	}

	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0050EditResponse edit(Dc0050EditRequest req) {
		return service.edit(req);
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0050SaveResponse save(Dc0050SaveRequest req) {
		return service.save(req);
	}

	@POST
	@Path("/move")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0050MoveResponse save(Dc0050MoveRequest req) {
		return service.move(req);
	}

}
