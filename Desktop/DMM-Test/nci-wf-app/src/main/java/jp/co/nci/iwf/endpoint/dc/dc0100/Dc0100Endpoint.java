package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.document.AttachFileDocRequest;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.component.document.DocInfoOperationRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.UpdateHistoryInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0004Request;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0004Response;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0004Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0005Request;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0005Response;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0005Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0006Request;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0006Response;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0006Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0007Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0008Request;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0008Response;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0008Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0010Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0011Request;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0011Response;
import jp.co.nci.iwf.endpoint.dc.dc0100.include.DcBl0011Service;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 業務文書登録・更新Endpoint.
 */
@Path("/dc0100")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0100Endpoint extends BaseEndpoint<Dc0100InitRequest> {


	@Inject private Dc0100Service service;
//	/** 業務文書ブロックサービス */
//	@Inject private DcBl0001Service bl0001Service;
//	/** 文書内容ブロックサービス */
//	@Inject private Bl0002Service bl0002Service;
//	/** 文書属性ブロックサービス */
//	@Inject private DcBl0003Service bl0003Service;
	/** 権限設定ブロックサービス */
	@Inject private DcBl0004Service bl0004Service;
	/** 文書属性(拡張)ブロックサービス */
	@Inject private DcBl0005Service bl0005Service;
	/** 文書ファイルブロックサービス */
	@Inject private DcBl0006Service bl0006Service;
	/** 更新履歴ブロックサービス */
	@Inject private DcBl0007Service bl0007Service;
	/** メモ情報ブロックサービス */
	@Inject private DcBl0008Service bl0008Service;
	/** 添付ファイルブロックサービス */
	@Inject private DcBl0010Service bl0010Service;
	/** WF連携ブロックサービス */
	@Inject private DcBl0011Service bl0011Service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0100InitResponse init(Dc0100InitRequest req) {
		return service.init(req);
	}

	/**
	 * ロック処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/lock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lock(DocInfoOperationRequest req) {
		return service.lock(req);
	}

	/**
	 * ロック解除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/unlock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlock(DocInfoOperationRequest req) {
		return service.unlock(req);
	}

	/**
	 * 文書情報の登録／更新処理.
	 * @param req
	 * @return
	 */
	@POST
	@Path("/execute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse execute(Dc0100ExecuteRequest req) {
		return service.execute(req);
	}

	/**
	 * 文書情報からWF申請処理.
	 * @param req
	 * @return
	 */
	@POST
	@Path("/applyWf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse applyWf(Dc0100ExecuteRequest req) {
		// リクエストのWF連携フラグをONに設定
		req.wfApplying = CommonFlag.ON;
		// ロックは常に解除する
		req.versionInfo.unlockFlag = CommonFlag.ON;
		// 文書情報の更新処理を行う
		// その際、内部でWF連携処理が実行される
		return service.execute(req);
	}

	/**
	 * 文書フォルダ変更に伴う文書権限情報取得.
	 * (リクエストの文書フォルダIDに該当する文書フォルダが持つ権限情報を返す)
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getAccessibles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0004Response getAccessibles(DcBl0004Request req) {
		final DcBl0004Response res = bl0004Service.getAccessiblesByFolderId(req);
		// 文書フォルダに対してメタテンプレートが指定してあったら紐づくメタ項目一覧を取得する
		if (res.folder.getMetaTemplateId() != null) {
			final DcBl0005Request req2 = new DcBl0005Request();
			req2.corporationCode = req.corporationCode;
			req2.metaTemplateId = res.folder.getMetaTemplateId();
			final DcBl0005Response res2 = bl0005Service.getAttributeExListByMetaTemplateId(req2);
			res.attributeExs = res2.attributeExs;
		}
		return res;
	}

	/**
	 * メタテンプレート変更に伴う文書属性(拡張)情報取得.
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getAttributeExs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0005Response getAttributeExs(DcBl0005Request req) {
		return bl0005Service.getAttributeExListByMetaTemplateId(req);
	}

	/**
	 * 文書ファイル一覧取得.
	 */
	@POST
	@Path("/getDocFiles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0006Response getDocFiles(DcBl0006Request req) {
		return bl0006Service.getDocFiles(req);
	}

	/**
	 * 文書ファイルデータアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload/docFileData")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse upload(FormDataMultiPart multiPart) {
		return bl0006Service.upload(multiPart);
	}

	/**
	 * 文書ファイルデータダウンロード
	 * @param attachFileWfId
	 */
	@POST
	@Path("/download/docFileData")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("docFileDataId") Long docFileDataId) {
		return bl0006Service.download(docFileDataId);
	}

	/**
	 * 文書ファイルロック処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/lockFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lockFile(DocFileOperationRequest req) {
		return bl0006Service.lock(req);
	}

	/**
	 * 文書ファイルロック解除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/unlockFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlockFile(DocFileOperationRequest req) {
		return bl0006Service.unlock(req);
	}

	/**
	 * 文書ファイル削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/deleteFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse deleteFile(DocFileOperationRequest req) {
		return bl0006Service.delete(req);
	}

	/**
	 * 文書ファイル取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse getFile(DocFileOperationRequest req) {
		return bl0006Service.getDocFile(req);
	}

	/**
	 * 文書ファイル移動取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/moveFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse moveFile(DocFileOperationRequest req) {
		return bl0006Service.move(req);
	}

	/**
	 * 文書ファイルコピー取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/copyFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse copyFile(DocFileOperationRequest req) {
		return bl0006Service.copy(req);
	}

//	/**
//	 * 添付ファイルブロックからのファイル削除
//	 * @param multiPart
//	 */
//	@POST
//	@Path("/deleteWf")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Bl0003Response deleteWf(Bl0003Request req) {
//		return bl0003Service.delete(req.attachFileWfId);
//	}

	/**
	 * 文書更新履歴取得
	 */
	@GET
	@Path("/getHistory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<UpdateHistoryInfo> getHistory(@QueryParam("docId") Long docId) {
		return bl0007Service.getHistoryList(docId);
	}

	/**
	 * メモブロック情報を取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getDocMemo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0008Response getDocMemo(DcBl0008Request req) {
		return bl0008Service.getDocMemo(req);
	}

	/**
	 * メモブロックの記事を新規投稿
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addDocMemo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0008Response addDocMemo(DcBl0008Request req) {
		return bl0008Service.addDocMemo(req);
	}

	/**
	 * 添付ファイルデータアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload/attachFileData")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse uploadAttachFile(FormDataMultiPart multiPart) {
		return bl0010Service.upload(multiPart);
	}

	/**
	 * 添付ファイルデータダウンロード
	 * @param attachFileWfId
	 */
	@POST
	@Path("/download/attachFileData")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadAttachFIle(@QueryParam("docFileDataId") Long docFileDataId) {
		return bl0010Service.download(docFileDataId);
	}

	/**
	 * 添付ファイル削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/deleteAttachFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse deleteAttachFile(AttachFileDocRequest req) {
		return bl0010Service.delete(req);
	}

	/**
	 * 対象プロセスIDで操作者がアクセス可能な最新のアクティビティ(アクティビティID)を取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getAccessibleActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DcBl0011Response getAccessibleActivity(DcBl0011Request req) {
		return bl0011Service.getAccessibleActivity(req);
	}
}
