package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0210.Mg0210Repository;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 源泉税マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorHldtax extends MgUploadValidator<MgExcelBookHldtax> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0210Repository repository;

	/** 源泉税マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookHldtax book) {

		int allErrors = 0;

		// 会社コード、源泉税コードをキーにMap化
		final Map<String, List<MgExcelEntityHldtax>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityHldtax> list = map.get(key);

			for (MgExcelEntityHldtax entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、源泉税情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					HldtaxMst dbEntity = repository.getByPk(entity.companyCd, entity.hldtaxTp);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "源泉税情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "源泉税情報が存在していません。";
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
	protected int validateBase(MgExcelBookHldtax book, MgExcelEntityHldtax entity) {
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

		// 源泉税コード
		fields.addField(entity.hldtaxTp, "源泉税区分")
			.required()
			.alphaNumeric(3);

		if (in(entity.processType, "A", "U", "C")) {

			// 源泉税名称
			fields.addField(entity.hldtaxNm, "源泉税名称")
				.anyString(100);

			// 源泉税率1
			fields.addField(entity.hldtaxRto1, "源泉税率1")
				.range(BigDecimal.ZERO, new BigDecimal("99.99"));

			// 源泉税率2
			fields.addField(entity.hldtaxRto2, "源泉税率2")
				.range(BigDecimal.ZERO, new BigDecimal("99.99"));

			// 勘定科目コード
			fields.addField(entity.accCd, "勘定科目コード")
				.required()
				.alphaNumeric(4)
				.master(book.existAccCodes, null, entity.companyCd + "_" + entity.accCd);

			// 勘定科目補助コード
			fields.addField(entity.accBrkdwnCd, "勘定科目補助コード")
				.required()
				.alphaNumeric(10)
				.master(book.existAccBrkdwnCodes, null, entity.companyCd + "_" + entity.accCd + "_" + entity.accBrkdwnCd);

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

	private String toKey(MgExcelEntityHldtax data) {
		return join(data.companyCd, "_", data.hldtaxTp);
	}

}
