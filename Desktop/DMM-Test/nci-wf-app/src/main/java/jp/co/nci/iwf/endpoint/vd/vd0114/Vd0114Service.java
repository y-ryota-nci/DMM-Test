package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.DesignerCodeBook.FontSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsCoodinationType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.PartsAttachFileEntity;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOption;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRepeater;
import jp.co.nci.iwf.designer.parts.design.PartsDesignSearchButton;
import jp.co.nci.iwf.designer.service.PartsAttachFileService;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * パーツプロパティ設定画面サービス
 */
@BizLogic
public class Vd0114Service extends BaseService {
	@Inject
	private Vd0114Repository repository;
	@Inject
	private MwmLookupService lookup;
	@Inject
	private TableMetaDataService meta;
	@Inject
	private PartsAttachFileService partsAttachFileService;

	/** ページサイズ */
	private static final Integer[] pageSizes = { 2, 5, 10, 20, 50, 100 };
	/** グリッドパーツで、セルに指定可能なパーツ種別 */
	private static final List<Integer> gridTargetPartsTypes = Arrays.asList(
			PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO
			, PartsType.DROPDOWN, PartsType.MASTER, PartsType.SEARCH_BUTTON
			, PartsType.EVENT_BUTTON, PartsType.ATTACHFILE, PartsType.IMAGE);
	/** 入力済み判定対象のパーツ種別 */
	private static final List<Integer> inputedJudgePartsTypes = Arrays.asList(
			PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO
			, PartsType.DROPDOWN, PartsType.MASTER, PartsType.NUMBERING
			, PartsType.ORGANIZATION, PartsType.USER);

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Vd0114Response res = createResponse(Vd0114Response.class, req);
		res.success = true;

		// フォントサイズの選択肢
		res.fontSizes = createFontSizeList();

		// カラム幅の選択肢(foundationのcssクラス 'small-x'や'medium-x'などの 'x'部分に相当)
		res.columnSizes = createColumnSizeList(RenderingMethod.BOOTSTRAP_GRID);

		// レンダリング方法の選択肢
		res.renderingMethods = lookup.getOptionItems(LookupGroupId.RENDERING_METHOD, false);

		// 業務管理項目の選択肢
		res.businessInfoCodes = createBusinessInfoCodes();

		// 文書管理項目の選択肢
		res.docBusinessInfoCodes = createDocBusinessInfoCodes();

