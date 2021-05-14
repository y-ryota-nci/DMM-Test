package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0141.Mg0141Repository;
import jp.co.dmm.customize.endpoint.mg.mg0141.Mg0141UpdateRequest;
import jp.co.dmm.customize.jpa.entity.mw.AccBrkdwnMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 勘定科目補助情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorAccBrkdwn extends MgUploadValidator<MgExcelBookAccBrkdwn> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0141Repository repository;

	/** 勘定科目補助マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookAccBrkdwn book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityAccBrkdwn>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));
		for (String key : map.keySet()) {
			final List<MgExcelEntityAccBrkdwn> list = map.get(key);

			for (MgExcelEntityAccBrkdwn entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロードコンテンツ内の整合性チェック
					errors += super.validateInternalUpload(entity, list);
				}

				if (errors == 0) {
					// 有効期間内の勘定科目が存在するか
					Mg0141UpdateRequest req = new Mg0141UpdateRequest();
					req.companyCd = entity.companyCd;
					req.accCd = entity.accCd;
					req.vdDtS = entity.vdDtS;
					req.vdDtE = entity.vdDtE;
					if(repository.countExistAcc(req) == 0 ) {
						entity.errorText = "有効期間内の勘定科目が存在しません";
						errors ++;
					}
				}

				if (errors == 0) {
					// 存在チェック
					AccBrkdwnMst dbEntity = repository.getByPk(entity.companyCd, entity.accCd, entity.accBrkdwnCd, entity.sqno);
					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq("A", entity.processType) && dbEntity != null) {
						entity.errorText = "勘定科目補助コード情報はデータベース内の同一が重複しています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "勘定科目補助コード情報はデータベース内の存在していません。";
						errors ++;
					}
				}

				if (errors == 0) {
					// データベースとの整合性チェック
					if (in(entity.processType, "A", "C")) {
						Mg0141UpdateRequest req = new Mg0141UpdateRequest();
						req.companyCd = entity.companyCd;
						req.accCd = entity.accCd;
						req.accBrkdwnCd = entity.accBrkdwnCd;
						req.vdDtS = entity.vdDtS;
						req.vdDtE = entity.vdDtE;
						req.sqno = String.valueOf(entity.sqno);

						int maxSqnoOverlap = repository.getMaxSqno(req, eq(entity.processType, "C"));
						if (maxSqnoOverlap > 0) {
							entity.errorText = "勘定科目補助コード情報はデータベースにおいて有効期間が重複しています。";
							errors ++;
						}
					}
				}

				allErrors += errors;
			}
		}

		return allErrors;
	}

	private String toKey(MgExcelEntityAccBrkdwn data) {
		return join(data.companyCd, "_", data.accCd, "_", data.accBrkdwnCd);
	}

	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookAccBrkdwn book, MgExcelEntityAccBrkdwn entity) {
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

		fields.addField(entity.accCd, "勘定科目コード")
			.required()
			.alphaNumeric(4)
			.master(book.existAccCodes, null, entity.companyCd + "_" + entity.accCd);


		fields.addField(entity.accBrkdwnCd, "勘定科目補助コード")
			.required()
			.alphaNumeric(10);

		// 連番
		fields.addField(entity.strSqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること

		// その他項目
		if (in(entity.processType, "A", "U", "C")) {
			fields.addField(entity.accBrkdwnNm, "勘定科目補助名称")
				.required()
				.anyString(60);

			fields.addField(entity.accBrkdwnNmS, "勘定科目補助略称")
				.anyString(30);

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
}
