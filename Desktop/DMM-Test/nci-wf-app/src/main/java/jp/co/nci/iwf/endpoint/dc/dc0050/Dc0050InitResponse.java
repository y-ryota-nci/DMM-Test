package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書フォルダ設定画面の初期化レスポンス
 */
public class Dc0050InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ツリーデータ */
	public List<Dc0050TreeItem> treeItems;
	/** メタテンプレートの選択肢 */
	public List<OptionItem> metaTemplates;
	/** 権限設定用ロールの選択肢 */
	public List<OptionItem> roles;
}
