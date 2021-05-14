package jp.co.nci.iwf.endpoint.vd.vd0330;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 申請画面(参照)Endpoint。
 * 各プロジェクトのカスタマイズ要件で使用され、別途、何らかの一覧画面から遷移する想定である。
 * 非画面デザイナー管理しているテーブルからデータを抽出して、画面デザイナーの描画エンジンに食わせて画面表示を行う。
 * 実際上は画面カスタマイズクラス経由でユーザデータを抽出し、それを画面デザイナーの描画エンジンに食わせてレンダリングを行う。
 */
@Path("/vd0330")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0330Endpoint extends BaseEndpoint<Vd0330Request> {
	@Inject private Vd0330Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0330Response init(Vd0330Request req) {
		return service.init(req);
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Vd0310ExecuteRequest req) {
		return service.update(req);
	}
}
