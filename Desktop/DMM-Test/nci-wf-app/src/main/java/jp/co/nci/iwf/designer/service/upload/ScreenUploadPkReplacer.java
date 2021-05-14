package jp.co.nci.iwf.designer.service.upload;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook.FormatType;
import jp.co.nci.iwf.component.upload.BasePkReplacer;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.component.upload.ReplacedPK;
import jp.co.nci.iwf.designer.DesignerCodeBook.CalcItemType;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.service.download.ScreenDownloadDto;
import jp.co.nci.iwf.endpoint.vd.vd0030.Up0010SaveConfig;
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
 * 画面アップロードのプライマリキーの置換エンジン
 */
@ApplicationScoped
public class ScreenUploadPkReplacer extends BasePkReplacer {

	/**
	 * DTO内の全エンティティに対して、各エンティティのユニークキーからプライマリキーの置換要否を調べ、
	 * すでに使われているのであればプライマリキーを置き換える。
	 * @param config アップロード設定
	 * @param dto
	 */
	public ChangedPKsMap replaceAllPK(Up0010SaveConfig config, ScreenDownloadDto dto) {
		log.debug("START replaceAllPK()");

		// プライマリーキー置換対象
		final ChangedPKsMap changedPKsMap = new ChangedPKsMap();
		//------------------------------------------
		// その他
		//------------------------------------------
		// 選択肢マスタ（MWM_OPTION）
		{
			final ChangedPKs<MwmOption> changes = replacePK(dto.optionList, MwmOption.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("optionId", changes, dto.partsList);
			// 選択肢項目マスタ（MWM_OPTION_ITEM）
			copyPK("optionId", changes, dto.optionItemList);
			// パーツ選択肢定義（MWM_PARTS_OPTION）
			copyPK("optionId", changes, dto.partsOptionList);
		}
		// 選択肢項目マスタ（MWM_OPTION_ITEM）
		{
			final ChangedPKs<MwmOptionItem> changes = replacePK(dto.optionItemList, MwmOptionItem.class);
			changedPKsMap.put(changes);
		}
		// 表示条件定義マスタ（MWM_DC）
		{
			final ChangedPKs<MwmDc> changes = replacePK(dto.dcList, MwmDc.class);
			changedPKsMap.put(changes);
			// パーツ表示条件（MWM_PARTS_DC）
			copyPK("dcId", changes, dto.partsDcList);
			// ブロック表示条件マスタ（MWM_BLOCK_DISPLAY）
			copyPK("dcId", changes, dto.blockDisplayList);
			// デフォルトブロック表示順マスタ（MWM_DEFAULT_BLOCK_DISPLAY）
			copyPK("dcId", changes, dto.defaultBlockDisplayList);
		}

		//------------------------------------------
		// Javascript
		//------------------------------------------
		// JavaScriptファイル定義（MWM_JAVASCRIPT）
		{
			final ChangedPKs<MwmJavascript> changes = replacePK(dto.javascriptList, MwmJavascript.class);
			changedPKsMap.put(changes);
			// JavaScriptファイル履歴（MWM_JAVASCRIPT_HISTORY）
			copyPK("javascriptId", changes, dto.javascriptHistoryList);
			// コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT）
			copyPK("javascriptId", changes, dto.containerJavascriptList);
			// 画面Javascript定義(MWM_SCREEN_JAVASCRIPT)
			copyPK("javascriptId", changes, dto.screenJavascriptList);
		}
		// JavaScriptファイル履歴（MWM_JAVASCRIPT_HISTORY）
		{
			final ChangedPKs<MwmJavascriptHistory> changes = replacePK(dto.javascriptHistoryList, MwmJavascriptHistory.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// コンテナ定義
		//------------------------------------------
		// コンテナ定義（MWM_CONTAINER）
		{
			final ChangedPKs<MwmContainer> changes = replacePK(dto.containerList, MwmContainer.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）
			copyPK("containerId", changes, dto.partsList);
			// コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT）
			copyPK("containerId", changes, dto.containerJavascriptList);
			// パーツカラム定義（MWM_PARTS_COLUMN）
			copyPK("containerId", changes, dto.partsColumnList);
			// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
			copyPK("childContainerId", changes, dto.partsChildHolderList);
			// 画面定義（MWM_SCREEN）
			copyPK("containerId", changes, dto.screenList);
		}
		// コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT）
		{
			final ChangedPKs<MwmContainerJavascript> changes = replacePK(dto.containerJavascriptList, MwmContainerJavascript.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 汎用テーブル
		//------------------------------------------
		// 汎用テーブルカテゴリ（MWM_CATEGORY）
		{
			final ChangedPKs<MwmCategory> changes = replacePK(dto.categoryList, MwmCategory.class);
			changedPKsMap.put(changes);
			// 汎用テーブルカテゴリ権限（MWM_CATEGORY_AUTHORITY）
			copyPK("categoryId", changes, dto.categoryAuthorityList);
			// 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG）
			copyPK("categoryId", changes, dto.categoryConfigList);
		}
		// 汎用テーブル（MWM_TABLE）
		{
			final ChangedPKs<MwmTable> changes = replacePK(dto.tableList, MwmTable.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("tableId", changes, dto.partsList);
			// 汎用テーブル検索（MWM_TABLE_SEARCH）
			copyPK("tableId", changes, dto.tableSearchList);
			// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
			copyPK("tableId", changes, dto.partsTableInfoList);
			// 汎用テーブル権限（MWM_TABLE_AUTHORITY）
			copyPK("tableId", changes, dto.tableAuthorityList);
			// 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG）
			copyPK("tableId", changes, dto.categoryConfigList);
		}
		// 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG）
		{
			final ChangedPKs<MwmCategoryConfig> changes = replacePK(dto.categoryConfigList, MwmCategoryConfig.class);
			changedPKsMap.put(changes);
		}
		// 汎用テーブルカテゴリ権限（MWM_CATEGORY_AUTHORITY）
		{
			final ChangedPKs<MwmCategoryAuthority> changes = replacePK(dto.categoryAuthorityList, MwmCategoryAuthority.class);
			changedPKsMap.put(changes);
		}
		// 汎用テーブル権限（MWM_TABLE_AUTHORITY）
		{
			final ChangedPKs<MwmTableAuthority> changes = replacePK(dto.tableAuthorityList, MwmTableAuthority.class);
			changedPKsMap.put(changes);
		}
		// 汎用テーブル検索（MWM_TABLE_SEARCH）
		{
			final ChangedPKs<MwmTableSearch> changes = replacePK(dto.tableSearchList, MwmTableSearch.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("tableSearchId", changes, dto.partsList);
			// 汎用テーブル検索カラム（MWM_TABLE_SEARCH_COLUMN）
			copyPK("tableSearchId", changes, dto.tableSearchColumnList);
			// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
			copyPK("tableSearchId", changes, dto.partsTableInfoList);
		}
		// 汎用テーブル検索カラム（MWM_TABLE_SEARCH_COLUMN）
		{
			final ChangedPKs<MwmTableSearchColumn> changes = replacePK(dto.tableSearchColumnList, MwmTableSearchColumn.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 採番
		//------------------------------------------
		// パーツ連番仕様マスタ（MWM_PARTS_SEQUENCE_SPEC）
		{
			final ChangedPKs<MwmPartsSequenceSpec> changes = replacePK(dto.partsSequenceSpecList, MwmPartsSequenceSpec.class);
			changedPKsMap.put(changes);
			// パーツ採番形式（MWM_PARTS_NUMBERING_FORMAT）
			// FORMAT_TYPE_X＝'S'ならFORMAT_VALUE_Xにはパーツ連番仕様マスタのIDを設定
			copyPK("formatValue1", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType1())));
			copyPK("formatValue2", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType2())));
			copyPK("formatValue3", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType3())));
			copyPK("formatValue4", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType4())));
			copyPK("formatValue5", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType5())));
			copyPK("formatValue6", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType6())));
			copyPK("formatValue7", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType7())));
			copyPK("formatValue8", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType8())));
			copyPK("formatValue9", changes, filter(dto.partsNumberingFormatList, e -> eq(FormatType.SEQUENCE, e.getFormatType9())));
		}
		// パーツ採番形式（MWM_PARTS_NUMBERING_FORMAT）
		{
			final ChangedPKs<MwmPartsNumberingFormat> changes = replacePK(dto.partsNumberingFormatList, MwmPartsNumberingFormat.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("partsNumberingFormatId", changes, dto.partsList);
		}

		//------------------------------------------
		// パーツ関係
		//------------------------------------------
		// パーツ定義（MWM_PARTS）
		{
			final ChangedPKs<MwmPart> changes = replacePK(dto.partsList, MwmPart.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("partsIdFor", changes, dto.partsList);				// 関連付けするパーツID
			replacePkInPartsExtInfo("inputedJudgePartsIds", changes, dto.partsList);	// 入力済み判定パーツIDリスト
			// パーツ計算式定義（MWM_PARTS_CALC）
			copyPK("partsId", changes, dto.partsCalcList);
			// パーツ計算式有効条件定義（MWM_PARTS_CALC_EC）
			copyPK("targetPartsId", changes, dto.partsCalcEcList);
			{
				copyPK("targetPartsId", changes, dto.partsCalcEcList);
				// EC_CLASS='3'なら EC_TYPEにはパーツIDが入る
				List<MwmPartsCalcEc> list = filter(dto.partsCalcEcList, ec -> eq(ec.getEcClass(), ItemClass.PARTS));
				copyPK("ecType", changes, list);
			}
			// パーツ計算項目定義（MWM_PARTS_CALC_ITEM）
			{
				// CALC_ITEM_TYPE='4'ならCALC_ITEM_VALUEにはパーツIDが入る
				List<MwmPartsCalcItem> list = filter(dto.partsCalcItemList, item -> eq(item.getCalcItemType(), CalcItemType.PARTS));
				copyPK("calcItemValue", changes, list);
			}
			// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
			copyPK("partsId", changes, dto.partsChildHolderList);
			// パーツカラム定義（MWM_PARTS_COLUMN）
			copyPK("partsId", changes, dto.partsColumnList);
			// パーツ表示条件（MWM_PARTS_DC）
			copyPK("partsId", changes, dto.partsDcList);
			// パーツ条件定義（MWM_PARTS_COND）
			{
				copyPK("partsId", changes, dto.partsCondList);
			}
			// パーツ条件項目定義（MWM_PARTS_COND_ITEM）
			{
				copyPK("targetPartsId", changes, dto.partsCondItemList);
				// ITEM_CLASS='3'なら COND_TYPEにはパーツIDが入る
				List<MwmPartsCondItem> list = filter(dto.partsCondItemList, ec -> eq(ec.getItemClass(), ItemClass.PARTS));
				copyPK("condType", changes, list);
			}
			// パーツ選択肢定義（MWM_PARTS_OPTION）
			copyPK("partsId", changes, dto.partsOptionList);
			// パーツ関連定義（MWM_PARTS_RELATION）
			copyPK("partsId", changes, dto.partsRelationList);
			copyPK("targetPartsId", changes, dto.partsRelationList);
			// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
			copyPK("partsId", changes, dto.partsTableInfoList);
			// パーツ添付ファイル定義(MWM_PARTS_ATTACH_FILE)
			copyPK("partsId", changes, dto.partsAttachFileList);
			// 画面パーツ条件定義（MWM_SCREEN_PARTS_COND）
			{
				copyPK("partsId", changes, dto.screenPartsCondList);
			}
			// 画面パーツ条件項目定義（MWM_SCREEN_PARTS_COND_ITEM）
			{
				copyPK("targetPartsId", changes, dto.screenPartsCondItemList);
				// ITEM_CLASS='3'なら EC_TYPEにはパーツIDが入る
				List<MwmScreenPartsCondItem> list = filter(dto.screenPartsCondItemList, ec -> eq(ec.getItemClass(), ItemClass.PARTS));
				copyPK("condType", changes, list);
			}
			// 画面計算式定義（MWM_SCREEN_CALC）
			copyPK("partsId", changes, dto.screenCalcList);
			// 画面計算式有効条件（MWM_SCREEN_CALC_EC）
			{
				copyPK("targetPartsId", changes, dto.screenCalcEcList);
				// EC_CLASS='3'なら EC_TYPEにはパーツIDが入る
				List<MwmScreenCalcEc> list = filter(dto.screenCalcEcList, ec -> eq(ec.getEcClass(), ItemClass.PARTS));
				copyPK("ecType", changes, list);
			}
			// 画面計算項目定義（MWM_SCREEN_CALC_ITEM）
			{
				// CALC_ITEM_TYPE='4'ならCALC_ITEM_VALUEにはパーツIDが入る
				List<MwmScreenCalcItem> list = filter(dto.screenCalcItemList, item -> eq(item.getCalcItemType(), CalcItemType.PARTS));
				copyPK("calcItemValue", changes, list);
			}
			// パーツイベント定義(MWM_PARTS_EVENT)
			copyPK("partsId", changes, dto.partsEventList);
		}
		// パーツ条件式定義（MWM_PARTS_COND）
		{
			final ChangedPKs<MwmPartsCond> changes = replacePK(dto.partsCondList, MwmPartsCond.class);
			changedPKsMap.put(changes);
			// パーツ条件項目定義（MWM_PARTS_COND_ITEM）
			copyPK("partsCondId", changes, dto.partsCondItemList);
		}
		// パーツ条件項目定義（MWM_PARTS_COND_ITEM）
		{
			final ChangedPKs<MwmPartsCondItem> changes = replacePK(dto.partsCondItemList, MwmPartsCondItem.class);
			changedPKsMap.put(changes);
		}
		// パーツ計算式定義（MWM_PARTS_CALC）
		{
			final ChangedPKs<MwmPartsCalc> changes = replacePK(dto.partsCalcList, MwmPartsCalc.class);
			changedPKsMap.put(changes);
			// パーツ計算式有効条件定義（MWM_PARTS_CALC_EC）
			copyPK("partsCalcId", changes, dto.partsCalcEcList);
			// パーツ計算項目定義（MWM_PARTS_CALC_ITEM）
			copyPK("partsCalcId", changes, dto.partsCalcItemList);
		}
		// パーツ計算式有効条件定義（MWM_PARTS_CALC_EC）
		{
			final ChangedPKs<MwmPartsCalcEc> changes = replacePK(dto.partsCalcEcList, MwmPartsCalcEc.class);
			changedPKsMap.put(changes);
		}
		// パーツ計算項目定義（MWM_PARTS_CALC_ITEM）
		{
			final ChangedPKs<MwmPartsCalcItem> changes = replacePK(dto.partsCalcItemList, MwmPartsCalcItem.class);
			changedPKsMap.put(changes);
		}
		// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
		{
			final ChangedPKs<MwmPartsChildHolder> changes = replacePK(dto.partsChildHolderList, MwmPartsChildHolder.class);
			changedPKsMap.put(changes);
		}
		// パーツカラム定義（MWM_PARTS_COLUMN）
		{
			final ChangedPKs<MwmPartsColumn> changes = replacePK(dto.partsColumnList, MwmPartsColumn.class);
			changedPKsMap.put(changes);
		}
		// パーツ表示条件（MWM_PARTS_DC）
		{
			final ChangedPKs<MwmPartsDc> changes = replacePK(dto.partsDcList, MwmPartsDc.class);
			changedPKsMap.put(changes);
		}
		// パーツ選択肢定義（MWM_PARTS_OPTION）
		{
			final ChangedPKs<MwmPartsOption> changes = replacePK(dto.partsOptionList, MwmPartsOption.class);
			changedPKsMap.put(changes);
		}
		// パーツ関連定義（MWM_PARTS_RELATION）
		{
			final ChangedPKs<MwmPartsRelation> changes = replacePK(dto.partsRelationList, MwmPartsRelation.class);
			changedPKsMap.put(changes);
		}
		// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
		{
			final ChangedPKs<MwmPartsTableInfo> changes = replacePK(dto.partsTableInfoList, MwmPartsTableInfo.class);
			changedPKsMap.put(changes);
		}
		// パーツ添付ファイル定義(MWM_PARTS_ATTACH_FILE)
		{
			final ChangedPKs<MwmPartsAttachFile> changes = replacePK(dto.partsAttachFileList, MwmPartsAttachFile.class);
			changedPKsMap.put(changes);
			// パーツ定義（MWM_PARTS）.拡張情報
			replacePkInPartsExtInfo("partsAttachFileId", changes, dto.partsList);
		}
		// パーツイベント定義(MWM_PARTS_EVENT)
		{
			final ChangedPKs<MwmPartsEvent> changes = replacePK(dto.partsEventList, MwmPartsEvent.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 画面定義
		//------------------------------------------
		// 画面定義（MWM_SCREEN）
		{
			final ChangedPKs<MwmScreen> changes = replacePK(dto.screenList, MwmScreen.class);
			changedPKsMap.put(changes);
			// 画面計算式定義（MWM_SCREEN_CALC）
			copyPK("screenId", changes, dto.screenCalcList);
			// 画面パーツ条件定義（MWM_SCREEN_PARTS_COND）
			copyPK("screenId", changes, dto.screenPartsCondList);
			// 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF）
			copyPK("screenId", changes, dto.screenProcessDefList);
			// 画面Javascript定義（MWM_SCREEN_JAVASCRIPT）
			copyPK("screenId", changes, dto.screenJavascriptList);
		}
		// 画面計算式定義（MWM_SCREEN_CALC）
		{
			final ChangedPKs<MwmScreenCalc> changes = replacePK(dto.screenCalcList, MwmScreenCalc.class);
			changedPKsMap.put(changes);
			// 画面計算式有効条件（MWM_SCREEN_CALC_EC）
			copyPK("screenCalcId", changes, dto.screenCalcEcList);
			// 画面計算項目定義（MWM_SCREEN_CALC_ITEM）
			copyPK("screenCalcId", changes, dto.screenCalcItemList);
		}
		// 画面計算式有効条件（MWM_SCREEN_CALC_EC）
		{
			final ChangedPKs<MwmScreenCalcEc> changes = replacePK(dto.screenCalcEcList, MwmScreenCalcEc.class);
			changedPKsMap.put(changes);
		}
		// 画面計算項目定義（MWM_SCREEN_CALC_ITEM）
		{
			final ChangedPKs<MwmScreenCalcItem> changes = replacePK(dto.screenCalcItemList, MwmScreenCalcItem.class);
			changedPKsMap.put(changes);
		}
		// 画面パーツ条件定義（MWM_SCREEN_PARTS_COND）
		{
			final ChangedPKs<MwmScreenPartsCond> changes = replacePK(dto.screenPartsCondList, MwmScreenPartsCond.class);
			changedPKsMap.put(changes);
			// 画面パーツ条件項目定義（MWM_SCREEN_PARTS_COND_ITEM）
			copyPK("screenPartsCondId", changes, dto.screenPartsCondItemList);
		}
		// 画面パーツ条件項目定義（MWM_SCREEN_PARTS_COND_ITEM）
		{
			final ChangedPKs<MwmScreenPartsCondItem> changes = replacePK(dto.screenPartsCondItemList, MwmScreenPartsCondItem.class);
			changedPKsMap.put(changes);
		}
		// 画面Javascript定義（MWM_SCREEN_JAVASCRIPT）
		{
			final ChangedPKs<MwmScreenJavascript> changes = replacePK(dto.screenJavascriptList, MwmScreenJavascript.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 画面プロセス定義
		//------------------------------------------
		if (config.isScreenProcess) {
			// 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF）
			{
				final ChangedPKs<MwmScreenProcessDef> changes = replacePK(dto.screenProcessDefList, MwmScreenProcessDef.class);
				changedPKsMap.put(changes);
				// アクセス可能画面マスタ（MWM_ACCESSIBLE_SCREEN）
				copyPK("screenProcessId", changes, dto.accessibleScreenList);
				// ブロック表示条件マスタ（MWM_BLOCK_DISPLAY）
				copyPK("screenProcessId", changes, dto.blockDisplayList);
				// 画面プロセス／メニュー連携マスタ(MWM_SCREEN_PROCESS_MENU)
				copyPK("screenProcessId", changes, dto.screenProcessMenuList);
			}
			// ブロック表示条件マスタ（MWM_BLOCK_DISPLAY）
			{
				final ChangedPKs<MwmBlockDisplay> changes = replacePK(dto.blockDisplayList, MwmBlockDisplay.class);
				changedPKsMap.put(changes);
			}
			// デフォルトブロック表示順マスタ（MWM_DEFAULT_BLOCK_DISPLAY）
			{
				final ChangedPKs<MwmDefaultBlockDisplay> changes = replacePK(dto.defaultBlockDisplayList, MwmDefaultBlockDisplay.class);
				changedPKsMap.put(changes);
			}
			// 画面プロセス／メニュー連携マスタ(MWM_SCREEN_PROCESS_MENU)
			if (config.isScreenProcessMenu){
				final ChangedPKs<MwmScreenProcessMenu> changes = replacePK(dto.screenProcessMenuList, MwmScreenProcessMenu.class);
				changedPKsMap.put(changes);
			}

			// 画面プロセス定義階層マスタ（MWM_SCREEN_PROCESS_LEVEL）
			if (config.isFolder) {
				final ChangedPKs<MwmScreenProcessLevel> changes = replacePK(dto.screenProcessLevelList, MwmScreenProcessLevel.class);
				changedPKsMap.put(changes);
				// 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF）
				copyPK("screenProcessLevelId", changes, dto.screenProcessDefList);
			}

			// アクセス可能画面マスタ（MWM_ACCESSIBLE_SCREEN）
			if (config.isPublication){
				final ChangedPKs<MwmAccessibleScreen> changes = replacePK(dto.accessibleScreenList, MwmAccessibleScreen.class);
				changedPKsMap.put(changes);
			}
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			final ChangedPKs<MwmMultilingual> changes = replacePK(dto.multilingualList, changedPKsMap);
			changedPKsMap.put(changes);
		}

		log.debug("END replaceAllPK()");

		return changedPKsMap;
	}

	/**
	 * パーツ拡張情報に含まれている他テーブルのPKを書き換え
	 * @param pkFieldName 書き換え対象のPKフィールド名
	 * @param changes PKの変更前・変更後を記録したMap
	 * @param partsList パーツリスト
	 */
	@SuppressWarnings("unchecked")
	private void replacePkInPartsExtInfo(String pkFieldName, ChangedPKs<?> changes, List<MwmPart> partsList) {
		for (MwmPart parts : partsList) {
			// JSON文字列であるパーツ拡張情報をMap化
			final Map<String, Object> map = toObjFromJson(parts.getExtInfo(), Map.class);

			// フィールド値が書き換え対象であるかを調べて、該当すれば置換
			final Object v = map.get(pkFieldName);
			if (v instanceof List) {
				// フィールド型がリストであれば、その要素のいずれかが旧パーツIDに該当すれば置換する
				final List<Long> collections = (List<Long>)v;
				for (int i = 0; i < collections.size(); i++) {
					final Long oldPK = toLong(collections.get(i));
					if (oldPK != null) {
						final ReplacedPK replaced = changes.get(oldPK);
						if (replaced != null) {
							final Long newPK = replaced.newPK;
							collections.set(i, newPK);
						}
					}
				}
			}
			else {
				// その他はフィールド値を旧パーツIDとみなして、該当すれば置換
				final Long oldPK = toLong(v);
				if (oldPK != null) {
					final ReplacedPK replaced = changes.get(oldPK);
					if (replaced != null) {
						Long newPK = replaced.newPK;
						map.put(pkFieldName, newPK);
					}
				}
			}
			// 置換し終えたMapをJSON文字列に書き戻して格納
			final String json = toJsonFromObj(map);
			parts.setExtInfo(json);
		}
	}
}