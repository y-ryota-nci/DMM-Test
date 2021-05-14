package jp.co.nci.iwf.designer.service.download;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmDefaultBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascriptHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsChildHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearchColumn;

/**
 * 画面定義のダウンロード用DTO
 */
public class ScreenDownloadDto extends BaseDownloadDto implements Serializable {
	//------------------------------------------
	// パーツ関係
	//------------------------------------------
	/** パーツ定義（MWM_PARTS） */
	public List<MwmPart> partsList = new ArrayList<>();
	/** パーツ計算式定義（MWM_PARTS_CALC） */
	public List<MwmPartsCalc> partsCalcList = new ArrayList<>();
	/** パーツ計算式有効条件定義（MWM_PARTS_CALC_EC） */
	public List<MwmPartsCalcEc> partsCalcEcList = new ArrayList<>();
	/** パーツ計算項目定義（MWM_PARTS_CALC_ITEM） */
	public List<MwmPartsCalcItem> partsCalcItemList = new ArrayList<>();
	/** パーツ子要素定義（MWM_PARTS_CHILD_HOLDER） */
	public List<MwmPartsChildHolder> partsChildHolderList = new ArrayList<>();
	/** パーツカラム定義（MWM_PARTS_COLUMN） */
	public List<MwmPartsColumn> partsColumnList = new ArrayList<>();
	/** パーツ表示条件（MWM_PARTS_DC） */
	public List<MwmPartsDc> partsDcList = new ArrayList<>();
	/** パーツ条件定義（MWM_PARTS_COND） */
	public List<MwmPartsCond> partsCondList = new ArrayList<>();
	/** パーツ条件項目定義（MWM_PARTS_COND_ITEM） */
	public List<MwmPartsCondItem> partsCondItemList = new ArrayList<>();
	/** パーツ選択肢定義（MWM_PARTS_OPTION） */
	public List<MwmPartsOption> partsOptionList = new ArrayList<>();
	/** パーツ関連定義（MWM_PARTS_RELATION） */
	public List<MwmPartsRelation> partsRelationList = new ArrayList<>();
	/** パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO） */
	public List<MwmPartsTableInfo> partsTableInfoList = new ArrayList<>();
	/** パーツ添付ファイル定義(MWM_PARTS_ATTACH_FILE) */
	public List<MwmPartsAttachFile> partsAttachFileList = new ArrayList<>();
	/** パーツイベント定義(MWM_PARTS_EVENT) */
	public List<MwmPartsEvent> partsEventList = new ArrayList<>();

	//------------------------------------------
	// 画面定義
	//------------------------------------------
	/** 画面定義（MWM_SCREEN） */
	public List<MwmScreen> screenList = new ArrayList<>();
	/** 画面計算式定義（MWM_SCREEN_CALC） */
	public List<MwmScreenCalc> screenCalcList = new ArrayList<>();
	/** 画面計算式有効条件（MWM_SCREEN_CALC_EC） */
	public List<MwmScreenCalcEc> screenCalcEcList = new ArrayList<>();
	/** 画面計算項目定義（MWM_SCREEN_CALC_ITEM） */
	public List<MwmScreenCalcItem> screenCalcItemList = new ArrayList<>();
	/** 画面パーツ条件定義（MWM_SCREEN_PARTS_COND） */
	public List<MwmScreenPartsCond> screenPartsCondList = new ArrayList<>();
	/** 画面パーツ条件項目定義（MWM_SCREEN_PARTS_COND_ITEM） */
	public List<MwmScreenPartsCondItem> screenPartsCondItemList = new ArrayList<>();
	/** 画面Javascript定義（MWM_SCREEN_JAVASCRIPT） */
	public List<MwmScreenJavascript> screenJavascriptList = new ArrayList<>();

	//------------------------------------------
	// コンテナ定義
	//------------------------------------------
	/** コンテナ定義（MWM_CONTAINER） */
	public List<MwmContainer> containerList = new ArrayList<>();
	/** コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT） */
	public List<MwmContainerJavascript> containerJavascriptList = new ArrayList<>();

