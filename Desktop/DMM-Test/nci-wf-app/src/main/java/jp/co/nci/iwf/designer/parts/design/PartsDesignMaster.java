package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.EmptyLineType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleMasterParts;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.service.tableInfo.ColumnMetaData;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;

/**
 * 【デザイン時】汎用マスタ選択パーツ。
 * V5に存在していた「ボタン押下で汎用テーブル検索ポップアップを開く機能」はボタンパーツへ移管された。
 * V6では動的に選択肢を変えるドロップダウンリストである。
 */
public class PartsDesignMaster extends PartsDesignAjax implements RoleMasterParts, PartsEvents  {
	/** 空行区分 */
	public int emptyLineType;
	/** カラム名：値 */
	public String columnNameValue;
	/** カラム名：ラベル */
	public String columnNameLabel;
	/** 読取専用か */
	public boolean readonly;

	/** カラム「コード」のデフォルト桁数 */
	private static final int DEFAULT_CODE_LENGTH = 30;
	/** カラム「ラベル」のデフォルト桁数 */
	private static final int DEFAULT_LABEL_LENGTH = 90;

	/** Textbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"emptyLineType",
			"columnNameValue",
			"columnNameLabel",
			"readonly",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		emptyLineType = EmptyLineType.USE_ALWAYS;
		copyTargetFlag = false;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId,
			DesignerContext ctx) {
		final PartsMaster parts = new PartsMaster();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = CODE;
		parts.emptyLineType = emptyLineType;
		parts.columnNameValue = columnNameValue;
		parts.columnNameLabel = columnNameLabel;
		parts.clearAndSetDefaultValue(this);
		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 */
	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 */
	@Override
	public List<PartsColumn> newColumns() {
		// ドロップダウンリストモードなら固有の値を持つ
		final List<PartsColumn> cols = new ArrayList<>();
		int columnSizeCode = DEFAULT_CODE_LENGTH;
		int columnSizeLabel = DEFAULT_LABEL_LENGTH;
		if (tableId != null && columnNameValue != null && columnNameLabel != null) {
			// 汎用テーブルのカラム長から求める
			final TableMetaDataService meta = CDI.current().select(TableMetaDataService.class).get();
			final Map<String, ColumnMetaData> map = meta.getColumnMap(tableId);
			columnSizeCode = toColumnSize(map.get(columnNameValue), DEFAULT_CODE_LENGTH);
			columnSizeLabel = toColumnSize(map.get(columnNameLabel), DEFAULT_LABEL_LENGTH);
		}
		// コード
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_CODE";
			col.columnType = ColumnType.VARCHAR;
			col.columnSize = columnSizeCode;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.roleCode = CODE;

			cols.add(col);
		}
		// コードに対するラベル
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_LABEL";
			col.columnType = ColumnType.VARCHAR;
			col.columnSize = columnSizeLabel;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.roleCode = LABEL;
			cols.add(col);
		}
		return cols;
	}

	/** カラムのメタ情報からカラム長を決定する */
	private int toColumnSize(ColumnMetaData c, int defaultSize) {
		if (c != null) {
			// DBカラム型が文字列ならカラム定義の桁数を流用、型がその他であればデフォルト値を使用
			final String dbColumnType = c.columnType;
			final TableMetaDataService meta = CDI.current().select(TableMetaDataService.class).get();
			if (meta.isString(dbColumnType))
				return c.columnSize;
		}
		return defaultSize;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {
		// イベント
		int i = 0;
		if (events.isEmpty()) {
			events.add(new PartsEvent(AFTER_SELECT, ++i));
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
