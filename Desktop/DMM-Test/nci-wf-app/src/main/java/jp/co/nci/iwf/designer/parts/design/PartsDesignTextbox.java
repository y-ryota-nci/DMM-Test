package jp.co.nci.iwf.designer.parts.design;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.LengthType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsInputType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleTextbox;
import jp.co.nci.iwf.designer.DesignerCodeBook.ValidateType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】Textboxパーツ
 */
public class PartsDesignTextbox extends PartsDesign implements RoleTextbox {
	/** 入力タイプ */
	public int inputType;
	/** 入力チェックタイプ */
	public String validateType;
	/** 桁数種別｛文字数｜バイト数｝ */
	public int lengthType;
	/** 最大文字数／最大バイト数 */
	public Integer maxLength;
	/** 最少文字数／最大バイト数 */
	public Integer minLength;
	/** HIDDENとしてレンダリングするか */
	public boolean renderAsHidden;
	/** IMEの制御 */
	public String imeMode;
	/** 読取専用か */
	public boolean readonly;
	/** 行数（TextArea用） */
	public Integer rowCount;
	/** 接頭語 */
	public String prefix;
	/** 接尾語 */
	public String suffix;
	/** 「連動するパーツ」のパーツID */
	public Long partsIdFor;
	/** 連動タイプ */
	public String coodinationType;
	/** 値をトリムする */
	public boolean trimValue = true;


	//------------------------------------------------------------------------------------------------------------
	// ■入力タイプ＝数値
	//------------------------------------------------------------------------------------------------------------
	/** 数値フォーマット */
	public Integer numberFormat;
	/** 端数処理タイプ */
	public Integer roundType;
	/** 小数点桁数 */
	public Integer decimalPlaces;
	/** (数値型での)最大値 */
	public BigDecimal max;
	/** (数値型での)最少値 */
	public BigDecimal min;
	/** マイナスなら赤字 */
	public boolean redIfNegative;
	/** 数値を1/100で保存する */
	public boolean saveAsPercent;

	//------------------------------------------------------------------------------------------------------------
	// ■入力チェックタイプ＝日付
	//------------------------------------------------------------------------------------------------------------
	/** カレンダーUIを使う（既存データ用に初期値はtrue） */
	public boolean useCalendarUI = true;
	/** 保存時にスラッシュを除去 */
	public boolean removeSlash;

	/** Textbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"inputType",
			"validateType",
			"lengthType",
			"maxLength",
			"minLength",
			"renderAsHidden",
			"imeMode",
			"readonly",
			"rowCount",
			"prefix",
			"suffix",
			"numberFormat",
			"roundType",
			"decimalPlaces",
			"max",
			"min",
			"redIfNegative",
			"saveAsPercent",
			"partsIdFor",
			"coodinationType",
			"useCalendarUI",
			"removeSlash",
			"trimValue",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		inputType = PartsInputType.TEXT;
		validateType = ValidateType.ALL;
		lengthType = LengthType.CHAR_LENGTH;
		defaultValue = "";
		maxLength = 10;
		useCalendarUI = true;
		trimValue = true;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsTextbox newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsTextbox parts = new PartsTextbox();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = TEXT;
		parts.clearAndSetDefaultValue(this);
		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	@Override
	public List<PartsColumn> newColumns() {
		final List<PartsColumn> list = new ArrayList<>();
		PartsColumn col = new PartsColumn();
		col.partsId = partsId;
		col.columnName = partsCode;
		col.columnType = ColumnType.VARCHAR;	// 暫定
		col.comments = labelText;
		col.sortOrder = sortOrder;
		col.roleCode = TEXT;

		if (PartsInputType.NUMBER == inputType) {
			col.columnType = ColumnType.NUMBER;
			// 小数点桁数、さらに「数値を1/100で保存する」なら＋２
			col.decimalPoint = MiscUtils.defaults(decimalPlaces, 0);
			if (saveAsPercent)
				col.decimalPoint += 2;
			col.columnSize = 18 + col.decimalPoint;
		}
		else if (PartsInputType.CLOB == inputType) {
			col.columnType = ColumnType.CLOB;
		}
		else if (PartsInputType.DATE == inputType) {
			col.columnType = ColumnType.DATE;
		}
		else {
			final int MAX_BYTE = 4000;
			col.columnType = ColumnType.VARCHAR;
			if (eq(validateType, ValidateType.YM))
				col.columnSize = removeSlash ? 6 : 7;
			else if (eq(validateType, ValidateType.DATE))
				col.columnSize = removeSlash ? 8 : 10;
			else if (maxLength == null)
				col.columnSize = MAX_BYTE;
			else if (lengthType == LengthType.CHAR_LENGTH)
				col.columnSize = Math.min(maxLength * 3, MAX_BYTE);	// 「文字数」＊「文字あたりのUTF-8のバイト数」
			else
				col.columnSize = Math.min(maxLength, MAX_BYTE);		// 「バイト数」
		}
		list.add(col);
		return list;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
