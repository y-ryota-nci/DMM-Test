package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the BGT_PLN_HST database table.
 * 
 */
@Entity
@Table(name="BGT_PLN_HST")
@NamedQuery(name="BgtPlnHst.findAll", query="SELECT b FROM BgtPlnHst b")
public class BgtPlnHst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BgtPlnHstPK id;

	@Column(name="BGT_AMT_01")
	private BigDecimal bgtAmt01;

	@Column(name="BGT_AMT_02")
	private BigDecimal bgtAmt02;

	@Column(name="BGT_AMT_03")
	private BigDecimal bgtAmt03;

	@Column(name="BGT_AMT_04")
	private BigDecimal bgtAmt04;

	@Column(name="BGT_AMT_05")
	private BigDecimal bgtAmt05;

	@Column(name="BGT_AMT_06")
	private BigDecimal bgtAmt06;

	@Column(name="BGT_AMT_07")
	private BigDecimal bgtAmt07;

	@Column(name="BGT_AMT_08")
	private BigDecimal bgtAmt08;

	@Column(name="BGT_AMT_09")
	private BigDecimal bgtAmt09;

	@Column(name="BGT_AMT_10")
	private BigDecimal bgtAmt10;

	@Column(name="BGT_AMT_11")
	private BigDecimal bgtAmt11;

	@Column(name="BGT_AMT_12")
	private BigDecimal bgtAmt12;

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

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public BgtPlnHst() {
	}

	public BgtPlnHstPK getId() {
		return this.id;
	}

	public void setId(BgtPlnHstPK id) {
		this.id = id;
	}

	public BigDecimal getBgtAmt01() {
		return this.bgtAmt01;
	}

	public void setBgtAmt01(BigDecimal bgtAmt01) {
		this.bgtAmt01 = bgtAmt01;
	}

	public BigDecimal getBgtAmt02() {
		return this.bgtAmt02;
	}

	public void setBgtAmt02(BigDecimal bgtAmt02) {
		this.bgtAmt02 = bgtAmt02;
	}

	public BigDecimal getBgtAmt03() {
		return this.bgtAmt03;
	}

	public void setBgtAmt03(BigDecimal bgtAmt03) {
		this.bgtAmt03 = bgtAmt03;
	}

	public BigDecimal getBgtAmt04() {
		return this.bgtAmt04;
	}

	public void setBgtAmt04(BigDecimal bgtAmt04) {
		this.bgtAmt04 = bgtAmt04;
	}

	public BigDecimal getBgtAmt05() {
		return this.bgtAmt05;
	}

	public void setBgtAmt05(BigDecimal bgtAmt05) {
		this.bgtAmt05 = bgtAmt05;
	}

	public BigDecimal getBgtAmt06() {
		return this.bgtAmt06;
	}

	public void setBgtAmt06(BigDecimal bgtAmt06) {
		this.bgtAmt06 = bgtAmt06;
	}

	public BigDecimal getBgtAmt07() {
		return this.bgtAmt07;
	}

	public void setBgtAmt07(BigDecimal bgtAmt07) {
		this.bgtAmt07 = bgtAmt07;
	}

	public BigDecimal getBgtAmt08() {
		return this.bgtAmt08;
	}

	public void setBgtAmt08(BigDecimal bgtAmt08) {
		this.bgtAmt08 = bgtAmt08;
	}

	public BigDecimal getBgtAmt09() {
		return this.bgtAmt09;
	}

	public void setBgtAmt09(BigDecimal bgtAmt09) {
		this.bgtAmt09 = bgtAmt09;
	}

	public BigDecimal getBgtAmt10() {
		return this.bgtAmt10;
	}

	public void setBgtAmt10(BigDecimal bgtAmt10) {
		this.bgtAmt10 = bgtAmt10;
	}

	public BigDecimal getBgtAmt11() {
		return this.bgtAmt11;
	}

	public void setBgtAmt11(BigDecimal bgtAmt11) {
		this.bgtAmt11 = bgtAmt11;
	}

	public BigDecimal getBgtAmt12() {
		return this.bgtAmt12;
	}

	public void setBgtAmt12(BigDecimal bgtAmt12) {
		this.bgtAmt12 = bgtAmt12;
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