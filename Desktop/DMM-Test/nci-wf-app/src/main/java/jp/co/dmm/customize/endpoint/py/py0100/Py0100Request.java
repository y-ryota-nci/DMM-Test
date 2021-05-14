package jp.co.dmm.customize.endpoint.py.py0100;

import java.util.Date;
import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払依頼対象選択画面リクエスト
 */
public class Py0100Request extends BasePagingRequest {

	/** 会社コード */
	public String corporationCode;
	/** 組織コード */
	public String orgnzCd;

	/** 発注No */
	public String purordNo;
	/** 発注件名 */
	public String purordNm;

	/** 検収No */
	public String rcvinspNo;
	/** 検収件名 */
	public String rcvinspNm;

	/** 取引先 */
	public String splrNmKj;

	/** 発注申請者 */
	public String purordSbmtrNm;
	/** 検収申請者 */
	public String rcvinspSbmtrNm;

	/** 検収完了日FROM */
	public Date rcvinspDtFrom;
	/** 検収完了日TO */
	public Date rcvinspDtTo;

	/** 支払予定日FROM */
	public Date payPlnDtFrom;
	/** 支払予定日TO */
	public Date payPlnDtTo;

	/** 支払依頼状況:未支払依頼 */
	public boolean rcvinspStsNotYet;
	/** 支払依頼状況:支払依頼済 */
	public boolean rcvinspStsDone;

	/** 検索結果から除外する「検収No＋検収明細No」 */
	public List<String> excludeRcvinspNoList;

	/** 検索結果にて選択された「検収No＋検収明細No」 */
	public List<String> selectedRcvinspNoList;

}
