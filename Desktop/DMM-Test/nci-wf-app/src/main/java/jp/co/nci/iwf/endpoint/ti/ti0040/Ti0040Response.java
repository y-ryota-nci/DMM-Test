package jp.co.nci.iwf.endpoint.ti.ti0040;

import java.util.List;

import jp.co.nci.iwf.designer.service.tableInfo.ColumnMetaData;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;

/**
 * 汎用テーブル定義画面のレスポンス
 */
public class Ti0040Response extends BaseResponse {

	public MwmTableEx table;
	public List<ColumnMetaData> columns;

}
