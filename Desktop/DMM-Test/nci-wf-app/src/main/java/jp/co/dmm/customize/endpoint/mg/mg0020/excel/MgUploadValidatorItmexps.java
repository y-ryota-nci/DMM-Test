package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0020.Mg0020Repository;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 費目情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorItmexps extends MgUploadValidator<MgExcelBookItmexps> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0020Repository repository;

	/** 費目マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookItmexps book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityItmexps>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityItmexps> list = map.get(key);

			for (MgExcelEntityItmexps entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、費目情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					ItmexpsMst dbEntity = repository.getByPk(entity.companyCd, entity.itmexpsCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "費目情報がすでにデータベースに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "費目情報がデータベースに存在していません。";
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
	protected int validateBase(MgExcelBookItmexps book, MgExcelEntityItmexps entity) {
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

		fields.addField(entity.itmexpsCd, "費目コード")
			.required()
			.alphaNumeric(8);

		// その他項目
		if (in(entity.processType, "A", "U", "C")) {
			fields.addField(entity.itmexpsNm, "費目名称")
				.anyString(60);

			fields.addField(entity.itmexpsNmS, "費目略称")
				.anyString(20);

			fields.addField(entity.itmexpsLevel, "費目階層")
				.numeric()
				.anyString(5);

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

	private String toKey(MgExcelEntityItmexps data) {
		return join(data.companyCd, "_", data.itmexpsCd);
	}

}
