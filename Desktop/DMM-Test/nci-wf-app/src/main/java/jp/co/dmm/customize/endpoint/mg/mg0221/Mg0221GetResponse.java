package jp.co.dmm.customize.endpoint.mg.mg0221;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 支払業務マスタ取得レスポンス
 *
 */
public class Mg0221GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0221Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
