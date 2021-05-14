package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmDocFolderAccessibleInfoEx;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書フォルダ設定画面の追加・編集レスポンス
 */
public class Dc0050EditResponse extends BaseResponse {

	/** 文書フォルダ情報 */
	public MwvDocFolder folder;
	/** 文書フォルダ権限情報一覧 */
	public List<MwmDocFolderAccessibleInfoEx> accessibles;
//	/** 権限設定用ロールの選択肢 */
//	public List<OptionItem> roles;
//	/** 親フォルダの文書フォルダID */
//	public Long docFolderParentId;
//	/** フォルダの選択肢 */
//	public List<OptionItem> parentFolders;
//	/** 並び順の選択肢 */
//	public List<OptionItem> sortOrders;

}
