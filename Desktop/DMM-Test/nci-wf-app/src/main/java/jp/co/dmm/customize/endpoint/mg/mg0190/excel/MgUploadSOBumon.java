package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBumon extends MgUploadStreamingOutput<MgExcelBookBumon, MgExcelWriterBumon>  {

	public MgUploadSOBumon(MgExcelBookBumon book) {
		super(book);
		super.writerClass = MgExcelWriterBumon.class;
	}

	@Override
	public String getSheetName() {
		return "部門マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0190.xlsx";
	}
}
