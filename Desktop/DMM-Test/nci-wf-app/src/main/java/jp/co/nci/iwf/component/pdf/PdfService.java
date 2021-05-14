package jp.co.nci.iwf.component.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;

import com.lowagie.text.pdf.PdfWriter;

import jp.co.nci.iwf.util.BeanToMapConveter;
import jp.co.nci.iwf.util.DownloadUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfVersionEnum;

/**
 * PDF生成サービス
 */
@ApplicationScoped
public class PdfService {
	private static final String AUTHER = "NCI Integration Inc,.";
	private static final String CREATOR = "NCIWF V6";

	/**
	 * PDFファイルを生成し、OutputStreamへ書き出す.（単一のJasperファイルから単一のPDFファイル化）
	 * @param jasperFileName *.jasperファイル名
	 * @param header ヘッダー項目の値を格納したBean
	 * @param details 明細行を格納した配列
	 * @param output 最終的な書き出し先ストリーム
	 */
	public void writePdfStream(String jasperFileName, Object header, Object[] details, OutputStream output) throws IOException {
		// メモリを圧迫しないよう、いったんPDFをローカルのTEMPファイルとして出力
		final File pdf = createPdfFile(jasperFileName, header, details);

		// 生成したTEMPファイルをストリームへ転写し、完了後にTEMPファイルを削除
		DownloadUtils.copyFileToStream(pdf, output, true);
	}

	/**
	 * PDFファイル作成.（単一のJasperファイルから単一のPDFファイルを作成）
	 * @param jasperFileName *.jasperファイル名
	 * @param header ヘッダー項目の値を格納したBean
	 * @param details 明細行を格納した配列
	 *
	 * @return テンポラリーフォルダに作成されたPDFファイル
	 */
	public File createPdfFile(String jasperFileName, Object header, Object[] details) {
		// ヘッダと明細から、PDFのページ定義を作成
		final Map<String, Object> params = BeanToMapConveter.toMap(header);
		final JRDataSource ds = JRDataSourceConverter.toDataSource(details);
		final PdfPage page = new PdfPage(jasperFileName, params, ds);
		// PDFページ定義を出力
		return createPdfFile(page);
	}

	/**
	 * PDFファイル作成（複数のJasperファイルをマージして単一PDFファイルを作成）
	 * @param pages PDFページ定義のリスト
	 *
	 * @return テンポラリーフォルダに作成されたPDFファイル
	 */
	public File createPdfFile(PdfPage... pages) {
		// PDF作成対象ファイルをテンポラリファイルとして生成
		final File file = createTempFile();
		try (OutputStream out = new FileOutputStream(file)) {
			// メモリをけちるため、中間生成物を仮想化（ようはTEMPファイルへアウトソースするってことだ）
			final String tempDir = System.getProperty("java.io.tmpdir");
			final JRVirtualizer virtualizer = new JRFileVirtualizer(2, tempDir);

			try {
				// 複数ページ定義をまとめるバインド済Jasper印刷情報
				JasperPrint jasperPrint = null;

				for (PdfPage page : pages) {
					final InputStream is = getJasperFileStream(page.jasperFileName);
					if (is == null)
						throw new FileNotFoundException(page.jasperFileName);
					if (jasperPrint == null) {
						jasperPrint = fillReport(virtualizer, is, page.params, page.ds);
					} else {
						// 二つ目以降は最初のへ追記していく
						JasperPrint printAdd = fillReport(virtualizer, is, page.params, page.ds);
						for (Object addPrintPage : printAdd.getPages()) {
							jasperPrint.addPage((JRPrintPage)addPrintPage);
						}
					}
				}

				// パスワード付PDFも作れるよ(PDF version1.5以降)
//				JRPdfExporter exporter = createEncryptPdfExporter(out, jasperPrint);
				JRPdfExporter exporter = createPdfExporter(out, jasperPrint);
				exporter.exportReport();
//				JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(file));
			}
			finally {
				virtualizer.cleanup();
			}
			return file;
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		}
	}

