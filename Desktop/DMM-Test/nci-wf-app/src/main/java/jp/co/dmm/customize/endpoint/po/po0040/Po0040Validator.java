package jp.co.dmm.customize.endpoint.po.po0040;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.TaxFg;
import jp.co.dmm.customize.component.DmmCodeBook.TaxUnt;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * kintoneデータアップロードのバリデーター.
 */
@ApplicationScoped
public class Po0040Validator extends MiscUtils {

	@Inject private I18nService i18n;
	@Inject private SessionHolder sessionHolder;
	@Inject private Po0040Repository repository;

	/**
	 * バリデーション実行
	 * @param book
	 * @return
	 */
	public boolean validate(Po0040Book book) {
		int errors = 0;
		errors += validateKntnInf(book);

		return errors == 0;
	}

	/** kintoneデータのバリデーション */
	private int validateKntnInf(Po0040Book book) {
		// kintoneデータを伝票の集約単位である「開催日(年月)」＋「取引先コード」にてMap化
		final Map<String, List<Po0040KntnInf>> map = book.kntnInfs.stream().collect(Collectors.groupingBy(b -> toVoucherKey(b)));

		int allErrors = 0;
		for (String key : map.keySet()) {
			final List<Po0040KntnInf> list = map.get(key);
			final int len = list.size();
			Po0040KntnInf bean;
			for (int i = 0; i < len; i++) {
				bean = list.get(i);

				// データ行の単項目単位のバリデーション
				final List<String> errors = validateBase(book, bean);

				// 取引先マスタおよび振込先銀行口座マスタの存在チェック
				errors.addAll( validateMasterExists(book, bean) );

				// 取込データ間での相関チェック
				errors.addAll( validateCorrelation(book, bean) );

				// エラー結果をbeanに書き戻す
				bean.errorText = String.join(" ", errors);

				allErrors += errors.size();
			}
		}

		if (allErrors == 0) {
			// kintoneデータを明細の集約単位である「開催日(年月)」＋「取引先コード」＋「イベント管理No」＋「費目コード(1)」＋「費目コード(2)」にてMap化
			final Map<String, List<Po0040KntnInf>> detailMap = book.kntnInfs.stream().collect(Collectors.groupingBy(b -> toDetailKey(b)));
			for (String key : detailMap.keySet()) {
				final List<Po0040KntnInf> list = detailMap.get(key);
				// 明細行の合計金額が0円以下であればエラーとする
//				final Double sum = list.stream().mapToDouble(d -> toDouble(d.invAmt) + toDouble(d.trnspExpAmt) + toDouble(d.adjTrnspExpAmt)).sum();
				final Long sum = list.stream().mapToLong(d -> d.invAmt + d.trnspExpAmt + d.adjTrnspExpAmt).sum();
				if (compareTo(sum, 0L) <= 0) {
					list.get(0).errorText = "開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎の合計金額が0円以下です。";
					allErrors++;
				}
			}
		}

		return allErrors;
	}

	/** kintoneデータを「開催日(年月)」＋「取引先コード」にてキー文字列化 */
	private String toVoucherKey(Po0040KntnInf data) {
		return join(getExhbYm(data.exhbDt), "-", data.splrCd);
	}

	/** kintoneデータを「開催日(年月)」＋「取引先コード」＋「イベント管理No」＋「費目コード(1)」＋「費目コード(2)」にてキー文字列化 */
	private String toDetailKey(Po0040KntnInf data) {
		return join(getExhbYm(data.exhbDt), "-", data.splrCd, "-", data.evntMngNo, "-", data.itmExpsCd1, "-", data.itmExpsCd2);
	}

	/** kintoneデータから費目コード(1)と費目コード(2)の組み合わせキー文字列化 */
	private String toHimokuKey(Po0040KntnInf data) {
		return join(data.itmExpsCd1, "-", data.itmExpsCd2);
	}

