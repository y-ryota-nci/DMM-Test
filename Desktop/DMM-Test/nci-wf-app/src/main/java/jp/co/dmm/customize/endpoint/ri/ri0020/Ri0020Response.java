package jp.co.dmm.customize.endpoint.ri.ri0020;

import java.util.ArrayList;
import java.util.List;

import jp.co.dmm.customize.endpoint.ri.ri0010.Ri0010Entity;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 検収明細分割の検索結果レスポンス
 */
public class Ri0020Response extends BaseResponse {
	public List<OptionItem> taxCds;
	public List<Ri0010Entity> results = new ArrayList<>();
}
