package jp.co.dmm.customize.endpoint.mg.mg0161;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 消費税マスタ取得レスポンス
 *
 */
public class Mg0161GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0161Entity entity;

	/** 税処理区分選択肢 */
	public List<OptionItem> taxTps;

	/** 端数処理区分選択肢 */
	public List<OptionItem> frcTps;

	/** 正残区分選択肢 */
	public List<OptionItem> dcTps;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;
}
