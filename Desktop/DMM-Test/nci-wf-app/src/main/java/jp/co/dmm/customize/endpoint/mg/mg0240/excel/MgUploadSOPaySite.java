package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOPaySite extends MgUploadStreamingOutput<MgExcelBookPaySite, MgExcelWriterPaySite>  {

	public MgUploadSOPaySite(MgExcelBookPaySite book) {
		super(book);
		super.writerClass = MgExcelWriterPaySite.class;
	}

	@Override
	public String getSheetName() {
		return "支払サイトマスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0240.xlsx";
	}
}
