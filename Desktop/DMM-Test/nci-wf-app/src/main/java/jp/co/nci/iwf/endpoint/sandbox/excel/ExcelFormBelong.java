package jp.co.nci.iwf.endpoint.sandbox.excel;

import com.gh.mygreen.xlsmapper.annotation.XlsColumn;

/**
 * EXCEL取込用のユーザ入力フォームのユーザ所属
 */
public class ExcelFormBelong {
	/** 列：ユーザ所属連番 */
	@XlsColumn(columnName="所属連番")
	public Long seqNoUserBelong;

	/** 列：組織コード */
	@XlsColumn(columnName="組織コード")
	public String organizationCode;

	/** 列：組織名 */
	@XlsColumn(columnName="組織名")
	public String organizationName;
}
