package jp.co.dmm.customize.endpoint.md.md0010;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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

import jp.co.dmm.customize.endpoint.md.MdExcelBook;
import jp.co.dmm.customize.endpoint.md.MdExcelReader;
import jp.co.dmm.customize.endpoint.md.MdExcelSheetOrgCrm;
import jp.co.dmm.customize.endpoint.md.MdExcelSheetPayeeBnkacc;
import jp.co.dmm.customize.endpoint.md.MdExcelSheetRltPrt;
import jp.co.dmm.customize.endpoint.md.MdExcelSheetSplr;
import jp.co.dmm.customize.endpoint.md.MdUploadResponse;
import jp.co.dmm.customize.endpoint.md.MdUploadSaveRequest;
import jp.co.dmm.customize.endpoint.md.MdUploadStreamingOutput;
import jp.co.dmm.customize.endpoint.md.MdUploadValidator;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 取引先一覧サービス
 */
@BizLogic
public class Md0010Service extends BasePagingService {

	@Inject private Md0010Repository repository;
	@Inject private MdUploadValidator validator;
	@Inject private MdExcelReader reader;
	@Inject private Logger log;

	// アップロードファイルデータマップ
	Map<String, MdExcelBook> bookMap = new HashMap<String, MdExcelBook>();

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Md0010SearchResponse init(Md0010SearchRequest req) {
		final Md0010SearchResponse res = createResponse(Md0010SearchResponse.class, req);
		res.success = true;
		res.adrPrfCds = repository.getSelectItems(sessionHolder.getWfUserRole().getCorporationCode(), "kbnPrefectures");

		LoginInfo login = sessionHolder.getLoginInfo();
		res.companyCd = login.getCorporationCode();
		res.companyNm = login.getCorporationName();

		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Md0010SearchResponse search(Md0010SearchRequest req) {
		final int allCount = repository.count(req);
		final Md0010SearchResponse res = createResponse(Md0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 取引先情報をアップロード
	 * @param multiPart
	 * @return
	 */
	public Response upload(FormDataMultiPart multiPart) {
		final MdUploadResponse res = createResponse(MdUploadResponse.class, null);

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
					final MdExcelBook book = parse(bais);

					//nullチェック sheet名が規定と異なるとnullになるため
					if (book == null
							|| book.sheetSplr == null
							|| book.sheetAcc == null
							|| book.sheetRlt == null
							|| book.sheetOrg == null
							|| book.sheetSplr.splrs == null) {
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
						res.splrCount = book.sheetSplr.splrs.stream().count();
						res.payeeBnkaccCount = book.sheetAcc.accs.stream().count();
						res.rltPrtCount = book.sheetRlt.rlts.stream().count();
						res.orgCrmCount = book.sheetOrg.orgs.stream().count();
						res.success = true;

						return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
					}
					else {
						// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
						MdUploadStreamingOutput streaming = new MdUploadStreamingOutput(book);
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
	 * 取引先情報アップロードファイルをDBに登録
	 * @param multiPart
	 * @return
	 */
	public Response register(MdUploadSaveRequest req) {
		try {
			// アップロードファイルをパースしてBookを復元
			final MdExcelBook book = bookMap.get(req.encoded);
			bookMap.remove(req.encoded);

			// バリデーション
			if (validator.validate(book)) {

				// 取引先情報の登録
				repository.uploadRegist(book, sessionHolder.getWfUserRole());

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.splrInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				MdUploadStreamingOutput streaming = new MdUploadStreamingOutput(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private MdExcelBook parse(InputStream stream) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		final MdExcelBook book = reader.parse(stream);

		// マスタの存在チェック用に、処理前から存在していたコード値を保存しておく
		repository.getUploadMasterCdInfo(book);

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

		return book;
	}

	/**
	 * テンプレートのダウンロード
	 * @return
	 */
	public Response download(Md0010SearchRequest req) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = u.getCorporationCode();
		final MdExcelBook book = new MdExcelBook();
		{
			// 取引先マスタ
			book.sheetSplr = new MdExcelSheetSplr();
			book.sheetSplr.splrs = repository.getSplr(req);
			// 振込先マスタ
			book.sheetAcc = new MdExcelSheetPayeeBnkacc();
			book.sheetAcc.accs = repository.getAcc(req);
			// 関係先マスタ
			book.sheetRlt = new MdExcelSheetRltPrt();
			book.sheetRlt.rlts = repository.getRlt(req);
			// 反社情報
			book.sheetOrg = new MdExcelSheetOrgCrm();
			book.sheetOrg.orgs = repository.getOrg(req);
		}
		final String fileName = MessageFormat.format("取引先_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));
		final MdUploadStreamingOutput streaming = new MdUploadStreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
