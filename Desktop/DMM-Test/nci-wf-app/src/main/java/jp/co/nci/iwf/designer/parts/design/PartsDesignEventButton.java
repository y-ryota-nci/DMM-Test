package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsButtonSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsEventButton;

/**
 * 【デザイン時】イベントボタンパーツ。
 * V5でJavascriptボタンの後継パーツ。単にイベントのトリガーとするためのボタンである
 */
public class PartsDesignEventButton extends PartsDesign {
	/** ボタン内のアイコンに適用するCSSクラス */
	public String iconCssClass;
	/** ボタンラベル名 */
	public String buttonLabel;
	/** ボタンサイズ */
	public int buttonSize;

	/** 空パーツカラムリスト */
	private static final List<PartsColumn> EMPTY = new ArrayList<>();

	/** パーツ固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"iconCssClass",
			"buttonLabel",
			"buttonSize",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		renderingMethod = RenderingMethod.INLINE;
		cssClass = "btn btn-default";
		iconCssClass = "glyphicon glyphicon-info-sign";
		copyTargetFlag = false;
		buttonLabel = partsCode;
		buttonSize = PartsButtonSize.NORMAL;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId,
			DesignerContext ctx) {
		final PartsEventButton parts = new PartsEventButton();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = null;
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
		return EMPTY;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
