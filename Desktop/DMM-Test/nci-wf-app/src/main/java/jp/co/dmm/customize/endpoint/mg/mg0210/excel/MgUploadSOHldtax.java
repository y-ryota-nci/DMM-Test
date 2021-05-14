package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOHldtax extends MgUploadStreamingOutput<MgExcelBookHldtax, MgExcelWriterHldtax>  {

	public MgUploadSOHldtax(MgExcelBookHldtax book) {
		super(book);
		super.writerClass = MgExcelWriterHldtax.class;
	}

	@Override
	public String getSheetName() {
		return "源泉税マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0210.xlsx";
	}
}
