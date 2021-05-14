package jp.co.dmm.customize.endpoint.mg.mg0290;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0290.excel.MgExcelBookBndFlr;
import jp.co.dmm.customize.endpoint.mg.mg0290.excel.MgExcelEntityBndFlr;
import jp.co.dmm.customize.jpa.entity.mw.BndFlrMst;
import jp.co.dmm.customize.jpa.entity.mw.BndFlrMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 結合フロアマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0290Repository extends BaseRepository {
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
	 * 結合フロアマスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0290SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0290_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 結合フロアマスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0290SearchRequest req, Mg0290SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.BND_FLR_CD," +
			"A.BND_FLR_NM," +
			"A.SORT_ORDER," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0290_01").replaceFirst(REPLACE, col));
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
	private List<Mg0290Entity> convertEntity(List<Object[]> results) {

		List<Mg0290Entity> list = new ArrayList<Mg0290Entity>();

		for (Object[] cols : results) {
			Mg0290Entity entity = new Mg0290Entity();

			entity.companyCd = (String)cols[0];
			entity.bndFlrCd = (String)cols[1];
			entity.bndFlrNm = (String)cols[2];
			entity.sortOrder = (BigDecimal) cols[3];
			entity.dltFg = (String)cols[4];
			entity.dltFgNm = (String)cols[5];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 結合フロアマスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0290RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0290_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 結合フロアマスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0290SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 結合フロアコード
		if (isNotEmpty(req.bndFlrCd)) {
			sql.append(" and A.BND_FLR_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.bndFlrCd));
		}

		// 結合フロア名称
		if (isNotEmpty(req.bndFlrNm)) {
			sql.append(" and A.BND_FLR_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bndFlrNm));
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

	public BndFlrMst getByPk(String companyCd, String bndFlrCd) {
		BndFlrMstPK id = new BndFlrMstPK();
		id.setCompanyCd(companyCd);
		id.setBndFlrCd(bndFlrCd);

		return em.find(BndFlrMst.class, id);
	}

	public List<MgExcelEntityBndFlr> getMasterData(Mg0290SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0290_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityBndFlr.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookBndFlr book) {
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

	public void uploadRegist(MgExcelBookBndFlr book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 結合フロアマスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0290_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0290_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0290_04"));

		for (MgExcelEntityBndFlr entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.bndFlrCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.bndFlrNm);
				params.add(entity.sortOrder);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.bndFlrCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.bndFlrCd);
				params.add(entity.bndFlrNm);
				params.add(entity.sortOrder);

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
