package jp.co.dmm.customize.endpoint.mg.mg0130;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0130.excel.MgExcelBookAcc;
import jp.co.dmm.customize.endpoint.mg.mg0130.excel.MgExcelEntityAcc;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 勘定科目マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0130Repository extends BaseRepository {
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
	 * 勘定科目マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0130SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0130_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 勘定科目一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0130SearchRequest req, Mg0130SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0130_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0130Entity> convertEntity(List<Object[]> results) {

		List<Mg0130Entity> list = new ArrayList<Mg0130Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0130Entity entity = new Mg0130Entity();

			entity.companyCd = (String)cols[0];
			entity.accCd = (String)cols[1];
			entity.sqno = cols[2] != null ? ((java.math.BigDecimal)cols[2]).longValue() : null;
			entity.accNm = (String)cols[3];
			entity.accNmS = (String)cols[4];
			entity.dcTp = (String)cols[5];
			entity.dcTpNm = (String)cols[6];
			entity.accBrkdwnTp = (String)cols[7];
			entity.accBrkdwnTpNm = (String)cols[8];
			entity.taxCdSs = (String)cols[9];
			entity.taxIptTp = (String)cols[10];
			entity.taxIptTpNm = (String)cols[11];
			entity.vdDtS = (java.util.Date)cols[12];
			entity.vdDtE = (java.util.Date)cols[13];
			entity.dltFg = (String)cols[14];
			entity.dltFgNm = (String)cols[15];

			entity.vdDtSStr = cols[12] != null ? sd.format(((java.util.Date)cols[12])) : null;
			entity.vdDtEStr = cols[13] != null ? sd.format(((java.util.Date)cols[13])) : null;

			list.add(entity);
		}
		return list;
	}

	/**
	 * 勘定科目削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0130Request req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0130_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 勘定科目マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0130SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and AM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 勘定科目コード
		if (isNotEmpty(req.accCd)) {
			sql.append(" and AM.ACC_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.accCd));
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and AM.SQNO = ? ");
			params.add(req.sqno);
		}

		// 勘定科目名称
		if (isNotEmpty(req.accNm)) {
			sql.append(" and AM.ACC_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.accNm));
		}

		// 貸借
		if (isNotEmpty(req.dcTp)) {
			sql.append(" and AM.DC_TP = ? ");
			params.add(req.dcTp);
		}

		// 税入力
		if (isNotEmpty(req.taxIptTp)) {
			sql.append(" and AM.TAX_IPT_TP = ? ");
			params.add(req.taxIptTp);
		}

		// 税処理コード(SuperStream)
		if (isNotEmpty(req.taxCdSs)) {
			sql.append(" and AM.TAX_CD_SS like ? escape '~'");
			params.add(escapeLikeBoth(req.taxCdSs));
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and AM.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and AM.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("AM.DLT_FG", dltFgList.size()));
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

	public List<MgExcelEntityAcc> getMasterData(Mg0130SearchRequest req) {
		StringBuilder sql = new StringBuilder("select AM.*, ROWNUM as ID, NULL as PROCESS_TYPE from ACC_MST AM where 1=1 ");
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityAcc.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookAcc book) {
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
	}

	public void uploadRegist(MgExcelBookAcc book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 品目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0130_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0131_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0130_04"));

		for (MgExcelEntityAcc entity : book.sheet.entityList) {

			if ("A".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.sqno);
				params.add(entity.accNm);
				params.add(entity.accNmS);
				params.add(entity.dcTp);
				params.add(entity.accBrkDwnTp);
				params.add(entity.taxCdSs);
				params.add(entity.taxIptTp);
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

				params.add(entity.accNm);
				params.add(entity.accNmS);
				params.add(entity.dcTp);
				params.add(entity.accBrkDwnTp);
				params.add(entity.taxCdSs);
				params.add(entity.taxIptTp);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if ("D".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			}
		}
	}
}
