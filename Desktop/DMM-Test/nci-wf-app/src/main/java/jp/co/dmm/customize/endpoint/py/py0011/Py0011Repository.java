package jp.co.dmm.customize.endpoint.py.py0011;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.CrdcrdInf;
import jp.co.dmm.customize.jpa.entity.mw.CrdcrdInfPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * クレカ明細消込画面のリポジトリ
 */
@ApplicationScoped
public class Py0011Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;

	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0011SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0011_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Py0011SearchRequest req, Py0011SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0011_01").replaceFirst(REPLACE, getSql("PY0011_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0011Entity.class, sql, params.toArray());
	}

	private void fillCondition(Py0011SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(LoginInfo.get().getLocaleCode());

		// 支払月
		if (isNotEmpty(req.payYm)) {
			sql.append(" and C.PAY_YM = replace(?, '/', '') ");
			params.add(req.payYm);
		}
		// 取引先
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and S.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// ユーザ
		if (isNotEmpty(req.usrNm)) {
			sql.append(" and U.USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.usrNm));
		}
		// 利用日
		if (isNotEmpty(req.useDtFrom)) {
			sql.append(" and ? <= C.USE_DT");
			params.add(req.useDtFrom);
		}
		if (isNotEmpty(req.useDtTo)) {
			sql.append(" and C.USE_DT <= ?");
			params.add(req.useDtTo);
		}
		// ステータス
		List<String> matStsList = new ArrayList<>();
		if (req.matSts0) matStsList.add("0");
		if (req.matSts1) matStsList.add("1");
		if (!matStsList.isEmpty()) {
			sql.append(" and " + toInListSql("C.MAT_STS", matStsList.size()));
			params.addAll(matStsList);
		}
		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	public Long getScreenProcessId(String companyCd, String screenCode) {
		MwmScreenProcessDef r = selectOne(MwmScreenProcessDef.class, getSql("PY0011_03"), new Object[] {companyCd, screenCode});
		return r == null ? null : r.getScreenProcessId();
	}

	public void update(List<Py0011InputEntity> entitys) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		for (Py0011InputEntity e : entitys) {
			CrdcrdInf entity = getCrdcrdInf(e.companyCd, e.crdcrdInNo);
			entity.setMatSts(CommonFlag.ON);
			entity.setItmexpsCd1(e.itmexpsCd1);
			entity.setItmexpsCd2(e.itmexpsCd2);
			entity.setItmCd(e.itmCd);
			entity.setBumonCd(e.bumonCd);
			entity.setCorporationCodeUpdated(login.getCorporationCode());
			entity.setUserCodeUpdated(login.getUserCode());
			entity.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
			entity.setTimestampUpdated(timestamp());
			em.merge(entity);
		}
		em.flush();
	}

	private CrdcrdInf getCrdcrdInf(String companyCd, String crdcrdInNo) {
		CrdcrdInfPK key = new CrdcrdInfPK();
		key.setCompanyCd(companyCd);
		key.setCrdcrdInNo(crdcrdInNo);
		return em.find(CrdcrdInf.class, key);
	}

	public String createPaySQL(boolean rcvinsp, UserInfo startUserInfo, List<Py0011InputEntity> entitys, List<Object> params) {
		StringBuilder sql = new StringBuilder();
		params.add(startUserInfo.getUserName());
		params.add(startUserInfo.getOrganizationCode5());
		params.add(startUserInfo.getOrganizationName5());
		params.add(startUserInfo.getExtendedInfo01());
		params.add(startUserInfo.getSbmtrAddr());
		params.add(startUserInfo.getOrganizationCodeUp3());
		params.add(entitys.get(0).companyCd);

		StringBuilder replace = new StringBuilder();
		if (rcvinsp) {
			params.add(entitys.get(0).rcvinspNo);
			params.add(entitys.get(0).rcvinspDtlNo);
			params.add(sessionHolder.getLoginInfo().getLocaleCode());

			for (Py0011InputEntity entity : entitys) {
				replace.append(replace.length() == 0 ? "(?, ?, ?)" : ", (?, ?, ?)");
				params.add(entity.crdcrdInNo);
				params.add(entity.rcvinspNo);
				params.add(entity.rcvinspDtlNo);
			}
		} else {
			params.add(sessionHolder.getLoginInfo().getLocaleCode());
			for (Py0011InputEntity entity : entitys) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(entity.crdcrdInNo);
			}
		}
		params.add(startUserInfo.getOrganizationCode());
		sql.append(getSql(rcvinsp ? "PY0011_04" : "PY0011_05").replaceFirst("###REPLACE###", replace.toString()));
		return sql.toString();
	}

	public String createPaydtlSQL(boolean rcvinsp, UserInfo startUserInfo, List<Py0011InputEntity> entitys, List<Object> params) {
		StringBuilder sql = new StringBuilder();
		params.add(startUserInfo.getOrganizationCodeUp3());
		params.add(entitys.get(0).companyCd);

		StringBuilder replace = new StringBuilder();
		if (rcvinsp) {
			params.add(entitys.get(0).rcvinspNo);
			params.add(sessionHolder.getLoginInfo().getLocaleCode());
			for (Py0011InputEntity entity : entitys) {
				replace.append(replace.length() == 0 ? "(?, ?, ?)" : ", (?, ?, ?)");
				params.add(entity.crdcrdInNo);
				params.add(entity.rcvinspNo);
				params.add(entity.rcvinspDtlNo);
			}
		} else {
			for (Py0011InputEntity entity : entitys) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(entity.crdcrdInNo);
			}
			params.add(sessionHolder.getLoginInfo().getLocaleCode());
		}
		sql.append(getSql(rcvinsp ? "PY0011_06" : "PY0011_07").replaceFirst("###REPLACE###", replace.toString()));
		return sql.toString();
	}
}
