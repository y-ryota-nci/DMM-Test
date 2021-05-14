package jp.co.nci.iwf.designer.parts.design;

import java.util.Map;
import java.util.stream.Collectors;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.parts.PartsEvent;

/**
 * 【デザイン時】子コンテナをもつパーツの抽象基底クラス
 */
public abstract class PartsDesignChildHolder extends PartsDesignContainer implements PartsEvents {
	/** パーツ子要素ID */
	public Long partsChildHolderId;
	/** 子コンテナID */
	public Long childContainerId;
	/** ボタンを上部に表示 */
	public boolean showButtonTopFlag;
	/** ボタン制御：空行追加 */
	public boolean showAddEmptyButtonFlag;
	/** ボタン制御：削除 */
	public boolean showDeleteButtonFlag;
	/** ボタン制御：コピー */
	public boolean showCopyButtonFlag;
	/** ボタン制御：行数変更 */
	public boolean showLineCountButtonFlag;

	/** 子コンテナをもつパーツの拡張情報のフィールド名 */
	protected static final String[] fields = {
			"showButtonTopFlag",
			"showAddEmptyButtonFlag",
			"showDeleteButtonFlag",
			"showCopyButtonFlag",
			"showLineCountButtonFlag",
			"inputedJudgePartsIds",		// PartsDesignContainer.javaで定義
	};

	/** イベント定義 */
	private static final String[] eventNames = {
			ADD_EMPTY_LINE, DELETE_LINE, COPY_LINE, CHANGE_LINE_COUNT, CHANGE_PAGE_NO, CAN_DELETE_LINE
	};

	/** 当パーツで定義されるイベント名 */
	protected String[] getEventNames() {
		return eventNames;
	}

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();

		initRowCount = 2;
		minRowCount = 1;
		pageSize = 5;

		// ボタン制御
		showButtonTopFlag = false;
		showAddEmptyButtonFlag = true;
		showDeleteButtonFlag = true;
		showCopyButtonFlag = true;
		showLineCountButtonFlag = true;
	}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		// 既存のイベント定義
		final Map<String, PartsEvent> exists = events.stream()
				.collect(Collectors.toMap(e -> e.eventName, e -> e));

		// 導入されていないイベント定義を追記
		for (int i = 0; i < getEventNames().length; i++) {
			String eventName = eventNames[i];
			if (!exists.containsKey(eventName))
				events.add(new PartsEvent(eventName, i));
			else
				exists.get(eventName).sortOrder = i;
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
