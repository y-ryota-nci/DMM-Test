package jp.co.dmm.customize.endpoint.mg.mg0161;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMstPK;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 消費税マスタ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mg0161Repository extends BaseRepository {
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
	 * 対象の消費税マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0161Entity get(Mg0161GetRequest req) {

		// SQL
		String col =
			"A.COMPANY_CD," +
			"A.TAX_CD," +
			"A.SQNO," +
			"A.TAX_NM," +
			"A.TAX_NM_S," +
			"A.TAX_RTO," +
			"A.TAX_TP," +
			"opi2.LABEL AS TAX_TP_NM," +
			"A.TAX_CD_SS," +
			"A.FRC_UNT," +
			"A.FRC_TP," +
			"opi4.LABEL AS FRC_NM," +
			"A.ACC_CD," +
			"am.ACC_NM," +
			"A.ACC_BRKDWN_CD," +
			"abm.ACC_BRKDWN_NM," +
			"A.DC_TP," +
			"opi3.LABEL AS DC_TP_NM," +
			"A.VD_DT_S," +
			"A.VD_DT_E," +
			"A.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0160_01").replaceFirst(REPLACE, col));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 消費税コード
		if (isNotEmpty(req.taxCd)) {
			sql.append(" and A.TAX_CD = ? ");
			params.add(req.taxCd);
		}

		// 連番
		if (req.sqno != null) {
			sql.append(" and A.SQNO = ? ");
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
			return new Mg0161Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0161Entity convertEntity(Object[] result) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		Mg0161Entity entity = new Mg0161Entity();

		entity.companyCd = (String)result[0];
		entity.taxCd = (String)result[1];
		entity.sqno = result[2] != null ? ((java.math.BigDecimal)result[2]).longValue() : null;
		entity.taxNm = (String)result[3];
		entity.taxNmS = (String)result[4];
		entity.taxRto = (java.math.BigDecimal)result[5];
		entity.taxTp = (String)result[6];
		entity.taxTpNm = (String)result[7];
		entity.taxCdSs = (String)result[8];
		entity.frcUnt = (String)result[9];
		entity.frcTp = (String)result[10];
		entity.frcNm = (String)result[11];
		entity.accCd = (String)result[12];
		entity.accNm = (String)result[13];
		entity.accBrkdwnCd = (String)result[14];
		entity.accBrkdwnNm = (String)result[15];
		entity.dcTp = (String)result[16];
		entity.dcTpNm = (String)result[17];
		entity.vdDtS = (java.util.Date)result[18];
		entity.vdDtE = (java.util.Date)result[19];
		entity.dltFg = (String)result[20];
		entity.dltFgNm = (String)result[21];

		entity.vdDtSStr = result[18] != null ? sd.format(((java.util.Date)result[18])) : null;
		entity.vdDtEStr = result[19] != null ? sd.format(((java.util.Date)result[19])) : null;

		return entity;
	}

	/**
	 * 消費税マスタ更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0161UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ更新
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		int updateCnt = 0;

		// SQL(登録)
		StringBuilder updateSql = new StringBuilder(getSql("MG0160_03"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		params.add(req.taxNm);
		params.add(req.taxNmS);
		params.add(req.taxRto);
		params.add(req.taxTp);
		params.add(req.taxCdSs);
		params.add(req.frcUnt);
		params.add(req.frcTp);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);
		params.add(req.dcTp);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.taxCd);
		params.add(req.sqno);

		Query updateQuery = em.createNativeQuery(updateSql.toString());
		putParams(updateQuery, params.toArray());

		updateCnt = updateQuery.executeUpdate();

		return updateCnt;
	}

	/**
	 * 消費税マスタ新規登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0161UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0160_04"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.taxCd);
		params.add(req.companyCd);
		params.add(req.taxCd);
		params.add(req.taxNm);
		params.add(req.taxNmS);
		params.add(req.taxRto);
		params.add(req.taxTp);
		params.add(req.taxCdSs);
		params.add(req.frcUnt);
		params.add(req.frcTp);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);
		params.add(req.dcTp);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
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
	 * 存在チェック
	 * @param req
	 * @return
	 */
	public int getMaxSqno(Mg0161UpdateRequest req, boolean updateCheck) {
		//連番取得
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0161_01"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.taxCd);

		if (req.vdDtE != null) {
			sql.append(" and (VD_DT_S is null or VD_DT_S <= ?) ");
			params.add(req.vdDtE);
		}

		if (req.vdDtS != null) {
			sql.append(" and (VD_DT_E is null or VD_DT_E >= ?) ");
			params.add(req.vdDtS);
		}

		if (updateCheck) {
			sql.append(" and SQNO != ? ");
			params.add(req.sqno);
		}

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	public TaxMst getByPk(String companyCd, String taxCd, int sqno) {
		TaxMstPK id = new TaxMstPK();
		id.setCompanyCd(companyCd);
		id.setTaxCd(taxCd);
		id.setSqno(sqno);

		return em.find(TaxMst.class, id);
	}
}
