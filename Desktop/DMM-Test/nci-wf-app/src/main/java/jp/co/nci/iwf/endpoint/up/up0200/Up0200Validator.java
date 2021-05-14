package jp.co.nci.iwf.endpoint.up.up0200;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.JobType;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Organization;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Post;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200User;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200UserBelong;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200Book;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetOrganization;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetPost;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.FieldValidators;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * プロファイル情報アップロードのバリデーター
 */
@ApplicationScoped
public class Up0200Validator extends MiscUtils {
	@Inject private I18nService i18n;
	@Inject private SessionHolder sessionHolder;

	/**
	 * バリデーション実行
	 * @param book
	 * @return
	 */
	public boolean validate(Up0200Book book) {
		int errors = 0;
		errors += validateOrg(book);
		errors += validatePost(book);
		errors += validateUser(book);
		errors += validateUserBelong(book);

		return errors == 0;
	}

	/** ユーザ所属のバリデーション */
	private int validateUserBelong(Up0200Book book) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final Set<String> userCodesJobType = new HashSet<>(book.sheetUser.users.size());
		final Set<String> duplicates = new HashSet<>(book.sheetUser.users.size());

		int allErrors = 0;
		for (Up0200UserBelong ub : book.sheetUserBelong.userBelongs) {
			final FieldValidators fields = new FieldValidators(i18n);

			// ユーザCDチェック用、企業コード＋ユーザコード
			final String self = Up0200Service.toKey(ub.corporationCode, ub.userCode, ub.seqNoUserBelong);
			final String codeOrg = Up0200Service.toKey(ub.corporationCode, ub.organizationCode);
			final String codePost = Up0200Service.toKey(ub.corporationCode, ub.postCode);
			final String codeUser = Up0200Service.toKey(ub.corporationCode, ub.userCode);

			// 会社コード
			fields.addField(ub.corporationCode, MessageCd.corporationCode)
					.required()
					.master(book.existCorporationCodes)
					.alphaNumeric(10)
					.myCorporationCode(u.getCorporationCode());
			// ユーザコード
			fields.addField(ub.userCode, MessageCd.userCode)
					.required()
					.master(book.existUserCodes, null, codeUser)
					.duplicate(duplicates, self);
			// 所属連番
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(ub.strSeqNoUserBelong, MessageCd.seqNoUserBelong)
				.required()	//	空白でないこと
				.numeric()	//	数値であること
				.alphaNumeric(10)	//	10桁以内で入力
				.min("1"); //	1以上であること
			// 組織コード
			fields.addField(ub.organizationCode, MessageCd.organizationCode)
					.required()
					.master(book.existsOrganizationCodes, null, codeOrg);
			// 役職コード
			fields.addField(ub.postCode, MessageCd.postCode)
				.required()
				.master(book.existsPostCodes, null, codePost);
			// 主務兼務区分
			// ※同一ユーザで複数の主務がないことのチェックは、この後で別途行っている
			fields.addField(ub.jobType, MessageCd.jobType).required().include(book.jobTypes);
			// 所属長フラグ
			fields.addField(ub.immediateManagerFlag, MessageCd.immediateManagerFlag).include(book.commonFlags);
			// 責任者フラグ
			fields.addField(ub.directorFlag, MessageCd.directorFlag).include(book.commonFlags);
			// 有効期間開始年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(ub.validStartDate, MessageCd.validStartDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(ub.validStartDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 有効期間終了年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(ub.validEndDate, MessageCd.validEndDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(ub.validStartDate, MessageCd.validStartDate);	// 開始日より大きいこと
			fields.addField(ub.validEndDate, MessageCd.validEndDate)
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(ub.validEndDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること

			// 削除フラグ
			fields.addField(ub.deleteFlag, MessageCd.deleteFlag)
					.required()
					.include(book.deleteFlags);

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();

			// 同一ユーザで複数の主務があればエラー
			if (eq(JobType.MAIN, ub.jobType) && eq(DeleteFlag.ON, ub.deleteFlag)) {
					if (userCodesJobType.contains(codeUser)) {
						errors.add(i18n.getText(MessageCd.MSG0121, MessageCd.jobType, ub.jobType));
					}
					userCodesJobType.add(codeUser);
			}
			ub.errorText = String.join(" ", errors);

			// エラーなしであれば、この「企業コード＋ユーザコード＋ユーザ所属連番」を有効データとして存在チェックで使用できるようにする
			if (isEmpty(ub.errorText)) {
				// 重複チェック用（EXCELデータのみ）
				duplicates.add(self);
			}

			allErrors += errors.size();
		}
		return allErrors;
	}

