package jp.co.dmm.customize.endpoint.mg.mg0030;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0030.excel.MgExcelBookItmexpsChrmst;
import jp.co.dmm.customize.endpoint.mg.mg0030.excel.MgExcelEntityItmexpsChrmst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps2Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps2ChrmstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 費目関連マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0030Repository extends BaseRepository {
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
	 * 費目関連マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0030SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0030_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 費目関連マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0030SearchRequest req, Mg0030SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0030_02"));
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0030Entity> convertEntity(List<Object[]> results) {

		List<Mg0030Entity> list = new ArrayList<Mg0030Entity>();

		for (Object[] cols : results) {
			Mg0030Entity entity = new Mg0030Entity();

			entity.companyCd = (String) cols[0];
			entity.orgnzCd = (String) cols[1];
			entity.orgnzNm = (String) cols[2];

			entity.itmExpsCd1 = (String) cols[3];
			entity.itmExpsNm1 = (String) cols[4];
			entity.itmExpsCd2 = (String) cols[5];
			entity.itmExpsNm2 = (String) cols[6];
			entity.jrnCd = (String) cols[7];
			entity.jrnNm = (String) cols[8];
			entity.accCd = (String) cols[9];
			entity.accNm = (String) cols[10];
			entity.accBrkDwnCd = (String) cols[11];
			entity.accBrkDwnNm = (String) cols[12];
			entity.mngAccCd = (String) cols[13];
			entity.mngAccBrkDwnCd = (String) cols[14];
			entity.bdgtAccCd = (String) cols[15];
			entity.asstTp = (String) cols[16];
			entity.taxCd = (String) cols[17];
			entity.taxNm = (String) cols[18];

			entity.slpGrpGl = (String) cols[19];
			entity.cstTp = (String) cols[20];

			entity.dltFg = (String)cols[21];
			entity.dltFgNm = (String)cols[22];

			entity.taxSbjTp = (String)cols[23];
			entity.taxSbjNm = (String)cols[24];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 費目関連情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0030RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {

				StringBuilder sql1 = new StringBuilder(getSql("MG0030_03"));
				String[] params = ArrayUtils.insert(0, target.split("\\|"), DeleteFlag.ON);
				delCnt += execSql(sql1.toString(), params);

			}
		}

		return delCnt;
	}

	/**
	 * 費目関連マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0030SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and I2.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 組織コード
		if (isNotEmpty(req.orgnzCd)) {
			sql.append(" and I2.ORGNZ_CD = ? ");
			params.add(req.orgnzCd);
		}

		// 費目コード1
		if (isNotEmpty(req.itmExpsCd1)) {
			sql.append(" and I2.ITMEXPS_CD1 = ? ");
			params.add(req.itmExpsCd1);
		}

		// 費目コード2
		if (isNotEmpty(req.itmExpsCd2)) {
			sql.append(" and I2.ITMEXPS_CD2 = ? ");
			params.add(req.itmExpsCd2);
		}

		// 仕訳コード
		if (isNotEmpty(req.jrnCd)) {
			sql.append(" and I1.JRN_CD = ? ");
			params.add(req.jrnCd);
		}


		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("I2.DLT_FG", dltFgList.size()));
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

	public Itmexps2Chrmst getByPk(String companyCd, String orgnzCd, String itmexpsCd1, String itmexpsCd2) {
		Itmexps2ChrmstPK id = new Itmexps2ChrmstPK();
		id.setCompanyCd(companyCd);
		id.setOrgnzCd(orgnzCd);
		id.setItmexpsCd1(itmexpsCd1);
		id.setItmexpsCd2(itmexpsCd2);

		return em.find(Itmexps2Chrmst.class, id);
	}

	public List<MgExcelEntityItmexpsChrmst> getMasterData(Mg0030SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0030_06"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityItmexpsChrmst.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookItmexpsChrmst book) {
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

		// 組織コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE || '_' || ORGANIZATION_CODE AS CODE_VALUE FROM WFM_ORGANIZATION WHERE DELETE_FLAG = ? and VALID_START_DATE <= SYSDATE and VALID_END_DATE >= SYSDATE ");
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

		// 消費税コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || TAX_CD AS CODE_VALUE FROM TAX_MST WHERE DLT_FG = ? and VD_DT_S <= SYSDATE and VD_DT_E >= SYSDATE ");
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

		// 費目コード一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || ITMEXPS_CD || '_' || ITMEXPS_LEVEL AS CODE_VALUE FROM ITMEXPS_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existItmexpsCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existItmexpsCds.add(code.codeValue);
		}

		// 仕訳コード一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || JRN_CD AS CODE_VALUE FROM JRN_MST WHERE DLT_FG = ?  ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existJrnCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existJrnCds.add(code.codeValue);
		}

		// 勘定科目コード一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || ACC_CD AS CODE_VALUE FROM ACC_MST WHERE DLT_FG = ? and VD_DT_S <= SYSDATE and VD_DT_E >= SYSDATE ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existAccCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existAccCds.add(code.codeValue);
		}

		// 勘定科目補助コード一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || ACC_CD || '_' || ACC_BRKDWN_CD AS CODE_VALUE FROM ACC_BRKDWN_MST WHERE DLT_FG = ? and VD_DT_S <= SYSDATE and VD_DT_E >= SYSDATE ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existAccBrkdwnCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existAccBrkdwnCds.add(code.codeValue);
		}

		// 予算科目コード一覧
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD || '_' || BGT_ITM_CD AS CODE_VALUE FROM BGT_ITM_MST WHERE DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existBdgtaccCds = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existBdgtaccCds.add(code.codeValue);
		}
	}

	public void uploadRegist(MgExcelBookItmexpsChrmst book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 予算科目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0030_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0031_03"));

		for (MgExcelEntityItmexpsChrmst entity : book.sheet.entityList) {
			if (eq("A", entity.processType)) {
				if (!isExistItmexps1Chrmst(entity.companyCd, entity.itmexpsCd1, entity.itmexpsCd2)) {
					insertItmexps1(entity, wfUserRole);
				} else {
					updateItmexps1(entity, wfUserRole);
				}

				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.orgnzCd);
				params.add(entity.itmexpsCd1);
				params.add(entity.itmexpsCd2);

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

			} else {//C OR D

				// ITMEXPS2_CHRMSTのDELETE_FLAGを更新
				List<Object> params = new ArrayList<>();
				params.add(eq("D", entity.processType) ? DeleteFlag.ON : entity.dltFg);
				params.add(entity.companyCd);
				params.add(entity.orgnzCd);
				params.add(entity.itmexpsCd1);
				params.add(entity.itmexpsCd2);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

				if (eq("C", entity.processType)) {

					updateItmexps1(entity, wfUserRole);

				}
			}
		}
	}

	/**
	 * 費目関連1マスタへインサートする前存在チェック
	 * @param req
	 * @return
	 */
	public boolean isExistItmexps1Chrmst(String companyCd, String itmexpsCd1, String itmexpsCd2) {
		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0031_05"));
		String[] params = {companyCd, itmexpsCd1, itmexpsCd2};

		return count(sql.toString(), params) > 0;
	}

	private void updateItmexps1(MgExcelEntityItmexpsChrmst entity, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		StringBuilder updateSql = new StringBuilder(getSql("MG0031_01"));

		List<Object> params = new ArrayList<>();

		params.add(entity.jrnCd);
		params.add(entity.accCd);
		params.add(entity.accBrkdwnCd);
		params.add(entity.mngaccCd);
		params.add(entity.mngaccBrkdwnCd);
		params.add(entity.bdgtaccCd);
		params.add(entity.asstTp);
		params.add(entity.taxCd);
		params.add(entity.slpGrpGl);
		params.add(entity.cstTp);
		params.add(entity.taxSbjTp);

		//params.add(DeleteFlag.OFF);

		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);

		params.add(entity.companyCd);
		params.add(entity.itmexpsCd1);
		params.add(entity.itmexpsCd2);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		query.executeUpdate();
	}

	private void insertItmexps1(MgExcelEntityItmexpsChrmst entity, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		StringBuilder insertSql = new StringBuilder(getSql("MG0031_02"));

		List<Object> params = new ArrayList<>();

		params.add(entity.companyCd);
		params.add(entity.itmexpsCd1);
		params.add(entity.itmexpsCd2);

		params.add(entity.jrnCd);
		params.add(entity.accCd);
		params.add(entity.accBrkdwnCd);
		params.add(entity.mngaccCd);
		params.add(entity.mngaccBrkdwnCd);
		params.add(entity.bdgtaccCd);
		params.add(entity.asstTp);
		params.add(entity.taxCd);
		params.add(entity.slpGrpGl);
		params.add(entity.cstTp);
		params.add(entity.taxSbjTp);

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
