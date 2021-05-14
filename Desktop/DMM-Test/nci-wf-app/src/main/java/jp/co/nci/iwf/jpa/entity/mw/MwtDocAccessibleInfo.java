package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_ACCESSIBLE_INFO database table.
 *
 */
@Entity
@Table(name="MWT_DOC_ACCESSIBLE_INFO")
@NamedQuery(name="MwtDocAccessibleInfo.findAll", query="SELECT m FROM MwtDocAccessibleInfo m")
public class MwtDocAccessibleInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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

	public MwtDocAccessibleInfo() {
	}

	public long getDocAccessibleId() {
		return this.docAccessibleId;
	}

	public void setDocAccessibleId(long docAccessibleId) {
		this.docAccessibleId = docAccessibleId;
	}

	public String getAuthCopy() {
		return this.authCopy;
	}

	public void setAuthCopy(String authCopy) {
		this.authCopy = authCopy;
	}

	public String getAuthDelete() {
		return this.authDelete;
	}

	public void setAuthDelete(String authDelete) {
		this.authDelete = authDelete;
	}

	public String getAuthDownload() {
		return this.authDownload;
	}

	public void setAuthDownload(String authDownload) {
		this.authDownload = authDownload;
	}

	public String getAuthEdit() {
		return this.authEdit;
	}

	public void setAuthEdit(String authEdit) {
		this.authEdit = authEdit;
	}

	public String getAuthMove() {
		return this.authMove;
	}

	public void setAuthMove(String authMove) {
		this.authMove = authMove;
	}

	public String getAuthPrint() {
		return this.authPrint;
	}

	public void setAuthPrint(String authPrint) {
		this.authPrint = authPrint;
	}

	public String getAuthRefer() {
		return this.authRefer;
	}

	public void setAuthRefer(String authRefer) {
		this.authRefer = authRefer;
	}

	public String getBelongType() {
		return this.belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getHashValue() {
		return this.hashValue;
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
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}