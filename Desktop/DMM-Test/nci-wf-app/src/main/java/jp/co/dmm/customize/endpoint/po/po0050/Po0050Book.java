package jp.co.dmm.customize.endpoint.po.po0050;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.dmm.customize.component.DmmCodeBook.TaxFgChg;
import jp.co.dmm.customize.component.DmmCodeBook.TaxUnt;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされた通販データのEXCELブック.
 */
public class Po0050Book implements Serializable {

	/** 通販データ一覧 */
	public List<Po0050MlordInf> mlordInfs = new ArrayList<>();

	// 以下、存在チェック用に使用するマスタ情報またはコード値
	/** 取引先コード */
	public Set<String> splrCds;
	/** 振込先銀行口座コード */
	public Set<String> payeeBnkaccCds;
	/** 部門コード */
	public Set<String> bumonCds;
	/** 消費税種類コード（キーは部門コード） */
	public Map<String, String> taxKndCdMap;
	/** 消費税コード */
	public Set<String> taxCds;
	/** 消費税フラグ(明細行) */
	public Set<String> taxFgChg1 = MiscUtils.asSet(TaxFgChg.OUT_TAX_10, TaxFgChg.OUT_TAX_8, TaxFgChg.OUT_TAX_8_OLD, TaxFgChg.NONE);
	/** 消費税フラグ(消費税行) */
	public Set<String> taxFgChg2 = MiscUtils.asSet(TaxFgChg.OUT_TAX_10, TaxFgChg.OUT_TAX_8, TaxFgChg.OUT_TAX_8_OLD);
//	/** 課税対象となる消費税コード */
//	// 課税対象＝税処理区分が「1：外税」または「2：内税」のもの
//	public Set<String> taxableCds;
	/** 消費税コード毎の消費税マスタ一覧 */
	public Map<String, List<TaxMst>> taxMap;
	/** 税処理単位 */
	public Set<String> taxUnts = MiscUtils.asSet(TaxUnt.DETAIL, TaxUnt.NEW_VOUCHER);
	/** 費目コード */
	public Set<String> itmExpsCds;
	/** 費目関連コード(費目コード(1)と費目コード(2)の組み合わせ) */
	public Set<String> itmExpsChrCd;
//	/** 勘定科目コード（キーは費目コード(1)と費目コード(2)の組み合わせ） */
//	public Map<String, String> accCdMap;
	/** 伝票グループ（キーは費目コード(1)と費目コード(2)の組み合わせ） */
	public Map<String, String> slpGrpGlMap;
	/** 経費区分（キーは費目コード(1)と費目コード(2)の組み合わせ） */
	public Map<String, String> cstTpMap;
	/** 勘定科目コード「1500:仮払消費税」の費目コード(1)と費目コード(2)の組み合わせ */
	public Set<String> accCdTaxSet;
	/** 課税対象区分（キーは費目コード(1)と費目コード(2)の組み合わせ） */
	public Map<String, String> taxSbjTpMap;

	// 以下、ファイル内におけるキー集約による項目の同一性チェックに使用
	/** 通販データ取込日(年月) + 取引先コードをキーにした通販データMap */
	public Map<String, Po0050MlordInf> mlordDataMap1 = new HashMap<>();
	/** 通販データ取込日(年月) + 取引先コード + 部門コード + 消費税コードをキーにした通販データMap */
	public Map<String, Po0050MlordInf> mlordDataMap2 = new HashMap<>();

	// 以下、課税対象区分、消費税種類コード、消費税フラグの組み合わせによる消費税フラグビューMap
	public Map<String, VTaxFgChg> taxFgChgMap;

	// 以下、発注No・検収Noを採番する際のパーツ採番形式ID
	/** 発注No用のパーツ採番形式ID */
	public Long purordNoId;
	/** 検収No用のパーツ採番形式ID */
	public Long rcvinspNoId;

	// 以下、新規_支払依頼申請の画面プロセス定義ID
	/** 新規_支払依頼申請の画面プロセス定義ID */
	public Long screenProcessId;
}
