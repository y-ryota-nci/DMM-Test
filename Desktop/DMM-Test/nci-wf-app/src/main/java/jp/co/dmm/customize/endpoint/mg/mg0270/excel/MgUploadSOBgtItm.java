package jp.co.dmm.customize.endpoint.mg.mg0270.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBgtItm extends MgUploadStreamingOutput<MgExcelBookBgtItm, MgExcelWriterBgtItm>  {

	public MgUploadSOBgtItm(MgExcelBookBgtItm book) {
		super(book);
		super.writerClass = MgExcelWriterBgtItm.class;
	}

	@Override
	public String getSheetName() {
		return "予算科目マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0270.xlsx";
	}
}
