package jp.co.dmm.customize.endpoint.mg.mg0330.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0330.Mg0330Repository;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 国マスタアップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorLnd extends MgUploadValidator<MgExcelBookLnd> {

	@Inject private Mg0330Repository repository;

	/** 国マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookLnd book) {

		int allErrors = 0;

		// 国コードにMap化
		final Map<String, List<MgExcelEntityLnd>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityLnd> list = map.get(key);

			for (MgExcelEntityLnd entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、国情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					boolean dbExist = repository.checkByPk(entity.lndCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbExist ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbExist) {
						entity.errorText = "国情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && !dbExist) {
						entity.errorText = "国情報が存在していません。";
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
	protected int validateBase(MgExcelBookLnd book, MgExcelEntityLnd entity) {
		int allErrors = 0;

		final FieldValidators fields = new FieldValidators(i18n);

		// 国コード
		fields.addField(entity.lndCd, "国コード")
			.required()
			.alphaNumeric(3);

		if (in(entity.processType, "A", "U", "C")) {

			// 国名
			fields.addField(entity.lndNm, "国名")
				.required()	//	空白でないこと
				.anyString(100);

			// 国コード（DJII）
			fields.addField(entity.lndCdDjii, "国コード（DJII）")
				.required()	//	空白でないこと
				.alphaNumeric(10);

			// ソート順
			fields.addField(entity.sortOrder, "ソート順")
				.required()	//	空白でないこと
				.integer()	//	数値であること
				.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
				.range("1","999");//1~999の範囲内であること

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

	private String toKey(MgExcelEntityLnd data) {
		return data.lndCd;
	}

}