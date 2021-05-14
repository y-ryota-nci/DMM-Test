package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * MW*_系テーブルの共通列を定義したインターフェース
 */
public interface MwCommonColumns extends Serializable {

	String getDeleteFlag();
	void setDeleteFlag(String deleteFlag);

	String getCorporationCodeCreated();
	void setCorporationCodeCreated(String corporationCodeCreated);

	String getCorporationCodeUpdated();
	void setCorporationCodeUpdated(String corporationCodeUpdated);

	Timestamp getTimestampCreated();
	void setTimestampCreated(Timestamp timestampCreated);

	Timestamp getTimestampUpdated();
	void setTimestampUpdated(Timestamp timestampUpdated);

	String getUserCodeCreated();
	void setUserCodeCreated(String userCodeCreated);

	String getUserCodeUpdated();
	void setUserCodeUpdated(String userCodeUpdated);

	Long getVersion();
	void setVersion(Long version);
}
