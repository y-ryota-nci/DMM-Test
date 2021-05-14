package jp.co.dmm.customize.endpoint.mg.mg0300;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.util.FieldValidators;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 住所マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class Mg0300UploadValidator extends MiscUtils {
	@Inject private I18nService i18n;


	/** 住所マスタのバリデーション */
	public boolean validate(List<Mg0300CsvEntity> listEntity) {

		int allErrors = 0;
		for (Mg0300CsvEntity entity : listEntity) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 都道府県コード
			fields.addField(entity.adrPrfCd, "都道府県コード")
			.required().anyString(5);

			// 郵便番号
			fields.addField(entity.zipCd, "郵便番号")
			.anyString(7);

			// 都道府県（漢字）
			fields.addField(entity.adrPrf, "都道府県（漢字）")
				.anyString(100);

			// 都道府県（カタカナ）
			fields.addField(entity.adrPrfKn, "都道府県（カナ）")
			.anyString(100);

			// 市区町村（漢字）
			fields.addField(entity.adr1, "市区町村（漢字）")
				.anyString(100);

			// 市区町村（カナ）
			fields.addField(entity.adr1Kn, "市区町村（カナ）")
			.anyString(100);

			// 町名番地（漢字）
			fields.addField(entity.adr2, "町名番地（漢字）")
				.anyString(100);

			// 町名番地（カナ）
			fields.addField(entity.adr2Kn, "町名番地（カナ）")
			.anyString(100);


			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();

			allErrors += errors.size();
		}
		return allErrors == 0;
	}

}
