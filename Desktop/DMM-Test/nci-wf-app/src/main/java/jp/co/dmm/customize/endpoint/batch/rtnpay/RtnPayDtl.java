package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.CntrctInf;
import jp.co.dmm.customize.jpa.entity.mw.CntrctSplrInf;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.dmm.customize.jpa.entity.mw.RtnPayMst;
import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMst;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;

/**
 * 経常支払情報保持用クラス
 *
 */
public class RtnPayDtl {

	/** 経常支払マスタ */
	public RtnPayMst rtnPayMst;
	/** 経常支払明細マスタ */
	public List<RtnPaydtlMst> rtnPaydtlMstList;
	/** 支払サイトマスタ */
	public PaySiteMst paySiteMst;
	/** 消費税マスタ */
	public TaxMst taxMst;
	/** 源泉税区分マスタ */
	public HldtaxMst hldtaxMst;
	/** 契約情報マスタ */
	public CntrctInf cntrctInf;
	/** 契約先マスタ */
	public List<CntrctSplrInf> cntrctSplrInfList;
	/** 取引先マスタ */
	public List<SplrMst> splrMstList;
	/** 振込先口座マスタ */
	public List<PayeeBnkaccMst> payeeBnkaccMstList;
	/** 契約明細情報マスタ */
	//public List<CntrctdtlInf> cntrctdtlInfList;
	/** 前回発注マスタ */
	public PurordInf previousPurordInf;

	/** 件名 */
	public String subject;

	/** 発注対象フラグ */
	public boolean isPurcharseFlg = false;
	/** 検収対象フラグ */
	public boolean isRcvinspFlg = false;
	/** 支払依頼対象フラグ */
	public boolean isPayFlg = false;

	/** 支払回数 */
	public int processMonthCnt;

	/** 発注No */
	public String purordNo;
	/** 検収No */
	public String rcvinspNo;
}
