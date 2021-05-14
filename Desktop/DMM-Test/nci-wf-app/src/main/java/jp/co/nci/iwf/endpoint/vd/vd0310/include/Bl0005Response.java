package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ApprovalRelationInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 決裁関連文書ブロックのレスポンス
 */
public class Bl0005Response extends BaseResponse {
	public List<ApprovalRelationInfo> approvalRelationList;
}
