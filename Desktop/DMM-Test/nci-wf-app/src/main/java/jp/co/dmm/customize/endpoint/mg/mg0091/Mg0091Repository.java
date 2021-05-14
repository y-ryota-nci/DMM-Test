package jp.co.dmm.customize.endpoint.mg.mg0091;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.jpa.entity.mw.BnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.BnkaccMstPK;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 銀行口座マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0091Repository extends BaseRepository {
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
	 * 存在チェック
	 * @param req
	 * @return
	 */
	public int getMaxSqno(Mg0091UpdateRequest req, boolean updateCheck) {
		//連番取得
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0091_03"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.bnkaccCd);
		params.add(req.bnkCd);
		params.add(req.bnkbrcCd);
		params.add(req.bnkaccNo);

		if (req.vdDtE != null) {
			sql.append(" and (VD_DT_S is null or VD_DT_S <= ?) ");
			params.add(req.vdDtE);
		}

		if (req.vdDtS != null) {
			sql.append(" and (VD_DT_E is null or VD_DT_E >= ?) ");
			params.add(req.vdDtS);
		}

		if (updateCheck) {
			sql.append(" and not (COMPANY_CD = ? and BNKACC_CD = ? and SQNO = ?) ");
			params.add(req.companyCd);
			params.add(req.bnkaccCd);
			params.add(Long.valueOf(req.sqno));
		}

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	/**
	 * 対象の銀行口座マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0091Entity get(Mg0091GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0090_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and BAM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 銀行コード
		if (isNotEmpty(req.bnkaccCd)) {
			sql.append(" and BAM.BNKACC_CD = ? ");
			params.add(req.bnkaccCd);
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and BAM.SQNO = ? ");
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
			return new Mg0091Entity();
		}
	}

	private Mg0091Entity convertEntity(Object[] result) {

		Mg0091Entity entity = new Mg0091Entity();

		entity.companyCd = (String)result[0];
		entity.bnkaccCd = (String)result[1];
		entity.sqno = result[2] != null ? ((java.math.BigDecimal)result[2]).longValue() : null;
		entity.bnkCd = (String)result[3];
		entity.bnkNm = (String)result[4];
		entity.bnkbrcCd = (String)result[5];
		entity.bnkbrcNm = (String)result[6];
		entity.bnkaccTp = (String)result[7];
		entity.bnkaccTpNm = (String)result[8];
		entity.bnkaccNo = (String)result[9];
		entity.bnkaccNm = (String)result[10];
		entity.bnkaccNmKn = (String)result[11];
		entity.vdDtS = (java.util.Date)result[12];
		entity.vdDtE = (java.util.Date)result[13];
		entity.dltFg = (String)result[14];

		return entity;
	}

	/**
	 * 銀行口座情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0091UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ更新(論理削除)
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0091_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.bnkCd);
		params.add(req.bnkbrcCd);
		params.add(req.bnkaccTp);
		params.add(req.bnkaccNo);
		params.add(req.bnkaccNm);
		params.add(req.bnkaccNmKn);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.bnkaccCd);
		params.add(req.sqno);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 銀行口座情報登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0091UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0091_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.bnkaccCd);
		params.add(req.companyCd);
		params.add(req.bnkaccCd);
		params.add(req.bnkCd);
		params.add(req.bnkbrcCd);
		params.add(req.bnkaccTp);
		params.add(req.bnkaccNo);
		params.add(req.bnkaccNm);
		params.add(req.bnkaccNmKn);
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

	public BnkaccMst getByPk(String companyCd, String bnkaccCd, int sqno) {
		BnkaccMstPK id = new BnkaccMstPK();
		id.setCompanyCd(companyCd);
		id.setBnkaccCd(bnkaccCd);
		id.setSqno(sqno);

		return em.find(BnkaccMst.class, id);
	}

	/**
	 * 銀行マスタ存在チェック
	 * @param req リクエスト
	 * @return res レスポンス
	 */
	public int countExistBnk(Mg0091UpdateRequest req) {
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
