package jp.co.nci.iwf.endpoint.vd.vd0061;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

public class Vd0061InsertRequest extends BaseRequest {

	/** 選択肢マスタ */
	public MwmOption entity;
}
