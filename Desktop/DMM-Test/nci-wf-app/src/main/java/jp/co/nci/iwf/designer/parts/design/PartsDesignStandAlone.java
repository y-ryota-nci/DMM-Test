package jp.co.nci.iwf.designer.parts.design;

import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsButtonSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;

/**
 * 独立画面パーツ。子コンテナをポップアップ画面として開く
 */
public class PartsDesignStandAlone extends PartsDesignChildHolder {
	/** パーツの拡張情報のフィールド名 */
	private static final String[] exFields;

	static {
		// 基底クラスの「子コンテナをもつパーツの拡張情報のフィールド名」に、当クラス独自分を追記
		final List<String> values = asList(PartsDesignChildHolder.fields);
		values.add("iconCssClass");
		values.add("buttonLabel");
		values.add("buttonSize");
		exFields = values.toArray(new String[values.size()]);
	}

	/** ボタン内のアイコンに適用するCSSクラス */
	public String iconCssClass;
	/** ボタンラベル名 */
	public String buttonLabel;
	/** ボタンサイズ */
	public int buttonSize;

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();

		initRowCount = 1;
		minRowCount = 1;
		pageSize = 0;
		renderingMethod = RenderingMethod.INLINE;
		cssClass = "btn btn-default";
		iconCssClass = "glyphicon glyphicon-new-window";
		copyTargetFlag = false;
		buttonLabel = partsCode;
		buttonSize = PartsButtonSize.NORMAL;
	}

	/**
	 * 【デザイン時】新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsStandAlone newParts(PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		PartsStandAlone parts = new PartsStandAlone();
		setPartsCommonValue(parts, parent, rowId, ctx);
		parts.defaultRoleCode = null;
		fillRows(parts, ctx);

		// ページ制御用
		parts.pageNo = 1;
		parts.pageSize = pageSize;

		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return exFields;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		int i = 0;
		if (events.isEmpty()) {
			events.add(new PartsEvent(OPEN_POPUP, ++i));
			events.add(new PartsEvent(BEFORE_CLOSE_POPUP, ++i));
			events.add(new PartsEvent(AFTER_CLOSE_POPUP, ++i));
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
