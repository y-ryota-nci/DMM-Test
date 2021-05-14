package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ブロック：添付ファイルのレスポンス
 */
public class Bl0003Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<AttachFileWfInfo> attachFileWfList;

}
