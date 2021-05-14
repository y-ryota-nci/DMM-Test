package jp.co.dmm.customize.endpoint.mg.mg0320;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0320.excel.MgExcelBookTaxexps;
import jp.co.dmm.customize.endpoint.mg.mg0320.excel.MgExcelEntityTaxexps;
import jp.co.dmm.customize.jpa.entity.mw.TaxexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxexpsMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 消費税関連マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0320Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	@Inject private SessionHolder sessionHolder;

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
	 * 消費税関連マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0320SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0320_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 消費税関連マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0320SearchRequest req, Mg0320SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.TAX_KND_CD," +
			"A.TAX_SPC," +
			"A.TAX_CD," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM," +
			"tm.TAX_NM AS TAX_NM," +
			"lp1.LOOKUP_NAME2 AS TAX_KND_NM," +
			"lp2.LOOKUP_NAME2 AS TAX_SPC_NM"
			;

		StringBuilder sql = new StringBuilder(getSql("MG0320_01").replaceFirst(REPLACE, col));
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
	private List<Mg0320Entity> convertEntity(List<Object[]> results) {

		List<Mg0320Entity> list = new ArrayList<Mg0320Entity>();

		for (Object[] cols : results) {
			Mg0320Entity entity = new Mg0320Entity();

			entity.companyCd = (String)cols[0];
			entity.taxKndCd = (String)cols[1];
			entity.taxSpc = (String)cols[2];
			entity.taxCd = (String)cols[3];
			entity.dltFg = (String)cols[4];
			entity.dltFgNm = (String)cols[5];
			entity.taxNm = (String)cols[6];
			entity.taxKndNm = (String)cols[7];
			entity.taxSpcNm = (String)cols[8];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 消費税関連マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0320RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0320_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 消費税関連マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0320SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 消費税種類コード
		if (isNotEmpty(req.taxKndCd)) {
			sql.append(" and A.TAX_KND_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.taxKndCd));
		}

		// 消費税種別
		if (isNotEmpty(req.taxSpc)) {
			sql.append(" and A.TAX_SPC like ? escape '~'");
			params.add(escapeLikeBoth(req.taxSpc));
		}

		// 消費税コード
		if (isNotEmpty(req.taxCd)) {
			sql.append(" and A.TAX_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.taxCd));
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

	public TaxexpsMst getByPk(String companyCd, String taxKndCd, String taxSpc) {
		TaxexpsMstPK id = new TaxexpsMstPK();
		id.setCompanyCd(companyCd);
		id.setTaxKndCd(taxKndCd);
		id.setTaxSpc(taxSpc);

		return em.find(TaxexpsMst.class, id);
	}

	public List<MgExcelEntityTaxexps> getMasterData(Mg0320SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0320_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityTaxexps.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookTaxexps book) {
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

		// 消費税コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || TAX_CD AS CODE_VALUE FROM TAX_MST WHERE DLT_FG = ?  ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existTaxCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existTaxCodes.add(code.codeValue);
		}

	}

	public void uploadRegist(MgExcelBookTaxexps book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 消費税関連マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0320_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0320_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0320_04"));

		for (MgExcelEntityTaxexps entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.taxKndCd);
				params.add(entity.taxSpc);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.taxCd);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.taxKndCd);
				params.add(entity.taxSpc);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.taxKndCd);
				params.add(entity.taxSpc);
				params.add(entity.taxCd);

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
