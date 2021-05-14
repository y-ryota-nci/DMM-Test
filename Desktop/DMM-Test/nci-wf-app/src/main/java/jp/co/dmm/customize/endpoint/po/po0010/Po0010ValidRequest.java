package jp.co.dmm.customize.endpoint.po.po0010;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 変更申請バリデーションのリクエスト
 */
public class Po0010ValidRequest extends BaseRequest {
	/** 会社コード */
	public String companyCd;
	/** 発注No */
	public String purordNo;
	/** 発注区分 */
	public String purordTp;
	/** 定期発注区分 */
	public String prdPurordTp;
}
