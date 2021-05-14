package jp.co.nci.iwf.endpoint.sandbox.excel;

import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsColumn;
import com.gh.mygreen.xlsmapper.annotation.XlsNestedRecords;

/**
 * XlsMapperの動作サンプル2用ユーザ
 */
public class SandboxUser {
	/** 列：企業コード */
	@XlsColumn(columnName="企業コード")
	public String corporationCode;

	/** 列：ユーザコード */
	@XlsColumn(columnName="ユーザコード")
	public String userCode;

	/** 列：ログインID */
	@XlsColumn(columnName="ログインID")
	public String userAddedInfo;

	/** 列：氏名 */
	@XlsColumn(columnName="氏名")
	public String userName;

	/** 列：ユーザ所属 */
	@XlsNestedRecords
	public List<SandboxUserBelong> belongs;
}
