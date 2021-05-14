package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 銀行口座情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBnkacc extends MgExcelReader<MgExcelBookBnkacc, MgExcelSheetBnkacc> {

	/** 銀行口座マスタシートの読み込み */
	@Override
	protected MgExcelSheetBnkacc readSheet(Sheet sheet) {
	    final MgExcelSheetBnkacc sheetObj = new MgExcelSheetBnkacc();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBnkacc entity = new MgExcelEntityBnkacc();
			int i = 0;

			// 処理区分
			entity.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(entity.processType)) {
				entity.companyCd = companyCd;
				entity.bnkaccCd = getCellStringValue(row, i++);

				entity.strSqno = getCellStringValue(row, i++);
				entity.sqno = NumberUtils.isDigits(entity.strSqno) ? NumberUtils.toInt(entity.strSqno) : null;

				entity.bnkCd = getCellStringValue(row, i++);
				entity.bnkbrcCd = getCellStringValue(row, i++);
				entity.bnkaccTp = getCellStringValue(row, i++);
				entity.bnkaccNo = getCellStringValue(row, i++);
				entity.bnkaccNm = getCellStringValue(row, i++);
				entity.bnkaccNmKn = getCellStringValue(row, i++);
				// 有効期間（開始）
				if(getCellType(row , i)== CellType.NUMERIC) {
					entity.vdDtS = getCellDateValue(row, i++);
					//entity.strVdDtS = new SimpleDateFormat("yyyy/M/d").format(entity.vdDtS);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						entity.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						entity.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				if(getCellType(row , i)== CellType.NUMERIC) {
					entity.vdDtE = getCellDateValue(row, i++);
					//entity.strVdDtE = new SimpleDateFormat("yyyy/M/d").format(entity.vdDtE);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						entity.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						entity.strVdDtE = strVdDtE;
					}
				}
				entity.dltFg = getCellStringValue(row, i++);

				entity.cmSqno = entity.sqno;
				entity.cmVdDtS = entity.vdDtS;
				entity.cmVdDtE = entity.vdDtE;

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "銀行口座マスタ";
	}

}