	/** ユーザのバリデーション */
	private int validateUser(Up0200Book book) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final Set<String> duplicates = new HashSet<>(book.sheetUser.users.size());
		int allErrors = 0;
		for (Up0200User user : book.sheetUser.users) {
			final FieldValidators fields = new FieldValidators(i18n);

			// ユーザCDチェック用、企業コード＋ユーザコード
			final String self = Up0200Service.toKey(user.corporationCode, user.userCode);

			// 企業コード
			fields.addField(user.corporationCode, MessageCd.corporationCode)
					.required()
					.master(book.existCorporationCodes)
					.alphaNumeric(10)
					.myCorporationCode(u.getCorporationCode());
			// ユーザコード
			fields.addField(user.userCode, MessageCd.userCode)
					.required()
					.alphaNumeric(25)
					.duplicate(duplicates, self);
			// ユーザ名称
			fields.addField(user.userNameJa, MessageCd.userNameJa).required().anyString(100);
			fields.addField(user.userNameEn, MessageCd.userNameEn).anyString(100);
			fields.addField(user.userNameZh, MessageCd.userNameZh).anyString(100);
			// ユーザ略称
			fields.addField(user.userNameAbbrJa, MessageCd.userNameAbbrJa).anyString(100);
			fields.addField(user.userNameAbbrJa, MessageCd.userNameAbbrEn).anyString(100);
			fields.addField(user.userNameAbbrJa, MessageCd.userNameAbbrZh).anyString(100);
			// 郵便番号
			fields.addField(user.postNum, MessageCd.postNum).anyString(10);
			// 住所
			fields.addField(user.addressJa, MessageCd.addressJa).anyString(300);
			fields.addField(user.addressEn, MessageCd.addressEn).anyString(300);
			fields.addField(user.addressZh, MessageCd.addressZh).anyString(300);
			// 電話番号
			fields.addField(user.telNum, MessageCd.telNum).anyString(20);
			//携帯電話番号
			fields.addField(user.telNumCel, MessageCd.telNumCel).anyString(20);
			// メールアドレス
			fields.addField(user.mailAddress, MessageCd.mailAddress).anyString(320);
			// ユーザID
			fields.addField(user.userAddedInfo, MessageCd.userAddedInfo).anyString(50);
			// 印章名称
			fields.addField(user.sealName, MessageCd.sealName).anyString(30);
			// 管理者区分
			fields.addField(user.administratorType, MessageCd.administratorType)
					.anyString(1)
					.include(book.administratorTypes);
			//デフォルト言語コード
			fields.addField(user.defaultLocaleCode, MessageCd.defaultLocaleCode)
					.anyString(2)
					.required()
					.include(book.localeCodes);
			// 拡張情報01～10
			for (int i = 0; i < 10; i++) {
				String fieldName = String.format("extendedInfo%02d", i + 1);
				String value = MiscUtils.getFieldValue(user, fieldName);
				fields.addField(value, fieldName).anyString(30);
			}
			// 有効期間開始年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(user.validStartDate, MessageCd.validStartDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(user.validStartDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 有効期間終了年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(user.validEndDate, MessageCd.validEndDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(user.validStartDate, MessageCd.validStartDate);	// 開始日より大きいこと
			fields.addField(user.validEndDate, MessageCd.validEndDate)
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(user.validEndDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 削除フラグ
			fields.addField(user.deleteFlag, MessageCd.deleteFlag)
					.required()
					.anyString(1)
					.include(book.deleteFlags);

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();
			user.errorText = String.join(" ", errors);

			// エラーなしであれば、このユーザコードを有効データとして存在チェックで使用できるようにする
			if (isEmpty(user.errorText)) {
				// 存在チェック用（＝既存データ＋EXCELデータ）
				book.existUserCodes.add(self);
				// 重複チェック用（EXCELデータのみ）
				duplicates.add(self);
			}

			allErrors += errors.size();
		}
		return allErrors;
	}

