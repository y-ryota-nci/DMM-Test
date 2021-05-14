package jp.co.dmm.customize.endpoint.mg.mg0321;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 消費税関連マスタ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mg0321Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItemsFromLookup(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select LOOKUP_ID, LOOKUP_NAME2, SCREEN_LOOKUP_ID from MWM_LOOKUP where CORPORATION_CODE = ? and LOOKUP_GROUP_ID = ? and LOCALE_CODE = 'ja' and DELETE_FLAG = '0' order by SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmLookup> results = select(MwmLookup.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmLookup item : results) {
			newItems.add(new OptionItem(item.getLookupId(), item.getLookupName2()));
		}

		return newItems;
	}

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
	 * 対象の消費税関連マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0321Entity get(Mg0321GetRequest req) {

		String col =
			"A.COMPANY_CD," +
			"A.TAX_KND_CD," +
			"A.TAX_SPC," +
			"A.TAX_CD," +
			"A.DLT_FG," +
			"tm.TAX_NM AS TAX_NM";

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0320_01").replaceFirst(REPLACE, col));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 消費税種類コード
		if (isNotEmpty(req.taxKndCd)) {
			sql.append(" and A.TAX_KND_CD = ? ");
			params.add(req.taxKndCd);
		}

		// 消費税種類
		if (isNotEmpty(req.taxSpc)) {
			sql.append(" and A.TAX_SPC = ? ");
			params.add(req.taxSpc);
		}

		List<Object[]> results = new ArrayList<>();

		//検索条件が存在する場合のみ検索(存在しない場合は新規追加)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = query.getResultList();
		}

		if (results.size() != 0) {
			return convertEntity(results.get(0));
		} else {
			return new Mg0321Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0321Entity convertEntity(Object[] result) {

		Mg0321Entity entity = new Mg0321Entity();

		entity.companyCd = (String)result[0];
		entity.taxKndCd = (String)result[1];
		entity.taxSpc = (String)result[2];
		entity.taxCd = (String)result[3];
		entity.dltFg = (String)result[4];
		entity.taxNm = (String)result[5];

		return entity;
	}

	/**
	 * 消費税関連マスタ更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0321UpdateRequest req, WfUserRole userRole) {

		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0320_03"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.taxCd);
		params.add(req.dltFg);

		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.taxKndCd);
		params.add(req.taxSpc);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 消費税関連マスタ新規登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0321UpdateRequest req, WfUserRole userRole) {

		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0320_04"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.taxKndCd);
		params.add(req.taxSpc);
		params.add(req.taxCd);
		params.add(req.dltFg);

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
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

}