	/** データ行の単項目単位のバリデーション. */
	private List<String> validateBase(Po0040Book book, Po0040KntnInf bean) {
		final FieldValidators fields = new FieldValidators(i18n);

		// レコード番号
		fields.addField(bean.recNo, "レコード番号").required().alphaNumeric(10).numeric();
		// イベントNO
		fields.addField(bean.evntNo, "イベントNo").alphaNumericSymbol(10);
		// ステータス
		fields.addField(bean.kntnSts, "ステータス").required().include(book.kntnSts);
		// 開催日
		fields.addField(bean.exhbDt, "開催日").required().date();
		// イベント管理No
		fields.addField(bean.evntMngNo, "イベント管理No").required().alphaNumeric(3);
		// イベント内容
		fields.addField(bean.evntCont, "イベント内容").anyString(100);
		// kintoneホールID
		fields.addField(bean.kntnHllId, "kintoneホールID").alphaNumeric(10);
		// ホール名
		fields.addField(bean.hllNm, "ホール名").anyString(100);
		// プロダクションID
		fields.addField(bean.prdctId, "プロダクションID").required().alphaNumeric(10);
		// プロダクション名
		fields.addField(bean.prdctNm, "プロダクション名").required().anyString(100);
		// タレント名
		fields.addField(bean.tlntNm, "タレント名").anyString(100);
		// 基本金額
		fields.addField(bean.baseAmt, "基本金額").numeric().range(-99999999999L, 999999999999L);
		// 調整額（基本金額）
		fields.addField(bean.adjBaseAmt, "調整額（基本金額）").numeric().range(-99999999999L, 999999999999L);
		// 交通費
		fields.addField(bean.trnspExpAmt, "交通費").numeric().range(-99999999999L, 999999999999L);
		// 調整額（交通費）
		fields.addField(bean.adjTrnspExpAmt, "調整額（交通費）").numeric().range(-99999999999L, 999999999999L);
		// 原稿作成費
		fields.addField(bean.mnscrExpAmt, "原稿作成費").numeric().range(-99999999999L, 999999999999L);
		// 請求額
		fields.addField(bean.invAmt, "請求額").numeric().range(-99999999999L, 999999999999L);
		// 取引先コード
		fields.addField(bean.splrCd, "取引先コード").required().alphaNumeric(12);
		// 部門コード
		fields.addField(bean.bumonCd, "部門コード").required().master(book.bumonCds);
		// 分析コード
		fields.addField(bean.anlysCd, "分析コード").alphaNumeric(20);
		// 概要
		fields.addField(bean.smry, "概要").anyString(300);
		// 消費税
		fields.addField(bean.taxFgChg, "消費税").required();
		// 税処理単位
		fields.addField(bean.taxUnt, "税処理単位").required().include(book.taxUnts);
		// 費目コード(1)
		fields.addField(bean.itmExpsCd1, "費目コード(1)").required().master(book.itmExpsCds);
		// 費目コード(2)
		fields.addField(bean.itmExpsCd2, "費目コード(2)").required().master(book.itmExpsCds);

		final List<String> errors = fields.validate();
		if (errors == null || errors.isEmpty()) {
			// 費目のチェック
			// kintone連携においては「1500:仮払消費税」はエラー
			if (book.accCdTaxSet.contains(toHimokuKey(bean))) {
				errors.add("仮払消費税のデータは取込対象外です。");
			}
		}
		return errors;
	}

