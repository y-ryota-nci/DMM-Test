package jp.co.nci.iwf.endpoint.sandbox.excel;

import java.util.Date;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.LabelledCellType;
import com.gh.mygreen.xlsmapper.annotation.XlsDateTimeConverter;
import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsLabelledCell;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;
import com.gh.mygreen.xlsmapper.annotation.XlsSheetName;

/**
 * EXCEL取込用のユーザ入力フォームSheet
 */
@XlsSheet(name="ユーザ入力フォーム")
public class ExcelFormSheet {
	/** シート名 */
	@XlsSheetName
	public String sheetName;

	/** 題名 */
	@XlsLabelledCell(label="題名", type=LabelledCellType.Right)
	public String title;

	/** 記入日 */
	@XlsLabelledCell(label="記入日", type=LabelledCellType.Right)
	@XlsDateTimeConverter(javaPattern="yyyy/MM/dd")
	public Date date;

	/** 企業コード */
	@XlsLabelledCell(label="企業コード", type=LabelledCellType.Right)
	public String corporationCode;

	/** ユーザコード */
	@XlsLabelledCell(label="ユーザコード", type=LabelledCellType.Right)
	public String userCode;

	/** ログインID */
	@XlsLabelledCell(label="ログインID", type=LabelledCellType.Right)
	public String userAddedInfo;

	/** 氏名 */
	@XlsLabelledCell(label="氏名", type=LabelledCellType.Right)
	public String userName;

	/** メールアドレス */
	@XlsLabelledCell(label="メールアドレス", type=LabelledCellType.Right)
	public String mailAddress;

	/** ユーザ所属 */
	@XlsHorizontalRecords(tableLabel="ユーザ所属")
	public List<ExcelFormBelong> belongs;
}
