package jp.co.dmm.customize.endpoint.md;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.component.DmmCodeBook.DmsAbrTp;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.util.FieldValidators;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 取引先情報アップロードのバリデーター
 */
@ApplicationScoped
public class MdUploadValidator extends MiscUtils {
	@Inject private I18nService i18n;

	private static final Pattern KANA_NAME_PATTERN = Pattern.compile("^[ｦ-ﾟA-Z0-9\\(\\)\\-\\.\\,\\｢\\｣\\/\\\\ &&[^ｧｨｩｪｫｬｭｮｯｰ]]+$");
	private static final Pattern POSTCODE_PATTERN = Pattern.compile("^(\\d{3}-\\d{4}||\\d{7})$");
	private static final Pattern TELFAX_PATTERN = Pattern.compile("^[0-9][0-9-]{1,19}$");
//	private static final String DGHD_COMPANY_CD = "00053";
//	private static final String SPLR_CD_MAX = "209999999";

	private Set<String> splrCdUploadSet = new HashSet<String>();
	private Set<String> rltPrtUploadSet = new HashSet<String>();

	/**
	 * バリデーション実行
	 * @param book
	 * @return
	 */
	public boolean validate(MdExcelBook book) {
		int errors = 0;
		errors += validateSplr(book);
		errors += validatePayeeBnkacc(book);
		rltPrtUploadSet.clear();
		errors += validateRltPrt(book);
		errors += validateOrgCrm(book);

		return errors == 0;
	}

