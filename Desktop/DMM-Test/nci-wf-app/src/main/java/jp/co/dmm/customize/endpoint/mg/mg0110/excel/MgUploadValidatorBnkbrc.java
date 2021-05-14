package jp.co.dmm.customize.endpoint.mg.mg0110.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0110.Mg0110Repository;
import jp.co.dmm.customize.endpoint.mg.mg0111.Mg0111Repository;
import jp.co.dmm.customize.endpoint.mg.mg0111.Mg0111UpdateRequest;
import jp.co.dmm.customize.jpa.entity.mw.BnkbrcMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 銀行支店情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBnkbrc extends MgUploadValidator<MgExcelBookBnkbrc> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0110Repository repository;
	@Inject private Mg0111Repository repositoryMg0111;

	/** 銀行支店マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBnkbrc book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityBnkbrc>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityBnkbrc> list = map.get(key);

			for (MgExcelEntityBnkbrc entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、銀行支店情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// 有効期間内の銀行が存在するか
					Mg0111UpdateRequest req = new Mg0111UpdateRequest();
					req.companyCd = entity.companyCd;
					req.bnkCd = entity.bnkCd;
					req.vdDtS = entity.vdDtS;
					req.vdDtE = entity.vdDtE;
					if(repositoryMg0111.countExistBnk(req) == 0 ) {
						entity.errorText = "有効期間内の銀行が存在しません";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					BnkbrcMst dbEntity = repository.getByPk(entity.companyCd, entity.bnkCd, entity.bnkbrcCd);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "銀行支店情報がすでにデータベースに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "銀行支店情報がデータベースに存在していません。";
						errors ++;
					}
				}

				allErrors += errors;
			}
		}

		return allErrors;
	}


	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookBnkbrc book, MgExcelEntityBnkbrc entity) {
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

		// 銀行コード
		fields.addField(entity.bnkCd, "銀行コード")
			.required()
			.numeric()
			.anyString(4)
			.master(book.existBnkCodes, null, entity.companyCd + "_" + entity.bnkCd);

		// 銀行支店コード
		fields.addField(entity.bnkbrcCd, "銀行支店コード")
			.required()
			.numeric()
			.anyString(3);

		// その他項目
		if ("A".equals(entity.processType) || "U".equals(entity.processType) || "C".equals(entity.processType)) {
			fields.addField(entity.bnkbrcNm, "銀行支店名称")
				.required()
				.anyString(40);

			fields.addField(entity.bnkbrcNmKn, "銀行支店名称（半角ｶﾅ）")
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

			fields.addField(entity.bnkbrcNmS, "銀行支店略称")
				.anyString(20);

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

	private String toKey(MgExcelEntityBnkbrc data) {
		return join(data.companyCd, "_", data.bnkCd, "_", data.bnkbrcCd);
	}
}
