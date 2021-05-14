package jp.co.nci.iwf.endpoint.unsecure.reset;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserPasswordImpl;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Repository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.MwtResetPassword;

/**
 * パスワードリセット画面サービス
 */
@BizLogic
public class ResetPasswordService extends BaseService {
	@Inject private WfInstanceWrapper wf;
	@Inject private Au0011Repository repository;

	/**
	 * パスワードリセット
	 * @param req
	 * @return
	 */
	@Transactional
	public ResetPasswordResponse resetPassword(ResetPasswordRequest req) {
		final ResetPasswordResponse res = createResponse(ResetPasswordResponse.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			// 復号化
			final String id = SecurityUtils.decrypt(req.hash);
			final long resetPasswordId = Long.valueOf(id);
			final MwtResetPassword pswd = repository.getMwtResetPassword(resetPasswordId);
			final Timestamp now = timestamp();
			final int maxHours = 24;
			if (pswd == null)
				// パスワードリセット要求されましたが、該当する仮パスワードは発行されていません。
				res.addAlerts(i18n.getText(MessageCd.MSG0180));
			else if (pswd.getResetDatetime() != null)
				// 該当する仮パスワードはすでに使用され、パスワードリセット済みです。
				res.addAlerts(i18n.getText(MessageCd.MSG0181));
			else if (now.after(toLimit(pswd.getIssueDatetime(), maxHours)))
				// 仮パスワード発行から{0}時間以上が経過しており、無効です。
				res.addAlerts(i18n.getText(MessageCd.MSG0182, maxHours));
			else {
				// リセットパスワード情報をリセット済みに更新
				pswd.setResetDatetime(now);

				// 既存パスワードを論理削除
				final WfmUserPassword up = toWfmUserPassword(pswd);
				final WfUserRole ur = toUserRole(pswd);
				repository.disableOldPassword(up, ur);

				// 新パスワードを生成
				final InsertWfmUserPasswordInParam in = new InsertWfmUserPasswordInParam();
				in.setWfmUserPassword(up);
				in.setWfUserRole(ur);
				final InsertWfmUserPasswordOutParam out = wf.insertWfmUserPassword(in);
				if (!eq(ReturnCode.SUCCESS, out.getReturnCode())) {
					// ロールバックさせたいので、例外としてスロー
					throw new WfException(out);
				}
				else {
					// パスワードのリセットを行いました。既存パスワードは無効化され、メールに記載されている仮パスワードが有効になっています。仮パスワードでログイン後は、パスワード変更してください。
					res.addSuccesses(i18n.getText(MessageCd.MSG0183));
					res.success = true;
				}
			}
		}
		else {
			res.addAlerts(error);
		}
		return res;
	}

	/** パスワードの有効期限 */
	private Timestamp toLimit(Date d, int hours) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.HOUR, hours);
		return new Timestamp(cal.getTimeInMillis());
	}

	/** リセットパスワード情報からWfUserPasswordを生成 */
	private WfmUserPassword toWfmUserPassword(MwtResetPassword src) {
		final WfmUserPassword p = new WfmUserPasswordImpl();
		p.setCorporationCode(src.getCorporationCode());
		p.setUserCode(src.getUserCode());
		p.setChangeRequestFlag(CommonFlag.ON);
		p.setDeleteFlag(DeleteFlag.OFF);
		p.setLockFlag(CommonFlag.OFF);
		p.setLoginNgCount(0L);
		p.setPassword(src.getPassword());
		p.setValidStartDate(now());
		return p;
	}

	/** バリデーション */
	private String validate(ResetPasswordRequest req) {
		if (isEmpty(req.hash))
			return i18n.getText(MessageCd.MSG0001, MessageCd.hashValue);

		// 復号化
		if (!SecurityUtils.tryDecrypt(req.hash))
			// {0}は改ざんされた可能性があります。処理を続行できません。
			return i18n.getText(MessageCd.MSG0179, MessageCd.hashValue);

		return null;
	}

	/** リセットパスワード情報からWfUserRoleを生成 */
	private WfUserRole toUserRole(MwtResetPassword pswd) {
		final WfUserRole ur = new WfUserRoleImpl();
		ur.setCorporationCode(pswd.getCorporationCode());
		ur.setUserCode(pswd.getUserCode());
		ur.setAuthTransferList(new ArrayList<>());
		try {
			ur.setIpAddress(hsr.getRemoteAddr());
		}
		catch (Exception e) {
			ur.setIpAddress("0.0.0.0");
		}
		return ur;
	}
}
