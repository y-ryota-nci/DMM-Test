package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書フォルダ設定画面の移動リクエスト
 */
public class Dc0050MoveRequest extends BaseRequest {

	public List<MwvDocFolder> folders;
	public Long parentDocFolderId;

}
