package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0320.Mg0320Repository;
import jp.co.dmm.customize.jpa.entity.mw.TaxexpsMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 消費税関連マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorTaxexps extends MgUploadValidator<MgExcelBookTaxexps> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0320Repository repository;

	/** 消費税関連マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookTaxexps book) {

		int allErrors = 0;

		// 会社コード、消費税種類コードをキーにMap化
		final Map<String, List<MgExcelEntityTaxexps>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityTaxexps> list = map.get(key);

			for (MgExcelEntityTaxexps entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、消費税関連情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					TaxexpsMst dbEntity = repository.getByPk(entity.companyCd, entity.taxKndCd, entity.taxSpc);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "消費税関連情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "消費税関連情報が存在していません。";
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
	protected int validateBase(MgExcelBookTaxexps book, MgExcelEntityTaxexps entity) {
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

		// 消費税種類コード
		fields.addField(entity.taxKndCd, "消費税種類コード")
			.required()
			.include(book.taxKndCds);

		// 消費税種別
		fields.addField(entity.taxSpc, "消費税種別")
			.required()
			.include(book.taxSpcs);

		if (in(entity.processType, "A", "U", "C")) {

			// 消費税コード
			fields.addField(entity.taxCd, "消費税コード")
				.required()
				.alphaNumeric(3)
				.master(book.existTaxCodes, null, entity.companyCd + "_" + entity.taxCd);

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

	private String toKey(MgExcelEntityTaxexps data) {
		return join(data.companyCd, "_", data.taxKndCd, "_", data.taxSpc);
	}

}
