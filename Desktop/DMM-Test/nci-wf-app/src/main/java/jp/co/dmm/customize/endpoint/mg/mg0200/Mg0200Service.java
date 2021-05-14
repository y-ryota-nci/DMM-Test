package jp.co.dmm.customize.endpoint.mg.mg0200;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ibm.icu.util.Calendar;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadResponse;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadSaveRequest;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgExcelBookAccClnd;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgExcelReaderAccClnd;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgExcelSheetAccClnd;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgUploadSOAccClnd;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgUploadValidatorAccClnd;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.DownloadUtils;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 銀行カレンダマスタ登録Service
 */
@BizLogic
public class Mg0200Service extends BaseService {

	@Inject private Mg0200Repository repository;
	@Inject private MgUploadValidatorAccClnd validator;
	@Inject private MgExcelReaderAccClnd reader;
	@Inject private Logger log;

	// アップロードファイルデータマップ
	Map<String, MgExcelBookAccClnd> bookMap = new HashMap<String, MgExcelBookAccClnd>();

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0200Response init(Mg0200Request req) {
		final Mg0200Response res = createResponse(Mg0200Response.class, req);
		if (isEmpty(res.companyCd)) {
			res.companyCd = sessionHolder.getLoginInfo().getCorporationCode();
		}
		if (isEmpty(req.clndYm)) {
			Date dtNow = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			res.clndYm = sdf.format(dtNow);
		}
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mg0200Response search(Mg0200Request req) {
		//指定年月の当月以外の検索の場合、年月を調整する
		if (req.searchType!=Mg0200Request.SEARCH_TYPE_NORMAL) {
			String[] ym = req.clndYm.split("/");
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
			if (req.searchType==Mg0200Request.SEARCH_TYPE_PREV) {
				cal.add(Calendar.MONTH, -1);
			} else if (req.searchType==Mg0200Request.SEARCH_TYPE_NEXT) {
				cal.add(Calendar.MONTH, 1);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			req.clndYm = sdf.format(cal.getTime());
		}

		final Mg0200Response res = createResponse(Mg0200Response.class, req);
		res.companyCd = req.companyCd;
		res.clndYm = req.clndYm;

		final int count = repository.count(req);
		res.count = count;
		if (count>0) {
			res.entitys = repository.select(req, res);
		} else {
			res.entitys = new ArrayList<Mg0200Entity>();
		}
		res.success = true;
		return res;
	}

	/**
	 * 生成
	 * @param req
	 * @return
	 */
	public Mg0200Response create(Mg0200Request req) {
		final Mg0200Response res = createResponse(Mg0200Response.class, req);
		final int count = repository.count(req);
		if (count>0) {
			res.addAlerts("指定された年月の会計カレンダマスタは既に生成されています。");
			res.success = false;
			return res;
		}

		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();
		String[] ym = req.clndYm.split("/");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
		int bgnDay = cal.getActualMinimum(Calendar.DATE);
		int endDay = cal.getActualMaximum(Calendar.DATE);
		for (int day=bgnDay; day<=endDay; day++) {
			cal.set(Calendar.DATE, day);
			Mg0200Entity entity = new Mg0200Entity();
			entity.companyCd = companyCd;
			entity.clndDt = cal.getTime();
			int weekDay = cal.get(Calendar.DAY_OF_WEEK);
			entity.clndDay = Integer.valueOf(weekDay).toString();
			entity.hldayTp = weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY ? Mg0200Entity.HLDAY_TP_HOLIDAY : Mg0200Entity.HLDAY_TP_NORMAL;
			entity.bnkHldayTp = weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY ? Mg0200Entity.HLDAY_TP_HOLIDAY : Mg0200Entity.HLDAY_TP_NORMAL;
			entity.stlTpPur = day<16 ? Mg0200Entity.STL_TP_OVERLAP : Mg0200Entity.STL_TP_CURRENT;
			entity.stlTpFncobl = day<16 ? Mg0200Entity.STL_TP_OVERLAP : Mg0200Entity.STL_TP_CURRENT;
			entity.stlTpFncaff = day<16 ? Mg0200Entity.STL_TP_OVERLAP : Mg0200Entity.STL_TP_CURRENT;
			entity.mlClsTm = "2359";
			entity.dltFg = "0";
			entity.corporationCodeCreated = companyCd;
			entity.userCodeCreated = userCode;
			entity.ipCreated = ipAddress;
			entity.timestampCreated = now;
			entity.corporationCodeUpdated = companyCd;
			entity.userCodeUpdated = userCode;
			entity.ipUpdated = ipAddress;
			entity.timestampUpdated = now;
			repository.insert(entity);
		}

		res.entitys = repository.select(req, res);

		res.success = true;
		return res;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	public Mg0200Response update(Mg0200Request req) {
		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		Map<Date,AccClndMst> clndMap = repository.getAccClndMst(req);
		Timestamp dispTs = req.entitys.get(0).timestampUpdated;
		Timestamp currTs = ((AccClndMst)clndMap.get(req.entitys.get(0).clndDt)).getTimestampUpdated();
		if (dispTs.compareTo(currTs)!=0) {
			throw new AlreadyUpdatedException();
		}

		for (Mg0200Entity input : req.entitys) {
			input.corporationCodeUpdated = companyCd;
			input.userCodeUpdated = userCode;
			input.ipUpdated = ipAddress;
			AccClndMst entity = clndMap.remove(input.clndDt);
			if (entity==null) {
				input.corporationCodeCreated = companyCd;
				input.userCodeCreated = userCode;
				input.ipCreated = ipAddress;
				input.timestampCreated = now;
				repository.insert(input);
			} else {
				repository.update(input, entity);
			}
		}

		final Mg0200Response res = createResponse(Mg0200Response.class, req);
		res.success = true;
		return res;
	}

	public Response download(Mg0200Request req) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = u.getCorporationCode();
		final String fileName = MessageFormat.format("会計カレンダーマスタ_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));

		MgExcelBookAccClnd book = new MgExcelBookAccClnd();

		book.sheet = new MgExcelSheetAccClnd();
		book.sheet.entityList = repository.getMasterData((Mg0200Request) req);

		final MgUploadSOAccClnd streaming = new MgUploadSOAccClnd(book);
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
					MgExcelBookAccClnd book = new MgExcelBookAccClnd();
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
						MgUploadSOAccClnd streaming = new MgUploadSOAccClnd(book);
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
			final MgExcelBookAccClnd book = bookMap.get(req.encoded);
			bookMap.remove(req.encoded);

			// バリデーション
			if (validator.validate(book)) {

				// クレカ口座情報の登録
				repository.uploadRegist(book, sessionHolder.getWfUserRole());

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.splrInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				MgUploadSOAccClnd streaming = new MgUploadSOAccClnd(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private void parse(InputStream stream, MgExcelBookAccClnd book) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		reader.parse(stream, book);

		// マスタの存在チェック用に、処理前から存在していたコード値を保存しておく
		repository.getUploadMasterCdInfo(book);

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

	}

}
