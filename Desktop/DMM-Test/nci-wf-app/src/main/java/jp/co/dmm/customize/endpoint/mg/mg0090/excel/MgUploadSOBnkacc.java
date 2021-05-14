package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBnkacc extends MgUploadStreamingOutput<MgExcelBookBnkacc, MgExcelWriterBnkacc>  {

	public MgUploadSOBnkacc(MgExcelBookBnkacc book) {
		super(book);
		super.writerClass = MgExcelWriterBnkacc.class;
	}

	@Override
	public String getSheetName() {
		return "銀行口座マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0090.xlsx";
	}
}
