package jp.co.nci.iwf.component.pdf;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * PDFページ定義。
 */
public class PdfPage {
	/** .jasperファイル名 */
	public String jasperFileName;
	/** パラメータMap */
	public Map<String, Object> params;
	/** 繰り返し要素用のデータソース */
	public JRDataSource ds;

	public PdfPage(String jasperFileName, Map<String, Object> params, JRDataSource ds) {
		this.jasperFileName = jasperFileName;
		this.params = params;
		this.ds = ds;
	}
}
