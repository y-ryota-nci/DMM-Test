package jp.co.dmm.customize.endpoint.ss.ss0010;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfHd;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfPd;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInfHd;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfHd;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfPd;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInfHd;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * SS連携データのEXCELファイルのライター
 */
@ApplicationScoped
public class Ss0010ExcelWriter extends PoiUtils {

	/**
	 * SS-GL送信情報書き込み処理
	 * @param sheet
	 * @param sheetSSGLSndInf
	 * @param styles
	 */
	public void writeSSGLSndInf(Sheet sheet, Ss0010SheetSSGLSndInf sheetSSGLSndInf, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsglSndInf dat : sheetSSGLSndInf.ssglSndInfs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssglSndNo);
			setCellValue(row, i++, dat.cnclTp);
			setCellValue(row, i++, dat.makSys);
			setCellValue(row, i++, dat.makDt);
			setCellValue(row, i++, dat.makTm);
			setCellValue(row, i++, dat.sndSts);
			setCellValue(row, i++, dat.sndDt);
			setCellValue(row, i++, dat.sndTm);
			setCellValue(row, i++, dat.buyNo);
			setCellValue(row, i++, dat.advpayNo);
			setCellValue(row, i++, dat.payhysNo);
			setCellValue(row, i++, dat.jrnslpNo);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-GL送信情報(ヘッダー)書き込み処理
	 * @param sheet
	 * @param sheetSSGLSndInfHd
	 * @param styles
	 */
	public void writeSSGLSndInfHd(Sheet sheet, Ss0010SheetSSGLSndInfHd sheetSSGLSndInfHd, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsglSndInfHd dat : sheetSSGLSndInfHd.ssglSndInfHds) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssglSndNo);
			setCellValue(row, i++, dat.rcrdTp);
			setCellValue(row, i++, dat.ssCompanyCd);
			setCellValue(row, i++, dat.slpGrp);
			setCellValue(row, i++, dat.slpNo);
			setCellValue(row, i++, dat.slpDt);
			setCellValue(row, i++, dat.slpSmry);
			setCellValue(row, i++, dat.sttSlpTp);
			setCellValue(row, i++, dat.rSlpTp);
			setCellValue(row, i++, dat.orgSlpGrp);
			setCellValue(row, i++, dat.orgSlpNo);
			setCellValue(row, i++, dat.orgSlpDt);
			setCellValue(row, i++, dat.elsSysSlpNo);
			setCellValue(row, i++, dat.exccltSlpTp);
			setCellValue(row, i++, dat.allctSlpTp);
			setCellValue(row, i++, dat.issvcherId);
			setCellValue(row, i++, dat.issvcherDt);
			setCellValue(row, i++, dat.sprChrItm1);
			setCellValue(row, i++, dat.sprChrItm2);
			setCellValue(row, i++, dat.sprChrItm3);
			setCellValue(row, i++, dat.sprChrItm4);
			setCellValue(row, i++, dat.sprChrItm5);
			setCellValue(row, i++, dat.sprChrItm6);
			setCellValue(row, i++, dat.sprChrItm7);
			setCellValue(row, i++, dat.sprChrItm8);
			setCellValue(row, i++, dat.sprNmrItm1);
			setCellValue(row, i++, dat.sprNmrItm2);
			setCellValue(row, i++, dat.sprNmrItm3);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-GL送信情報(明細)書き込み処理
	 * @param sheet
	 * @param sheetSSGLSndInfDt
	 * @param styles
	 */
	public void writeSSGLSndInfDt(Sheet sheet, Ss0010SheetSSGLSndInfDt sheetSSGLSndInfDt, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsglSndInfDt dat : sheetSSGLSndInfDt.ssglSndInfDts) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssglSndNo);
			setCellValue(row, i++, dat.ssglSndDtlNo);
			setCellValue(row, i++, dat.rcrdTp);
			setCellValue(row, i++, dat.ssCompanyCd);
			setCellValue(row, i++, dat.slpGrp);
			setCellValue(row, i++, dat.slpNo);
			setCellValue(row, i++, dat.slpDt);
			setCellValue(row, i++, dat.dtlLnno);
			setCellValue(row, i++, dat.dcTp);
			setCellValue(row, i++, dat.accCd);
			setCellValue(row, i++, dat.accBrkdwnCd);
			setCellValue(row, i++, dat.dptCd);
			setCellValue(row, i++, dat.fncCd1);
			setCellValue(row, i++, dat.fncCd2);
			setCellValue(row, i++, dat.fncCd3);
			setCellValue(row, i++, dat.fncCd4);
			setCellValue(row, i++, dat.prjCd);
			setCellValue(row, i++, dat.splrPrsTp);
			setCellValue(row, i++, dat.splrPrsCd);
			setCellValue(row, i++, dat.jrnAmt);
			setCellValue(row, i++, dat.amtExctax);
			setCellValue(row, i++, dat.taxAmt);
			setCellValue(row, i++, dat.taxCd);
			setCellValue(row, i++, dat.taxIptTp);
			setCellValue(row, i++, dat.smry1);
			setCellValue(row, i++, dat.smry2);
			setCellValue(row, i++, dat.prtnAccCd);
			setCellValue(row, i++, dat.trdMnyCd);
			setCellValue(row, i++, dat.rtoTp);
			setCellValue(row, i++, dat.chgRto);
			setCellValue(row, i++, dat.trdMnyAmt);
			setCellValue(row, i++, dat.sprChrItm1);
			setCellValue(row, i++, dat.sprChrItm2);
			setCellValue(row, i++, dat.sprChrItm3);
			setCellValue(row, i++, dat.sprChrItm4);
			setCellValue(row, i++, dat.sprChrItm5);
			setCellValue(row, i++, dat.sprChrItm6);
			setCellValue(row, i++, dat.sprChrItm7);
			setCellValue(row, i++, dat.sprChrItm8);
			setCellValue(row, i++, dat.sprNmrItm1);
			setCellValue(row, i++, dat.sprNmrItm2);
			setCellValue(row, i++, dat.sprNmrItm3);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-AP送信情報書き込み処理
	 * @param sheet
	 * @param sheetSSAPSndInf
	 * @param styles
	 */
	public void writeSSAPSndInf(Sheet sheet, Ss0010SheetSSAPSndInf sheetSSAPSndInf, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsapSndInf dat : sheetSSAPSndInf.ssapSndInfs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssapSndNo);
			setCellValue(row, i++, dat.cnclTp);
			setCellValue(row, i++, dat.makSys);
			setCellValue(row, i++, dat.makDt);
			setCellValue(row, i++, dat.makTm);
			setCellValue(row, i++, dat.sndSts);
			setCellValue(row, i++, dat.sndDt);
			setCellValue(row, i++, dat.sndTm);
			setCellValue(row, i++, dat.payhysNo);
			setCellValue(row, i++, dat.jrnslpNo);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-AP送信情報(ヘッダー)書き込み処理
	 * @param sheet
	 * @param sheetSSAPSndInfHd
	 * @param styles
	 */
	public void writeSSAPSndInfHd(Sheet sheet, Ss0010SheetSSAPSndInfHd sheetSSAPSndInfHd, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsapSndInfHd dat : sheetSSAPSndInfHd.ssapSndInfHds) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssapSndNo);
			setCellValue(row, i++, dat.rcrdTp);
			setCellValue(row, i++, dat.ssCompanyCd);
			setCellValue(row, i++, dat.slpGrp);
			setCellValue(row, i++, dat.slpNo);
			setCellValue(row, i++, dat.slpDt);
			setCellValue(row, i++, dat.payerTp);
			setCellValue(row, i++, dat.buyerCd);
			setCellValue(row, i++, dat.invshtNo);
			setCellValue(row, i++, dat.invshtDt);
			setCellValue(row, i++, dat.payApplCd);
			setCellValue(row, i++, dat.apSltTp);
			setCellValue(row, i++, dat.dcRvrTp);
			setCellValue(row, i++, dat.rSlpTp);
			setCellValue(row, i++, dat.orgSlpGrp);
			setCellValue(row, i++, dat.orgSlpNo);
			setCellValue(row, i++, dat.orgSlpDt);
			setCellValue(row, i++, dat.elsSysSlpNo);
			setCellValue(row, i++, dat.mnyCd);
			setCellValue(row, i++, dat.rtoTp);
			setCellValue(row, i++, dat.chgRto);
			setCellValue(row, i++, dat.slpSmry);
			setCellValue(row, i++, dat.sptPayTp);
			setCellValue(row, i++, dat.issvcherId);
			setCellValue(row, i++, dat.issvcherDt);
			setCellValue(row, i++, dat.sprChrItm1);
			setCellValue(row, i++, dat.sprChrItm2);
			setCellValue(row, i++, dat.sprChrItm3);
			setCellValue(row, i++, dat.sprChrItm4);
			setCellValue(row, i++, dat.sprChrItm5);
			setCellValue(row, i++, dat.sprChrItm6);
			setCellValue(row, i++, dat.sprChrItm7);
			setCellValue(row, i++, dat.sprChrItm8);
			setCellValue(row, i++, dat.sprNmrItm1);
			setCellValue(row, i++, dat.sprNmrItm2);
			setCellValue(row, i++, dat.sprNmrItm3);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-AP送信情報(支払明細)書き込み処理
	 * @param sheet
	 * @param sheetSSAPSndInfPd
	 * @param styles
	 */
	public void writeSSAPSndInfPd(Sheet sheet, Ss0010SheetSSAPSndInfPd sheetSSAPSndInfPd, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsapSndInfPd dat : sheetSSAPSndInfPd.ssapSndInfPds) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssapSndNo);
			setCellValue(row, i++, dat.ssapSndDtlNo);
			setCellValue(row, i++, dat.rcrdTp);
			setCellValue(row, i++, dat.ssCompanyCd);
			setCellValue(row, i++, dat.slpGrp);
			setCellValue(row, i++, dat.slpNo);
			setCellValue(row, i++, dat.slpDt);
			setCellValue(row, i++, dat.payLnno);
			setCellValue(row, i++, dat.payMthCd);
			setCellValue(row, i++, dat.dtAppPlnDt);
			setCellValue(row, i++, dat.payPlnDt);
			setCellValue(row, i++, dat.payAmt);
			setCellValue(row, i++, dat.cshdlvDptCd);
			setCellValue(row, i++, dat.payerBnkaccCd);
			setCellValue(row, i++, dat.trsfndInfCd);
			setCellValue(row, i++, dat.issDt);
			setCellValue(row, i++, dat.bllTrmDt);
			setCellValue(row, i++, dat.blldlvDptCd);
			setCellValue(row, i++, dat.dpstPrsFg);
			setCellValue(row, i++, dat.dpstAccCd);
			setCellValue(row, i++, dat.dpstAccBrkdwnCd);
			setCellValue(row, i++, dat.dpstDptCd);
			setCellValue(row, i++, dat.dpstFncCd1);
			setCellValue(row, i++, dat.dpstFncCd2);
			setCellValue(row, i++, dat.dpstFncCd3);
			setCellValue(row, i++, dat.dpstFncCd4);
			setCellValue(row, i++, dat.dpstPrjCd);
			setCellValue(row, i++, dat.dpstSplrPrsTp);
			setCellValue(row, i++, dat.dpstSplrCd);
			setCellValue(row, i++, dat.dpstAmtExctax);
			setCellValue(row, i++, dat.dpstTaxAmt);
			setCellValue(row, i++, dat.dpstTaxCd);
			setCellValue(row, i++, dat.dpstTaxIptTp);
			setCellValue(row, i++, dat.dpstSmry1);
			setCellValue(row, i++, dat.dpstSmry2);
			setCellValue(row, i++, dat.dpstAmt);
			setCellValue(row, i++, dat.dtTrsAccCd);
			setCellValue(row, i++, dat.dtTrsAccBrkdwnCd);
			setCellValue(row, i++, dat.dtTrsDptCd);
			setCellValue(row, i++, dat.dtTrsFncCd1);
			setCellValue(row, i++, dat.dtTrsFncCd2);
			setCellValue(row, i++, dat.dtTrsFncCd3);
			setCellValue(row, i++, dat.dtTrsFncCd4);
			setCellValue(row, i++, dat.dtTrsPrjCd);
			setCellValue(row, i++, dat.dtTrsSmry1);
			setCellValue(row, i++, dat.dtTrsSmry2);
			setCellValue(row, i++, dat.amtFc);
			setCellValue(row, i++, dat.sprChrItm1);
			setCellValue(row, i++, dat.sprChrItm2);
			setCellValue(row, i++, dat.sprChrItm3);
			setCellValue(row, i++, dat.sprChrItm4);
			setCellValue(row, i++, dat.sprChrItm5);
			setCellValue(row, i++, dat.sprChrItm6);
			setCellValue(row, i++, dat.sprChrItm7);
			setCellValue(row, i++, dat.sprChrItm8);
			setCellValue(row, i++, dat.sprNmrItm1);
			setCellValue(row, i++, dat.sprNmrItm2);
			setCellValue(row, i++, dat.sprNmrItm3);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

	/**
	 * SS-AP送信情報書き込み処理
	 * @param sheet
	 * @param sheetSSAPSndInfDt
	 * @param styles
	 */
	public void writeSSAPSndInfDt(Sheet sheet, Ss0010SheetSSAPSndInfDt sheetSSAPSndInfDt, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Ss0010SsapSndInfDt dat : sheetSSAPSndInfDt.ssapSndInfDts) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.ssapSndNo);
			setCellValue(row, i++, dat.ssapSndDtlNo);
			setCellValue(row, i++, dat.rcrdTp);
			setCellValue(row, i++, dat.ssCompanyCd);
			setCellValue(row, i++, dat.slpGrp);
			setCellValue(row, i++, dat.slpNo);
			setCellValue(row, i++, dat.slpDt);
			setCellValue(row, i++, dat.dtlLnno);
			setCellValue(row, i++, dat.apAccTp);
			setCellValue(row, i++, dat.accCd);
			setCellValue(row, i++, dat.accBrkdwnCd);
			setCellValue(row, i++, dat.dptCd);
			setCellValue(row, i++, dat.fncCd1);
			setCellValue(row, i++, dat.fncCd2);
			setCellValue(row, i++, dat.fncCd3);
			setCellValue(row, i++, dat.fncCd4);
			setCellValue(row, i++, dat.prjCd);
			setCellValue(row, i++, dat.splrPrsTp);
			setCellValue(row, i++, dat.splrPrsCd);
			setCellValue(row, i++, dat.jrnAmt);
			setCellValue(row, i++, dat.amtExctax);
			setCellValue(row, i++, dat.taxAmt);
			setCellValue(row, i++, dat.taxCd);
			setCellValue(row, i++, dat.taxIptTp);
			setCellValue(row, i++, dat.smry1);
			setCellValue(row, i++, dat.smry2);
			setCellValue(row, i++, dat.trdMnyAmt);
			setCellValue(row, i++, dat.sprChrItm1);
			setCellValue(row, i++, dat.sprChrItm2);
			setCellValue(row, i++, dat.sprChrItm3);
			setCellValue(row, i++, dat.sprChrItm4);
			setCellValue(row, i++, dat.sprChrItm5);
			setCellValue(row, i++, dat.sprChrItm6);
			setCellValue(row, i++, dat.sprChrItm7);
			setCellValue(row, i++, dat.sprChrItm8);
			setCellValue(row, i++, dat.sprNmrItm1);
			setCellValue(row, i++, dat.sprNmrItm2);
			setCellValue(row, i++, dat.sprNmrItm3);
			setCellValue(row, i++, dat.dltFg);
			setCellValue(row, i++, dat.corporationCodeCreated);
			setCellValue(row, i++, dat.userCodeCreated);
			setCellValue(row, i++, dat.ipCreated);
			setCellValue(row, i++, dat.timestampCreated);
			setCellValue(row, i++, dat.corporationCodeUpdated);
			setCellValue(row, i++, dat.userCodeUpdated);
			setCellValue(row, i++, dat.ipUpdated);
			setCellValue(row, i++, dat.timestampUpdated);
		}
	}

}
