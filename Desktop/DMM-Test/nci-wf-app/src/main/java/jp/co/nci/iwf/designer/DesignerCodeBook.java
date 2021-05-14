package jp.co.nci.iwf.designer;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * 画面デザイナの定数
 */
public interface DesignerCodeBook {

	/** パーツ種別の定数 */
	public interface PartsType {
		/** 1:テキストボックス */
		int TEXTBOX = 1;
		/** 2:ラベル */
		int LABEL = 2;
		/** 3:チェックボックス */
		int CHECKBOX = 3;
//		/** 4:カレンダー */
//		int CALENDAR = 4;
		/** 5:ラジオボタン */
		int RADIO = 5;
		/** 6:ドロップダウンリスト */
		int DROPDOWN = 6;
		/** 7:採番 */
		int NUMBERING = 7;
		/** 8:組織選択 */
		int ORGANIZATION = 8;
		/** 9:ユーザー選択 */
		int USER = 9;
		/** 12:検索ボタン */
		int SEARCH_BUTTON = 12;
		/** 13:イベントボタン */
		int EVENT_BUTTON = 13;
		/** 14:画像 */
		int IMAGE = 14;
		/** 15:グリッド（可変テーブル） */
		int GRID = 15;
		/** 16：スタンプ */
		int STAMP = 16;
		/** 17:ハイパーリンク */
		int HYPERLINK = 17;
		/** 20:添付ファイル */
		int ATTACHFILE = 20;
		/** 50:マスタ選択 */
		int MASTER = 50;
		/** 51:独立画面パーツ */
		int STAND_ALONE = 51;
		/** 52:リピーター */
		int REPEATER = 52;
		/** ルートコンテナ(レンダリング時にしか使わないはず) */
		int ROOT_CONTAINER = Integer.MAX_VALUE;
	}

	/** 入力値の型の定数 */
	public interface PartsInputType {
		/** 1:文字 */
		int TEXT = 1;
		/** 2:文章 */
		int TEXTAREA = 2;
		/** 3:文章（CLOB) */
		int CLOB = 3;
		/** 4:数値 */
		int NUMBER = 4;
		/** 5:日付 */
		int DATE = 5;
	}

	/** バリデーション型 */
	public interface ValidateType {
		/** 半角全角 */
		String ALL = "all";
		/** 年月(yyyy/MM) */
		String YM = "ym";
		/** 年月日(yyyy/MM/dd) */
		String DATE = "date";
		/** 時刻(HH:mm) */
		String TIME = "time";
		/** 郵便番号(日本) */
		String POSTCODE = "postCode";
		/** IPアドレス */
		String IPADDR = "ipAddr";
		/** 電話番号 */
		String TEL = "tel";
		/** メールアドレス */
		String MAIL = "mail";
		/** URL */
		String URL = "url";
		/** 数値型（±,.を含む数値） */
		String NUMERIC = "numeric";
		/** 整数型（±含む数値だが、,.を含まない） */
		String INTEGER = "integer";
		/** 数字のみ(0-9) */
		String NUMBER_ONLY = "numberOnly";
		/** アルファベット(大文字＋小文字) */
		String ALPHA = "alpha";
		/** アルファベット＋数字 */
		String ALPHA_NUMBER = "alphaNumber";
		/** アルファベット＋記号 */
		String ALPHA_SYMBOL = "alphaSymbol";
		/** アルファベット＋数字＋アンダースコア */
		String ALPHA_NUMBER_UNDERSCORE = "alphaNumberUnderscore";
		/** アルファベット＋数字＋記号 */
		String ALPHA_SYMBOL_NUMBER = "alphaSymbolNumber";
		/** アルファベット＋数字＋記号＋改行 */
		String ALPHA_SYMBOL_NUMBER_LF = "alphaSymbolNumberLf";
		/** 全角のみ */
		String FULL_WIDTH_ONLY = "fullWidthOnly";
		/** 半角のみ */
		String HALF_WIDTH_ONLY = "halfWidthOnly";
		/** 半角カナのみ */
		String HALF_KANA_ONLY = "halfKanaOnly";
		/** 全角カナのみ */
		String FULL_KANA_ONLY = "fullKanaOnly";
//		/** 正規表現 */
//		String REGEXP = "regExp";
//		/** 数値/英大文字/英小文字/記号のうち三種以上 */
//		String PASSWORD_COMPLEXITY = "passwordComplexity";
	}

