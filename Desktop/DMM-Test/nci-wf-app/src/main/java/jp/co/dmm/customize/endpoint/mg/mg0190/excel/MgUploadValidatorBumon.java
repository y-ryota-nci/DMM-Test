package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0190.Mg0190Repository;
import jp.co.dmm.customize.jpa.entity.mw.BumonMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 部門マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBumon extends MgUploadValidator<MgExcelBookBumon> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0190Repository repository;

	/** 部門マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBumon book) {

		int allErrors = 0;

		// 会社コード、部門コードをキーにMap化
		final Map<String, List<MgExcelEntityBumon>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBumon> list = map.get(key);

			for (MgExcelEntityBumon entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、部門情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BumonMst dbEntity = repository.getByPk(entity.companyCd, entity.bumonCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "部門情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "部門情報が存在していません。";
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
	protected int validateBase(MgExcelBookBumon book, MgExcelEntityBumon entity) {
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

		// 部門コード
		fields.addField(entity.bumonCd, "部門コード")
			.required()
			.alphaNumeric(14);
		if (entity.bumonCd != null) {
			fields.addField(entity.bumonCd.length(), "部門コード桁数")
			.range(14, 14);
		}

		if (in(entity.processType, "A", "U", "C")) {

			// 部門名称
			fields.addField(entity.bumonNm, "部門名称")
				.required()
				.anyString(100);

			// 消費税種類コード
				fields.addField(entity.taxKndCd, "消費税種類コード")
				.required()
				.include(book.taxKndCds);

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

	private String toKey(MgExcelEntityBumon data) {
		return join(data.companyCd, "_", data.bumonCd);
	}

}
