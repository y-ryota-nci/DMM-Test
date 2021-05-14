package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;

/**
 * 経常支払い情報
 */
public class RtnPayData {

	/** 処理日 */
	public Date processingDate;
	/** 対象月(yyyyMM) */
	public String targetDate;

	/** 明細（会社コード単位Map） */
	public Map<String, List<RtnPayDtl>> dtlListMap;
	/** 消費税マスタマップ（会社コード単位Map） */
	public Map<String, Map<String, TaxMst>> taxMstMap;
	/** 費目マップ（会社コード単位Map） **/
	public Map<String, Map<String, ItmexpsMst>> itmexpsMap;
	/** 費目関連マスタマップ（会社コード単位Map） **/
	public Map<String, Map<String, Itmexps1Chrmst>> itmexpsChrMap;
	/** 品目マップ（会社コード単位Map） **/
	public Map<String, Map<String, ItmMst>> itmMap;
	/** 支払業務コードマップ（会社コード単位Map） **/
	public Map<String, Map<String, String>> payApplMap;
	/** 通貨マスタマップ（会社コード単位Map） **/
	public Map<String, Map<String, MnyMst>> mnyMstMap;
	/** コード値 */
	public Map<String, Map<String, Map<String, String>>> codeMap;

	/** 会社別支払依頼プロセス定義コード */
	public Map<String, String[]> targetPaymentMap;

	//TODO 起票者はとりあえず固定にしておく
	/** 起票者組織コード : 合同会社DMM.com */
	public String organizationCodeStart = "0000000001";
	/** 起票役職コード : 役職無し */
	public String postCodeStart = "0000000013";


}
