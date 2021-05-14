package jp.co.dmm.customize.endpoint.py.py0010;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.CrdcrdInf;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;

/**
 * クレカ明細データ取込画面リポジトリ.
 */
@ApplicationScoped
public class Py0010Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;
	@Inject protected WfInstanceWrapper wf;

	public boolean existsSplrMst(String companyCd, String splrCd) {
		return count(getSql("PY0010_01"), new Object[] {companyCd, splrCd}) > 0;
	}

	public boolean existsUser(String companyCd, String usrCd) {
		wf.setLocale(sessionHolder.getLoginInfo().getLocaleCode());

		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(companyCd);
		in.setUserAddedInfo(usrCd);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {new OrderBy(true, WfcUserBelong.JOB_TYPE)});

		return isNotEmpty(wf.searchWfvUserBelong(in).getUserBelongList());
	}

	public boolean existsCrdcrdBnkaccMst(String companyCd, String splrCd, String usrCd) {
		return count(getSql("PY0010_02"), new Object[] {companyCd, splrCd, usrCd}) > 0;
	}


	/** パーツ採番形式IDの取得. */
	public Long getPartsNumberingFormatId(String corporationCode, String partsNumberingFormatCode) {
		final Object[] params = { corporationCode, partsNumberingFormatCode };
		final MwmPartsNumberingFormat entity = selectOne(MwmPartsNumberingFormat.class, getSql("PO0040_10"), params);
		if (entity != null) {
			em.detach(entity);
			return entity.getPartsNumberingFormatId();
		}
		return null;
	}

	public void save(CrdcrdInf entity) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		entity.setDltFg("0");
		entity.setCorporationCodeCreated(login.getCorporationCode());
		entity.setUserCodeCreated(login.getUserCode());
		entity.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
		entity.setTimestampCreated(timestamp());
		entity.setCorporationCodeUpdated(login.getCorporationCode());
		entity.setUserCodeUpdated(login.getUserCode());
		entity.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		entity.setTimestampUpdated(timestamp());
		em.persist(entity);
	}
}
