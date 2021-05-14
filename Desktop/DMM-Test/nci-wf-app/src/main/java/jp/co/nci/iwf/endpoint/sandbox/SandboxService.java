package jp.co.nci.iwf.endpoint.sandbox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;

import com.gh.mygreen.xlsmapper.XlsMapper;
import com.gh.mygreen.xlsmapper.XlsMapperException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.accesslog.AccessLogRepository;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.mail.MailAttachFile;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailConfig;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.component.pdf.PdfService;
import jp.co.nci.iwf.endpoint.al.al0010.Al0010Entity;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.sandbox.excel.ExcelFormSheet;
import jp.co.nci.iwf.endpoint.sandbox.excel.SandboxSheet;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLog;
import jp.co.nci.iwf.util.BeanToMapConveter;
import jp.co.nci.iwf.util.ClassPathResource;

/**
 * サンドボックス(実験ページ)サービス
 */
@BizLogic
public class SandboxService extends BasePagingService implements CodeBook {
	@Inject private AccessLogRepository repository;
	@Inject private DownloadNotifyService notify;
	@Inject private MailService mailService;
	@Inject private MailConfig mailConfig;
	@Inject private Logger log;
	@Inject private PdfService pdfService;

	public SandboxResponse init(BaseRequest req) {
		SandboxResponse res = createResponse(SandboxResponse.class, req);
		res.success = true;
		res.mailEnv = mailConfig.getMailEnv().toString();
		res.dummySendTo = mailConfig.getDummySendTo();
		res.smtpHost = mailConfig.getHost();
		res.smtpPort = mailConfig.getPort();
		return res;
	}

