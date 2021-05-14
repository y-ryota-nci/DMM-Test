package jp.co.nci.iwf.endpoint.up.up0100;

import java.util.List;
import java.util.Set;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * アップロード履歴画面レスポンス
 */
public class Up0100Response extends BasePagingResponse {
	/** アップロード種別の選択肢 */
	public List<OptionItem> uploadKinds;
	/** 操作者のアクセス可能な画面ID */
	public Set<String> accessibleScreenIds;
}