	/** 役職のバリデーション */
	private int validatePost(Up0200Book book) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final Up0200SheetPost sheet = book.sheetPost;
		final Set<String> duplicates = new HashSet<>(sheet.posts.size());
		int allErrors = 0;
		for (Up0200Post post : sheet.posts) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 役職CDチェック用、企業コード＋役職コード
			final String self = Up0200Service.toKey(post.corporationCode, post.postCode);

			// 会社コード
			fields.addField(post.corporationCode, MessageCd.corporationCode)
				.required()
				.master(book.existCorporationCodes)
				.alphaNumeric(10)
				.myCorporationCode(u.getCorporationCode());
			// 役職コード
			fields.addField(post.postCode, MessageCd.postCode).required().alphaNumeric(10).duplicate(duplicates, self);
			// 役職名称
			fields.addField(post.postNameJa, MessageCd.postNameJa).required().anyString(100);
			fields.addField(post.postNameEn, MessageCd.postNameEn).anyString(100);
			fields.addField(post.postNameZh, MessageCd.postNameZh).anyString(100);
			//役職付加情報
			fields.addField(post.postAddedInfo, MessageCd.postAddedInfo).required().anyString(50);
			//役職略称
			fields.addField(post.postNameAbbrJa, MessageCd.postNameAbbrJa).anyString(100);
			fields.addField(post.postNameAbbrEn, MessageCd.postNameAbbrEn).anyString(100);
			fields.addField(post.postNameAbbrZh, MessageCd.postNameAbbrZh).anyString(100);
			//役職階層
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(post.strPostLevel, MessageCd.postLevel)
				.numeric();	//	数値であること
			fields.addField(post.postLevel, MessageCd.postLevel)
				.range(-999L, 999L);
			//上位役職者設定フラグ
			fields.addField(post.uppperPostSettingsFlag, MessageCd.uppperPostSettingsFlag)
					.anyString(1)
					.include(book.commonFlags);
			// 有効期間開始年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(post.validStartDate, MessageCd.validStartDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(post.validStartDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 有効期間終了年月日
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(post.validEndDate, MessageCd.validEndDate)
				.required()	//	空白でないこと
				.date() //	日付形式であること
				.gte(post.validStartDate, MessageCd.validStartDate);	// 開始日より大きいこと
			fields.addField(post.validEndDate, MessageCd.validEndDate)
				.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
			fields.addField(post.validEndDate, MessageCd.validEndDate)
				.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 削除フラグ
			fields.addField(post.deleteFlag, MessageCd.deleteFlag)
					.required()
					.include(book.deleteFlags);

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();
			post.errorText = String.join(" ", errors);

			// エラーなしであれば、この組織コードを有効データとして存在チェックで使用できるようにする
			if (isEmpty(post.errorText)) {
				// 存在チェック用（＝既存データ＋EXCELデータ）
				book.existsPostCodes.add(self);
				// 重複チェック用（EXCELデータのみ）
				duplicates.add(self);
			}

