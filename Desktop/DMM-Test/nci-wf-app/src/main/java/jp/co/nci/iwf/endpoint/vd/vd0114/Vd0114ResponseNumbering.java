package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：採番パーツのレスポンス
 */
public class Vd0114ResponseNumbering extends BaseResponse {

	public List<OptionItem> partsNumberingFormats;
}
