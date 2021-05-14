package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BizDocInfoEntity;

/**
 * 業務文書情報.
 */
public class BizDocInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 業務文書ID */
	public Long bizDocId;
	/** 画面文書定義ID */
	public Long screenDocId;
	/** トランザクションID(プロセスID) */
	public Long processId;
	/** WF連携先画面プロセス定義コード */
	public String screenProcessCode;
	/** WF→文書管理連携か */
	public String cooperateWf2DocFlag;

	/** コンストラクタ(デフォルト). */
	public BizDocInfo() {
	}

//	/** コンストラクタ. */
//	public BizDocInfo(DocInfoEntity e) {
//		this.docContentsId = e.docContentsId;
//		this.docVersionId = e.docVersionId;
//		this.bizDocId = e.bizDocId;
//		this.screenDocId = e.screenDocId;
//		this.processId = e.processId;
//	}

	/** コンストラクタ. */
	public BizDocInfo(BizDocInfoEntity e) {
		this.bizDocId = e.bizDocId;
		this.screenDocId = e.screenDocId;
		this.processId = e.tranId;
		this.screenProcessCode = e.screenProcessCode;
	}
}
