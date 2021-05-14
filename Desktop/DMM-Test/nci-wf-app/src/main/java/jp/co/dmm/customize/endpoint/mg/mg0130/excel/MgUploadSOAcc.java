package jp.co.dmm.customize.endpoint.mg.mg0130.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOAcc extends MgUploadStreamingOutput<MgExcelBookAcc, MgExcelWriterAcc>  {

	public MgUploadSOAcc(MgExcelBookAcc book) {
		super(book);
		super.writerClass = MgExcelWriterAcc.class;
	}

	@Override
	public String getSheetName() {
		return "勘定科目マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0130.xlsx";
	}
}
