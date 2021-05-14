package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.List;

import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Dc;

/**
 * コンテナ一覧のEXCELダウンロード用表示条件設定シートBean
 */
public class Vd0010DcSheet {
	/** シート名 */
	public String sheetName;

	/** コンテナコード */
	public String containerCode;

	/** コンテナ名 */
	public String containerName;

	/** 出力日時 */
	public java.util.Date outputDate;

	/** 表示条件マスタリスト */
	public List<Mm0020Dc> dcList;

	/** 表示条件設定リスト  */
	public List<Vd0010DcParts> partsList;
}
