package jp.co.nci.iwf.designer.service.download;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.designer.service.PartsExtService;
import jp.co.nci.iwf.designer.service.numbering.PartsNumberingService;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面定義ダウンロードのサービス
 */
@BizLogic
public class ScreenDownloadService extends BaseDownloadService {
	@Inject private ScreenDownloadRepository repository;
	@Inject private PartsExtService extService;
	@Inject private PartsNumberingService pnService;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private CorporationService corp;

	/**
	 * 画面コードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param screenCode 画面コード
	 * @return
	 */
	public ScreenDownloadDto createDto(MwmScreen s) {
		final Long[] containerIds = repository.getContainerIdsByScreenId(s.getScreenId());
		return createDto(s.getCorporationCode(), s.getScreenId(), containerIds);
	}

	/**
	 * 現在のアップロードファイルの画面コードとコンテナコードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param screenCode 画面コード
	 * @return
	 */
	public ScreenDownloadDto createDto(String corporationCode, ScreenDownloadDto uploadDto) {
		// アップロードファイルの画面コードから、この環境での差分比較対象である画面IDを求める
		final String screenCode = uploadDto.screenList.get(0).getScreenCode();
		final List<MwmScreen> list = repository.getMwmScreenByUniqueKey(corporationCode, screenCode);
		if (list.size() > 1)
			throw new InternalServerErrorException("画面コード=" + screenCode + "はユニークキーのはずですが、に対応したレコードが" + list.size() + "件ありました。MWM_SCREENのユニークキーの定義に誤りがあります");
		final long screenId = list.isEmpty() ? -1L : list.get(0).getScreenId();

		// アップロードファイルのコンテナコードから、この環境での差分比較対象であるコンテナIDを求める
		// アップロードした際に差分更新をするため、現在のDBのコンテナ情報ではなくアップロードファイルのコンテナコードのコンテナ情報を求めて差分を取る
		final List<String> containerCodes = uploadDto.containerList.stream()
				.map(c -> c.getContainerCode())
				.collect(Collectors.toList());
		final Long[] containerIds = repository.getContainerIdsByContainerCode(corporationCode, containerCodes);

		// 差分比較対象である画面IDとコンテナIDをつかって、
		return createDto(corporationCode, screenId, containerIds);
	}

