package jp.co.dmm.customize.endpoint.mg.mg0170;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0170.excel.MgExcelBookMny;
import jp.co.dmm.customize.endpoint.mg.mg0170.excel.MgExcelEntityMny;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.MnyMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 通貨マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0170Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

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
	 * 通貨マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0170SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0170_01").replaceFirst(REPLACE, "count(1)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 通貨マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0170SearchRequest req, Mg0170SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.MNY_CD," +
			"A.MNY_NM," +
			"A.MNY_MRK," +
			"A.RDXPNT_GDT," +
			"A.SORT_ORDER," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0170_01").replaceFirst(REPLACE, col));
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
	private List<Mg0170Entity> convertEntity(List<Object[]> results) {

		List<Mg0170Entity> list = new ArrayList<Mg0170Entity>();

		for (Object[] cols : results) {
			Mg0170Entity entity = new Mg0170Entity();

			entity.companyCd = (String)cols[0];
			entity.mnyCd = (String)cols[1];
			entity.mnyNm = (String)cols[2];
			entity.mnyMrk = (String)cols[3];
			entity.rdxpntGdt = (java.math.BigDecimal)cols[4];
			entity.sortOrder = (java.math.BigDecimal)cols[5];
			entity.dltFg = (String)cols[6];
			entity.dltFgNm = (String)cols[7];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 通貨マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0170RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0170_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 通貨マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0170SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 通貨コード
		if (isNotEmpty(req.mnyCd)) {
			sql.append(" and A.MNY_CD like ? escape '~' ");
			params.add(escapeLikeBoth(req.mnyCd));
		}

		// 通貨名称
		if (isNotEmpty(req.mnyNm)) {
			sql.append(" and A.MNY_NM like ? escape '~' ");
			params.add(escapeLikeBoth(req.mnyNm));
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("DLT_FG", dltFgList.size()));
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

	public MnyMst getByPk(String companyCd, String mnyCd) {
		MnyMstPK id = new MnyMstPK();
		id.setCompanyCd(companyCd);
		id.setMnyCd(mnyCd);

		return em.find(MnyMst.class, id);
	}

	public List<MgExcelEntityMny> getMasterData(Mg0170SearchRequest req) {
		StringBuilder sql = new StringBuilder("select COMPANY_CD, ");
		sql.append("MNY_CD, ");
		sql.append("MNY_NM, ");
		sql.append("MNY_MRK, ");
		sql.append("RDXPNT_GDT, ");
		sql.append("SORT_ORDER, ");
		sql.append("DLT_FG, ");
		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from MNY_MST A ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityMny.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookMny book) {
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

	public void uploadRegist(MgExcelBookMny book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0170_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0170_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0170_04"));

		for (MgExcelEntityMny entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.mnyCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.mnyNm);
				params.add(entity.mnyMrk);
				params.add(entity.rdxpntGdt);
				params.add(entity.sortOrder);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.mnyCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.mnyCd);
				params.add(entity.mnyNm);
				params.add(entity.mnyMrk);
				params.add(entity.rdxpntGdt);
				params.add(entity.sortOrder);

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
