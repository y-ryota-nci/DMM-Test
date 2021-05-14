package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.dmm.customize.component.parts.DmmRowToContainerRequest;
import jp.co.dmm.customize.component.parts.DmmRowToContainerService;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomRequest;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomResponse;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomService;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WftInformationSharer;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.FileStreamingOutput;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.tableSearch.TableSearchEntity;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFileRow;
import jp.co.nci.iwf.designer.service.PartsAttachFileService;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.HistoryInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002MasterRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002PartsRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002PartsResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0005Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0005Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0005Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0006Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0008Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0009Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0011Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0011Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0011Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Service;
import jp.co.nci.iwf.endpoint.vd.vd0312.Vd0312Request;
import jp.co.nci.iwf.endpoint.vd.vd0312.Vd0312Response;
import jp.co.nci.iwf.endpoint.vd.vd0312.Vd0312Service;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;
import jp.co.nci.iwf.util.DownloadUtils;

@Path("/vd0310")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0310Endpoint extends BaseEndpoint<Vd0310InitRequest> {

	@Inject
	private Vd0310Service service;
	@Inject
	private Bl0002Service bl0002Service;
	@Inject
	private Bl0003Service bl0003Service;
	@Inject
	private Bl0005Service bl0005Service;
	@Inject
	private Bl0006Service bl0006Service;
	@Inject @SuppressWarnings("unused")
	private Bl0007Service bl0007Service;
	@Inject
	private Bl0008Service bl0008Service;
	@Inject
	private Bl0009Service bl0009Service;
	@Inject
	private Bl0010Service bl0010Service;
	@Inject
	private Bl0011Service bl0011Service;
	@Inject
	private Bl0015Service bl0015Service;
	@Inject
	private PartsAttachFileService partsAttachFileService;
	@Inject
	private Vd0312Service vd0312Service;
	@Inject private DmmCustomService dmmCustomService;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0310InitResponse init(Vd0310InitRequest req) {
		return service.init(req);
	}

	/**
	 * コピー起票可能か.
	 * クライアント側とサーバ（セッション）が保持しているユーザ情報が同一かを判定処理
	 * なおクライアントとサーバ間でログイン者情報が同一化をチェックはAuthenticateEndpointInterceptor内で行われるため、
	 * ここまでたどり着いた時にはチェックはＯＫとして扱ってよい
	 * @param req
	 * @return
	 */
	@POST
	@Path("/canCopy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse canCopy(BaseRequest req) {
		return service.canCopy(req);
	}

	/**
	 * WFアクション実行
	 * @param req
	 * @return
	 */
	@POST
	@Path("/execute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse regist(Vd0310ExecuteRequest req) {
		return service.execute(req);
	}

	/**
	 * （帳票など）コンテンツのダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/doDownload")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doDownload(Vd0310ExecuteRequest req) {
		final FileStreamingOutput stream = service.doDownload(req);
		return DownloadUtils.download(stream.getFileName(), stream);
	}

	/**
	 * ファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/uploadWf")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0003Response uploadWf(FormDataMultiPart multiPart) {
		return bl0003Service.upload(multiPart);
	}

	/**
	 * 添付ファイルブロックからのファイルダウンロード
	 * @param attachFileWfId
	 */
	@POST
	@Path("/downloadWf")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadWf(@QueryParam("attachFileWfId") Long attachFileWfId) {
		return bl0003Service.download(attachFileWfId);
	}

	/**
	 * 添付ファイルブロックからのファイル削除
	 * @param multiPart
	 */
	@POST
	@Path("/deleteWf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0003Response deleteWf(Bl0003Request req) {
		return bl0003Service.delete(req.attachFileWfId);
	}

	/**
	 * 添付ファイルブロックからの添付ファイルリスト取得
	 */
	@GET
	@Path("/getAttachFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<AttachFileWfInfo> getAttachFile(@QueryParam("corporationCode") String corporationCode
			, @QueryParam("processId") Long processId) {
		return bl0003Service.getAttachFileWfList(corporationCode, processId);
	}

	/**
	 * 承認履歴取得
	 */
	@GET
	@Path("/getHistory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<HistoryInfo> getHistory(@QueryParam("corporationCode") String corporationCode
			, @QueryParam("processId") Long processId) {
		return bl0006Service.getHistoryList(corporationCode, processId);
	}

	/**
	 * ルート情報取得
	 */
	@POST
	@Path("/getRoute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0312Response getRoute(Vd0312Request req) {
		return vd0312Service.init(req);
	}

	/**
	 * デフォルト参照者取得
	 */
	@GET
	@Path("/getInformationSharerDef")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<WfmInformationSharerDef> getInformationSharerDef(
			@QueryParam("corporationCode") String corporationCode,
			@QueryParam("processDefCode") String processDefCode,
			@QueryParam("processDefDetailCode") String processDefDetailCode,
			@QueryParam("processId") Long processId) {
		return bl0008Service.getInformationSharerDefList(corporationCode, processDefCode, processDefDetailCode, processId);
	}

	/**
	 * 参照者取得
	 */
	@GET
	@Path("/getInformationSharer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<WftInformationSharer> getInformationSharer(
			@QueryParam("corporationCode") String corporationCode,
			@QueryParam("processId") Long processId) {
		return bl0009Service.getInformationSharerList(corporationCode, processId);
	}

	/**
	 * コンテナへの空行追加
	 */
	@POST
	@Path("/addEmptyLine")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse addEmptyLine(Bl0002PartsRequest req) {
		return bl0002Service.addEmptyLine(req);
	}

	/**
	 * コンテナへの行削除
	 */
	@POST
	@Path("/deleteLine")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse deleteLine(Bl0002PartsRequest req) {
		return bl0002Service.deleteLine(req);
	}

	/**
	 * コンテナへの行コピー
	 */
	@POST
	@Path("/copyLine")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse copyLine(Bl0002PartsRequest req) {
		return bl0002Service.copyLine(req);
	}

	/**
	 * コンテナへの行数変更
	 */
	@POST
	@Path("/changeLineCount")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse changeLineCount(Bl0002PartsRequest req) {
		return bl0002Service.changeLineCount(req);
	}

	/**
	 * コンテナのページ番号の変更
	 */
	@POST
	@Path("/changePageNo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse changePageNo(Bl0002PartsRequest req) {
		return bl0002Service.changePageNo(req);
	}

	/**
	 * マスタ選択パーツの選択肢を抽出
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getMasterOptionItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> getMasterOptionItem(Bl0002MasterRequest req) {
		return bl0002Service.getMasterOptionItem(req);
	}

	/**
	 * マスタ選択パーツを選択したことによる検索
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getMasterResult")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<TableSearchEntity> getMasterResult(Bl0002MasterRequest req) {
		return bl0002Service.getMasterResult(req);
	}

	/**
	 * ワークフローパーツ添付ファイルの削除
	 */
	@POST
	@Path("/deletePartsAttachFileWf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse deletePartsAttachFileWf(Bl0002PartsRequest req) {
		return bl0002Service.deletePartsAttachFile(req);
	}

	/**
	 * ワークフローパーツ添付ファイルの追加
	 */
	@POST
	@Path("/addPartsAttachFileWf")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public List<PartsAttachFileRow> addPartsAttachFile(
			@FormDataParam("partsId") Long partsId,
			@FormDataParam("multiple") boolean multiple,
			@FormDataParam("maxFileCount") Integer maxFileCount,
			@FormDataParam("fileRegExp") String fileRegExp,
			@FormDataParam("file") List<FormDataBodyPart> bodyParts) {
		return bl0002Service.addPartsAttachFileWf(partsId, multiple, maxFileCount, fileRegExp, bodyParts);
	}

	/**
	 * パーツの再描画用HTML取得
	 */
	@POST
	@Path("/refreshParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse refreshParts(Bl0002PartsRequest req) {
		return bl0002Service.refreshParts(req);
	}

	/**
	 * 決裁関連文書ブロック情報を取得
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/getApprovalRelationList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0005Response getApprovalRelationList(Bl0005Request req) {
		return bl0005Service.getApprovalRelationList(req);
	}

	/**
	 * 要説明(掲示板)ブロック情報を取得
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/getProcessBbsList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0010Response getProcessBbsList(Bl0010Request req) {
		return bl0010Service.getProcessBbs(req);
	}

	/**
	 * 要説明(掲示板)ブロックの記事を削除
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/deleteProcessBbs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0010Response deleteProcessBbs(Bl0010Request req) {
		return bl0010Service.delete(req);
	}

	/**
	 * 要説明(掲示板)ブロックの記事を新規投稿
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/submitProcessBbs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0010Response submitProcessBbs(Bl0010Request req) {
		return bl0010Service.submit(req);
	}

	/**
	 * メモブロック情報を取得
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/getProcessMemoList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0011Response getProcessMemoList(Bl0011Request req) {
		return bl0011Service.getProcessMemo(req);
	}

	/**
	 * メモブロックの記事を新規投稿
	 * @param corporationCode
	 * @param processId
	 * @return
	 */
	@POST
	@Path("/submitProcessMemo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0011Response submitProcessMemo(Bl0011Request req) {
		return bl0011Service.submitProcessMemo(req);
	}

	/**
	 * 添付ファイルパーツで添付ファイルのファイルダウンロード
	 * @param partsAttachFileWfId ワークフローパーツ添付ファイルID
	 */
	@GET
	@Path("/download/partsAttachFileWf")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadPartsAttachFileWf(@QueryParam("partsAttachFileWfId") Long partsAttachFileWfId) {
		// 申請時に添付ファイルパーツで添付したファイル
		// プロセスIDに依存する
		MwtPartsAttachFileWf entity = partsAttachFileService.getMwtPartsAttachFileWfByPK(partsAttachFileWfId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

	/**
	 * ハイパーリンクパーツで「添付ファイルをアップロードしてリンク」を押下したときのファイルダウンロード
	 * @param attachFileWfId
	 */
	@GET
	@Path("/download/partsAttachFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadPartsAttachFile(@QueryParam("partsAttachFileId") Long partsAttachFileId) {
		// パーツデザイン時にハイパーリンクパーツで添付したファイル
		// プロセスIDに依存しない
		MwmPartsAttachFile entity = partsAttachFileService.getMwmPartsAttachFileByPK(partsAttachFileId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

	/**
	 * プロセス掲示板情報添付ファイルで添付ファイルのファイルダウンロード
	 * @param partsAttachFileWfId ワークフローパーツ添付ファイルID
	 */
	@GET
	@Path("/download/bbsAttachFileWf")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadBbsAttachFileWf(@QueryParam("bbsAttachFileWfId") Long bbsAttachFileWfId) {
		// 申請時に添付ファイルパーツで添付したファイル
		// プロセスIDに依存する
		MwtBbsAttachFileWf entity = bl0010Service.download(bbsAttachFileWfId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

	/**
	 * 全パーツを再描画する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/redrawAllParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse redrawAllParts(Bl0002PartsRequest req) {
		return bl0002Service.redrawAllParts(req);
	}

	/**
<<<<<<< HEAD
	 * 行データをコンテナへ反映
	 * @param req
	 * @return
	 */
	@POST
	@Path("/rowsToContainer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0002PartsResponse rowsToContainer(DmmRowToContainerRequest req) {
		DmmRowToContainerService service = CDI.current().select(DmmRowToContainerService.class).get();
		return service.rowsToContainer(req);
	}

	/**
	 * 文書ファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/uploadWfDocFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0015Response uploadWfDocFile(FormDataMultiPart multiPart) {
		return bl0015Service.upload(multiPart);
	}

	/**
	 * 文書ファイルブロックからのファイルダウンロード
	 * @param attachFileWfId
	 */
	@POST
	@Path("/downloadWfDocFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadWfDocFile(@QueryParam("docFileWfId") Long docFileWfId) {
		return bl0015Service.download(docFileWfId);
	}

	/**
	 * 文書ファイルブロックからのファイル削除
	 * @param multiPart
	 */
	@POST
	@Path("/deleteWfDocFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bl0015Response deleteWfDocFile(Bl0015Request req) {
		return bl0015Service.delete(req.docFileWfId);
	}

	/**
	 * 文書ファイルブロックからの文書ファイルリスト取得
	 */
	@GET
	@Path("/getDocFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocFileWfInfo> getDocFile(@QueryParam("corporationCode") String corporationCode
			, @QueryParam("processId") Long processId) {
		return bl0015Service.getDocFileWfList(corporationCode, processId);
	}

	/**
	 * DMM用カスタムリクエストの処理を行う
	 * @return
	 */
	@POST
	@Path("/getDmmCustomResponse")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DmmCustomResponse getDmmCustomResponse(DmmCustomRequest req) {
		if (isEmpty(req.methodName))
			throw new BadRequestException("メソッド名が未指定です");

		try {
			final Method m = dmmCustomService.getClass().getMethod(req.methodName, DmmCustomRequest.class);
			if (m == null)
				throw new BadRequestException("サービスに該当するメソッドがありません。methodName=" + req.methodName);

			return (DmmCustomResponse)m.invoke(dmmCustomService, req);
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
