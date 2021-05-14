package jp.co.dmm.customize.endpoint.mg.mg0181;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Mg0181Response extends BaseResponse {

	/** 社内レートマスタ */
	public Mg0181Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
	/** 通貨コード選択肢 */
	public List<OptionItem> mnyCds;
}
