package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * アップロードのEXCELファイルのライター
 */
public abstract class MgExcelWriter<T extends MgExcelSheet> extends PoiUtils {

	public abstract void writeMaster(Sheet sheet, T sheetMaster, Map<Integer, CellStyle> styles);
}
