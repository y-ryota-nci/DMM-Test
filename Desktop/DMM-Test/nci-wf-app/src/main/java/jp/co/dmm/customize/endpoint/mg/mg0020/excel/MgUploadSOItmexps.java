package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOItmexps extends MgUploadStreamingOutput<MgExcelBookItmexps, MgExcelWriterItmexps>  {

	public MgUploadSOItmexps(MgExcelBookItmexps book) {
		super(book);
		super.writerClass = MgExcelWriterItmexps.class;
	}

	@Override
	public String getSheetName() {
		return "費目マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0020.xlsx";
	}
}