	/** 取引先、振込先口座マスタおよび費目関連2マスタの存在チェック. */
	private List<String> validateMasterExists(Po0040Book book, Po0040KntnInf bean) {
		final List<String> errors = new ArrayList<>();
		// 取引先マスタおよび振込先銀行口座マスタの存在チェック
		// まずはキャッシュから取得する
		// 見つからない場合はDBに問い合わせて存在チェックを行う
		// データが見つかった場合、キャッシュに追加
		if (isNotEmpty(bean.splrCd)) {
			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			// 取引先マスタの存在チェック
			if (!book.splrCds.contains(bean.splrCd)) {
				final int count = repository.countSplrMst(corporationCode, bean.splrCd);
				if (count == 0) {
					errors.add("有効な取引先情報が存在しません。");
				} else if (count > 1) {
					errors.add("有効な取引先情報が複数存在します。");
				} else {
					book.splrCds.add(bean.splrCd);
				}
			}
			// 振込先銀行口座マスタの存在チェック
			if (!book.payeeBnkaccCds.contains(bean.splrCd)) {
				final Date exhbDt = toDate(bean.exhbDt);
				if (exhbDt != null) {
					// 振込先銀行口座マスタは有効開始日≦開催日の翌月月末日(＝支払予定日)≦有効終了日で検索する
					final List<PayeeBnkaccMst> mstList = repository.getPayeeBnkaccMst(corporationCode, bean.splrCd, exhbDt);
					if (mstList == null || mstList.isEmpty()) {
						errors.add("有効な振込先銀行口座情報が存在しません。");
					} else if (!mstList.stream().anyMatch(e -> isNotEmpty(e.getPayeeBnkaccCdSs()))) {
						errors.add("振込先銀行口座コード(SuperStream)が設定されていません。");
					} else {
						book.payeeBnkaccCds.add(bean.splrCd);
					}
				}
			}
		}
		// 費目コード(1)と費目コード(2)の組み合わせによる費目関連2マスタの存在チェック
		// 費目コード(1)と費目コード(2)の組み合わせによる課税対象区分の存在チェック
		// 部門コードに対する消費税種類コードの存在チェック
		// 課税対象区分、消費税種類コード、消費税フラグの組み合わせによる整合性チェック
		if (isNotEmpty(bean.itmExpsCd1) && isNotEmpty(bean.itmExpsCd2) && isNotEmpty(bean.bumonCd)) {
			String key1 = String.join("-", bean.itmExpsCd1, bean.itmExpsCd2);
			if (!book.itmExpsChrCd.contains(key1)) {
				errors.add("費目関連2マスタに費目コード(1)と費目コード(2)の組み合わせは存在しません。");
			}
			if (!book.taxSbjTpMap.containsKey(key1)) {
				errors.add("費目関連1マスタに費目コード(1)と費目コード(2)の組み合わせによる課税対象区分が未設定です。");
			}
			if (!book.taxKndCdMap.containsKey(bean.bumonCd)) {
				errors.add("部門コードに対して消費税種類コードが未設定です。");
			}
			String taxSbjTp = book.taxSbjTpMap.get(key1);
			String taxKndCd = book.taxKndCdMap.get(bean.bumonCd);
			String taxFgChg = bean.taxFgChg;
			if (isNotEmpty(taxSbjTp) && isNotEmpty(taxKndCd) && isNotEmpty(taxFgChg)) {
				String key2 = String.join("-", taxSbjTp, taxKndCd, taxFgChg);
				if (!book.taxFgChgMap.containsKey(key2)) {
					errors.add("課税対象区分、消費税種類コード、消費税フラグでの組み合わせが存在しません。");
				} else {
					VTaxFgChg e = book.taxFgChgMap.get(key2);
					// 開催日における消費税の有効期間内かどうかのチェック
					final Date exhbDt = toDate(bean.exhbDt);
					if (!between(exhbDt, e.getVdDtS(), e.getVdDtE())) {
						errors.add("開催日における有効な消費税マスタが存在しません。");
					} else {
						bean.taxSbjTp = taxSbjTp;
						bean.taxKndCd = taxKndCd;
						bean.taxCd = e.getTaxCd();
						bean.taxFg = getTaxFg(e.getTaxTp());
					}
				}
			}
		}
		return errors;
	}

