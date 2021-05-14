package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0091.Mg0091Repository;
import jp.co.dmm.customize.endpoint.mg.mg0091.Mg0091UpdateRequest;
import jp.co.dmm.customize.jpa.entity.mw.BnkaccMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 取引先情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorBnkacc extends MgUploadValidator<MgExcelBookBnkacc> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0091Repository repository;

	/** 取引先マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookBnkacc book) {

		int allErrors = 0;
		final Map<String, List<MgExcelEntityBnkacc>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));
		for (String key : map.keySet()) {
			final List<MgExcelEntityBnkacc> list = map.get(key);

			for (MgExcelEntityBnkacc entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロードコンテンツ内の整合性チェック
					errors += super.validateInternalUpload(entity, list);
				}

				if (errors == 0) {
					// 有効期間内の銀行が存在するか
					Mg0091UpdateRequest req = new Mg0091UpdateRequest();
					req.companyCd = entity.companyCd;
					req.bnkCd = entity.bnkCd;
					req.vdDtS = entity.vdDtS;
					req.vdDtE = entity.vdDtE;
					if(repository.countExistBnk(req) == 0 ) {
						entity.errorText = "有効期間内の銀行が存在しません";
						errors ++;
					}
				}

				if (errors == 0) {
					// 存在チェック
					BnkaccMst dbEntity = repository.getByPk(entity.companyCd, entity.bnkaccCd, entity.sqno);
					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq("A", entity.processType) && dbEntity != null) {
						entity.errorText = "銀行口座コード情報はデータベース内の同一が重複しています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "銀行口座コード情報はデータベース内に存在していません。";
						errors ++;
					}
				}

				if (errors == 0) {
					// データベースとの整合性チェック
					if (in(entity.processType, "A", "C")) {
						Mg0091UpdateRequest req = new Mg0091UpdateRequest();
						req.companyCd = entity.companyCd;
						req.bnkaccCd = entity.bnkaccCd;
						req.bnkCd = entity.bnkCd;
						req.bnkbrcCd = entity.bnkbrcCd;
						req.bnkaccNo = entity.bnkaccNo;
						req.vdDtS = entity.vdDtS;
						req.vdDtE = entity.vdDtE;
						req.sqno = String.valueOf(entity.sqno);

						int maxSqnoOverlap = repository.getMaxSqno(req, eq(entity.processType, "C"));
						if (maxSqnoOverlap > 0) {
							entity.errorText = "銀行口座コード情報はデータベースにおいて有効期間が重複しています。";
							errors ++;
						}
					}
				}

				allErrors += errors;
			}
		}
		return allErrors;
	}

	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookBnkacc book, MgExcelEntityBnkacc entity) {
		int allErrors = 0;
		final FieldValidators fields = new FieldValidators(i18n);

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

		fields.addField(entity.bnkaccCd, "銀行口座コード")
			.required()
			.alphaNumeric(4);

		// 連番
		fields.addField(entity.strSqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること


		// その他項目
		if ("A".equals(entity.processType) || "U".equals(entity.processType) || "C".equals(entity.processType)) {
			fields.addField(entity.bnkCd, "銀行コード")
				.required()
				.alphaNumeric(4)
				.master(book.existBnkCodes, null, entity.companyCd + "_" + entity.bnkCd);

			fields.addField(entity.bnkbrcCd, "銀行支店コード")
				.required()
				.alphaNumeric(3)
				.master(book.existBnkbrcCodes, null, entity.companyCd + "_" + entity.bnkCd + "_" + entity.bnkbrcCd);


			fields.addField(entity.bnkaccTp, "銀行口座種別")
				.include(book.bnkaccTps);

			fields.addField(entity.bnkaccNo, "銀行口座番号")
				.required()
				.numeric()
				.anyString(7);

			fields.addField(entity.bnkaccNm, "銀行口座名称")
				.anyString(40);

			fields.addField(entity.bnkaccNmKn, "銀行口座名称（半角ｶﾅ）")
				.halfWidthOnly(30);

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

		}

		// 行単位のバリデーションとその結果をエンティティに書き戻し
		final List<String> errors = fields.validate();

		// エラー結果をbeanに書き戻す
		entity.errorText = String.join(" ", errors);

		allErrors += errors.size();

		return allErrors;
	}

	private String toKey(MgExcelEntityBnkacc data) {
		return join(data.companyCd, "_", data.bnkaccCd);
	}
}
