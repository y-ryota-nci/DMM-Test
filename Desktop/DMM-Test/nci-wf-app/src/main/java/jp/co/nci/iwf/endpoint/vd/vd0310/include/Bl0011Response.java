package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessMemoInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ブロック：メモ　レスポンス
 */
public class Bl0011Response extends BaseResponse {

	public List<ProcessMemoInfo> processMemoList;

}
