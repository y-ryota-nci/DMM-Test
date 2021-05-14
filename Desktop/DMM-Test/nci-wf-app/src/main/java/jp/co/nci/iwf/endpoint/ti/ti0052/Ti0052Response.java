package jp.co.nci.iwf.endpoint.ti.ti0052;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 汎用テーブル検索条件カラム設定レスポンス
 */
public class Ti0052Response extends BaseResponse {

	/** 検索条件表示区分の選択肢 */
	public List<OptionItem> conditionDisplayTypes;
	/** 検索条件初期値区分の選択肢 */
	public List<OptionItem> conditionInitTypes;
	/** 検索条件一致区分の選択肢 */
	public List<OptionItem> conditionMatchTypes;
	/** 検索条件ブランク区分の選択肢 */
	public List<OptionItem> conditionBlankTypes;
	/** 検索結果表示区分の選択肢 */
	public List<OptionItem> resultDisplayTypes;
	/** 検索結果ソート方向フラグの選択肢 */
	public List<OptionItem> resultOrderByDirections;
	/** 検索結果の表示揃え区分の選択肢 */
	public List<OptionItem> resultAlignTypes;
	/** 選択肢マスタの選択肢 */
	public List<OptionItem> conditionOptions;
	/** 表示位置の選択肢 */
	public List<OptionItem> positions;
	/** 列数の選択肢 */
	public List<OptionItem> colWidths;
	/** カラム型区分の選択肢 */
	public List<OptionItem> searchColumnTypes;
	/** カナ変換区分の選択肢 */
	public List<OptionItem> conditionKanaConvertTypes;
	/** 「半角スペースの除去」の選択肢 */
	public List<OptionItem> conditionTrimFlags;
}