	//------------------------------------------
	// 画面プロセス定義
	//------------------------------------------
	/** 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF） */
	public List<MwmScreenProcessDef> screenProcessDefList = new ArrayList<>();
	/** 画面プロセス定義階層マスタ（MWM_SCREEN_PROCESS_LEVEL） */
	public List<MwmScreenProcessLevel> screenProcessLevelList = new ArrayList<>();
	/** アクセス可能画面マスタ（MWM_ACCESSIBLE_SCREEN） */
	public List<MwmAccessibleScreen> accessibleScreenList = new ArrayList<>();
	/** ブロック表示条件マスタ（MWM_BLOCK_DISPLAY） */
	public List<MwmBlockDisplay> blockDisplayList = new ArrayList<>();
	/** 画面プロセス／メニュー連携マスタ(MWM_SCREEN_PROCESS_MENU) */
	public List<MwmScreenProcessMenu> screenProcessMenuList = new ArrayList<>();

	//------------------------------------------
	// 汎用テーブル
	//------------------------------------------
	/** 汎用テーブル（MWM_TABLE） */
	public List<MwmTable> tableList = new ArrayList<>();
	/** 汎用テーブル権限（MWM_TABLE_AUTHORITY） */
	public List<MwmTableAuthority> tableAuthorityList = new ArrayList<>();
	/** 汎用テーブル検索（MWM_TABLE_SEARCH） */
	public List<MwmTableSearch> tableSearchList = new ArrayList<>();
	/** 汎用テーブル検索カラム（MWM_TABLE_SEARCH_COLUMN） */
	public List<MwmTableSearchColumn> tableSearchColumnList = new ArrayList<>();
	/** 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG） */
	public List<MwmCategoryConfig> categoryConfigList = new ArrayList<>();
	/** 汎用テーブルカテゴリ（MWM_CATEGORY） */
	public List<MwmCategory> categoryList = new ArrayList<>();
	/** 汎用テーブルカテゴリ権限（MWM_CATEGORY_AUTHORITY） */
	public List<MwmCategoryAuthority> categoryAuthorityList = new ArrayList<>();

	//------------------------------------------
	// 採番
	//------------------------------------------
	/** パーツ採番形式（MWM_PARTS_NUMBERING_FORMAT） */
	public List<MwmPartsNumberingFormat> partsNumberingFormatList = new ArrayList<>();
	/** パーツ連番仕様マスタ（MWM_PARTS_SEQUENCE_SPEC） */
	public List<MwmPartsSequenceSpec> partsSequenceSpecList = new ArrayList<>();

	//------------------------------------------
	// Javascript
	//------------------------------------------
	/** JavaScriptファイル定義（MWM_JAVASCRIPT） */
	public List<MwmJavascript> javascriptList = new ArrayList<>();
	/** JavaScriptファイル履歴（MWM_JAVASCRIPT_HISTORY） */
	public List<MwmJavascriptHistory> javascriptHistoryList = new ArrayList<>();

	//------------------------------------------
	// その他
	//------------------------------------------
	/** 選択肢マスタ（MWM_OPTION） */
	public List<MwmOption> optionList = new ArrayList<>();
	/** 選択肢項目マスタ（MWM_OPTION_ITEM） */
	public List<MwmOptionItem> optionItemList = new ArrayList<>();
	/** 表示条件定義マスタ（MWM_DC） */
	public List<MwmDc> dcList = new ArrayList<>();
	/** デフォルトブロック表示順マスタ（MWM_DEFAULT_BLOCK_DISPLAY） */
	public List<MwmDefaultBlockDisplay> defaultBlockDisplayList;


	//------------------------------------------
	// 多言語
	//------------------------------------------
	/** 多言語対応マスタ（MWM_MULTILINGUAL） */
	public List<MwmMultilingual> multilingualList = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public ScreenDownloadDto() {}

	/**
	 * コンストラクタ
	 * @param corporationCode
	 * @param corporationName
	 */
	public ScreenDownloadDto(String corporationCode, String corporationName) {
		super(corporationCode, corporationName);
	}
}
