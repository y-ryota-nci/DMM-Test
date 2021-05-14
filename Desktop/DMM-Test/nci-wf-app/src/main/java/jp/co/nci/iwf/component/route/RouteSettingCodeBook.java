package jp.co.nci.iwf.component.route;

public interface RouteSettingCodeBook {

	/**
	 * プロセス定義コード枝番
	 */
	interface ProcessDefDetailCode {
		/** デフォルト */
		String DEFAULT = "00001";
	}

	/**
	 * アクティビティ定義
	 */
	interface ActivityDef {
		/** 開始X座標 */
		Long START_XCOORDINATE = new Long(100);
		/** 開始Y座標 */
		Long START_YCOORDINATE = new Long(640);
		/** 終了X座標 */
		Long END_XCOORDINATE = new Long(900);
		/** 終了Y座標 */
		Long END_YCOORDINATE = new Long(640);
	}

	/**
	 * 業務管理項目区分
	 */
	interface BusinessInfoType {
		/** 検索キー */
		String SEARCH_KEY = "1";
		/** WF変数  */
		String WF_VARIABLE = "2";
		/** 両方 */
		String BOTH = "3";
	}

	/**
	 * 有効フラグ
	 */
	interface ValidFlag {
		/** 無効 */
		String INVALID = "0";
		/** 有効 */
		String VALID = "1";
	}
}