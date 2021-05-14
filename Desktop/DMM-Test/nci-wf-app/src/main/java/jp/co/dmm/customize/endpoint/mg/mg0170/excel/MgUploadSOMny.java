package jp.co.dmm.customize.endpoint.mg.mg0170.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOMny extends MgUploadStreamingOutput<MgExcelBookMny, MgExcelWriterMny>  {

	public MgUploadSOMny(MgExcelBookMny book) {
		super(book);
		super.writerClass = MgExcelWriterMny.class;
	}

	@Override
	public String getSheetName() {
		return "通貨マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0170.xlsx";
	}
}
