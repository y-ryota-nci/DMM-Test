package jp.co.dmm.customize.endpoint.mg.mg0280;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0280.excel.MgExcelBookMda;
import jp.co.dmm.customize.endpoint.mg.mg0280.excel.MgExcelEntityMda;
import jp.co.dmm.customize.jpa.entity.mw.MdaMst;
import jp.co.dmm.customize.jpa.entity.mw.MdaMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * メディアマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0280Repository extends BaseRepository {
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
	 * メディアマスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0280SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0280_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * メディアマスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0280SearchRequest req, Mg0280SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.MDA_ID," +
			"A.MDA_NM," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0280_01").replaceFirst(REPLACE, col));
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
	private List<Mg0280Entity> convertEntity(List<Object[]> results) {

		List<Mg0280Entity> list = new ArrayList<Mg0280Entity>();

		for (Object[] cols : results) {
			Mg0280Entity entity = new Mg0280Entity();

			entity.companyCd = (String)cols[0];
			entity.mdaId = (String)cols[1];
			entity.mdaNm = (String)cols[2];
			entity.dltFg = (String)cols[3];
			entity.dltFgNm = (String)cols[4];

			list.add(entity);
		}
		return list;
	}

	/**
	 * メディアマスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0280RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0280_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * メディアマスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0280SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// メディアID
		if (isNotEmpty(req.mdaId)) {
			sql.append(" and A.MDA_ID like ? escape '~'");
			params.add(escapeLikeBoth(req.mdaId));
		}

		// メディア名称
		if (isNotEmpty(req.mdaNm)) {
			sql.append(" and A.MDA_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.mdaNm));
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

	public MdaMst getByPk(String companyCd, String mdaId) {
		MdaMstPK id = new MdaMstPK();
		id.setCompanyCd(companyCd);
		id.setMdaId(mdaId);

		return em.find(MdaMst.class, id);
	}

	public List<MgExcelEntityMda> getMasterData(Mg0280SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0280_05"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityMda.class, sql, params.toArray());
	}

	public void getUploadMasterCdInfo(MgExcelBookMda book) {
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

	public void uploadRegist(MgExcelBookMda book, WfUserRole wfUserRole) {
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// メディアマスタ削除・更新・登録のSQL
		StringBuilder deleteSql = new StringBuilder(getSql("MG0280_02"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0280_03"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0280_04"));

		for (MgExcelEntityMda entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.mdaId);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.mdaNm);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.mdaId);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.mdaId);
				params.add(entity.mdaNm);

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
