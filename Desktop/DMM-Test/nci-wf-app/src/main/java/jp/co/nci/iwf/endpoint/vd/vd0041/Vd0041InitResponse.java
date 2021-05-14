package jp.co.nci.iwf.endpoint.vd.vd0041;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;

/**
 * 画面プロセス定義設定の初期化リクエスト
 */
public class Vd0041InitResponse extends BaseResponse {

	/** プロセス定義の選択肢 */
	public List<OptionItem> processDefCodes;
	/** プロセス定義明細の選択肢 */
	public List<OptionItem> processDefDetailCodes;
	/** 画面の選択肢 */
	public List<OptionItem> screenIds;
	/** 画面プロセス定義 */
	public MwvScreenProcessDef entity;

}
