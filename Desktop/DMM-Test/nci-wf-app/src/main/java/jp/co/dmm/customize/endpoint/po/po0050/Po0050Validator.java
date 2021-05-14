package jp.co.dmm.customize.endpoint.po.po0050;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.TaxFg;
import jp.co.dmm.customize.component.DmmCodeBook.TaxFgChg;
import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 通販データアップロードのバリデーター.
 */
@ApplicationScoped
public class Po0050Validator extends MiscUtils {

	@Inject private I18nService i18n;
	@Inject private SessionHolder sessionHolder;
	@Inject private Po0050Repository repository;

	/**
	 * バリデーション実行
	 * @param book
	 * @return
	 */
	public boolean validate(Po0050Book book) {
		int errors = 0;
		errors += validateMlordInf(book);

		return errors == 0;
	}

	/** 通販データのバリデーション */
	private int validateMlordInf(Po0050Book book) {
		final String inYm = toStr(today(), "yyyyMM");
		// 通販データを伝票の集約単位である「取込日(年月)」＋「取引先コード」にてMap化
		final Map<String, List<Po0050MlordInf>> map = book.mlordInfs.stream().collect(Collectors.groupingBy(b -> toVoucherKey(inYm, b)));

		int allErrors = 0;
		for (String key : map.keySet()) {
			final List<Po0050MlordInf> list = map.get(key);
			final int len = list.size();
			Po0050MlordInf bean;
			// 同一伝票内における(消費税行を除く)明細行の消費税フラグ一覧
			final Set<String> taxFgChg1 = list.stream().filter(b -> !book.accCdTaxSet.contains(toHimokuKey(b))).map(b -> b.taxFgChg).collect(Collectors.toSet());
			// 同一伝票内における消費税行の消費税フラグ一覧
			final Set<String> taxFgChg2 = list.stream().filter(b -> book.accCdTaxSet.contains(toHimokuKey(b))).map(b -> b.taxFgChg).collect(Collectors.toSet());
			for (int i = 0; i < len; i++) {
				bean = list.get(i);

				// データ行の単項目単位のバリデーション
				final List<String> errors = validateBase(book, bean);

				// 取引先マスタおよび振込先銀行口座マスタの存在チェック
				errors.addAll( validateMasterExists(book, bean) );

				// 取込データ間での相関チェック
				errors.addAll( validateCorrelation(book, bean) );

				// 1行目のデータであれば「消費税」のデータ行の存在と明細レコードの関連チェック
				if (i == 0) {
					// 同一伝票内の明細レコードに「消費税」のデータ行があるか
					final boolean isExistsTax = list.stream().anyMatch(b -> book.accCdTaxSet.contains(toHimokuKey(b)));
					// 同一伝票内の明細レコードに課税対象(＝消費税フラグが"4","5","6")となるデータ行があるか
					// なお「消費税」のデータ行は除外
					final boolean isExistsTaxable = list.stream()
															.filter(b -> !book.accCdTaxSet.contains(toHimokuKey(b)))
															.anyMatch(b -> in(b.taxFgChg, TaxFgChg.OUT_TAX_10, TaxFgChg.OUT_TAX_8, TaxFgChg.OUT_TAX_8_OLD));

					// 「消費税」のデータ行がある場合、課税対象となるデータ行が存在すること
					// 「消費税」のデータ行がない場合、課税対象となるデータ行が存在しないこと
					// 上記条件に合致しない場合、エラーとする
					if (isExistsTax && !isExistsTaxable) {
						errors.add("課税対象データ行がない場合は同一伝票内に消費税データ行は不要です。");
					} else if (!isExistsTax && isExistsTaxable) {
						errors.add("課税対象データ行がある場合は同一伝票内に消費税データ行が必要です。");
					}
				}
				// 消費税行の場合
				if (book.accCdTaxSet.contains(toHimokuKey(bean))) {
					// 消費税列が"9：対象外"以外で明細行の消費税フラグ一覧に同じ消費税フラグが存在しなければエラー
					// （消費税行の場合、消費税列の"9：対象外"はvalidateBase内でエラー判定済み）
					if (!eq(TaxFgChg.NONE, bean.taxFgChg) && !taxFgChg1.contains(bean.taxFgChg)) {
						errors.add("消費税列の値に合致する明細行のデータがありません。");
					}
				// 明細行の場合
				} else {
					// 消費税列が"9：対象外"以外で消費税行の消費税フラグ一覧に同じ消費税フラグが存在しなければエラー
					if (!eq(TaxFgChg.NONE, bean.taxFgChg) && !taxFgChg2.contains(bean.taxFgChg)) {
						errors.add("消費税列の値に合致する消費税行のデータがありません。");
					}
				}

				// エラー結果をbeanに書き戻す
				bean.errorText = String.join(" ", errors);

				allErrors += errors.size();
			}
		}

		// データ行毎のエラーがなければ、以下のチェックを実施
		if (allErrors == 0) {
			for (String key : map.keySet()) {
				// 合計金額は全ての明細行の金額の合計（消費税行も含める）
				final List<Po0050MlordInf> list = map.get(key);
				final Long sum = list.stream().mapToLong(d -> d.amt).sum();
				// １申請書における合計金額が0円の場合、取込エラーとする
				if (compareTo(sum, 0L) == 0) {
					list.get(0).errorText = "合計金額が0円です。";
					allErrors++;
				}
			}
		}

		return allErrors;
	}

