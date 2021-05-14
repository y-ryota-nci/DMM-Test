package jp.co.nci.iwf.endpoint.wl.wl0011;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;

/**
 * トレイ編集のレスポンス
 */
public class Wl0011Response extends BaseResponse {
	/** 文書ステータスの選択肢 */
	public List<OptionItem> businessStatusOptions;
	/** 文書種別の選択肢 */
	public List<OptionItem> processDefOptions;
	/** プロセス状態の選択肢 */
	public List<OptionItem> processStatusOptions;
	/** 検索条件項目の選択肢 */
	public List<MwmBusinessInfoName> businessInfoNames;
	/** 検索条件一致区分の選択肢 */
	public List<OptionItem> conditionMatchTypes;

	/** テンプレートのHTML */
	public String trayTemplateHtml;
	/** 表示位置揃えの選択肢 */
	public List<OptionItem> alignTypeOptions;

	/** トレイ設定マスタ */
	public Wl0011Entity entity;
	/** トレイ検索条件マスタ */
	public List<Wl0011Condition> conditions;
	/** トレイ検索結果マスタ */
	public List<Wl0011Result> results;
	/** 画面の選択肢 */
	public List<OptionItem> screens;
	/** 画面プロセスの選択肢 */
	public List<OptionItem> screenProcesses;
}
