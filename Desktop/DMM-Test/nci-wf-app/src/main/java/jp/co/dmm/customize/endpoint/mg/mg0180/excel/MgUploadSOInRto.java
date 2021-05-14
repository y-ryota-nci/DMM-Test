package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOInRto extends MgUploadStreamingOutput<MgExcelBookInRto, MgExcelWriterInRto>  {

	public MgUploadSOInRto(MgExcelBookInRto book) {
		super(book);
		super.writerClass = MgExcelWriterInRto.class;
	}

	@Override
	public String getSheetName() {
		return "社内レートマスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0180.xlsx";
	}
}
