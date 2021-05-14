package jp.co.nci.iwf.endpoint.vd.vd0030;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 画面一覧Endpoint
 */
@Path("/vd0030")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0030Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Vd0030Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0030InitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0030SearchResponse search(Vd0030SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Vd0030Request req) {
		return service.delete(req);
	}

	/**
	 * 対象画面の画面定義をZIPファイルとしてダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/downloadZip")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadZip(Vd0030Request req) {
		String fileName = null;
		if (req.screenIds.size() == 1) {
			// 単一画面ではファイル名に画面コードを含める
			final MwmScreen s = service.getMwmScreen(req.screenIds.get(0));
			fileName = String.format("%s_%s.zip", s.getScreenCode(), s.getScreenName());
		}
		else {
			// 複数画面はファイル名に画面コードを含めない
			final String yyyyMMdd = toStr(now(), "yyyyMMdd");
			fileName = String.format("SCREEN_%s.zip", yyyyMMdd);
		}
		return DownloadUtils.download(fileName, service.downloadZip(req));
	}

	/**
	 * 画面定義をEXCEL形式でダウンロード
	 */
	@POST
	@Path("/downloadExcel")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadExcel(Vd0030Request req) {
		final Long screenId = req.screenIds.get(0);
		if (screenId == null)
			throw new BadRequestException("コンテナIDが未指定です");

		final MwmScreen screen = service.get(screenId);
		if (screen == null)
			throw new NotFoundException("画面情報が見つかりません。screenId=" + screenId);

		final String fileName = screen.getScreenCode() + "_" + screen.getScreenName() + ".xlsx";
		return DownloadUtils.download(fileName, service.downloadExcel(screenId));
	}
}
