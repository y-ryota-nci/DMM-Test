package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0240.Mg0240Repository;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 支払サイトマスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorPaySite extends MgUploadValidator<MgExcelBookPaySite> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0240Repository repository;

	/** 支払サイトマスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookPaySite book) {

		int allErrors = 0;

		// 会社コード、支払サイトコードをキーにMap化
		final Map<String, List<MgExcelEntityPaySite>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityPaySite> list = map.get(key);

			for (MgExcelEntityPaySite entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、支払サイト情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					PaySiteMst dbEntity = repository.getByPk(entity.companyCd, entity.paySiteCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "支払サイト情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "支払サイト情報が存在していません。";
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
	protected int validateBase(MgExcelBookPaySite book, MgExcelEntityPaySite entity) {
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

		// 支払サイトコード
		fields.addField(entity.paySiteCd, "支払サイトコード")
			.required()
			.alphaNumeric(10);

		if (in(entity.processType, "A", "U", "C")) {

			// 支払サイト名称
			fields.addField(entity.paySiteNm, "支払サイト名称")
				.anyString(100);

			// 支払サイト（月）
			fields.addField(entity.paySiteM, "支払サイト（月）")
				.include(book.paySieMonths);

			// 支払サイト（日）
			fields.addField(entity.paySiteN, "支払サイト（日）")
				.include(book.paySieDayNumbers);

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

	private String toKey(MgExcelEntityPaySite data) {
		return join(data.companyCd, "_", data.paySiteCd);
	}

}
