package jp.co.dmm.customize.endpoint.mg.mg0090;

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
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadRepository;
import jp.co.dmm.customize.endpoint.mg.mg0090.excel.MgExcelBookBnkacc;
import jp.co.dmm.customize.endpoint.mg.mg0090.excel.MgExcelEntityBnkacc;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 銀行口座マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0090Repository extends MgUploadRepository<MgExcelBookBnkacc, MgExcelEntityBnkacc, Mg0090SearchRequest> {
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
	 * 銀行口座マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0090SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0090_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 銀行口座一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0090SearchRequest req, Mg0090SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0090_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0090Entity> convertEntity(List<Object[]> results) {

		List<Mg0090Entity> list = new ArrayList<Mg0090Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0090Entity entity = new Mg0090Entity();

			entity.companyCd = (String)cols[0];
			entity.bnkaccCd = (String)cols[1];
			entity.sqno = cols[2] != null ? ((java.math.BigDecimal)cols[2]).longValue() : null;
			entity.bnkCd = (String)cols[3];
			entity.bnkNm = (String)cols[4];
			entity.bnkbrcCd = (String)cols[5];
			entity.bnkbrcNm = (String)cols[6];
			entity.bnkaccTp = (String)cols[7];
			entity.bnkaccTpNm = (String)cols[8];
			entity.bnkaccNo = (String)cols[9];
			entity.bnkaccNm = (String)cols[10];
			entity.bnkaccNmKn = (String)cols[11];
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
	 * 銀行口座削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0090Request req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0090_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 銀行口座マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0090SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BAM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 銀行口座コード
		if (isNotEmpty(req.bnkaccCd)) {
			sql.append(" and BAM.BNKACC_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccCd));
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and BAM.SQNO = ? ");
			params.add(req.sqno);
		}

		// 銀行コード
		if (isNotEmpty(req.bnkCd)) {
			sql.append(" and BAM.BNK_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkCd));
		}

		// 銀行支店コード
		if (isNotEmpty(req.bnkbrcCd)) {
			sql.append(" and BAM.BNKBRC_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkbrcCd));
		}

		// 銀行口座種別
		if (isNotEmpty(req.bnkaccTp)) {
			sql.append(" and BAM.BNKACC_TP	 like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccTp));
		}

		// 銀行口座番号
		if (isNotEmpty(req.bnkaccNo)) {
			sql.append(" and BAM.BNKACC_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccNo));
		}

		// 銀行口座名称
		if (isNotEmpty(req.bnkaccNm)) {
			sql.append(" and BAM.BNKACC_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccNm));
		}

		// 銀行口座名称（カタカナ）
		if (isNotEmpty(req.bnkaccNmKn)) {
			sql.append(" and BAM.BNKACC_NM_KN like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccNmKn));
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and BAM.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and BAM.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("BAM.DLT_FG", dltFgList.size()));
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

	@Override
	public List<MgExcelEntityBnkacc> getMasterData(Mg0090SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0090_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBnkacc.class, sql, params.toArray());
	}

	@Override
	public void getUploadMasterCdInfo(MgExcelBookBnkacc book) {
		boolean isDGHD = eq("00053", sessionHolder.getLoginInfo().getCorporationCode());

		//  会社コード
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE AS CODE_VALUE FROM WFM_CORPORATION WHERE DELETE_FLAG = ? ");
		List<Object> params = new ArrayList<>();
		params.add(CommonFlag.OFF);

		List<MgMstCode> results = select(MgMstCode.class, sql, params.toArray());
		book.existCompanyCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existCompanyCodes.add(code.codeValue);
		}

		// 銀行一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || BNK_CD AS CODE_VALUE FROM BNK_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existBnkCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existBnkCodes.add(code.codeValue);
		}

		// 銀行支店一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || BNK_CD || '_' || BNKBRC_CD AS CODE_VALUE FROM BNKBRC_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existBnkbrcCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existBnkbrcCodes.add(code.codeValue);
		}

	}

	@Override
	public void uploadRegist(MgExcelBookBnkacc book, WfUserRole userRole) {
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 取引先
		StringBuilder deleteSql = new StringBuilder(getSql("MG0090_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0091_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0091_04"));

		for (MgExcelEntityBnkacc entity : book.sheet.entityList) {

			if ("A".equals(entity.processType)) {

				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bnkaccCd);
				params.add(entity.sqno);
				params.add(entity.bnkCd);
				params.add(entity.bnkbrcCd);
				params.add(entity.bnkaccTp);
				params.add(entity.bnkaccNo);
				params.add(entity.bnkaccNm);
				params.add(entity.bnkaccNmKn);
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

				params.add(entity.bnkCd);
				params.add(entity.bnkbrcCd);
				params.add(entity.bnkaccTp);
				params.add(entity.bnkaccNo);
				params.add(entity.bnkaccNm);
				params.add(entity.bnkaccNmKn);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bnkaccCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if ("D".equals(entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bnkaccCd);
				params.add(entity.sqno);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			}
		}

	}
}
