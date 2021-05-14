package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BinderInfoEntity;

/**
 * バインダー情報.
 */
public class BinderInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** バインダーID */
	public Long binderId;
	/** 文書ID */
	public Long docId;

	/** コンストラクタ(デフォルト). */
	public BinderInfo() {
	}

	/** コンストラクタ. */
	public BinderInfo(BinderInfoEntity e) {
		this.binderId = e.binderId;
		this.docId = e.docId;
	}
}