			allErrors += errors.size();
		}
		return allErrors;
	}

	/** 組織のバリデーション */
	private int validateOrg(Up0200Book book) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final Up0200SheetOrganization sheet = book.sheetOrg;
		final Set<String> duplicates = new HashSet<>(sheet.organizations.size());
		int allErrors = 0;

		for (Up0200Organization org : sheet.organizations) {
			final FieldValidators fields = new FieldValidators(i18n);

			// 組織CDチェック用、企業コード＋組織コード
			final String self = Up0200Service.toKey(org.corporationCode, org.organizationCode);
			// 上位組織CDチェック用、企業コード＋上位組織コード
			final String up = Up0200Service.toKey(org.corporationCode, org.organizationCodeUp);

			// 企業コード
			fields.addField(org.corporationCode, MessageCd.corporationCode)
					.required()
					.master(book.existCorporationCodes)
					.alphaNumeric(10)
					.myCorporationCode(u.getCorporationCode());
			// 組織コード
			fields.addField(org.organizationCode, MessageCd.organizationCode)
					.required()
					.alphaNumeric(10)
					.duplicate(duplicates, self);
			// 組織名称
			fields.addField(org.organizationNameJa, MessageCd.organizationNameJa).required().anyString(100);
			fields.addField(org.organizationNameEn, MessageCd.organizationNameEn).anyString(100);
			fields.addField(org.organizationNameZh, MessageCd.organizationNameZh).anyString(100);
			// 組織付加情報
			fields.addField(org.organizationAddedInfo, MessageCd.organizationAddedInfo).required().anyString(50);
			// 組織略称
			fields.addField(org.organizationNameAbbrJa, MessageCd.organizationNameAbbrJa).anyString(100);
			fields.addField(org.organizationNameAbbrEn, MessageCd.organizationNameAbbrEn).anyString(100);
			fields.addField(org.organizationNameAbbrZh, MessageCd.organizationNameAbbrZh).anyString(100);
			// 上位組織コード
			fields.addField(org.organizationCodeUp, MessageCd.organizationCodeUp)
//					.required()	/* 未指定のときは自動的に第一階層となるよう、更新時に上位組織CD＝組織CDとしているので必須としない */
					.master(book.existsOrganizationCodes, self, up)	/* 存在チェックは企業コード＋組織コードなので、単純比較できない */
					.alphaNumeric(10);
			// 郵便番号
			fields.addField(org.postNum, MessageCd.postNum).anyString(10);
			// 住所
			fields.addField(org.addressJa, MessageCd.addressJa).anyString(300);
			fields.addField(org.addressEn, MessageCd.addressEn).anyString(300);
			fields.addField(org.addressZh, MessageCd.addressZh).anyString(300);
			// 電話番号
			fields.addField(org.telNum, MessageCd.telNum).anyString(20);
			// FAX番号
			fields.addField(org.faxNum, MessageCd.faxNum).anyString(20);
			// 組織階層
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(org.strOrganizationLevel, MessageCd.organizationLevel)
				.numeric();	//	数値であること
			fields.addField(org.organizationLevel, MessageCd.organizationLevel)
				.range(-9999L, 9999L);
			// ソート順
			// 2019/11/05 Excelアップロードバグの対応
			fields.addField(org.strSortOrder, MessageCd.sortOrder)
			.numeric();	//	数値であること
			fields.addField(org.sortOrder, MessageCd.sortOrder)
			.range(-9999L, 9999L);
			// 拡張情報01～10
			for (int i = 0; i < 10; i++) {
				String fieldName = String.format("extendedInfo%02d", i + 1);
				String value = (String)MiscUtils.getFieldValue(org, fieldName);
				fields.addField(value, fieldName).anyString(30);
			}
			// 有効期間開始年月日
						// 2019/11/05 Excelアップロードバグの対応
						fields.addField(org.validStartDate, MessageCd.validStartDate)
							.required()	//	空白でないこと
							.date() //	日付形式であること
							.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
						fields.addField(org.validStartDate, MessageCd.validEndDate)
							.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
						// 有効期間終了年月日
						// 2019/11/05 Excelアップロードバグの対応
						fields.addField(org.validEndDate, MessageCd.validEndDate)
							.required()	//	空白でないこと
							.date() //	日付形式であること
							.gte(org.validStartDate, MessageCd.validStartDate);	// 開始日より大きいこと
						fields.addField(org.validEndDate, MessageCd.validEndDate)
							.gte(STARTDATE, MessageCd.validTerm); //	1970年以降であること
						fields.addField(org.validEndDate, MessageCd.validEndDate)
							.lte(ENDDATE, MessageCd.validTerm);	//	2100年以前であること
			// 削除フラグ
			fields.addField(org.deleteFlag, MessageCd.deleteFlag)
					.required()
					.include(book.deleteFlags);

			// 行単位のバリデーションとその結果をエンティティに書き戻し
			final List<String> errors = fields.validate();
			org.errorText = String.join(" ", errors);

			// エラーなしであれば、この組織コードを有効データとして存在チェックで使用できるようにする
			if (isEmpty(org.errorText)) {
				// 存在チェック用（＝既存データ＋EXCELデータ）
				book.existsOrganizationCodes.add(self);
				// 重複チェック用（EXCELデータのみ）
				duplicates.add(self);
			}

			allErrors += errors.size();
		}
		return allErrors;
	}
}
