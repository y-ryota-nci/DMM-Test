package jp.co.nci.iwf.endpoint.al.al0010;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * アクセスログ検索の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Al0010Entity extends BaseJpaEntity {

	@Id
	@Column(name="ACCESS_LOG_ID")
	public long accessLogId;

	@Column(name="ACCESS_LOG_RESULT_TYPE")
	public String accessLogResultType;

	@Column(name="ACCESS_LOG_RESULT_TYPE_NAME")
	public String accessLogResultTypeName;

	@Column(name="ACCESS_TIME")
	public Timestamp accessTime;

	@Column(name="ACCESS_TM")
	public String accessTm;

	@Column(name="APP_VERSION")
	public String appVersion;

	@Column(name="DB_CONNECT_STRING")
	public String dbConnectString;

	@Column(name="HOST_IP_ADDRESS")
	public String hostIpAddress;

	@Column(name="HOST_PORT")
	public Integer hostPort;

	@Column(name="OPE_CORPORATION_CODE")
	public String opeCorporationCode;

	@Column(name="OPE_CORPORATION_NAME")
	public String opeCorporationName;

	@Column(name="OPE_IP_ADDRESS")
	public String opeIpAddress;

	@Column(name="OPE_USER_ADDED_INFO")
	public String opeUserAddedInfo;

	@Column(name="OPE_USER_CODE")
	public String opeUserCode;

	@Column(name="OPE_USER_NAME")
	public String opeUserName;

	@Column(name="SCREEN_ID")
	public String screenId;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="ACTION_NAME")
	public String actionName;

	@Column(name="SESSION_ID")
	public String sessionId;

	@Column(name="SPOOFING_CORPORATION_CODE")
	public String spoofingCorporationCode;

	@Column(name="SPOOFING_USER_CODE")
	public String spoofingUserCode;

	@Column(name="SPOOFING_USER_ADDED_INFO")
	public String spoofingUserAddedInfo;

	@Column(name="SPOOFING_USER_NAME")
	public String spoofingUserName;

	@Column(name="SPOOFING_SIGN")
	public String spoofingSign;

	@Column(name="THREAD_NAME")
	public String threadName;

	public String uri;

	@Column(name="USER_AGENT")
	public String userAgent;
}
