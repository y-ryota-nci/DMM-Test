package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.ex.MwmDocFolderAccessibleInfoEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocAccessibleInfoEx;

/**
 * 文書権限情報.
 */
public class DocAccessibleInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書権限ID */
	public Long docAccessibleId;
	/** 文書ID */
	public Long docId;
	/** 連番 */
	public Integer seqNo;
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
	/** 所属区分 */
	public String belongType;
	/** ハッシュ値 */
	public String hashValue;
	/** 企業コード */
	public String corporationCode;
	/** ロールコード */
	public String roleCode;
	/** ロール名称 */
	public String roleName;
	/** ユーザコード */
	public String userCode;
	/** ユーザ名称 */
	public String userName;

	/** コンストラクタ. */
	public DocAccessibleInfo() {
	}

	/** コンストラクタ. */
	public DocAccessibleInfo(MwtDocAccessibleInfoEx entity) {
		this.docAccessibleId = entity.getDocAccessibleId();
		this.docId = entity.getDocId();
		this.seqNo = entity.getSeqNo();
		this.authRefer = entity.getAuthRefer();
		this.authDownload = entity.getAuthDownload();
		this.authEdit = entity.getAuthEdit();
		this.authDelete = entity.getAuthDelete();
		this.authCopy = entity.getAuthCopy();
		this.authMove = entity.getAuthMove();
		this.authPrint = entity.getAuthPrint();
		this.belongType = entity.getBelongType();
		this.corporationCode = entity.getCorporationCode();
		this.roleCode = entity.getAssignRoleCode();
		this.roleName = entity.getAssignRoleName();
		this.userCode = entity.getUserCode();
		this.userName = entity.getUserName();
	}

	/** コンストラクタ. */
	public DocAccessibleInfo(MwmDocFolderAccessibleInfoEx entity) {
		this.docAccessibleId = null;
		this.docId = null;
		this.seqNo = null;
		this.authRefer = entity.getAuthRefer();
		this.authDownload = entity.getAuthDownload();
		this.authEdit = entity.getAuthEdit();
		this.authDelete = entity.getAuthDelete();
		this.authCopy = entity.getAuthCopy();
		this.authMove = entity.getAuthMove();
		this.authPrint = entity.getAuthPrint();
		this.belongType = entity.getBelongType();
		this.corporationCode = entity.getCorporationCode();
		this.roleCode = entity.getAssignRoleCode();
		this.roleName = entity.getAssignRoleName();
		this.userCode = entity.getUserCode();
		this.userName = entity.getUserName();
	}
}
