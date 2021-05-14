package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public abstract class MgExcelBook<S extends MgExcelSheet> implements Serializable {
	/** 区切り文字 */
	public final String SEPARATOR = "\t";

	/** シート「マスタ」 */
	public S sheet;

	/** 処理区分 */
	public Set<String> processTypes = MiscUtils.asSet("A", "U", "C", "D");

	/** 削除フラグ */
	public Set<String> dltFgs = MiscUtils.asSet("0", "1");

	/** 単位コード
	 * 1:個
	 * 2:台
	 * */
	public Set<String> untCds = MiscUtils.asSet("1", "2");

	/** 在庫区分
	 * 1:在庫対象
	 * 2:在庫対象外
	 * */
	public Set<String> stckTps = MiscUtils.asSet("1", "2");

	/** 調達部門
	 * 10:人事
	 * 20:総務
	 * 30:情シス
	 * 50:その他
	 * */
	public Set<String> prcFldTps = MiscUtils.asSet("10", "20", "30", "50");

	/** 会社コード一覧 */
	public Set<String> existCompanyCodes;

	/** 組織コード一覧 */
	public Set<String> existOrgnzCodes;

	/** 取引先コード一覧 */
	public Set<String> existSplrCodes;

	/** 消費税コード一覧 */
	public Set<String> existTaxCodes;

	/** 既存銀行一覧 */
	public Set<String> existBnkCodes;

	/** 既存銀行支店一覧 */
	public Set<String> existBnkbrcCodes;

	/** 有効期限付きマスタ一覧 */
	public Map<String, Set<MgMstCodePeriod>> existEntityPeriods;

	public Calendar VD_DT_S_DEF = null;
	public Calendar VD_DT_E_DEF = null;

	public MgExcelBook() {
		if (VD_DT_S_DEF == null) {
			VD_DT_S_DEF = Calendar.getInstance();
			VD_DT_S_DEF.set(1999, 0, 1, 0, 0, 0);
		}

		if (VD_DT_E_DEF == null) {
			VD_DT_E_DEF = Calendar.getInstance();
			VD_DT_E_DEF.set(2099, 11, 31, 0, 0, 0);
		}
	}

	/** ユーザコード一覧 */
	public Set<String> existUserCodes;

	/** BS/PL区分
	 * 1:BS
	 * 2:PL
	 * */
	public Set<String> bsPlTps = MiscUtils.asSet("1", "2");
}
