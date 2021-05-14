package jp.co.nci.iwf.endpoint.au.au0011;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserPasswordImpl;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.WfvUserPassword;
import jp.co.nci.iwf.jpa.entity.wm.WfvCorporationProperty;

/**
 * パスワード変更画面(通常)サービス
 */
@BizLogic
@Typed(Au0011Service.class)
public class Au0011Service extends BaseService {
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private Logger log;
	@Inject
	private Au0011Repository repository;
	@Inject
	private CorporationPropertyService prop;

	/** 数字の正規表現 */
	protected static final Pattern NUMBER = Pattern.compile("\\d+");
	/** 小文字の正規表現 */
	protected static final Pattern LOWER = Pattern.compile("[a-z]+");
	/** 大文字の正規表現 */
	protected static final Pattern UPPER = Pattern.compile("[A-Z]+");
	/** 記号の正規表現 */
	protected static final Pattern MARK = Pattern.compile("[!-/:-@≠\\[-`{-~]+");


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Au0011Response init(Au0011Request req) {
		// 通常のパスワード変更画面は操作者専用なので、もしパラメータが渡ってきたとしても操作者以外のであれば間違いである
		final LoginInfo u = sessionHolder.getLoginInfo();
		if (isNotEmpty(req.corporationCode) && !eq(u.getCorporationCode(), req.corporationCode))
			throw new BadRequestException("企業コードが指定されています");
		if (isNotEmpty(req.userCode) && !eq(u.getUserCode(), req.userCode))
			throw new BadRequestException("ユーザコードが指定されています");

		final Au0011Response res = createResponse(Au0011Response.class, req);

		// 対象者が未設定なら、操作者自身とする
		if (isEmpty(req.corporationCode) || isEmpty(req.userCode)) {
			req.corporationCode = u.getCorporationCode();
			req.userCode = u.getUserCode();

			// パスワード変更要求ありか？
			if (u.isChangePassword()) {
				res.addWarns(i18n.getText(MessageCd.MSG0150));
			}
		}

		// ユーザ情報＋最新のパスワード情報
		loadPasswordInfo(req, res);

		res.success = true;

		return res;
	}

