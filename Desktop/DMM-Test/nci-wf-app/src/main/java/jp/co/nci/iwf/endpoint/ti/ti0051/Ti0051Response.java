package jp.co.nci.iwf.endpoint.ti.ti0051;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;

/**
 * 汎用テーブル検索条件設定の初期化レスポンス
 */
public class Ti0051Response extends BaseResponse {
	/** 汎用テーブル */
	public MwmTableEx tableDef;
	/** 汎用テーブル検索条件 */
	public MwmTableSearchEx table;
	/** 汎用テーブル検索条件カラム */
	public List<MwmTableSearchColumnEx> columns;

}
