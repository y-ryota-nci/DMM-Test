package jp.co.nci.iwf.designer.parts.design;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;

/**
 * 【デザイン時】Repeaterパーツ
 */
public class PartsDesignRepeater extends PartsDesignChildHolder {

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
	}

	/**
	 * 【デザイン時】新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsRepeater newParts(PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		PartsRepeater parts = new PartsRepeater();
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
		return fields;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		super.afterLoad();
	}
}
