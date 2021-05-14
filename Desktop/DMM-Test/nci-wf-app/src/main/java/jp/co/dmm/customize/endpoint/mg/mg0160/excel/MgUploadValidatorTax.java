package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0161.Mg0161Repository;
import jp.co.dmm.customize.endpoint.mg.mg0161.Mg0161UpdateRequest;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 消費税情報アップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorTax extends MgUploadValidator<MgExcelBookTax> {

	@Inject private SessionHolder sessionHolder;

	@Inject private Mg0161Repository repository;

	/** 消費税マスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookTax book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityTax>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));
		for (String key : map.keySet()) {
			final List<MgExcelEntityTax> list = map.get(key);

			for (MgExcelEntityTax entity : list) {
				// 入力チェック
				int errors = validateBase(book, entity);

				if (errors == 0) {
					// アップロードコンテンツ内の整合性チェック
					errors += super.validateInternalUpload(entity, list);
				}

				if (errors == 0) {
					// 存在チェック
					TaxMst dbEntity = repository.getByPk(entity.companyCd, entity.taxCd, entity.sqno);
					if (eq("U", entity.processType)) {
						entity.processType = dbEntity != null ? "C" : "A";
					}

					if (eq("A", entity.processType) && dbEntity != null) {
						entity.errorText = "消費税コード情報はデータベース内の同一が重複しています。";
						errors ++;
					} else if (in(entity.processType, "C", "D") && dbEntity == null) {
						entity.errorText = "消費税コード情報はデータベース内に存在していません。";
						errors ++;
					}
				}

				if (errors == 0) {
					// データベースとの整合性チェック
					if (in(entity.processType, "A", "C")) {
						Mg0161UpdateRequest req = new Mg0161UpdateRequest();
						req.companyCd = entity.companyCd;
						req.taxCd = entity.taxCd;
						req.vdDtS = entity.vdDtS;
						req.vdDtE = entity.vdDtE;
						req.sqno = (long) entity.sqno;

						int maxSqnoOverlap = repository.getMaxSqno(req, eq(entity.processType, "C"));
						if (maxSqnoOverlap > 0) {
							entity.errorText = "消費税コード情報はデータベースにおいて有効期間が重複しています。";
							errors ++;
						}
					}
				}

				allErrors += errors;
			}
		}

		return allErrors;
	}

	private String toKey(MgExcelEntityTax data) {
		return join(data.companyCd, "_", data.taxCd);
	}

	/** データ行の単項目単位のバリデーション. */
	private int validateBase(MgExcelBookTax book, MgExcelEntityTax entity) {
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


		fields.addField(entity.taxCd, "消費税コード")
			.required()
			.alphaNumeric(3);

		// 連番
		fields.addField(entity.strSqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	整数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること


		// その他項目
		if (in(entity.processType, "A", "U", "C")) {
			fields.addField(entity.taxNm, "消費税名称")
				.anyString(20);

			fields.addField(entity.taxNmS, "消費税略称")
				.anyString(10);

			fields.addField(entity.taxRto, "消費税率")
				.numeric()
				.range(BigDecimal.ZERO, new BigDecimal("999.9999"));

			fields.addField(entity.taxTp, "税処理区分")
				.include(book.taxTps);

			fields.addField(entity.taxCdSs, "税処理コード（SuperStream）")
				.alphaNumeric(4);

			fields.addField(entity.frcUnt, "端数処理単位")
				.alphaNumeric(1);

			fields.addField(entity.frcTp, "端数処理区分")
				.include(book.frcTps);

			fields.addField(entity.accCd, "勘定科目")
				.alphaNumeric(4)
				.master(book.existAccCodes, null, entity.companyCd + "_" + entity.accCd);

			fields.addField(entity.accBrkdwnCd, "勘定科目補助")
				.alphaNumeric(10)
				.master(book.existAccBrkdwnCodes, null, entity.companyCd + "_" + entity.accCd + "_" + entity.accBrkdwnCd);

			fields.addField(entity.dcTp, "正残区分")
				.include(book.dcTps);

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
