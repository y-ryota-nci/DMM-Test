package jp.co.dmm.customize.endpoint.po.po0010;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 発注一覧の検索リクエスト
 */
public class Po0010SearchRequest extends BasePagingRequest {
	/** 発注No */
	public String purordNo;
	/** 発注件名 */
	public String purordNm;
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称 */
	public String splrNmKj;
	/** 発注申請者コード */
	public String sbmtrCd;
	public String sbmtrNm;
	/** 発注依頼日From */
	public Date purordRqstDtFrom;
	/** 発注依頼日To */
	public Date purordRqstDtTo;
	/** 購入依頼No */
	public String purrqstNo;
	/** 契約書No */
	public String cntrctNo;
	/** 発注区分：通常発注 */
	public boolean purordTpNormal;
	/** 発注区分：Web発注 */
	public boolean purordTpRoutine;
	/** 発注区分：集中購買 */
	public boolean purordTpFocus;
	/** 発注区分：経費 */
	public boolean purordTpExpense;
	/** 納期（納品予定日）FROM */
	public Date dlvPlnDtFrom;
	/** 納期（納品予定日）TO */
	public Date dlvPlnDtTo;
	/** 検収予定日FROM */
	public Date inspCompDtFrom;
	/** 検収予定日TO */
	public Date inspCompDtTo;
	/** ステータス：発注済 */
	public boolean purordStsPurordFixed;
	/** ステータス：検収済 */
	public boolean purordStsRcvinspFixed;
	/** ステータス：支払済 */
	public boolean purordStsPayFixed;
	/** ステータス：キャンセル */
	public boolean purordStsCancel;
}
