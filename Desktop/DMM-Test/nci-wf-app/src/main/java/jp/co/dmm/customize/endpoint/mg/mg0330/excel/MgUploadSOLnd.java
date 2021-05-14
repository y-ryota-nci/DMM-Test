package jp.co.dmm.customize.endpoint.mg.mg0330.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOLnd extends MgUploadStreamingOutput<MgExcelBookLnd, MgExcelWriterLnd>  {

	public MgUploadSOLnd(MgExcelBookLnd book) {
		super(book);
		super.writerClass = MgExcelWriterLnd.class;
	}

	@Override
	public String getSheetName() {
		return "国マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0330.xlsx";
	}
}
