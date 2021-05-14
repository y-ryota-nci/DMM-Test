package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0100.Mg0100Repository;
import jp.co.dmm.customize.jpa.entity.mw.BnkMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 銀行情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBnk extends MgUploadValidator<MgExcelBookBnk> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0100Repository repository;

	/** 銀行マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBnk book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityBnk>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBnk> list = map.get(key);

			for (MgExcelEntityBnk entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、銀行情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BnkMst dbEntity = repository.getByPk(entity.companyCd, entity.bnkCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "銀行情報がすでにデータベースに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "銀行情報がデータベースに存在していません。";
						errors ++;
					}
				}

				allErrors += errors;
			}
		}

		return allErrors;
	}


	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookBnk book, MgExcelEntityBnk entity) {
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

		fields.addField(entity.bnkCd, "銀行コード")
			.required()
			.numeric()
			.anyString(4);

		// その他項目
		if (in(entity.processType, "A", "U", "C")) {
			fields.addField(entity.bnkNm, "銀行名称")
				.required()
				.anyString(40);

			fields.addField(entity.bnkNmKn, "銀行名称（半角ｶﾅ）")
				.required()
				.halfWidthOnly(20);

			// 有効期間（開始）
			fields.addField(entity.vdDtS, "有効期間（開始）")
				.required()
				.date()
				.gte(STARTDATE, MessageCd.validTerm);
			fields.addField(entity.vdDtS, "有効期間（開始）")
				.lte(ENDDATE, MessageCd.validTerm);

			// 有効期間（終了）
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.required()
				.date()
				.gte(entity.vdDtS, MessageCd.validStartDate);
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.gte(STARTDATE, MessageCd.validTerm);
			fields.addField(entity.vdDtE, "有効期間（終了）")
				.lte(ENDDATE, MessageCd.validTerm);

			// 削除フラグ
			fields.addField(entity.dltFg, "削除フラグ")
				.required()
				.include(book.dltFgs);

			fields.addField(entity.bnkNmS, "銀行略称")
				.anyString(20);
		}

		// 行単位のバリデーションとその結果をエンティティに書き戻し
		final List<String> errors = fields.validate();

		// エラー結果をbeanに書き戻す
		entity.errorText = String.join(" ", errors);

		allErrors += errors.size();

		return allErrors;
	}

	private String toKey(MgExcelEntityBnk data) {
		return join(data.companyCd, "_", data.bnkCd);
	}

}
