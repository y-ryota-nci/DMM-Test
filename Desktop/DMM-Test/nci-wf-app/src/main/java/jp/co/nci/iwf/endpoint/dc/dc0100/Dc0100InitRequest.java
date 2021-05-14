package jp.co.nci.iwf.endpoint.dc.dc0100;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 業務文書の登録・更新画面の初期化リクエスト
 */
public class Dc0100InitRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public Long docId;
	public Long screenDocId;
//	public Long timestampUpdated;
//	public TrayType trayType;

	/** (コピー作成)コピー元文書の文書ID */
	public Long copyDocId;
//	/** (コピー起票)コピー元文書情報のプロセスID */
//	public Long copyProcessId;
	/** WF→文書管理への連携か */
	public String fromCoopWfFlag;
}
