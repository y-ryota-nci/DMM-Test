package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOZip extends MgUploadStreamingOutput<MgExcelBookZip, MgExcelWriterZip>  {

	public MgUploadSOZip(MgExcelBookZip book) {
		super(book);
		super.writerClass = MgExcelWriterZip.class;
	}

	@Override
	public String getSheetName() {
		return "郵便番号マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0310.xlsx";
	}
}
