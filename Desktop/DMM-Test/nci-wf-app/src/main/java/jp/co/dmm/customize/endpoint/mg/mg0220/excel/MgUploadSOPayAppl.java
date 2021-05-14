package jp.co.dmm.customize.endpoint.mg.mg0220.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOPayAppl extends MgUploadStreamingOutput<MgExcelBookPayAppl, MgExcelWriterPayAppl>  {

	public MgUploadSOPayAppl(MgExcelBookPayAppl book) {
		super(book);
		super.writerClass = MgExcelWriterPayAppl.class;
	}

	@Override
	public String getSheetName() {
		return "支払業務マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0220.xlsx";
	}
}
