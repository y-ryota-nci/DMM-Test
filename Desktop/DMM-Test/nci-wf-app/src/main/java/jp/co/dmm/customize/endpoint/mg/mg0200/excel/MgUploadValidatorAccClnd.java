package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0200.Mg0200Repository;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 会計カレンダーマスタ情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorAccClnd extends MgUploadValidator<MgExcelBookAccClnd> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0200Repository repository;

	/** 取引先マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookAccClnd book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityAccClnd>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityAccClnd> list = map.get(key);

			for (MgExcelEntityAccClnd entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロード内の重複チェック
					if (list.size() > 1) {
						entity.errorText = "アップロードデータにおいて、会計カレンダー情報が重複しています。";
						errors ++;
					}
				}

				if (errors == 0) {
					// DBに存在チェック
					AccClndMst dbEntity = repository.getByPk(entity.companyCd, entity.clndDt);

					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq(entity.processType, "A") && dbEntity != null) {
						entity.errorText = "会計カレンダー情報がすでに登録されています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "会計カレンダー情報が存在していません。";
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
	protected int validateBase(MgExcelBookAccClnd book, MgExcelEntityAccClnd entity) {
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

		// 日付
		fields.addField(entity.clndDt, "日付")
			.required()
			.date()
			.gte(STARTDATE, MessageCd.validTerm);
		fields.addField(entity.clndDt, "日付")
			.lte(ENDDATE, MessageCd.validTerm);


		if (in(entity.processType, "A", "U", "C")) {
			// 曜日
			fields.addField(entity.clndDay, "曜日")
				.required()
				.include(book.clndDayNos);

			// 休日区分
			fields.addField(entity.hldayTp, "休日区分")
				.include(book.hldayTps);

			// 銀行休日区分
			fields.addField(entity.bnkHldayTp, "銀行休日区分")
				.include(book.hldayTps);

			// 決済区分(購買)
			fields.addField(entity.stlTpPur, "決済区分(購買)")
				.include(book.stlTps);

			// 決済区分(債務)
			fields.addField(entity.stlTpFncobl, "決済区分(債務)")
				.include(book.stlTps);

			// 決済区分(財務)
			fields.addField(entity.stlTpFncaff, "決済区分(財務)")
				.include(book.stlTps);

			// 月次締め時間
			fields.addField(entity.mlClsTm, "月次締め時間")
				.numeric().anyString(4).max("2359");

		}

		// 行単位のバリデーションとその結果をエンティティに書き戻し
		final List<String> errors = fields.validate();

		// エラー結果をbeanに書き戻す
		entity.errorText = String.join(" ", errors);

		allErrors += errors.size();

		return allErrors;
	}

	private String toKey(MgExcelEntityAccClnd data) {
		return join(data.companyCd, "_", toStr(data.clndDt, "yyyyMMdd"));
	}

}
