package jp.co.dmm.customize.endpoint.mg.mg0191;

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
 * 部門マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0191Repository extends BaseRepository {

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
	 * 対象の部門マスタ抽出
	 * @param req リクエスト
	 * @param localeCode ロケーションコード
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0191Entity get(Mg0191GetRequest req, String localeCode) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0190_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(localeCode);
		params.add(localeCode);
		params.add(localeCode);
		params.add(localeCode);
		params.add(localeCode);

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 部門コード
		if (isNotEmpty(req.bumonCd)) {
			sql.append(" and BM.BUMON_CD = ? ");
			params.add(req.bumonCd);
		}

		List<Object[]> results = new ArrayList<>();

		//検索条件が存在する場合のみ検索(存在しない場合は新規追加)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = query.getResultList();
		}

		if (results.size() == 1) {
			return convertEntity(results.get(0));
		} else {
			return new Mg0191Entity();
		}
	}

	private Mg0191Entity convertEntity(Object[] result) {

		Mg0191Entity entity = new Mg0191Entity();

		entity.companyCd = (String)result[0];
		entity.bumonCd = (String)result[1];
		entity.bumonNm = (String)result[2];
		entity.entrpTpCd = (String)result[3];
		entity.entrpCd = (String)result[4];
		entity.entrpNm = (String)result[5];
		entity.tabCd = (String)result[6];
		entity.tabNm = (String)result[7];
		entity.siteCd = (String)result[8];
		entity.siteNm = (String)result[9];
		entity.tpCd = (String)result[10];
		entity.tpNm = (String)result[11];
		entity.areaCd = (String)result[12];
		entity.areaNm = (String)result[13];
		entity.dltFg = (String)result[14];
		entity.dltFgNm = (String)result[15];
		entity.taxKndCd = (String)result[16];
		entity.taxKndNm = (String)result[17];

		return entity;
	}

	/**
	 * 部門情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0191UpdateRequest req, WfUserRole userRole) {

		// 部門マスタ更新
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0191_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.bumonNm);
		params.add(req.taxKndCd);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.bumonCd);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 部門登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0191UpdateRequest req, WfUserRole userRole) {

		// 部門マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0191_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.bumonCd);
		params.add(req.bumonCd.length() >= 3 ? req.bumonCd.substring(0, 3) : "");
		params.add(req.bumonCd.length() >= 6 ? req.bumonCd.substring(3, 6) : "");
		params.add(req.bumonCd.length() >= 9 ? req.bumonCd.substring(6, 9) : "");
		params.add(req.bumonCd.length() >= 10 ? req.bumonCd.substring(9, 10) : "");
		params.add(req.bumonCd.length() >= 11 ? req.bumonCd.substring(10, 11) : "");
		params.add(req.bumonCd.length() == 14 ? req.bumonCd.substring(11, 14) : "");
		params.add(req.bumonNm);
		params.add(req.taxKndCd);
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
