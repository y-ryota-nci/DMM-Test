package jp.co.nci.iwf.endpoint.vd.vd0061;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

/**
 * 選択肢マスタ登録の初期化レスポンス
 */
public class Vd0061InitResponse extends BaseResponse {
	/** 選択肢マスタ */
	public MwmOption entity;
}
