package jp.co.nci.iwf.component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.util.MiscUtils;

public interface CodeBook {
	/** 改行コード */
	String CRLF = "\r\n";

	/** デフォルトのブロック表示順 */
	long DEFAULT_SCREEN_PROCESS_ID = -1L;

	interface HTTP {
		/** HTTPヘッダ：ETAGの確認用 */
		String IF_NONE_MATCH = "If-None-Match";

		/** HTTPヘッダ：最終更新日時の確認用 */
		String IF_MODIFIED_SINCE = "If-Modified-Since";

		/** HTTPヘッダ：最終更新日時 */
		String LAST_MODIFIED = "Last-Modified";

		/** HTTPヘッダ：コンテンツタイプ＝Javascript */
		String TEXT_JAVASCRIPT = "text/javascript";
	}

	/** APPで使用するURLの定数 */
	interface AppURL {

		/** ログイン不要なURLのプレフィックス */
		String LOGIN_FREE_DIR = "/unsecure/";

		/** ログイン画面URL */
		String LOGIN = "unsecure/login.html";

		/** ワークリストURL */
		String WORKLIST_PAGE = "wl/wl0030.html";

		/** パスワード変更画面URL */
		String CHANGE_PASSWORD = "au/au0011.html";

		/** システムエラーURL */
		String SYSTEM_ERROR = "error/systemError.html";

		/** 認証なし／セッションタイムアウトエラー画面 */
		String NOT_AUTHENTICATED = "error/error401.html";

		/** エラー系画面のURLのプレフィックスディレクトリ */
		String ERROR_DIR = "/error/";

		/** なりすまし先URL */
		String SPOOFING_PAGE = "au/au0000.html";
	}

	/** クエリストリングの定数 */
	interface QueryString {
		/** 遷移元 */
		String FROM = "from";
	}

	/** 企業コードの定数 */
	interface CorporationCodes {
		/** ASP管理用 */
		String ASP = CorporationCode.ASP;
		/** 企業グループ管理用 */
		String GROUP = CorporationCode.GROUP;

		//-------------------------------
		// 以降、DMM案件用
		//-------------------------------
		/** 株式会社DMM.comBase */
		String BASE = "00001";
		/** 合同会社DMM.com （DMM.COM） */
		String DMM_COM = "00020";
		/** 株式会社DGホールディングス(DGHD) */
		String DGHD = "00053";
		/** 株式会社デジタルコマース （DCM） */
		String DCM = "00064";
	}

	/** ユーザコード */
	interface UserAddedInfos {
		/** 管理者 */
		String ADMIN = "Administrator";
	}

	/** メニューロールCDの定数 */
	interface MenuRoleCodes {
		/** グループ管理者 */
		String GroupAdmin = "GroupAdmin";
		/** 企業管理者 */
		String CorpAdmin = "CorpAdmin";
		/** ASP管理者 */
		String ASP = "ASP";
		/** ユーザ管理者 */
		String UserAdmin = "UserAdmin";
		/** 全社員 */
		String AllUser = "AllUser";
	}

	/** メニューIDの定数 */
	interface MenuIds {
		/** ワークリスト */
		long WORKLIST = 100L;
		/** 案件一覧(複合ワークリスト) */
		long MULTI_TRAY = 130L;
		/** 起案 */
		long START_DOC = 200L;
		/** 業務管理(第一階層メニュー) */
		long TRANSFER_TITLE = 400L;
		/** 業務管理(第二階層メニュー) */
		long TRANSFER = 410L;
	}

