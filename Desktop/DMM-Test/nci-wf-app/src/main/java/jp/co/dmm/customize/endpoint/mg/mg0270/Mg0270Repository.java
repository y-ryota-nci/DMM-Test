package jp.co.dmm.customize.endpoint.mg.mg0270;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0270.excel.MgExcelBookBgtItm;
import jp.co.dmm.customize.endpoint.mg.mg0270.excel.MgExcelEntityBgtItm;
import jp.co.dmm.customize.jpa.entity.mw.BgtItmMst;
import jp.co.dmm.customize.jpa.entity.mw.BgtItmMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 予算科目マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0270Repository extends BaseRepository {
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
	 * 予算科目マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0270SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0270_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 予算科目マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0270SearchRequest req, Mg0270SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.BGT_ITM_CD," +
			"A.BGT_ITM_NM," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM," +
			"A.BS_PL_TP," +
			"LOK.LOOKUP_NAME AS BS_PL_TP_NM," +
			"A.SORT_ORDER";

		StringBuilder sql = new StringBuilder(getSql("MG0270_01").replaceFirst(REPLACE, col));
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
	private List<Mg0270Entity> convertEntity(List<Object[]> results) {

		List<Mg0270Entity> list = new ArrayList<Mg0270Entity>();

		for (Object[] cols : results) {
			Mg0270Entity entity = new Mg0270Entity();

			entity.companyCd = (String)cols[0];
			entity.bgtItmCd = (String)cols[1];
			entity.bgtItmNm = (String)cols[2];
			entity.dltFg = (String)cols[3];
			entity.dltFgNm = (String)cols[4];
			entity.bsPlTp = (String)cols[5];
			entity.bsPlTpNm = (String)cols[6];
			entity.sortOrder = (java.math.BigDecimal)cols[7];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 予算科目マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0270RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0270_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 予算科目マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0270SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 予算科目コード
		if (isNotEmpty(req.bgtItmCd)) {
			sql.append(" and A.BGT_ITM_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bgtItmCd));
		}

		// 予算科目名称
		if (isNotEmpty(req.bgtItmNm)) {
			sql.append(" and A.BGT_ITM_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bgtItmNm));
		}

		// 予算科目名称
		if (isNotEmpty(req.bsPlTp)) {
			sql.append(" and A.BS_PL_TP = ? ");
			params.add(req.bsPlTp);
		}


		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("A.DLT_FG", dltFgList.size()));
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

	public BgtItmMst getByPk(String companyCd, String bgtItmCd) {
		BgtItmMstPK id = new BgtItmMstPK();
		id.setCompanyCd(companyCd);
		id.setBgtItmCd(bgtItmCd);

		return em.find(BgtItmMst.class, id);
	}

	public List<MgExcelEntityBgtItm> getMasterData(Mg0270SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0270_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBgtItm.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookBgtItm book) {
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

	public void uploadRegist(MgExcelBookBgtItm book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0270_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0270_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0270_04"));

		for (MgExcelEntityBgtItm entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.bgtItmCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.bgtItmNm);
				params.add(entity.bsPlTp);
				params.add(entity.sortOrder);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bgtItmCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bgtItmCd);
				params.add(entity.bgtItmNm);
				params.add(entity.bsPlTp);
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
