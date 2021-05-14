package jp.co.dmm.customize.endpoint.md;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * 取引先情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MdExcelWriter extends PoiUtils {

	/** 取引先マスタシートの書き込み */
	public void writeSplr(Sheet sheet, MdExcelSheetSplr sheetSplr, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MdExcelSplrEntity sp : sheetSplr.splrs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, sp.processType);
			// 会社コード
			setCellValue(row, i++, sp.companyCd);
			// 取引先コード
			setCellValue(row, i++, sp.splrCd);
			// 適格請求書発行事業者登録番号
			setCellValue(row, i++, sp.cmptEtrNo);
			// 取引先名称（漢字）
			setCellValue(row, i++, sp.splrNmKj);
			// 取引先名称（カタカナ）
			setCellValue(row, i++, sp.splrNmKn);
			// 取引先名称（略称）
			setCellValue(row, i++, sp.splrNmS);
			// 取引先名称（英名）
			setCellValue(row, i++, sp.splrNmE);
			// 法人・個人区分
			setCellValue(row, i++, sp.crpPrsTp);
			// 国内・海外区分
			setCellValue(row, i++, sp.dmsAbrTp);
			// 国コード
			setCellValue(row, i++, sp.lndCd);
			// 国名
			setCellValue(row, i++, sp.lndNm);
			// 法人番号
			setCellValue(row, i++, sp.crpNo);
			// 郵便番号
			setCellValue(row, i++, sp.zipCd);
			// 住所（都道府県）コード
			setCellValue(row, i++, sp.adrPrfCd);
			// 住所（都道府県）
			setCellValue(row, i++, sp.adrPrf);
			// 住所（市区町村）
			setCellValue(row, i++, sp.adr1);
			// 住所（町名番地）
			setCellValue(row, i++, sp.adr2);
			// 住所（建物名）
			setCellValue(row, i++, sp.adr3);
			// 電話番号
			setCellValue(row, i++, sp.telNo);
			// FAX番号
			setCellValue(row, i++, sp.faxNo);
			// 関係会社区分
			setCellValue(row, i++, sp.affcmpTp);
			// 生年月日
			setCellValue(row, i++, StringUtils.isNotEmpty(sp.strBrthDt) ? sp.strBrthDt : sp.brthDt);
			// 取引状況区分
			setCellValue(row, i++, sp.trdStsTp);
			// 有効期間（開始）
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(sp.strVdDtS) ? sp.strVdDtS : sp.vdDtS);
			// 有効期間（終了）
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(sp.strVdDtE) ? sp.strVdDtE : sp.vdDtE);
			// 備考
			setCellValue(row, i++, sp.rmk);
			// 部門コード
			setCellValue(row, i++, sp.bumonCd);
			// 最終判定区分
			setCellValue(row, i++, sp.lastJdgTp);
			// 最終判定備考
			setCellValue(row, i++, sp.lastJdgRmk);

			// エラー
			setCellValue(row, i++, sp.errorText);
		}
	}

	/** 振込先マスタシートを書き込み */
	public void writePayeeBnkacc(Sheet sheet, MdExcelSheetPayeeBnkacc sheetAcc, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MdExcelSplrAccEntity acc : sheetAcc.accs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, acc.processType);
			// 会社コード
			setCellValue(row, i++, acc.companyCd);
			// 振込先銀行口座コード
			setCellValue(row, i++, acc.payeeBnkaccCd);
			// 仕入先社員区分
			setCellValue(row, i++, acc.buyeeStfTp);
			// 取引先コード
			setCellValue(row, i++, acc.splrCd);
			// 銀行コード
			setCellValue(row, i++, acc.bnkCd);
			// 銀行支店コード
			setCellValue(row, i++, acc.bnkbrcCd);
			// 銀行口座種別
			setCellValue(row, i++, acc.bnkaccTp);
			// 銀行口座番号
			setCellValue(row, i++, acc.bnkaccNo);
			// 銀行口座名称
			setCellValue(row, i++, acc.bnkaccNm);
			// 銀行口座名称（カタカナ）
			setCellValue(row, i++, acc.bnkaccNmKn);
			// 振込手数料負担区分
			setCellValue(row, i++, acc.payCmmOblTp);
			// 休日処理区分
			setCellValue(row, i++, acc.hldTrtTp);
			// 有効期間（開始）
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(acc.strVdDtS) ? acc.strVdDtS : acc.vdDtS);
			// 有効期間（終了）
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(acc.strVdDtE) ? acc.strVdDtE : acc.vdDtE);
			// 振込元銀行口座コード
			setCellValue(row, i++, acc.srcBnkaccCd);
			// 振込先銀行口座コード(SuperStream)
			setCellValue(row, i++, acc.payeeBnkaccCdSs);
			// 備考
			setCellValue(row, i++, acc.rmk);
			// エラー
			setCellValue(row, i++, acc.errorText);
		}
	}

	/** 関係先マスタシートを書き込み */
	public void writeRltPrt(Sheet sheet, MdExcelSheetRltPrt sheetRlt, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MdExcelRltPrtEntity rlt : sheetRlt.rlts) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 会社コード
			setCellValue(row, i++, rlt.companyCd);
			// 取引先コード
			setCellValue(row, i++, rlt.splrCd);
			// 連番
			setCellValue(row, i++, rlt.sqno);
			// 関係先名
			setCellValue(row, i++, rlt.rltPrtNm);
			// 法人・個人区分
			setCellValue(row, i++, rlt.crpPrsTp);
			// 国コード
			setCellValue(row, i++, rlt.lndCd);
			// 生年月日
			setCellValue(row, i++, isNotEmpty(rlt.strBrthDt) ? rlt.strBrthDt : rlt.brthDt);
			// 一致件数
			setCellValue(row, i++, rlt.mtchCnt);
			// 一致プロファイルID
			setCellValue(row, i++, rlt.mtchPeid);
			// 判定
			setCellValue(row, i++, rlt.jdgTp);
			// コメント
			setCellValue(row, i++, rlt.rltPrtRmk);
			// エラー
			setCellValue(row, i++, rlt.errorText);
		}
	}

	/** 反社情報シートを書き込み */
	public void writeOrgCrm(Sheet sheet, MdExcelSheetOrgCrm sheetOrg, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MdExcelOrgCrmEntity org : sheetOrg.orgs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 会社コード
			setCellValue(row, i++, org.companyCd);
			// 取引先コード
			setCellValue(row, i++, org.splrCd);
			// 連番
			setCellValue(row, i++, org.sqno);
			// プロファイルID
			setCellValue(row, i++, org.peid);
			// 一致名称
			setCellValue(row, i++, org.mtchNm);
			// 国コード
			setCellValue(row, i++, org.lndCd);
			// 性別
			setCellValue(row, i++, org.gndTp);
			// 生年月日
			setCellValue(row, i++, org.brthDt);
			// エラー
			setCellValue(row, i++, org.errorText);

		}
	}

}
