package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.component.tableSearch.TableSearchRepository;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsInputType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.parts.design.PartsDesignCheckbox;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignHyperlink;
import jp.co.nci.iwf.designer.parts.design.PartsDesignLabel;
import jp.co.nci.iwf.designer.parts.design.PartsDesignNumbering;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOption;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOrganizationSelect;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStamp;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.design.PartsDesignUserSelect;
import jp.co.nci.iwf.designer.service.ContainerLoadService;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Dc;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020PartsDc;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Repository;
import jp.co.nci.iwf.endpoint.mm.mm0051.Mm0051Repository;
import jp.co.nci.iwf.endpoint.vd.vd0010.Vd0010Entity;
import jp.co.nci.iwf.endpoint.vd.vd0010.Vd0010Repository;
import jp.co.nci.iwf.endpoint.vd.vd0062.Vd0062Repository;
import jp.co.nci.iwf.endpoint.vd.vd0114.Vd0114Service;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * コンテナ一覧のEXCELダウンロード用StreamingOutput
 */
public class Vd0010OutputStreamExcel extends PoiUtils implements StreamingOutput {
	/** ダウンロード通知サービス */
	private DownloadNotifyService notify;
	/** パーツロードサービス */
	private ContainerLoadService partsLoadService;
	/** パーツ属性画面サービス */
	private Vd0114Service vd0114service;
	/** 対象コンテナID */
	private long containerId;
	/** ルックアップサービス */
	private MwmLookupService lookup;
	/** 表示条件設定画面リポジトリ */
	private Mm0020Repository repository;
	/** 国際化対応の文字リソースサービス */
	private I18nService i18n;
	/** 入力タイプMap */
	private Map<String, String> inputTypes;
	/** 入力チェックタイプ（文字） */
	private Map<String, String> validateTypeString;
	/** 入力チェックタイプ（数値） */
	private Map<String, String> validateTypeNumbers;
	/** 桁数タイプ */
	private Map<String, String> lengthTypes;
	/** 数値フォーマット */
	private Map<String, String> numberFormats;
	/** コンテナ一覧リポジトリ */
	private Vd0010Repository vd0010repository;
	/** 採番形式設定リポジトリ */
	private Mm0051Repository mm0051;
	/** 選択肢設定リポジトリ */
	private Vd0062Repository vd0062;
	/** 汎用テーブルのリポジトリ */
	private TableSearchRepository tsRepository;

	/**
	 * コンストラクタ
	 */
	public Vd0010OutputStreamExcel(long containerId) {
		this.containerId = containerId;
		this.notify = CDI.current().select(DownloadNotifyService.class).get();
		this.partsLoadService = CDI.current().select(ContainerLoadService.class).get();
		this.vd0114service = CDI.current().select(Vd0114Service.class).get();
		this.lookup = CDI.current().select(MwmLookupService.class).get();
		this.repository = CDI.current().select(Mm0020Repository.class).get();
		this.i18n = CDI.current().select(I18nService.class).get();
		this.vd0010repository = CDI.current().select(Vd0010Repository.class).get();
		this.mm0051 = CDI.current().select(Mm0051Repository.class).get();
		this.vd0062 = CDI.current().select(Vd0062Repository.class).get();
		this.tsRepository = CDI.current().select(TableSearchRepository.class).get();

		final MwmLookupService lookup = CDI.current().select(MwmLookupService.class).get();
		this.inputTypes = lookup.getNameMap(LookupGroupId.PARTS_INPUT_TYPE);
		this.validateTypeNumbers = lookup.getNameMap(LookupGroupId.PARTS_VALIDATE_TYPE_NUMBER);
		this.validateTypeString = lookup.getNameMap(LookupGroupId.PARTS_VALIDATE_TYPE_STRING);
		this.lengthTypes = lookup.getNameMap(LookupGroupId.LENGTH_TYPE);
		this.numberFormats = lookup.getNameMap(LookupGroupId.NUMBER_FORMAT);
	}

