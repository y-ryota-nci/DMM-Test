package jp.co.dmm.customize.endpoint.mg.mg0251;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 部門関連マスタ取得レスポンス
 *
 */
public class Mg0251GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0251Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
