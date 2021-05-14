package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.List;

/**
 * コンテナ定義とEXCEL帳票のマッピングを行う構造体
 */
public class Vd0010ContainerSheet {
	/** コンテナコード */
	public String containerCode;

	/** コンテナ名 */
	public String containerName;

	/** テーブル名 */
	public String tableName;

	/** 出力日時 */
	public java.util.Date outputDate;

	/** パーツリスト */
	public List<Vd0010ContainerRow> partsList;
}
