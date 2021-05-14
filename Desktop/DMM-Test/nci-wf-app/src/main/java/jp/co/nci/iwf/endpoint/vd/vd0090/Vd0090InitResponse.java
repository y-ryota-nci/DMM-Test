package jp.co.nci.iwf.endpoint.vd.vd0090;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 新規申請メニュー割当一覧の初期化リクエスト
 */
public class Vd0090InitResponse extends BaseResponse {

	public List<OptionItem> processDefs;

}
