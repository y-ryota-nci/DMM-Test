package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadStreamingOutput;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MgUploadSOAccBrkdwn extends MgUploadStreamingOutput<MgExcelBookAccBrkdwn, MgExcelWriterAccBrkdwn>  {

	public MgUploadSOAccBrkdwn(MgExcelBookAccBrkdwn book) {
		super(book);
		super.writerClass = MgExcelWriterAccBrkdwn.class;
	}

	@Override
	public String getSheetName() {
		return "勘定科目補助マスタ";
	}

	@Override
	public String getinputExcelFilePath() {
		return "excel/mg0140.xlsx";
	}
}
