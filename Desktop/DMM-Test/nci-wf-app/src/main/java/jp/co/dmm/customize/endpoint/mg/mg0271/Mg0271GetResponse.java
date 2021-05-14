package jp.co.dmm.customize.endpoint.mg.mg0271;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 予算科目マスタ取得レスポンス
 *
 */
public class Mg0271GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0271Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/** BS/PL区分 */
	public List<OptionItem> bsPlTps;
}
