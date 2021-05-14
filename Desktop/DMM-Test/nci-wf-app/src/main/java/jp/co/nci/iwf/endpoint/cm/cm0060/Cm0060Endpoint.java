package jp.co.nci.iwf.endpoint.cm.cm0060;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.integrated_workflow.model.custom.WfProcessableActivity;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 関連文書選択画面Endpoint
 */
@Path("/cm0060")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Cm0060Endpoint extends BaseEndpoint<Cm0060InitRequest> {
	@Inject private Cm0060Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cm0060InitResponse init(Cm0060InitRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cm0060SearchResponse search(Cm0060SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 対象プロセスIDで操作者がアクセス可能な最新の履歴情報を取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getAccessibleActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<WfProcessableActivity> getAccessibleActivity(Cm0060SearchRequest req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.processId == null)
			throw new BadRequestException("プロセスIDが未指定です");
		if (req.trayType == null)
			throw new BadRequestException("トレイタイプが未指定です");
		return service.getAccessibleActivity(req.corporationCode, req.processId, req.trayType);
	}
}
