package jp.co.nci.iwf.endpoint.ws;

import java.sql.Date;
import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 代理設定の永続化リクエスト.
 */
public class WsPersistenceRequest extends BaseRequest {

	/** */
	private static final long serialVersionUID = 1L;

	// 以下、登録用
	/** 文書種別(プロセス定義コード)一覧 */
	public List<String> processDefCodes;
	/** 決裁区分 */
	public String approvalType;
	/** 代理元ユーザ */
	public String userCode;
	/** 代理先ユーザ(ユーザコード)一覧 */
	public List<String> userCodeTransfers;
	/** 代理開始日 */
	public Date validStartDate;
	/** 代理終了日 */
	public Date validEndDate;

	/** 更新内容 */
	public String entry;

}