package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOTaxexps extends MgUploadStreamingOutput<MgExcelBookTaxexps, MgExcelWriterTaxexps>  {

	public MgUploadSOTaxexps(MgExcelBookTaxexps book) {
		super(book);
		super.writerClass = MgExcelWriterTaxexps.class;
	}

	@Override
	public String getSheetName() {
		return "消費税関連マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0320.xlsx";
	}
}
