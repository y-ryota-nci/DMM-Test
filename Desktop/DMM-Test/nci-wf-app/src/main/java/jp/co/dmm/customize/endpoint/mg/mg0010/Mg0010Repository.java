package jp.co.dmm.customize.endpoint.mg.mg0010;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.dmm.customize.component.DmmCodeBook.ProcFldTp;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCodePeriod;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadRepository;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgExcelBookItm;
import jp.co.dmm.customize.endpoint.mg.mg0010.excel.MgExcelEntityItm;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 品目マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0010Repository extends MgUploadRepository<MgExcelBookItm, MgExcelEntityItm, Mg0010SearchRequest> {
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
	 * 品目マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0010_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 品目マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0010SearchRequest req, Mg0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0010_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0010Entity> convertEntity(List<Object[]> results) {

		List<Mg0010Entity> list = new ArrayList<Mg0010Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0010Entity entity = new Mg0010Entity();

			entity.companyCd = (String) cols[0];
			entity.orgnzCd = (String) cols[1];
			entity.orgnzNm = (String) cols[2];
			entity.itmCd = (String) cols[3];
			entity.sqno = (BigDecimal) cols[4];
			entity.itmNm = (String) cols[5];
			entity.ctgryCd = (String) cols[6];

			entity.stckTp = (String) cols[7];


			entity.untCd = (String) cols[8];
			entity.amt = cols[9] != null ? (BigDecimal) cols[9] : null;
			entity.makerNm = (String) cols[10];
			entity.makerMdlNo = (String) cols[11];
			entity.itmRmk = (String) cols[12];

			entity.prcFldTp = (String) cols[13];

			entity.vdDtS = cols[14] != null ? (Date) cols[14] : null;
			entity.vdDtE = cols[15] != null ? (Date) cols[15] : null;
			entity.vdDtSStr = entity.vdDtS != null ? sd.format(entity.vdDtS) : null;
			entity.vdDtEStr = entity.vdDtE != null ? sd.format(entity.vdDtE) : null;

			entity.dltFg = (String)cols[16];
			entity.dltFgNm = (String)cols[17];

			entity.stckTpNm = (String) cols[18];
			entity.prcFldTpNm = (String) cols[19];



			list.add(entity);
		}
		return list;
	}

	/**
	 * 品目情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0010Request req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0010_04"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 品目マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0010SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and IM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 組織コード
		if (isNotEmpty(req.orgnzCd)) {
			sql.append(" and IM.ORGNZ_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.orgnzCd));
		}

		// 品目コード
		if (isNotEmpty(req.itmCd)) {
			sql.append(" and IM.ITM_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.itmCd));
		}

		// 品目名称
		if (isNotEmpty(req.itmNm)) {
			sql.append(" and IM.ITM_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.itmNm.trim()));
		}

		// 調達部門区分
		List<String> prcFldTpList = new ArrayList<>();

		if (req.prcFldTpHr) prcFldTpList.add(ProcFldTp.HR);
		if (req.prcFldTpGa) prcFldTpList.add(ProcFldTp.GA);
		if (req.prcFldTpIs) prcFldTpList.add(ProcFldTp.IS);
		if (req.prcFldTpOt) prcFldTpList.add(ProcFldTp.OT);

		if (prcFldTpList.size() != 0) {
			sql.append(" and " + toInListSql("IM.PRC_FLD_TP", prcFldTpList.size()));
			params.addAll(prcFldTpList);
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and IM.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and IM.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}


		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("IM.DLT_FG", dltFgList.size()));
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
	public List<MgExcelEntityItm> getMasterData(Mg0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0010_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityItm.class, sql, params.toArray());
	}

	@Override
	public void getUploadMasterCdInfo (MgExcelBookItm book) {
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

		// 品目一覧
		book.existItmCodes = new HashSet<String>();
		// 品目一覧（有効期限付き）
		book.existEntityPeriods = new HashMap<String, Set<MgMstCodePeriod>>();

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

		// 取引先コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || SPLR_CD AS CODE_VALUE FROM SPLR_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existSplrCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existSplrCodes.add(code.codeValue);
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

	@Override
	public void uploadRegist(MgExcelBookItm book, WfUserRole userRole) {
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 品目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0010_04"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0010_06"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0010_07"));

		for (MgExcelEntityItm entity : book.sheet.entityList) {

			if ("A".equals(entity.processType)) {
				List<Object> insertParams = new ArrayList<>();

				insertParams.add(entity.companyCd);
				insertParams.add(entity.orgnzCd);
				insertParams.add(entity.itmCd);
				insertParams.add(entity.sqno);
				insertParams.add(entity.itmNm);
				insertParams.add(entity.ctgryCd);
				insertParams.add(entity.stckTp);
				insertParams.add(entity.splrCd);
				insertParams.add(entity.splrNmKj);
				insertParams.add(entity.splrNmKn);
				insertParams.add(entity.untCd);
				insertParams.add(entity.amt);
				insertParams.add(entity.taxCd);
				insertParams.add(entity.makerNm);
				insertParams.add(entity.makerMdlNo);
				insertParams.add(entity.itmRmk);
				insertParams.add(entity.itmVrsn);
				insertParams.add(entity.prcFldTp);
				insertParams.add(entity.vdDtS);
				insertParams.add(entity.vdDtE);

				insertParams.add(corporationCode);
				insertParams.add(userCode);
				insertParams.add(ipAddr);
				insertParams.add(now);
				insertParams.add(corporationCode);
				insertParams.add(userCode);
				insertParams.add(ipAddr);
				insertParams.add(now);

				Query query = em.createNativeQuery(insertSql.toString());
				putParams(query, insertParams.toArray());
				query.executeUpdate();
			} else if ("C".equals(entity.processType)) {
				List<Object> updateParams = new ArrayList<>();

				updateParams.add(entity.itmNm);
				updateParams.add(entity.ctgryCd);
				updateParams.add(entity.stckTp);
				updateParams.add(entity.splrCd);
				updateParams.add(entity.splrNmKj);
				updateParams.add(entity.splrNmKn);
				updateParams.add(entity.untCd);
				updateParams.add(entity.amt);
				updateParams.add(entity.taxCd);
				updateParams.add(entity.makerNm);
				updateParams.add(entity.makerMdlNo);
				updateParams.add(entity.itmRmk);
				updateParams.add(entity.itmVrsn);
				updateParams.add(entity.prcFldTp);
				updateParams.add(entity.vdDtS);
				updateParams.add(entity.vdDtE);
				updateParams.add(entity.dltFg);

				updateParams.add(corporationCode);
				updateParams.add(userCode);
				updateParams.add(ipAddr);
				updateParams.add(now);

				updateParams.add(entity.companyCd);
				updateParams.add(entity.orgnzCd);
				updateParams.add(entity.itmCd);
				updateParams.add(entity.sqno);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, updateParams.toArray());
				query.executeUpdate();
			} else if ("D".equals(entity.processType)) {
				List<Object> deleteParams = new ArrayList<>();
				deleteParams.add(entity.companyCd);
				deleteParams.add(entity.orgnzCd);
				deleteParams.add(entity.itmCd);
				deleteParams.add(entity.sqno);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, deleteParams.toArray());
				query.executeUpdate();
			}
		}
	}
}
