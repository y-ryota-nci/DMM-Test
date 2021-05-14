package jp.co.nci.iwf.endpoint.au.au0011;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.WfvUserPassword;

/**
 * パスワード変更レスポンス
 */
public class Au0011Response extends BaseResponse {
	/** パスワード */
	public WfvUserPassword password;
	/** パスワードの変更履歴 */
	public List<Au0011History> histories;
	/** 企業属性 */
	public List<Au0011CorpProperty> corpProperties;
}
