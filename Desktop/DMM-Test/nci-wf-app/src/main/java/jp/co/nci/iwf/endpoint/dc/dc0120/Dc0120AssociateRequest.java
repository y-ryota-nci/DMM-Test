package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;

/**
 * 業務文書メニュー編集画面の紐付けリクエスト
 */
public class Dc0120AssociateRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String parentLevelCode;
	public List<MwmScreenDocDef> defs;

}
