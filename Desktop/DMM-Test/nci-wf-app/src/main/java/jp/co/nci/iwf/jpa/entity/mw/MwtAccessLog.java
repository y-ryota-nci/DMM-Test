package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_ACCESS_LOG database table.
 *
 */
@Entity
@Table(name="MWT_ACCESS_LOG")
@NamedQuery(name="MwtAccessLog.findAll", query="SELECT m FROM MwtAccessLog m")
public class MwtAccessLog extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESS_LOG_ID")
	private long accessLogId;

	@Column(name="ACCESS_LOG_RESULT_TYPE")
	private String accessLogResultType;

	@Column(name="ACCESS_TIME")
	private Timestamp accessTime;

	@Column(name="APP_VERSION")
	private String appVersion;

	@Column(name="DB_CONNECT_STRING")
	private String dbConnectString;

	@Column(name="HOST_IP_ADDRESS")
	private String hostIpAddress;

	@Column(name="HOST_PORT")
	private Integer hostPort;

	@Column(name="OPE_CORPORATION_CODE")
	private String opeCorporationCode;

	@Column(name="OPE_IP_ADDRESS")
	private String opeIpAddress;

	@Column(name="OPE_USER_ADDED_INFO")
	private String opeUserAddedInfo;

	@Column(name="OPE_USER_CODE")
	private String opeUserCode;

	@Column(name="SCREEN_ID")
	private String screenId;

	@Column(name="SCREEN_NAME")
	private String screenName;

	@Column(name="ACTION_NAME")
	private String actionName;

	@Column(name="SESSION_ID")
	private String sessionId;

	private String uri;

	@Column(name="USER_AGENT")
	private String userAgent;

	@Column(name="SPOOFING_CORPORATION_CODE")
	private String spoofingCorporationCode;

	@Column(name="SPOOFING_USER_CODE")
	private String spoofingUserCode;

	@Column(name="SPOOFING_USER_ADDED_INFO")
	private String spoofingUserAddedInfo;

	@Column(name="THREAD_NAME")
	private String threadName;

	public MwtAccessLog() {
	}

	public long getAccessLogId() {
		return this.accessLogId;
	}

	public void setAccessLogId(long accessLogId) {
		this.accessLogId = accessLogId;
	}

	public String getAccessLogResultType() {
		return this.accessLogResultType;
	}

	public void setAccessLogResultType(String accessLogResultType) {
		this.accessLogResultType = accessLogResultType;
	}

	public Timestamp getAccessTime() {
		return this.accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDbConnectString() {
		return dbConnectString;
	}

	public void setDbConnectString(String dbConnectString) {
		this.dbConnectString = dbConnectString;
	}

	public String getHostIpAddress() {
		return hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getOpeCorporationCode() {
		return this.opeCorporationCode;
	}

	public void setOpeCorporationCode(String opeCorporationCode) {
		this.opeCorporationCode = opeCorporationCode;
	}

	public String getOpeIpAddress() {
		return this.opeIpAddress;
	}

	public void setOpeIpAddress(String opeIpAddress) {
		this.opeIpAddress = opeIpAddress;
	}

	public String getOpeUserAddedInfo() {
		return this.opeUserAddedInfo;
	}

	public void setOpeUserAddedInfo(String opeUserAddedInfo) {
		this.opeUserAddedInfo = opeUserAddedInfo;
	}

	public String getOpeUserCode() {
		return this.opeUserCode;
	}

	public void setOpeUserCode(String opeUserCode) {
		this.opeUserCode = opeUserCode;
	}

	public String getScreenId() {
		return this.screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}

	public String getSpoofingCorporationCode() {
		return spoofingCorporationCode;
	}

	public void setSpoofingCorporationCode(String spoofingCorporationCode) {
		this.spoofingCorporationCode = spoofingCorporationCode;
	}

	public String getSpoofingUserCode() {
		return spoofingUserCode;
	}

	public void setSpoofingUserCode(String spoofingUserCode) {
		this.spoofingUserCode = spoofingUserCode;
	}

	public String getSpoofingUserAddedInfo() {
		return spoofingUserAddedInfo;
	}

	public void setSpoofingUserAddedInfo(String spoofingUserAddedInfo) {
		this.spoofingUserAddedInfo = spoofingUserAddedInfo;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}