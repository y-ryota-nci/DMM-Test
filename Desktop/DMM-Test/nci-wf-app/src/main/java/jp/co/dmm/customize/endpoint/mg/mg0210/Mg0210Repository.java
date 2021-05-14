package jp.co.dmm.customize.endpoint.mg.mg0210;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0210.excel.MgExcelBookHldtax;
import jp.co.dmm.customize.endpoint.mg.mg0210.excel.MgExcelEntityHldtax;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 源泉税区分マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0210Repository extends BaseRepository {
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
	 * 源泉税区分マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0210Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0210_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 源泉税区分マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Mg0210Request req, Mg0210Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("MG0210_01").replaceFirst(REPLACE, getSql("MG0210_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Mg0210Entity.class, sql, params.toArray());
	}

	/**
	 * 源泉税区分マスタ削除
	 * @param companyCd
	 * @param mnyCd
	 * @param sqno
	 */
	public void delete(HldtaxMstPK id) {
		HldtaxMst entity = em.find(HldtaxMst.class, id);
		entity.setDltFg(DeleteFlag.ON);
		em.merge(entity);
	}

	/**
	 * 源泉税区分マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0210Request req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and HLD.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 源泉税区分
		if (isNotEmpty(req.hldtaxTp)) {
			sql.append(" and HLD.HLDTAX_TP = ? ");
			params.add(req.hldtaxTp);
		}

		// 源泉税名称
		if (isNotEmpty(req.hldtaxNm)) {
			sql.append(" and HLD.HLDTAX_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.hldtaxNm));
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and HLD.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and HLD.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<String>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("HLD.DLT_FG", dltFgList.size()));
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

	public HldtaxMst getByPk(String companyCd, String hldtaxTp) {
		HldtaxMstPK id = new HldtaxMstPK();
		id.setCompanyCd(companyCd);
		id.setHldtaxTp(hldtaxTp);

		return em.find(HldtaxMst.class, id);
	}

	public List<MgExcelEntityHldtax> getMasterData(Mg0210Request req) {
		StringBuilder sql = new StringBuilder("select COMPANY_CD, ");
		sql.append("HLDTAX_TP, ");
		sql.append("HLDTAX_NM, ");
		sql.append("HLDTAX_RTO1, ");
		sql.append("HLDTAX_RTO2, ");
		sql.append("ACC_CD, ");
		sql.append("ACC_BRKDWN_CD, ");
		sql.append("VD_DT_S, ");
		sql.append("VD_DT_E, ");
		sql.append("SORT_ORDER, ");
		sql.append("DLT_FG, ");

		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from HLDTAX_MST HLD ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityHldtax.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookHldtax book) {
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

	public void uploadRegist(MgExcelBookHldtax book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		for (MgExcelEntityHldtax entity : book.sheet.entityList) {
			HldtaxMst dbEntity = new HldtaxMst();

			if (eq("D", entity.processType)) {
				dbEntity = getByPk(entity.companyCd, entity.hldtaxTp);

				dbEntity.setDltFg(DeleteFlag.ON);
				dbEntity.setCorporationCodeUpdated(corporationCode);
				dbEntity.setUserCodeUpdated(userCode);
				dbEntity.setIpUpdated(ipAddr);
				dbEntity.setTimestampUpdated(now);

				em.merge(dbEntity);
			} else if (eq("C", entity.processType)) {
				dbEntity = getByPk(entity.companyCd, entity.hldtaxTp);

				dbEntity.setHldtaxNm(entity.hldtaxNm);
				dbEntity.setHldtaxRto1(entity.hldtaxRto1);
				dbEntity.setHldtaxRto2(entity.hldtaxRto2);
				dbEntity.setAccCd(entity.accCd);
				dbEntity.setAccBrkdwnCd(entity.accBrkdwnCd);
				dbEntity.setVdDtS(entity.vdDtS);
				dbEntity.setVdDtE(entity.vdDtE);
				dbEntity.setSortOrder(isEmpty(entity.sortOrder) ? null : new BigDecimal(entity.sortOrder));
				dbEntity.setDltFg(entity.dltFg);

				dbEntity.setCorporationCodeUpdated(corporationCode);
				dbEntity.setUserCodeUpdated(userCode);
				dbEntity.setIpUpdated(ipAddr);
				dbEntity.setTimestampUpdated(now);

				em.merge(dbEntity);

			} else if (eq("A", entity.processType)) {
				HldtaxMstPK id = new HldtaxMstPK();
				id.setCompanyCd(entity.companyCd);
				id.setHldtaxTp(entity.hldtaxTp);

				dbEntity.setId(id);

				dbEntity.setHldtaxNm(entity.hldtaxNm);
				dbEntity.setHldtaxRto1(entity.hldtaxRto1);
				dbEntity.setHldtaxRto2(entity.hldtaxRto2);
				dbEntity.setAccCd(entity.accCd);
				dbEntity.setAccBrkdwnCd(entity.accBrkdwnCd);
				dbEntity.setVdDtS(entity.vdDtS);
				dbEntity.setVdDtE(entity.vdDtE);
				dbEntity.setSortOrder(isEmpty(entity.sortOrder) ? null : new BigDecimal(entity.sortOrder));

				dbEntity.setDltFg(DeleteFlag.OFF);
				dbEntity.setCorporationCodeCreated(corporationCode);
				dbEntity.setUserCodeCreated(userCode);
				dbEntity.setIpCreated(ipAddr);
				dbEntity.setTimestampCreated(now);
				dbEntity.setCorporationCodeUpdated(corporationCode);
				dbEntity.setUserCodeUpdated(userCode);
				dbEntity.setIpUpdated(ipAddr);
				dbEntity.setTimestampUpdated(now);

				em.persist(dbEntity);
			}
		}
	}

}
