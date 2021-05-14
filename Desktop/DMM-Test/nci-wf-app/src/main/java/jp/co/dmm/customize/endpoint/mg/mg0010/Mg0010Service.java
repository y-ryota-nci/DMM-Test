package jp.co.dmm.customize.endpoint.mg.mg0010;

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
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgExcelBookItm;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgExcelReaderItm;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgExcelSheetItm;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgUploadSOItm;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgUploadValidatorItm;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;

@ApplicationScoped
public class Mg0010Service extends MgUploadService  {


	@Inject private Mg0010Repository repository;
	@Inject private MgUploadValidatorItm validator;
	@Inject private MgExcelReaderItm reader;
	@Inject private Logger log;

	// アップロードファイルデータマップ
	Map<String, MgExcelBookItm> bookMap = new HashMap<String, MgExcelBookItm>();

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0010SearchResponse init(Mg0010SearchRequest req) {
		final Mg0010SearchResponse res = createResponse(Mg0010SearchResponse.class, req);
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
	public Mg0010SearchResponse search(Mg0010SearchRequest req) {

		LoginInfo u = sessionHolder.getLoginInfo();

		if (StringUtils.isEmpty(req.companyCd)) {
			req.companyCd = u.getCorporationCode();
		}

		final int allCount = repository.count(req);
		final Mg0010SearchResponse res = createResponse(Mg0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	public Mg0010Response delete(Mg0010Request req) {
		final Mg0010Response res = createResponse(Mg0010Response.class, req);
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
		final String fileName = MessageFormat.format("品目マスタ_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));

		MgExcelBookItm book = new MgExcelBookItm();

		book.sheet = new MgExcelSheetItm();
		book.sheet.entityList = repository.getMasterData((Mg0010SearchRequest) req);

		final MgUploadSOItm streaming = new MgUploadSOItm(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}

	/**
	 * 品目情報をアップロード
	 * @param multiPart
	 * @return
	 */
	@Override
	public Response upload(FormDataMultiPart multiPart) {
		// 2019/11/05 Excelアップロードバグの対応
		//
		final MgUploadResponse res = createResponse(MgUploadResponse.class, null);

		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				// 2019/11/05 Excelアップロードバグの対応
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
					MgExcelBookItm book = new MgExcelBookItm();
					parse(bais, book);

					// 2019/11/05 Excelアップロードバグの対応
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
						MgUploadSOItm streaming = new MgUploadSOItm(book);
						return DownloadUtils.download(f.fileName, streaming, ContentType.XLSX);
					}
				}
			}
			// 2019/11/05 Excelアップロードバグの対応
			catch (XlsMapperException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
			// 2019/11/05 Excelアップロードバグの対応
			catch (EmptyFileException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		}
		// 2019/11/05 Excelアップロードバグの対応
		// ファイル形式が正しくありません
		res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
		res.success = false;
		return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	/**
	 * 品目情報アップロードファイルをDBに登録
	 * @param multiPart
	 * @return
	 */
	@Override
	public Response register(MgUploadSaveRequest req) {
		try {
			// アップロードファイルをパースしてBookを復元
			final MgExcelBookItm book = bookMap.get(req.encoded);
			bookMap.remove(req.encoded);

			// バリデーション
			if (validator.validate(book)) {

				// 品目情報の登録
				repository.uploadRegist(book, sessionHolder.getWfUserRole());

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.splrInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				MgUploadSOItm streaming = new MgUploadSOItm(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private void parse(InputStream stream, MgExcelBookItm book) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		reader.parse(stream, book);

		// マスタの存在チェック用に、処理前から存在していたコード値を保存しておく
		repository.getUploadMasterCdInfo(book);

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

	}

}
