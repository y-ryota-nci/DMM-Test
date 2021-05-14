package jp.co.dmm.customize.endpoint.mg.mg0241;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 支払サイトマスタ取得レスポンス
 *
 */
public class Mg0241GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0241Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;
	/** 支払サイト（月） */
	public List<OptionItem> paySiteMOpts;
	/** 支払サイト（日） */
	public List<OptionItem> paySiteNOpts;

}
