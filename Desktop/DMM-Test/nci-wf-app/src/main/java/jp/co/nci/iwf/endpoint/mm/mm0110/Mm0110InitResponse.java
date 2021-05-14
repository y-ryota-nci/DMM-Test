package jp.co.nci.iwf.endpoint.mm.mm0110;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;

/**
 * システムプロパティ編集画面の初期化レスポンス
 */
public class Mm0110InitResponse extends Mm0110SearchResponse {

	public String corporationCode;
	public List<OptionItem> corporations;
}
