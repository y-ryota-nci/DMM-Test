package jp.co.dmm.customize.endpoint.bd.bd0801;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算入力画面の初期化レスポンス
 */
public class Bd0801InitResponse extends BaseResponse {
	/** 年度の選択肢 */
	public List<OptionItem> years;
	/** 本部の選択肢  */
	public List<OptionItem> orgLv2s;
	/** 本部の以前の選択内容（検索条件の復元用） */
	public String organizationCodeLv2;
	/** 部・室の選択肢 */
	public List<OptionItem> orgLv3s;
	/** 部・室の以前の選択内容（検索条件の復元用） */
	public String organizationCodeLv3;
	/** 検収基準／支払い基準の選択肢 */
	public List<OptionItem> rcvCostPayTps;
	/** B/S／P/Lの選択肢 */
	public List<OptionItem> bsplTp;
}
