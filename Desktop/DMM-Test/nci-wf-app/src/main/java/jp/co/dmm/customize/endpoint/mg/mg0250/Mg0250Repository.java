package jp.co.dmm.customize.endpoint.mg.mg0250;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0250.excel.MgExcelBookBumonExps;
import jp.co.dmm.customize.endpoint.mg.mg0250.excel.MgExcelEntityBumonExps;
import jp.co.dmm.customize.jpa.entity.mw.BumonexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.BumonexpsMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 部門関連マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0250Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;

	/**
	 * 自身が所属する会社取得処理
	 * @param userAddedInfo ユーザ付加コード
	 * @param localeCode ロケールコード
	 * @return 自身が所属する会社リスト
	 */
	@SuppressWarnings("unchecked")
	public List<OptionItem> getCompanyItems (String userAddedInfo, String localeCode) {

		List<OptionItem> companyList = new ArrayList<OptionItem>();

		String sql = "SELECT CV.CORPORATION_CODE, CV.CORPORATION_NAME FROM WFM_CORPORATION_V CV, WFM_USER WU " +
				"WHERE CV.CORPORATION_CODE = WU.CORPORATION_CODE " +
				"AND WU.USER_ADDED_INFO = ? " +
				"AND CV.LOCALE_CODE = ?";

		Object[] params = {userAddedInfo, localeCode};

		Query query = em.createNativeQuery(sql);
		putParams(query, params);

		List<Object[]> results = query.getResultList();

		for (Object[] record : results) {
			OptionItem item = new OptionItem();
			item.setValue((String)record[0]);
			item.setLabel((String)record[1]);
			companyList.add(item);
		}

		return companyList;
	}

	/**
	 * 部門関連マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0250SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0250_01"));

		List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 部門関連マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0250SearchRequest req, Mg0250SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder(getSql("MG0250_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());

		return convertEntity(query.getResultList());
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private List<Mg0250Entity> convertEntity(List<Object[]> results) {

		List<Mg0250Entity> list = new ArrayList<Mg0250Entity>();

		for (Object[] cols : results) {
			Mg0250Entity entity = new Mg0250Entity();

			entity.companyCd = (String)cols[0];
			entity.bumonCd = (String)cols[1];
			entity.bumonNm = (String)cols[2];
			entity.orgnzCd = (String)cols[3];
			entity.orgnzNm = (String)cols[4];
			entity.dltFg = (String)cols[5];
			entity.dltFgNm = (String)cols[6];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 部門関連マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0250RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0250_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 部門関連マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0250SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and bm.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 部門関連コード
		if (isNotEmpty(req.bumonCd)) {
			sql.append(" and bm.BUMON_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bumonCd));
		}

		// 組織コード
		if (isNotEmpty(req.orgnzCd)) {
			sql.append(" and bm.ORGNZ_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.orgnzCd));
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("bm.DLT_FG", dltFgList.size()));
			params.addAll(dltFgList);
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

	public BumonexpsMst getByPk(String companyCd, String bumonCd, String orgnzCd) {
		BumonexpsMstPK id = new BumonexpsMstPK();
		id.setCompanyCd(companyCd);
		id.setBumonCd(bumonCd);
		id.setOrgnzCd(orgnzCd);

		return em.find(BumonexpsMst.class, id);
	}

	public List<MgExcelEntityBumonExps> getMasterData(Mg0250SearchRequest req) {
		StringBuilder sql = new StringBuilder("select COMPANY_CD, ");
		sql.append("BUMON_CD, ");
		sql.append("ORGNZ_CD, ");
		sql.append("DLT_FG, ");
		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from BUMONEXPS_MST bm ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBumonExps.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookBumonExps book) {
		boolean isDGHD = eq("00053", sessionHolder.getLoginInfo().getCorporationCode());

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

		// 部門コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || BUMON_CD AS CODE_VALUE FROM BUMON_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existBumonCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existBumonCds.add(code.codeValue);
		}

		// 組織コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE || '_' || ORGANIZATION_CODE AS CODE_VALUE FROM WFM_ORGANIZATION WHERE DELETE_FLAG = ? and VALID_START_DATE <= SYSDATE and VALID_END_DATE >= SYSDATE and ORGANIZATION_LEVEL = 3 ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND CORPORATION_CODE = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existOrgnzCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existOrgnzCodes.add(code.codeValue);
		}
	}

	public void uploadRegist(MgExcelBookBumonExps book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0250_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0250_04"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0250_05"));

		for (MgExcelEntityBumonExps entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.bumonCd);
				params.add(entity.orgnzCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.bumonCd);
				params.add(entity.orgnzCd);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bumonCd);
				params.add(entity.orgnzCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bumonCd);
				params.add(entity.orgnzCd);

				params.add(DeleteFlag.OFF);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);
				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				Query query = em.createNativeQuery(insertSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			}
		}
	}

}