	/** パスワード情報＋システムプロパティ＋変更履歴を読み込む */
	protected WfvUserPassword loadPasswordInfo(Au0011Request req, Au0011Response res) {
		// ユーザ情報＋最新のパスワード情報
		final WfvUserPassword pswd = getWfvUserPassword(req);
		if (pswd == null) {
			// 該当レコードなしなので、続行不能だ
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.noRecord));
			log.error("企業コード={} ユーザコード={}のWFV_USER_PASSWORDが見つかりません。",
					req.corporationCode, req.userCode);
		} else {
			// 現在のパスワード情報
			res.password = pswd;

			// パスワード変更履歴
			res.histories = getHistories(req);

			// システム環境情報
			res.corpProperties = getProperties(req);
		}
		return pswd;
	}

	/** パスワード変更履歴を抽出 */
	private List<Au0011History> getHistories(Au0011Request req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = req.corporationCode;
		final String userCode = req.userCode;

		final List<WfvUserPassword> list = repository.getHistories(corporationCode, userCode, localeCode);
		final List<Au0011History> results = new ArrayList<>(list.size());
		int i = 0;
		for (WfvUserPassword p : list) {
			Au0011History h = new Au0011History();
			h.loginNgCount = p.getLoginNgCount();
			h.passwordStatus = toPasswordStatus(p);
			h.rowNo = ++i;
			h.validStartDate = toStr(p.getValidStartDate());
			h.passwordChangeRequest = p.getChangeRequestFlagName();
			results.add(h);
		}
		return results;
	}

	/** パスワードビューを抽出 */
	private WfvUserPassword getWfvUserPassword(Au0011Request req) {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = req.corporationCode;
		final String userCode = req.userCode;

		final String localeCode = u.getLocaleCode();
		WfvUserPassword pswd = repository.get(corporationCode, userCode, localeCode);
		return pswd;
	}

	/**
	 * パスワード状況
	 * @param pswd
	 * @return
	 */
	private String toPasswordStatus(WfvUserPassword pswd) {
		if (CommonFlag.OFF.equals(pswd.getExistPasswordFlag()))
			return i18n.getText(MessageCd.notRegistered);
		if (CommonFlag.ON.equals(pswd.getLockFlag()))
			return i18n.getText(MessageCd.locking);
		if (CommonFlag.ON.equals(pswd.getPasswordExpiredFlag()))
			return i18n.getText(MessageCd.outOfDate);

		return i18n.getText(MessageCd.registered);
	}

	/** 企業属性マスタ抽出 */
	private List<Au0011CorpProperty> getProperties(Au0011Request req) {
		final String corporationCode = req.corporationCode;
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final List<String> props = Arrays.asList(
				CorporationProperty.MIN_PASSWORD_LENGTH.toString(),
				CorporationProperty.ALLOWED_LOGINID_CONTAIN.toString(),
				CorporationProperty.PASSWORD_COMPLEXITY.toString(),
				CorporationProperty.PASSWORD_VALIDITY_GENERATION.toString()
		);
		// 抽出
		final List<WfvCorporationProperty> list = repository.getCorpProperties(corporationCode, props, localeCode);
		final List<Au0011CorpProperty> results = new ArrayList<>();
		for (WfvCorporationProperty src : list) {
			Au0011CorpProperty dest = new Au0011CorpProperty();
			dest.setPropertyCode(src.getPropertyCode());
			dest.setPropertyName(src.getPropertyName());

			String code = src.getPropertyCode();
			String v = src.getPropertyValue();
			String value = "";
			if (CorporationProperty.PASSWORD_COMPLEXITY.equals(code))
				value = ("true".equalsIgnoreCase(v) ? i18n.getText(MessageCd.yes) : i18n.getText(MessageCd.no));
			else if (CorporationProperty.ALLOWED_LOGINID_CONTAIN.equals(code))
				value = ("true".equalsIgnoreCase(v) ? i18n.getText(MessageCd.yes) : i18n.getText(MessageCd.no));
			else
				value = v;
			dest.setPropertyValue(value);
			results.add(dest);
		}
		return results;
	}

	/**
	 * パスワードの保存
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Au0011Request req) {
		final Au0011Response res = createResponse(Au0011Response.class, req);

		// 対象ユーザ特定
		final WfmUserPassword pswd = toWfmUserPassword(req);

		// バリデーション
		final List<String> alerts = validate(req, pswd);
		res.alerts = alerts;

		if (alerts == null || alerts.isEmpty()) {
			// 既存パスワードを論理削除
			int count = repository.disableOldPassword(pswd, sessionHolder.getWfUserRole());
			log.debug("既存パスワードを{}件論理削除しました", count);
			// 新パスワードを登録
			final InsertWfmUserPasswordInParam in = new InsertWfmUserPasswordInParam();
			in.setWfmUserPassword(pswd);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmUserPasswordOutParam out = wf.insertWfmUserPassword(in);

			if (ReturnCode.SUCCESS.equals(out.getReturnCode())) {
				res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.password));
				res.success = true;
			} else {
				res.addAlerts(out.getReturnMessage());
			}

			// データを読み直し
			loadPasswordInfo(req, res);

			// 書き換えたのが自分自身なら、パスワード変更要求を落とす
			final LoginInfo login = sessionHolder.getLoginInfo();
			if (eq(login.getCorporationCode(), pswd.getCorporationCode())
					&& eq(login.getUserCode(), pswd.getUserCode())
			) {
				login.setChangePassword(false);
			}
		}
		return res;
	}

	/** ユーザパスワードをモデル化 */
	protected WfmUserPassword toWfmUserPassword(Au0011Request req) {
		// パスワードを暗号化
		final String encrypt = (isEmpty(req.newPassword1) ? null : SecurityUtils.hash(req.newPassword1));

		final WfmUserPassword p = new WfmUserPasswordImpl();
		p.setCorporationCode(req.corporationCode);
		p.setUserCode(req.userCode);
		p.setDeleteFlag(DeleteFlag.OFF);
		p.setLockFlag(CommonFlag.OFF);
		p.setChangeRequestFlag(CommonFlag.OFF);
		p.setLoginNgCount(0L);
		p.setPassword(encrypt);
		p.setValidStartDate(now());

		return p;
	}

	/** バリデーション */
	private List<String> validate(Au0011Request req, WfmUserPassword pswd) {
		final List<String> alerts = new ArrayList<>();

		alerts.addAll(validateAu0011Only(req, pswd));

		// パスワードは必須
		String field = i18n.getText(MessageCd.newPassword);
		String field2 = i18n.getText(MessageCd.newPassword2);
		final String plain = req.newPassword1;	// 平文パスワード

		if (isEmpty(plain)) {
			alerts.add(i18n.getText(MessageCd.MSG0001, field));
		}
		else {
			// (平文の)パスワード桁数が30桁以下であること
			final int MAX_LENGTH = 30;
			if (plain.length() > MAX_LENGTH)
				alerts.add(i18n.getText(MessageCd.MSG0021, field, MAX_LENGTH));

			// パスワード１とパスワード２が同じであること
			if (!eq(req.newPassword1, req.newPassword2))
				alerts.add(i18n.getText(MessageCd.MSG0059, field, field2));

			// [企業属性依存] 最低パスワード桁数以上であること
			Integer min = prop.getInt(CorporationProperty.MIN_PASSWORD_LENGTH);
			if (min != null && plain.length() < min)
				alerts.add(i18n.getText(MessageCd.MSG0021, field, min));

			// [企業属性依存] パスワードにログインIDが含まれていないこと
			Boolean containsLoginId = prop.getBool(CorporationProperty.ALLOWED_LOGINID_CONTAIN);
			if (containsLoginId != null && !containsLoginId) {
				String userAddedInfo = getUser(pswd).getUserAddedInfo();
				if (plain != null && plain.indexOf(userAddedInfo) >= 0)
					alerts.add(i18n.getText(MessageCd.MSG0033, field, i18n.getText(MessageCd.userAddedInfo)));
			}
			// [企業属性依存] パスワードに文字種が3つ以上で構成されていること（数字／英(大)／英(小)／記号）
			Boolean complexity = prop.getBool(CorporationProperty.PASSWORD_COMPLEXITY);
			if (complexity != null && complexity) {
				int count = getCharTypeCount(plain);
				if (count < 3)
					alerts.add(i18n.getText(MessageCd.MSG0058, field));
			}
			// [企業属性依存] 過去X世代で同一パスワードを使用していないこと
			Integer generations = prop.getInt(CorporationProperty.PASSWORD_VALIDITY_GENERATION);
			if (generations != null && generations > 0) {
				final List<WfvUserPassword> olds = repository.getHistories(
						pswd.getCorporationCode(), pswd.getUserCode(), sessionHolder.getLoginInfo().getLocaleCode());
				// １件目は現在なのでスキップし、2件目から
				for (int i = 1; i < olds.size() && i <= generations; i++) {
					final WfvUserPassword old = olds.get(i);
					if (old.getPassword().equals(pswd.getPassword())) {
						alerts.add(i18n.getText(MessageCd.MSG0061, field, generations));
						break;
					}
				}
			}
		}

		return alerts;
	}

	/** 通常のパスワード画面専用のバリデーション */
	protected List<String> validateAu0011Only(Au0011Request req, WfmUserPassword pswd) {
		List<String> alerts = new ArrayList<>();
		LoginInfo u = sessionHolder.getLoginInfo();
		if (!eq(req.corporationCode, u.getCorporationCode())
				|| !eq(req.userCode, u.getUserCode())) {
			throw new ForbiddenException("操作者以外のパスワードを変更しようとしていますが、許されません");
		}
		return alerts;
	}

	/** 使用されている文字種数を返す */
	private int getCharTypeCount(String s) {
		if (isEmpty(s)) return 0;

		int count = 0;
		if (NUMBER.matcher(s).find())
			count++;
		if (UPPER.matcher(s).find())
			count++;
		if (LOWER.matcher(s).find())
			count++;
		if (MARK.matcher(s).find())
			count++;
		return count;
	}

	/** ユーザマスタ抽出 */
	protected WfmUser getUser(WfmUserPassword pswd) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(pswd.getCorporationCode());
		in.setUserCode(pswd.getUserCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		List<WfmUser> list = wf.searchWfmUser(in).getUserList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}
}
