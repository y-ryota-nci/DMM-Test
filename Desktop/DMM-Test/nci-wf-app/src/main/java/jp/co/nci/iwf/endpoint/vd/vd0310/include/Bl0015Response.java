package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ブロック：ワークフロー文書ファイルのレスポンス
 */
public class Bl0015Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<DocFileWfInfo> docFileWfList;

}
