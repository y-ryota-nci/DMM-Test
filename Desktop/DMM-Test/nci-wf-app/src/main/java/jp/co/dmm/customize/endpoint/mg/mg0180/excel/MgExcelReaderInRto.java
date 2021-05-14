package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.math.BigDecimal;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 社内レートマスタアップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderInRto extends MgExcelReader<MgExcelBookInRto, MgExcelSheetInRto> {

	/** 社内レートマスタシートの読み込み */
	@Override
	protected MgExcelSheetInRto readSheet(Sheet sheet) {
	    final MgExcelSheetInRto sheetObj = new MgExcelSheetInRto();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityInRto entity = new MgExcelEntityInRto();
			int i = 0;

			// 処理区分
			entity.processType = getCellStringValue(row, i++);

			// 企業コード
			String companyCd = getCellStringValue(row, i++);
			// 企業コードが未入力なら終わり
			if (isEmpty(companyCd)) {
				break;
			}

			if (isNotEmpty(entity.processType)) {
				entity.companyCd = companyCd;
				entity.mnyCd = getCellStringValue(row, i++);
				entity.strSqno = getCellStringValue(row, i++);
				entity.sqno = NumberUtils.isDigits(entity.strSqno) ? NumberUtils.toInt(entity.strSqno) : null;
				// getCellDoubleValueを使うと中身が空だと"0"として戻ってくるため、空の場合のチェックができない
				// そのため社内レートはEXCELから文字列として取得し、BigDecimal型に変換
				// 中身が空であればinRtoはNULLとなり、バリデーションエラーとなるはず
				// ※追記(yasaka) Excelに出力するときにStringを参照したいためentityに文字列型のinRtoを追加します。
				// 参照したい理由は文字列が入力された場合、エラー出力時のExcelに0が表示されるからです。上記の連番も同様
				entity.strInRto = getCellStringValue(row, i++);
				if (NumberUtils.isNumber(entity.strInRto)) {
					entity.inRto = new BigDecimal(entity.strInRto);
				}
				entity.rtoTp = getCellStringValue(row, i++);
				// 有効期間（開始）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					entity.vdDtS = getCellDateValue(row, i++);
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
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					entity.vdDtE = getCellDateValue(row, i++);
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

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "社内レートマスタ";
	}

}
