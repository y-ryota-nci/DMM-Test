package jp.co.dmm.customize.endpoint.mg.mg0330.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 国マスタ情報アップロードの「国マスタ」シートのマッピング定義
 */
@XlsSheet(name="国マスタ")
public class MgExcelSheetLnd extends MgExcelSheet {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 国マスタ */
	@XlsHorizontalRecords(tableLabel="国マスタ")
	public List<MgExcelEntityLnd> entityList = new ArrayList<>();

}
