package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOItmexpsChrmst extends MgUploadStreamingOutput<MgExcelBookItmexpsChrmst, MgExcelWriterItmexpsChrmst>  {

	public MgUploadSOItmexpsChrmst(MgExcelBookItmexpsChrmst book) {
		super(book);
		super.writerClass = MgExcelWriterItmexpsChrmst.class;
	}

	@Override
	public String getSheetName() {
		return "費目関連マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0030.xlsx";
	}
}
