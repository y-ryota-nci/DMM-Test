package jp.co.nci.iwf.designer.service.upload;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.upload.BaseUploadRepository;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.designer.service.download.ScreenDownloadDto;
import jp.co.nci.iwf.designer.service.download.ScreenDownloadService;
import jp.co.nci.iwf.endpoint.vd.vd0030.Up0010SaveConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面アップロードサービス
 */
@BizLogic
public class ScreenUploadService extends BaseUploadRepository {
	@Inject private ScreenUploadPkReplacer replacer;
	@Inject private ScreenDownloadService downloader;
	@Inject private ScreenUploadRepository repository;
	@Inject private Logger log;

	@PostConstruct
	public void init() {
	}

	/**
	 * アップロード内容をDBへ反映
	 * @param config アップロード設定
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 */
	public void persist(Up0010SaveConfig config, ScreenDownloadDto dto) {
		// ★実際の更新処理：採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる
		syncMwmNumbering(dto);

		// ◇ユーザ指定内容でユニークキーを書き換え（画面コード／コンテナコードなど）
		replaceUniqueKeys(config, dto);

		// ◇既存データがあれば、プライマリキーを既存データのもので置換（メモリ上のみ）
		final ChangedPKsMap changedPKsMap = replacer.replaceAllPK(config, dto);

		// ◇差分更新するため、現在のデータベースの内容を抽出（メモリ上のみ）
		final ScreenDownloadDto currentDto = downloader.createDto(config.corporationCode, dto);

		// ◇アップロードデータの改変
		overrideEntities(config, dto, changedPKsMap);

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		upsertAll(config, dto, currentDto, changedPKsMap);

		// アップロード日時の更新
		updateUploadDatetime(dto);
	}

	/** 画面マスタ.アップロード日時の更新 */
	private void updateUploadDatetime(ScreenDownloadDto dto) {
		for (MwmScreen s : dto.screenList) {
			repository.updateUploadDatetime(s);
		}
	}

	/**
	 * アップロードデータの改変
	 * @param config アップロード設定
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 * @param changedPKsMap PK変更点Map
	 */
	private void overrideEntities(Up0010SaveConfig config, ScreenDownloadDto dto, ChangedPKsMap changedPKsMap) {
		// コンテナ
		for (MwmContainer c : dto.containerList) {
			// テーブル同期日時をクリアすることで、テーブル定義が最新でない可能性があることを知らしめる
			c.setTableSyncTimestamp(null);
		}
	}

	/**
	 * 採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 */
	private void syncMwmNumbering(ScreenDownloadDto dto) {
		log.debug("START syncMwmNumbering()");

		// DTOのフィールドがリストであればアップロード用エンティティとみなす
		for (Field f : dto.getClass().getDeclaredFields()) {
			if (!List.class.isAssignableFrom(f.getType()))
				continue;
			final List<? extends MwmBaseJpaEntity> entities = getFieldValue(dto, f.getName());
			final Class<? extends MwmBaseJpaEntity> clazz = getParameterizedType(f);
			long max = 0L;
			if (entities != null && !entities.isEmpty()) {
				// 各エンティティのプライマリキーの最大値を求める
				final String pkFieldName = jpaEntityDef.getPkFieldName(clazz);
				max = entities.stream()
						.map(e -> (Long)getPropertyValue(e, pkFieldName))
						.filter(id -> id != null)
						.max((v1, v2) -> compareTo(v1, v2))
						.orElse(0L);
			}
			// 実テーブルの最大値
			final String tblName = jpaEntityDef.getTableName(clazz);
			final String pkColName = jpaEntityDef.getPkColumnName(clazz);
			final long current = repository.getCurrentMaxValue(tblName, pkColName);
			if (max < current) {
				max = current;
			}

			// エンティティのプライマリキーの最大値で「採番マスタの現在値」を更新
			// （採番マスタ.現在値＞新しい値なら更新されないよ）
			if (max > 0L)
				numbering.sync(clazz, max);
		};
		log.debug("END syncMwmNumbering()");
	}

