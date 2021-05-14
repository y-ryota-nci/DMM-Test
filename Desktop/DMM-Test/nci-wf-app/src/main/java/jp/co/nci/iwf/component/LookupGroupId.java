package jp.co.nci.iwf.component;;

/**
 * ルックアップグループID定数(MWM_LOOKUP_GROUP.LOOKUP_GROUP_ID)
 */
public enum LookupGroupId {
	/** アクセスログ結果種別 */
	ACCESS_LOG_RESULT_TYPE,
	/** 前払金ステータス */
	ADVPAY_STS,
	/** 算術演算子 */
	ARITHMETIC_OPERATOR,
	/** バインダーブロックID */
	BINDER_BLOCK_ID,
	/** 業務文書ブロックID */
	BIZDOC_BLOCK_ID,
	/** ブロックID */
	BLOCK_ID,
	/** B/S／P/L区分 */
	BS_PL_TP,
	/** 業務管理項目区分 */
	BUSINESS_INFO_TYPE,
	/** 表示項目ソート区分 */
	COLUMN_SORT_TYPE,
	/** カラム型 */
	COLUMN_TYPE,
	/** 共通フラグ */
	COMMON_FLAG,
	/** DMM会社 */
	COMPANYS,
	/** 比較演算子 */
	COMPARISON_OPERATOR,
	/** 汎用テーブルの検索条件ブランク区分 */
	CONDITION_BLANK_TYPE,
	/** 汎用テーブル検索条件表示区分 */
	CONDITION_DISPLAY_TYPE,
	/** 汎用テーブル検索条件初期値区分 */
	CONDITION_INIT_TYPE,
	/** 汎用テーブル検索条件カナ変換区分 */
	CONDITION_KANA_CONVERT_TYPE,
	/** 汎用テーブル検索条件一致区分 */
	CONDITION_MATCH_TYPE,
	/** 汎用テーブル検索条件トリムフラグ */
	CONDITION_TRIM_FLAG,
	/** 検索条件区分 */
	CONDITION_TYPE,
	/** コンテンツ種別（文書管理用） */
	CONTENTS_TYPE,
	/** CSS切替対象会社コード */
	CSS_TARGET_COMPANY_CODE,
	/** パーツ表示条件.表示区分 */
	DC_TYPE,
	/** 振込元銀行口座コード */
	DEFAULT_BNKACC,
	/** デフォルトチェック有無 */
	DEFAULT_CHECKED_FLAG,
	/** 引落銀行口座コード */
	DEFAULT_CHRG_BNKACC,
	/** 文書トレイタイプ */
	DOC_TRAY_TYPE,
	/** ダウ・ジョーンズ設定 */
	DOWJONES_CONFIG,
	/** 未入力行の表示 */
	EMPTY_LINE_TYPE,
	/** エンティティ区分 */
	ENTITY_TYPE,
	/** 設定状況区分 */
	EXIST_FLAG,
	/**  */
	EXIST_PASSWORD_FLAG,
	/** フォントサイズ */
	FONT_SIZE,
	/** 人事用対象費目コード */
	HR_DPT_FG_SBJ_ITMEXPS_CD,
	/** IMEモード */
	IME_MODE,
	/** 桁タイプ  */
	LENGTH_TYPE,
	/** 論理演算子 */
	LOGICAL_OPERATOR,
	/** メタ項目入力タイプ */
	META_INPUT_TYPE,
	/** 数値フォーマット */
	NUMBER_FORMAT,
	/** 採番形式：システム日付 */
	NUMBERING_FORMAT_DATE,
	/** 採番形式：ログイン者組織 */
	NUMBERING_FORMAT_ORG,
	/** 採番形式区分 */
	NUMBERING_FORMAT_TYPE,
	/** 採番形式：ログイン者ユーザ */
	NUMBERING_FORMAT_USER,
	/** 反社情報参照可能ロール */
	ORG_CRM_INF_REFER_ROLE,
	/** 1ページ表示件数 */
	PAGE_SIZE,
	/** 括弧 */
	PARENTHESIS,
	/** パーツのボタンサイズ */
	PARTS_BUTTON_SIZE,
	/** パーツ条件区分 */
	PARTS_CONDITION_TYPE,
	/** パーツ連動タイプ */
	PARTS_COODINATION_TYPE,
	/** パーツ入力タイプ */
	PARTS_INPUT_TYPE,
	/** パーツI/O区分 */
	PARTS_IO_TYPE,
	/** 組織選択パーツのデフォルト値 */
	PARTS_ORGANIZATION_SELECT_DEFAULT_VALUE,
	/** パーツ端数処理 */
	PARTS_ROUND_TYPE,
	/** パーツ検索区分 */
	PARTS_SEARCH_TYPE,
	/** ユーザ選択パーツのデフォルト値 */
	PARTS_USER_SELECT_DEFAULT_VALUE,
	/** パーツ入力チェックタイプ（日付） */
	PARTS_VALIDATE_TYPE_DATE,
	/** パーツ入力チェックタイプ（数値） */
	PARTS_VALIDATE_TYPE_NUMBER,
	/** パーツ入力チェックタイプ（文字） */
	PARTS_VALIDATE_TYPE_STRING,
	/** DMM支払ステータス */
	PAY_STS,
	/** プロセス掲示板メール区分 */
	PROCESS_BBS_MAIL_TYPE,
	/** プロキシ設定 */
	PROXY_CONFIG,
	/** 公開／非公開 */
	PUBLISH_FLAG,
	/** DMM発注ステータス */
	PURORD_STS,
	/** (予算)検収/費用/支払基準区分 */
	RCV_COST_PAY_TP,
	/** (予算)費用基準/支払基準区分 */
	RCVINSP_PAY_TP,
	/** DMM検収ステータス */
	RCVINSP_STS,
	/** レンダリング方法 */
	RENDERING_METHOD,
	/** 必須フラグ */
	REQUIRED_FLAG,
	/** リセット区分 */
	RESET_TYPE,
	/** 汎用テーブル検索結果表示揃え区分 */
	RESULT_ALIGN_TYPE,
	/** 汎用テーブル検索結果表示区分 */
	RESULT_DISPLAY_TYPE,
	/** 汎用テーブル検索結果ソート方向フラグ */
	RESULT_ORDER_BY_DIRECTION,
	/** 保存期間区分 */
	RETENTION_TERM_TYPE,
	/** パーセントの格納方法 */
	SAVE_METHOD_PERCENT,
	/** スクラッチ区分 */
	SCRATCH_FLAG,
	/** 画面パーツ入力可否 */
	SCREEN_PARTS_INPUT_FLAG,
	/** 汎用テーブルのカラム型 */
	SEARCH_COLUMN_TYPE,
	/** 連番桁数 */
	SEQUENCE_LENGTH,
	/** Slackプロパティ */
	SLACK_PROPERTIES,
	/** ・７．部門マスタ(SS 会計部門マスタ) */
	SS_KAIKEI_BUMON,
	/** ・７．部門マスタ(SS 機能コード１) */
	SS_KINO_CD_1,
	/** ・７．部門マスタ(SS 機能コード２) */
	SS_KINO_CD_2,
	/** ・７．部門マスタ(SS 機能コード３) */
	SS_KINO_CD_3,
	/** ・７．部門マスタ(SS 機能コード４) */
	SS_KINO_CD_4,
	/** ・７．部門マスタ(SS プロジェクト名称マスタ) */
	SS_PROJECT_MST,
	/** スタンプ種別 */
	STAMP_TYPE,
	/** テーブル同期区分 */
	SYNC_TABLE,
	/** システムフラグ */
	SYSTEM_FLAG,
	/** 消費税フラグ（税率変更） */
	TAX_FG_CHG,
	/** 消費税種類コード */
	TAX_KND_CD,
	/** 課税対象区分 */
	TAX_SBJ_TP,
	/** 消費税種別 */
	TAX_SPC,
	/** トレイタイプ */
	TRAY_TYPE,
	/** アップロード種別 */
	UPLOAD_KIND,
	/** 拡張情報01：所在地（WFM_USER） */
	USER_LOCATION,
	/** 有効/無効 */
	VALID_FLAG,
	;
}
