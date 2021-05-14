package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;
import java.util.Date;

import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書情報(共通).
 */
public class DocInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	// 以下、文書情報
	/** 文書ID */
	public Long docId;
	/** 企業コード */
	public String corporationCode;
	/** コンテンツ種別 */
	public String contentsType;
	/** メジャーバージョン */
	public Integer majorVersion;
	/** マイナーバージョン */
	public Integer minorVersion;
	/** 件名 */
	public String title;
	/** コメント */
	public String comments;
	/** 表示回数 */
	public Integer dispCount;
	/** 文書フォルダID */
	public Long docFolderId;
	/** フォルダパス */
	public String folderPath;
	/** 公開日時 */
	public String publishTimestamp;
	/** 公開企業コード */
	public String publishCorporationCode;
	/** 公開企業名称 */
	public String publishCorporationName;
	/** 公開ユーザコード */
	public String publishUserCode;
	/** 公開ユーザ名称 */
	public String publishUserName;
	/** 文書責任者企業コード */
	public String ownerCorporationCode;
	/** 文書責任者企業名称 */
	public String ownerCorporationName;
	/** 文書責任者ユーザコード */
	public String ownerUserCode;
	/** 文書責任者ユーザ名称 */
	public String ownerUserName;
	/** 公開区分(コード) */
	public String publishFlag;
	/** 公開開始日 */
	public Date publishStartDate;
	/** 公開終了日 */
	public Date publishEndDate;
	/** 保存期間区分 */
	public String retentionTermType;
	/** 保存期間 */
	public Integer retentionTerm;
	/** 保存期間(年) */
	public Integer retentionTermYear;
	/** 保存期間(月) */
	public Integer retentionTermMonths;
//	/** 表示用保存期間 */
//	public String dispRetentionTerm;
	/** ロックフラグ(1：ロック中) */
	public String lockFlag;
	/** ロック日時 */
	public String lockTimestamp;
	/** ロック企業コード */
	public String lockCorporationCode;
	/** ロック企業名称 */
	public String lockCorporationName;
	/** ロックユーザコード */
	public String lockUserCode;
	/** ロックユーザ名称 */
	public String lockUserName;
	/** 登録日時 */
	public String timestampCreated;
	/** 登録者名 */
	public String userNameCreated;
	/** 更新日時 */
	public String timestampUpdated;
	/** 更新者 */
	public String userNameUpdated;
	/** プロセスID（WF連携により設定される） */
	public Long processId;
	// 以下、操作者のこの文書に対するアクセス権限
	/** 参照権限 */
	public String authRefer;
	/** ダウンロード権限 */
	public String authDownload;
	/** 編集権限 */
	public String authEdit;
	/** 削除権限 */
	public String authDelete;
	/** コピー権限 */
	public String authCopy;
	/** 移動権限 */
	public String authMove;
	/** 印刷権限 */
	public String authPrint;
	/** 公開区分(名称) */
	public String publishFlagName;
	/** 保存期間区分名称 */
	public String retentionTermTypeName;
	/** 操作者自身がロック中か(1：ロック中) */
	public String ownLockFlag;
	/** WF連携中か(1：連携中) */
	// WF連携した申請書が申請／承認中なら連携中とする
	public String wfApplying;


	/** コンストラクタ(デフォルト). */
	public DocInfo() {
	}

	/** コンストラクタ. */
	public DocInfo(DocInfoEntity e) {
		this.docId = e.docId;
		this.corporationCode = e.corporationCode;
		this.contentsType = e.contentsType;
		this.majorVersion = e.majorVersion;
		this.minorVersion = e.minorVersion;
		this.title = e.title;
		this.comments = e.comments;
		this.dispCount = e.dispCount;
		this.docFolderId = e.docFolderId;
		this.folderPath = e.folderPath;
		if (e.publishTimestamp != null) {
			this.publishTimestamp = MiscUtils.FORMATTER_DATETIME.get().format(e.publishTimestamp);
		}
		this.publishCorporationCode = e.publishCorporationCode;
		this.publishCorporationName = e.publishCorporationName;
		this.publishUserCode = e.publishUserCode;
		this.publishUserName = e.publishUserName;
		this.ownerCorporationCode = e.ownerCorporationCode;
		this.ownerCorporationName = e.ownerCorporationName;
		this.ownerUserCode = e.ownerUserCode;
		this.ownerUserName = e.ownerUserName;
		this.publishFlag = e.publishFlag;
		this.publishStartDate = e.publishStartDate;
		this.publishEndDate = e.publishEndDate;
		this.retentionTermType = e.retentionTermType;
		this.retentionTerm = e.retentionTerm;
		if (MiscUtils.isNotEmpty(e.retentionTerm)) {
			Integer retentionTermYear = (e.retentionTerm / 12);
			Integer retentionTermMonths = (e.retentionTerm % 12);
			this.retentionTermYear = retentionTermYear;
			this.retentionTermMonths = retentionTermMonths;
		}
		if (e.timestampCreated != null) {
			this.timestampCreated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampCreated);
		}
		this.userNameCreated = e.userNameCreated;
		if (e.timestampUpdated != null) {
			this.timestampUpdated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampUpdated);
		}
		this.userNameUpdated = e.userNameUpdated;
		this.lockFlag = e.lockFlag;
		if (e.lockTimestamp != null) {
			this.lockTimestamp = MiscUtils.FORMATTER_DATETIME.get().format(e.lockTimestamp);
		}
		this.lockCorporationCode = e.lockCorporationCode;
		this.lockCorporationName = e.lockCorporationName;
		this.lockUserCode = e.lockUserCode;
		this.lockUserName = e.lockUserName;
		this.authRefer = e.authRefer;
		this.authDownload = e.authDownload;
		this.authEdit = e.authEdit;
		this.authDelete = e.authDelete;
		this.authCopy = e.authCopy;
		this.authPrint = e.authPrint;
		this.publishFlagName = e.publishFlagName;
		this.retentionTermTypeName = e.retentionTermTypeName;
		this.ownLockFlag = e.ownLockFlag;
		this.wfApplying = e.wfApplying;
		this.processId = e.processId;
	}
}
