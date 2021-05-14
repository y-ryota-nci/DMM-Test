package jp.co.nci.iwf.component.tray;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * トレイ系画面の初期化レスポンス
 */
public class TrayInitResponse extends BaseResponse {
	/** 文書種別(プロセス定義)の選択肢 */
	public List<OptionItem> processDefCodes;
	/** 画面の選択肢 */
	public List<OptionItem> selectableScreenCodes;
	/** 画面プロセスの選択肢 */
	public List<OptionItem> selectableScreenProcesses;
	/** プロセスステータスの選択肢 */
	public List<OptionItem> processStatuses;
	/** 業務ステータスの選択肢 */
	public List<OptionItem> businessStatuses;
	/** 代理元ユーザの選択肢 */
	public List<OptionItem> proxyUsers;
	/** トレイタイプ */
	public String trayType;
	/** トレイ設定 */
	public TrayConfig config;
	/** トレイ設定検索条件 */
	public List<TrayConditionDef> configConditions;
	/** トレイ設定検索結果 */
	public List<TrayResultDef> configResults;
	/** トレイ設定のテンプレートHTML */
	public String trayTemplateHtml;

	// 以下、文書管理用 ---------------------------
	/** コンテンツ種別（文書管理用）の選択肢 */
	public List<OptionItem> contentsTypes;
	/** 公開／非公開の選択肢 */
	public List<OptionItem> publishFlags;
	/** 保存期間区分の選択肢 */
	public List<OptionItem> retentionTermTypes;
}
