package jp.co.dmm.customize.endpoint.mg.mg0280.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOMda extends MgUploadStreamingOutput<MgExcelBookMda, MgExcelWriterMda>  {

	public MgUploadSOMda(MgExcelBookMda book) {
		super(book);
		super.writerClass = MgExcelWriterMda.class;
	}

	@Override
	public String getSheetName() {
		return "メディアマスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0280.xlsx";
	}
}
