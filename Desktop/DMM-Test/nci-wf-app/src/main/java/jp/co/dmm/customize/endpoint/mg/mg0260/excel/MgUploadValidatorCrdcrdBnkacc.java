package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0261.Mg0261Repository;
import jp.co.dmm.customize.jpa.entity.mw.CrdcrdBnkaccMst;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * クレカ口座マスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorCrdcrdBnkacc extends MgUploadValidator<MgExcelBookCrdcrdBnkacc> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0261Repository repository;

	/** 取引先マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookCrdcrdBnkacc book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityCrdcrdBnkacc>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityCrdcrdBnkacc> list = map.get(key);

			for (MgExcelEntityCrdcrdBnkacc entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、クレカ口座情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					CrdcrdBnkaccMst dbEntity = repository.getByPk(entity.companyCd, entity.splrCd, entity.usrCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "クレカ口座情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "クレカ口座情報が存在していません。";
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
	protected int validateBase(MgExcelBookCrdcrdBnkacc book, MgExcelEntityCrdcrdBnkacc entity) {
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

		// 取引先コード
		fields.addField(entity.splrCd, "取引先コード")
			.required()
			.alphaNumeric(20)
			.master(book.existSplrCodes, null, entity.companyCd + "_" + entity.splrCd)
			;

		// ユーザコード
		fields.addField(entity.usrCd, "ユーザコード")
			.required()
			.alphaNumeric(25)
			.master(book.existUserCodes, null, entity.companyCd + "_" + entity.usrCd)
			;

		if (in(entity.processType, "A", "U", "C")) {
			// カード会社名称
			fields.addField(entity.crdCompanyNm, "カード会社名称")
				.required();

			if (entity.crdCompanyNm != null && entity.crdCompanyNm.trim().length() > 0) {
				fields.addField(entity.crdCompanyNm.getBytes().length, "カード会社名称")
					.max(200);
			}


			// 銀行口座コード
			fields.addField(entity.bnkaccCd, "銀行口座コード")
				.required()
				.numeric()
				.anyString(4)
				.master(book.existBnkaccCodes, null, entity.companyCd + "_" + entity.bnkaccCd)
			;

			// 口座引落日
			fields.addField(entity.bnkaccChrgDt, "口座引落日")
				.include(book.bnkaccChrgDts);
			;

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

	private String toKey(MgExcelEntityCrdcrdBnkacc data) {
		return join(data.companyCd, "_", data.splrCd, "_", data.usrCd);
	}

}
