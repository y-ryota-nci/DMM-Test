package jp.co.dmm.customize.endpoint.mg.mg0111;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 銀行支店マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0111Repository extends BaseRepository {
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
	 * 対象の銀行支店マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0111Entity get(Mg0111GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0110_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BBM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 銀行コード
		if (isNotEmpty(req.bnkCd)) {
			sql.append(" and BBM.BNK_CD = ? ");
			params.add(req.bnkCd);
		}

		// 銀行支店コード
		if (isNotEmpty(req.bnkbrcCd)) {
			sql.append(" and BBM.BNKBRC_CD = ? ");
			params.add(req.bnkbrcCd);
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
			return new Mg0111Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0111Entity convertEntity(Object[] result) {

		Mg0111Entity entity = new Mg0111Entity();

		entity.companyCd = (String)result[0];
		entity.bnkCd = (String)result[1];
		entity.bnkbrcCd = (String)result[3];
		entity.bnkbrcNm = (String)result[4];
		entity.bnkbrcNmS = (String)result[5];
		entity.bnkbrcNmKn = (String)result[6];
		entity.vdDtS = (java.util.Date)result[7];
		entity.vdDtE = (java.util.Date)result[8];
		entity.dltFg = (String)result[9];

		return entity;
	}

	/**
	 * 銀行支店情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0111UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ更新
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0111_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.bnkbrcNm);
		params.add(req.bnkbrcNmS);
		params.add(req.bnkbrcNmKn);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.bnkCd);
		params.add(req.bnkbrcCd);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 銀行支店情報登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0111UpdateRequest req, WfUserRole userRole) {

		// 銀行支店マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0111_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.bnkCd);
		params.add(req.bnkbrcCd);
		params.add(req.bnkbrcNm);
		params.add(req.bnkbrcNmS);
		params.add(req.bnkbrcNmKn);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
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

	/**
	 * 銀行マスタ存在チェック
	 * @param req リクエスト
	 * @return res レスポンス
	 */
	public int countExistBnk(Mg0111UpdateRequest req) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0100_01").replaceFirst(REPLACE , "count(*)"));

		List<Object> params = new ArrayList<>();
		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 銀行コード
		if (isNotEmpty(req.bnkCd)) {
			sql.append(" and BM.BNK_CD = ? ");
			params.add(req.bnkCd);
		}

		if (req.vdDtE != null) {
			sql.append(" and (VD_DT_S is null or VD_DT_S <= ?) ");
			params.add(req.vdDtE);
		}

		if (req.vdDtS != null) {
			sql.append(" and (VD_DT_E is null or VD_DT_E >= ?) ");
			params.add(req.vdDtS);
		}

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}
}
