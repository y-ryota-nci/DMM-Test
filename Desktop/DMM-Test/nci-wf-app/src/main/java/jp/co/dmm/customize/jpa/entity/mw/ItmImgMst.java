package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the ITM_IMG_MST database table.
 * 
 */
@Entity
@Table(name="ITM_IMG_MST")
@NamedQuery(name="ItmImgMst.findAll", query="SELECT i FROM ItmImgMst i")
public class ItmImgMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItmImgMstPK id;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NM")
	private String fileNm;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_CD")
	private String itmCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	private BigDecimal vrsn;

	public ItmImgMst() {
	}

	public ItmImgMstPK getId() {
		return this.id;
	}

	public void setId(ItmImgMstPK id) {
		this.id = id;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public byte[] getFileData() {
		return this.fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileNm() {
		return this.fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getItmCd() {
		return this.itmCd;
	}

	public void setItmCd(String itmCd) {
		this.itmCd = itmCd;
	}

	public String getOrgnzCd() {
		return this.orgnzCd;
	}

	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public BigDecimal getVrsn() {
		return this.vrsn;
	}

	public void setVrsn(BigDecimal vrsn) {
		this.vrsn = vrsn;
	}

}