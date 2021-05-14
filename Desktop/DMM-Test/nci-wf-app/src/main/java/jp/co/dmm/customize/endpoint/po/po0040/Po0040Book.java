package jp.co.dmm.customize.endpoint.po.po0040;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.dmm.customize.component.DmmCodeBook.TaxUnt;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたkintoneデータのEXCELブック.
 */
public class Po0040Book implements Serializable {

	/** kintoneデータ一覧 */
	public List<Po0040KntnInf> kntnInfs = new ArrayList<>();

	// 以下、存在チェック用に使用するコード値
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
	/** 課税対象となる消費税コード */
	// 課税対象＝税処理区分が「1：外税」または「2：内税」のもの
	public Set<String> taxableCds;
	/** 消費税コード毎の消費税マスタ一覧 */
	public Map<String, List<TaxMst>> taxMap;
	/** 税処理単位 */
	public Set<String> taxUnts = MiscUtils.asSet(TaxUnt.DETAIL, TaxUnt.NEW_VOUCHER);
	/** kintoneステータス */
	// 10:営業依頼中、20:手配中、30:手配完了のみ許可
	public Set<String> kntnSts = MiscUtils.asSet("10", "20", "30");
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
	/** 開催日(年月) + 取引先コードをキーにしたkintoneデータMap */
	public Map<String, Po0040KntnInf> kntnDataMap1 = new HashMap<>();
	/** 開催日(年月) + 取引先コード + イベント管理No + 費目コード1 + 費目コード2をキーにしたkintoneデータMap */
	public Map<String, Po0040KntnInf> kntnDataMap2 = new HashMap<>();

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
