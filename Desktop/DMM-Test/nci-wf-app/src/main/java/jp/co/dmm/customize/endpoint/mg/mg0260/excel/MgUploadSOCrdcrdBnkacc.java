package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOCrdcrdBnkacc extends MgUploadStreamingOutput<MgExcelBookCrdcrdBnkacc, MgExcelWriterCrdcrdBnkacc>  {

	public MgUploadSOCrdcrdBnkacc(MgExcelBookCrdcrdBnkacc book) {
		super(book);
		super.writerClass = MgExcelWriterCrdcrdBnkacc.class;
	}

	@Override
	public String getSheetName() {
		return "クレカ口座マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0260.xlsx";
	}
}
