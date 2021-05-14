package jp.co.dmm.customize.endpoint.mg.mg0311;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.jpa.entity.mw.ZipMst;
import jp.co.dmm.customize.jpa.entity.mw.ZipMstPK;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 住所マスタ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mg0311Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	private final String DMM_COMPANY_CD = "00020";

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
	 * 対象の住所マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0311Entity get(Mg0311GetRequest req) {

		String col =
				"Z.COMPANY_CD," +
				"Z.ZIP_CD," +
				"Z.SQNO," +
				"Z.ADR_PRF_CD," +
				"Z.ADR_PRF," +
				"Z.ADR_PRF_KN," +
				"Z.ADR1," +
				"Z.ADR1_KN," +
				"Z.ADR2," +
				"Z.ADR2_KN," +
				"Z.DLT_FG";

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0310_01").replaceFirst(REPLACE, col));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and Z.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 郵便番号
		if (isNotEmpty(req.zipCd)) {
			sql.append(" and Z.ZIP_CD = ? ");
			params.add(req.zipCd);
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and Z.SQNO = ? ");
			params.add(req.sqno);
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
			return new Mg0311Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0311Entity convertEntity(Object[] result) {

		Mg0311Entity entity = new Mg0311Entity();

		entity.companyCd = (String) result[0];
		entity.zipCd = (String) result[1];
		entity.sqno = (BigDecimal) result[2];
		entity.adrPrfCd = (String) result[3];
		entity.adrPrf = (String) result[4];
		entity.adrPrfKn = (String) result[5];
		entity.adr1 = (String) result[6];
		entity.adr1Kn = (String) result[7];
		entity.adr2 = (String) result[8];
		entity.adr2Kn = (String) result[9];

		entity.dltFg = (String)result[10];

		return entity;
	}

	/**
	 * 住所情報を更新
	 * @param req
	 * @param wfUserRole
	 */
	public void update(Mg0311UpdateRequest req, WfUserRole wfUserRole) {
		//
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// DBに存在する住所情報を取得
		ZipMstPK id = new ZipMstPK();
		id.setCompanyCd(DMM_COMPANY_CD);
		id.setZipCd(req.zipCd);
		id.setSqno(req.sqno.longValue());

		ZipMst dbEntity = em.find(ZipMst.class, id);
		if (dbEntity != null) {
			dbEntity.setAdrPrfCd(req.adrPrfCd);
			dbEntity.setAdrPrf(req.adrPrf);
			dbEntity.setAdrPrfKn(req.adrPrfKn);

			dbEntity.setAdr1(req.adr1);
			dbEntity.setAdr1Kn(req.adr1Kn);
			dbEntity.setAdr2(req.adr2);
			dbEntity.setAdr2Kn(req.adr2Kn);

			dbEntity.setDltFg(req.dltFg);
			dbEntity.setCorporationCodeUpdated(corporationCode);
			dbEntity.setUserCodeUpdated(userCode);
			dbEntity.setIpUpdated(ipAddr);
			dbEntity.setTimestampUpdated(now);

			em.merge(dbEntity);
		}
	}

	/**
	 * 住所情報を新規登録
	 * @param req
	 * @param wfUserRole
	 */
	public void insert(Mg0311UpdateRequest req, WfUserRole wfUserRole) {
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0311_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(DMM_COMPANY_CD);
		params.add(req.zipCd);
		params.add(DMM_COMPANY_CD);
		params.add(req.zipCd);

		params.add(req.adrPrfCd);
		params.add(req.adrPrf);
		params.add(req.adrPrfKn);
		params.add(req.adr1);
		params.add(req.adr1Kn);
		params.add(req.adr2);
		params.add(req.adr2Kn);

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
		query.executeUpdate();

	}

	/**
	 * 登録用存在チェック
	 * @param req
	 * @return 都道府県・市区町村・町名地番が重複か、<br/>
	 * 郵便番号が同じで市区町村が異なるとか場合がないかカウント
	 */
	public int getMaxSqnoInsert(Mg0311UpdateRequest req) {
		// カウントSQL
		StringBuilder sql = new StringBuilder(getSql("MG0311_02"));

		List<Object> params = new ArrayList<>();
		params.add(DMM_COMPANY_CD);
		params.add(req.adrPrf);
		params.add(req.adr1);
		params.add(req.adr2);

		params.add(DMM_COMPANY_CD);
		params.add(req.zipCd);
		params.add(req.adrPrf);
		params.add(req.adr1);

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	/**
	 * 更新用存在チェック
	 * @param req
	 * @return 都道府県・市区町村・町名地番が重複か、<br/>
	 * 郵便番号が同じで市区町村が異なるとか場合がないかカウント
	 */
	public int getMaxSqnoUpdate(Mg0311UpdateRequest req) {
		// カウントSQL
		StringBuilder sql = new StringBuilder(getSql("MG0311_03"));

		List<Object> params = new ArrayList<>();
		params.add(DMM_COMPANY_CD);
		params.add(req.adrPrf);
		params.add(req.adr1);
		params.add(req.adr2);

		params.add(DMM_COMPANY_CD);
		params.add(req.zipCd);
		params.add(req.adrPrf);
		params.add(req.adr1);

		params.add(req.zipCd);
		params.add(req.sqno);

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}


}
