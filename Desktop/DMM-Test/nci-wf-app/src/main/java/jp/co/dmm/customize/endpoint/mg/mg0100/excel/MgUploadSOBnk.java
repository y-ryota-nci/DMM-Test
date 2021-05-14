package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBnk extends MgUploadStreamingOutput<MgExcelBookBnk, MgExcelWriterBnk>  {

	public MgUploadSOBnk(MgExcelBookBnk book) {
		super(book);
		super.writerClass = MgExcelWriterBnk.class;
	}

	@Override
	public String getSheetName() {
		return "銀行マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0100.xlsx";
	}
}
