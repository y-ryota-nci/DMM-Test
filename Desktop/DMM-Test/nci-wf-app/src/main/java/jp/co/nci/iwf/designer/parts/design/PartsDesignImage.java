package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsFileLimitation;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsImage;

/**
 * 画像パーツ。
 * 画像データのアップロード機能、およびアップロードしたファイルをイメージとして表示するパーツ
 */
public class PartsDesignImage extends PartsDesign implements PartsEvents {
	/** パーツ添付ファイルID（デザイン時専用。画像を添付ファイルとして保存している） */
	public Long partsAttachFileId;
	/** 枠線を使う */
	public boolean useBorderLine;
	/** 枠線の太さ */
	public Integer borderLineWidth;
	/** 枠線の色 */
	public String borderLineColor;
	/** 枠の幅 */
	public Long borderWidth;
	/** 枠の高さ */
	public Long borderHeight;
	/** 実行時にアップロードするか */
	public boolean runtimeUploadFlag;
	/** 画像未設定時にダミー画像を表示するか */
	public boolean useDummyIfNoImage;
	/** アップロードボタンのラベル */
	public String uploadButtonLabel;
	/** クリアボタンのラベル */
	public String clearButtonLabel;

	/** どんなファイル形式でも許可 */
	public boolean enableAny;
	/** 画像を許可 */
	public boolean enableImage = true;
	/** 「その他」を許可 */
	public boolean enableOther;
	/** 「その他」のファイル拡張子の正規表現 */
	public String regExpOther;

	/** 許可されたファイル名の正規表現 */
	public String fileRegExp;

	/** Checkbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"partsAttachFileId",
			"useBorderLine",
			"borderLineWidth",
			"borderLineColor",
			"borderWidth",
			"borderHeight",
			"runtimeUploadFlag",
			"useDummyIfNoImage",
			"uploadButtonLabel",
			"clearButtonLabel",
			"enableAny",
			"enableImage",
			"enableOther",
			"regExpOther",
//			"fileRegExp",	// 毎回計算し直すので保存不要
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		renderingMethod = RenderingMethod.INLINE;
		copyTargetFlag = false;
		cssClass = "text-center";
		useBorderLine = true;
		borderLineColor = "#000";
		borderLineWidth = 1;
		borderWidth = null;
		borderHeight = null;
		runtimeUploadFlag = true;
		useDummyIfNoImage = true;
		// 許可されたファイル名の正規表現
		fileRegExp = new PartsFileLimitation(this).toRegExp();
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId,
			DesignerContext ctx) {
		final PartsImage parts = new PartsImage();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.useDummyIfNoImage = this.useDummyIfNoImage;
		parts.partsAttachFileId = this.partsAttachFileId;
		// 許可されたファイル名の正規表現
		parts.fileRegExp = new PartsFileLimitation(this).toRegExp();

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
		return new ArrayList<>();	// 画像ファイルの実体は ワークフロー添付ファイル情報(MWT_ATTACH_FILE_WF)にある
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {
	}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		// 許可されたファイル名の正規表現
		fileRegExp = new PartsFileLimitation(this).toRegExp();
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
