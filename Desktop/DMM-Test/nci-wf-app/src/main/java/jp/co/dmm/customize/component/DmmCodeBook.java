package jp.co.dmm.customize.component;

/**
 * DMM用定数
 */
public interface DmmCodeBook {
	/**
	 * 検収ステータス
	 */
	public interface RcvinspSts {
		/** 10:検収済 */
		String RCVINSP_FIXED = "10";
		/** 20:支払済 */
		String PAY_FIXED = "20";
		/** 90:キャンセル */
		String CANCEL = "90";
	}

	/**
	 * 支払ステータス
	 */
	public interface PaySts {
		/** 10:支払依頼 */
		String PAY_REQ = "10";
		/** 20:一部支払 */
		String PARTIAL_PAY = "20";
		/** 30:支払完了 */
		String PAY_FIXED = "30";
	}

	/**
	 * 発注ステータス
	 */
	public interface PurordSts {
		/** 10:発注済 */
		String PURORD_FIXED = "10";
		/** 20:検収済 */
		String RCVINSP_FIXED = "20";
		/** 30:支払済 */
		String PAY_FIXED = "30";
		/** 90:キャンセル */
		String CANCEL = "90";
	}

	/** 発注区分 */
	public interface PurordTp {
		/** 1:通常発注 */
		String NORMAL = "1";
		/** 2:定期発注 */
		String ROUTINE = "2";
		/** 3:集中購買 */
		String FOCUS = "3";
		/** 4:経費 */
		String EXPENSE = "4";
	}

	/** 法人・個人区分 */
	public interface CrpPrsTp {
		/** 法人 */
		String CORPORATION = "1";
		/** 個人 */
		String PERSONAL = "2";
	}

	/** 取引状態区分 */
	public interface TrdStsTp {
		/** 利用前 */
		String BEFORE_USE = "1";
		/** 利用中 */
		String USING = "2";
		/** 利用停止 */
		String STOP_USING = "3";
	}

	/** 月次計上区分 */
	public interface MlAddTp {
		/** 買掛計上 */
		String ACC_PAYABLE = "1";
		/** 前払計上 */
		String PREPAYMENT = "2";
	}

	/** 定期発注区分 */
	public interface PrdPurordTp {
		/** 0:定期発注以外 */
		String NORMAL = "0";
		/** 1：定期発注(広告出稿) */
		String ROUTINE_ADS = "1";
		/** 2：定期発注(広告出稿外) */
		String ROUTINE_OTHER = "2";
	}

	/** 消費税処理単位 */
	public interface TaxUnt {
		/** 1:明細単位 */
		String DETAIL = "1";
		/** 2:伝票単位 */
		String VOUCHER = "2";
		/** 3:伝票単位 */
		String NEW_VOUCHER = "3";
	}

	/** 在庫区分 */
	public interface StockTp {
		/** 在庫対象 */
		String STOCK_IN = "1";
		/** 在庫対象外 */
		String STOCK_OUT = "2";
	}

	/** 調達部門区分 */
	public interface ProcFldTp {
		/** 人事 */
		String HR = "10";
		/** 総務 */
		String GA = "20";
		/** 情シス */
		String IS = "30";
		/** その他 */
		String OT = "50";
	}

	/** 消費税フラグ */
	public interface TaxFg {
		/** 1:税込 */
		String IN_TAX = "1";
		/** 2:税抜 */
		String OUT_TAX = "2";
		/** 3:リバースチャージ */
		String REVERSE = "3";
		/** 9:対象外 */
		String NONE = "9";
	}

	/** 前払金ステータス */
	public interface AdvpaySts {
		/** 0:未消込 */
		String INIT = "0";
		/** 1:消込中 */
		String DOING = "1";
		/** 2:消込済 */
		String DONE = "2";
	}

//	費用対応したので、もはや不要となった
//	/** 予算の検収基準/支払基準区分 */
//	public interface RcvinspPayTp {
//		/** 1:検収基準 */
//		String RCVINSP = "1";
//		/** 2:支払基準 */
//		String PAY = "2";
//	}

	/** 予算の費用/検収/支払基準区分 */
	public interface RcvCostPayTp {
		/** 0:費用予算 / 費用実績 */
		String COST = "1";
		/** 1:費用予算 / 検収実績 */
//		String RCV = "1";
		/** 2:支払予算 / 支払実績 */
		String PAY = "2";
	}

	/** 国内・海外区分 */
	public interface DmsAbrTp {
		/** 1:国内 */
		String DOMESTIC = "1";
		/** 2:海外 */
		String ABROAD = "2";
	}

	/** 定期支払予定マスタ.定期支払ステータスステータス */
	public interface PrdPaySts {
		/** 10:未自動起票 */
		String PRE = "10";
		/** 20:自動起票済 */
		String COMPLATED = "20";
	}

	/** 参加者ロールCD */
	public interface AssignRoleCodes {
		/** 法務部ロール */
		String LEGAL = "DMM003";
		/** CFO室ロール */
		String CFO = "DMM014";
		/** DMM経理ロール */
		String ACCOUNT = "DMM025";
		/** 主計ロール */
		String BUDGET = "DMM026";
	}

	/** 支払方法 */
	public interface PayMth {
		/** 10:振込 */
		String FURIKOMI = "10";
		/** 20:引落 */
		String HIKIOTOSHI = "20";
		/** 30:手形 */
		String TEGATA = "30";
		/** 40:現金 */
		String GENKIN = "40";
		/** 50:クレカ */
		String KUREKA = "50";
		/** 60:海外送金 */
		String KAIGAI_SOUKIN = "60";
		/** 70:PAYPAL */
		String PAYPAL = "70";
		/** 80:PAXUM */
		String PAXUM = "80";
		/** 90:SBIレミット */
		String SBI = "90";
		/** AA:Pay-easy */
		String PAY_EASY = "AA";
		/** BB:窓口 */
		String MADOGUCHI = "BB";
	}

	/** 消費税(税率変更)フラグ */
	public interface TaxFgChg {
		/** 1：税込（10%） */
		String IN_TAX_10 = "1";
		/** 2：税込（軽減8%） */
		String IN_TAX_8 = "2";
		/** 3：税込（旧税率8%） */
		String IN_TAX_8_OLD = "3";
		/** 4：税抜（10%） */
		String OUT_TAX_10 = "4";
		/** 5：税抜（軽減8%） */
		String OUT_TAX_8 = "5";
		/** 6：税抜（旧税率8%） */
		String OUT_TAX_8_OLD = "6";
		/** 7：ﾘﾊﾞｰｽﾁｬｰｼﾞ(海外10%) */
		String REVERSE_10 = "7";
		/** 8：ﾘﾊﾞｰｽﾁｬｰｼﾞ(海外旧8%) */
		String REVERSE_8 = "8";
		/** 9：対象外 */
		String NONE = "9";
	}
}
