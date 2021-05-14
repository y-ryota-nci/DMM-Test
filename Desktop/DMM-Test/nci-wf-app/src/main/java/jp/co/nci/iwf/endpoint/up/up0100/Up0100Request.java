package jp.co.nci.iwf.endpoint.up.up0100;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * アップロード履歴画面のリクエスト
 */
public class Up0100Request extends BasePagingRequest {

	public String fileCorporationCode;
	public String fileCorporationName;
	public String fileName;
	public Date fileTimestampFrom;
	public Date fileTimestampTo;
	public String registeredFlag;
	public Date uploadDatetimeFrom;
	public Date uploadDatetimeTo;
	public String uploadKind;
	public String fileAppVersion;
	public Long uploadFileId;
}
