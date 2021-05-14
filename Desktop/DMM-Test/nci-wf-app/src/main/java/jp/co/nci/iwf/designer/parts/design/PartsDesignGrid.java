package jp.co.nci.iwf.designer.parts.design;

import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;

/**
 * 【デザイン時】グリッドパーツ（旧名：可変テーブル）
 */
public class PartsDesignGrid extends PartsDesignChildHolder {
	/** tableタグの最小幅(pixel) */
	public int minWidth;
	/** テーブルヘッダー非表示 */
	public boolean hideTableHeader;

	/** グリッドパーツの拡張情報のフィールド名 */
	private static final String[] exFields;

	/** スクロールバーの出ない最小幅：PC */
	protected static final int PC = 1120;
	/** スクロールバーの出ない最小幅：iPad */
	protected static final int iPad = 960;
	/** スクロールバーの出ない最小幅：iPadPro */
	protected static final int iPadPro = 1280;
	/** スクロールバーの出ない最小幅：iPhone6～8 */
	protected static final int iPhone8 = 580;
	/** スクロールバーの出ない最小幅：iPhone5/SE */
	protected static final int iPhone5 = 480;

	/** tableタグの最小幅(pixel)の初期値。 */
	private static final int DEFAULT_MIN_WIDTH = iPad;

	static {
		// 基底クラスの「子コンテナをもつパーツの拡張情報のフィールド名」に、当クラス独自分を追記
		final List<String> values = asList(PartsDesignChildHolder.fields);
		values.add("minWidth");
		values.add("hideTableHeader");
		exFields = values.toArray(new String[values.size()]);
	}

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		// ダミー行
		int i = 0;
		relations.add(new PartsRelation("", 4, PartsIoType.BOTH, ++i, false));
		relations.add(new PartsRelation("", 4, PartsIoType.BOTH, ++i, false));
		relations.add(new PartsRelation("", 4, PartsIoType.BOTH, ++i, false));

		// tableタグの最小幅(pixel)
		minWidth = DEFAULT_MIN_WIDTH;
		// テーブルヘッダー非表示
		hideTableHeader = false;
	}

	/**
	 * 【デザイン時】新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsGrid newParts(PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		PartsGrid parts = new PartsGrid();
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
	public void beforeSave() {
		for (PartsRelation pr : relations) {
			pr.partsIoType = PartsIoType.BOTH;
		}
	}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		super.afterLoad();

		if (minWidth < 1)
			minWidth = DEFAULT_MIN_WIDTH;
	}
}