	/** 通販データを「取込日(年月)」＋「取引先コード」にてキー文字列化 */
	private String toVoucherKey(final String inYm, Po0050MlordInf data) {
		return join(inYm, "-", data.splrCd);
	}

//	/** 通販データを「取込日(年月)」＋「取引先コード」＋「部門コード」＋「費目コード(1)」＋「費目コード(2)」にてキー文字列化 */
//	private String toDetailKey(final String inYm, Po0050MlordInf data) {
//		return join(inYm, "-", data.splrCd, "-", data.bumonCd, "-", data.itmExpsCd1, "-", data.itmExpsCd2);
//	}

	/** 通販データから費目コード(1)と費目コード(2)の組み合わせキー文字列化 */
	private String toHimokuKey(Po0050MlordInf data) {
		return join(data.itmExpsCd1, "-", data.itmExpsCd2);
	}

	/** データ行の単項目単位のバリデーション. */
	private List<String> validateBase(Po0050Book book, Po0050MlordInf bean) {
		final FieldValidators fields = new FieldValidators(i18n);

		// 伝票No
		fields.addField(bean.slpNo, "伝票No").required().alphaNumeric(12);
		// 行NO
		fields.addField(bean.lnNo, "行No").numeric().range(0, 99999);
		// 仕入日
		fields.addField(bean.buyDt, "仕入日").required().date();
		// 仕入先コード
		fields.addField(bean.buyCd, "仕入先コード").alphaNumeric(12);
		// 仕入先名称
		fields.addField(bean.buyNmKj, "仕入先名称").anyString(100);
		// 概要
		fields.addField(bean.abst, "概要").anyString(300);
		// 商品コード
		fields.addField(bean.cmmdtCd, "商品コード").alphaNumeric(20);
		// 品番
		fields.addField(bean.prtNo, "品番").alphaNumericSymbol(30);
		// 商品タイトル
		fields.addField(bean.cmmdtTtl, "商品タイトル").anyString(100);
		// 数量
		fields.addField(bean.qnt, "数量").numeric().range(-999999999L, 9999999999L);
		// 単価
		fields.addField(bean.uc, "単価").numeric().range(-999999999D, 9999999999D);
		// 金額
		// 消費税のデータ行だけはマイナスは許可しない
		// 2019/03/20 modify 消費税もマイナスを許可する
		fields.addField(bean.amt, "金額").required().numeric().range(-999999999L, 9999999999L);
//		if (book.accCdTaxSet.contains(toHimokuKey(bean))) {
//			fields.addField(bean.amt, "金額(消費税)").required().numeric().range(0L, 9999999999L);
//		} else {
//			fields.addField(bean.amt, "金額").required().numeric().range(-999999999L, 9999999999L);
//		}
		// 備考
		fields.addField(bean.rmk, "備考").anyString(300);
		// 取引先コード
		fields.addField(bean.splrCd, "取引先コード").required().alphaNumeric(12);
		// 部門コード
		fields.addField(bean.bumonCd, "部門コード").required().master(book.bumonCds);
		// 消費税
		if (book.accCdTaxSet.contains(toHimokuKey(bean))) {
			fields.addField(bean.taxFgChg, "消費税").required().include(book.taxFgChg2);
		} else {
			fields.addField(bean.taxFgChg, "消費税").required().include(book.taxFgChg1);
		}
		// 税処理単位
		fields.addField(bean.taxUnt, "税処理単位").required().alphaNumeric(1);
		// 費目コード(1)
		fields.addField(bean.itmExpsCd1, "費目コード(1)").required().master(book.itmExpsCds);
		// 費目コード(2)
		fields.addField(bean.itmExpsCd2, "費目コード(2)").required().master(book.itmExpsCds);

		final List<String> errors = fields.validate();
		return errors;
	}

