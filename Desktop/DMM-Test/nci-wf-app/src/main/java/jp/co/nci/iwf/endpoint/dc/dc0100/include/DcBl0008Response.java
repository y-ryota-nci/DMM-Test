package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAppendedInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メモ情報ブロック：レスポンス
 */
public class DcBl0008Response extends BaseResponse {

	/** 文書メモ一覧 */
	public List<DocAppendedInfo> memos;

}