	/** 数値フォーマット */
	public interface PartsNumberFormat {
		/** 9,999.0 */
		int N9_999_0 = 1;
		/** 9,999.9 */
		int N9_999_9 = 2;
		/** 9999.0 */
		int N9999_0 = 3;
		/** 9999.9 */
		int N9999_9 = 4;
	}

	/** 端数処理タイプ（BigDecimalの定数に準拠） */
	public interface PartsRoundType {
		/** 0:端数切上げ（0から離れるように丸めるモードです。破棄される0以外の小数部に先行する桁を常に増分します。この丸めモードは、計算された値の絶対値を減らしません） */
		int ROUND_UP = BigDecimal.ROUND_UP;
		/** 1:端数切捨て（0に近づくように丸めるモードです。破棄される小数部に先行する桁を増分しません(つまり切り捨て)。この丸めモードは、計算された値の絶対値を増やしません） */
		int ROUND_DOWN = BigDecimal.ROUND_DOWN;
//		/** 2:正の数なら切上げ、負の数なら切捨て（正の無限大に近づくように丸めるモードです。BigDecimalが正の場合はROUND_UPのように動作し、負の場合はROUND_DOWNのように動作します。この丸めモードは、計算された値を減らしません） */
//		int ROUND_CEILING = BigDecimal.ROUND_CEILING;
//		/** 3:正の数なら切捨て、負の数なら切上げ（負の無限大に近づくように丸めるモードです。BigDecimalが正の場合はROUND_DOWNのように動作し、負の場合はROUND_UPのように動作します。この丸めモードは、計算された値を増やしません。） */
//		int ROUND_FLOOR = BigDecimal.ROUND_FLOOR;
		/** 4:四捨五入（「もっとも近い数字」に丸める丸めモードです(ただし、両隣りの数字が等距離の場合は切り上げます)。破棄される小数部が0.5以上の場合はROUND_UPのように、それ以外の場合はROUND_DOWNのように動作します。これは我々の大半が小学校で習った丸めモードのことです。） */
		int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
//		/** 5:四捨五入（「もっとも近い数字」に丸める丸めモードです(両隣りの数字が等距離の場合は切り捨てます)。破棄される小数部が0.5を超える場合はROUND_UPのように、それ以外の場合はROUND_DOWNのように動作します。） */
//		int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
//		/** 6:四捨五入（「もっとも近い数字」に丸める丸めモードです(ただし、両隣りの数字が等距離の場合は偶数側に丸めます)。破棄する小数部の左側の桁が奇数の場合はROUND_HALF_UPのように、偶数の場合はROUND_HALF_DOWNのように動作します。この丸めモードは、連続する計算で繰返し適用される場合に累積エラーを最小限にします。） */
//		int ROUND_HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;
	}

	/** 桁タイプ */
	public interface LengthType {
		/** 1:文字数  */
		int CHAR_LENGTH = 1;
		/** 2:SHIFT-JIS換算でのバイト数 */
		int BYTE_SJIS = 2;
		/** 3:UTF-8でのバイト数 */
		int BYTE_UTF8 = 3;
	}

	/** パーツ表示条件.表示区分の定数 */
	public interface DcType {
		/** 0:未定義（パーツ表示条件が生成されていない） */
		int UKNOWN = 0;
		/** 1:入力可 */
		int INPUTABLE = 1;
		/** 2:入力不可 */
		int READONLY = 2;
		/** 3:非表示 */
		int HIDDEN = 3;

		/** 入力可とみなせる表示条件か */
		public static boolean isInputable(int dcType) {
			return INPUTABLE == dcType || UKNOWN == dcType;
		}
	}

	/** 表示方法の定数 */
	public interface DisplayMethod {
		/** 0:横表示 */
		int HORIZONTAL = 0;
		/** 1:縦表示 */
		int VERTICAL = 1;
	}

	/** フォントサイズの定数 */
	public interface FontSize {
		/** コンテナに従う（＝親コンテナのフォントサイズを継承） */
		int Inherit = 0;
		/** 小 */
		int Small = 1;
		/** 中 */
		int Medium = 2;
		/** 大 */
		int Large = 3;
		/** 巨大 */
		int Big = 4;

