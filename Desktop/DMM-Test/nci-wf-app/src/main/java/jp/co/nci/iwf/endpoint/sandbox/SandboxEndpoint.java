package jp.co.nci.iwf.endpoint.sandbox;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 実験画面のEndPoint
 */
@Path("/sandbox")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class SandboxEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject
	private SandboxService service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public SandboxResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * CSVダウンロード
	 */
	@POST
	@Path("/downloadAccessLog")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadAccessLog() {
		return DownloadUtils.download("さんぷる.csv", service.downloadCsv());
	}

	/**
	 * PDFダウンロード
	 */
	@POST
	@Path("/downloadPDF")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadPDF() {
		return DownloadUtils.preview("さんぷる.pdf", service.downloadPDF(), ContentType.PDF);
	}

	/**
	 * メール送信
	 * @param req
	 * @return
	 */
	@POST
	@Path("/sendMail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse sendMail(SandboxRequest req) {
		return service.sendMail(req);
	}

	/**
	 * ファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * サーバ側でのバリデーション表示用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/validationError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse validationError(BaseRequest req) {
		return service.validationError(req);
	}

	/**
	 * サーバ側でのシステム表示用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/systemError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse systemError(BaseRequest req) {
		throw new InternalServerErrorException("システムエラー表示用のサンプルとして、例外をスローしました。");
	}

	/**
	 * サーバ側でのコンテンツが見つからない場合の表示用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/notFoundError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse notFoundError(BaseRequest req) {
		throw new NotFoundException("コンテンツが見つからない場合のサンプルとして、例外をスローしました。");
	}

	/**
	 * 排他ロックエラーの表示用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/alreadyUpdatedError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse alreadyUpdatedError(BaseRequest req) {
		throw new AlreadyUpdatedException("排他ロックエラーのサンプルとして、例外をスローしました。");
	}

	/**
	 * サービス利用不可エラー用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/serviceUnavailableError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse serviceUnavailableError(BaseRequest req) {
		throw new ServiceUnavailableException("サービス利用不可エラーのサンプルとして、例外をスローしました。");
	}

	/**
	 * 操作権限なしエラー用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/forbiddenError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse forbiddenError(BaseRequest req) {
		throw new ForbiddenException("操作権限なしエラーのサンプルとして、例外をスローしました。");
	}

	/**
	 * バリデーション例外用テスト
	 * @param multiPart
	 */
	@POST
	@Path("/validationException")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse validationException(BaseRequest req) {
		throw new InvalidUserInputException(MessageCd.MSG0062, MessageCd.action);
	}

	/**
	 * 空のEXCEL入力フォームをダウンロード
	 */
	@POST
	@Path("/downloadExcelForm")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadExcelForm() {
		return DownloadUtils.download("Excel入力フォーム.xlsx", service.downloadExcelForm());
	}

	/**
	 * 入力済みEXCELフォームをアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/uploadExcelForm")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public SandboxUploadExcelResponse uploadExcelForm(FormDataMultiPart multiPart) {
		return service.uploadExcelForm(multiPart);
	}

	/**
	 * EXCELテンプレートにデータを差し込んでからダウンロード(セル結合あり)
	 */
	@POST
	@Path("/downloadExcel2")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadExcel2() {
		return DownloadUtils.download("セル結合あり.xlsx", service.downloadExcel2());
	}

	/**
	 * リクエストタイムアウト検証用
	 * @param req
	 * @return
	 */
	@POST
	@Path("/verifyTimeout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse verifyTimeout(SandboxRequest req) {
		return service.verifyTimeout(req);
	}
}
