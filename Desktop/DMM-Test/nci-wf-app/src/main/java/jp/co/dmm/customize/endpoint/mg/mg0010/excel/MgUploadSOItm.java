package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOItm extends MgUploadStreamingOutput<MgExcelBookItm, MgExcelWriterItm>  {

	public MgUploadSOItm(MgExcelBookItm book) {
		super(book);
		super.writerClass = MgExcelWriterItm.class;
	}

	@Override
	public String getSheetName() {
		return "品目マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0010.xlsx";
	}
}
