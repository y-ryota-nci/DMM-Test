package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOAccClnd extends MgUploadStreamingOutput<MgExcelBookAccClnd, MgExcelWriterAccClnd>  {

	public MgUploadSOAccClnd(MgExcelBookAccClnd book) {
		super(book);
		super.writerClass = MgExcelWriterAccClnd.class;
	}

	@Override
	public String getSheetName() {
		return "会計カレンダーマスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0200.xlsx";
	}
}