	/** 取引先マスタのバリデーション */
	private int validateSplr(MdExcelBook book) {

		int allErrors = 0;
		for (MdExcelSplrEntity sp : book.sheetSplr.splrs) {
			final FieldValidators fields = new FieldValidators(i18n);
			splrCdUploadSet.add(sp.companyCd + "_" + sp.splrCd);

			// 会社コード
			fields.addField(sp.companyCd, "会社コード")
				.required()
				.master(book.existCompanyCodes);

			// 取引先コード
			if ("A".equals(sp.processType)) {
				fields.addField(sp.splrCd, "取引先コード")
					.required()
					.alphaNumeric(20)
					.duplicate(book.existSplrCodes, sp.companyCd + "_" + sp.splrCd);
			} else if ("U".equals(sp.processType)) {
				fields.addField(sp.splrCd, "取引先コード")
				.required()
				.alphaNumeric(20);
			} else { // C-D

				fields.addField(sp.splrCd, "取引先コード")
				.required()
				.alphaNumeric(20)
				.master(book.existSplrCodes, null, sp.companyCd + "_" + sp.splrCd);
			}

			//その他必須項目
			if (in(sp.processType, "A", "U", "C")) {
				// 適格請求書発行事業者
				fields.addField(sp.cmptEtrNo, "適格請求書発行事業者")
					.alphaNumeric(50);

				// 法人・個人区分
				fields.addField(sp.crpPrsTp, "法人・個人区分")
					.required()
					.include(book.crpPrsTypes);

				// 国内・海外区分
				fields.addField(sp.dmsAbrTp, "国内・海外区分")
					.required()
					.include(book.dmsAbrTypes);

				// 取引先名称（漢字）
				fields.addField(sp.splrNmKj, "取引先名称（漢字）")
					.required()
					.anyString(80);

				// 取引先名称（略称）
				fields.addField(sp.splrNmS, "取引先名称（略称）")
					.required()
					.anyString(20);

				if (DmsAbrTp.DOMESTIC.equals(sp.dmsAbrTp)) {
					// 取引先名称（半角ｶﾅ）
					fields.addField(sp.splrNmKn, "取引先名称（半角ｶﾅ）")
						.required()
						.halfWidthOnly(50);

					// 取引先名称（英名）
					fields.addField(sp.splrNmE, "取引先名称（英名）")
						.alphaNumericSymbol(80);
				} else if (DmsAbrTp.ABROAD.equals(sp.dmsAbrTp)) {
					// 取引先名称（半角ｶﾅ）
					fields.addField(sp.splrNmKn, "取引先名称（半角ｶﾅ）")
						.halfWidthOnly(50);

					// 取引先名称（英名）
					fields.addField(sp.splrNmE, "取引先名称（英名）")
						.required()
						.alphaNumericSymbol(80);

				}

				// 関係会社区分
				fields.addField(sp.affcmpTp, "関係会社区分")
					.required()
					.include(book.affcmpTypes);

				// 生年月日
				if (isEmpty(sp.brthDt)) {
					fields.addField(sp.strBrthDt, "生年月日")
					.date();
				} else {
					fields.addField(sp.brthDt, "生年月日")
					.date();
				}

				// 有効期間（開始）
				fields.addField(sp.vdDtS, "有効期間（開始）")
					.required()
					.date()
					.gte(STARTDATE, MessageCd.validTerm);
				fields.addField(sp.vdDtS, "有効期間（開始）")
					.lte(ENDDATE, MessageCd.validTerm);

				// 有効期間（終了）
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.required()
					.date()
					.gte(sp.vdDtS, MessageCd.validStartDate);
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.gte(STARTDATE, MessageCd.validTerm);
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.lte(ENDDATE, MessageCd.validTerm);

				// 国コード
				fields.addField(sp.lndCd, "国コード")
					.required()
					.alphaNumeric(3)
					.master(book.existLndCds);

				// 国名
				fields.addField(sp.lndNm, "国名")
					.required()
					.anyString(100);

				// 法人番号
				fields.addField(sp.crpNo, "法人番号")
					.numeric()
					.anyString(13);

				// 郵便番号
				fields.addField(sp.zipCd, "郵便番号")
					.alphaNumericSymbol(8);

				// 住所（都道府県）コード
				fields.addField(sp.adrPrfCd, "住所（都道府県）コード")
					.include(book.ardPrfTypes);

				// 住所（都道府県）
				fields.addField(sp.adrPrf, "住所（都道府県）")
					.anyString(30);

				// 住所（市区町村）
				fields.addField(sp.adr1, "住所（市区町村）")
					.anyString(60);

				// 住所（都道府県＋市区町村）
				fields.addField(sp.adrPrf + sp.adr1, "住所（都道府県＋市区町村）")
					.anyString(60);

				// 住所（町名番地）
				fields.addField(sp.adr2, "住所（町名番地）")
					.anyString(60);

				// 住所（建物名）
				fields.addField(sp.adr3, "住所（建物名）")
					.anyString(60);

				// 電話番号
				fields.addField(sp.telNo, "電話番号")
					.alphaNumericSymbol(20);

				// FAX番号
				fields.addField(sp.faxNo, "FAX番号")
					.alphaNumericSymbol(20);

				// 取引状況区分
				fields.addField(sp.trdStsTp, "取引状況区分")
					.required()
					.include(book.trdStsTypes);

				// 備考
				fields.addField(sp.rmk, "備考")
					.anyString(300);

				// 部門コード
				fields.addField(sp.bumonCd, "部門コード")
					.alphaNumeric(14)
					.master(book.existBumonCodes, null, sp.companyCd + "_" + sp.bumonCd);

				// 最終判定区分
				fields.addField(sp.lastJdgTp, "最終判定区分")
					.include(book.lastJdgTps);

				fields.addField(sp.lastJdgRmk, "最終判定備考")
					.anyString(300);
			}


			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();

			// エラー結果をbeanに書き戻す
			sp.errorText = String.join(" ", errors);

			allErrors += errors.size();

			// SS連携禁則文字チェック
			String ssCharTypeCheckError = validateNameTypeVal(sp.splrNmKj, "取引先名称（漢字）") + " "
					+ validateNameTypeVal(sp.splrNmS, "取引先名称（略称）") + " "
					+ validateNameTypeVal(sp.splrNmE, "取引先名称（英名）") + " "
					+ validatePostCode(sp.zipCd, "郵便番号") + " "
					+ validateTelFax(sp.telNo, "電話番号") + " "
					+ validateTelFax(sp.faxNo, "FAX番号") + " "
					+ validateRemarkTypeVal(sp.rmk, "備考");
			if (!"".equals(ssCharTypeCheckError.trim())) {
				sp.errorText = sp.errorText + " " + ssCharTypeCheckError.trim();
				allErrors += ssCharTypeCheckError.trim().split(" ").length;
			}
		}
		return allErrors;
	}

