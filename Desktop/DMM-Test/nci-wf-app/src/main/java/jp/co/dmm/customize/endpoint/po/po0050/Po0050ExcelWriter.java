package jp.co.dmm.customize.endpoint.po.po0050;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * 通販データアップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class Po0050ExcelWriter extends PoiUtils {

	/** 通販データの書込. */
	public void write(Sheet sheet, List<Po0050MlordInf> datas, Map<Integer, CellStyle> styles) {
		// 通販連携では２行目からがデータ行
		final int START = 1;
		int r = START;
		for (Po0050MlordInf d : datas) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, d.slpNo);			//伝票No
			setCellValue(row, i++, d.lnNo);				//行No
			setCellValue(row, i++, toYMD(d.buyDt));		//仕入日
			setCellValue(row, i++, d.buyCd);			//仕入先コード
			setCellValue(row, i++, d.buyNmKj);			//仕入先名称
			setCellValue(row, i++, d.abst);				//概要
			setCellValue(row, i++, d.cmmdtCd);			//商品コード
			setCellValue(row, i++, d.prtNo);			//品番
			setCellValue(row, i++, d.cmmdtTtl);			//商品タイトル
			setCellValue(row, i++, d.qnt);				//数量
			setCellValue(row, i++, d.uc);				//単価
			setCellValue(row, i++, d.amt);				//金額
			setCellValue(row, i++, d.rmk);				//備考
			setCellValue(row, i++, d.splrCd);			//取引先コード
			setCellValue(row, i++, d.bumonCd);			//部門コード
//			setCellValue(row, i++, d.taxCd);			//消費税コード
			setCellValue(row, i++, d.taxFgChg);			//消費税
			setCellValue(row, i++, d.taxUnt);			//税処理単位
			setCellValue(row, i++, d.itmExpsCd1);		//費目コード(1)
			setCellValue(row, i++, d.itmExpsCd2);		//費目コード(2)
			setCellValue(row, i++, d.errorText);		//エラー内容
		}
	}

	private String toYMD(String val) {
		if (isEmpty(val)) return val;
		String[] ymd = val.split("/");
		if (ymd.length == 3) return ymd[0] + ymd[1] + ymd[2];
		return val;
	}
}
