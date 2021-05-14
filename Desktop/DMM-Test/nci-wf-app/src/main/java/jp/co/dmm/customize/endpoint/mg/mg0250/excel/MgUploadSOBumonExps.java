package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOBumonExps extends MgUploadStreamingOutput<MgExcelBookBumonExps, MgExcelWriterBumonExps>  {

	public MgUploadSOBumonExps(MgExcelBookBumonExps book) {
		super(book);
		super.writerClass = MgExcelWriterBumonExps.class;
	}

	@Override
	public String getSheetName() {
		return "部門関連マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0250.xlsx";
	}
}
