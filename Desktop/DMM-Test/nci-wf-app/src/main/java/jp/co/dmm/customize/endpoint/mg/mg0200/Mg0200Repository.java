package jp.co.dmm.customize.endpoint.mg.mg0200;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgExcelBookAccClnd;
import jp.co.dmm.customize.endpoint.mg.mg0200.excel.MgExcelEntityAccClnd;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 会計カレンダマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0200Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 会計カレンダマスタ件数
	 * @param req
	 * @return
	 */
	public int count(Mg0200Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0200_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 会計カレンダマスタ検索
	 * @param req リクエスト
	 * @param res
	 * @return
	 */
	public List<Mg0200Entity> select(Mg0200Request req, Mg0200Response res) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0200_01").replaceFirst(REPLACE, getSql("MG0200_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Mg0200Entity.class, sql, params.toArray());
	}

	/**
	 * 会計カレンダマスタ検索
	 * @param req リクエスト
	 * @param res
	 * @return
	 */
	public Map<Date, AccClndMst> getAccClndMst(Mg0200Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0200_01").replaceFirst(REPLACE, getSql("MG0200_03")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(AccClndMst.class, sql, params.toArray())
				.stream()
				.collect(Collectors.toMap(ti -> ti.getId().getClndDt(), ti -> ti));
	}

	public void insert(Mg0200Entity inputed) {
		final AccClndMst entity = new AccClndMst();
		final AccClndMstPK id = new AccClndMstPK();
		id.setCompanyCd(inputed.companyCd);
		id.setClndDt(inputed.clndDt);
		entity.setId(id);
		entity.setClndDay(inputed.clndDay);
		entity.setHldayTp(inputed.hldayTp);
		entity.setBnkHldayTp(inputed.bnkHldayTp);
		entity.setStlTpPur(inputed.stlTpPur);
		entity.setStlTpFncobl(inputed.stlTpFncobl);
		entity.setStlTpFncaff(inputed.stlTpFncaff);
		entity.setMlClsTm(inputed.mlClsTm);
		entity.setDltFg(inputed.dltFg);
		entity.setCorporationCodeCreated(inputed.corporationCodeCreated);
		entity.setUserCodeCreated(inputed.userCodeCreated);
		entity.setIpCreated(inputed.ipCreated);
		entity.setTimestampCreated(inputed.timestampCreated);
		entity.setCorporationCodeUpdated(inputed.corporationCodeUpdated);
		entity.setUserCodeUpdated(inputed.userCodeUpdated);
		entity.setIpUpdated(inputed.ipUpdated);
		entity.setTimestampUpdated(inputed.timestampUpdated);
		em.persist(entity);
	}

	public void update(Mg0200Entity inputed, AccClndMst entity) {
		entity.setClndDay(inputed.clndDay);
		entity.setHldayTp(inputed.hldayTp);
		entity.setBnkHldayTp(inputed.bnkHldayTp);
		entity.setStlTpPur(inputed.stlTpPur);
		entity.setStlTpFncobl(inputed.stlTpFncobl);
		entity.setStlTpFncaff(inputed.stlTpFncaff);
		entity.setMlClsTm(inputed.mlClsTm);
		entity.setCorporationCodeUpdated(inputed.corporationCodeUpdated);
		entity.setUserCodeUpdated(inputed.userCodeUpdated);
		entity.setIpUpdated(inputed.ipUpdated);
		entity.setTimestampUpdated(inputed.timestampUpdated);
	}

	/**
	 * 会計カレンダマスタ検索条件設定
	 * @param req
	 * @param sql
	 * @param params
	 * @param paging
	 */
	private void fillCondition(Mg0200Request req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and ACM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 年月
		if (isNotEmpty(req.clndYm)) {
			String[] ym = req.clndYm.split("/");
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
			Date dtBgn = cal.getTime();
			int lastDay = cal.getActualMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, lastDay);
			Date dtEnd = cal.getTime();
			sql.append(" and ACM.DLT_FG = ? ");
			sql.append(" and ACM.CLND_DT between ? and ? ");
			params.add(DeleteFlag.OFF);
			params.add(dtBgn);
			params.add(dtEnd);
		}

		sql.append(" order by ACM.COMPANY_CD, ACM.CLND_DT");
	}

	public AccClndMst getByPk(String companyCd, Date clndDt) {
		AccClndMstPK id = new AccClndMstPK();
		id.setCompanyCd(companyCd);
		id.setClndDt(clndDt);

		return em.find(AccClndMst.class, id);
	}

	public List<MgExcelEntityAccClnd> getMasterData(Mg0200Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0200_01").replaceFirst(REPLACE, getSql("MG0200_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityAccClnd.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookAccClnd book) {

		// 会社コード
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE AS CODE_VALUE FROM WFM_CORPORATION WHERE DELETE_FLAG = ? ");
		List<Object> params = new ArrayList<>();
		params.add(CommonFlag.OFF);

		List<MgMstCode> results = select(MgMstCode.class, sql, params.toArray());
		book.existCompanyCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existCompanyCodes.add(code.codeValue);
		}
	}

	public void uploadRegist(MgExcelBookAccClnd book, WfUserRole wfUserRole) {

		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 会計カレンダー削除・更新のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0200_04"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0200_05"));

		for (MgExcelEntityAccClnd inputed : book.sheet.entityList) {

			if ((eq("A", inputed.processType))) {
				insertUpload(inputed, wfUserRole);
			} else {
				AccClndMstPK id = new AccClndMstPK();
				id.setCompanyCd(inputed.companyCd);
				id.setClndDt(inputed.clndDt);

				AccClndMst dbEntity = em.find(AccClndMst.class, id);
				if (dbEntity != null) {
					if ((eq("C", inputed.processType))) {
						List<Object> params = new ArrayList<>();

						params.add(inputed.clndDay);
						params.add(inputed.hldayTp);
						params.add(inputed.bnkHldayTp);
						params.add(inputed.stlTpPur);
						params.add(inputed.stlTpFncobl);
						params.add(inputed.stlTpFncaff);
						params.add(inputed.mlClsTm);

						params.add(DeleteFlag.OFF);

						params.add(corporationCode);
						params.add(userCode);
						params.add(ipAddr);
						params.add(now);

						params.add(inputed.companyCd);
						params.add(inputed.clndDt);

						Query query = em.createNativeQuery(updateSql.toString());
						putParams(query, params.toArray());
						query.executeUpdate();
					} else {//D
						List<Object> params = new ArrayList<>();
						params.add(inputed.companyCd);
						params.add(inputed.clndDt);

						Query query = em.createNativeQuery(deleteSql.toString());
						putParams(query, params.toArray());
						query.executeUpdate();
					}
				}
			}
		}
	}

	/**
	 * DBへ新規登録（アップロード用）
	 * @param inputed
	 */
	private void insertUpload(MgExcelEntityAccClnd inputed, WfUserRole wfUserRole) {
		final AccClndMst entity = new AccClndMst();
		final AccClndMstPK id = new AccClndMstPK();

		id.setCompanyCd(inputed.companyCd);
		id.setClndDt(inputed.clndDt);
		entity.setId(id);

		entity.setClndDay(inputed.clndDay);
		entity.setHldayTp(inputed.hldayTp);
		entity.setBnkHldayTp(inputed.bnkHldayTp);
		entity.setStlTpPur(inputed.stlTpPur);
		entity.setStlTpFncobl(inputed.stlTpFncobl);
		entity.setStlTpFncaff(inputed.stlTpFncaff);
		entity.setMlClsTm(inputed.mlClsTm);

		entity.setDltFg(DeleteFlag.OFF);

		entity.setCorporationCodeCreated(wfUserRole.getCorporationCode());
		entity.setUserCodeCreated(wfUserRole.getUserCode());
		entity.setIpCreated(wfUserRole.getIpAddress());
		entity.setTimestampCreated(MiscUtils.timestamp());

		entity.setCorporationCodeUpdated(wfUserRole.getCorporationCode());
		entity.setUserCodeUpdated(wfUserRole.getUserCode());
		entity.setIpUpdated(wfUserRole.getIpAddress());
		entity.setTimestampUpdated(MiscUtils.timestamp());

		em.persist(entity);
	}
}