	/** CSV標準のCSV出力書式 */
	public static CSVFormat NciCsvFormat =
			CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);

	/** ブロック表示順のブロックIDの定数 */
	interface BlockIds {
		/** 0:アクション情報(1) */
		int BUTTON1 = 0;
		/** 1:申請情報 */
		int APPLICANT = 1;
		/** 2:文書内容 */
		int CONTENTS = 2;
		/** 3:添付ファイル */
		int ATTACH_FILE = 3;
//		/** 4:見積関連文書 */
//		int ESTIMATE = 4;
		/** 5:決裁関連文書 */
		int APPROVAL_RELATION = 5;
		/** 6:履歴情報 */
		int HISTORY = 6;
		/** 7:承認者情報 */
		int APPROVER = 7;;
		/** 8:デフォルト閲覧者（デフォルト参照者情報） */
		int DEFAULT_REFERER = 8;
		/** 9:参照者情報 */
		int REFERER = 9;
		/** 10:要説明情報（掲示板） */
		int BBS = 10;
		/** 11:メモ情報 */
		int MEMO = 11;
		/** 12:アクション情報(2) */
		int BUTTON2 = 12;
		/** 14:文書管理関連文書 */
		int DOC_MANAGEMENT = 14;
		/** 15:文書ファイル */
		int DOC_FILE = 15;
	}

	/** アクセスログ処理結果 */
	interface AccessLogResultType {
		/** 不明 */
		String UNKNOWN = "U";
		/** 成功 */
		String SUCCESS = "S";
		/** 失敗 */
		String FAIL = "F";
	}

	/** トレイタイプ */
	public enum TrayType {
		/** 新規起案 */
		NEW,
		/** TODOリスト(ワークリスト) */
		WORKLIST,
		/** 自案件 */
		OWN,
		/** 全案件 */
		ALL,
		/** 強制変更 */
		FORCE,
		/** 一括承認 */
		BATCH;

		public boolean equals(String s) {
			if (s == null) return false;
			return this.toString().equals(s);
		}

		/** jackson用、文字列を列挙型TrayTypeへ変換 */
		@JsonCreator
		public static TrayType fromValue(String value) {
			if (value == null || value.length() == 0)
				return null;
			return TrayType.valueOf(value);
		}

		/** jackson用、列挙型TrayTypeを文字列へ変換 */
		@JsonValue
		public static String toValue(TrayType trayType) {
			return (trayType == null ? "" : trayType.name());
		}
	}

	/** キャッシュ間隔 */
	public enum CacheInterval {
		/** 10秒毎に読み直す */
		EVERY_10SECONDS,
		/** DB依存（＝[WFM_CORP_PROP_MASTER.PROPERTY_CODE='CACHE_INTERVAL_SECONDS']の秒数ごとに読み直す） */
		FROM_DATABASE,
		/** 不変 */
		FOREVER,
	}

	/** 汎用テーブル.エンティティタイプ */
	public interface EntityType {
		/** T:テーブル */
		String TABLE = "T";
		/** V:ビュー */
		String VIEW = "V";
	}

	/** 汎用テーブル検索条件カラム.検索結果表示区分 */
	public interface ResultDisplayType {
		/** 0:不使用 */
		String NON_USE = "0";
		/** 1:表示 */
		String DISPLAY = "1";
		/** 2:隠し項目 */
		String HIDDEN = "2";
	}

	/** 汎用テーブル検索条件表示区分 */
	public interface ConditionDisplayType {
		/** 0:不使用 */
		String NON_USE = "0";
		/** 1:表示(テキストボックス) */
		String DISPLAY_TEXTBOX = "1";
		/** 2:表示(ドロップダウン) */
		String DISPLAY_DROPDOWN = "2";
		/** 3:非表示(暗黙の条件) */
		String HIDDEN = "3";
	}

	/** 汎用テーブル検索条件カラム.検索条件の初期値区分 */
	public interface ConditionInitType {
		/** 0:ブランク */
		String BLANK = "0";
		/** 1:固定値 */
		String LITERAL = "1";
		/** 2:ログイン者企業コード */
		String CORPORATION_CODE = "2";
		/** 3:ログイン者ユーザID */
		String USER_ADDED_INFO = "3";
		/** 4:ログイン者言語コード */
		String LOCALE_CODE = "4";
		/** 5:システム日付(yyyy/MM/dd) */
		String SYSDATE = "5";
		/** 6:パーツ */
		String PARTS = "6";
	}

	/** 汎用テーブル検索条件カラム.検索条件の初期値区分 */
	public interface ResultOrderByDirection {
		String ASC = "A";
		String DESC = "D";
	}

	/** 汎用テーブル検索条件カラム.検索条件の一致区分 */
	public interface ConditionMatchType {
		/** 1:完全一致 */
		String FULL = "1";
		/** 2:前方一致 */
		String FRONT = "2";
		/** 3:部分一致 */
		String BOTH = "3";
		/** 4:範囲（以上～以下） */
		String RANGE = "4";
//		/** 5:選択肢リスト */
//		String DROPDOWN = "5";
		/** 6:以上「＞＝」 */
		String GTE = "6";
		/** 7:以下「＜＝」 */
		String LTE = "7";
		/** 8:超過「＞」 */
		String GT = "8";
		/** 9:未満「＜」 */
		String LT = "9";
		/** A:不等価「！＝」 */
		String NOT_EQUAL = "5";
	}

	/** 汎用テーブル検索条件カラム.カラム型 */
	public interface SearchColumnType {
		/** S:文字 */
		String STRING = "S";
		/** N:数値 */
		String NUMBER = "N";
		/** D:日付 */
		String DATE = "D";
		/** T:日付時刻 */
		String TIMESTAMP = "T";
	}

	/** 汎用テーブル検索条件カラム.ブランク区分 */
	public interface ConditionBlankType {
		/** 1:絞込み対象外 */
		String IGNORE = "1";
		/** 2:必須入力 */
		String REQUIRED = "2";
		/** 3:NULLで絞込む */
		String SEARCH = "3";
		/** 4:検索結果0件を強制 */
		String FORCE_RECORD_ZERO = "4";
	}

	/** 汎用テーブル検索条件カラム.カナ変換区分 */
	public interface ConditionKanaConvertType {
		/** 変換なし */
		String NONE = "NONE";
		/** [全角カナ]へ変換 */
		String FULL_WIDTH_KANA = "FW_KANA";
		/** [全角ひらがな]へ変換 */
		String FULL_WIDTH_HIRAGANA = "FW_HIRA";
		/** [半角カナ]へ変換 */
		String SINGLE_WIDTH_KANA = "SW_KANA";
	}

	/** 汎用テーブル検索条件カラム.トリムフラグ */
	public interface ConditionTrimFlag {
		/** 1:する */
		String ON = "1";
		/** 0:しない */
		String OFF = "0";
	}

	/** キャラクターセット：MS932（Windows-31J/Shift-JISの拡張） */
	public Charset MS932 = Charset.forName("MS932");
	/** キャラクターセット：UTF8 */
	public Charset UTF8 = StandardCharsets.UTF_8;

	/** メールのテンプレートファイル名の定数 */
	public interface MailTemplateFileName {
		/** サンプルメール（Sandboxで使用されています） */
		String SAMPLE = "Sample.txt";
		/** 仮パスワード発行通知メール */
		String ONETIME_PASSWORD = "OneTimePassword.txt";
		/** リセットパスワード通知メール */
		String RESET_PASSWORD = "ResetPassword.txt";
		/** アカウントロック通知メール */
		String ACCOUNT_LOCK = "LockedAccount.txt";
		/** WF次遷移通知メール[状態遷移] */
		String WF_NOTIFICATION_CREATE = "NotificationCreate.txt";
		/** WF次遷移通知メール[状態遷移] */
		String WF_NOTIFICATION_MOVE = "NotificationMove.txt";
		/** WF次遷移通知メール[引戻し] */
		String WF_NOTIFICATION_PULLBACK = "NotificationPullback.txt";
		/** WF次遷移通知メール[却下] */
		String WF_NOTIFICATION_REJECT = "NotificationReject.txt";
		/** WF次遷移通知メール[差戻し] */
		String WF_NOTIFICATION_SENDBACK = "NotificationSendback.txt";
		/** WF次遷移通知メール[取下げ] */
		String WF_NOTIFICATION = "NotificationWithdraw.txt";
		/** WF完了通知メール */
		String WF_COMPLETE = "NotificationComplation.txt";
		/** WF参照権限付与通知メール	 */
		String WF_REFERENCE = "NotificationReference.txt";
		/** WF要説明（プロセス掲示板）通知メール */
		String WF_PROCESS_BBS = "ProcessBbsSubmit.txt";
		/** 代理者就任通知メール */
		String AUTH_TRANSFER = "AuthTransfer.txt";
		/** 代理者離任通知メール */
		String AUTH_TRANSFER_DELETE = "AuthTransferDelete.txt";
	}

	/** パーツ採番形式(MWM_PARTS_NUMBERING_FORMAT).書式区分の定数 */
	public interface FormatType {
		/** 固定値／リテラル値 */
		String LITERAL = "L";
		/** システム日付 */
		String TIME = "T";
		/** 連番 */
		String SEQUENCE = "S";
		/** ログイン者組織 */
		String ORGANIZATION = "O";
	}

	/** プロセス掲示板メール区分 */
	public interface ProcessBbsMailType {
		/** 通知なし */
		String NONE = "0";
		/** 起案者だけに通知 */
		String STARTER_ONLY = "1";
		/** 起案者と返信先 */
		String STARTER_AND_REPLY = "2";
		/** 起案者と承認済の参加者に通知 */
		String APPROVED_USERS = "3";
		/** 起案者と未承認を含む全承認者に通知 */
		String ALL_ASSIGNED_USERS = "4";
	}

	/** トレイ表示項目設定.共有設定 */
	public interface TraySharingType {
		/** 1:自社共通 */
		String COMPANY = "1";
		/** 2:部 */
		String ORGANIZATION = "2";
		/** 3:個人 */
		String PERSON = "3";
		/** 4:メニューロール */
		String MENU_ROLE_CODE = "4";
		/** 5:参加者ロール */
		String ASSIGN_ROLE_CODE = "5";
	}

	/** クライアントのデバイス・カテゴリー */
	public enum ViewWidth {
		/** モバイル */
		XS,
		/** タブレット */
		SM,
		/** PC(通常) */
		MD,
		/** PC(大型) */
		LG,
		;

		/** モバイルとみなせる画面の表示幅か */
		public static boolean isMobile(DesignerContext ctx) {
			return ctx.viewWidth == null
					|| ViewWidth.XS == ctx.viewWidth;
		}

		/** jackson用、文字列を列挙型TrayTypeへ変換 */
		@JsonCreator
		public static ViewWidth fromValue(String value) {
			if (value == null || value.length() == 0)
				return null;
			return ViewWidth.valueOf(value.toUpperCase());
		}

		/** jackson用、列挙型TrayTypeを文字列へ変換 */
		@JsonValue
		public static String toValue(ViewWidth trayType) {
			return (trayType == null ? "" : trayType.toString());
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	/** 業務文書用ブロック表示順のブロックIDの定数 */
	interface BizDocBlockIds {
		/** 0:ボタン */
		int BUTTON = 0;
		/** 1:業務文書 */
		int BIZDOC = 1;
		/** 2:文書内容 */
		int CONTENTS = 2;
		/** 3:文書属性 */
		int ATTRIBUTE = 3;
		/** 4:権限設定 */
		int AUTHORITY = 4;
		/** 5:関連元文書 */
		int RELATE_SRC = 5;
		/** 6:関連先文書 */
		int RELATE_DST = 6;
		/** 7:更新履歴 */
		int HISTORY = 7;;
		/** 8:メモ情報 */
		int MEMO = 8;
	}

	/** バインダー用ブロック表示順のブロックIDの定数 */
	interface BinderBlockIds {
		/** 0:ボタン */
		int BUTTON = 0;
		/** 9:バインダー */
		int BINDER = 1;
		/** 3:文書属性 */
		int ATTRIBUTE = 3;
		/** 4:権限設定 */
		int AUTHORITY = 4;
		/** 5:関連元文書 */
		int RELATE_SRC = 5;
		/** 6:関連先文書 */
		int RELATE_DST = 6;
		/** 7:更新履歴 */
		int HISTORY = 7;;
		/** 8:メモ情報 */
		int MEMO = 8;
	}

	/** 画面IDの定数 */
	public interface ScreenIds {
		/** ワークリスト */
		String WORKLIST = MessageCd.WL0030.toString();
		/** 強制変更 */
		String FORCE = MessageCd.WL0031.toString();
		/** 自案件 */
		String OWN = MessageCd.WL0032.toString();
		/** 汎用案件 */
		String ALL = MessageCd.WL0033.toString();
		/** 一括承認 */
		String BATCH = MessageCd.WL0037.toString();
		/** ログイン */
		String LOGIN = MessageCd.LOGIN.toString();
		/** パスワード復旧 */
		String RESTORE_PASSWORD = MessageCd.RESTOREPASSWORD.toString();
		/** パスワードリセット */
		String RESET_PASSWORD = MessageCd.RESETPASSWORD.toString();
		/** 詳細検索(文書管理) */
		String DETAIL_SEARCH = MessageCd.DC0022.toString();
	}

	/** ユーザコードの定数 */
	public interface UserCodes {
		/** トレイ設定で、全ユーザに適用したいときのユーザコード */
		String COMMON_USER_CODE = "#";
	}

	/** ContentType（MIME型）の定数 */
	public interface ContentType {
		// Office文書 ------------------------------
		/** Microsoft Excel */
		String XLS = "application/vnd.ms-excel";
		/** Microsoft Excel (OpenXML) */
		String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		/** Microsoft PowerPoint */
		String PPT = "application/vnd.ms-powerpoint";
		/** Microsoft PowerPoint (OpenXML) */
		String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		/** Microsoft Word */
		String DOC = "application/msword";
		/** Microsoft Word (OpenXML) */
		String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		/** Microsoft Visio */
		String VISIO = "application/vnd.visio";

		// 圧縮ファイル -------------------------------
		/** ZIP圧縮ファイル */
		String ZIP = "application/zip";
		/** RAR アーカイブ */
		String RAR = "application/x-rar-compressed";

		// 画像 ------------------------------------
		/** JPEG 画像 */
		String JPEG = "image/jpeg";
		/** グラフィック交換形式 (GIF) */
		String GIF = "image/gif";
		/** Portable Network Graphics */
		String PNG = "image/png";

		// その他 -----------------------------------
		/**  Adobe PDF */
		String PDF = "application/pdf";
		/** Scalable Vector Graphics (SVG) */
		String SVG = "image/svg+xml";
		/** コンマ区切り値 (CSV) */
		String CSV = "text/csv";
		/** プレーンテキスト */
		String TEXT = "text/plain";
		/** 汎用バイナリーファイル */
		String OCTET_STREAM = "application/octet-stream";
	}


	/** DB予約語(主にOracle) */
	public static Set<String> DBMS_RESERVED_COL_NAMES = MiscUtils.asSet(
			"ACCESS", "ACCOUNT", "ACTIVATE", "ADD", "ADMIN", "ADVISE", "AFTER", "ALL",
			"ALL_ROWS", "ALLOCATE", "ALTER", "ANALYZE", "AND", "ANY", "ARCHIVE",
			"ARCHIVELOG", "ARRAY", "AS", "ASC", "AT", "AUDIT", "AUTHENTICATED",
			"AUTHORIZATION", "AUTOEXTEND", "AUTOMATIC", "BACKUP", "BECOME", "BEFORE",
			"BEGIN", "BETWEEN", "BFILE", "BITMAP", "BLOB", "BLOCK", "BODY", "BY",
			"CACHE", "CACHE_INSTANCES", "CANCEL", "CASCADE", "CAST", "CFILE", "CHAINED",
			"CHANGE", "CHAR", "CHAR_CS", "CHARACTER", "CHECK", "CHECKPOINT", "CHOOSE",
			"CHUNK", "CLEAR", "CLOB", "CLONE", "CLOSE", "CLOSE_CACHED_OPEN_CURSORS",
			"CLUSTER", "COALESCE", "COLUMN", "COLUMNS", "COMMENT", "COMMIT", "COMMITTED",
			"COMPATIBILITY", "COMPILE", "COMPLETE", "COMPOSITE_LIMIT", "COMPRESS",
			"COMPUTE", "CONNECT", "CONNECT_TIME", "CONSTRAINT", "CONSTRAINTS", "CONTENTS",
			"CONTINUE", "CONTROLFILE", "CONVERT", "COST", "CPU_PER_CALL", "CPU_PER_SESSION",
			"CREATE", "CURRENT", "CURRENT_SCHEMA", "CURREN_USER", "CURSOR", "CYCLE", "DANGLING",
			"DATABASE", "DATAFILE", "DATAFILES", "DATAOBJNO", "DATE", "DBA", "DBHIGH",
			"DBLOW", "DBMAC", "DEALLOCATE", "DEBUG", "DEC", "DECIMAL", "DECLARE",
			"DEFAULT", "DEFERRABLE", "DEFERRED", "DEGREE", "DELETE", "DEREF", "DESC",
			"DIRECTORY", "DISABLE", "DISCONNECT", "DISMOUNT", "DISTINCT", "DISTRIBUTED",
			"DML", "DOUBLE", "DROP", "DUMP", "EACH", "ELSE", "ENABLE", "END", "ENFORCE",
			"ENTRY", "ESCAPE", "EXCEPT", "EXCEPTIONS", "EXCHANGE", "EXCLUDING",
			"EXCLUSIVE", "EXECUTE", "EXISTS", "EXPIRE", "EXPLAIN", "EXTENT", "EXTENTS",
			"EXTERNALLY", "FAILED_LOGIN_ATTEMPTS", "FALSE", "FAST", "FILE", "FIRST_ROWS",
			"FLAGGER", "FLOAT", "FLOB", "FLUSH", "FOR", "FORCE", "FOREIGN", "FREELIST",
			"FREELISTS", "FROM", "FULL", "FUNCTION", "GLOBAL", "GLOBALLY", "GLOBAL_NAME",
			"GRANT", "GROUP", "GROUPS", "HASH", "HASHKEYS", "HAVING", "HEADER", "HEAP",
			"IDENTIFIED", "IDGENERATORS", "IDLE_TIME", "IF", "IMMEDIATE", "IN", "INCLUDING",
			"INCREMENT", "INDEX", "INDEXED", "INDEXES", "INDICATOR", "IND_PARTITION",
			"INITIAL", "INITIALLY", "INITRANS", "INSERT", "INSTANCE", "INSTANCES",
			"INSTEAD", "INT", "INTEGER", "INTERMEDIATE", "INTERSECT", "INTO", "IS",
			"ISOLATION", "ISOLATION_LEVEL", "KEEP", "KEY", "KILL", "LABEL", "LAYER",
			"LESS", "LEVEL", "LIBRARY", "LIKE", "LIMIT", "LINK", "LIST", "LOB", "LOCAL",
			"LOCK", "LOCKED", "LOG", "LOGFILE", "LOGGING", "LOGICAL_READS_PER_CALL",
			"LOGICAL_READS_PER_SESSION", "LONG", "MANAGE", "MASTER", "MAX", "MAXARCHLOGS",
			"MAXDATAFILES", "MAXEXTENTS", "MAXINSTANCES", "MAXLOGFILES", "MAXLOGHISTORY",
			"MAXLOGMEMBERS", "MAXSIZE", "MAXTRANS", "MAXVALUE", "MIN", "MEMBER", "MINIMUM",
			"MINEXTENTS", "MINUS", "MINVALUE", "MLSLABEL", "MLS_LABEL_FORMAT", "MODE",
			"MODIFY", "MOUNT", "MOVE", "MTS_DISPATCHERS", "MULTISET", "NATIONAL", "NCHAR",
			"NCHAR_CS", "NCLOB", "NEEDED", "NESTED", "NETWORK", "NEW", "NEXT", "NOARCHIVELOG",
			"NOAUDIT", "NOCACHE", "NOCOMPRESS", "NOCYCLE", "NOFORCE", "NOLOGGING",
			"NOMAXVALUE", "NOMINVALUE", "NONE", "NOORDER", "NOOVERRIDE", "NOPARALLEL",
			"NOPARALLEL", "NOREVERSE", "NORMAL", "NOSORT", "NOT", "NOTHING", "NOWAIT",
			"NULL", "NUMBER", "NUMERIC", "NVARCHAR2", "OBJECT", "OBJNO", "OBJNO_REUSE",
			"OF", "OFF", "OFFLINE", "OID", "OIDINDEX", "OLD", "ON", "ONLINE", "ONLY",
			"OPCODE", "OPEN", "OPTIMAL", "OPTIMIZER_GOAL", "OPTION", "OR", "ORDER",
			"ORGANIZATION", "OSLABEL", "OVERFLOW", "OWN", "PACKAGE", "PARALLEL",
			"PARTITION", "PASSWORD", "PASSWORD_GRACE_TIME", "PASSWORD_LIFE_TIME",
			"PASSWORD_LOCK_TIME", "PASSWORD_REUSE_MAX", "PASSWORD_REUSE_TIME",
			"PASSWORD_VERIFY_FUNCTION", "PCTFREE", "PCTINCREASE", "PCTTHRESHOLD",
			"PCTUSED", "PCTVERSION", "PERCENT", "PERMANENT", "PLAN", "PLSQL_DEBUG",
			"POST_TRANSACTION", "PRECISION", "PRESERVE", "PRIMARY", "PRIOR", "PRIVATE",
			"PRIVATE_SGA", "PRIVILEGE", "PRIVILEGES", "PROCEDURE", "PROFILE", "PUBLIC",
			"PURGE", "QUEUE", "QUOTA", "RANGE", "RAW", "RBA", "READ", "READUP", "REAL",
			"REBUILD", "RECOVER", "RECOVERABLE", "RECOVERY", "REF", "REFERENCES",
			"REFERENCING", "REFRESH", "RENAME", "REPLACE", "RESET", "RESETLOGS",
			"RESIZE", "RESOURCE", "RESTRICTED", "RETURN", "RETURNING", "REUSE",
			"REVERSE", "REVOKE", "ROLE", "ROLES", "ROLLBACK", "ROW", "ROWID", "ROWNUM",
			"ROWS", "RULE", "SAMPLE", "SAVEPOINT", "SB4", "SCAN_INSTANCES", "SCHEMA",
			"SCN", "SCOPE", "SD_ALL", "SD_INHIBIT", "SD_SHOW", "SEGMENT", "SEG_BLOCK",
			"SEG_FILE", "SELECT", "SEQUENCE", "SERIALIZABLE", "SESSION",
			"SESSION_CACHED_CURSORS", "SESSIONS_PER_USER", "SET", "SHARE", "SHARED",
			"SHARED_POOL", "SHRINK", "SIZE", "SKIP", "SKIP_UNUSABLE_INDEXES", "SMALLINT",
			"SNAPSHOT", "SOME", "SORT", "SPECIFICATION", "SPLIT", "SQL_TRACE", "STANDBY",
			"START", "STATEMENT_ID", "STATISTICS", "STOP", "STORAGE", "STORE", "STRUCTURE",
			"SUCCESSFUL", "SWITCH", "SYS_OP_ENFORCE_NOT_NULL$", "SYS_OP_NTCIMG$",
			"SYNONYM", "SYSDATE", "SYSDBA", "SYSOPER", "SYSTEM", "TABLE", "TABLES",
			"TABLESPACE", "TABLESPACE_NO", "TABNO", "TEMPORARY", "THAN", "THE", "THEN",
			"THREAD", "TIMESTAMP", "TIME", "TO", "TOPLEVEL", "TRACE", "TRACING",
			"TRANSACTION", "TRANSITIONAL", "TRIGGER", "TRIGGERS", "TRUE", "TRUNCATE",
			"TX", "TYPE", "UB2", "UBA", "UID", "UNARCHIVED", "UNDO", "UNION", "UNIQUE",
			"UNLIMITED", "UNLOCK", "UNRECOVERABLE", "UNTIL", "UNUSABLE", "UNUSED",
			"UPDATABLE", "UPDATE", "USAGE", "USE", "USER", "USING", "VALIDATE",
			"VALIDATION", "VALUE", "VALUES", "VARCHAR", "VARCHAR2", "VARYING", "VIEW",
			"WHEN", "WHENEVER", "WHERE", "WITH", "WITHOUT", "WORK", "WRITE", "WRITEDOWN",
			"WRITEUP", "XID", "YEAR");	/*  END of RESERVED_DB_COLUMNS */

	/** アップロード種別 */
	public interface UploadKind {
		/** UP0010:画面定義 */
		String SCREEN = "UP0010";
		/** UP0020:プロセス定義 */
		String PROCESS = "UP0020";
		/** UP0030:メニューロール定義 */
		String MENU_ROLE = "UP0030";
		/** トレイ設定 */
		String TRAY = "UP0040";
		/** UP0050:メールテンプレート */
		String MAIL_TEMPLATE = "UP0050";
		/** UP0060:システム環境設定 */
		String SYSTEM_ENV = "UP0060";
	}
}
