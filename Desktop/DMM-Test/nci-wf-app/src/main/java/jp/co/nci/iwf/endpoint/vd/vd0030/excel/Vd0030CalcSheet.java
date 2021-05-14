package jp.co.nci.iwf.endpoint.vd.vd0030.excel;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0010.excel.Vd0010CalcParts;

/**
 * 画面一覧のEXCELダウンロード用計算式シートBean
 */
public class Vd0030CalcSheet {
	/** シート名 */
	public String sheetName;

	/** コンテナコード */
	public String containerCode;

	/** コンテナ名 */
	public String containerName;

	/** 出力日時 */
	public java.util.Date outputDate;

	/** パーツリスト */
	public List<Vd0010CalcParts> partsList;

	/** 画面コード */
	public String screenCode;

	/** 画面名 */
	public String screenName;
}
