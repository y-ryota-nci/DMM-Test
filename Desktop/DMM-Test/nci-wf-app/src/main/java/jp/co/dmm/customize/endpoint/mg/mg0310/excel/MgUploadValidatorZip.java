package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0310.Mg0310Repository;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 郵便番号マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorZip extends MgUploadValidator<MgExcelBookZip> {

	@Inject private Mg0310Repository repository;

	/** 郵便番号マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookZip book) {

		int allErrors = 0;

		// 郵便番号コード、連番をキーにMap化
		final Map<String, List<MgExcelEntityZip>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityZip> list = map.get(key);

			for (MgExcelEntityZip entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、郵便番号情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					boolean dbExist = repository.checkByPk(entity.zipCd, entity.sqno);

					if (eq("U", entity.processType)) {
						entity.processType = dbExist ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbExist) {
						entity.errorText = "郵便番号情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && !dbExist) {
						entity.errorText = "郵便番号情報が存在していません。";
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
	protected int validateBase(MgExcelBookZip book, MgExcelEntityZip entity) {
		int allErrors = 0;

		final FieldValidators fields = new FieldValidators(i18n);

		// 郵便番号コード
		fields.addField(entity.zipCd, "郵便番号")
			.required()
			.anyString(7)
			.numeric();

		// 連番 元のソースの不具合はStringutil.isNumeric()がマイナスの値をfalseで返すことが原因
		fields.addField(entity.sqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること

		if (in(entity.processType, "A", "U", "C")) {

			// 住所(都道府県)コード
			fields.addField(entity.adrPrfCd, "住所(都道府県)コード")
				.anyString(5)
				.numeric();

			// 住所(都道府県)
			fields.addField(entity.adrPrf, "住所(都道府県)")
				.anyString(30);

			// 住所(都道府県)(ｶﾀｶﾅ)
			fields.addField(entity.adrPrfKn, "住所(都道府県)(ｶﾀｶﾅ)")
				.halfWidthOnly(30);

			// 住所(市区町村)
			fields.addField(entity.adr1, "住所(市区町村)")
				.anyString(60);

			// 住所(市区町村)(ｶﾀｶﾅ)
			fields.addField(entity.adr1Kn, "住所(市区町村)(ｶﾀｶﾅ)")
				.halfWidthOnly(100);

			// 住所(町名番地)
			fields.addField(entity.adr2, "住所(町名番地)")
			.anyString(60);

			// 住所(町名番地)(ｶﾀｶﾅ)
			fields.addField(entity.adr2Kn, "住所(町名番地)(ｶﾀｶﾅ)")
				.halfWidthOnly(100);

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

	private String toKey(MgExcelEntityZip data) {
		return join(data.zipCd, "_", data.sqno);
	}

}
