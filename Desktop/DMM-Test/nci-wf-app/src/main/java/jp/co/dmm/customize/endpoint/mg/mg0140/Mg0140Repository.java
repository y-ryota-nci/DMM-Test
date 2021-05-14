package jp.co.dmm.customize.endpoint.mg.mg0140;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0140.excel.MgExcelBookAccBrkdwn;
import jp.co.dmm.customize.endpoint.mg.mg0140.excel.MgExcelEntityAccBrkdwn;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 勘定科目補助マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0140Repository extends BaseRepository {
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
	 * 勘定科目補助マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0140SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0140_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 勘定科目補助一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0140SearchRequest req, Mg0140SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0140_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0140Entity> convertEntity(List<Object[]> results) {

		List<Mg0140Entity> list = new ArrayList<Mg0140Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0140Entity entity = new Mg0140Entity();

			entity.companyCd = (String)cols[0];
			entity.accCd = (String)cols[1];
			entity.accNm = (String)cols[2];
			entity.accBrkdwnCd = (String)cols[3];
			entity.sqno = cols[4] != null ? ((java.math.BigDecimal)cols[4]).longValue() : null;
			entity.accBrkdwnNm = (String)cols[5];
			entity.accBrkdwnNmS = (String)cols[6];
			entity.vdDtS = (java.util.Date)cols[7];
			entity.vdDtE = (java.util.Date)cols[8];
			entity.dltFg = (String)cols[9];
			entity.dltFgNm = (String)cols[10];

			entity.vdDtSStr = cols[7] != null ? sd.format(((java.util.Date)cols[7])) : null;
			entity.vdDtEStr = cols[8] != null ? sd.format(((java.util.Date)cols[8])) : null;

			list.add(entity);
		}
		return list;
	}

	/**
	 * 勘定科目補助削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0140Request req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0140_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 勘定科目補助マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0140SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and ABM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 勘定科目コード
		if (isNotEmpty(req.accCd)) {
			sql.append(" and ABM.ACC_CD = ? ");
			params.add(req.accCd);
		}

		// 勘定科目補助コード
		if (isNotEmpty(req.accBrkdwnCd)) {
			sql.append(" and ABM.ACC_BRKDWN_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.accBrkdwnCd));
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and ABM.SQNO = ? ");
			params.add(req.sqno);
		}

		// 勘定科目補助名称
		if (isNotEmpty(req.accBrkdwnNm)) {
			sql.append(" and ABM.ACC_BRKDWN_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.accBrkdwnNm));
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and ABM.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and ABM.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("ABM.DLT_FG", dltFgList.size()));
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

	public List<MgExcelEntityAccBrkdwn> getMasterData(Mg0140SearchRequest req) {
		StringBuilder sql = new StringBuilder("select ABM.*, ROWNUM as ID, NULL as PROCESS_TYPE from ACC_BRKDWN_MST ABM where 1=1 ");
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityAccBrkdwn.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookAccBrkdwn book) {
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
	}

	public void uploadRegist(MgExcelBookAccBrkdwn book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 勘定科目補助マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0140_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0141_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0140_04"));

		for (MgExcelEntityAccBrkdwn entity : book.sheet.entityList) {

			if ("A".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.accBrkdwnCd);
				params.add(entity.sqno);
				params.add(entity.accBrkdwnNm);
				params.add(entity.accBrkdwnNmS);
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

				params.add(entity.accBrkdwnNm);
				params.add(entity.accBrkdwnNmS);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.accBrkdwnCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if ("D".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.accCd);
				params.add(entity.accBrkdwnCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			}
		}
	}
}
