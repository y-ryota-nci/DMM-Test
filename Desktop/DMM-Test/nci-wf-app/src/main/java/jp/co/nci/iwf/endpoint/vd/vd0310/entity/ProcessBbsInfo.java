package jp.co.nci.iwf.endpoint.vd.vd0310.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.bean.BbsAttachFileWfInfo;
import jp.co.nci.iwf.jpa.entity.ex.MwtProcessBbsEx;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * プロセス掲示板エンティティ
 */
public class ProcessBbsInfo implements Serializable {
	/** プロセス掲示板ID */
	public Long processBbsId;
	/** 親プロセス掲示板ID */
	public Long processBbsIdUp;
	/** 投稿者企業コード */
	public String corporationCodeSubmit;
	/** 投稿者ユーザコード */
	public String userCodeSubmit;
	/** 投稿者氏名 */
	public String userNameSubmit;
	/** 投稿者組織コード */
	public String organizationCodeSubmit;
	/** 投稿者組織名 */
	public String organizationNameSubmit;
	/** 投稿者役職コード */
	public String postCodeSubmit;
	/** 投稿者役職名 */
	public String postNameSubmit;
	/** 投稿者所属（＝組織コード＋"@"＋役職コード） */
	public String belong;
	/** 投稿日時 */
	public String timestampSubmit;
	/** プロセス掲示板メール区分  */
	public String processBbsMailType;
	/** 記事内容 */
	public String contents;
	/** NEWラベル */
	public String labelNew;
	/** 削除区分 */
	public String deleteFlag;
	/** 返信リスト */
	public List<ProcessBbsInfo> replies;
	/** 添付ファイルリスト */
	public List<BbsAttachFileWfInfo> attachFiles;

	/** コンストラクタ */
	public ProcessBbsInfo() {
	}

	/** コンストラクタ */
	public ProcessBbsInfo(MwtProcessBbsEx src) {
		MiscUtils.copyFieldsAndProperties(src, this);
		timestampSubmit = MiscUtils.FORMATTER_DATETIME.get().format(src.timestampSubmit);

		// 過去48時間以内の投稿なら「NEW」ラベルを表示
		Date to = MiscUtils.now();
		Date from = MiscUtils.addHour(to, -48);
		if (MiscUtils.between(src.timestampSubmit, from, to)) {
			this.labelNew = "NEW";
		}
	}
}
