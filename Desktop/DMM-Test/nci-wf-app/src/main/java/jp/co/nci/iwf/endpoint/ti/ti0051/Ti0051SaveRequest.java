package jp.co.nci.iwf.endpoint.ti.ti0051;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;

/**
 * 汎用テーブル検索条件設定の保存リクエスト
 */
public class Ti0051SaveRequest extends BaseRequest {

	/** 汎用テーブル検索条件 */
	public MwmTableSearch table;
	/** 汎用テーブル検索条件カラム */
	public List<MwmTableSearchColumnEx> columns;
}
