package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwtProcessBbsEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;
import jp.co.nci.iwf.jpa.entity.mw.MwtProcessBbs;

/**
 * ブロック：要説明(掲示板)リポジトリ
 */
@ApplicationScoped
public class Bl0010Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/**
	 * プロセスIDに紐付くプロセス掲示板情報を、投稿者情報を含めて抽出
	 * @param corporationCode
	 * @param processId
	 * @param localeCode
	 * @return
	 */
	public List<MwtProcessBbsEx> getProcessBbs(String corporationCode, Long processId, String localeCode) {
		final Object[] params = { localeCode, corporationCode, processId };
		return select(MwtProcessBbsEx.class, getSql("VD0310_12"), params);
	}

	/**
	 * プロセスIDに紐付くプロセス掲示板情報を、投稿者情報を含めて抽出
	 * @param corporationCode
	 * @param processId
	 * @param localeCode
	 * @return
	 */
	public List<MwtBbsAttachFileWf> getBbsAttachFile(String corporationCode, Long processId) {
		final Object[] params = { corporationCode, processId };
		return select(MwtBbsAttachFileWf.class, getSql("VD0310_24"), params);
	}

	/**
	 * プロセス掲示板情報をインサート
	 * @param entity
	 */
	public void insert(MwtProcessBbs entity) {
		entity.setProcessBbsId(numbering.newPK(MwtProcessBbs.class));
		em.persist(entity);
	}

	/**
	 * プロセス掲示板情報を論理削除
	 * @param processBbsId
	 */
	public void delete(Long processBbsId, LoginInfo login) {
		MwtProcessBbs entity = get(processBbsId);
		if (entity != null) {
			entity.setDeleteFlag(DeleteFlag.ON);
		}
		// 添付ファイルも論理削除
		final Object[] params = {
				login.getCorporationCode(),
				login.getUserCode(),
				timestamp(),
				processBbsId
		};
		execSql(getSql("VD0310_25"), params);
	}

	/**
	 * プロセス掲示板情報をPKで抽出
	 * @param processBbsId
	 * @return
	 */
	public MwtProcessBbs get(Long processBbsId) {
		if (processBbsId == null || processBbsId < 1L)
			return null;
		return em.find(MwtProcessBbs.class, processBbsId);
	}

	/**
	 * プロセス掲示板情報添付ファイルをPKで抽出
	 * @param bbsAttachFileWfId
	 * @return
	 */
	public MwtBbsAttachFileWf getAttachFile(Long bbsAttachFileWfId) {
		if (bbsAttachFileWfId == null || bbsAttachFileWfId < 1L)
			return null;
		return em.find(MwtBbsAttachFileWf.class, bbsAttachFileWfId);
	}

	public List<Bl0010AssignedUser> getAssignedUser(WftProcess proc, boolean approvedActivity) {
		final StringBuilder sql = new StringBuilder(getSql("VD0310_13"));
		final Object[] params = { proc.getCorporationCode(), proc.getProcessId() };
		if (approvedActivity) {
			sql.append(" and A.ACTIVITY_ID > 0");
		}
		return select(Bl0010AssignedUser.class, sql.toString(), params);
	}

	public Bl0010AssignedUser getWfmUser(String corporationCode, String userCode) {
		final Object[] params = { corporationCode, userCode };
		return selectOne(Bl0010AssignedUser.class, getSql("VD0310_14"), params);
	}

	public void update(MwtBbsAttachFileWf entity) {
		em.merge(entity);
		em.flush();
	}
}
