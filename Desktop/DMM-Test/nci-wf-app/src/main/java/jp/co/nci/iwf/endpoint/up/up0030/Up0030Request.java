package jp.co.nci.iwf.endpoint.up.up0030;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メニューロール定義アップロード画面の保存レスポンス
 */
public class Up0030Request extends BaseRequest {
	/** 取込対象のメニューロールコード */
	public Set<String> menuRoleCodes;
	/** メニューロール構成を取り込むか */
	public boolean isMenuRoleDetail;
	/** アップロードファイルID */
	public Long uploadFileId;
}
