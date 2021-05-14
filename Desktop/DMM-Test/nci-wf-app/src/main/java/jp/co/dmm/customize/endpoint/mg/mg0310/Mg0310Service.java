package jp.co.dmm.customize.endpoint.mg.mg0310;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.EmptyFileException;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;

import com.gh.mygreen.xlsmapper.XlsMapperException;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadResponse;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadSaveRequest;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgExcelBookZip;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgExcelReaderZip;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgExcelSheetZip;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgUploadSOZip;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgUploadValidatorZip;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;

@ApplicationScoped
public class Mg0310Service extends BasePagingService  {


	@Inject private Mg0310Repository repository;
	@Inject private MgUploadValidatorZip validator;
	@Inject private MgExcelReaderZip reader;
	@Inject private Logger log;
	@Inject private SessionHolder sessionHolder;

	// アップロードファイルデータマップ
	Map<String, MgExcelBookZip> bookMap = new HashMap<String, MgExcelBookZip>();

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0310SearchResponse init(Mg0310SearchRequest req) {
		final Mg0310SearchResponse res = createResponse(Mg0310SearchResponse.class, req);
		LoginInfo u = sessionHolder.getLoginInfo();
		res.companyCd = u.getCorporationCode();
		res.companyItems = repository.getCompanyItems(u.getUserAddedInfo(), u.getLocaleCode());
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mg0310SearchResponse search(Mg0310SearchRequest req) {
		final int allCount = repository.count(req);
		final Mg0310SearchResponse res = createResponse(Mg0310SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	public Mg0310RemoveResponse delete(Mg0310RemoveRequest req) {
		final Mg0310RemoveResponse res = createResponse(Mg0310RemoveResponse.class, req);
		int delTotalCnt = repository.delete(req);
		res.success = (delTotalCnt > 0);
		return res;
	}

	public Response download(Mg0310SearchRequest req) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = u.getCorporationCode();
		final String fileName = MessageFormat.format("郵便番号マスタ_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));

		MgExcelBookZip book = new MgExcelBookZip();

		book.sheet = new MgExcelSheetZip();
		book.sheet.entityList = repository.getMasterData((Mg0310SearchRequest) req);

		final MgUploadSOZip streaming = new MgUploadSOZip(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}

	public Response upload(FormDataMultiPart multiPart) {
		final MgUploadResponse res = createResponse(MgUploadResponse.class, null);

		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				if (StringUtils.isEmpty(f.fileName) || !f.fileName.trim().endsWith(".xlsx")) {
					// アップロード可能なファイルはExcel文書だけです。
					res.addAlerts(i18n.getText(MessageCd.MSG0208, MessageCd.excel));
					res.success = false;
					return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
				}

				// アップロードファイルをバイト配列化
				final byte[] xlsBytes = IOUtils.toByteArray(f.stream);

				try (ByteArrayInputStream bais = new ByteArrayInputStream(xlsBytes)) {
					// アップロードファイルをパースしてEXCELBook生成
					MgExcelBookZip book = new MgExcelBookZip();
					parse(bais, book);

					//nullチェック sheet名が規定と異なるとnullになるため
					if (book == null || book.sheet == null || book.sheet.entityList == null) {
						// シート名が正しくありません。
						res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.sheetName));
						res.success = false;
						return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
					}

					// アップロードされたEXCELファイルに対してバリデーションを実施し、
					// エラー無しならアップロード内容をいったん画面へ送り返す。エラーがあればエラー内容をEXCELファイルとして送り返す
					if (validator.validate(book)) {

						// セッションに保存
						String bookMapSessionKey = sessionHolder.getLoginInfo().getCorporationCode() + "_"
								+ sessionHolder.getLoginInfo().getUserCode() + "_"
								+ sessionHolder.getWfUserRole().getIpAddress() + "_"
								+ System.currentTimeMillis();
						bookMap.put(bookMapSessionKey, book);

						// アップロード内容をBASE64エンコードで文字列化し、クライアントへ返す
						// ※これが出来るのは、アップロードファイルがせいぜい30KB程度にしかならないと分かっているから。
						// ※もし別処理でアップロードファイルをBASE64エンコードしてクライアントへ返そうと考えているなら、
						// ※ファイルの最大サイズを考慮せよ。もし100KBを超える可能性があるなら別方式を選ぶべし。
						res.encoded = bookMapSessionKey;
						res.fileName = f.fileName;

						// EXCELファイルに含まれていたマスタの件数
						res.count = book.sheet.entityList.stream().count();
						res.success = true;

						return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
					}
					else {
						// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
						MgUploadSOZip streaming = new MgUploadSOZip(book);
						return DownloadUtils.download(f.fileName, streaming, ContentType.XLSX);
					}
				}
			}
			catch (XlsMapperException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}catch (EmptyFileException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		}
		// ファイル形式が正しくありません
		res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
		res.success = false;
		return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	public Response register(MgUploadSaveRequest req) {
		try {
			// アップロードファイルをパースしてBookを復元
			final MgExcelBookZip book = bookMap.get(req.encoded);
			bookMap.remove(req.encoded);

			// バリデーション
			if (validator.validate(book)) {

				// 郵便番号情報の登録
				repository.uploadRegist(book, sessionHolder.getWfUserRole());

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.splrInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				MgUploadSOZip streaming = new MgUploadSOZip(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private void parse(InputStream stream, MgExcelBookZip book) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		reader.parse(stream, book);

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

	}
}