	/**
	 * アクセスログをCSVダウンロード
	 */
	public StreamingOutput downloadCsv() {
		return new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				// ダウンロードモニターへ開始を通知
				notify.begin();

				// CSVPrinterはCSV形式でストリームへの書き込みを出来るようにする機能を提供する
				try (CSVPrinter csv = NciCsvFormat.print(new OutputStreamWriter(output, MS932))) {
					// CSVヘッダ
					csv.printRecord("アクセスログID", "日付", "画面", "アクション");
					// CSVボディ
					// RowHandlerを使って抽出処理を行うと、一定行数をフェッチして少量ずつ逐次処理を行うため、メモリを食わない。
					// よってRowHandlerを使ってストリームへ逐次書き込みをすれば、1000万行のCSVダウンロードでも OutOfMemoryを誘発しない。
					repository.selectAll((MwtAccessLog entity) -> {
						csv.printRecord(
								toStr(entity.getAccessLogId()),
								toStrTimestamp(entity.getAccessTime()),
								entity.getScreenName(),
								entity.getActionName()
						);
						return true;
					});
				}
				finally {
					// ダウンロードモニターへ終了を通知
					notify.end();
				}
			}
		};
	}

	/**
	 * メール送信
	 * @param req
	 * @return
	 */
	public BaseResponse sendMail(SandboxRequest req) {
		try {
			final LoginInfo login = sessionHolder.getLoginInfo();
			final String corporationCode = login.getCorporationCode();
			final String localeCode = defaults(req.localeCode, login.getLocaleCode());
			final String to = req.sendTo;
			if (isEmpty(to))
				throw new BadRequestException("メール送信先が未指定です");

			// メールテンプレートの置換文字列Map
			final Map<String, String> variables = new HashMap<>();
			variables.put(MailVariables.LOGIN_USER_NAME, login.getUserName());

			// 添付ファイル
			final List<MailAttachFile> attachFiles = new ArrayList<>();
			attachFiles.add(new MailAttachFile("/pdf/sample.pdf"));
			variables.put(MailVariables.ATTACH_FILE, "sample.pdf");

			// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
			final MailEntry entry = new MailEntry(localeCode, to, variables, attachFiles);
			final MailTemplate template = mailService.toTemplate(MailTemplateFileName.SAMPLE, corporationCode);
			mailService.send(template, entry);

			final BaseResponse res = createResponse(BaseResponse.class, req);
			res.addSuccesses(i18n.getText(MessageCd.MSG0101, MessageCd.email));

			return res;
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * ファイルアップロード
	 */
	public BaseResponse upload(FormDataMultiPart multiPart) {
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			// UploadFileはメンバーとして stream を持っているので、
			// そこからファイルに落とすなりDBに書くなりすべし
			final UploadFile f = new UploadFile(bodyPart);
			log.info("fileName = {} {}bytes", f.fileName, f.size);
		}
		BaseResponse res = createResponse(BaseResponse.class, null);
		res.success = true;
		return res;
	}

	/**
	 * PDFダウンロード
	 */
	public StreamingOutput downloadPDF() {
		return new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				// ダウンロードモニターへ開始を通知
				notify.begin();
				try {
					// ヘッダ（PDFのヘッダ部分をMapとしてパラメータ化）
					Map<String, Object> header = BeanToMapConveter.toMap(LoginInfo.get());
					// 明細（PDFの繰り返し部分をリストとしてパラメータ化）
					// 	※アクセスログ全件をPDF化するのは可能ではあるがテストのたびに長時間待つのは辛いので、
					// 	※先頭10000件だけPDF化。それでも625頁あるのでテストには十分だろう。
					List<Al0010Entity> details = repository.selectTop(10000);

					// PDF生成し、ストリームへ書き出し
					pdfService.writePdfStream("sample.jasper", header, details.toArray(), output);
				}
				catch (IOException e) {
					throw new InternalServerErrorException(e);
				}
				finally {
					// ダウンロードモニターへ終了を通知
					notify.end();
				}
			}
		};
	}

	/** サーバ側でのバリデーション表示用テスト */
	public BaseResponse validationError(BaseRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = false;
		res.addAlerts(i18n.getText(MessageCd.MSG0001, MessageCd.SANDBOX));
		return res;
	}

	/**
	 * EXCELテンプレートにデータを差し込んでからダウンロード(セル結合あり)
	 */
	public StreamingOutput downloadExcel2() {
		return new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					// ダウンロードモニターへ開始を通知
					notify.begin();

					// ExcelダウンロードデータをアノテーションされたBeanへ読み込み
					// もしこのBeanにJPAのアノテーションも付与しておけば、一発で全データが揃うよ
					final SandboxSheet sheet1 = new SandboxSheet();
					sheet1.date = today();
					sheet1.sheetName = "ユーザ一覧";
					sheet1.title = "XlsMapperの動作サンプル";
					sheet1.users = repository.getSampleUsers(sessionHolder.getLoginInfo().getCorporationCode());

					// アノテーションされたBeanからExcelへ反映し、ダウンロード用ストリームへ書き込む
					final XlsMapper xlsMapper = new XlsMapper();
					xlsMapper.getConiguration().setMergeCellOnSave(true);

					final ClassPathResource cpr = new ClassPathResource("excel/XlsMapperSample2.xlsx");
					try (InputStream input = new FileInputStream(cpr.getFile())){
						xlsMapper.save(input, output, sheet1);
					}
					catch (XlsMapperException e) {
						throw new InternalServerErrorException("Excel出力に失敗しました。", e);
					}
				}
				finally {
					// ダウンロードモニターへ終了を通知
					notify.end();
				}
			}
		};
	}

	/**
	 * 空のEXCEL入力フォームをダウンロード
	 * @return
	 */
	public StreamingOutput downloadExcelForm() {
		return new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					// ダウンロードモニターへ開始を通知
					notify.begin();

					// EXCELテンプレートをHTTPレスポンスへ転写
					final ClassPathResource cpr = new ClassPathResource("excel/XlsMapperSample1.xlsx");
					try (InputStream input = new BufferedInputStream(new FileInputStream(cpr.getFile()))) {
						IOUtils.copy(input, output);
					}
				}
				finally {
					// ダウンロードモニターへ終了を通知
					notify.end();
				}
			}
		};
	}

	/**
	 * 入力済みEXCELフォームをアップロード
	 * @param multiPart
	 */
	public SandboxUploadExcelResponse uploadExcelForm(FormDataMultiPart multiPart) {
		// Beanにアノテーションを施してEXCELファイルとのマッピングを行い、XlsMapperでEXCELからBeanへとデータの吸い上げする
		// @see http://mygreen.github.io/xlsmapper/sphinx/howtouse.html
		//   ※ダウンロードのサンプルが欲しければ「VD0010コンテナ一覧」のEXCELダウンロードを参照せよ
		final SandboxUploadExcelResponse res = new SandboxUploadExcelResponse();
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			final XlsMapper xlsMapper = new XlsMapper();
			xlsMapper.getConiguration().setMergeCellOnSave(true);	// 保存時にセル結合
			try {
				res.user = xlsMapper.load(f.stream, ExcelFormSheet.class);
			}
			catch (XlsMapperException | IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		res.success = (res.user != null);
		return res;
	}

	/**
	 * リクエストタイムアウトの検証として、指定秒数だけWAIT
	 * @param req
	 * @return
	 */
	public BaseResponse verifyTimeout(SandboxRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		if (req.timeoutSec != null) {
			long timeout = req.timeoutSec * 1000L;
			log.info("SLEEP begin for {}msec", timeout);
			try {
				Thread.sleep(timeout);
				log.info("SLEEP end for {}msec", timeout);
			}
			catch (InterruptedException e) {};
		}
		res.success = true;
		return res;
	}
}