	/**
	 * エンティティに設定されている企業コードを、アップロード先企業コードに書き換える
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 * @param corporationCode 企業コード
	 */
	@SuppressWarnings("unchecked")
	private void replaceByNewCorporationCode(ScreenDownloadDto dto, String corporationCode) {
		for (Field field : dto.getClass().getDeclaredFields()) {
			if (!List.class.isAssignableFrom(field.getType()))
				continue;

			try {
				List<? extends MwmBaseJpaEntity> entities = (List<? extends MwmBaseJpaEntity>)field.get(dto);
				for (MwmBaseJpaEntity e : entities) {
					PropertyDescriptor pd = getPropertyDescriptor(e, "corporationCode");
					if (pd == null || pd.getWriteMethod() == null)
						break;
					pd.getWriteMethod().invoke(e, corporationCode);
				}
			}
			catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				throw new InternalServerErrorException("エンティティへアップロード先企業コードをセットしようとして失敗しました。", e);
			}
		};
	}

	/**
	 * アップロードファイルと現在のデータベース内容の差分更新を行う
	 * @param req リクエスト
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 * @param currentDto 現在のデータベースの内容
	 * @param changedPKsMap PK変更内容
	 */
	private void upsertAll(
			Up0010SaveConfig config,
			ScreenDownloadDto dto,
			ScreenDownloadDto currentDto,
			ChangedPKsMap changedPKsMap
	) {
		log.debug("START upsertAll()");

		//------------------------------------------
		// パーツ関係
		//------------------------------------------
		{
			// パーツ定義（MWM_PARTS）
			upsert(dto.partsList, currentDto.partsList, changedPKsMap, true, dto.containerList);
			final Set<Long> containerIdInUse = dto.containerList.stream()
					.map(c -> c.getContainerId())
					.collect(Collectors.toSet());
			final List<MwmPart> partsInUse = dto.partsList.stream()
					.filter(p -> containerIdInUse.contains(p.getContainerId()))
					.collect(Collectors.toList());
			final Set<Long> partsIdInUse = partsInUse.stream()
					.map(p -> p.getPartsId())
					.collect(Collectors.toSet());
			// パーツ計算式定義（MWM_PARTS_CALC）
			upsert(dto.partsCalcList, currentDto.partsCalcList, changedPKsMap, true, partsInUse);
			final List<MwmPartsCalc> partsCalcInUse = dto.partsCalcList.stream()
					.filter(pc -> partsIdInUse.contains(pc.getPartsId()))
					.collect(Collectors.toList());
			// パーツ計算式有効条件定義（MWM_PARTS_CALC_EC）
			upsert(dto.partsCalcEcList, currentDto.partsCalcEcList, changedPKsMap, true, partsCalcInUse);
			// パーツ計算項目定義（MWM_PARTS_CALC_ITEM）
			upsert(dto.partsCalcItemList, currentDto.partsCalcItemList, changedPKsMap, true, partsCalcInUse);
			// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
			upsert(dto.partsChildHolderList, currentDto.partsChildHolderList, changedPKsMap, true, partsInUse);
			// パーツカラム定義（MWM_PARTS_COLUMN）
			upsert(dto.partsColumnList, currentDto.partsColumnList, changedPKsMap, true, partsInUse);
			// パーツ表示条件（MWM_PARTS_DC）
			upsert(dto.partsDcList, currentDto.partsDcList, changedPKsMap, true, partsInUse);
			// パーツ条件定義（MWM_PARTS_COND）
			upsert(dto.partsCondList, currentDto.partsCondList, changedPKsMap, true, partsInUse);
			final List<MwmPartsCond> partsCondInUse = dto.partsCondList.stream()
					.filter(pc -> partsIdInUse.contains(pc.getPartsId()))
					.collect(Collectors.toList());
			// パーツ条件項目定義（MWM_PARTS_COND_ITEM）
			upsert(dto.partsCondItemList, currentDto.partsCondItemList, changedPKsMap, true, partsCondInUse);
			// パーツ選択肢定義（MWM_PARTS_OPTION）
			upsert(dto.partsOptionList, currentDto.partsOptionList, changedPKsMap, true, partsInUse);
			// パーツ関連定義（MWM_PARTS_RELATION）
			upsert(dto.partsRelationList, currentDto.partsRelationList, changedPKsMap, true, partsInUse);
			// パーツ汎用テーブル情報（MWM_PARTS_TABLE_INFO）
			upsert(dto.partsTableInfoList, currentDto.partsTableInfoList, changedPKsMap, true, partsInUse);
			// パーツ添付ファイル定義(MWM_PARTS_ATTACH_FILE)
			upsert(dto.partsAttachFileList, currentDto.partsAttachFileList, changedPKsMap, true, partsInUse);
			// パーツイベント定義(MWM_PARTS_EVENT)
			upsert(dto.partsEventList, currentDto.partsEventList, changedPKsMap, true, partsInUse);
		}
		//------------------------------------------
		// 画面定義
		//------------------------------------------
		{
			// 画面定義（MWM_SCREEN）
			upsert(dto.screenList, currentDto.screenList, changedPKsMap, true);
			// 画面計算式定義（MWM_SCREEN_CALC）
			upsert(dto.screenCalcList, currentDto.screenCalcList, changedPKsMap, true);
			// 画面計算式有効条件（MWM_SCREEN_CALC_EC）
			upsert(dto.screenCalcEcList, currentDto.screenCalcEcList, changedPKsMap, true);
			// 画面計算項目定義（MWM_SCREEN_CALC_ITEM）
			upsert(dto.screenCalcItemList, currentDto.screenCalcItemList, changedPKsMap, true);
			// 画面パーツ条件定義（MWM_SCREEN_PARTS_COND）
			upsert(dto.screenPartsCondList, currentDto.screenPartsCondList, changedPKsMap, true);
			// 画面パーツ条件項目定義（MWM_SCREEN_PARTS_COND_ITEM）
			upsert(dto.screenPartsCondItemList, currentDto.screenPartsCondItemList, changedPKsMap, true);
			// 画面Javascript定義（MWM_SCREEN_JAVASCRIPT）
			upsert(dto.screenJavascriptList, currentDto.screenJavascriptList, changedPKsMap, true);
		}
		//------------------------------------------
		// コンテナ定義
		//------------------------------------------
		{
			// コンテナ定義（MWM_CONTAINER）
			upsert(dto.containerList, currentDto.containerList, changedPKsMap, false);
			// コンテナJavaScript定義（MWM_CONTAINER_JAVASCRIPT）
			upsert(dto.containerJavascriptList, currentDto.containerJavascriptList, changedPKsMap, true, dto.containerList);
		}
		//------------------------------------------
		// 画面プロセス定義
		//------------------------------------------
		{
			if (config.isScreenProcess) {
				// 画面プロセス定義マスタ（MWM_SCREEN_PROCESS_DEF）
				upsert(dto.screenProcessDefList, currentDto.screenProcessDefList, changedPKsMap, true);
				// ブロック表示条件マスタ（MWM_BLOCK_DISPLAY）
				upsert(dto.blockDisplayList, currentDto.blockDisplayList, changedPKsMap, true, dto.screenProcessDefList);
				// デフォルトブロック表示順マスタ（MWM_DEFAULT_BLOCK_DISPLAY）
				upsert(dto.defaultBlockDisplayList, currentDto.defaultBlockDisplayList, changedPKsMap, true);

				// 画面プロセス／メニュー連携マスタ(MWM_SCREEN_PROCESS_MENU)
				if (config.isScreenProcessMenu) {
					upsert(dto.screenProcessMenuList, currentDto.screenProcessMenuList, changedPKsMap, true, dto.screenProcessDefList);
				}

				// 画面プロセス定義階層マスタ（MWM_SCREEN_PROCESS_LEVEL）
				if (config.isFolder) {
					upsert(dto.screenProcessLevelList, currentDto.screenProcessLevelList, changedPKsMap, false);
				}

				if (config.isPublication) {
					// アクセス可能画面マスタ（MWM_ACCESSIBLE_SCREEN）
					upsert(dto.accessibleScreenList, currentDto.accessibleScreenList, changedPKsMap, true, dto.screenProcessDefList);
				}
			}
		}
		//------------------------------------------
		// 汎用テーブル
		//------------------------------------------
		// 汎用テーブル（MWM_TABLE）
		{
			upsert(dto.tableList, currentDto.tableList, changedPKsMap, false);
			// 汎用テーブル権限（MWM_TABLE_AUTHORITY）
			upsert(dto.tableAuthorityList, currentDto.tableAuthorityList, changedPKsMap, true, dto.tableList);
			// 汎用テーブル検索（MWM_TABLE_SEARCH）
			upsert(dto.tableSearchList, currentDto.tableSearchList, changedPKsMap, true, dto.tableList);
			// 汎用テーブル検索カラム（MWM_TABLE_SEARCH_COLUMN）
			upsert(dto.tableSearchColumnList, currentDto.tableSearchColumnList, changedPKsMap, true, dto.tableSearchList);
			// 汎用テーブルカテゴリ構成（MWM_CATEGORY_CONFIG）
			upsert(dto.categoryConfigList, currentDto.categoryConfigList, changedPKsMap, true, dto.tableList);
			// 汎用テーブルカテゴリ（MWM_CATEGORY）
			upsert(dto.categoryList, currentDto.categoryList, changedPKsMap, false);
			// 汎用テーブルカテゴリ権限（MWM_CATEGORY_AUTHORITY）
			upsert(dto.categoryAuthorityList, currentDto.categoryAuthorityList, changedPKsMap, true, dto.categoryList);
		}
		//------------------------------------------
		// 採番
		//------------------------------------------
		{
			// パーツ採番形式（MWM_PARTS_NUMBERING_FORMAT）
			// パーツ採番形式は書式区分=[S]連番のときに書式値に連番仕様IDが設定されているので、それを置換しなければならぬ
			modifyPartsSequenceSpecId(dto.partsNumberingFormatList, changedPKsMap);
			upsert(dto.partsNumberingFormatList, currentDto.partsNumberingFormatList, changedPKsMap, false);
			// パーツ連番仕様マスタ（MWM_PARTS_SEQUENCE_SPEC）
			upsert(dto.partsSequenceSpecList, currentDto.partsSequenceSpecList, changedPKsMap, false);
		}
		//------------------------------------------
		// Javascript
		//------------------------------------------
		{
			// JavaScriptファイル定義（MWM_JAVASCRIPT）
			upsert(dto.javascriptList, currentDto.javascriptList, changedPKsMap, false);
			// JavaScriptファイル履歴（MWM_JAVASCRIPT_HISTORY）
			upsert(dto.javascriptHistoryList, currentDto.javascriptHistoryList, changedPKsMap, true, dto.javascriptList);
		}
		//------------------------------------------
		// その他
		//------------------------------------------
		{
			// 選択肢マスタ（MWM_OPTION）
			upsert(dto.optionList, currentDto.optionList, changedPKsMap, false);
			// 選択肢項目マスタ（MWM_OPTION_ITEM）
			upsert(dto.optionItemList, currentDto.optionItemList, changedPKsMap, true, dto.optionList);
			// 表示条件定義マスタ（MWM_DC）
			upsert(dto.dcList, currentDto.dcList, changedPKsMap, true);
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語対応マスタ（MWM_MULTILINGUAL）
			Map<String, MwmMultilingual> currents =
					currentDto.multilingualList.stream().collect(Collectors.toMap(m -> toKey(m), m -> m));
			upsertMwmMultilingual(dto.multilingualList, currents, changedPKsMap);
		}
		log.debug("END upsertAll()");
	}

	/** パーツ採番形式マスタへの、パーツ連番仕様IDの置換処理 */
	private void modifyPartsSequenceSpecId(List<MwmPartsNumberingFormat> partsNumberingFormatList,
			ChangedPKsMap changedPKsMap) {

		ChangedPKs<?> changed = changedPKsMap.get(MwmPartsSequenceSpec.class);
		for (MwmPartsNumberingFormat f : partsNumberingFormatList) {
			for (int colNo = 1; colNo < 10; colNo++) {
				// 書式区分=[S]連番なら、書式値にはパーツ連番仕様マスタ.パーツ連番仕様IDが
				// 設定されているので、旧IDを新IDで置換してやる
				final String formatType = getPropertyValue(f, "formatType" + colNo++);
				if (eq(formatType, FormatType.SEQUENCE)) {
					final Long oldId = getPropertyValue(f, "formatValue" + colNo++);
					if (changed.containsKey(oldId)) {
						final Long newId = changed.get(oldId).newPK;
						setPropertyValue(f, "formatValue", newId);
					}
				}
			}
		}
	}

	/**
	 * ユーザ指定内容でユニークキーを書き換え
	 * @param config アップロード設定
	 * @param dto
	 */
	private void replaceUniqueKeys(Up0010SaveConfig config, ScreenDownloadDto dto) {
		// ◇企業コードをアップロード先企業へ変更（メモリ上のみ）
		replaceByNewCorporationCode(dto, config.corporationCode);

		// 新しい画面コード
		for (MwmScreen s : dto.screenList) {
			s.setScreenCode(config.newScreenCode);
		}

		// 新しいコンテナコード
		for (MwmContainer c : dto.containerList) {
			String containerCode = c.getContainerCode();
			if (!config.newContainerCodes.containsKey(containerCode))
				throw new InternalServerErrorException("旧コンテナコード(" + containerCode + ")がアップロードされたコンテナコードにありません");
			String newContainerCode = config.newContainerCodes.get(containerCode);
			if (isEmpty(newContainerCode))
				throw new InternalServerErrorException("旧コンテナコード(" + containerCode + ")に対応する新コンテナコードが空です");
			c.setContainerCode(newContainerCode);
		}
	}

	/** 既存の画面コードが存在するか */
	public boolean existScreenCode(String corporationCode, String screenCode) {
		return repository.existScreenCode(corporationCode, screenCode);
	}

	/** 既存のコンテナコードが存在するか */
	public boolean existsContainerCode(String corporationCode, String containerCode) {
		return repository.existsContainerCode(corporationCode, containerCode);
	}
}
