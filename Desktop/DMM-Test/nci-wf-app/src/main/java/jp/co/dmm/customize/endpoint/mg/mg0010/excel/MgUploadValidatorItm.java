package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0011.Mg0011Repository;
import jp.co.dmm.customize.endpoint.mg.mg0011.Mg0011UpdateRequest;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 品目情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorItm extends MgUploadValidator<MgExcelBookItm> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0011Repository repository;

	/** 品目マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookItm book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityItm>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));
		for (String key : map.keySet()) {
			final List<MgExcelEntityItm> list = map.get(key);

			for (MgExcelEntityItm entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロードコンテンツ内の整合性チェック
					errors += super.validateInternalUpload(entity, list);
				}

				if (errors == 0) {
					// 存在チェック
					ItmMst dbEntity = repository.getByPk(entity.companyCd, entity.orgnzCd, entity.itmCd, entity.sqno);
					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq("A", entity.processType) && dbEntity != null) {
						entity.errorText = "品目コード情報はデータベース内の同一が重複しています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && (dbEntity == null)) {
						entity.errorText = "品目コード情報はデータベース内の存在していません。";
						errors ++;
					}
				}

				if (errors == 0) {
					// データベースとの整合性チェック
					if (in(entity.processType, "A", "C")) {
						Mg0011UpdateRequest req = new Mg0011UpdateRequest();
						req.companyCd = entity.companyCd;
						req.orgnzCd = entity.orgnzCd;
						req.itmCd = entity.itmCd;
						req.vdDtS = entity.vdDtS;
						req.vdDtE = entity.vdDtE;
						req.sqno = (long) entity.sqno;

						int maxSqnoOverlap = repository.getMaxSqno(req, eq(entity.processType, "C"));
						if (maxSqnoOverlap > 0) {
							entity.errorText = "品目コード情報はデータベースにおいて有効期間が重複しています。";
							errors ++;
						}
					}
				}

				allErrors += errors;

			}
		}

		return allErrors;
	}

	private String toKey(MgExcelEntityItm data) {
		return join(data.companyCd, "_", data.orgnzCd, "_", data.itmCd);
	}

	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookItm book, MgExcelEntityItm entity) {
		int allErrors = 0;
		final FieldValidators fields = new FieldValidators(i18n);

		// 会社コード
		if (eq("00053", sessionHolder.getLoginInfo().getCorporationCode())) {
			fields.addField(entity.companyCd, "会社コード")
			.required()
			.master(book.existCompanyCodes);
		} else {
			fields.addField(entity.companyCd, "会社コード")
			.required()
			.master(book.existCompanyCodes)
			.myCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		}


		fields.addField(entity.orgnzCd, "組織コード")
			.required()
			.master(book.existOrgnzCodes, null, entity.companyCd + "_" + entity.orgnzCd);

		// 連番
		// 2019/11/05 Excelアップロードバグの対応
		fields.addField(entity.strSqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること

		fields.addField(entity.itmCd, "品目コード")
			.required()
			.alphaNumeric(20);

		// その他項目
		if (in(entity.processType, "A", "U", "C")) {
			fields.addField(entity.itmNm, "品目名称")
				.required()
				.anyString(30);

			fields.addField(entity.splrCd, "取引先コード")
				.anyString(20)
				.master(book.existSplrCodes, null, entity.companyCd + "_" + entity.splrCd);

			fields.addField(entity.stckTp, "在庫区分")
				.include(book.stckTps);

			fields.addField(entity.untCd, "単位コード")
				.include(book.untCds);

			fields.addField(entity.amt, "金額")
				.numeric()
				.anyString(12);

			fields.addField(entity.taxCd, "消費税コード")
				.master(book.existTaxCodes, null, entity.companyCd + "_" + entity.taxCd);

			fields.addField(entity.prcFldTp, "調達部門区分")
				.include(book.prcFldTps);


			// 有効期間（開始）
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(entity.vdDtS, "有効期間（開始）")
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(STARTDATE, MessageCd.validTerm); //	1900年以降であること
			fields.addField(entity.vdDtS, "有効期間（開始）")
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること

			// 有効期間（終了）
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(entity.vdDtS, MessageCd.validStartDate);
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.gte(STARTDATE, MessageCd.validTerm); //	1900年以降であること
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること

			fields.addField(entity.ctgryCd, "カテゴリコード")
				.alphaNumericSymbol(10);

			fields.addField(entity.splrNmKj, "取引先名称（漢字）")
				.anyString(80);

			fields.addField(entity.splrNmKn, "取引先名称（ｶﾀｶﾅ）")
				.halfWidthOnly(50);


			fields.addField(entity.makerNm, "メーカー名称")
				.anyString(30);

			fields.addField(entity.makerMdlNo, "メーカー型式")
				.anyString(30);

			fields.addField(entity.itmRmk, "品目備考")
				.anyString(1000);

			fields.addField(entity.itmVrsn, "品目バージョン")
				.numeric()
				.anyString(18);

			fields.addField(entity.dltFg, "削除フラグ")
				.required()
				.include(book.dltFgs);
		}

		// 行単位のバリデーションとその結果をエンティティに書き戻し
		final List<String> errors = fields.validate();

		// エラー結果をbeanに書き戻す
		entity.errorText = String.join(" ", errors);

		allErrors += errors.size();

		return allErrors;
	}
}
