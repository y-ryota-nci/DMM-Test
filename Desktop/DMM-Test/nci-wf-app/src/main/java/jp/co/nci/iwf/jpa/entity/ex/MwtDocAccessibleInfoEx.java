package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * The persistent class for the MWM_DOC_ACCESSIBLE_INFO database table.
 *
 */
@Entity
public class MwtDocAccessibleInfoEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="DOC_ACCESSIBLE_ID")
	private long docAccessibleId;

	@Column(name="AUTH_COPY")
	private String authCopy;

	@Column(name="AUTH_DELETE")
	private String authDelete;

	@Column(name="AUTH_DOWNLOAD")
	private String authDownload;

	@Column(name="AUTH_EDIT")
	private String authEdit;

	@Column(name="AUTH_MOVE")
	private String authMove;

	@Column(name="AUTH_PRINT")
	private String authPrint;

	@Column(name="AUTH_REFER")
	private String authRefer;

	@Column(name="BELONG_TYPE")
	private String belongType;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="HASH_VALUE")
	private String hashValue;

	@Column(name="ASSIGN_ROLE_CODE")
	private String assignRoleCode;

	@Column(name="SEQ_NO")
	private Integer seqNo;

	@Column(name="USER_CODE")
	private String userCode;

	@Column(name="ASSIGN_ROLE_NAME")
	private String assignRoleName;

	@Column(name="USER_NAME")
	private String userName;

	public long getDocAccessibleId() {
		return docAccessibleId;
	}

	public void setDocAccessibleId(long docAccessibleId) {
		this.docAccessibleId = docAccessibleId;
	}

	public String getAuthCopy() {
		return authCopy;
	}

	public void setAuthCopy(String authCopy) {
		this.authCopy = authCopy;
	}

	public String getAuthDelete() {
		return authDelete;
	}

	public void setAuthDelete(String authDelete) {
		this.authDelete = authDelete;
	}

	public String getAuthDownload() {
		return authDownload;
	}

	public void setAuthDownload(String authDownload) {
		this.authDownload = authDownload;
	}

	public String getAuthEdit() {
		return authEdit;
	}

	public void setAuthEdit(String authEdit) {
		this.authEdit = authEdit;
	}

	public String getAuthMove() {
		return authMove;
	}

	public void setAuthMove(String authMove) {
		this.authMove = authMove;
	}

	public String getAuthPrint() {
		return authPrint;
	}

	public void setAuthPrint(String authPrint) {
		this.authPrint = authPrint;
	}

	public String getAuthRefer() {
		return authRefer;
	}

	public void setAuthRefer(String authRefer) {
		this.authRefer = authRefer;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public String getAssignRoleCode() {
		return this.assignRoleCode;
	}

	public void setAssignRoleCode(String assignRoleCode) {
		this.assignRoleCode = assignRoleCode;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getAssignRoleName() {
		return assignRoleName;
	}

	public void setAssignRoleName(String assignRoleName) {
		this.assignRoleName = assignRoleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
