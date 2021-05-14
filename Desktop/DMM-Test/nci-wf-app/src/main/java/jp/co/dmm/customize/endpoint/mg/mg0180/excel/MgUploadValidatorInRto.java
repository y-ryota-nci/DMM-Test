package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadValidator;
import jp.co.dmm.customize.endpoint.mg.mg0180.Mg0180Repository;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;

/**
 * 社内レートマスタアップロードのバリデーター
 */
@ApplicationScoped
public class MgUploadValidatorInRto extends MgUploadValidator<MgExcelBookInRto> {

	@Inject private SessionHolder sessionHolder;
	@Inject private Mg0180Repository repository;

	/** 社内レートマスタのバリデーション */
	@Override
	protected int validateMaster(MgExcelBookInRto book) {

		int allErrors = 0;

		// 会社コード、通貨コードをキーにMap化
		final Map<String, List<MgExcelEntityInRto>> map = book.sheet.entityList.stream().collect(Collectors.groupingBy(d -> toKey(d)));

		for (String key : map.keySet()) {
			final List<MgExcelEntityInRto> list = map.get(key);
			final Set<Integer> duplicate = new HashSet<>();

			// データ行の単項目単位のバリデーション
			for (MgExcelEntityInRto entity : list) {
				final List<String> errors = validateBase(book, entity);

				if (errors.isEmpty()) {
					// 連番が重複していればエラー
					if (duplicate.contains(entity.sqno)) {
						errors.add("同一通貨コードにおいて連番が重複しています。");
					} else {
						duplicate.add(entity.sqno);

						// 社内レートマスタの存在チェック
						boolean isExists = repository.isExists(entity.companyCd, entity.mnyCd, entity.sqno);
						// 新規登録時はデータが存在していればエラー
						if (eq("A", entity.processType) && isExists) {
							errors.add("対象データはマスタに登録済みです。");
						}
						// 更新・削除時はデータが存在していなければエラー
						else if (in(entity.processType, "C", "D") && !isExists) {
							errors.add("対象データはマスタに存在しません。");
						}
					}
				}

				// エラー結果をbeanに書き戻す
				entity.errorText = String.join(" ", errors);

				allErrors += errors.size();
			}
		}

		// エラーがなければ通貨コード毎の整合性チェック
		if (allErrors == 0) {
			for (String key : map.keySet()) {
				final List<MgExcelEntityInRto> list = map.get(key);

				int tmpErrors = 0;
				int len = list.size();
				for (int i = 0; i < len; i++) {
					MgExcelEntityInRto entity1 = list.get(i);
					if (eq(DeleteFlag.OFF, entity1.dltFg)) {
						// アップロードデータ内において有効期間が重複していればエラー
						for (int j = i + 1; j < len; j++) {
							MgExcelEntityInRto entity2 = list.get(j);
							if (eq(DeleteFlag.OFF, entity2.dltFg) && overlap(entity1.vdDtS, entity1.vdDtE, entity2.vdDtS, entity2.vdDtE)) {
								entity1.errorText = "アップロードファイル内の同一通貨コードにおいて有効期間が重複しています。";
								tmpErrors++;
								break;
							}
						}
					}
				}

				// アップロードデータ内にて有効期間の重複がなければ、今回アップロードデータ以外のデータで重複があるかをチェック
				if (tmpErrors == 0) {
					// 今回登録対象となる連番一覧を取得
					final Set<Integer> sqnos = list.stream().map(d -> d.sqno).collect(Collectors.toSet());
					for (MgExcelEntityInRto entity : list) {
						// 今回登録対象外のデータにおいて有効期間の重複があればエラー
						if (repository.isOverlap(entity.companyCd, entity.mnyCd, sqnos, entity.vdDtS, entity.vdDtE)) {
							entity.errorText = "同一通貨コードにおいて有効期間が重複しています。";
							tmpErrors++;
						}
					}
				}

				allErrors += tmpErrors;
			}
		}

		return allErrors;
	}

	private String toKey(MgExcelEntityInRto data) {
		return join(data.companyCd, "@@@", data.mnyCd);
	}

	/** データ行の単項目単位のバリデーション. */
	private List<String> validateBase(MgExcelBookInRto book, MgExcelEntityInRto entity) {
		final FieldValidators fields = new FieldValidators(i18n);

		// 処理区分
		fields.addField(entity.processType, "処理区分")
			.required()
			.include(book.processTypes);

		// 会社コード
		fields.addField(entity.companyCd, "会社コード")
			.required()
			.master(book.existCompanyCodes)
			.myCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());

		// 通貨コード
		fields.addField(entity.mnyCd, "通貨コード")
			.required()
			.alphaNumeric(3)
			.master(book.mnyCds, null, entity.companyCd + "_" + entity.mnyCd);

		// 連番
		fields.addField(entity.strSqno, "連番")
			.required()	//	空白でないこと
			.integer()	//	数値であること
			.anyString(3)//文字列の1~999だと1000以上を通すため桁数チェックも加える
			.range("1","999");//1~999の範囲内であること


		// その他項目
		if ("A".equals(entity.processType) || "U".equals(entity.processType) || "C".equals(entity.processType)) {
			// 社内レート
			fields.addField(entity.inRto, "社内レート")
				.required()
				.numeric()
				.range(BigDecimal.ZERO, new BigDecimal("9999.9999"));

			// レートタイプ
			fields.addField(entity.rtoTp, "レートタイプ")
				.required()
				.alphaNumeric(2);

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

		// データ行の単項目単位のバリデーションを実施し、エラー内容を戻す
		return fields.validate();
	}

}
