package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0250.Mg0250Repository;
import jp.co.dmm.customize.jpa.entity.mw.BumonexpsMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 部門関連マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBumonExps extends MgUploadValidator<MgExcelBookBumonExps> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0250Repository repository;

	/** 部門関連マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBumonExps book) {

		int allErrors = 0;

		// 会社コード、部門関連コードをキーにMap化
		final Map<String, List<MgExcelEntityBumonExps>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBumonExps> list = map.get(key);

			for (MgExcelEntityBumonExps entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、部門関連情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BumonexpsMst dbEntity = repository.getByPk(entity.companyCd, entity.bumonCd, entity.orgnzCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "部門関連情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "部門関連情報が存在していません。";
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
	protected int validateBase(MgExcelBookBumonExps book, MgExcelEntityBumonExps entity) {
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
			.alphaNumeric(14)
			.master(book.existBumonCds, null, entity.companyCd + "_" + entity.bumonCd);

		// 組織コード
		fields.addField(entity.orgnzCd, "組織コード")
			.required()
			.alphaNumeric(10)
			.master(book.existOrgnzCodes, null, entity.companyCd + "_" + entity.orgnzCd);

		// 削除フラグ
		fields.addField(entity.dltFg, "削除フラグ")
			.required()
			.include(book.dltFgs);

		// 行単位のバリデーションとその結果をエンティティに書き戻し
		final List<String> errors = fields.validate();

		// エラー結果をbeanに書き戻す
		entity.errorText = String.join(" ", errors);

		allErrors += errors.size();

		return allErrors;
	}

	private String toKey(MgExcelEntityBumonExps data) {
		return join(data.companyCd, "_", data.bumonCd, "_", data.orgnzCd);
	}

}
