package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsStamp;

/**
 * 【デザイン時】スタンプパーツ
 */
public class PartsDesignStamp extends PartsDesign {

	/** (アクティビティと連動した)スタンプコード */
	public String stampCode;
	/** スタンプサイズ */
	public Integer stampSize;
	/** 枠付き表示 */
	public boolean stampFrameDisplaty;
	/** 枠線カラー */
	public String stampBorderColor;

	/** スタンプパーツ固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"stampCode",
			"stampSize",
			"stampFrameDisplaty",
			"stampBorderColor",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		stampSize = 60;
		copyTargetFlag = false;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsStamp newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsStamp parts = new PartsStamp();
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
		final List<PartsColumn> list = new ArrayList<>();
		return list;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
