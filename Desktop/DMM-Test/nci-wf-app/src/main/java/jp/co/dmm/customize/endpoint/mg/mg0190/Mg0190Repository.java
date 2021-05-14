package jp.co.dmm.customize.endpoint.mg.mg0190;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0190.excel.MgExcelBookBumon;
import jp.co.dmm.customize.endpoint.mg.mg0190.excel.MgExcelEntityBumon;
import jp.co.dmm.customize.jpa.entity.mw.BumonMst;
import jp.co.dmm.customize.jpa.entity.mw.BumonMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 部門マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0190Repository extends BaseRepository {
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
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItemsFromLookup(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select LOOKUP_ID, LOOKUP_NAME2, SCREEN_LOOKUP_ID from MWM_LOOKUP where CORPORATION_CODE = ? and LOOKUP_GROUP_ID = ? and LOCALE_CODE = 'ja' and DELETE_FLAG = '0' order by SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmLookup> results = select(MwmLookup.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmLookup item : results) {
			newItems.add(new OptionItem(item.getLookupId(), item.getLookupName2()));
		}

		return newItems;
	}

	/**
	 * 部門マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0190SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0190_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 部門情報一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0190SearchRequest req, Mg0190SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0190_02"));
		final List<Object> params = new ArrayList<>();

		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0190Entity> convertEntity(List<Object[]> results) {

		List<Mg0190Entity> list = new ArrayList<Mg0190Entity>();

		for (Object[] cols : results) {
			Mg0190Entity entity = new Mg0190Entity();

			entity.companyCd = (String)cols[0];
			entity.bumonCd = (String)cols[1];
			entity.bumonNm = (String)cols[2];
			entity.entrpTpCd = (String)cols[3];
			entity.entrpCd = (String)cols[4];
			entity.entrpNm = (String)cols[5];
			entity.tabCd = (String)cols[6];
			entity.tabNm = (String)cols[7];
			entity.siteCd = (String)cols[8];
			entity.siteNm = (String)cols[9];
			entity.tpCd = (String)cols[10];
			entity.tpNm = (String)cols[11];
			entity.areaCd = (String)cols[12];
			entity.areaNm = (String)cols[13];
			entity.dltFg = (String)cols[14];
			entity.dltFgNm = (String)cols[15];
			entity.taxKndCd = (String)cols[16];
			entity.taxKndNm = (String)cols[17];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 部門情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0190Request req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0190_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 部門マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0190SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 部門コード
		if (isNotEmpty(req.bumonCd)) {
			sql.append(" and BM.BUMON_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bumonCd));
		}

		// 部門名称
		if (isNotEmpty(req.bumonNm)) {
			sql.append(" and BM.BUMON_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bumonNm));
		}

		// 事業分類コード
		if (isNotEmpty(req.entrpTpCd)) {
			sql.append(" and BM.ENTRP_TP_CD = ? ");
			params.add(req.entrpTpCd);
		}

		// 事業コード
		if (isNotEmpty(req.entrpCd)) {
			sql.append(" and BM.ENTRP_CD = ? ");
			params.add(req.entrpCd);
		}

		// タブコード
		if (isNotEmpty(req.tabCd)) {
			sql.append(" and BM.TAB_CD = ? ");
			params.add(req.tabCd);
		}

		// サイトコード
		if (isNotEmpty(req.siteCd)) {
			sql.append(" and BM.SITE_CD = ? ");
			params.add(req.siteCd);
		}

		// 分類コード
		if (isNotEmpty(req.tpCd)) {
			sql.append(" and BM.TP_CD = ? ");
			params.add(req.tpCd);
		}

		// 消費税種類コード
		if (isNotEmpty(req.taxKndCd)) {
			sql.append(" and BM.TAX_KND_CD = ? ");
			params.add(req.taxKndCd);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("BM.DLT_FG", dltFgList.size()));
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

	public BumonMst getByPk(String companyCd, String bumonCd) {
		BumonMstPK id = new BumonMstPK();
		id.setCompanyCd(companyCd);
		id.setBumonCd(bumonCd);

		return em.find(BumonMst.class, id);
	}

	public List<MgExcelEntityBumon> getMasterData(Mg0190SearchRequest req) {
		StringBuilder sql = new StringBuilder("select COMPANY_CD, ");
		sql.append("BUMON_CD, ");
		sql.append("BUMON_NM, ");
		sql.append("TAX_KND_CD, ");
		sql.append("DLT_FG, ");
		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from BUMON_MST BM ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBumon.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookBumon book) {
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

	public void uploadRegist(MgExcelBookBumon book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0190_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0191_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0191_02"));

		for (MgExcelEntityBumon entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.bumonCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.bumonNm);
				params.add(entity.taxKndCd);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bumonCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bumonCd);

				params.add(entity.entrpTpCd);
				params.add(entity.entrpCd);
				params.add(entity.tabCd);
				params.add(entity.siteCd);
				params.add(entity.tpCd);
				params.add(entity.areaCd);
				params.add(entity.bumonNm);
				params.add(entity.taxKndCd);

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
