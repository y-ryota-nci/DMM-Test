package jp.co.dmm.customize.endpoint.mg.mg0290.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBndFlr extends MgUploadStreamingOutput<MgExcelBookBndFlr, MgExcelWriterBndFlr>  {

	public MgUploadSOBndFlr(MgExcelBookBndFlr book) {
		super(book);
		super.writerClass = MgExcelWriterBndFlr.class;
	}

	@Override
	public String getSheetName() {
		return "結合フロアマスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0290.xlsx";
	}
}