		/** フォントサイズをスタイル font-size の値として返す */
		public static String toStyle(int value) {
			if (FontSize.Inherit == value) return "inherit";
			if (FontSize.Small == value) return "80%";
			if (FontSize.Medium == value) return "120%";
			if (FontSize.Large == value) return "150%";
			if (FontSize.Big == value) return "200%";
			return null;
		}

		/** フォントサイズを CSSクラスとして返す（コントロール用） */
		public static String toCssClass(int value) {
			if (FontSize.Inherit == value) return "";
			if (FontSize.Small == value) return "input-sm";
			if (FontSize.Medium == value) return "input-md";
			if (FontSize.Large == value) return "input-lg";
			if (FontSize.Big == value) return "input-lg";
			return null;
		}

		/** フォントサイズを CSSクラスとして返す（コンテナ用） */
		public static String toContainerCssClass(int value) {
			if (FontSize.Inherit == value) return "";
			if (FontSize.Small == value) return "form-group-sm";
			if (FontSize.Medium == value) return "form-group-md";
			if (FontSize.Large == value) return "form-group-lg";
			if (FontSize.Big == value) return "form-group-lg";
			return null;
		}

		/** フォントの全選択肢 */
		public static int[] values() {
			return new int[] { Inherit, Small, Medium, Large, Big };
		}
	}

	/** レンダリングモードの定数 */
	public enum RenderMode {
		/** 画面デザイン時 */
		DESIGN,
		/** プレビュー時 */
		PREVIEW,
		/** 実行時／申請時 */
		RUNTIME,
		;
		public static RenderMode fromString(final String s) {
			if (MiscUtils.isEmpty(s))
				return RenderMode.DESIGN;
			return RenderMode.valueOf(s);
		}
	}

//	/** 画面遷移状態の定数 */
//	public enum ScreenTransition {
//		/** 入力中 */
//		INPUT,
//		/** 承認ルート編集中 */
//		ROUTE,
//		/** 確認中 */
//		CONFIRM,
//		/** 処理結果参照中 */
//		RESULT,
//		;
//		public static ScreenTransition fromString(final String s) {
//			if (MiscUtils.isEmpty(s))
//				return ScreenTransition.INPUT;
//			return ScreenTransition.valueOf(s);
//		}
//	}

	/** カラム型の定数 */
	public interface ColumnType {
		/** 1:VARCHAR */
		int VARCHAR = 1;
		/** 2:NUMBER */
		int NUMBER = 2;
		/** 3:DATE */
		int DATE = 3;
		/** 4:TIMESTAMP */
		int TIMESTAMP = 4;
		/** 5:CLOB */
		int CLOB = 5;
		/** 6:BLOB */
		int BLOB = 6;
	}

	/** レンダリング方法 */
	public interface RenderingMethod {
		/** 0:Bootstrapのグリッドでレンダリング */
		int BOOTSTRAP_GRID = 0;
		/** 1:インラインでレンダリング */
		int INLINE = 1;
	}

	/** 条件種別 */
	public interface ItemClass {
		/** 論理演算子 */
		String LOGICAL_OPERATOR = "1";
		/** カッコ */
		String PARENTHESIS = "2";
		/** パーツ */
		String PARTS = "3";
	}

	/** 比較演算子 */
	public interface Operator {
		/** ＝ */
		String EQUAL = "1";
		/** ≠ */
		String NOT_EQUAL = "2";
		/** ＞ */
		String GREATER_THAN = "3";
		/** ≧ */
		String GREATER_EQUAL = "4";
		/** ＜ */
		String LESS_THAN = "5";
		/** ≦ */
		String LESS_EQUAL = "6";

		public static String toFunction(String value) {
			if (StringUtils.equals(EQUAL, value)) return "Evaluate._eq";
			if (StringUtils.equals(NOT_EQUAL, value)) return "Evaluate._ne";
			if (StringUtils.equals(GREATER_THAN, value)) return "Evaluate._gt";
			if (StringUtils.equals(GREATER_EQUAL, value)) return "Evaluate._ge";
			if (StringUtils.equals(LESS_THAN, value)) return "Evaluate._lt";
			if (StringUtils.equals(LESS_EQUAL, value)) return "Evaluate._le";
			throw new RuntimeException("未定義の比較演算子です。デザイナー定義を確認してください。");
		}
	}

	/** 計算項目区分 */
	public interface CalcItemType {
		/** 算術演算子 */
		String ARITHMETIC_OPERATOR = "1";
		/** カッコ */
		String PARENTHESIS = "2";
		/** リテラル値 */
		String LITERAL = "3";
		/** パーツ */
		String PARTS = "4";
	}

