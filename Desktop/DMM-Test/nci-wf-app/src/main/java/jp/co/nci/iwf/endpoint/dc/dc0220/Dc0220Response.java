package jp.co.nci.iwf.endpoint.dc.dc0220;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 文書トレイ編集のレスポンス
 */
public class Dc0220Response extends BaseResponse {
	/** 検索条件項目の選択肢 */
	public List<MwmDocBusinessInfoName> docBusinessInfoNames;
	/** 検索条件一致区分の選択肢 */
	public List<OptionItem> conditionMatchTypes;

	/** テンプレートのHTML */
	public String trayTemplateHtml;
	/** 表示位置揃えの選択肢 */
	public List<OptionItem> alignTypeOptions;

	/** 文書管理トレイ設定マスタ */
	public Dc0220Entity entity;
	/** 文書管理トレイ検索条件マスタ */
	public List<Dc0220Condition> conditions;
	/** 文書管理トレイ検索結果マスタ */
	public List<Dc0220Result> results;
	/** コンテンツ種別（文書管理用）の選択肢 */
	public List<OptionItem> contentsTypes;
	/** 公開／非公開の選択肢 */
	public List<OptionItem> publishFlags;
	/** 保存期間区分の選択肢 */
	public List<OptionItem> retentionTermTypes;
}
