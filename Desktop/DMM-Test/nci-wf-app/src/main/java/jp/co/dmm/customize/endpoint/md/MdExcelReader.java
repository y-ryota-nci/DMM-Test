package jp.co.dmm.customize.endpoint.md;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.StreamingReader.Builder;

import jp.co.nci.iwf.util.PoiUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 取引先情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MdExcelReader extends PoiUtils {

	/** EXCELファイルを読み込んで、ブックを生成 */
	public MdExcelBook parse(InputStream stream) throws IOException {
		// ストリーミングとしてEXCELを読み込む
		final Builder builder = StreamingReader.builder()
				.rowCacheSize(100)
				.bufferSize(4096);
		try (Workbook workbook = builder.open(stream)) {
			final MdExcelBook book = new MdExcelBook();
			for (Sheet sheet : workbook) {
				String name = sheet.getSheetName();
				if (eq("取引先マスタ", name))
					book.sheetSplr = readSheetSplr(sheet);
				else if (eq("振込先マスタ", name))
					book.sheetAcc = readSheetAcc(sheet);
			}
			// 関係先マスタ、反社情報
			final Set<String> splrSet = book.sheetSplr.splrs.stream().map(s -> String.format("%s_%s", s.companyCd, s.splrCd)).collect(Collectors.toSet());
			for (Sheet sheet : workbook) {
				String name = sheet.getSheetName();
				if (eq("関係先マスタ", name))
					book.sheetRlt = readSheetRlt(splrSet, sheet);
				else if (eq("反社情報", name))
					book.sheetOrg = readSheetOrg(splrSet, sheet);
			}
			return book;
		}
	}

	/** 取引先マスタシートの読み込み */
	private MdExcelSheetSplr readSheetSplr(Sheet sheet) {
	    final MdExcelSheetSplr sheetSplr = new MdExcelSheetSplr();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MdExcelSplrEntity splr = new MdExcelSplrEntity();
			int i = 0;

			// 処理区分
			splr.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(splr.processType)) {
				// 会社コード
				splr.companyCd = companyCd;
				// 取引先コード
				splr.splrCd = getCellStringValue(row, i++);
				// 適格請求書発行事業者
				splr.cmptEtrNo = getCellStringValue(row, i++);
				// 取引先名称（漢字）
				splr.splrNmKj = getCellStringValue(row, i++);
				// 取引先名称（カタカナ）
				splr.splrNmKn = getCellStringValue(row, i++);
				// 取引先名称（略称）
				splr.splrNmS = getCellStringValue(row, i++);
				// 取引先名称（英名）
				splr.splrNmE = getCellStringValue(row, i++);
				// 法人・個人区分
				splr.crpPrsTp = getCellStringValue(row, i++);
				// 国内・海外区分
				splr.dmsAbrTp = getCellStringValue(row, i++);
				// 国コード
				splr.lndCd = getCellStringValue(row, i++);
				// 国名
				splr.lndNm = getCellStringValue(row, i++);
				// 法人番号
				splr.crpNo = getCellStringValue(row, i++);
				// 郵便番号
				splr.zipCd = getCellStringValue(row, i++);
				// 住所（都道府県）コード
				splr.adrPrfCd = getCellStringValue(row, i++);
				// 住所（都道府県）
				splr.adrPrf = getCellStringValue(row, i++);
				// 住所（市区町村）
				splr.adr1 = getCellStringValue(row, i++);
				// 住所（町名番地）
				splr.adr2 = getCellStringValue(row, i++);
				// 住所（建物名）
				splr.adr3 = getCellStringValue(row, i++);
				// 電話番号
				splr.telNo = getCellStringValue(row, i++);
				// FAX番号
				splr.faxNo = getCellStringValue(row, i++);
				// 関係会社区分
				splr.affcmpTp = getCellStringValue(row, i++);
				// 生年月日
				if(getCellType(row , i) == CellType.NUMERIC) {
					splr.brthDt = getCellDateValue(row, i++);
				}else{
					String strBrthDt = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strBrthDt)) {
						splr.brthDt = toDate(strBrthDt, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						splr.strBrthDt = strBrthDt;
					}
				}
				// 取引状況区分
				splr.trdStsTp = getCellStringValue(row, i++);
				// 有効期間（開始）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					splr.vdDtS = getCellDateValue(row, i++);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						splr.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						splr.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					splr.vdDtE = getCellDateValue(row, i++);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						splr.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						splr.strVdDtE = strVdDtE;
					}
				}
				// 備考
				splr.rmk = getCellStringValue(row, i++);
				// 部門コード
				splr.bumonCd = getCellStringValue(row, i++);
				// 最終判定区分
				splr.lastJdgTp = getCellStringValue(row, i++);
				// 最終判定備考
				splr.lastJdgRmk = getCellStringValue(row, i++);

				sheetSplr.splrs.add(splr);
			}
		}
		return sheetSplr;
	}

	/** 振込先マスタシートの読み込み */
	private MdExcelSheetPayeeBnkacc readSheetAcc(Sheet sheet) {
	    final MdExcelSheetPayeeBnkacc sheetPayeeBnkacc = new MdExcelSheetPayeeBnkacc();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MdExcelSplrAccEntity splrAcc = new MdExcelSplrAccEntity();

			int i = 0;

			// 処理区分
			splrAcc.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(splrAcc.processType)) {
				// 会社コード
				splrAcc.companyCd = companyCd;
				// 振込先銀行口座コード
				splrAcc.payeeBnkaccCd = getCellStringValue(row, i++);
				// 仕入先社員区分
				splrAcc.buyeeStfTp = getCellStringValue(row, i++);
				// 取引先コード
				splrAcc.splrCd = getCellStringValue(row, i++);
				// 銀行コード
				splrAcc.bnkCd = getCellStringValue(row, i++);
				// 銀行支店コード
				splrAcc.bnkbrcCd = getCellStringValue(row, i++);
				// 銀行口座種別
				splrAcc.bnkaccTp = getCellStringValue(row, i++);
				// 銀行口座番号
				splrAcc.bnkaccNo = getCellStringValue(row, i++);
				// 銀行口座名称
				splrAcc.bnkaccNm = getCellStringValue(row, i++);
				// 銀行口座名称（カタカナ）
				splrAcc.bnkaccNmKn = getCellStringValue(row, i++);
				// 振込手数料負担区分
				splrAcc.payCmmOblTp = getCellStringValue(row, i++);
				// 休日処理区分
				splrAcc.hldTrtTp = getCellStringValue(row, i++);
				// 有効期間（開始）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					splrAcc.vdDtS = getCellDateValue(row, i++);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						splrAcc.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						splrAcc.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					splrAcc.vdDtE = getCellDateValue(row, i++);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						splrAcc.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						splrAcc.strVdDtE = strVdDtE;
					}
				}
				// 振込元銀行口座コード
				splrAcc.srcBnkaccCd = getCellStringValue(row, i++);
				// 振込先銀行口座コード(SuperStream)
				splrAcc.payeeBnkaccCdSs = getCellStringValue(row, i++);
				// 備考
				splrAcc.rmk = getCellStringValue(row, i++);

				sheetPayeeBnkacc.accs.add(splrAcc);
			}
		}
		return sheetPayeeBnkacc;
	}

	/** 関係先マスタシートの読み込み */
	private MdExcelSheetRltPrt readSheetRlt(Set<String> splrSet, Sheet sheet) {
		final MdExcelSheetRltPrt sheetRltPrt = new MdExcelSheetRltPrt();
		int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MdExcelRltPrtEntity rltPrt = new MdExcelRltPrtEntity();

			int i = 0;

			// 企業コードが未入力なら終わり
			final String companyCd = getCellStringValue(row, i++);
			final String splrCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd) || isEmpty(splrCd)) {
				break;
			}

			// 取引先マスタが更新対象じゃない場合、更新処理を行わない
			if (!splrSet.contains(String.format("%s_%s", companyCd, splrCd))) {
				continue;
			}

			// 会社コード
			rltPrt.companyCd = companyCd;
			// 取引先コード
			rltPrt.splrCd = splrCd;
			// 連番
			rltPrt.sqno = getCellStringValue(row, i++);
			// 関係先名
			rltPrt.rltPrtNm = getCellStringValue(row, i++);
			// 法人・個人区分
			rltPrt.crpPrsTp = getCellStringValue(row, i++);
			// 国コード
			rltPrt.lndCd = getCellStringValue(row, i++);
			// 生年月日
			if(getCellType(row , i) == CellType.NUMERIC) {
				rltPrt.brthDt = getCellDateValue(row, i++);
			}else{
				String strBrthDt = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strBrthDt)) {
					rltPrt.brthDt = toDate(strBrthDt, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					rltPrt.strBrthDt = strBrthDt;
				}
			}
			// 一致件数
			rltPrt.mtchCnt = getCellStringValue(row, i++);
			// 一致プロファイルID
			rltPrt.mtchPeid = getCellStringValue(row, i++);
			// 判定
			rltPrt.jdgTp = getCellStringValue(row, i++);
			// コメント
			rltPrt.rltPrtRmk = getCellStringValue(row, i++);
			sheetRltPrt.rlts.add(rltPrt);
		}
		return sheetRltPrt;
	}

	/** 反社情報シートの読み込み */
	private MdExcelSheetOrgCrm readSheetOrg(Set<String> splrSet, Sheet sheet) {
		final MdExcelSheetOrgCrm sheetOrgCrm = new MdExcelSheetOrgCrm();
		int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MdExcelOrgCrmEntity orgCrm = new MdExcelOrgCrmEntity();

			int i = 0;

			// 企業コードが未入力なら終わり
			final String companyCd = getCellStringValue(row, i++);
			final String splrCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd) || isEmpty(splrCd)) {
				break;
			}

			// 取引先マスタが更新対象じゃない場合、更新処理を行わない
			if (!splrSet.contains(String.format("%s_%s", companyCd, splrCd))) {
				continue;
			}

			// 会社コード
			orgCrm.companyCd = companyCd;
			// 取引先コード
			orgCrm.splrCd = splrCd;
			// 連番
			orgCrm.sqno = getCellStringValue(row, i++);
			// プロファイルID
			orgCrm.peid = getCellStringValue(row, i++);
			// 一致名称
			orgCrm.mtchNm = getCellStringValue(row, i++);
			// 国コード
			orgCrm.lndCd = getCellStringValue(row, i++);
			// 性別
			orgCrm.gndTp = getCellStringValue(row, i++);
			// 生年月日
			if(getCellType(row , i) == CellType.NUMERIC) {
				orgCrm.brthDt = toStr(getCellDateValue(row, i++));
			}else{
				orgCrm.brthDt = getCellStringValue(row, i++);
			}
			sheetOrgCrm.orgs.add(orgCrm);
		}
		return sheetOrgCrm;
	}

	// 2019/11/05 Excelアップロードバグの対応
	// 直接取得しようとするとセルが存在しない場合にNullPointerExceptionを起こすため
	// セルが存在しない場合はnullを返す
	protected CellType getCellType(Row row , int i) {
		Cell cell = row.getCell(i);
		// セルがあるならセルタイプをセルがないならnullを返す
		return cell != null ? cell.getCellTypeEnum() : null;
	}
}
