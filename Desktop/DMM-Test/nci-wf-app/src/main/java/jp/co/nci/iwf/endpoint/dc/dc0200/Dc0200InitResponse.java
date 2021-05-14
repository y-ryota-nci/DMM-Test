package jp.co.nci.iwf.endpoint.dc.dc0200;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocTrayType;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書トレイ選択(個人用)画面の初期化レスポンス
 */
public class Dc0200InitResponse extends BaseResponse {
	public List<Dc0200Entity> entities;
	public Map<DocTrayType, List<OptionItem>> docTrayConfigOptions;
}
