package jp.co.dmm.customize.endpoint.mg.mg0110.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBnkbrc extends MgUploadStreamingOutput<MgExcelBookBnkbrc, MgExcelWriterBnkbrc>  {

	public MgUploadSOBnkbrc(MgExcelBookBnkbrc book) {
		super(book);
		super.writerClass = MgExcelWriterBnkbrc.class;
	}

	@Override
	public String getSheetName() {
		return "銀行支店マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0110.xlsx";
	}
}
