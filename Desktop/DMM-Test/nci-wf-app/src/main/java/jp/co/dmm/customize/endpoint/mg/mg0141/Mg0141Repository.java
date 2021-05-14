package jp.co.dmm.customize.endpoint.mg.mg0141;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.jpa.entity.mw.AccBrkdwnMst;
import jp.co.dmm.customize.jpa.entity.mw.AccBrkdwnMstPK;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 勘定科目補助マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0141Repository extends BaseRepository {

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
	 * 対象の勘定科目補助マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0141Entity get(Mg0141GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0140_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and ABM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 勘定科目コード
		if (isNotEmpty(req.accCd)) {
			sql.append(" and ABM.ACC_CD = ? ");
			params.add(req.accCd);
		}

		// 勘定科目補助コード
		if (isNotEmpty(req.accBrkdwnCd)) {
			sql.append(" and ABM.ACC_BRKDWN_CD = ? ");
			params.add(req.accBrkdwnCd);
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and ABM.SQNO = ? ");
			params.add(req.sqno);
		}

		List<Mg0141Entity> results = new ArrayList<>();

		//検索条件が存在する場合のみ検索(存在しない場合は新規追加s)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = convertEntity(query.getResultList());
		}

		if (results.size() != 0) {
			return results.get(0);
		} else {
			return new Mg0141Entity();
		}
	}

	private List<Mg0141Entity> convertEntity(List<Object[]> results) {

		List<Mg0141Entity> list = new ArrayList<Mg0141Entity>();

		for (Object[] cols : results) {
			Mg0141Entity entity = new Mg0141Entity();

			entity.companyCd = (String)cols[0];
			entity.accCd = (String)cols[1];
			entity.accNm = (String)cols[2];
			entity.accBrkdwnCd = (String)cols[3];
			entity.sqno = cols[4] != null ? ((java.math.BigDecimal)cols[4]).longValue() : null;
			entity.accBrkdwnNm = (String)cols[5];
			entity.accBrkdwnNmS = (String)cols[6];
			entity.vdDtS = (java.util.Date)cols[7];
			entity.vdDtE = (java.util.Date)cols[8];
			entity.dltFg = (String)cols[9];

			list.add(entity);
		}
		return list;
	}


	/**
	 * 勘定科目補助情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0141UpdateRequest req, WfUserRole userRole) {

		// 勘定科目補助マスタ更新(論理削除)
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0141_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.accBrkdwnNm);
		params.add(req.accBrkdwnNmS);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);
		params.add(req.sqno);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 勘定科目補助情報登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0141UpdateRequest req, WfUserRole userRole) {

		// 勘定科目補助マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0141_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);
		params.add(req.companyCd);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);
		params.add(req.accBrkdwnNm);
		params.add(req.accBrkdwnNmS);
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
	 * 勘定科目マスタ存在チェック
	 * @return
	 */
	public int getMaxSqno(Mg0141UpdateRequest req, boolean updateCheck) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0141_03"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.accCd);
		params.add(req.accBrkdwnCd);

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
			params.add(Long.valueOf(req.sqno));
		}

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	/**
	 * 勘定科目マスタ存在チェック
	 * @return
	 */
	public int countExistAcc(Mg0141UpdateRequest req) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0141_04"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.accCd);

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

	public AccBrkdwnMst getByPk(String companyCd, String accCd, String accBrkdwnCd, int sqno) {
		AccBrkdwnMstPK id = new AccBrkdwnMstPK();
		id.setCompanyCd(companyCd);
		id.setAccCd(accCd);
		id.setAccBrkdwnCd(accBrkdwnCd);
		id.setSqno(sqno);

		return em.find(AccBrkdwnMst.class, id);
	}

}
