package jp.co.nci.iwf.endpoint.api.request;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WftVariable;

public class GetRouteRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセスID. */
	private Long processId;
	/** ロール展開フラグ. */
	private boolean unfoldRoleFlg;
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
	 * プロセスIDを取得する.
	 * @return プロセスID
	 */
	public final Long getProcessId() {
		return processId;
	}
	/**
	 * プロセスIDを設定する.
	 * @param pProcessId プロセスID
	 */
	public final void setProcessId(final Long pProcessId) {
		this.processId = pProcessId;
	}

	/**
	 * ロール展開フラグを取得する.
	 *
	 * @return ロール展開フラグ
	 */
	public final boolean isUnfoldRoleFlg() {
		return unfoldRoleFlg;
	}

	/**
	 * ロール展開フラグを設定する.
	 *
	 * @param pUnfoldRoleFlg ロール展開フラグ
	 */
	public final void setUnfoldRoleFlg(final boolean pUnfoldRoleFlg) {
		this.unfoldRoleFlg = pUnfoldRoleFlg;
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
