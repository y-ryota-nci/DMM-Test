package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0030.Mg0030Repository;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps2Chrmst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 費目関連マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorItmexpsChrmst extends MgUploadValidator<MgExcelBookItmexpsChrmst> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0030Repository repository;

	/** 費目関連マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookItmexpsChrmst book) {

		int allErrors = 0;

		// 会社コード、費目関連コードをキーにMap化
		final Map<String, List<MgExcelEntityItmexpsChrmst>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityItmexpsChrmst> list = map.get(key);

			for (MgExcelEntityItmexpsChrmst entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、費目関連情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					Itmexps2Chrmst dbEntity = repository.getByPk(entity.companyCd, entity.orgnzCd, entity.itmexpsCd1, entity.itmexpsCd2);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "費目関連情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "費目関連情報が存在していません。";
						errors ++;
					}
				}

				allErrors += errors;
			}
		}

		return allErrors;
	}

	/**
	 * 入力チェック
	 * @param book
	 * @param entity
	 * @return
	 */
	protected int validateBase(MgExcelBookItmexpsChrmst book, MgExcelEntityItmexpsChrmst entity) {
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

		// 組織コード
		if (!eq("##########", entity.orgnzCd)) {
			fields.addField(entity.orgnzCd, "組織コード")
				.required()
				.alphaNumericSymbol(10)
				.master(book.existOrgnzCodes, null, entity.companyCd + "_" + entity.orgnzCd);
		}

		// 費目コード（１）
		fields.addField(entity.itmexpsCd1, "費目コード（１）")
			.required()
			.alphaNumeric(8)
			.master(book.existItmexpsCds, null, entity.companyCd + "_" + entity.itmexpsCd1 + "_1");

		// 費目コード（２）
		fields.addField(entity.itmexpsCd2, "費目コード（２）")
			.required()
			.alphaNumeric(8)
			.master(book.existItmexpsCds, null, entity.companyCd + "_" + entity.itmexpsCd2 + "_2");

		if (in(entity.processType, "A", "U", "C")) {
			// 仕訳コード
			fields.addField(entity.jrnCd, "仕訳コード")
				.alphaNumeric(3)
				.master(book.existJrnCds, null, entity.companyCd + "_" + entity.jrnCd);

			// 勘定科目コード
			fields.addField(entity.accCd, "勘定科目コード")
				.alphaNumeric(4)
				.master(book.existAccCds, null, entity.companyCd + "_" + entity.accCd);

			// 勘定科目補助コード
			fields.addField(entity.accBrkdwnCd, "勘定科目補助コード")
				.alphaNumeric(10)
				.master(book.existAccBrkdwnCds, null, entity.companyCd + "_" + entity.accCd + "_" + entity.accBrkdwnCd);

			// 管理科目コード
			fields.addField(entity.mngaccCd, "管理科目コード")
				.alphaNumeric(5);

			// 管理科目補助コード
			fields.addField(entity.mngaccBrkdwnCd, "管理科目補助コード")
				.alphaNumeric(10);

			// 予算科目コード
			fields.addField(entity.bdgtaccCd, "予算科目コード")
				.alphaNumeric(10)
				.master(book.existBdgtaccCds, null, entity.companyCd + "_" + entity.bdgtaccCd);

			// 資産区分
			fields.addField(entity.asstTp, "資産区分")
				.include(book.asstTps);

			// 消費税コード
			fields.addField(entity.taxCd, "消費税コード")
				.required()
				.alphaNumeric(3)
				.master(book.existTaxCodes, null, entity.companyCd + "_" + entity.taxCd);

			// 伝票グループ（GL）
			fields.addField(entity.slpGrpGl, "伝票グループ（GL）")
				.alphaNumeric(3);

			// 経費区分
			fields.addField(entity.cstTp, "経費区分")
				.include(book.cstTps);

			// 課税対象区分
			fields.addField(entity.taxSbjTp, "課税対象区分")
				.required()
				.include(book.taxSbjTps);

			// 削除フラグ
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

	private String toKey(MgExcelEntityItmexpsChrmst data) {
		return join(data.companyCd, "_", data.orgnzCd, "_", data.itmexpsCd1, "_", data.itmexpsCd2);
	}

}
