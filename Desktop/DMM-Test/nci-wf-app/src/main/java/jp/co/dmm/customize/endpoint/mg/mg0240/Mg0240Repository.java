package jp.co.dmm.customize.endpoint.mg.mg0240;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0240.excel.MgExcelBookPaySite;
import jp.co.dmm.customize.endpoint.mg.mg0240.excel.MgExcelEntityPaySite;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMst;
import jp.co.dmm.customize.jpa.entity.mw.PaySiteMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 支払サイトマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0240Repository extends BaseRepository {
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
	 * 支払サイトマスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0240SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0240_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 支払サイトマスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0240SearchRequest req, Mg0240SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.PAY_SITE_CD," +
			"A.PAY_SITE_NM," +
			"A.PAY_SITE_M," +
			"A.PAY_SITE_N," +
			"A.SORT_ORDER," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0240_01").replaceFirst(REPLACE, col));
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
	private List<Mg0240Entity> convertEntity(List<Object[]> results) {

		List<Mg0240Entity> list = new ArrayList<Mg0240Entity>();

		for (Object[] cols : results) {
			Mg0240Entity entity = new Mg0240Entity();

			entity.companyCd = (String) cols[0];
			entity.paySiteCd = (String) cols[1];
			entity.paySiteNm = (String) cols[2];
			entity.paySiteM = (String) cols[3];
			entity.paySiteN = (String) cols[4];
			entity.sortOrder = (BigDecimal) cols[5];
			entity.dltFg = (String) cols[6];
			entity.dltFgNm = (String) cols[7];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 支払サイトマスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0240RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0240_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 支払サイトマスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0240SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 支払サイトコード
		if (isNotEmpty(req.paySiteCd)) {
			sql.append(" and PAY_SITE_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.paySiteCd));
		}

		// 支払サイト名称
		if (isNotEmpty(req.paySiteNm)) {
			sql.append(" and A.PAY_SITE_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.paySiteNm));
		}

		// 支払サイト（日数）
		if (isNotEmpty(req.paySiteM)) {
			sql.append(" and A.PAY_SITE_M = ? ");
			params.add(req.paySiteM);
		}

		// 支払サイト（月数）
		if (isNotEmpty(req.paySiteN)) {
			sql.append(" and A.PAY_SITE_N = ? ");
			params.add(req.paySiteN);
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

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	public PaySiteMst getByPk(String companyCd, String paySiteCd) {
		PaySiteMstPK id = new PaySiteMstPK();
		id.setCompanyCd(companyCd);
		id.setPaySiteCd(paySiteCd);

		return em.find(PaySiteMst.class, id);
	}

	public List<MgExcelEntityPaySite> getMasterData(Mg0240SearchRequest req) {
		StringBuilder sql = new StringBuilder("select COMPANY_CD, ");
		sql.append("PAY_SITE_CD, ");
		sql.append("PAY_SITE_NM, ");
		sql.append("PAY_SITE_M, ");
		sql.append("PAY_SITE_N, ");
		sql.append("SORT_ORDER, ");
		sql.append("DLT_FG, ");
		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from PAY_SITE_MST A ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityPaySite.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookPaySite book) {
		//   会社コード
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

	public void uploadRegist(MgExcelBookPaySite book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0240_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0240_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0240_04"));

		for (MgExcelEntityPaySite entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.paySiteCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.paySiteNm);
				params.add(entity.paySiteM);
				params.add(entity.paySiteN);
				params.add(entity.sortOrder);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.paySiteCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.paySiteCd);
				params.add(entity.paySiteNm);
				params.add(entity.paySiteM);
				params.add(entity.paySiteN);
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
