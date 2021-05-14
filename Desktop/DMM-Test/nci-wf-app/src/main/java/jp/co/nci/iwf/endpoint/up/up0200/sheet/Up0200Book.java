package jp.co.nci.iwf.endpoint.up.up0200.sheet;

import java.io.Serializable;
import java.util.Set;

import jp.co.nci.integrated_workflow.common.CodeMaster.AdministratorType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.JobType;
import jp.co.nci.integrated_workflow.common.CodeMaster.LocaleCode;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class Up0200Book implements Serializable {
	/** 区切り文字 */
	public final String SEPARATOR = "\t";

	/** シート「組織」 */
	public Up0200SheetOrganization sheetOrg;
	/** シート「役職」 */
	public Up0200SheetPost sheetPost;
	/** シート「ユーザ」 */
	public Up0200SheetUser sheetUser;
	/** シート「所属」 */
	public Up0200SheetUserBelong sheetUserBelong;

	/** 企業コード一覧 */
	public Set<String> existCorporationCodes;
	/** 企業コード＋組織コード一覧 */
	public Set<String> existsOrganizationCodes;
	/** 企業コード＋役職コード一覧 */
	public Set<String> existsPostCodes;
	/** 企業コード＋ユーザコード一覧 */
	public Set<String> existUserCodes;
	/** 削除区分一覧 */
	public Set<String> deleteFlags = MiscUtils.asSet(DeleteFlag.ON, DeleteFlag.OFF);
	/** 一般的なフラグ一覧 */
	public Set<String> commonFlags = MiscUtils.asSet(CommonFlag.ON, CommonFlag.OFF);
	/** 管理者区分 */
	public Set<String> administratorTypes = MiscUtils.asSet(AdministratorType.ADMINISTRATOR, AdministratorType.NORMAL_USER);
	/** 言語コード一覧 */
	public Set<String> localeCodes = MiscUtils.asSet(LocaleCode.JP, LocaleCode.EN, LocaleCode.ZH);
	/** 主務兼務区分一覧 */
	public Set<String> jobTypes = MiscUtils.asSet(JobType.MAIN, JobType.SUB);
;
}
