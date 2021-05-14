package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;

/**
 * 文書バージョン情報.
 */
public class DocVersionInfo implements Serializable {

	/** 版管理：メジャーバージョン */
	public Integer majorVersion;
	/** 版管理：マイナーバージョン */
	public Integer minorVersion;
	/** 版管理：バージョン更新区分 */
	public String updateVersionType;
	/** ロック解除(0：ロック解除しない、1：ロック解除する) */
	public String unlockFlag;

	/** コンストラクタ(デフォルト). */
	public DocVersionInfo() {
	}

	/** コンストラクタ. */
	public DocVersionInfo(DocInfoEntity e) {
		this.majorVersion = e.majorVersion;
		this.minorVersion = e.minorVersion;
		this.updateVersionType = DcCodeBook.UpdateVersionType.DO_NOT_UPDATE;	// 初期値は「0：版を更新しない」
		this.unlockFlag = CommonFlag.OFF;	// 初期値は「0：ロック解除しない」
	}
}
