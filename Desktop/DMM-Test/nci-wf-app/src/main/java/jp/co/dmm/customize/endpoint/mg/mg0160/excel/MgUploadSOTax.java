package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOTax extends MgUploadStreamingOutput<MgExcelBookTax, MgExcelWriterTax>  {

	public MgUploadSOTax(MgExcelBookTax book) {
		super(book);
		super.writerClass = MgExcelWriterTax.class;
	}

	@Override
	public String getSheetName() {
		return "消費税マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0160.xlsx";
	}
}
