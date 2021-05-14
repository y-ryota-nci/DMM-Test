package jp.co.nci.iwf.designer.parts.runtime;

import java.util.List;

import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.renderer.MasterPartsColumn;

/**
 * 【実行時】汎用テーブルを参照するパーツの基底クラス
 *
 * @param <D> パーツ定義
 */
public abstract class PartsAjax<D extends PartsDesignAjax> extends PartsBase<D> {
	/** 汎用テーブル検索条件ID */
	public Long tableSearchId;
	/** 汎用マスタ検索条件リスト */
	public List<MasterPartsColumn> conditions;
	/** 汎用マスタ検索結果リスト */
	public List<MasterPartsColumn> results;
}
