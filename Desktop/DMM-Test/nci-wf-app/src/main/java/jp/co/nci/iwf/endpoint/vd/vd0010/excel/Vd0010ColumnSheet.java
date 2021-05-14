package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.List;

/**
 * カラム定義のEXCELダウンロード用
 */
public class Vd0010ColumnSheet {
	/** コンテナコード */
	public String containerCode;

	/** コンテナ名 */
	public String containerName;

	/** テーブル名 */
	public String tableName;

	/** 出力日時 */
	public java.util.Date outputDate;

	/** 列：DBカラムリスト */
	public List<Vd0010Column> columnList;
}
