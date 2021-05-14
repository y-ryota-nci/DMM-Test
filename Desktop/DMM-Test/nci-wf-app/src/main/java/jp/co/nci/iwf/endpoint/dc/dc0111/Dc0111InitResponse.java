package jp.co.nci.iwf.endpoint.dc.dc0111;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;

/**
 * 画面文書定義設定の初期化リクエスト
 */
public class Dc0111InitResponse extends BaseResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** 画面の選択肢 */
	public List<OptionItem> screenIds;
	/** WF連携先画面の選択肢 */
	public List<OptionItem> screenProcessCodes;
	/** 画面文書定義 */
	public MwvScreenDocDef entity;

}