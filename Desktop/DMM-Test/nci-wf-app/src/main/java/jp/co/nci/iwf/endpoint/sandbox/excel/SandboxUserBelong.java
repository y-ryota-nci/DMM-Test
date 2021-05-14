package jp.co.nci.iwf.endpoint.sandbox.excel;

import com.gh.mygreen.xlsmapper.annotation.XlsColumn;

/**
 * XlsMapperの動作サンプル2用ユーザ所属
 */
public class SandboxUserBelong {
	/** 列：ユーザ所属連番 */
	@XlsColumn(columnName="ユーザ所属", headerMerged=0)
	public Long seqNoUserBelong;

	/** 列：主務区分 */
	@XlsColumn(columnName="ユーザ所属", headerMerged=1)
	public String jobType;

	/** 列：組織コード */
	@XlsColumn(columnName="ユーザ所属", headerMerged=2)
	public String organizationCode;

	/** 列：組織名 */
	@XlsColumn(columnName="ユーザ所属", headerMerged=3)
	public String organizationName;
}
