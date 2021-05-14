package jp.co.nci.iwf.endpoint.unsecure.restore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.MwtResetPassword;

/**
 * パスワード復旧画面のリポジトリ
 */
@ApplicationScoped
public class RestorePasswordRepository extends BaseRepository {
	@Inject private NumberingService numbering;

	/**
	 * パスワードリセットエントリを登録
	 * @param user パスワードリセットする対象ユーザ
	 * @param newPassword 新パスワード（平文）
	 * @return
	 */
	public long insertMwtResetPassword(WfmUser user, String newPassword) {
		final long resetPasswordId = numbering.newPK(MwtResetPassword.class);
		final MwtResetPassword p = new MwtResetPassword();
		p.setCorporationCode(user.getCorporationCode());
		p.setDeleteFlag(DeleteFlag.OFF);
		p.setIssueDatetime(timestamp());
		p.setPassword(SecurityUtils.hash(newPassword));
		p.setResetDatetime(null);
		p.setResetPasswordId(resetPasswordId);
		p.setUserCode(user.getUserCode());
		p.setVersion(1L);
		em.persist(p);
		em.flush();
		return resetPasswordId;
	}

}
