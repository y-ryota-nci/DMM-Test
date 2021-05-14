package jp.co.dmm.customize.endpoint.mg.mg0280.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0280.Mg0280Repository;
import jp.co.dmm.customize.jpa.entity.mw.MdaMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * メディアマスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorMda extends MgUploadValidator<MgExcelBookMda> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0280Repository repository;

	/** メディアマスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookMda book) {

		int allErrors = 0;

		// 会社コード、メディアIDをキーにMap化
		final Map<String, List<MgExcelEntityMda>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityMda> list = map.get(key);

			for (MgExcelEntityMda entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、メディア情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					MdaMst dbEntity = repository.getByPk(entity.companyCd, entity.mdaId);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "メディア情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "メディア情報が存在していません。";
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
	protected int validateBase(MgExcelBookMda book, MgExcelEntityMda entity) {
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

		// メディアID
		fields.addField(entity.mdaId, "メディアID")
			.required()
			.alphaNumeric(6);

		if (in(entity.processType, "A", "U", "C")) {

			// メディア名称
			fields.addField(entity.mdaNm, "メディア名称")
				.required()
				.anyByteString(390);

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

	private String toKey(MgExcelEntityMda data) {
		return join(data.companyCd, "_", data.mdaId);
	}

}