	/**
	 * コンテンツ出力
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロードモニターへ開始を通知
		notify.begin();

		// テンプレートを作業ファイルとしてコピー
		final ClassPathResource cpr = new ClassPathResource("excel/vd0010.xlsx");
		final File template = cpr.copyFile();
		try {
			// コンテナ定義をロード
			final ViewWidth viewWidth = ViewWidth.XS;	// ダミー値なので何でもいい
			final DesignerContext ctx = DesignerContext.designInstance(viewWidth);
			partsLoadService.loadRootDesign(containerId, ctx);

			// 空EXCELテンプレートへシート単位でコンテナ情報を書き込む
			try (Workbook wb = WorkbookFactory.create(template)) {
				final Map<Integer, CellStyle> styles = new HashMap<>();
				writeSheetContainer(wb, ctx, styles);
				writeSheetColumn(wb, ctx, styles);
				writeSheetDc(wb, ctx, styles);
				writeSheetEc(wb, ctx, styles);
				writeSheetCalc(wb, ctx, styles);
				writeSheetEvent(wb, ctx, styles);
				wb.setActiveSheet(0);
				wb.write(output);
			}
			catch (EncryptedDocumentException | InvalidFormatException e) {
				throw new WebApplicationException(e);
			}
		}
		finally {
			// ダウンロードモニターへ終了を通知
			notify.end();

			// 作業ファイルは不要になったので削除
			if (template.exists())
				template.delete();
		}
	}

	/** イベントシートの書き込み */
	private void writeSheetEvent(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("イベント");

		final Vd0010EventSheet bean = new Vd0010EventSheet();
		bean.screenCode = ctx.screenCode;
		bean.screenName = ctx.screenName;
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.screenCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.screenName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		// コンテナ直下のパーツでイベントのあるものだけを抜き出す
		final List<PartsDesign> designs = ctx.root.childPartsIds.stream()
				.map(id -> ctx.designMap.get(id))
				.filter(d -> d.events != null && !d.events.isEmpty())
				.sorted((d1, d2) -> compareTo(d1.sortOrder, d2.sortOrder))
				.collect(Collectors.toList());

		int r = 7;
		for (PartsDesign d : designs) {
			for (PartsEvent ev : d.events) {
				if (isEmpty(ev.functionName))
					continue;

				// 次行を生成＆現行行の書式をコピー
				final Row row = getOrCreateRow(sheet, r);
				final Row nextRow = getOrCreateRow(sheet, ++r);
				copyRow(row, nextRow, styles, false);

				int c = 1;
				setCellValue(row, c++, d.designCode);
				setCellValue(row, c++, d.labelText);
				setCellValue(row, c++, ev.eventName);
				setCellValue(row, c++, ev.functionName);
				setCellValue(row, c++, ev.functionParameter);
			}
		}
	}

	/** カラム定義シートの書き込み */
	private void writeSheetColumn(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("カラム定義");
		final Vd0010ColumnSheet bean = new Vd0010ColumnSheet();
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.tableName = ctx.root.tableName;
		bean.outputDate = now();

		// DBカラム
		bean.columnList = new ArrayList<>();
		for (Long partsId : ctx.root.childPartsIds) {
			PartsDesign design = ctx.designMap.get(partsId);
			for (PartsColumn pc : design.columns) {
				bean.columnList.add(new Vd0010Column(design, pc));
			}
		}
		bean.columnList.sort((c1, c2) -> compareTo(c1.sortOrder, c2.sortOrder));

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.containerCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.containerName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.tableName);

