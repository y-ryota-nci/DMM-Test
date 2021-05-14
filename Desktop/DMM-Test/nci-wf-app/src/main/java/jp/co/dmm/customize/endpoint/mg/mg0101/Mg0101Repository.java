package jp.co.dmm.customize.endpoint.mg.mg0101;

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
 * 銀行マスタ一覧画面のリポジトリ
 */
@ApplicationScoped
public class Mg0101Repository extends BaseRepository {

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
	 * 対象の銀行マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0101Entity get(Mg0101GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0100_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

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
			return new Mg0101Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0101Entity convertEntity(Object[] result) {

		Mg0101Entity entity = new Mg0101Entity();

		entity.companyCd = (String)result[0];
		entity.bnkCd = (String)result[1];
		entity.bnkNm = (String)result[2];
		entity.bnkNmS = (String)result[3];
		entity.bnkNmKn = (String)result[4];
		entity.vdDtS = (java.util.Date)result[5];
		entity.vdDtE = (java.util.Date)result[6];
		entity.dltFg = (String)result[7];

		return entity;
	}

	/**
	 * 銀行情報登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0101UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ更新
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0101_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.bnkNm);
		params.add(req.bnkNmS);
		params.add(req.bnkNmKn);
		params.add(req.vdDtS);
		params.add(req.vdDtE);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.bnkCd);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 銀行情報新規登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0101UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0101_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.bnkCd);
		params.add(req.bnkNm);
		params.add(req.bnkNmS);
		params.add(req.bnkNmKn);
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

}
