package jp.co.dmm.customize.endpoint.ri.ri0030;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 検収一覧の検索リクエスト
 */
public class Ri0030SearchRequest extends BasePagingRequest {
	/** 検収No */
	public String rcvinspNo;
	/** 取引先コード */
	public String splrCd;
	public String splrNmKj;
	public String splrNmKn;
	/** 納品日From */
	public Date dlvDtFrom;
	/** 納品日To */
	public Date dlvDtTo;
	/** 検収日From */
	public Date rcvinspDtFrom;
	/** 検収日To */
	public Date rcvinspDtTo;
	/** ステータス：発注済 */
	public boolean rcvinspStsRcvinspFixed;
	/** ステータス：検収済 */
	public boolean rcvinspStsPayFixed;
	/** ステータス：キャンセル */
	public boolean rcvinspStsCancel;
}
