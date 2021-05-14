package jp.co.dmm.customize.endpoint.mg.mg0230;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 支払条件マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0230Repository extends BaseRepository {
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
	 * 支払条件マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0230SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0170_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 支払条件マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0230SearchRequest req, Mg0230SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		String col =
			"A.COMPANY_CD," +
			"A.PAY_COND_CD," +
			"A.PAY_COND_NM," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0230_01").replaceFirst(REPLACE, col));
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
	private List<Mg0230Entity> convertEntity(List<Object[]> results) {

		List<Mg0230Entity> list = new ArrayList<Mg0230Entity>();

		for (Object[] cols : results) {
			Mg0230Entity entity = new Mg0230Entity();

			entity.companyCd = (String)cols[0];
			entity.payCondCd = (String)cols[1];
			entity.payCondNm = (String)cols[2];
			entity.dltFg = (String)cols[3];
			entity.dltFgNm = (String)cols[4];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 支払条件マスタ情報削除
	 * @param req
	 * @return
	 */
	public void delete(Mg0230RemoveRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0230_02"));
		final List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.payCondCd);

		execSql(sql.toString(), params.toArray());
	}

	/**
	 * 支払条件マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0230SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 支払条件コード
		if (isNotEmpty(req.payCondCd)) {
			sql.append(" and A.PAY_COND_CD = ? ");
			params.add(req.payCondCd);
		}

		// 支払条件名称
		if (isNotEmpty(req.payCondNm)) {
			sql.append(" and A.PAY_COND_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.payCondNm));
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
}
