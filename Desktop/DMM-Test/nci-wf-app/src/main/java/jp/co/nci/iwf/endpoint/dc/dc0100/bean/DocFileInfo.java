package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoHistoryEntity;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocFileDataEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書ファイル情報.
 */
public class DocFileInfo implements Serializable {

	/** 文書ファイルID */
	public Long docFileId;
	/** バージョン */
	public Long version;
	/** 企業コード */
	public String corporationCode;
	/** 文書ファイル番号 */
	public String docFileNum;
	/** 文書ID */
	public Long docId;
	/** メジャーバージョン */
	public Integer majorVersion;
	/** マイナーバージョン */
	public Integer minorVersion;
	/** コメント */
	public String comments;
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
	/** 文書ファイルデータID */
	public Long docFileDataId;
	/** ファイル名 */
	public String fileName;
	/** (親である文書情報の)タイトル */
	public String title;
	/** 登録会社CD */
	public String corporationCodeCreated;
	/** 登録者 */
	public String userCodeCreated;
	/** 登録日時 */
	public String timestampCreated;
	/** 登録者名 */
	public String userNameCreated;
	/** 更新会社CD */
	public String corporationCodeUpdated;
	/** 更新者 */
	public String userCodeUpdated;
	/** 更新日時 */
	public String timestampUpdated;
	/** 更新者名 */
	public String userNameUpdated;
	// 以下、操作者のこの文書ファイルに対するアクセス権限
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
	/** 操作者がファイルをロックしているか(1：ロック中) */
	public String locked;
	/** WF連携中か(1:連携中) */
	public String wfApplying;
	/** ワークフロー文書ファイルID */
	public Long docFileWfId;

	/** コンストラクタ(デフォルト). */
	public DocFileInfo() {
	}

	/** コンストラクタ. */
	public DocFileInfo(DocFileInfoEntity e, LoginInfo login) {
		this.docFileId = e.docFileId;
		this.version = e.version;
		this.corporationCode = e.corporationCode;
		this.docFileNum = e.docFileNum;
		this.docId = e.docId;
		this.majorVersion = e.majorVersion;
		this.minorVersion = e.minorVersion;
		this.comments = e.comments;
		this.lockFlag = e.lockFlag;
		if (e.lockTimestamp != null) {
			this.lockTimestamp = MiscUtils.FORMATTER_DATETIME.get().format(e.lockTimestamp);
		}
		this.lockCorporationCode = e.lockCorporationCode;
		this.lockCorporationName = e.lockCorporationName;
		this.lockUserCode = e.lockUserCode;
		this.lockUserName = e.lockUserName;
		this.docFileDataId = e.docFileDataId;
		this.timestampCreated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampCreated);
		this.userNameCreated = e.userNameCreated;
		this.timestampUpdated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampUpdated);
		this.userNameUpdated = e.userNameUpdated;
		this.fileName = e.fileName;
		this.title = e.title;
		this.authRefer = e.authRefer;
		this.authDownload = e.authDownload;
		this.authEdit = e.authEdit;
		this.authDelete = e.authDelete;
		this.authCopy = e.authCopy;
		this.authMove = e.authMove;
		this.authPrint = e.authPrint;
		// 操作者がロックしているか
		if (MiscUtils.eq(CommonFlag.ON, e.lockFlag) && MiscUtils.eq(e.lockCorporationCode, login.getCorporationCode()) && MiscUtils.eq(e.lockUserCode, login.getUserCode())) {
			this.locked = CommonFlag.ON;
		} else {
			this.locked = CommonFlag.OFF;
		}
		this.wfApplying = e.wfApplying;
		this.docFileWfId = e.docFileWfId;
	}

	/** コンストラクタ. */
	public DocFileInfo(DocFileInfoHistoryEntity e) {
		this.docFileId = e.docFileId;
		this.version = e.version;
		this.corporationCode = e.corporationCode;
		this.docFileNum = e.docFileNum;
		this.docId = e.docId;
		this.majorVersion = e.majorVersion;
		this.minorVersion = e.minorVersion;
		this.comments = e.comments;
		this.docFileDataId = e.docFileDataId;
		this.timestampCreated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampCreated);
		this.userNameCreated = e.userNameCreated;
		this.timestampUpdated = MiscUtils.FORMATTER_DATETIME.get().format(e.timestampUpdated);
		this.userNameUpdated = e.userNameUpdated;
		this.fileName = e.fileName;
		this.title = e.title;

	}

	/** コンストラクタ. */
	public DocFileInfo(MwtDocFileData e) {
		// ファイル登録時点ではバージョンは"1.0"固定
		this.majorVersion = 1;
		this.minorVersion = 0;
		this.docFileDataId = e.getDocFileDataId();
		this.fileName = e.getFileName();
	}

	/** コンストラクタ. */
	public DocFileInfo(MwtDocFileDataEx e) {
		// ファイル登録時点ではバージョンは"1.0"固定
		this.majorVersion = 1;
		this.minorVersion = 0;
		this.docFileDataId = e.getDocFileDataId();
		this.fileName = e.getFileName();
		this.corporationCodeCreated = e.getCorporationCodeCreated();
		this.userCodeCreated = e.getUserCodeCreated();
		this.timestampCreated = MiscUtils.FORMATTER_DATETIME.get().format(e.getTimestampCreated());
		this.userNameCreated = e.getUserNameCreated();
		this.corporationCodeUpdated = e.getCorporationCodeUpdated();
		this.userCodeUpdated = e.getUserCodeUpdated();
		this.timestampUpdated = MiscUtils.FORMATTER_DATETIME.get().format(e.getTimestampUpdated());
		this.userNameUpdated = e.getUserNameUpdated();
	}
}
