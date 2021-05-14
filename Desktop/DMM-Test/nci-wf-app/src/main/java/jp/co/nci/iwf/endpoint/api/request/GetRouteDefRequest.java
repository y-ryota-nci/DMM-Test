package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Date;
import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WftVariable;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;

public class GetRouteDefRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセス定義コード. */
	private String processDefCode;
	/** プロセス定義コード枝番. */
	private String processDefDetailCode;
	/** ロール展開フラグ. */
	private boolean unfoldRoleFlg;
	/** 起票組織コード. */
	private String organizationCodeStart;
	/** 起票役職コード. */
	private String postCodeStart;
	/** 起票者ユーザロール. */
	private WfUserRole wfUserRoleStart;
	/** 起票日. */
	private Date startDate;
	/** シミュレーションかどうか. */
	private boolean isSimulation;
	/** プロセス変数リスト. */
	private List<WftVariable> wftVariableList;

	/**
	 * 会社コードを取得する.
	 * @return 会社コード
	 */
	public final String getCorporationCode() {
		return corporationCode;
	}
	/**
	 * 会社コードを設定する.
	 * @param pCorporationCode 会社コード
	 */
	public final void setCorporationCode(final String pCorporationCode) {
		this.corporationCode = pCorporationCode;
	}
	/**
	 * プロセス定義コードを取得する.
	 * @return プロセス定義コード
	 */
	public final String getProcessDefCode() {
		return processDefCode;
	}
	/**
	 * プロセス定義コードを設定する.
	 * @param pProcessDefCode プロセス定義コード
	 */
	public final void setProcessDefCode(final String pProcessDefCode) {
		this.processDefCode = pProcessDefCode;
	}
	/**
	 * プロセス定義コード枝番を取得する.
	 * @return プロセス定義コード枝番
	 */
	public final String getProcessDefDetailCode() {
		return processDefDetailCode;
	}
	/**
	 * プロセス定義コード枝番を設定する.
	 * @param pProcessDefDetailCode プロセス定義コード枝番
	 */
	public final void setProcessDefDetailCode(final String pProcessDefDetailCode) {
		this.processDefDetailCode = pProcessDefDetailCode;
	}
	/**
	 * ロール展開フラグを取得する.
	 * @return ロール展開フラグ
	 */
	public final boolean isUnfoldRoleFlg() {
		return unfoldRoleFlg;
	}
	/**
	 * ロール展開フラグを設定する.
	 * @param pUnfoldRoleFlg ロール展開フラグ
	 */
	public final void setUnfoldRoleFlg(final boolean pUnfoldRoleFlg) {
		this.unfoldRoleFlg = pUnfoldRoleFlg;
	}
	/**
	 * 起票組織コードを取得する.
	 * @return 起票組織ｺｰﾄﾞ
	 */
	public final String getOrganizationCodeStart() {
		return organizationCodeStart;
	}
	/**
	 * 起票組織コードを設定する.
	 * @param pOrganizationCodeStart 起票組織ｺｰﾄﾞ
	 */
	public final void setOrganizationCodeStart(final String pOrganizationCodeStart) {
		this.organizationCodeStart = pOrganizationCodeStart;
	}
	/**
	 * 起票役職コードを取得する.
	 * @return 起票役職コード
	 */
	public String getPostCodeStart() {
		return postCodeStart;
	}
	/**
	 * 起票役職コードを設定する.
	 * @param postCodeStart 起票役職コード
	 */
	public void setPostCodeStart(String postCodeStart) {
		this.postCodeStart = postCodeStart;
	}
	/**
	 * 起票者ユーザロールを取得する.
	 * @return 起票者ユーザロール
	 */
	public final WfUserRole getWfUserRoleStart() {
		return wfUserRoleStart;
	}
	/**
	 * 起票者ユーザロールを設定する.
	 * @param pUserRoleStart 起票者ユーザロール
	 */
	public final void setWfUserRoleStart(final WfUserRole pUserRoleStart) {
		this.wfUserRoleStart = pUserRoleStart;
	}
	/**
	 * 起票日を取得する.
	 * @return 起票日
	 */
	public final Date getStartDate() {
		return startDate;
	}
	/**
	 * 起票日を設定する.
	 * @param pStartDate 起票日
	 */
	public final void setStartDate(final Date pStartDate) {
		this.startDate = pStartDate;
	}

	/**
	 * シミュレーションかどうか.
	 * @return true:シミュレーション false:通常
	 */
	public final boolean isSimulation() {
		return this.isSimulation;
	}

	/**
	 * シミュレーションかどうかを設定する.
	 * @param pIsSimulation true:シミュレーション false:通常
	 */
	public final void setSimulation(final boolean pIsSimulation) {
		this.isSimulation = pIsSimulation;
	}

	/**
	 * プロセス変数リストを取得する.
	 * @return プロセス変数リスト
	 */
	public final List<WftVariable> getWftVariableList() {
		return wftVariableList;
	}

	/**
	 * プロセス変数リストを設定する.
	 * @param pWftVariableList プロセス変数リスト
	 */
	public final void setWftVariableList(final List<WftVariable> pWftVariableList) {
		this.wftVariableList = pWftVariableList;
	}
}