		return res;
	}

	/** 業務管理項目の選択肢 */
	public List<OptionItem> createBusinessInfoCodes() {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		repository.getMwmBusinessInfoName(corporationCode, localeCode).forEach(b -> {
			items.add(new OptionItem(
					b.getBusinessInfoCode(),
					b.getBusinessInfoCode() + " " + b.getBusinessInfoName()));
		});
		return items;
	}

	/** 文書管理項目の選択肢 */
	public List<OptionItem> createDocBusinessInfoCodes() {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		repository.getMwmDocBusinessInfoName(corporationCode, localeCode).forEach(b -> {
			items.add(new OptionItem(
					b.getDocBusinessInfoCode(),
					b.getDocBusinessInfoCode() + " " + b.getDocBusinessInfoName()));
		});
		return items;
	}

	/** フォントサイズの選択肢 */
	public List<OptionItem> createFontSizeList() {
		return Arrays.asList(
				new OptionItem(FontSize.Inherit, i18n.getText(MessageCd.inheritContainer)),
				new OptionItem(FontSize.Small, i18n.getText(MessageCd.fontSizeSmall)),
				new OptionItem(FontSize.Medium, i18n.getText(MessageCd.fontSizeMedium)),
				new OptionItem(FontSize.Large, i18n.getText(MessageCd.fontSizeLarge)),
				new OptionItem(FontSize.Big, i18n.getText(MessageCd.fontSizeBig))
		);
	}

	/** カラム幅の選択肢 */
	private List<OptionItem> createColumnSizeList(int renderingMethod) {
		final List<OptionItem> columnSizes = new ArrayList<>();
		columnSizes.add(OptionItem.EMPTY);

		// レンダリング方法により、幅の選択肢を切り替える
		if (RenderingMethod.BOOTSTRAP_GRID == renderingMethod) {
			for (int x = 1; x <= 12; x++) {
				String value = String.valueOf(x);
				columnSizes.add(new OptionItem(value, value));
			}
		}
		return columnSizes;
	}

	/**
	 * Textboxパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseTextbox initTextbox(Vd0114Request req) {
		final Vd0114ResponseTextbox res = createResponse(Vd0114ResponseTextbox.class, req);

		// 入力タイプ選択肢
		res.inputTypeList = lookup.getOptionItems(LookupGroupId.PARTS_INPUT_TYPE, true);
		// 入力チェックタイプ選択肢（文字）
		res.stringValidateTypeList = lookup.getOptionItems(LookupGroupId.PARTS_VALIDATE_TYPE_STRING, true);
		// 入力チェックタイプ選択肢（文字）
		res.numberValidateTypeList = lookup.getOptionItems(LookupGroupId.PARTS_VALIDATE_TYPE_NUMBER, true);
		// 入力チェックタイプ選択肢（日付）
		res.dateValidateTypeList = lookup.getOptionItems(LookupGroupId.PARTS_VALIDATE_TYPE_DATE, true);
		// 桁数タイプ選択肢
		res.lengthTypeList = lookup.getOptionItems(LookupGroupId.LENGTH_TYPE, true);
		// IME制御の格納方法
		res.imeModeList = lookup.getOptionItems(LookupGroupId.IME_MODE, true);
		// 数値フォーマット選択肢
		res.numberFormatList = lookup.getOptionItems(LookupGroupId.NUMBER_FORMAT, true);
		// 端数処理
		res.roundTypeList = lookup.getOptionItems(LookupGroupId.PARTS_ROUND_TYPE, true);
		// パーセントの格納方法
		res.saveMethodPercentList = lookup.getOptionItems(LookupGroupId.SAVE_METHOD_PERCENT, true);
		// 連動タイプ（期間）
		res.coodinationTypeList = lookup.getOptionItems(LookupGroupId.PARTS_COODINATION_TYPE, true, PartsCoodinationType.getAsPeriods());

		// カラム型の変更可否
		final List<PartsColumn> columns = req.design.columns;
		final String tableName = req.tableName;
		res.lockInputType =  isNotEmpty(tableName)
				&& meta.isExistTable(tableName)
				&& meta.isExistColumn(tableName, columns)
				&& meta.isExistRecord(tableName);

		res.success = true;
		return res;
	}

	/**
	 * リピーターパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseRepeater initRepeater(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Vd0114ResponseRepeater res = createResponse(Vd0114ResponseRepeater.class, req);

		// ページ制御(ページサイズ)の選択肢
		res.pageSizes = new ArrayList<>();
		res.pageSizes.add(new OptionItem("", i18n.getText(MessageCd.noPaging)));
		for (Integer val : pageSizes) {
			res.pageSizes.add(new OptionItem(val.toString(), val.toString()));
		}

		// 子コンテナ内で入力済み判定対象に指定可能なパーツ
		Long childContainerId = ((PartsDesignRepeater)req.design).childContainerId;
		if (childContainerId != null) {
			res.targetParts = createInputedJudgeParts(childContainerId);
		}

		// 子コンテナの選択肢
		res.childContainers = repository.getMwmContainerList(corporationCode, req.design.containerId)
				.stream()
				.map(c -> new OptionItem(c.getContainerId(), c.getContainerName()))
				.collect(Collectors.toList());

		res.success = true;
		return res;
	}

	/** 入力済み判定対象パーツの選択肢を生成 */
	private List<OptionItem> createInputedJudgeParts(Long childContainerId) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final List<OptionItem> items = repository
				.findChildParts(childContainerId, inputedJudgePartsTypes, localeCode)
				.stream()
				.map(p -> new OptionItem(p.getPartsId(), p.getPartsCode() + " " + p.getLabelText()))
				.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * グリッドパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseGrid initGrid(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Vd0114ResponseGrid res = createResponse(Vd0114ResponseGrid.class, req);

		// ページ制御(ページサイズ)の選択肢
		res.pageSizes = new ArrayList<>();
		res.pageSizes.add(new OptionItem("", i18n.getText(MessageCd.noPaging)));
		for (Integer val : pageSizes) {
			res.pageSizes.add(new OptionItem(val.toString(), val.toString()));
		}

		// 子コンテナの選択肢
		res.childContainers = repository.getMwmContainerList(corporationCode, req.design.containerId)
				.stream()
				.map(c -> new OptionItem(c.getContainerId(), c.getContainerName()))
				.collect(Collectors.toList());
		// 子コンテナ内でセルに指定可能なパーツ
		Long childContainerId = ((PartsDesignGrid)req.design).childContainerId;
		if (childContainerId != null) {
			res.targetParts = createGridTargetParts(childContainerId);
		}

		// 列の幅の選択肢
		res.columnSizes = IntStream.rangeClosed(0, 12).boxed()
				.map(n -> new OptionItem(n, n == 0 ? i18n.getText(MessageCd.hiddenItem) : String.valueOf(n)))
				.collect(Collectors.toList());

		res.success = true;
		return res;
	}

	/**
	 * グリッドパーツで子コンテナを変更したことによる対象パーツ一覧の再抽出
	 * @param req
	 * @return
	 */
	public List<OptionItem> findGridChildParts(Vd0114GridRequest req) {
		return createGridTargetParts(req.childContainerId);
	}

	/**
	 * 子コンテナ内で入力済み判定対象に使用できるパーツ一覧の再抽出
	 * @param req
	 * @return
	 */
	public List<OptionItem> findInputedJudgeParts(Vd0114GridRequest req) {
		return createInputedJudgeParts(req.childContainerId);
	}

	/** コンテナ配下のパーツで、グリッドのセルに指定可能なパーツの一覧 */
	private List<OptionItem> createGridTargetParts(Long childContainerId) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<OptionItem> items = repository
				.findChildParts(childContainerId, gridTargetPartsTypes, localeCode)
				.stream()
				.map(p -> new OptionItem(p.getPartsId(), p.getPartsCode() + " " + p.getLabelText()))
				.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * ユーザ選択パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseUserSelect initUserSelect(Vd0114Request req) {
		final Vd0114ResponseUserSelect res = createResponse(Vd0114ResponseUserSelect.class, req);

		// カラム幅の選択肢(foundationのcssクラス 'small-x'や'medium-x'などの 'x'部分に相当)
		res.columnSizes = createColumnSizeList(RenderingMethod.BOOTSTRAP_GRID);
		// デフォルト値の選択肢
		res.defaultValues = lookup.getOptionItems(LookupGroupId.PARTS_USER_SELECT_DEFAULT_VALUE, true);
		// ボタンサイズ
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);

		res.success = true;
		return res;
	}

	/**
	 * 組織選択パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseOrganizationSelect initOrganizationSelect(Vd0114Request req) {
		final Vd0114ResponseOrganizationSelect res = createResponse(Vd0114ResponseOrganizationSelect.class, req);

		// カラム幅の選択肢(foundationのcssクラス 'small-x'や'medium-x'などの 'x'部分に相当)
		res.columnSizes = createColumnSizeList(RenderingMethod.BOOTSTRAP_GRID);
		// デフォルト値の選択肢
		res.defaultValues = lookup.getOptionItems(LookupGroupId.PARTS_ORGANIZATION_SELECT_DEFAULT_VALUE, true);
		// ボタンサイズ
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);

		res.success = true;
		return res;
	}

	/**
	 * Checkboxパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseCheckbox initCheckbox(Vd0114Request req) {
		final Vd0114ResponseCheckbox res = createResponse(Vd0114ResponseCheckbox.class, req);

		// デフォルト値選択肢
		res.defaultValueList = lookup.getOptionItems(LookupGroupId.DEFAULT_CHECKED_FLAG, false);

		res.success = true;
		return res;
	}

	/**
	 * Radioパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseRadio initRadio(Vd0114Request req) {
		final Vd0114ResponseRadio res = createResponse(Vd0114ResponseRadio.class, req);
		PartsDesignOption d = (PartsDesignOption)req.design;

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.options = repository.getMwmOptions(corporationCode, localeCode)
				.stream()
				.map(o -> new OptionItem(o.getOptionId(), o.getOptionName()))
				.collect(Collectors.toList());

		// デフォルト値選択肢
		if (isNotEmpty(d.optionId)) {
			res.defaultValueList = repository.getMwmOptionItems(corporationCode, d.optionId, localeCode)
					.stream()
					.map(o -> new PartsOptionItem(o.getCode(), o.getLabel()))
					.collect(Collectors.toList());
			res.defaultValueList.add(0, PartsOptionItem.EMPTY);
		}

		if (isEmpty(res.defaultValueList)) {
			res.defaultValueList = Arrays.asList(PartsOptionItem.EMPTY);
		}

		res.success = true;
		return res;
	}

	/**
	 * Dropdownパーツ用の初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseDropdown initDropdown(Vd0114Request req) {
		final Vd0114ResponseDropdown res = createResponse(Vd0114ResponseDropdown.class, req);
		PartsDesignOption d = (PartsDesignOption)req.design;

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.options = repository.getMwmOptions(corporationCode, localeCode)
				.stream()
				.map(o -> new OptionItem(o.getOptionId(), o.getOptionName()))
				.collect(Collectors.toList());

		// デフォルト値選択肢
		if (isNotEmpty(d.optionId)) {
			res.defaultValueList = repository.getMwmOptionItems(corporationCode, d.optionId, localeCode)
					.stream()
					.map(o -> new PartsOptionItem(o.getCode(), o.getLabel()))
					.collect(Collectors.toList());
			// デフォルト値なしを入力させるため、ブランク行がなければ追加する
			if (res.defaultValueList.isEmpty() || !isEmpty(res.defaultValueList.get(0).value)) {
				res.defaultValueList.add(0, PartsOptionItem.EMPTY);
			}
		}
		// 未選択行の表示の選択肢
		res.emptyLineTypes = lookup.getOptionItems(LookupGroupId.EMPTY_LINE_TYPE, true);

		if (isEmpty(res.defaultValueList)) {
			res.defaultValueList = Arrays.asList(PartsOptionItem.EMPTY);
		}

		res.success = true;
		return res;
	}

	/**
	 * 選択肢プロパティ変更時のデフォルト値変更処理
	 * @param req
	 * @return
	 */
	public Vd0114ResponseOptionChange changeOption(Vd0114Request req) {
		final Vd0114ResponseOptionChange res = createResponse(Vd0114ResponseOptionChange.class, req);
		PartsDesignOption d = (PartsDesignOption)req.design;

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// デフォルト値選択肢
		res.defaultValueList = repository.getMwmOptionItems(corporationCode, d.optionId, localeCode)
				.stream()
				.map(o -> new PartsOptionItem(o.getCode(), o.getLabel()))
				.collect(Collectors.toList());
		// デフォルト値なしを入力させるため、ブランク行がなければ追加する
		if (res.defaultValueList.isEmpty() || !isEmpty(res.defaultValueList.get(0).value)) {
			res.defaultValueList.add(0, PartsOptionItem.EMPTY);
		}

		if (isEmpty(res.defaultValueList)) {
			res.defaultValueList = Arrays.asList(PartsOptionItem.EMPTY);
		}

		res.success = true;
		return res;
	}

	/**
	 * 採番パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseNumbering initNumbering(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Vd0114ResponseNumbering res = createResponse(Vd0114ResponseNumbering.class, req);

		// 採番形式マスタ一覧
		res.partsNumberingFormats = repository.getPartsNumberingFormats(corporationCode, localeCode)
				.stream()
				.map(f -> new OptionItem(
						f.getPartsNumberingFormatId(),
						f.getPartsNumberingFormatCode() + " " + f.getPartsNumberingFormatName()))
				.collect(Collectors.toList());

		res.success = true;
		return res;
	}

	/**
	 * マスタ検索Buttonパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseButton initSearchButton(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final PartsDesignSearchButton d = (PartsDesignSearchButton)req.design;

		final Vd0114ResponseButton res = createResponse(Vd0114ResponseButton.class, req);
		res.partsSearchTypes = lookup.getOptionItems(LookupGroupId.PARTS_SEARCH_TYPE, true);
		res.tableIds = repository.getMwmTables(corporationCode, localeCode);
		if (d.tableId != null) {
			res.tableSearchIds = repository.getMwmTableSearchs(d.tableId, corporationCode, localeCode);
		}
		if (d.tableSearchId != null) {
			res.columnNamesInOut = repository.getColumnsInOrOut(d.tableSearchId);
		}
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);

		res.success = true;
		return res;
	}

	/**
	 * イベントButtonパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseButton initEventButton(Vd0114Request req) {
		final Vd0114ResponseButton res = createResponse(Vd0114ResponseButton.class, req);
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);
		res.success = true;
		return res;
	}

	/**
	 * ButtonパーツでtableIdを変更したことによる「汎用テーブル検索条件の選択肢」を抽出
	 * @param tableId
	 * @return
	 */
	public List<OptionItem> findTableSearch(Vd0114ButtonRequest req) {
		if (req.tableId == null)
			return new ArrayList<>();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		return repository.getMwmTableSearchs(req.tableId, corporationCode, localeCode);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件）
	 * @param tableId
	 * @return
	 */
	public List<OptionItem> getTableSearchColumnsOut(Vd0114ButtonRequest req) {
		if (req.tableSearchId == null)
			return new ArrayList<>();
		return repository.getColumnsOut(req.tableSearchId);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件 or 検索結果）
	 * @param tableId
	 * @return
	 */
	public List<OptionItem> getTableSearchColumnsInOrOut(Vd0114ButtonRequest req) {
		if (req.tableSearchId == null)
			return new ArrayList<>();
		return repository.getColumnsInOrOut(req.tableSearchId);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件 and 検索結果）
	 * @param tableId
	 * @return
	 */
	public List<OptionItem> getTableSearchColumnsInAndOut(Vd0114ButtonRequest req) {
		if (req.tableSearchId == null)
			return new ArrayList<>();
		return repository.getColumnsInAndOut(req.tableSearchId);
	}

	/**
	 * Masterパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseMaster initMaster(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final PartsDesignMaster d = (PartsDesignMaster)req.design;

		final Vd0114ResponseMaster res = createResponse(Vd0114ResponseMaster.class, req);
		res.partsSearchTypes = lookup.getOptionItems(LookupGroupId.PARTS_SEARCH_TYPE, true);
		res.tableIds = repository.getMwmTables(corporationCode, localeCode);
		if (d.tableId != null) {
			res.tableSearchIds = repository.getMwmTableSearchs(d.tableId, corporationCode, localeCode);
		}
		if (d.tableSearchId != null) {
			// ドロップダウンの「値」カラムの選択肢（＝検索条件かつ検索結果になれるもの）
			res.columnNamesValue = repository.getColumnsInAndOut(d.tableSearchId);
			// ドロップダウンの「ラベル」カラムの選択肢（＝検索結果になれるもの）
			res.columnNamesLabel = repository.getColumnsOut(d.tableSearchId);
			// 他パーツにマッピングできるカラムの選択肢（＝検索条件または検索結果になれるもの）
			res.columnNamesInOut = repository.getColumnsInOrOut(d.tableSearchId);
		}
		res.emptyLineTypes = lookup.getOptionItems(LookupGroupId.EMPTY_LINE_TYPE, true);
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);

		res.success = true;
		return res;
	}

	/**
	 * 添付ファイルのアップロード
	 * @param partsId アップロード内容を登録する対象パーツID
	 * @param fileRegExp ファイル名の制限用の正規表現
	 * @param multiPart マルチパートデータ
	 */
	@Transactional
	public Vd0114ResponsePartsDesignAttachFile uploadAttachFile(Long partsId, String fileRegExp, FormDataMultiPart multiPart) {
		List<PartsAttachFileEntity> files = new ArrayList<>();
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			FormDataContentDisposition cd = (FormDataContentDisposition)bodyPart.getContentDisposition();
			if (eq("file", cd.getName())) {
				final UploadFile src = new UploadFile(bodyPart);
				// ファイル名の制限用の正規表現
				if (isNotEmpty(fileRegExp)) {
					final Pattern p = Pattern.compile(fileRegExp, Pattern.CASE_INSENSITIVE);
					if (!p.matcher(src.fileName).find())
						throw new InvalidUserInputException(MessageCd.MSG0230, src.fileName);
				}

				final PartsAttachFileEntity f = new PartsAttachFileEntity();
				f.fileName = src.fileName;
				f.groupKey = null;
				f.partsAttachFileId = partsAttachFileService.save(src, partsId);
				f.comments = null;
				files.add(0, f);
			}
		}

		final Vd0114ResponsePartsDesignAttachFile res = createResponse(Vd0114ResponsePartsDesignAttachFile.class, null);
		res.files = files;
		res.success = true;
		return res;
	}

	/**
	 * 独立画面パーツの初期化
	 * @param req
	 * @return
	 */
	public Vd0114ResponseStandAlone initStandAloneScreen(Vd0114Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Vd0114ResponseStandAlone res = createResponse(Vd0114ResponseStandAlone.class, req);
		// ボタンサイズの選択肢
		res.buttonSizes = lookup.getOptionItems(LookupGroupId.PARTS_BUTTON_SIZE, false);
		// 子コンテナの選択肢
		res.childContainers = repository.getMwmContainerList(corporationCode, req.design.containerId)
				.stream()
				.map(c -> new OptionItem(c.getContainerId(), c.getContainerName()))
				.collect(Collectors.toList());

		res.success = true;
		return res;
	}
}
