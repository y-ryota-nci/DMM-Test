package jp.co.dmm.customize.endpoint.mg.mg0290.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0290.Mg0290Repository;
import jp.co.dmm.customize.jpa.entity.mw.BndFlrMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 結合フロアマスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBndFlr extends MgUploadValidator<MgExcelBookBndFlr> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0290Repository repository;

	/** 結合フロアマスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBndFlr book) {

		int allErrors = 0;

		// 会社コード、結合フロアコードをキーにMap化
		final Map<String, List<MgExcelEntityBndFlr>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBndFlr> list = map.get(key);

			for (MgExcelEntityBndFlr entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、結合フロア情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BndFlrMst dbEntity = repository.getByPk(entity.companyCd, entity.bndFlrCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "結合フロア情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "結合フロア情報が存在していません。";
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
	protected int validateBase(MgExcelBookBndFlr book, MgExcelEntityBndFlr entity) {
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

		// 結合フロアコード
		fields.addField(entity.bndFlrCd, "結合フロアコード")
			.required()
			.alphaNumeric(3);

		if (in(entity.processType, "A", "U", "C")) {

			// 結合フロア名称
			fields.addField(entity.bndFlrNm, "結合フロア名称")
				.required()
				.anyByteString(150);

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

	private String toKey(MgExcelEntityBndFlr data) {
		return join(data.companyCd, "_", data.bndFlrCd);
	}

}