	/** 取込データ内での相関チェック. */
	private List<String> validateCorrelation(Po0040Book book, Po0040KntnInf bean) {
		final List<String> errors = new ArrayList<>();
		final String exhbYm = getExhbYm(bean.exhbDt);
		final String splrCd = bean.splrCd;
		final String evntMngNo = bean.evntMngNo;
		final String itmExpsCd1 = bean.itmExpsCd1;
		final String itmExpsCd2 = bean.itmExpsCd2;
		if (isNotEmpty(exhbYm) && isNotEmpty(splrCd) && isNotEmpty(evntMngNo) && isNotEmpty(itmExpsCd1) && isNotEmpty(itmExpsCd2)) {
			// 開催日(年月) + 取引先コードでキーを生成
			final String key1 = exhbYm + "-" + splrCd;
			// 生成したキーに対してデータが存在した場合、下記の同一性をチェック
			if (book.kntnDataMap1.containsKey(key1)) {
				final Po0040KntnInf org = book.kntnDataMap1.get(key1);
				// 消費税処理単位
				if (!eq(org.taxUnt, bean.taxUnt)) {
					errors.add("開催日(年月)と取引先毎では消費税処理単位は統一してください。");
				}
//				// 2019/09/25 軽減税率対応により消費税処理単位は"2:伝票単位"で設定されることはなくなったはず
//				// そのため下記チェック処理はコメントアウトとする
//				// 消費税処理単位が"2:伝票単位"の場合、消費税コードは同一
//				else if (eq(TaxUnt.VOUCHER, bean.taxUnt)) {
//					// 消費税コード
//					if (!eq(org.taxCd, bean.taxCd)) {
//						errors.add("消費税処理単位が「2:伝票単位」の場合、消費税コードは統一してください。");
//					}
//				}
				// プロダクション名の同一性チェック
				if (!eq(org.prdctNm, bean.prdctNm)) {
					errors.add("開催日(年月)と取引先毎ではプロダクション名は統一してください。");
				}

			} else {
				book.kntnDataMap1.put(key1, bean);
			}

			// 開催日(年月) + 取引先コード + イベント管理No + 費目コード(1) + 費目コード(2)でキーを生成
			final String key2 = exhbYm + "-" + splrCd + "-" + evntMngNo + "-" + itmExpsCd1 + "-" + itmExpsCd2;
			// 生成したキーに対してデータが存在した場合、下記の同一性をチェック
			if (book.kntnDataMap2.containsKey(key2)) {
				final Po0040KntnInf org = book.kntnDataMap2.get(key2);
				// イベント内容
				if (!eq(org.evntCont, bean.evntCont)) {
					errors.add("開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎ではイベント内容は統一してください。");
				}
				// プロダクション名称
				if (!eq(org.prdctNm, bean.prdctNm)) {
					errors.add("開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎ではプロダクション名称は統一してください。");
				}
				// 部門コード
				if (!eq(org.bumonCd, bean.bumonCd)) {
					errors.add("開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎では部門コードは統一してください。");
				}
				// 分析コード
				if (!eq(org.anlysCd, bean.anlysCd)) {
					errors.add("開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎では分析コードは統一してください。");
				}
				// 概要
				if (!eq(org.smry, bean.smry)) {
					errors.add("開催日(年月)、取引先、イベント管理No、費目コード(1)、費目コード(2)毎では概要は統一してください。");
				}
				// 消費税処理単位が"1:明細単位"の場合、消費税コード・消費税フラグは同一
				if (eq(TaxUnt.DETAIL, bean.taxUnt)) {
//					// 消費税コード
//					if (!eq(org.taxCd, bean.taxCd)) {
//						errors.add("消費税処理単位が「1:明細単位」の場合、消費税コードは統一してください。");
//					}
					// 消費税
					if (!eq(org.taxFgChg, bean.taxFgChg)) {
						errors.add("消費税処理単位が「1:明細単位」の場合、消費税は統一してください。");
					}
				}

			} else {
				book.kntnDataMap2.put(key2, bean);
				final Po0040KntnInf org = book.kntnDataMap1.get(key1);
				// 同一伝票内の明細であれば「伝票グループ(GL)」と「経費区分」は混在してはいけない
				final String key3 = String.join("-", org.itmExpsCd1, org.itmExpsCd2);
				final String key4 = String.join("-", bean.itmExpsCd1, bean.itmExpsCd2);
				// 伝票グループの混在チェック
				if (book.slpGrpGlMap.containsKey(key3) && book.slpGrpGlMap.containsKey(key4) && !eq(book.slpGrpGlMap.get(key3), book.slpGrpGlMap.get(key4))) {
					errors.add("伝票グループ(GL)が混在しています。");
				}
				// 経費区分の混在チェック
				if (book.cstTpMap.containsKey(key3) && book.cstTpMap.containsKey(key4) && !eq(book.cstTpMap.get(key3), book.cstTpMap.get(key4))) {
					errors.add("経費区分が混在しています。");
				}
			}
		}
		return errors;
	}

	private String getExhbYm(String exhbDt) {
		if (ValidatorUtils.isYMD(exhbDt)) {
			return toStr(toDate(exhbDt, MiscUtils.FORMAT_DATE), "yyMM");
		}
		return null;
	}

	private Date toDate(String exhbDt) {
		if (ValidatorUtils.isYMD(exhbDt)) {
			return MiscUtils.toDate(exhbDt, MiscUtils.FORMAT_DATE);
		}
		return null;
	}

	private boolean between(Date date, Date from, Date to) {
		if (date == null) {
			return true;
		}
		return overlap(from, to, date, date);
	}

	private String getTaxFg(String taxTp) {
		switch (taxTp) {
		case "1":
			return TaxFg.OUT_TAX;
		case "2":
			return TaxFg.IN_TAX;
		default:
			return TaxFg.NONE;
		}
	}
}