		int r = 7;
		for (Vd0010Column col : bean.columnList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, col.partsCode);
			setCellValue(row, c++, col.labelText);
			setCellValue(row, c++, col.columnName);
			setCellValue(row, c++, col.columnRole);
			setCellValue(row, c++, col.columnType);
			setCellValue(row, c++, col.sortOrder);
		}
	}

	/** 有効条件シートの書き込み */
	private void writeSheetEc(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("条件設定");

		final Vd0010EcSheet bean = new Vd0010EcSheet();
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();
		bean.partsList = ctx.root.childPartsIds.stream()
				.map(id -> ctx.designMap.get(id))
				.filter(p -> p.partsConds != null && !p.partsConds.isEmpty())
				.sorted((p1, p2) -> compareTo(p1.sortOrder, p2.sortOrder))
				.flatMap(p -> toCondRow(p, ctx.designMap))
				.collect(Collectors.toList());

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.containerCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.containerName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		// 本文
		int r = 7;
		for (Vd0010CondParts parts : bean.partsList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, parts.partsCode);
			setCellValue(row, c++, parts.labelText);
			setCellValue(row, c++, parts.partsConditionTypeName);
			setCellValue(row, c++, parts.expression);
			setCellValue(row, c++, parts.callbackFunction);
		}
	}

	/** 条件式の行データに変換 */
	private Stream<Vd0010CondParts> toCondRow(PartsDesign design, Map<Long, PartsDesign> designMap) {
		// 論理演算子一覧
		final Map<String, String> logiclOperators = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);
		// 比較演算子一覧
		final Map<String, String> comparisonOperators = lookup.getNameMap(LookupGroupId.COMPARISON_OPERATOR);

		return design.partsConds.stream().map(pc -> new Vd0010CondParts(
				design, pc, designMap, logiclOperators, parentheses, comparisonOperators));
	}

	/** 計算式シートの書き込み */
	private void writeSheetCalc(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("計算式");
		final Vd0010CalcSheet bean = new Vd0010CalcSheet();
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();
		bean.partsList = ctx.root.childPartsIds.stream()
				.map(id -> ctx.designMap.get(id))
				.filter(p -> p.partsCalcs != null && !p.partsCalcs.isEmpty())
				.flatMap(p -> toCalcRow(p, ctx.designMap))
				.collect(Collectors.toList());
		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.containerCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.containerName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		// 本文
		int r = 7;
		for (Vd0010CalcParts calc : bean.partsList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, calc.partsCode);
			setCellValue(row, c++, calc.labelText);
			setCellValue(row, c++, calc.calcName);
			setCellValue(row, c++, calc.calcExpression);
			setCellValue(row, c++, calc.callbackFunction);
			setCellValue(row, c++, calc.sortOrder);
			setCellValue(row, c++, calc.defaultFlag);
			setCellValue(row, c++, calc.ecExpression);
		}
	}

	/** 表示条件シートの書き込み */
	private void writeSheetDc(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("表示条件設定");
		final String localeCode = LoginInfo.get().getLocaleCode();

		// 表示条件マスタ.表示区分
		final Map<String, String> dcTypes = lookup.getNameMap(LookupGroupId.DC_TYPE);

		// パーツID/表示条件IDをキーとしたMapへ変換
		final List<Mm0020PartsDc> partsDcList = repository.getPartsDcList(ctx.root.containerId, localeCode);
		final Map<Long, Map<Long, Mm0020PartsDc>> partsDcMap = partsDcList.stream().collect(
				Collectors.groupingBy(Mm0020PartsDc::getPartsId,
				Collectors.toMap(Mm0020PartsDc::getDcId, pdc -> pdc)));

		// パーツ表示条件で定義済みの表示条件IDリスト
		final Set<Long> dcIds = partsDcList.stream()
				.map(pd -> pd.getDcId())
				.collect(Collectors.toSet());

		// 表示条件マスタ
		final List<Mm0020Dc> dcList = repository.getDcList(LoginInfo.get().getLocaleCode())
				.stream()
				.filter(dc -> dcIds.contains(dc.getDcId()))
				.collect(Collectors.toList());
		final Map<Long, Mm0020Dc> dcMap = dcList.stream()
				.filter(dc -> dcIds.contains(dc.getDcId()))
				.collect(Collectors.toMap(Mm0020Dc::getDcId, dc -> dc));

		final Vd0010DcSheet bean = new Vd0010DcSheet();
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();
		bean.dcList = dcList;
		bean.partsList = repository.getPartsList(ctx.root.containerId, localeCode)
			.stream()
			.map(parts -> new Vd0010DcParts(parts, dcMap, partsDcMap, dcTypes))
			.collect(Collectors.toList());

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.containerCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.containerName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		if (!bean.partsList.isEmpty()) {
			// ヘッダ
			String[] dcNames = asArray(bean.partsList.get(0).maps.keySet());
			for (int r = 6; r <= 7; r++) {
				Row row = getOrCreateRow(sheet, r);
				int start = 3, c = start, lastC = start + dcNames.length;
				for (String dcName : dcNames) {
					Cell cell = getOrCreateCell(row, c++);
					Cell nextCell = getOrCreateCell(row, c);
					if (c < lastC)
						nextCell.setCellStyle(cell.getCellStyle());
					if (r == 6)
						cell.setCellValue(dcName);
				}
			}

			// 本文
			int r = 7;
			for (Vd0010DcParts parts : bean.partsList) {
				// 次行を生成＆現行行の書式をコピー
				final Row row = getOrCreateRow(sheet, r);
				final Row nextRow = getOrCreateRow(sheet, ++r);
				copyRow(row, nextRow, styles, false);

				int c = 1, lastC = 3 + dcNames.length;
				setCellValue(row, c++, parts.partsCode);
				setCellValue(row, c++, parts.labelText);
				for (String dcName : dcNames) {
					Cell cell = getOrCreateCell(row, c++);
					Cell nextCell = getOrCreateCell(row, c);
					if (c < lastC)
						nextCell.setCellStyle(cell.getCellStyle());
					String dc = parts.maps.get(dcName);
					cell.setCellValue(dc);
				}
			}
		}
	}

	/** コンテナ定義シートの書き込み */
	private void writeSheetContainer(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("コンテナ定義");

		// 業務管理項目名称マスタ
		Map<String, String> businessInfos = vd0114service.createBusinessInfoCodes()
				.stream()
				.collect(Collectors.toMap(
						item -> item.getValue(), item -> item.getLabel()));

		// フォントサイズ
		Map<String, String> fontSizes = vd0114service.createFontSizeList()
				.stream()
				.collect(Collectors.toMap(
						item -> item.getValue(), item -> item.getLabel()));

		// レンダリング方法
		Map<String, String> renderMethods = lookup.getOptionItems(LookupGroupId.RENDERING_METHOD, false)
				.stream()
				.collect(Collectors.toMap(
						item -> item.getValue(), item -> item.getLabel()));

		// コンテナ情報から出力データをかき集めてアノテーションされたBeanへ流し込む
		final Vd0010ContainerSheet bean = new Vd0010ContainerSheet();
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.tableName = ctx.root.tableName;
		bean.outputDate = now();
		bean.partsList = ctx.root.childPartsIds.stream()
				.map(partsId -> ctx.designMap.get(partsId))
				.map(design -> toRow(design, businessInfos, fontSizes, renderMethods, ctx.designMap))
				.collect(Collectors.toList());

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.containerCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.containerName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);
		// パーツ部
		int r = 7;
		for (Vd0010ContainerRow parts : bean.partsList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, parts.partsCode);
			setCellValue(row, c++, parts.labelText);
			setCellValue(row, c++, parts.partsTypeName);
			setCellValue(row, c++, parts.description);
			setCellValue(row, c++, parts.copyTargetFlag);
			setCellValue(row, c++, parts.grantTabIndexFlag);
			setCellValue(row, c++, parts.requiredFlag);
			setCellValue(row, c++, parts.sortOrder);
			setCellValue(row, c++, parts.businessInfo);
			setCellValue(row, c++, parts.defaultValue);
			setCellValue(row, c++, parts.extInfo);
			setCellValue(row, c++, parts.renderingMethodName);
			setCellValue(row, c++, parts.colLg);
			setCellValue(row, c++, parts.colMd);
			setCellValue(row, c++, parts.colSm);
			setCellValue(row, c++, parts.mobileInvisibleFlag);
			setCellValue(row, c++, parts.cssClass);
			setCellValue(row, c++, parts.cssStyle);
			setCellValue(row, c++, parts.fontSizeName);
		}
	}

	/** 計算式の行データに変換 */
	private Stream<Vd0010CalcParts> toCalcRow(PartsDesign design, Map<Long, PartsDesign> designMap) {
		// 論理演算子一覧
		final Map<String, String> logiclOperators = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);
		// 比較演算子一覧
		final Map<String, String> comparisonOperators = lookup.getNameMap(LookupGroupId.COMPARISON_OPERATOR);
		// 算術演算子一覧
		final Map<String, String> arithmeticOperators = lookup.getNameMap(LookupGroupId.ARITHMETIC_OPERATOR);

		return design.partsCalcs.stream().map(pc -> new Vd0010CalcParts(
				design, pc, designMap, logiclOperators, parentheses, comparisonOperators, arithmeticOperators));
	}

	private Vd0010ContainerRow toRow(
			PartsDesign design, Map<String, String> businessInfos,
			Map<String, String> fontSizes, Map<String, String> renderingMethods, Map<Long, PartsDesign> designMap) {

		final Vd0010ContainerRow row = new Vd0010ContainerRow();
		MiscUtils.copyFields(design, row);

		// パーツ種別名
		row.partsTypeName = PartsUtils.getPartsTypeName(design.partsType);
		// 業務管理項目
		row.businessInfo = businessInfos.get(design.businessInfoCode);
		// フォントサイズ
		row.fontSizeName = fontSizes.get(String.valueOf(design.fontSize));
		// レンダリング方法
		row.renderingMethodName = renderingMethods.get(String.valueOf(design.renderingMethod));
		// 拡張情報
		if (design instanceof PartsDesignTextbox)
			// テキストボックス
			row.extInfo = toExtInfo((PartsDesignTextbox)design);
		if (design instanceof PartsDesignLabel) {
			// ラベル
			row.extInfo = toExtInfo((PartsDesignLabel)design, designMap);
		}
		if (design instanceof PartsDesignNumbering)
			// 採番パーツ
			row.extInfo = toExtInfo((PartsDesignNumbering)design);
		if (design instanceof PartsDesignCheckbox)
			// チェックボックス
			row.extInfo = toExtInfo((PartsDesignCheckbox)design);
		if (design instanceof PartsDesignOption)
			// ラジオ／ドロップダウンリスト
			row.extInfo = toExtInfo((PartsDesignOption)design);
		if (design instanceof PartsDesignAjax)
			// マスタ検索/検索ボタン
			row.extInfo = toExtInfo((PartsDesignAjax)design, designMap);
		if (design instanceof PartsDesignChildHolder)
			// 子要素ありのコンテナ
			row.extInfo = toExtInfo((PartsDesignChildHolder)design);
		if (design instanceof PartsDesignHyperlink)
			// ハイパーリンク
			row.extInfo = toExtInfo((PartsDesignHyperlink)design);
		if (design instanceof PartsDesignStamp)
			// スタンプ
			row.extInfo = toExtInfo((PartsDesignStamp)design);
		if (design instanceof PartsDesignAttachFile)
			// 添付ファイル
			row.extInfo = toExtInfo((PartsDesignAttachFile)design);
		if (design instanceof PartsDesignUserSelect)
			// ユーザ選択
			row.extInfo = toExtInfo((PartsDesignUserSelect)design);
		if (design instanceof PartsDesignOrganizationSelect)
			// 組織選択
			row.extInfo = toExtInfo((PartsDesignOrganizationSelect)design);
		return row;
	}

	/** 組織選択の拡張情報 */
	private String toExtInfo(PartsDesignOrganizationSelect design) {
		List<String> fields = new ArrayList<>();
		if (design.organizationNameDisplay)
			fields.add(i18n.getText(MessageCd.organizationName));
		if (design.organizationAddedInfoDisplay)
			fields.add(i18n.getText(MessageCd.organizationAddedInfo));
		if (design.organizationNameAbbrDisplay)
			fields.add(i18n.getText(MessageCd.organizationNameAbbr));
		if (design.telNumDisplay)
			fields.add(i18n.getText(MessageCd.telNum));
		if (design.faxNumDisplay)
			fields.add(i18n.getText(MessageCd.faxNum));
		if (design.faxNumDisplay)
			fields.add(i18n.getText(MessageCd.faxNum));
		if (design.addressDisplay)
			fields.add(i18n.getText(MessageCd.address));

		List<String> params = new ArrayList<>();
		if (!fields.isEmpty()) {
			params.add(i18n.getText(MessageCd.dispColumn));
			params.add("[ " + String.join(", ", fields) + " ]");
		}
		return toFormattedValue(params);
	}

	/** ユーザ選択の拡張情報 */
	private String toExtInfo(PartsDesignUserSelect design) {
		List<String> fields = new ArrayList<>();
		if (design.userNameDisplay)
			fields.add(i18n.getText(MessageCd.userName));
		if (design.userAddedInfoDisplay)
			fields.add(i18n.getText(MessageCd.userAddedInfo));
		if (design.userNameAbbrDisplay)
			fields.add(i18n.getText(MessageCd.userNameAbbr));
		if (design.telNumDisplay)
			fields.add(i18n.getText(MessageCd.telNum));
		if (design.telNumCelDisplay)
			fields.add(i18n.getText(MessageCd.telNumCel));
		if (design.mailAddressDisplay)
			fields.add(i18n.getText(MessageCd.mailAddress));

		List<String> params = new ArrayList<>();
		if (!fields.isEmpty()) {
			params.add(i18n.getText(MessageCd.dispColumn));
			params.add("[ " + String.join(", ", fields) + " ]");
		}
		return toFormattedValue(params);
	}

	/** 添付ファイルの拡張情報 */
	private String toExtInfo(PartsDesignAttachFile design) {
		List<Object> params = new ArrayList<>();
		// 複数添付ファイルを扱う
		params.add(i18n.getText(MessageCd.useMultipleAttachFiles));
		params.add(design.multiple);
		// 必須ファイル数
		params.add(i18n.getText(MessageCd.requiredFileCount));
		params.add(design.requiredFileCount);
		// 最大ファイル数
		params.add(i18n.getText(MessageCd.maxFileCount));
		params.add(design.maxFileCount);
		// ページサイズ
		params.add(i18n.getText(MessageCd.pageSize));
		params.add(design.pageSize);

		// ファイル名の制限 ----------------------------------
		// 制限なし
		if (design.enableAny) {
			params.add(i18n.getText(MessageCd.fileLimitation) + ":" + i18n.getText(MessageCd.noLimitation));
			params.add(design.enableAny);
		}
		// 画像ファイルを許可
		if (design.enableImage) {
			params.add(i18n.getText(MessageCd.allowImage));
			params.add(design.enableImage);
		}
		// PDFを許可
		if (design.enablePdf) {
			params.add(i18n.getText(MessageCd.allowPdf));
			params.add(design.enablePdf);
		}
		// WORD文書を許可
		if (design.enableWord) {
			params.add(i18n.getText(MessageCd.allowWord));
			params.add(design.enableWord);
		}
		// EXCEL文書を許可
		if (design.enableExcel) {
			params.add(i18n.getText(MessageCd.allowExcel));
			params.add(design.enableExcel);
		}
		// PowerPoint文書を許可
		if (design.enablePowerPoint) {
			params.add(i18n.getText(MessageCd.allowPowerPoint));
			params.add(design.enablePowerPoint);
		}
		// CSVファイルを許可
		if (design.enableCsv) {
			params.add(i18n.getText(MessageCd.allowCsv));
			params.add(design.enableCsv);
		}
		// テキストファイルを許可
		if (design.enableText) {
			params.add(i18n.getText(MessageCd.allowText));
			params.add(design.enableText);
		}
		/** HTMLを許可 */
		if (design.enableHtml) {
			params.add(i18n.getText(MessageCd.allowHtml));
			params.add(design.enableHtml);
		}
		/** 「その他」を許可 */
		if (design.enableOther) {
			params.add(i18n.getText(MessageCd.allowOthersWithRegExp));
			params.add(design.enableOther);
			/** 「その他」のファイル拡張子の正規表現 */
			params.add(i18n.getText(MessageCd.allowOthersWithRegExp));
			params.add(design.enableOther + " " + design.regExpOther);
		}
		return toFormattedValue(params);
	}

	/** スタンプの拡張情報 */
	private String toExtInfo(PartsDesignStamp design) {
		List<Object> params = new ArrayList<>();
		// スタンプコード
		params.add(i18n.getText(MessageCd.stampCode));
		params.add(design.stampCode);
		// スタンプサイズ
		params.add(i18n.getText(MessageCd.stampSize));
		params.add(design.stampSize);

		return toFormattedValue(params);
	}

	/** ハイパーリンクの拡張情報 */
	private String toExtInfo(PartsDesignHyperlink design) {
		List<Object> params = new ArrayList<>();
		if (isNotEmpty(design.url)) {
			params.add(i18n.getText(MessageCd.linkUrl));
			params.add(design.url);
		}
		return toFormattedValue(params);
	}

	/** マスタ検索/ボタンの拡張情報 */
	private String toExtInfo(PartsDesignAjax design, Map<Long, PartsDesign> designMap) {
		final String localeCode = LoginInfo.get().getLocaleCode();
		List<Object> params = new ArrayList<>();
		if (design.tableId != null) {
			// 汎用マスタのテーブル名
			MwmTableEx t = tsRepository.getMwmTable(design.tableId);
			if (t != null) {
				params.add(i18n.getText(MessageCd.tableName));
				params.add(t.logicalTableName);
			}
			// 汎用マスタの検索条件
			if (design.tableSearchId != null) {
				MwmTableSearchEx ts = tsRepository.getMwmTableSearch(design.tableSearchId, localeCode);
				if (ts != null) {
					params.add(i18n.getText(MessageCd.searchCondition));
					params.add(ts.tableSearchName);
				}
			}
			// 検索条件と検索結果の配布先
			List<String> toList = new ArrayList<>();
			for (PartsRelation pr : design.relations) {
				String colName = pr.columnName;
				String arrow = eq(PartsIoType.IN, pr.partsIoType) ? "←"
						: eq(PartsIoType.OUT, pr.partsIoType) ? "→" : "⇔";
				PartsDesign target = designMap.get(pr.targetPartsId);
				if (target != null) {
				String partsCode = target.partsCode;
				toList.add(colName + arrow +  partsCode);
			}
			}
			if (!toList.isEmpty()) {
				params.add(i18n.getText(MessageCd.searchConditionAndResult));
				params.add("[ " + String.join(", ", toList) + " ]");
			}
		}
		return toFormattedValue(params);
	}

	/** ラジオ／ドロップダウンの拡張情報 */
	private String toExtInfo(PartsDesignOption design) {
		List<Object> params = new ArrayList<>();
		if (design.optionId != null) {
			MwmOption opt = vd0062.get(design.optionId);
			if (opt != null) {
				// 選択肢設定
				params.add(i18n.getText(MessageCd.optionName));
				params.add(opt.getOptionName());
				// 	デフォルト値
				if (isNotEmpty(design.defaultValue)) {
					MwmOptionItem item = vd0062.getMwmOptionItem(design.optionId, design.defaultValue);
					if (item != null) {
						params.add(i18n.getText(MessageCd.defaultValue));
						params.add(item.getLabel());
					}
				}
			}
		}
		return toFormattedValue(params);
	}

	/** チェックボックスの拡張情報 */
	private String toExtInfo(PartsDesignCheckbox design) {
		List<Object> params = new ArrayList<>();
		// チェックありの時の値
		params.add(i18n.getText(MessageCd.checkedValue));
		params.add(design.checkedValue);
		// チェックなしの時の値
		params.add(i18n.getText(MessageCd.uncheckedValue));
		params.add(design.uncheckedValue);
		// デフォルト値
		params.add(i18n.getText(MessageCd.defaultValue));
		params.add(eq(CommonFlag.ON, design.defaultValue)
				? i18n.getText(MessageCd.checkedValue)
				: i18n.getText(MessageCd.uncheckedValue));
		return toFormattedValue(params);
	}

	/** 採番パーツの拡張情報 */
	private String toExtInfo(PartsDesignNumbering design) {
		List<Object> params = new ArrayList<>();
		// パーツ採番形式
		if (design.partsNumberingFormatId != null) {
			MwmPartsNumberingFormat f = mm0051.getMwmPartsNumberingFormat(
					design.partsNumberingFormatId);
			if (f != null) {
				params.add(i18n.getText(MessageCd.numberingFormat));
				params.add(f.getPartsNumberingFormatName());
			}
		}
		// (アクティビティと連動した)採番コード
		if (isNotEmpty(design.numberingCode)) {
			params.add(i18n.getText(MessageCd.numberingCode));
			params.add(design.numberingCode);
		}
		// 申請/承認時のみ発番する
		if (design.fireIfNormalAction) {
			params.add(i18n.getText(MessageCd.fireIfNormalAction));
			params.add(design.fireIfNormalAction);
		}

		return toFormattedValue(params);
	}

	/** 子要素ありのコンテナの拡張情報 */
	private String toExtInfo(PartsDesignChildHolder design) {
		final List<Object> params = new ArrayList<>(64);
		// 子コンテナ
		if (design.containerId != null) {
			Vd0010Entity entity = vd0010repository.get(design.containerId);
			if (entity != null) {
				params.add(i18n.getText(MessageCd.childContainer));
				params.add(entity.containerName);
			}
		}
		// ページ制御
		if (design.pageSize != null) {
			params.add(i18n.getText(MessageCd.pageSize));
			params.add(design.pageSize);
		}
		// 初期行数
		if (design.initRowCount != null) {
			params.add(i18n.getText(MessageCd.initRowCount));
			params.add(design.initRowCount);
		}
		// 最小行数
		if (design.minRowCount != null) {
			params.add(i18n.getText(MessageCd.minRowCount));
			params.add(design.minRowCount);
		}
		// ボタンを上部に配置
		if (design.showButtonTopFlag) {
			params.add(i18n.getText(MessageCd.showButtonOnTop));
			params.add(design.showButtonTopFlag);
		}
		// 空行追加ボタン
		if (design.showAddEmptyButtonFlag) {
			params.add(i18n.getText(MessageCd.btnAddEmpty));
			params.add(design.showAddEmptyButtonFlag);
		}
		// 削除ボタン
		if (design.showDeleteButtonFlag) {
			params.add(i18n.getText(MessageCd.btnDelete));
			params.add(design.showDeleteButtonFlag);
		}
		// コピーボタン
		if (design.showCopyButtonFlag) {
			params.add(i18n.getText(MessageCd.btnCopy));
			params.add(design.showCopyButtonFlag);
		}
		// 行数変更
		if (design.showLineCountButtonFlag) {
			params.add(i18n.getText(MessageCd.btnChangeLineCount));
			params.add(design.showLineCountButtonFlag);
		}

		return toFormattedValue(params);
	}

	/** Textboxパーツの拡張情報 */
	private String toExtInfo(PartsDesignTextbox design) {
		final List<Object> params = new ArrayList<>(64);
		// 入力タイプ
		params.add(i18n.getText(MessageCd.inputType));
		params.add(inputTypes.get(toStr(design.inputType)));
		// 入力チェックタイプ
		params.add(i18n.getText(MessageCd.validateType));
		if (validateTypeString.containsKey(design.validateType))
			params.add(validateTypeString.get(toStr(design.validateType)));
		else if (validateTypeNumbers.containsKey(design.validateType))
			params.add(validateTypeNumbers.get(toStr(design.validateType)));
		// 桁数タイプ
		if (design.lengthType != 0) {
			params.add(i18n.getText(MessageCd.lengthType));
			params.add(lengthTypes.get(toStr(design.lengthType)));
		}
		// 最大文字数／バイト数
		if (design.maxLength != null) {
			params.add(i18n.getText(MessageCd.maxLengthCharByte));
			params.add(design.maxLength);
		}
		// 最少文字数／バイト数
		if (design.minLength != null) {
			params.add(i18n.getText(MessageCd.minLengthCharByte));
			params.add(design.minLength);
		}
		// デフォルト値
		if (isNotEmpty(design.defaultValue)) {
			params.add(i18n.getText(MessageCd.defaultValue));
			params.add(design.defaultValue);
		}
		// 接頭語
		if (isNotEmpty(design.prefix)) {
			params.add(i18n.getText(MessageCd.prefix));
			params.add(design.prefix);
		}
		// 末尾後
		if (isNotEmpty(design.suffix)) {
			params.add(i18n.getText(MessageCd.suffix));
			params.add(design.suffix);
		}
		// 読取専用
		if (design.readonly) {
			params.add(i18n.getText(MessageCd.readonly));
			params.add(design.readonly);
		}
		// HIDDENとしてレンダリングするか
		if (design.renderAsHidden) {
			params.add(i18n.getText(MessageCd.renderAsHidden));
			params.add(design.renderAsHidden);
		}
		// 数値用のプロパティ群
		if (PartsInputType.NUMBER == design.inputType) {
			// 数値フォーマット
			if (design.numberFormat != null) {
				params.add(i18n.getText(MessageCd.numberFormat));
				params.add(numberFormats.get(toStr(design.numberFormat)));
			}
			// 端数処理タイプ
			if (design.roundType != null) {
				params.add(i18n.getText(MessageCd.roundType));
				params.add(design.roundType);
			}
			// 小数点桁数
			if (design.decimalPlaces != null) {
				params.add(i18n.getText(MessageCd.decimalPlaces));
				params.add(design.decimalPlaces);
			}
			// 最大値
			if (design.max != null) {
				params.add(i18n.getText(MessageCd.max));
				params.add(design.max);
			}
			// 最小値
			if (design.min != null) {
				params.add(i18n.getText(MessageCd.min));
				params.add(design.min);
			}
			// マイナスだと赤字
			if (design.redIfNegative) {
				params.add(i18n.getText(MessageCd.redIfNegative));
				params.add(design.redIfNegative);
			}
			// 数値を1/100で保存する
			if (design.saveAsPercent) {
				params.add(i18n.getText(MessageCd.saveAsPercent));
				params.add(design.saveAsPercent);
			}
		}

		return toFormattedValue(params);
	}

	/** Labelパーツの拡張情報 */
	private String toExtInfo(PartsDesignLabel design, Map<Long, PartsDesign> designMap) {
		List<String> params = new ArrayList<>();
		for (PartsRelation pr : design.relations) {
			PartsDesign target = designMap.get(pr.targetPartsId);
			if (target != null) {
			params.add(i18n.getText(MessageCd.associatedParts));
			params.add(target.partsCode + " " + target.labelText);
			}
		}
		return toFormattedValue(params);
	}

	/** パラメータを「キー=値」の形式で文字連結 */
	private String toFormattedValue(List<?> params) {
		StringBuilder sb = new StringBuilder();
		int size = params.size();
		for (int i = 0; i < size; i++) {
			final Object p = params.get(i);
			if (p != null)
				sb.append(toStr(p));

			if (i % 2 == 0)
				sb.append("=");		// 偶数ならフィールド名なので、末尾に "="を追記
			else if (i != (size - 1))
				sb.append(", ");	// カンマ区切り
		}
		return sb.toString();
	}
}
