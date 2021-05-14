package jp.co.dmm.customize.endpoint.mg.mg0170.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0170.Mg0170Repository;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 通貨マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorMny extends MgUploadValidator<MgExcelBookMny> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0170Repository repository;

	/** 通貨マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookMny book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityMny>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityMny> list = map.get(key);

			for (MgExcelEntityMny entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、通貨情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					MnyMst dbEntity = repository.getByPk(entity.companyCd, entity.mnyCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "通貨情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "通貨情報が存在していません。";
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
	protected int validateBase(MgExcelBookMny book, MgExcelEntityMny entity) {
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

		// 通貨コード
		fields.addField(entity.mnyCd, "通貨コード")
			.required()
			.alphaNumeric(3);

		if (in(entity.processType, "A", "U", "C")) {

			// 通貨名称
			fields.addField(entity.mnyNm, "通貨名称")
				.required()
				.anyString(40);

			// 通貨記号
			fields.addField(entity.mnyMrk, "通貨記号")
				.anyString(3);

			// 小数点桁数
			fields.addField(entity.rdxpntGdt, "小数点桁数")
				.numeric()
				.anyString(1);

			// ソート順
			fields.addField(entity.sortOrder, "ソート順")
				.numeric()
				.anyString(5);

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

	private String toKey(MgExcelEntityMny data) {
		return join(data.companyCd, "_", data.mnyCd);
	}

}
