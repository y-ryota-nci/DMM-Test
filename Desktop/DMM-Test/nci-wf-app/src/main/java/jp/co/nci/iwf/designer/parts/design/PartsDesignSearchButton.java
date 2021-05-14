package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsButtonSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsSearchButton;

/**
 * 【デザイン時】検索ボタンパーツ
 * V5で汎用マスタパーツが有していた「ボタン押下で汎用テーブル検索ポップアップを開く機能」は当パーツへ移管された。
 */
public class PartsDesignSearchButton extends PartsDesignAjax implements PartsEvents {
	/** ボタン内のアイコンに適用するCSSクラス */
	public String iconCssClass;
	/** ボタンラベル名 */
	public String buttonLabel;
	/** クリアボタンを非表示 */
	public boolean hideClearButton;
	/** ボタンサイズ */
	public int buttonSize;

	/** Textbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"iconCssClass",
			"buttonLabel",
			"hideClearButton",
			"buttonSize",
	};

	/** 空パーツカラムリスト */
	private static final List<PartsColumn> EMPTY = new ArrayList<PartsColumn>();

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		renderingMethod = RenderingMethod.INLINE;
		cssClass = "btn btn-default";
		iconCssClass = "glyphicon glyphicon-search";
		copyTargetFlag = false;
		buttonLabel = partsCode;
		hideClearButton = false;
		buttonSize = PartsButtonSize.NORMAL;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId,
			DesignerContext ctx) {
		final PartsSearchButton parts = new PartsSearchButton();
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
	public void afterLoad() {
		// イベント
		int i = 0;
		if (events.isEmpty()) {
			events.add(new PartsEvent(BEFORE_SELECT, ++i));
			events.add(new PartsEvent(AFTER_SELECT, ++i));
			events.add(new PartsEvent(BEFORE_CLEAR, ++i));
			events.add(new PartsEvent(AFTER_CLEAR, ++i));
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
