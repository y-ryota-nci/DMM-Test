package jp.co.dmm.customize.endpoint.mg.mg0331;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 国マスタ取得レスポンス
 *
 */
public class Mg0331Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 対象のEntity */
	public Mg0331Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgItems;

}
