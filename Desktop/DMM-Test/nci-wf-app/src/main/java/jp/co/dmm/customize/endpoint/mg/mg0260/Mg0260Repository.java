package jp.co.dmm.customize.endpoint.mg.mg0260;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0260.excel.MgExcelBookCrdcrdBnkacc;
import jp.co.dmm.customize.endpoint.mg.mg0260.excel.MgExcelEntityCrdcrdBnkacc;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ｸﾚｶ口座マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0260Repository extends BaseRepository {

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
	 * クレカ口座マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0260SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0260_01"));

		List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * クレカ口座マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0260SearchRequest req, Mg0260SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder(getSql("MG0260_02"));
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
	private List<Mg0260Entity> convertEntity(List<Object[]> results) {

		List<Mg0260Entity> list = new ArrayList<Mg0260Entity>();

		for (Object[] cols : results) {
			Mg0260Entity entity = new Mg0260Entity();
			int col = 0;
			entity.companyCd = (String) cols[col++];
			entity.crdCompanyNm = (String) cols[col++];
			entity.usrCd = (String) cols[col++];
			entity.usrNm = (String) cols[col++];
			entity.bnkaccCd = (String) cols[col++];
			entity.bnkaccNm = (String) cols[col++];
			entity.dltFg = (String) cols[col++];
			entity.dltFgNm = (String) cols[col++];
			entity.splrCd = (String) cols[col++];
			entity.splrNmKj = (String) cols[col++];
			entity.splrNmKn = (String) cols[col++];
			entity.bnkaccChrgDt = (String) cols[col++];

			list.add(entity);
		}
		return list;
	}

	/**
	 * クレカ口座マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0260RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0260_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * クレカ口座マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0260SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and cbm.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// カード会社名称
		if (isNotEmpty(req.crdCompanyNm)) {
			sql.append(" and cbm.CRD_COMPANY_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.crdCompanyNm));
		}

		// ユーザコード
		if (isNotEmpty(req.usrCd)) {
			sql.append(" and cbm.USR_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.usrCd));
		}

		// 銀行口座コード
		if (isNotEmpty(req.bnkaccCd)) {
			sql.append(" and cbm.BNKACC_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkaccCd));
		}


		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("cbm.DLT_FG", dltFgList.size()));
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

	public List<MgExcelEntityCrdcrdBnkacc> getMasterData(Mg0260SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0260_06"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityCrdcrdBnkacc.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookCrdcrdBnkacc book) {
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

		// ユーザコード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE || '_' || USER_CODE AS CODE_VALUE from WFM_USER_V where LOCALE_CODE = ? AND DELETE_FLAG = '0' ");
		params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		if (!isDGHD) {
			sql.append(" AND CORPORATION_CODE = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existUserCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existUserCodes.add(code.codeValue);
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

		// 銀行口座コード
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT COMPANY_CD  || '_' || BNKACC_CD AS CODE_VALUE  from BNKACC_MST where trunc(sysdate) between VD_DT_S and VD_DT_E and DLT_FG = ? ");
		params = new ArrayList<>();
		params.add(CommonFlag.OFF);
		if (!isDGHD) {
			sql.append(" AND COMPANY_CD = ? ");
			params.add(sessionHolder.getLoginInfo().getCorporationCode());
		}

		results = select(MgMstCode.class, sql, params.toArray());
		book.existBnkaccCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existBnkaccCodes.add(code.codeValue);
		}

	}

	public void uploadRegist(MgExcelBookCrdcrdBnkacc book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 品目マスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0260_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0260_04"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0260_05"));

		for (MgExcelEntityCrdcrdBnkacc entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> deleteParams = new ArrayList<>();
				deleteParams.add(entity.companyCd);
				deleteParams.add(entity.splrCd);
				deleteParams.add(entity.usrCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, deleteParams.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> updateParams = new ArrayList<>();

				updateParams.add(entity.crdCompanyNm);
				updateParams.add(entity.bnkaccCd);
				updateParams.add(entity.bnkaccChrgDt);
				updateParams.add(entity.dltFg);

				updateParams.add(corporationCode);
				updateParams.add(userCode);
				updateParams.add(ipAddr);
				updateParams.add(now);

				updateParams.add(entity.companyCd);
				updateParams.add(entity.splrCd);
				updateParams.add(entity.usrCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, updateParams.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> insertParams = new ArrayList<>();

				insertParams.add(entity.companyCd);
				insertParams.add(entity.splrCd);
				insertParams.add(entity.usrCd);

				insertParams.add(entity.crdCompanyNm);
				insertParams.add(entity.bnkaccCd);
				insertParams.add(entity.bnkaccChrgDt);
				insertParams.add(DeleteFlag.OFF);

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
			}
		}
	}

}
