package jp.co.dmm.customize.endpoint.batch.purord;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PrdPurordPlnMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;


/**
 * 定期発注バッチ処理情報
 *
 */
public class PurOrdData {

	/** 実行日 */
	public Date processingDate;

	/** 発注予定情報一覧 */
	public List<PrdPurordPlnMst> prdPurordPlnMstList;

	/** バッチ処理対象発注情報一覧 */
	public List<PurOrdEntity> purOrdEntities;

	/** 会社別発注申請プロセス定義コード */
	public Map<String, String[]> targetOrderMap;

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
}
