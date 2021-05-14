package jp.co.dmm.customize.endpoint.batch.purord;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.PurorddtlInf;

/**
 * 定期発注関連情報
 *
 */
public class PurOrdEntity {

	/** よく使う企業コード */
	public String companyCd;

	/** 支払情報 */
	public PurordInf purordInf;
	/** 支払情報明細一覧 */
	public List<PurorddtlInf> purorddtlInfList;

	/** 支払予約登録マスタ */
	public PrdPurordMst prdPurordMst;
	/** 支払予約明細マスタ */
	public PrdPurordPlnMst prdPurordPlnMst;

	/** 支払サイトマスタ */
	public PaySiteMst paySiteMst;
	/** 源泉税区分マスタ */
	public HldtaxMst hldtaxMst;

	/** 発注対象フラグ */
	public boolean isPreOrderFlg = false;
}
