package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the RTN_PAY_PLN_MST database table.
 * 
 */
@Entity
@Table(name="RTN_PAY_PLN_MST")
@NamedQuery(name="RtnPayPlnMst.findAll", query="SELECT r FROM RtnPayPlnMst r")
public class RtnPayPlnMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtnPayPlnMstPK id;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="PAY_NO")
	private String payNo;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYSBMT_BTCH_PLN_DT")
	private Date paysbmtBtchPlnDt;

	@Temporal(TemporalType.DATE)
	@Column(name="PAYSBMT_BTCH_RSLT_DT")
	private Date paysbmtBtchRsltDt;

	@Column(name="PAYSBMT_BTCH_TP")
	private String paysbmtBtchTp;

	@Temporal(TemporalType.DATE)
	@Column(name="PURORD_BTCH_PLN_DT")
	private Date purordBtchPlnDt;

	@Temporal(TemporalType.DATE)
	@Column(name="PURORD_BTCH_RSLT_DT")
	private Date purordBtchRsltDt;

	@Column(name="PURORD_BTCH_TP")
	private String purordBtchTp;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSP_BTCH_PLN_DT")
	private Date rcvinspBtchPlnDt;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSP_BTCH_RSLT_DT")
	private Date rcvinspBtchRsltDt;

	@Column(name="RCVINSP_BTCH_TP")
	private String rcvinspBtchTp;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public RtnPayPlnMst() {
	}

	public RtnPayPlnMstPK getId() {
		return this.id;
	}

	public void setId(RtnPayPlnMstPK id) {
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

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Date getPaysbmtBtchPlnDt() {
		return this.paysbmtBtchPlnDt;
	}

	public void setPaysbmtBtchPlnDt(Date paysbmtBtchPlnDt) {
		this.paysbmtBtchPlnDt = paysbmtBtchPlnDt;
	}

	public Date getPaysbmtBtchRsltDt() {
		return this.paysbmtBtchRsltDt;
	}

	public void setPaysbmtBtchRsltDt(Date paysbmtBtchRsltDt) {
		this.paysbmtBtchRsltDt = paysbmtBtchRsltDt;
	}

	public String getPaysbmtBtchTp() {
		return this.paysbmtBtchTp;
	}

	public void setPaysbmtBtchTp(String paysbmtBtchTp) {
		this.paysbmtBtchTp = paysbmtBtchTp;
	}

	public Date getPurordBtchPlnDt() {
		return this.purordBtchPlnDt;
	}

	public void setPurordBtchPlnDt(Date purordBtchPlnDt) {
		this.purordBtchPlnDt = purordBtchPlnDt;
	}

	public Date getPurordBtchRsltDt() {
		return this.purordBtchRsltDt;
	}

	public void setPurordBtchRsltDt(Date purordBtchRsltDt) {
		this.purordBtchRsltDt = purordBtchRsltDt;
	}

	public String getPurordBtchTp() {
		return this.purordBtchTp;
	}

	public void setPurordBtchTp(String purordBtchTp) {
		this.purordBtchTp = purordBtchTp;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public Date getRcvinspBtchPlnDt() {
		return this.rcvinspBtchPlnDt;
	}

	public void setRcvinspBtchPlnDt(Date rcvinspBtchPlnDt) {
		this.rcvinspBtchPlnDt = rcvinspBtchPlnDt;
	}

	public Date getRcvinspBtchRsltDt() {
		return this.rcvinspBtchRsltDt;
	}

	public void setRcvinspBtchRsltDt(Date rcvinspBtchRsltDt) {
		this.rcvinspBtchRsltDt = rcvinspBtchRsltDt;
	}

	public String getRcvinspBtchTp() {
		return this.rcvinspBtchTp;
	}

	public void setRcvinspBtchTp(String rcvinspBtchTp) {
		this.rcvinspBtchTp = rcvinspBtchTp;
	}

	public String getRcvinspNo() {
		return this.rcvinspNo;
	}

	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
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

}