	/** resources配下の静的Javascriptファイルの定数 */
	public interface StaticJavascript {
		/** 有効条件評価用 */
		String EnabledEvaluate = "enabledEvaluate.js";
	}

	/** 画面デザイナの生成するユーザデータのテーブルカラム定義 */
	public interface UserTable {
		String RUNTIME_ID = "RUNTIME_ID";
		String CORPORATION_CODE = "CORPORATION_CODE";
		String PROCESS_ID = "PROCESS_ID";
		String PARENT_RUNTIME_ID = "PARENT_RUNTIME_ID";
		String SORT_ORDER = "SORT_ORDER";
		String DELETE_FLAG = "DELETE_FLAG";
		String CORPORATION_CODE_CREATED = "CORPORATION_CODE_CREATED";
		String USER_CODE_CREATED = "USER_CODE_CREATED";
		String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
		String CORPORATION_CODE_UPDATED = "CORPORATION_CODE_UPDATED";
		String USER_CODE_UPDATED = "USER_CODE_UPDATED";
		String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";
		String VERSION = "VERSION";

		/** 更新不可なシステムカラム */
		public static final Set<String> NOT_UPDATABLES = Stream.of(
					UserTable.RUNTIME_ID, UserTable.CORPORATION_CODE,
					UserTable.PROCESS_ID, UserTable.PARENT_RUNTIME_ID,
					UserTable.CORPORATION_CODE_CREATED ,UserTable.USER_CODE_CREATED,
					UserTable.TIMESTAMP_CREATED
				)
				.collect(Collectors.toSet());

		/** 画面デザイナーの予約カラム名（ユーザが定義できないカラム） */
		public static final Set<String> RESERVED_COL_NAMES = MiscUtils.asSet(
				RUNTIME_ID, CORPORATION_CODE, PROCESS_ID, PARENT_RUNTIME_ID, SORT_ORDER,
				DELETE_FLAG, CORPORATION_CODE_CREATED, USER_CODE_CREATED,
				TIMESTAMP_CREATED, CORPORATION_CODE_UPDATED, USER_CODE_UPDATED,
				TIMESTAMP_UPDATED, VERSION);
	}

	/** ユーザ選択：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleUserSelect {
		/** ロール：企業コード */
		String CORPORATION_CODE = "corporationCode";
		/** ロール：ユーザコード */
		String USER_CODE = "userCode";
		/** ロール：氏名 */
		String USER_NAME = "userName";
		/** ロール：ユーザID */
		String USER_ADDED_INFO = "userAddedInfo";
		/** ロール：略称 */
		String USER_NAME_ABBR = "userNameAbbr";
		/** ロール：電話番号 */
		static final String TEL_NUM = "telNum";
		/** ロール：携帯電話番号 */
		String TEL_NUM_CEL = "telNumCel";
		/** ロール：メールアドレス */
		String MAIL_ADDRESS = "mailAddress";

