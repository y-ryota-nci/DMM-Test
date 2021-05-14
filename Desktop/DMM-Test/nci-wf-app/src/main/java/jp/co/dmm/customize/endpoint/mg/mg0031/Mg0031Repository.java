package jp.co.dmm.customize.endpoint.mg.mg0031;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 費目関連マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0031Repository extends BaseRepository {

	@Inject
	protected SessionHolder sessionHolder;

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
	 * 対象の費目関連マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0031Entity get(Mg0031GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0030_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

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

		// 費目コード（１）
		if (isNotEmpty(req.itmExpsCd1)) {
			sql.append(" and I2.ITMEXPS_CD1 = ? ");
			params.add(req.itmExpsCd1);
		}

		// 費目コード（２）
		if (isNotEmpty(req.itmExpsCd2)) {
			sql.append(" and I2.ITMEXPS_CD2 = ? ");
			params.add(req.itmExpsCd2);
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
			return new Mg0031Entity();
		}
	}

	private Mg0031Entity convertEntity(Object[] result) {

		Mg0031Entity entity = new Mg0031Entity();

		entity.companyCd = (String) result[0];
		entity.orgnzCd = (String) result[1];
		entity.orgnzNm = (String) result[2];

		entity.itmExpsCd1 = (String) result[3];
		entity.itmExpsNm1 = (String) result[4];
		entity.itmExpsCd2 = (String) result[5];
		entity.itmExpsNm2 = (String) result[6];
		entity.jrnCd = (String) result[7];
		entity.jrnNm = (String) result[8];
		entity.accCd = (String) result[9];
		entity.accNm = (String) result[10];
		entity.accBrkDwnCd = (String) result[11];
		entity.accBrkDwnNm = (String) result[12];
		entity.mngAccCd = (String) result[13];
		entity.mngAccBrkDwnCd = (String) result[14];
		entity.bdgtAccCd = (String) result[15];
		entity.asstTp = (String) result[16];
		entity.taxCd = (String) result[17];
		entity.taxNm = (String) result[18];

		entity.slpGrpGl = (String) result[19];
		entity.cstTp = (String) result[20];


		entity.dltFg = (String)result[21];
		entity.dltFgNm = (String)result[22];

		entity.taxSbjTp = (String)result[23];

		return entity;
	}

	/**
	 * 品目情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0031UpdateRequest req, WfUserRole userRole) {

		// ITMEXPS2_CHRMSTのDELETE_FLAGを更新
		// パラメータ
		final List<Object> params = new ArrayList<>();
		StringBuilder deleteSql = new StringBuilder(getSql("MG0030_03"));

		params.add(req.dltFg);
		params.add(req.companyCd);
		params.add(req.orgnzCd);
		params.add(req.itmExpsCd1);
		params.add(req.itmExpsCd2);

		Query query = em.createNativeQuery(deleteSql.toString());
		putParams(query, params.toArray());
		query.executeUpdate();

		// ITMEXPS1_CHRMST更新
		return updateItmExps1(req, userRole);

	}

	/**
	 * 品目登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0031UpdateRequest req, WfUserRole userRole) {
		// ITMEXPS2_CHRMST登録または更新
		if (!isExistItmexps1Chrmst(req)) {
			insertItmExps1(req, userRole);
		} else {
			updateItmExps1(req, userRole);
		}

		// ITMEXPS1_CHRMST更新
		return insertItmExps2(req, userRole);
	}

	int insertItmExps1(Mg0031UpdateRequest req, WfUserRole userRole) {
		// 費目関連マスタ１登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0031_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.itmExpsCd1);
		params.add(req.itmExpsCd2);
		params.add(req.jrnCd);
		params.add(req.accCd);
		params.add(req.accBrkDwnCd);
		params.add(req.mngAccCd);
		params.add(req.mngAccBrkDwnCd);
		params.add(req.bdgtAccCd);
		params.add(req.asstTp);
		params.add(req.taxCd);
		params.add(req.slpGrpGl);
		params.add(req.cstTp);
		params.add(req.taxSbjTp);
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

	int updateItmExps1(Mg0031UpdateRequest req, WfUserRole userRole) {
		// 費目マスタ更新
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0031_01"));

		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final List<Object> params = new ArrayList<>();
		params.add(req.jrnCd);
		params.add(req.accCd);
		params.add(req.accBrkDwnCd);
		params.add(req.mngAccCd);
		params.add(req.mngAccBrkDwnCd);
		params.add(req.bdgtAccCd);
		params.add(req.asstTp);
		params.add(req.taxCd);
		params.add(req.slpGrpGl);
		params.add(req.cstTp);
		params.add(req.taxSbjTp);

		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.itmExpsCd1);
		params.add(req.itmExpsCd2);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		return query.executeUpdate();
	}

	int insertItmExps2(Mg0031UpdateRequest req, WfUserRole userRole) {
		// 費目関連マスタ２登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0031_03"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.orgnzCd);
		params.add(req.itmExpsCd1);
		params.add(req.itmExpsCd2);
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

	/**
	 * インサートする前、費目コード（1）・（2）が同様存在されるかどうか（組織コードを関係なく）
	 * @param req
	 * @return
	 */
	public boolean isExist(Mg0031GetRequest req) {
		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0031_04"));
		String[] params = {req.companyCd, req.orgnzCd, req.itmExpsCd1, req.itmExpsCd2};

		return count(sql.toString(), params) > 0;
	}

	/**
	 * 費目関連1マスタへインサートする前存在チェック
	 * @param req
	 * @return
	 */
	public boolean isExistItmexps1Chrmst(Mg0031UpdateRequest req) {
		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0031_05"));
		String[] params = {req.companyCd, req.itmExpsCd1, req.itmExpsCd2};

		return count(sql.toString(), params) > 0;
	}

}
