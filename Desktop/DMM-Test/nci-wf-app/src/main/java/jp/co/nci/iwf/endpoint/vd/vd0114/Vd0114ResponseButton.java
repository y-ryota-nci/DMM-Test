package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：Buttonパーツのレスポンス
 */
public class Vd0114ResponseButton extends BaseResponse {
	/** パーツI/O区分の選択肢 */
	public List<OptionItem> partsSearchTypes;
	/** 検索条件用カラム名の選択肢 */
	public List<OptionItem> columnNamesValue;
	/** 検索結果用カラム名の選択肢 */
	public List<OptionItem> columnNamesLabel;
	/** 絞込条件＋検索結果のカラム名の選択肢 */
	public List<OptionItem> columnNamesInOut;
	/** 汎用テーブルの選択肢 */
	public List<OptionItem> tableIds;
	/** 汎用テーブル検索条件の選択肢 */
	public List<OptionItem> tableSearchIds;
	/** ボタンサイズの選択肢 */
	public List<OptionItem> buttonSizes;
}
