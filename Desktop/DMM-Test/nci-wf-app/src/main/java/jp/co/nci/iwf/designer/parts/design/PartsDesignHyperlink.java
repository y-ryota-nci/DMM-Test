package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsHyperlink;

/**
 * 【デザイン時】ハイパーリンクパーツ。
 * デザイン時にあらかじめリンク先ファイル／URLを決定しておくコントロール。
 * 申請／承認時にリンク先を変えることは出来ない。申請／承認時にリンク先を変えたい（＝アップロードさせたい）なら添付ファイルパーツを使うこと。
 */
public class PartsDesignHyperlink extends PartsDesign {
	/** リンクを開く対象window */
	public String target;
	/** URL */
	public String url;
	/** アップロードするかURL指定するか（trueならアップロードする） */
	public boolean requiredUpload;

	/** Hyperlink固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"target",
			"url",
			"requiredUpload",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		target = "_blank";
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsHyperlink parts = new PartsHyperlink();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = null;

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

	@Override
	public List<PartsColumn> newColumns() {
		return new ArrayList<>();	// ハイパーリンクは実行時データを保持しない
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {
	}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
	}

}
