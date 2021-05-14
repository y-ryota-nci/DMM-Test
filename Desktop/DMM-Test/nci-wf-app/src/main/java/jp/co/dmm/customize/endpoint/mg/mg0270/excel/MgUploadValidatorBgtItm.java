package jp.co.dmm.customize.endpoint.mg.mg0270.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0270.Mg0270Repository;
import jp.co.dmm.customize.jpa.entity.mw.BgtItmMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 予算科目マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBgtItm extends MgUploadValidator<MgExcelBookBgtItm> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0270Repository repository;

	/** 予算科目マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBgtItm book) {

		int allErrors = 0;

		// 会社コード、予算科目コードをキーにMap化
		final Map<String, List<MgExcelEntityBgtItm>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBgtItm> list = map.get(key);

			for (MgExcelEntityBgtItm entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、予算科目情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BgtItmMst dbEntity = repository.getByPk(entity.companyCd, entity.bgtItmCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "予算科目情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "予算科目情報が存在していません。";
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
	protected int validateBase(MgExcelBookBgtItm book, MgExcelEntityBgtItm entity) {
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

		// 予算科目コード
		fields.addField(entity.bgtItmCd, "予算科目コード")
			.required()
			.alphaNumeric(10);

		if (in(entity.processType, "A", "U", "C")) {

			// 予算科目名称
			fields.addField(entity.bgtItmNm, "予算科目名称")
				.required()
				.anyString(40);

			// BS/PL区分
			fields.addField(entity.bsPlTp, "BS/PL区分")
				.required()
				.include(book.bsPlTps);

			// ソート順
			fields.addField(entity.sortOrder, "ソート順")
				.required()
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

	private String toKey(MgExcelEntityBgtItm data) {
		return join(data.companyCd, "_", data.bgtItmCd);
	}

}
