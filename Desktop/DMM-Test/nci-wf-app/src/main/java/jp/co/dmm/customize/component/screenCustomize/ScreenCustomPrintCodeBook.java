package jp.co.dmm.customize.component.screenCustomize;

public interface ScreenCustomPrintCodeBook {

	interface ReportName {
		/** WF発注情報 */
		String ORDER_REPORT = "WF発注情報";
		/** WF発注明細情報 */
		String OREDER_DETAIL_REPORT = "WF発注明細情報";
		/** WF検収情報 */
		String ACCEPT_REPORT = "WF検収情報";
		/** WF検収明細情報 */
		String ACCEPT_DETAIL_REPORT = "WF検収明細情報";
		/** WF支払情報 */
		String PAYMENT_REPORT = "WF支払情報";
		/** WF支払明細情報 */
		String PAYMENT_DETAIL_REPORT = "WF支払明細情報";
	}

	interface JasperParam {
		/** 共通スタンプパーツ　*/
		String STAMP = "STAMP";
	}

	interface WfAssignedStatus {
		/** 終了 */
		String END = "end";
		/** 差し戻し */
		String REMAND = "end_s";
		/** スキップ */
		String SKIP = "skip";
	}

	interface PrintTriggerActivity {
		/** 発注申請WF上で分岐後、営業経理承認が下りた時点でスタンプの表示対象となる */
		String ACTIVITY_FOR_ORDER_STAMP = "0000000006";
		String REMAND_ROLE_FOR_ORDER_STAMP = "0000000002";
		String ACTIVITY_FOR_ACEPT_STAMP = "0000000003";
		String REMAND_ROLE_FOR_ACCEPT_STAMP = "0000000002";
		String ACTIVITY_FOR_PAYMENT_STAMP = "0000000004";
		String REMAND_ROLE_FOR_PAYMENT_STAMP = "0000000005";
	}

	interface StampName {
		String CORNER_STAMP = "角印";
		String CIRCLE_STAMP = "丸印";
		String NOTHING = "なし";
	}

	interface PartsId {
		/** 発注申請書の印鑑種別 */
		String ORDER_STAMP_TYPE = "DDL0083";
		/** 発注申請書の印鑑種別 */
		String ACCEPT_STAMP_TYPE = "DDL0081";
		/** 発注申請書の消費税処理単位 */
		String ORDER_TAX_TYPE = "RAD0139";
		/** 検収申請書の消費税処理単位 */
		String ACCEPT_TAX_TYPE = "RAD0127";
		/** 支払申請書の消費税処理単位 */
		String PAYMENT_TAX_TYPE = "RAD0106";
		/** 発注申請書の通貨単位 */
		String ORDER_MONEY_TYPE = "MST0159";
		/** 検収申請書の通貨単位 */
		String ACCEPT_MONEY_TYPE = "MST0104";
		/** 支払申請書の通貨単位 */
		String PAYMENT_MONEY_TYPE = "MST0149";
		/** 発注申請書の支払額 */
		String ORDER_TOTAL = "TXT0187";
		/** 検収申請書の支払額 */
		String ACCEPT_TOTAL = "TXT0112";
		/** 支払申請書の支払額 */
		String PAYMENT_TOTAL = "TXT0177";
		/** 発注申請書の申請日 */
		String ORDER_APPLICATION_DATE = "TXT0070";
		/** 検収申請書の申請日 */
		String ACCEPT_APPLICATION_DATE = "TXT0010";
		/** 検収申請書の完了日 */
		String ACCEPT_END_DATE = "TXT0009";
		/** 支払申請書の申請日 */
		String PAYMENT_APPLICATION_DATE = "TXT0069";
		/** 発注申請書の納品予定日 */
		String ORDER_ACCEPTING_DATE = "TXT0174";
		/** 検収申請書の申請日 */
		String ACCEPT_ACCEPTED_DATE = "TXT0008";
		/** 支払申請書の申請日 */
		String PAYMENT_PAYING_DATE = "TXT0044";
		/** 支払申請書の証憑区分 */
		String PAYMENT_EVIDENCE_TYPE = "RAD0168";
	}

	interface TaxType {
		String DETAIL = "明細単位";
		String ORDER = "伝票単位";
	}

	interface EvidenceType {
		String INVOICE = "請求書";
		String PAYMENT = "支払通知書";
	}
}
