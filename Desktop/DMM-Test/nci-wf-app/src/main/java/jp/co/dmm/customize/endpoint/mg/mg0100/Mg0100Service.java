package jp.co.dmm.customize.endpoint.mg.mg0100;

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
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadService;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgExcelBookBnk;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgExcelReaderBnk;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgExcelSheetBnk;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgUploadSOBnk;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgUploadValidatorBnk;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;

@ApplicationScoped
public class Mg0100Service extends MgUploadService  {


	@Inject private Mg0100Repository repository;
	@Inject private MgUploadValidatorBnk validator;
	@Inject private MgExcelReaderBnk reader;
	@Inject private Logger log;

	// アップロードファイルデータマップ
	Map<String, MgExcelBookBnk> bookMap = new HashMap<String, MgExcelBookBnk>();

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0100SearchResponse init(Mg0100SearchRequest req) {
		final Mg0100SearchResponse res = createResponse(Mg0100SearchResponse.class, req);
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
	public Mg0100SearchResponse search(Mg0100SearchRequest req) {

		LoginInfo u = sessionHolder.getLoginInfo();

		if (StringUtils.isEmpty(req.companyCd)) {
			req.companyCd = u.getCorporationCode();
		}

		final int allCount = repository.count(req);
		final Mg0100SearchResponse res = createResponse(Mg0100SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	public Mg0100Response delete(Mg0100Request req) {
		final Mg0100Response res = createResponse(Mg0100Response.class, req);
		int delTotalCnt = repository.delete(req);
		res.success = (delTotalCnt > 0);
		return res;
	}

	/**
	 * テンプレートのダウンロード
	 * @return
	 */
	@Override
	public Response download(BasePagingRequest req) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = u.getCorporationCode();
		final String fileName = MessageFormat.format("銀行マスタ_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));

		MgExcelBookBnk book = new MgExcelBookBnk();

		book.sheet = new MgExcelSheetBnk();
		book.sheet.entityList = repository.getMasterData((Mg0100SearchRequest) req);

		final MgUploadSOBnk streaming = new MgUploadSOBnk(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}

	/**
	 * 銀行情報をアップロード
	 * @param multiPart
	 * @return
	 */
	@Override
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
					MgExcelBookBnk book = new MgExcelBookBnk();
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
						MgUploadSOBnk streaming = new MgUploadSOBnk(book);
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

	/**
	 * 銀行情報アップロードファイルをDBに登録
	 * @param multiPart
	 * @return
	 */
	@Override
	public Response register(MgUploadSaveRequest req) {
		try {
			// アップロードファイルをパースしてBookを復元
			final MgExcelBookBnk book = bookMap.get(req.encoded);
			bookMap.remove(req.encoded);

			// バリデーション
			if (validator.validate(book)) {

				// 銀行情報の登録
				repository.uploadRegist(book, sessionHolder.getWfUserRole());

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.splrInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				MgUploadSOBnk streaming = new MgUploadSOBnk(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private void parse(InputStream stream, MgExcelBookBnk book) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		reader.parse(stream, book);

		// マスタの存在チェック用に、処理前から存在していたコード値を保存しておく
		repository.getUploadMasterCdInfo(book);

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

	}
}
