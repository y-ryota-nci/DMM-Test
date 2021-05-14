package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.mw.MwtDocUpdateLog;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書更新履歴情報.
 */
public class UpdateHistoryInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書更新履歴ID */
	public Long docUpdateLogId;
	/** 文書バージョンID */
	public Long docId;
	/** コンテンツ種別名称 */
	public String contentsType;
	/** 文書更新日時 */
	public String docUpdateDate;
	/** 文書更新者名 */
	public String docUpdateUserName;
	/** 文書更新ログ */
	public String docUpdateLog;
	/** 文書履歴ID */
	public Long docHistoryId;
	/** 文書ファイル履歴ID */
	public Long docFileHistoryId;

	/** コンストラクタ(デフォルト). */
	public UpdateHistoryInfo() {
	}

	/** コンストラクタ. */
	public UpdateHistoryInfo(MwtDocUpdateLog e) {
		this.docUpdateLogId = e.getDocUpdateLogId();
		this.contentsType = e.getContentsType();
		this.docUpdateDate = MiscUtils.FORMATTER_DATETIME.get().format(e.getDocUpdateTimestamp());
		this.docUpdateUserName = e.getDocUpdateUserName();
		this.docUpdateLog = e.getDocUpdateLog();
		this.docHistoryId = e.getDocHistoryId();
		this.docFileHistoryId = e.getDocFileHistoryId();
	}
}
