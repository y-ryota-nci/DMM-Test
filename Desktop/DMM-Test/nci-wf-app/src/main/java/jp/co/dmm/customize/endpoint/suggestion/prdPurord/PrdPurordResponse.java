package jp.co.dmm.customize.endpoint.suggestion.prdPurord;

import java.util.Date;
import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/** 定期発注条件登録のリクエスト */
public class PrdPurordResponse extends BaseResponse {
	/** 直近の定期支払日 */
	public Date lastPrdPayDt;
	/** 自動起票済みの定期発注予定マスタ */
	public List<PrdPurordPlnMst> results;
	/** 支払サイトマスタ */
	public PaySiteMst paySiteMst;
}
