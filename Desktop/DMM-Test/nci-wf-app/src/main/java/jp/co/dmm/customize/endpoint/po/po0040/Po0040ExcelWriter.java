package jp.co.dmm.customize.endpoint.po.po0040;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * プロファイル情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class Po0040ExcelWriter extends PoiUtils {

	/** kintoneデータの書込. */
	public void write(Sheet sheet, List<Po0040KntnInf> datas, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Po0040KntnInf d : datas) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, d.recNo);			//レコード番号
			setCellValue(row, i++, d.evntNo);			//イベントNo
			setCellValue(row, i++, d.kntnSts);			//ステータス
			setCellValue(row, i++, d.exhbDt);			//開催日
			setCellValue(row, i++, d.evntMngNo);		//イベント管理No
			setCellValue(row, i++, d.evntCont);			//イベント内容
			setCellValue(row, i++, d.kntnHllId);		//kintoneホールID
			setCellValue(row, i++, d.hllNm);			//ホール名
			setCellValue(row, i++, d.prdctId);			//プロダクションID
			setCellValue(row, i++, d.tlntNm);			//タレント名
			setCellValue(row, i++, d.splrCd);			//取引先コード
			setCellValue(row, i++, d.prdctNm);			//プロダクション名
			setCellValue(row, i++, d.baseAmt);			//基本金額
			setCellValue(row, i++, d.adjBaseAmt);		//調整額（基本金額）
			setCellValue(row, i++, d.invAmt);			//請求額
			setCellValue(row, i++, d.trnspExpAmt);		//交通費
			setCellValue(row, i++, d.adjTrnspExpAmt);	//調整額（交通費）
			setCellValue(row, i++, d.mnscrExpAmt);		//原稿作成費
			setCellValue(row, i++, d.bumonCd);			//部門コード
			setCellValue(row, i++, d.anlysCd);			//分析コード
			setCellValue(row, i++, d.smry);				//概要
//			setCellValue(row, i++, d.taxCd);			//税区分
			setCellValue(row, i++, d.taxFgChg);			//消費税
			setCellValue(row, i++, d.taxUnt);			//税処理単位
			setCellValue(row, i++, d.itmExpsCd1);		//費目コード(1)
			setCellValue(row, i++, d.itmExpsCd2);		//費目コード(2)
			setCellValue(row, i++, d.errorText);		//エラー内容
		}
	}
}
