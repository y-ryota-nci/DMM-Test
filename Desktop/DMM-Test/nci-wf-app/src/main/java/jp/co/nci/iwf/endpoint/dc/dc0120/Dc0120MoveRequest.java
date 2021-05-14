package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 業務文書メニュー編集画面の移動リクエスト
 */
public class Dc0120MoveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<MwmScreenDocLevel> levels;
	public List<MwmScreenDocDef> defs;

}