		/** 全ロールの配列 */
		public static final String[] ROLE_CODES = {
				CORPORATION_CODE, USER_CODE, USER_NAME, USER_ADDED_INFO
				, USER_NAME_ABBR, TEL_NUM, TEL_NUM_CEL, MAIL_ADDRESS
		};
	}

	/** ユーザ選択のデフォルト値 */
	public interface DefaultUserSelect {
		/** デフォルト値：ログイン者 */
		String LOGIN_USER = "1";

		// 以下、結局どれを選んでも全部ログイン者と同じになってしまうので廃止
//		/** デフォルト値：入力者 */
//		String PROCESS_USER = "2";
//		/** デフォルト値：起案者 */
//		String START_USER = "3";
	}

	/** ユーザ選択：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleOrganizationSelect {
		/** ロール：企業コード */
		String CORPORATION_CODE = "corporationCode";
		/** ロール：組織コード */
		String ORGANIZATION_CODE = "organizationCode";
		/** ロール：組織名 */
		String ORGANIZATION_NAME = "organizationName";
		/** ロール：組織ID */
		String ORGANIZATION_ADDED_INFO = "organizationAddedInfo";
		/** ロール：略称 */
		String ORGANIZATION_NAME_ABBR = "organizationNameAbbr";
		/** ロール：住所 */
		String ADDRESS = "address";
		/** ロール：電話番号 */
		String TEL_NUM = "telNum";
		/** ロール：FAX番号 */
		String FAX_NUM = "faxNum";

		/** 全ロールの配列 */
		public static final String[] ROLE_CODES = {
				CORPORATION_CODE, ORGANIZATION_CODE, ORGANIZATION_NAME, ORGANIZATION_ADDED_INFO
				, ORGANIZATION_NAME_ABBR, TEL_NUM, FAX_NUM, ADDRESS
		};
	}

	/** ユーザ選択のデフォルト値 */
	public interface DefaultOrganizationSelect {
		/** デフォルト値：ログイン者 */
		String LOGIN_USER = "1";
	}

	/** テキストボックス：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleTextbox {
		String TEXT = "text";
	}

	/** 採番パーツ：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleNumbering {
		String NUMBERING = "numbering";
	}

	/** チェックボックスパーツ：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleCheckbox {
		String CHECK = "check";
	}

	/** ドロップダウンリスト：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleDropdown {
		String DROPDOWN_CODE = "dropdownCode";
		String DROPDOWN_LABEL = "dropdownLabel";
	}

	/** ラジオボタン：パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数 */
	public interface RoleRadio {
		String RADIO_CODE = "radioCode";
		String RADIO_LABEL = "radioLabel";
	}

	/** 汎用マスタパーツ.パーツを構成するエレメントで、どのエレメントが何を表すかを定めた定数  */
	public interface RoleMasterParts {
		String CODE = "code";
		String LABEL = "label";
	}

	/** パーツ関連マスタ.パーツI/O区分 */
	public interface PartsIoType {
		/** 1:入力 */
		String IN = "1";
		/** 2:出力 */
		String OUT = "2";
		/** 3:両方 */
		String BOTH = "3";
	}

	/** 未選択行の表示*/
	public interface EmptyLineType {
		/** 0:不使用 */
		int NONE = 0;
		/** 1:常に使用 */
		int USE_ALWAYS = 1;
		/** 2:複数行のみ使用 */
		int USE_MULTI = 2;
	}

	/** パーツのJavascriptイベント */
	public interface PartsEvents {
		/** 空行追加 */
		public String ADD_EMPTY_LINE = "addEmptyLine";
		/** 行削除後 */
		public String DELETE_LINE = "deleteLine";
		/** 行コピー後 */
		public String COPY_LINE = "copyLine";
		/** 行数変更後 */
		public String CHANGE_LINE_COUNT = "changeLineCount";
		/** ページ番号変更後 */
		public String CHANGE_PAGE_NO = "changePageNo";
		/** 選択前 */
		public String BEFORE_SELECT = "beforeSelect";
		/** 選択後 */
		public String AFTER_SELECT = "afterSelect";
		/** クリア前 */
		public String BEFORE_CLEAR = "beforeClear";
		/** クリア後 */
		public String AFTER_CLEAR = "afterClear";
		/** ポップアップを開くとき */
		public String OPEN_POPUP = "openPopup";
		/** ポップアップ閉じた直後(データの書き換え前) */
		public String BEFORE_CLOSE_POPUP = "beforeClosePopup";
		/** ポップアップ閉じた直後(データの書き換え後) */
		public String AFTER_CLOSE_POPUP = "afterClosePopup";
		/** 行の削除が可能か */
		public String CAN_DELETE_LINE = "canDeleteLine";
	}

	/** パーツのボタンサイズ */
	public interface PartsButtonSize {
		/** 0:小 */
		int SMALL = 0;
		/** 1:通常 */
		int NORMAL = 1;
		/** 2:大 */
		int LARGE = 2;
	}

	/** パーツの連動タイプ */
	public interface PartsCoodinationType {
		/** (日付／年月の)開始 */
		String PERIOD_FROM = "PERIOD_FROM";
		/** (日付／年月の)終了 */
		String PERIOD_TO = "PERIOD_TO";

		/** 連動タイプ：期間 */
		public static String[] getAsPeriods() {
			return new String[] { PERIOD_FROM, PERIOD_TO };
		}
	}

	/** パーツ条件区分 */
	public interface PartsConditionType {
		/** 1:有効条件 */
		String ENABLED = "1";
		/** 2:可視条件 */
		String VISIBLED = "2";
		/** 3:読取専用条件 */
		String READONLY = "3";
	}
}
