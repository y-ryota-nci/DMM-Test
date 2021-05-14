package jp.co.dmm.customize.endpoint.mg.mg0220.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0220.Mg0220Repository;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 支払業務マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorPayAppl extends MgUploadValidator<MgExcelBookPayAppl> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0220Repository repository;

	/** 支払業務マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookPayAppl book) {

		int allErrors = 0;

		// 会社コード、支払業務コードをキーにMap化
		final Map<String, List<MgExcelEntityPayAppl>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityPayAppl> list = map.get(key);

			for (MgExcelEntityPayAppl entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、支払業務情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					PayApplMst dbEntity = repository.getByPk(entity.companyCd, entity.payApplCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "支払業務情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "支払業務情報が存在していません。";
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
	protected int validateBase(MgExcelBookPayAppl book, MgExcelEntityPayAppl entity) {
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

		// 支払業務コード
		fields.addField(entity.payApplCd, "支払業務コード")
			.required()
			.alphaNumeric(2);

		if (in(entity.processType, "A", "U", "C")) {

			// 支払業務名称
			fields.addField(entity.payApplNm, "支払業務名称")
				.anyString(100);

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

	private String toKey(MgExcelEntityPayAppl data) {
		return join(data.companyCd, "_", data.payApplCd);
	}

}
