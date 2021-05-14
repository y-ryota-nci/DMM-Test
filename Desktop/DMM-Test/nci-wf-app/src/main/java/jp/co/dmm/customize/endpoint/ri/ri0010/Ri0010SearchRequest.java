package jp.co.dmm.customize.endpoint.ri.ri0010;

import java.sql.Date;
import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 検収対象選択の検索リクエスト
 */
public class Ri0010SearchRequest extends BasePagingRequest {
	/** 発注No */
	public String purordNo;
	/** 会社コード */
	public String corporationCode;
	/** 発注件名 */
	public String purordNm;
	/** 通貨 */
	public String mnyCd;
	/** 品目コード */
	public String itmCd;
	public String itmNm;
	/** 取引先コード */
	public String splrCd;
	public String splrNmKj;
	public String splrNmKn;
	/** 発注申請者コード */
	public String sbmtrCd;
	public String sbmtrNm;
	/** 発注依頼日From, To */
	public Date purordRqstDtFrom;
	public Date purordRqstDtTo;
	/** 検収予定日From/To */
	public Date inspCompDtFrom;
	public Date inspCompDtTo;
	/** 支払予定日From/To */
	public Date payPlnDtFrom;
	public Date payPlnDtTo;
	/** ステータス：発注済 */
	public boolean purordStsPurordFixed;
	/** ステータス：検収済 */
	public boolean purordStsRcvinspFixed;
	/** ステータス：支払済 */
	public boolean purordStsPayFixed;
	/** ステータス：キャンセル */
	public boolean purordStsCancel;
	/** 第三階層組織(部・室) */
	public String orgnzCd;

	/** 検索結果から除外する「発注No＋発注明細No」 */
	public List<String> excludePurOrdNoList;
}
