package jp.co.nci.iwf.endpoint.mm.mm0004;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 【プロファイル管理】ユーザ所属編集画面のレスポンス
 */
public class Mm0004Response extends BaseResponse {
	/** 主務兼務区分の選択肢 */
	public List<OptionItem> jobTypeList;

}
