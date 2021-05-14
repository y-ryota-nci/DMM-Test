package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;
import java.util.Date;

import jp.co.nci.iwf.jpa.entity.mw.MwtDocAppendedInfo;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書メモ情報.
 */
public class DocAppendedInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 記載者会社コード */
	public String corporationCodeAppended;
	/** 記載者ユーザコード */
	public String userCodeAppended;
	/** 記載者ユーザ名称 */
	public String userNameAppended;
	/** 記載日時 */
	public String appendedDate;
	/** メモ */
	public String memo;
	/** 新規登録用ラベル文字. */
	public String labelNew;

	/** コンストラクタ(デフォルト). */
	public DocAppendedInfo() {
	}

	public DocAppendedInfo(MwtDocAppendedInfo e) {
		this.corporationCodeAppended = e.getCorporationCodeAppended();
		this.userCodeAppended = e.getUserCodeAppended();
		this.userNameAppended = e.getUserNameAppended();
		this.appendedDate = MiscUtils.FORMATTER_DATETIME.get().format(e.getAppendedDate());
		this.memo = e.getMemorandom();

		// 過去48時間以内の投稿なら「NEW」ラベルを表示
		Date to = MiscUtils.now();
		Date from = MiscUtils.addHour(to, -48);
		if (MiscUtils.between(e.getAppendedDate(), from, to)) {
			this.labelNew = "NEW";
		}
	}
}