	/** 取引先、振込先口座マスタおよび費目関連2マスタの存在チェック. */
	private List<String> validateMasterExists(Po0050Book book, Po0050MlordInf bean) {
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
				// 振込先銀行口座マスタは有効開始日≦データ取込日の翌月月末日(＝支払予定日)≦有効終了日で検索する
				final List<PayeeBnkaccMst> mstList = repository.getPayeeBnkaccMst(corporationCode, bean.splrCd, today());
				if (mstList == null || mstList.isEmpty()) {
					errors.add("有効な振込先銀行口座情報が存在しません。");
				} else if (!mstList.stream().anyMatch(e -> isNotEmpty(e.getPayeeBnkaccCdSs()))) {
					errors.add("振込先銀行口座コード(SuperStream)が設定されていません。");
				} else {
					book.payeeBnkaccCds.add(bean.splrCd);
				}
			}
		}

		// 消費税行でなければ以下の処理を実施
		if (!book.accCdTaxSet.contains(toHimokuKey(bean))) {
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
						final Date exhbDt = today();
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
		}
		return errors;
	}

	/** 取込データ内での相関チェック. */
	private List<String> validateCorrelation(Po0050Book book, Po0050MlordInf bean) {
		final List<String> errors = new ArrayList<>();
		final String inYm = toStr(today(), "yyyyMM");
		final String splrCd = bean.splrCd;
		final String bumonCd = bean.bumonCd;
		final String itmExpsCd1 = bean.itmExpsCd1;
		final String itmExpsCd2 = bean.itmExpsCd2;
		final String taxCd = bean.taxCd;
		if (isNotEmpty(splrCd) && isNotEmpty(bumonCd) && isNotEmpty(itmExpsCd1) && isNotEmpty(itmExpsCd2)) {
			// 取込日(年月) + 取引先コードでキーを生成
			final String key1 = inYm + "-" + splrCd;
			// 生成したキーに対してデータが存在した場合、下記の同一性をチェック
			if (book.mlordDataMap1.containsKey(key1)) {
//				final Po0050MlordInf org = book.mlordDataMap1.get(key1);
//				// 消費税処理単位
//				if (!eq(org.taxUnt, bean.taxUnt)) {
//					errors.add("取引先毎では消費税処理単位は統一してください。");
//				}
//				// 消費税処理単位が"2:伝票単位"の場合、消費税コードは同一
//				else if (eq(TaxUnt.VOUCHER, bean.taxUnt)) {
//					// 消費税コード
//					if (!eq(org.taxCd, bean.taxCd)) {
//						errors.add("消費税処理単位が「2:伝票単位」の場合、消費税コードは統一してください。");
//					}
//				}
			} else {
				book.mlordDataMap1.put(key1, bean);
			}

			// 取込日(年月) + 取引先コード + 部門コード + 費目コード(1) + 費目コード(2)でキーを生成
			// 2019/03/26 modify
			// 明細の集約単位は[取込日(年月) + 取引先コード + 部門コード + 費目コード(1) + 費目コード(2) + 消費税コード]に変更
//			final String key2 = inYm + "-" + splrCd + "-" + bumonCd + "-" + itmExpsCd1 + "-" + itmExpsCd2;
			final String key2 = inYm + "-" + splrCd + "-" + bumonCd + "-" + itmExpsCd1 + "-" + itmExpsCd2 + "-" + taxCd;
			// 生成したキーに対してデータが存在した場合、下記の同一性をチェック
			if (book.mlordDataMap2.containsKey(key2)) {
//				final Po0050MlordInf org = book.mlordDataMap2.get(key2);
//				// 消費税処理単位が"1:明細単位"の場合、消費税コード・消費税フラグは同一
//				if (eq(TaxUnt.DETAIL, bean.taxUnt)) {
//					// 消費税コード
//					if (!eq(org.taxCd, bean.taxCd)) {
//						errors.add("消費税処理単位が「1:明細単位」の場合、消費税コードは統一してください。");
//					}
//				}
			} else {
				book.mlordDataMap2.put(key2, bean);
				final Po0050MlordInf org = book.mlordDataMap1.get(key1);
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
