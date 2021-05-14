package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;

/**
 * 文書属性情報.
 */
public class DocAttributeInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 表示回数 */
	public Integer dispCount;
	/** 文書フォルダID */
	public Long docFolderId;
	/** フォルダパス */
	public String folderPath;
	/** 文書責任者企業コード */
	public String ownerCorporationCode;
	/** 文書責任者ユーザコード */
	public String ownerUserCode;
	/** 文書責任者ユーザ名称 */
	public String ownerUserName;
	/** 公開区分(コード) */
	public String publishFlag;
	/** 公開区分(名称) */
	public String publishFlagName;
	/** 公開開始日 */
	public Date publishStartDate;
	/** 公開終了日 */
	public Date publishEndDate;
	/** 登録日時 */
	public String timestampCreated;
	/** 登録者名 */
	public String userNameCreated;
	/** 更新日時 */
	public String timestampUpdated;
	/** 更新者 */
	public String userNameUpdated;

	/** コンストラクタ(デフォルト). */
	public DocAttributeInfo() {
	}

	/** コンストラクタ. */
	public DocAttributeInfo(DocInfoEntity e) {
		this.dispCount = e.dispCount;
		this.docFolderId = e.docFolderId;
		this.folderPath = null;
		this.ownerCorporationCode = e.ownerCorporationCode;
		this.ownerUserCode = e.ownerUserCode;
		this.ownerUserName = e.ownerUserName;
		this.publishFlag = e.publishFlag;
		this.publishFlagName = e.publishFlagName;
		this.publishStartDate = e.publishStartDate;
		this.publishEndDate = e.publishEndDate;
		if (e.timestampCreated != null) {
			this.timestampCreated = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(e.timestampCreated);
		}
		this.userNameCreated = e.userNameCreated;
		if (e.timestampUpdated != null) {
			this.timestampUpdated = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(e.timestampUpdated);
		}
		this.userNameUpdated = e.userNameUpdated;
	}
}
