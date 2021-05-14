package jp.co.dmm.customize.endpoint.mg.mg0100;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadRepository;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgExcelBookBnk;
import jp.co.dmm.customize.endpoint.mg.mg0100.excel.MgExcelEntityBnk;
import jp.co.dmm.customize.jpa.entity.mw.BnkMst;
import jp.co.dmm.customize.jpa.entity.mw.BnkMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 銀行マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0100Repository extends MgUploadRepository<MgExcelBookBnk, MgExcelEntityBnk, Mg0100SearchRequest> {
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
	 * 銀行マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0100SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0100_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 銀行マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0100SearchRequest req, Mg0100SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder(getSql("MG0100_02"));
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
	private List<Mg0100Entity> convertEntity(List<Object[]> results) {

		List<Mg0100Entity> list = new ArrayList<Mg0100Entity>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		for (Object[] cols : results) {
			Mg0100Entity entity = new Mg0100Entity();

			entity.companyCd = (String)cols[0];
			entity.bnkCd = (String)cols[1];
			entity.bnkNm = (String)cols[2];
			entity.bnkNmS = (String)cols[3];
			entity.bnkNmKn = (String)cols[4];
			entity.vdDtS = (java.util.Date)cols[5];
			entity.vdDtE = (java.util.Date)cols[6];
			entity.dltFg = (String)cols[7];
			entity.dltFgNm = (String)cols[8];

			entity.vdDtSStr = cols[5] != null ? sd.format(((java.util.Date)cols[5])) : null;
			entity.vdDtEStr = cols[6] != null ? sd.format(((java.util.Date)cols[6])) : null;

			list.add(entity);
		}
		return list;
	}

	/**
	 * 銀行マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0100Request req) {


		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0100_03"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 銀行マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0100SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 銀行コード
		if (isNotEmpty(req.bnkCd)) {
			sql.append(" and BM.BNK_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkCd));
		}

		// 銀行名称
		if (isNotEmpty(req.bnkNm)) {
			sql.append(" and BM.BNK_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bnkNm));
		}

		// 銀行名称（カタカナ）
		if (isNotEmpty(req.bnkNmKn)) {
			sql.append(" and to_single_width_kana(BM.BNK_NM_KN) like to_single_width_kana(?) escape '~'");
			params.add(escapeLikeBoth(req.bnkNmKn));
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and BM.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and BM.VD_DT_S <= ? ");
			params.add(req.vdDtE);
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

	@Override
	public void getUploadMasterCdInfo(MgExcelBookBnk book) {

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

	@Override
	public List<MgExcelEntityBnk> getMasterData(Mg0100SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0100_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBnk.class, sql, params.toArray());
	}

	@Override
	public void uploadRegist(MgExcelBookBnk book, WfUserRole userRole) {
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		StringBuilder deleteSql = new StringBuilder(getSql("MG0100_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0101_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0101_02"));

		// まずは削除
		for (MgExcelEntityBnk entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.bnkCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.bnkNm);
				params.add(entity.bnkNmS);
				params.add(entity.bnkNmKn);
				params.add(entity.vdDtS);
				params.add(entity.vdDtE);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bnkCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bnkCd);
				params.add(entity.bnkNm);
				params.add(entity.bnkNmS);
				params.add(entity.bnkNmKn);
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
			}
		}
	}

	public BnkMst getByPk(String companyCd, String bnkCd) {
		BnkMstPK id = new BnkMstPK();
		id.setCompanyCd(companyCd);
		id.setBnkCd(bnkCd);

		return em.find(BnkMst.class, id);
	}
}
