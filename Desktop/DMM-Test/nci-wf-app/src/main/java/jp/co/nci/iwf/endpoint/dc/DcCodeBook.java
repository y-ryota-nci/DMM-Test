package jp.co.nci.iwf.endpoint.dc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public interface DcCodeBook {

	interface TreeItem {
		String ID = "home";
		String TOP = "top";
		String PARENT = "#";
	}

	interface Icon {
		String HOME = "glyphicon glyphicon-home";
		String LIST = "glyphicon glyphicon-list";
		String BOOK = "glyphicon glyphicon-folder-open";
		String FILE = "glyphicon glyphicon-file";
	}

	interface Type {
		String HOME = "home";
		String BOOK = "book";
		String FILE = "file";
	}

	interface State {
		String OPENED = "opened";
		String DISABLED = "disabled";
		String SELECTED = "selected";
	}

	interface DataKey {
		String VERSION = "version";
		String HIERARCHY_ID = "hierarchyId";
		String SORTORDER = "sortOrder";
		String SCREEN_DOC_LEVEL_ID = "screenDocLevelId";
		String EXPANSION_FLAG = "expansionFlag";
		String SCREEN_DOC_ID = "screenDocId";
		String VALID_START_DATE = "validStartDate";
		String VALID_END_DATE = "validEndDate";
		String FOLDER_PATH = "folderPath";
	}

	interface Auth {
		String ON = "1";
		String OFF = "0";
	}

	interface AuthBelongType {
		String ROLE = "R";
		String USER = "U";
	}

	/** 文書更新区分 */
	interface UpdateVersionType {
		/** 0：版を更新しない */
		String DO_NOT_UPDATE = "0";
		/** 1：小さな変更として更新する(マイナーバージョンアップ) */
		String MINOR_VERSION_UP = "1";
		/** 2：大きな変更として更新する(メジャーバージョンアップ) */
		String MAJOR_VERSION_UP = "2";
	}

	/** 文書コンテンツ種別 */
	interface ContentsType {
		/** 1:業務文書 */
		String BIZ_DOC = "1";
		/** 2:バインダー */
		String BINDER = "2";
		/** 3:ファイル */
		String FILE = "3";
	}

	/** 文書権限 */
	public enum DocAuth {
		/** 参照 */
		REFER,
		/** ダウンロード */
		DOWNLOAD,
		/** 編集 */
		EDIT,
		/** 削除 */
		DELETE,
		/** コピー */
		COPY,
		/** 移動 */
		MOVE,
		/** 印刷 */
		PRINT,
	}

	/** 検索タイプ */
	interface SearchType {
		/** 1:通常検索 */
		String NORMAL = "1";
		/** 2:簡易検索(キーワード検索) */
		String SIMPLE = "2";
		/** 3:詳細検索 */
		String ADVANCED = "3";
	}

	/** 文書管理用ブロックのブロックIDの定数 */
	interface DocBlockIds {
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
		/** 5:文書属性(拡張) */
		int ATTRIBUTE_EX = 5;
		/** 6:文書ファイル */
		int DOC_FILE = 6;
		/** 7:更新履歴 */
		int HISTORY = 7;;
		/** 8:メモ情報 */
		int MEMO = 8;
		/** 9:バインダー */
		int BINDER = 9;
		/** 10:添付ファイル */
		int ATTACH_FILE = 10;
		/** 11:WF連携 */
		int WF_RELATION = 11;
	}

	/** 文書トレイタイプ */
	public enum DocTrayType {
		/** 詳細検索 */
		DETAIL_SEARCH,
		;

		public boolean equals(String s) {
			if (s == null) return false;
			return this.toString().equals(s);
		}

		/** jackson用、文字列を列挙型TrayTypeへ変換 */
		@JsonCreator
		public static DocTrayType fromValue(String value) {
			if (value == null || value.length() == 0)
				return null;
			return DocTrayType.valueOf(value);
		}

		/** jackson用、列挙型TrayTypeを文字列へ変換 */
		@JsonValue
		public static String toValue(DocTrayType docTrayType) {
			return (docTrayType == null ? "" : docTrayType.name());
		}
	}

	/** 文書WF連携区分. */
	interface DocWfRelationType {
		/** 1:WF→文書管理への連携 */
		String TO_DOC = "1";
		/** 2:文書管理→WFへの連携 */
		String TO_WF = "2";
	}

	/** 文書更新区分 */
	public enum DocUpdateType {
		/** 登録 */
		REGIST,
		/** 更新 */
		UPDATE,
		/** 削除 */
		DELETE,
		/** コピー */
		COPY,
		/** 移動 */
		MOVE,
		/** WF連携 */
		WF,
	}

	/** 文書管理用の表示条件ID */
	interface DocDcId {
		/** 101:編集時 */
		Long EDITING = 101L;
		/** 102:参照時 */
		Long REFERRING = 102L;
	}
}
