package jp.co.nci.iwf.endpoint.api.request;

/**
 * 引戻アクティビティ定義照会APIの引数格納クラス.
 */
public class GetPullbackActivityDefListRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセスID. */
	private Long processId;

	/**
	 * 会社コードを取得する.
	 * @return 会社コード
	 */
	public final String getCorporationCode() {
		return corporationCode;
	}
	/**
	 * 会社コードを設定する.
	 * @param pCorporationCode 設定する会社コード
	 */
	public final void setCorporationCode(final String pCorporationCode) {
		this.corporationCode = pCorporationCode;
	}
	/**
	 * プロセスIDを取得する.
	 * @return プロセスID
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * プロセスIDを設定する.
	 * @param processId プロセスID
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
}