	/** 振込先マスタのバリデーション */
	private int validatePayeeBnkacc(MdExcelBook book) {

		int allErrors = 0;
		for (MdExcelSplrAccEntity sp : book.sheetAcc.accs) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 会社コード
			fields.addField(sp.companyCd, "会社コード")
				.required()
				.master(book.existCompanyCodes);

			// 取引先コード
			splrCdUploadSet.addAll(book.existSplrCodes);
			fields.addField(sp.splrCd, "取引先コード")
				.required()
				.alphaNumeric(20)
				.master(splrCdUploadSet, null, sp.companyCd + "_" + sp.splrCd);

			//振込先銀行口座コード(SuperStream)
			if (eq(sp.processType, "A")) {
				fields.addField(sp.payeeBnkaccCdSs, "振込先銀行口座コード(SuperStream)")
					.required()
					.alphaNumeric(4)
					.numeric()
					.duplicate(book.existBnkaccSSCodes, sp.companyCd + "_" + sp.splrCd + "_" + sp.payeeBnkaccCdSs);
			} else if (in(sp.processType, "C", "D")) {
				fields.addField(sp.payeeBnkaccCdSs, "振込先銀行口座コード(SuperStream)")
					.required()
					.alphaNumeric(4)
					.numeric()
					.master(book.existBnkaccSSCodes, null, sp.companyCd + "_" + sp.splrCd + "_" + sp.payeeBnkaccCdSs);
			} else {//U
				fields.addField(sp.payeeBnkaccCdSs, "振込先銀行口座コード(SuperStream)")
					.required()
					.alphaNumeric(4)
					.numeric();
			}

			//その他必須項目
			if (in(sp.processType, "A", "U", "C")) {
				// 銀行コード
				fields.addField(sp.bnkCd, "銀行コード")
					.required()
					.alphaNumeric(4);

				// 銀行支店コード
				fields.addField(sp.bnkbrcCd, "銀行支店コード")
					.required()
					.alphaNumeric(3);

				// 銀行口座種別
				fields.addField(sp.bnkaccTp, "銀行口座種別")
					.required()
					.include(book.bnkaccTypes);

				// 銀行口座番号
				fields.addField(sp.bnkaccNo, "銀行口座番号")
					.required()
					.numeric()
					.anyString(7);

				// 銀行口座名称
				fields.addField(sp.bnkaccNm, "銀行口座名称")
					.required()
					.anyString(40);

				// 銀行口座名称（半角ｶﾅ）
				fields.addField(sp.bnkaccNmKn, "銀行口座名称（半角ｶﾅ）")
					.required()
					.halfWidthOnly(30);

				// 振込手数料負担区分
				fields.addField(sp.payCmmOblTp, "振込手数料負担区分")
					.required()
					.include(book.payCmmOblTypes);

				// 休日処理区分
				fields.addField(sp.hldTrtTp, "休日処理区分")
					.required()
					.include(book.hldTrtTypes);

				// 有効期間（開始）
				fields.addField(sp.vdDtS, "有効期間（開始）")
					.required()
					.date()
					.gte(STARTDATE, MessageCd.validTerm);
				fields.addField(sp.vdDtS, "有効期間（開始）")
					.lte(ENDDATE, MessageCd.validTerm);

				// 有効期間（終了）
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.required()
					.date()
					.gte(sp.vdDtS, MessageCd.validStartDate);
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.gte(STARTDATE, MessageCd.validTerm);
				fields.addField(sp.vdDtE, "有効期間（終了）")
					.lte(ENDDATE, MessageCd.validTerm);

				// 振込元銀行口座コード
				fields.addField(sp.srcBnkaccCd, "振込元銀行口座コード")
					.required()
					.alphaNumeric(4);

				// 仕入先社員区分
				fields.addField(sp.buyeeStfTp, "仕入先社員区分")
					.include(book.buyeeStfTypes);

				// 備考
				fields.addField(sp.rmk, "備考")
					.anyString(80);
			}

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();

			// エラー結果をbeanに書き戻す
			sp.errorText = String.join(" ", errors);
			allErrors += errors.size();

			// SS連携禁則文字チェック
			String ssCharTypeCheckError = validateNameTypeVal(sp.bnkaccNm, "振込先口座名") + " "
					+ validateKanaName(sp.bnkaccNmKn, "振込先口座名（半角ｶﾅ）");
			if (!"".equals(ssCharTypeCheckError.trim())) {
				sp.errorText = sp.errorText + " " + ssCharTypeCheckError.trim();
				allErrors += ssCharTypeCheckError.trim().split(" ").length;
			}

		}
		return allErrors;
	}

	/** 関係先マスタのバリデーション */
	private int validateRltPrt(MdExcelBook book) {

		int allErrors = 0;
		for (MdExcelRltPrtEntity sp : book.sheetRlt.rlts) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 会社コード
			fields.addField(sp.companyCd, "会社コード")
				.required()
				.master(book.existCompanyCodes);

			// 取引先コード
			splrCdUploadSet.addAll(book.existSplrCodes);
			fields.addField(sp.splrCd, "取引先コード")
				.required()
				.alphaNumeric(20)
				.master(splrCdUploadSet, null, sp.companyCd + "_" + sp.splrCd);

			// 連番
			final String sqnoErrors = fields.addField(sp.sqno, "連番")
				.required()
				.integer().validate();

			if (isEmpty(sqnoErrors)) {
				fields.addField(Long.valueOf(sp.sqno), "連番")
				.range(Long.valueOf("0"), Long.valueOf("999"));
			}

			// 関係先名
			fields.addField(sp.rltPrtNm, "関係先名")
				.required()
				.anyByteString(100);

			// 法人・個人区分
			fields.addField(sp.crpPrsTp, "法人・個人区分")
				.required()
				.include(book.crpPrsTypes);

			// 国コード
			fields.addField(sp.lndCd, "国コード")
				.alphaNumeric(3)
				.master(book.existLndCds);

			// 生年月日
			if (isEmpty(sp.brthDt)) {
				fields.addField(sp.strBrthDt, "生年月日")
				.date();
			} else {
				fields.addField(sp.brthDt, "生年月日")
				.date();
			}

			// 一致件数
			final String mtchCntErrors = fields.addField(sp.mtchCnt, "一致件数")
				.integer().validate();

			if (isNotEmpty(sp.mtchCnt) && isEmpty(mtchCntErrors)) {
				fields.addField(Long.valueOf(sp.mtchCnt), "一致件数")
				.range(Long.valueOf("0"), Long.valueOf("9999999"));
			}

			// 一致プロファイルID
			fields.addField(sp.mtchPeid, "一致プロファイルID")
				.alphaNumeric(20);

			// 判定区分
			fields.addField(sp.jdgTp, "判定区分")
				.include(book.jdgTps);

			// コメント
			fields.addField(sp.rltPrtRmk, "コメント")
				.anyByteString(1000);

			final String key = String.format("%s_%s_%s", sp.companyCd, sp.splrCd, sp.sqno);
			boolean exists = rltPrtUploadSet.contains(key);
			if (!exists) {
				rltPrtUploadSet.add(key);
			}

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();
			if (exists) {
				errors.add("連番が重複しています。");
			}

			// エラー結果をbeanに書き戻す
			sp.errorText = String.join(" ", errors);
			allErrors += errors.size();

		}
		return allErrors;
	}

	/** 反社情報のバリデーション */
	private int validateOrgCrm(MdExcelBook book) {
		final Map<String, Integer> counts = new LinkedHashMap<>();
		int allErrors = 0;
		for (MdExcelOrgCrmEntity sp : book.sheetOrg.orgs) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 会社コード
			fields.addField(sp.companyCd, "会社コード")
				.required()
				.master(book.existCompanyCodes);

			// 取引先コード
			splrCdUploadSet.addAll(book.existSplrCodes);
			fields.addField(sp.splrCd, "取引先コード")
				.required()
				.alphaNumeric(20)
				.master(splrCdUploadSet, null, sp.companyCd + "_" + sp.splrCd);

			// 連番
			final String key = String.format("%s_%s_%s", sp.companyCd, sp.splrCd, sp.sqno);
			final String sqnoErros = fields.addField(sp.sqno, "連番")
				.required()
				.integer().validate();
			if (isEmpty(sqnoErros)) {
				fields.addField(sp.sqno, "関係先マスタに連番")
				.master(rltPrtUploadSet, null, key);
			}

			if (!counts.containsKey(key)) {
				counts.put(key, 0);
			}
			counts.put(key, counts.get(key) + 1);

			// プロファイルID
			fields.addField(sp.peid, "プロファイルID")
				.required()
				.alphaNumeric(20);

			// 一致名称
			fields.addField(sp.mtchNm, "一致名称")
				.anyByteString(1000);

			// 国コード
			fields.addField(sp.lndCd, "国コード")
				.alphaNumeric(3)
				.master(book.existLndCds);

			// 性別
			fields.addField(sp.gndTp, "性別")
				.alphaNumeric(10);

			// 生年月日
			fields.addField(sp.brthDt, "生年月日")
				.alphaNumericSymbol(300);

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();
			if (counts.get(key) > 100) {
				errors.add("反社情報は関係先毎に100件以内で入力して下さい。");
			}

			// エラー結果をbeanに書き戻す
			sp.errorText = String.join(" ", errors);
			allErrors += errors.size();

		}
		return allErrors;
	}

	/**
	 * SS連携用カナ名チェック
	 * @param kanaName
	 * @param colName
	 * @return
	 */
	private String validateKanaName(String kanaName, String colName) {
		if (StringUtils.isNotEmpty(kanaName)) {
			if (!KANA_NAME_PATTERN.matcher(kanaName).matches()) {
				return colName + "は、「ｧｨｩｪｫｬｭｮｯｰ」を除く半角ｶﾅ・半角ｱﾙﾌｧﾍﾞｯﾄ(大文字)・半角数字・濁点/半濁点と「(」「)」「-」「.」「 」のみ入力可能です。";
			}

			return "";
		} else {
			return "";
		}
	}

	/**
	 * SS連携用名称チェック
	 * @param nameVal
	 * @param colName
	 * @return
	 */
	private String validateNameTypeVal(String nameVal, String colName) {
		if (StringUtils.isNotEmpty(nameVal)) {
			if (nameVal.contains(",") || nameVal.contains("'") || nameVal.contains("\"")) {
				return colName + "はカンマ「,」、単一引用符「\\'」、引用符「\\\"」が入力不可です。";
			}

			return "";
		} else {
			return "";
		}
	}

	/**
	 * SS連携用摘要チェック
	 * @param nameVal
	 * @param colName
	 * @return
	 */
	private String validateRemarkTypeVal(String nameVal, String colName) {
		if (StringUtils.isNotEmpty(nameVal)) {
			if (nameVal.contains("\"")) {
				return colName + "は引用符「\\\"」が入力不可です。";
			}

			return "";
		} else {
			return "";
		}
	}

	/**
	 * SS連携用郵便番号チェック
	 * @param nameVal
	 * @param colName
	 * @return
	 */
	private String validatePostCode(String nameVal, String colName) {
		if (StringUtils.isNotEmpty(nameVal)) {
			if (!POSTCODE_PATTERN.matcher(nameVal).matches()) {
				return colName + "は数字とハイフン、「数字(3)-数字(4)」か「数字(7)」のパターンのみ入力可能です。";
			}

			return "";
		} else {
			return "";
		}
	}

	/**
	 * SS連携用TEL・FAXチェック
	 * @param nameVal
	 * @param colName
	 * @return
	 */
	private String validateTelFax(String nameVal, String colName) {
		if (StringUtils.isNotEmpty(nameVal)) {
			if (!TELFAX_PATTERN.matcher(nameVal).matches()) {
				return colName + "は数字とハイフンのみ入力可能です。";
			}

			return "";
		} else {
			return "";
		}
	}
}
