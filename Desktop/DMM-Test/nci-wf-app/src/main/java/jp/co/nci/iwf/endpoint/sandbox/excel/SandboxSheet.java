package jp.co.nci.iwf.endpoint.sandbox.excel;

import java.util.Date;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.LabelledCellType;
import com.gh.mygreen.xlsmapper.annotation.XlsDateTimeConverter;
import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsLabelledCell;
import com.gh.mygreen.xlsmapper.annotation.XlsRecordOption;
import com.gh.mygreen.xlsmapper.annotation.XlsRecordOption.OverOperation;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;
import com.gh.mygreen.xlsmapper.annotation.XlsSheetName;

/**
 * XlsMapperの動作サンプル1用Sheet
 */
@XlsSheet(name="ユーザ一覧")
public class SandboxSheet {
	/** シート名 */
	@XlsSheetName
	public String sheetName;

	/** 題名 */
	@XlsLabelledCell(label="題名", type=LabelledCellType.Right)
	public String title;

	/** 出力日 */
	@XlsLabelledCell(label="出力日", type=LabelledCellType.Right)
	@XlsDateTimeConverter(javaPattern="yyyy/MM/dd")
	public Date date;

	/** ユーザ一覧リスト */
	@XlsHorizontalRecords(tableLabel="ユーザ一覧", headerBottom=2)
	@XlsRecordOption(overOperation=OverOperation.Insert)
	public List<SandboxUser> users;
}
