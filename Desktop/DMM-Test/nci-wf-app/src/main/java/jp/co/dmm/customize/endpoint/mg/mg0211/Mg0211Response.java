package jp.co.dmm.customize.endpoint.mg.mg0211;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Mg0211Response extends BaseResponse {

	/** 源泉税区分マスタ */
	public Mg0211Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
