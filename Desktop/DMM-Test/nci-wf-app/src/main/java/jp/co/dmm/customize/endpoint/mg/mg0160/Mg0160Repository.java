package jp.co.dmm.customize.endpoint.mg.mg0160;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0160.excel.MgExcelBookTax;
import jp.co.dmm.customize.endpoint.mg.mg0160.excel.MgExcelEntityTax;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 消費税マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0160Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

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
	 * 消費税マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0160SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0160_01").replaceFirst(REPLACE, "count(1)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 消費税マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0160SearchRequest req, Mg0160SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		String col =
			"A.COMPANY_CD," +
			"A.TAX_CD," +
			"A.SQNO," +
			"A.TAX_NM," +
			"A.TAX_NM_S," +
			"A.TAX_RTO," +
			"A.TAX_TP," +
			"opi2.LABEL AS TAX_TP_NM," +
			"A.TAX_CD_SS," +
			"A.FRC_UNT," +
			"A.FRC_TP," +
			"opi4.LABEL AS FRC_NM," +
			"A.ACC_CD," +
			"am.ACC_NM," +
			"A.ACC_BRKDWN_CD," +
			"abm.ACC_BRKDWN_NM," +
			"A.DC_TP," +
			"opi3.LABEL AS DC_TP_NM," +
			"A.VD_DT_S," +
			"A.VD_DT_E," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0160_01").replaceFirst(REPLACE, col));
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
	private List<Mg0160Entity> convertEntity(List<Object[]> results) {

		List<Mg0160Entity> list = new ArrayList<Mg0160Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0160Entity entity = new Mg0160Entity();

			entity.companyCd = (String)cols[0];
			entity.taxCd = (String)cols[1];
			entity.sqno = cols[2] != null ? ((java.math.BigDecimal)cols[2]).longValue() : null;
			entity.taxNm = (String)cols[3];
			entity.taxNmS = (String)cols[4];
			entity.taxRto = (java.math.BigDecimal)cols[5];
			entity.taxTp = (String)cols[6];
			entity.taxTpNm = (String)cols[7];
			entity.taxCdSs = (String)cols[8];
			entity.frcUnt = (String)cols[9];
			entity.frcTp = (String)cols[10];
			entity.frcNm = (String)cols[11];
			entity.accCd = (String)cols[12];
			entity.accNm = (String)cols[13];
			entity.accBrkdwnCd = (String)cols[14];
			entity.accBrkdwnNm = (String)cols[15];
			entity.dcTp = (String)cols[16];
			entity.dcTpNm = (String)cols[17];
			entity.vdDtS = (java.util.Date)cols[18];
			entity.vdDtE = (java.util.Date)cols[19];
			entity.dltFg = (String)cols[20];
			entity.dltFgNm = (String)cols[21];

			entity.vdDtSStr = cols[18] != null ? sd.format(((java.util.Date)cols[18])) : null;
			entity.vdDtEStr = cols[19] != null ? sd.format(((java.util.Date)cols[19])) : null;

			list.add(entity);
		}
		return list;
	}

	/**
	 * 消費税マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0160RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0160_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 消費税マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0160SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 消費税コード
		if (isNotEmpty(req.taxCd)) {
			sql.append(" and A.TAX_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.taxCd));
		}

		// 消費税名称
		if (isNotEmpty(req.taxNm)) {
			sql.append(" and A.TAX_NM like ? escape '~' ");
			params.add(escapeLikeBoth(req.taxNm));
		}

		// 消費税率(開始)
		if (req.taxRtoFrom != null) {
			sql.append(" and A.TAX_RTO >= ? ");
			params.add(req.taxRtoFrom);
		}

		// 消費税率（終了）
		if (req.taxRtoTo != null) {
			sql.append(" and A.TAX_RTO <= ? ");
			params.add(req.taxRtoTo);
		}

		// 消費税区分
		List<String> taxTpList = new ArrayList<>();
		if (req.taxTp0) taxTpList.add("0");
		if (req.taxTp1) taxTpList.add("1");
		if (req.taxTp2) taxTpList.add("2");

		if (taxTpList.size() != 0) {
			sql.append(" and " + toInListSql("A.TAX_TP", taxTpList.size()));
			params.addAll(taxTpList);
		}

		// 有効期間（開始）
		if (req.vdDtS != null) {
			sql.append(" and A.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (req.vdDtE != null) {
			sql.append(" and A.VD_DT_S <= ? ");
			params.add(req.vdDtE);
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

	public List<MgExcelEntityTax> getMasterData(Mg0160SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0160_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityTax.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookTax book) {
		boolean isDGHD = eq("00053", sessionHolder.getLoginInfo().getCorporationCode());

		// 会社コード
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct CORPORATION_CODE as CODE_VALUE from WFM_CORPORATION where DELETE_FLAG = ? ");
		List<Object> params = new ArrayList<>();
		params.add(CommonFlag.OFF);

		List<MgMstCode> results = select(MgMstCode.class, sql, params.toArray());
		book.existCompanyCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existCompanyCodes.add(code.codeValue);
		}

		// 勘定科目一覧
		sql = new StringBuilder();
		sql.append("select distinct COMPANY_CD || '_' || ACC_CD as CODE_VALUE from ACC_MST where DLT_FG = ? and SYSDATE between VD_DT_S and VD_DT_E ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" and COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existAccCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existAccCodes.add(code.codeValue);
		}

		// 勘定科目補助一覧
		sql = new StringBuilder();
		sql.append("select distinct COMPANY_CD || '_' || ACC_CD || '_' || ACC_BRKDWN_CD as CODE_VALUE from ACC_BRKDWN_MST where DLT_FG = ? and SYSDATE between VD_DT_S and VD_DT_E ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" and COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existAccBrkdwnCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existAccBrkdwnCodes.add(code.codeValue);
		}

	}

	public void uploadRegist(MgExcelBookTax book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 品目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0160_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0160_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0160_06"));

		for (MgExcelEntityTax entity : book.sheet.entityList) {

			if ("A".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.taxCd);
				params.add(entity.sqno);
				params.add(entity.taxNm);
				params.add(entity.taxNmS);
				params.add(entity.taxRto);
				params.add(entity.taxTp);
				params.add(entity.taxCdSs);
				params.add(entity.frcUnt);
				params.add(entity.frcTp);
				params.add(entity.accCd);
				params.add(entity.accBrkdwnCd);
				params.add(entity.dcTp);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);

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
			} else if ("C".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.taxNm);
				params.add(entity.taxNmS);
				params.add(entity.taxRto);
				params.add(entity.taxTp);
				params.add(entity.taxCdSs);
				params.add(entity.frcUnt);
				params.add(entity.frcTp);
				params.add(entity.accCd);
				params.add(entity.accBrkdwnCd);
				params.add(entity.dcTp);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.taxCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if ("D".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.taxCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			}
		}
	}
}