	/**
	 * PDFのExporterを作成する
	 * @param out アウトプットストリーム
	 * @param jasperPrints バインド済Jasper印刷情報
	 * @return
	 */
	protected JRPdfExporter createPdfExporter(OutputStream out, JasperPrint...jasperPrints) {
		final JRPdfExporter exporter = new JRPdfExporter();

		final SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
		conf.setPdfVersion(PdfVersionEnum.VERSION_1_4);
		conf.setMetadataAuthor(AUTHER);
		conf.setMetadataCreator(CREATOR);
		exporter.setConfiguration(conf);

		final SimpleExporterInput input = SimpleExporterInput.getInstance(Arrays.asList(jasperPrints));
		exporter.setExporterInput(input);

		final SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(out);
		exporter.setExporterOutput(output);

		return exporter;
	}

	/**
	 * パスワード付PDFのExporterを作成する
	 * @param out アウトプットストリーム
	 * @param jasperPrint バインド済Jasper印刷情報
	 * @return
	 */
	protected JRPdfExporter createEncryptPdfExporter(OutputStream out, JasperPrint... jasperPrint) {
		// パスワードなしPDF用のExporerをベースにする
		final JRPdfExporter exporter = createPdfExporter(out, jasperPrint);

		//----------------------------------------------------------------------------------------------
		// 暗号化
		// 暗号化するためには、jasperreports-5.1.0.jar＋itext-2.1.7.js2.jar＋bcprov-jdk15-1.46.jarが必須
		// １つでもバージョンの組み合わせが違うと動かないので、細心の注意を払うこと！
		//----------------------------------------------------------------------------------------------
		final SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
		conf.setPdfVersion(PdfVersionEnum.VERSION_1_4);
		conf.setMetadataAuthor(AUTHER);
		conf.setMetadataCreator(CREATOR);
		conf.set128BitKey(true);
		conf.setEncrypted(true);
		exporter.setConfiguration(conf);

		// PDFを開くときのパスワード
		final String userPassword = "";
		if (StringUtils.isNotEmpty(userPassword)) {
			conf.setUserPassword(userPassword);
		}
		// PDFを編集するときのパスワード
		final String ownerPassword = "";
		if (StringUtils.isNotEmpty(ownerPassword)) {
			conf.setOwnerPassword(ownerPassword);
		}
		// パスワードで保護された権限（ビット演算）
		int permissions =
				PdfWriter.ALLOW_SCREENREADERS
				| PdfWriter.ALLOW_FILL_IN
				| PdfWriter.ALLOW_PRINTING
				| PdfWriter.ALLOW_DEGRADED_PRINTING
				| PdfWriter.ALLOW_COPY;
		conf.setPermissions(permissions);

		exporter.setConfiguration(conf);

		return exporter;
	}

	/**
	 * .jasperファイル名から対象ファイルのInputStreamを返す
	 * @param jasperFileName .jasperファイル名
	 * @return
	 */
	protected InputStream getJasperFileStream(String jasperFileName) throws FileNotFoundException {
		return getClass().getClassLoader().getResourceAsStream("pdf/" + jasperFileName);
	}

	/**
	 * .jasperファイル＋パラメータ＋DataSourceをバインド
	 * @param virtualizer バーチャライザ
	 * @param jasperFile .jasperファイルへの絶対パス
	 * @param params パラメータ（帳票のヘッダ部部分）
	 * @param ds データソース（帳票の繰り返し部分）
	 * @return
	 * @throws JRException
	 */
	protected JasperPrint fillReport(JRVirtualizer virtualizer, final InputStream jasperFile, Map<String, Object> params, JRDataSource ds) throws JRException {
		// ワークディレクトリを指定して、メモリ使用量を抑制
		params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

		JasperReport report = JasperCompileManager.compileReport(jasperFile);

		// .jasperファイル＋パラメータ＋DataSourceをバインド
		return JasperFillManager.fillReport(report, params, ds);
	}

	/** テンポラリファイル作成 */
	protected File createTempFile() {
		try {
			return File.createTempFile("pdfService_", "pdf");
		} catch (IOException e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		}
	}

}