	/**
	 * 画面コードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param screenCode 画面コード
	 * @return
	 */
	private ScreenDownloadDto createDto(String corporationCode, long screenId, Long[] containerIds) {
		final WfmCorporation c = corp.getWfmCorporation(corporationCode);
		final ScreenDownloadDto dto = new ScreenDownloadDto(corporationCode, c.getCorporationName());
		final Long[] screenIds = new Long[]{ screenId };
		final Long[] screenProcessIds = repository.getScreenProcessIdsByScreenId(screenId);

		//------------------------------------------
		// ダウンロード時のAPPバージョンとDB接続先情報
		//------------------------------------------
		dto.appVersion = manifest.getVersion();
		dto.dbDestination = destination.getUrl();
		dto.dbUser = destination.getUser();
		dto.timestampCreated = timestamp();
		dto.hostIpAddr = hsr.getLocalAddr();
		dto.hostName = hsr.getLocalName();

		//------------------------------------------
		// パーツ関係
		//------------------------------------------
		final Set<Long> partsNumberingFormatIds, tableIds, tableSearchIds, optionIds, partsCalcIds;
		{
			// パーツ定義（MWM_PARTS）
			dto.partsList = repository.getMwmParts(containerIds);
			partsNumberingFormatIds = toPartsNumberingFormatIds(dto.partsList);
			// パーツ計算式定義（MWM_PARTS_CALC）
			dto.partsCalcList = repository.getMwmPartsCalc(containerIds);
			partsCalcIds = toPartsCalcIds(dto.partsCalcList);
			// パーツ計算式有効条件定義（MWM_PARTS_CALC_EC）
			dto.partsCalcEcList = repository.getMwmPartsCalcEc(containerIds);
			// パーツ計算項目定義（MWM_PARTS_CALC_ITEM）
			dto.partsCalcItemList = repository.getMwmPartsCalcItem(containerIds);
			// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
			dto.partsChildHolderList =  repository.getMwmPartsChildHolder(containerIds);
			// パーツカラム定義（MWM_PARTS_COLUMN）
			dto.partsColumnList = repository.getMwmPartsColumn(containerIds);
			// パーツ表示条件（MWM_PARTS_DC）
			dto.partsDcList = repository.getMwmPartsDc(containerIds);
			// パーツ条件定義（MWM_PARTS_COND）
			dto.partsCondList = repository.getMwmPartsCond(containerIds);
			// パーツ条件項目定義（MWM_PARTS_COND_ITEM）
			dto.partsCondItemList = repository.getMwmPartsCondItem(containerIds);
			// パーツ選択肢定義（MWM_PARTS_OPTION）
			dto.partsOptionList = repository.getMwmPartsOption(containerIds);
			optionIds = toOptionIds(dto.partsOptionList);
			// パーツ関連定義（MWM_PARTS_RELATION）
			dto.partsRelationList = repository.getMwmPartsRelation(containerIds);
			// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
			dto.partsTableInfoList = repository.getMwmPartsTableInfo(containerIds);
			tableIds = toTableIds(dto.partsTableInfoList);
			tableSearchIds = toTableSearchIds(dto.partsTableInfoList);
			// パーツ添付ファイル情報(MWM_PARTS_ATTACH_FILE)
			dto.partsAttachFileList = repository.getMwmPartsAttachFile(containerIds);
			// パーツイベント定義
			dto.partsEventList = repository.getMwmPartsEvent(containerIds);
		}
		//------------------------------------------
		// 画面定義
		//------------------------------------------
		{
			// 画面定義（MWM_SCREEN）
			dto.screenList = repository.getMwmScreen(screenIds);
			// 画面計算式定義（MWM_SCREEN_CALC）
			dto.screenCalcList = repository.getMwmScreenCalc(screenIds);
			// 画面計算式有効条件（MWM_SCREEN_CALC_EC）
			dto.screenCalcEcList = repository.getMwmScreenCalcEc(screenIds);
			// 画面計算項目定義（MWM_SCREEN_CALC_ITEM）
			dto.screenCalcItemList = repository.getMwmScreenCalcItem(screenIds);
			// 画面パーツ条件定義（MWM_PARTS_COND）
			dto.screenPartsCondList = repository.getMwmScreenPartsCond(screenIds);
			// 画面パーツ条件項目定義（MWM_PARTS_COND_ITEM）
			dto.screenPartsCondItemList = repository.getMwmScreenPartsCondItem(screenIds);
			// 画面Javascript定義(MWM_SCREEN_JAVASCRIPT)
			dto.screenJavascriptList = repository.getMwmScreenJavascript(screenIds);
		}
		//------------------------------------------
		// コンテナ定義
		//------------------------------------------
		final Set<Long> javascriptIds;
		{
			// コンテナ定義（MWM_CONTAINER）
			dto.containerList = repository.getMwmContainer(containerIds);
			// コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT）
			dto.containerJavascriptList = repository.getMwmContainerJavascript(containerIds);
			javascriptIds = toJavascriptIds(dto.containerJavascriptList);
		}
		//------------------------------------------
		// 画面プロセス定義
		//------------------------------------------
		{
			// 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF）
			dto.screenProcessDefList = repository.getMwmScreenProcessDef(screenProcessIds);
			// 画面プロセス定義階層マスタ（MWM_SCREEN_PROCESS_LEVEL）
			dto.screenProcessLevelList = repository.getMwmScreenProcessLevel(screenProcessIds);
			// アクセス可能画面マスタ（MWM_ACCESSIBLE_SCREEN）
			dto.accessibleScreenList = repository.getMwmAccessibleScreen(screenProcessIds);
			// ブロック表示条件マスタ（MWM_BLOCK_DISPLAY）
			dto.blockDisplayList = repository.getMwmBlockDisplay(screenProcessIds);
			// デフォルトブロック表示順マスタ（MWM_DEFAULT_BLOCK_DISPLAY）
			dto.defaultBlockDisplayList = repository.getMwmDefaultBlockDisplay(corporationCode);
			// 画面プロセス／メニュー連携マスタ(MWM_SCREEN_PROCESS_MENU)
			dto.screenProcessMenuList = repository.getMwmScreenProcessMenu(screenProcessIds);
		}
		//------------------------------------------
		// 汎用テーブル
		//------------------------------------------
		// 汎用テーブル（MWM_TABLE）
		final Set<Long> categoryIds;
		{
			dto.tableList = repository.getMwmTable(tableIds);
			// 汎用テーブル権限（MWM_TABLE_AUTHORITY）
			dto.tableAuthorityList = repository.getMwmTableAuthority(corporationCode, tableIds);
			// 汎用テーブル検索（MWM_TABLE_SEARCH）
			dto.tableSearchList = repository.getMwmTableSearch(corporationCode, tableSearchIds);
			// 汎用テーブル検索カラム（MWM_TABLE_SEARCH_COLUMN）
			dto.tableSearchColumnList = repository.getMwmTableSearchColumn(tableSearchIds);
			// 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG）
			dto.categoryConfigList = repository.getMwmCategoryConfig(corporationCode, tableIds);
			categoryIds = toCategoryIds(dto.categoryConfigList);
			// 汎用テーブルカテゴリ（MWM_CATEGORY）
			dto.categoryList = repository.getMwmCategory(corporationCode, categoryIds);
			// 汎用テーブルカテゴリ権限（MWM_CATEGORY_AUTHORITY）
			dto.categoryAuthorityList = repository.getMwmCategoryAuthority(corporationCode, categoryIds);
		}
		//------------------------------------------
		// 採番
		//------------------------------------------
		final Set<Long> partsSequenceSpecIds;
		{
			// パーツ採番形式（MWM_PARTS_NUMBERING_FORMAT）
			dto.partsNumberingFormatList = repository.getMwmPartsNumberingFormat(partsNumberingFormatIds);
			// パーツ連番仕様マスタ（MWM_PARTS_SEQUENCE_SPEC）
			partsSequenceSpecIds = toPartsSequenceSpecIds(partsNumberingFormatIds);
			dto.partsSequenceSpecList = repository.getMwmPartsSequenceSpec(partsSequenceSpecIds);
		}
		//------------------------------------------
		// Javascript
		//------------------------------------------
		{
			// JavaScriptファイル定義（MWM_JAVASCRIPT）
			dto.javascriptList = repository.getMwmJavascript(javascriptIds);
			// JavaScriptファイル履歴（MWM_JAVASCRIPT_HISTORY）
			dto.javascriptHistoryList = repository.getMwmJavascriptHistory(javascriptIds);
		}
		//------------------------------------------
		// その他
		//------------------------------------------
		Set<Long> dcIds;
		{
			// 選択肢マスタ（MWM_OPTION）
			dto.optionList = repository.getMwmOption(optionIds);
			// 選択肢項目マスタ（MWM_OPTION_ITEM）
			dto.optionItemList = repository.getMwmOptionItem(optionIds);
			// 表示条件定義マスタ（MWM_DC）
			dcIds = getAllDcIds();
			dto.dcList = repository.getMwmDc(dcIds);
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語対応マスタ（MWM_MULTILINGUAL）
			dto.multilingualList = new ArrayList<>(256);
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_PARTS", "PARTS_ID", "CONTAINER_ID", containerIds));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_PARTS_CALC", "PARTS_CALC_ID", partsCalcIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_CONTAINER", "CONTAINER_ID", containerIds));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_JAVASCRIPT", "JAVASCRIPT_ID", javascriptIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_SCREEN", "SCREEN_ID", screenIds));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_SCREEN_CALC", "SCREEN_ID", screenIds));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_SCREEN_PROCESS_DEF", "SCREEN_PROCESS_ID", screenProcessIds));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_DC", "DC_ID", dcIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_TABLE", "TABLE_ID", tableIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_TABLE_SEARCH", "TABLE_SEARCH_ID", tableSearchIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_TABLE_SEARCH_COLUMN", "TABLE_SEARCH_ID", tableSearchIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_CATEGORY", "CATEGORY_ID", categoryIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_PARTS_NUMBERING_FORMAT", "PARTS_NUMBERING_FORMAT_ID", partsNumberingFormatIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_PARTS_SEQUENCE_SPEC", "PARTS_SEQUENCE_SPEC_ID", partsSequenceSpecIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_OPTION", "OPTION_ID", optionIds.toArray()));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_OPTION_ITEM", "OPTION_ITEM_ID", "OPTION_ID", optionIds.toArray()));
			// MWM_SCREEN_PROCESS_LEVELだけはデータ構造がちょっと変わっているので、汎用関数が使えず専用メソッドだ
			dto.multilingualList.addAll(repository.getMwmMultilingual("SCREEN_PROCESS_ID", screenProcessIds));
		}

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}

	/** パーツ計算式定義からパーツ計算式定義IDをリスト化 */
	private Set<Long> toPartsCalcIds(List<MwmPartsCalc> partsCalcList) {
		return partsCalcList.stream()
				.map(pc -> pc.getPartsCalcId())
				.collect(Collectors.toSet());
	}

	/** パーツ表示条件から表示条件IDをリスト化 */
	private Set<Long> getAllDcIds() {
		return repository.getMwmDcAll().stream()
				.map(dc -> dc.getDcId())
				.collect(Collectors.toSet());
	}

	/** パーツ選択肢から選択肢IDをリスト化 */
	private Set<Long> toOptionIds(List<MwmPartsOption> partsOptions) {
		return partsOptions.stream()
				.filter(o -> o.getOptionId() != null)
				.map(o -> o.getOptionId())
				.collect(Collectors.toSet());
	}

	/** 汎用テーブル構成から汎用テーブルカテゴリIDをリスト化 */
	private Set<Long> toCategoryIds(List<MwmCategoryConfig> configs) {
		return configs.stream()
				.filter(c -> c.getCategoryId() != null)
				.map(c -> c.getCategoryId())
				.collect(Collectors.toSet());
	}

	/** 汎用テーブル検索条件IDをリスト化 */
	private Set<Long> toTableSearchIds(List<MwmPartsTableInfo> tblList) {
		return tblList
				.stream()
				.filter(t -> t.getTableSearchId() != null)
				.map(t -> t.getTableSearchId())
				.collect(Collectors.toSet());
	}

	/** 汎用テーブルIDをリスト化 */
	private Set<Long> toTableIds(List<MwmPartsTableInfo> tblList) {
		return tblList
				.stream()
				.filter(t -> t.getTableId() != null)
				.map(t -> t.getTableId())
				.collect(Collectors.toSet());
	}

	/** パーツ採番形式で連番にあたるものから、パーツ連番仕様IDをリスト化 */
	private Set<Long> toPartsSequenceSpecIds(Set<Long> partsNumberingFormatIds) {
		Set<Long> ids = new HashSet<>();
		for (Long partsNumberingFormatId : partsNumberingFormatIds) {
			ids.addAll( pnService.getPartsNumberingSpecIds(partsNumberingFormatId) );
		}
		return ids;
	}

	/** 拡張情報からパーツ採番仕様IDを抜き出してリスト化 */
	private Set<Long> toPartsNumberingFormatIds(List<MwmPart> partsList) {
		Set<Long> ids = new HashSet<>();
		partsList.forEach(p -> {
			String id = extService.getExtValue(p.getExtInfo(), "partsNumberingFormatId");
			if (isNotEmpty(id))
				ids.add(Long.valueOf(id));
		});
		return ids;
	}

	private Set<Long> toJavascriptIds(List<MwmContainerJavascript> cjList) {
		Set<Long> javascriptIds;
		javascriptIds = cjList.stream().map(cj -> cj.getJavascriptId()).collect(Collectors.toSet());
		return javascriptIds;
	}
}
