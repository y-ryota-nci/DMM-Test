package jp.co.dmm.customize.endpoint.mg.mg0261;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.jpa.entity.mw.CrdcrdBnkaccMst;
import jp.co.dmm.customize.jpa.entity.mw.CrdcrdBnkaccMstPK;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ｸﾚｶ口座マスタ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mg0261Repository extends BaseRepository {

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
	 * 対象のｸﾚｶ口座マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0261Entity get(Mg0261GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0260_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and cbm.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and cbm.SPLR_CD = ? ");
			params.add(StringUtils.trim(req.splrCd));
		}

		// ユーザコード
		if (isNotEmpty(req.usrCd)) {
			sql.append(" and cbm.USR_CD = ? ");
			params.add(req.usrCd);
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
			return new Mg0261Entity();
		}
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private Mg0261Entity convertEntity(Object[] result) {

		Mg0261Entity entity = new Mg0261Entity();
		int col = 0;
		entity.companyCd = (String)result[col++];
		entity.crdCompanyNm = (String)result[col++];
		entity.usrCd = (String)result[col++];
		entity.usrNm = (String)result[col++];
		entity.bnkaccCd = (String)result[col++];
		entity.bnkaccNm = (String)result[col++];
		entity.dltFg = (String)result[col++];
		entity.dltFgNm = (String) result[col++];
		entity.splrCd = (String) result[col++];
		entity.splrNmKj = (String) result[col++];
		entity.splrNmKn = (String) result[col++];
		entity.bnkaccChrgDt = (String) result[col++];

		return entity;
	}

	/**
	 * ｸﾚｶ口座マスタ更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int update(Mg0261UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ更新
		// SQL
		StringBuilder updateSql = new StringBuilder(getSql("MG0260_04"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.crdCompanyNm);
		params.add(req.bnkaccCd);
		params.add(req.bnkaccChrgDt);
		params.add(req.dltFg);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.splrCd);
		params.add(req.usrCd);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * ｸﾚｶ口座マスタ新規登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public int insert(Mg0261UpdateRequest req, WfUserRole userRole) {

		// 銀行マスタ登録
		// SQL
		StringBuilder insertSql = new StringBuilder(getSql("MG0260_05"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.companyCd);
		params.add(req.splrCd);
		params.add(req.usrCd);
		params.add(req.crdCompanyNm);
		params.add(req.bnkaccCd);
		params.add(req.bnkaccChrgDt);
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

	public CrdcrdBnkaccMst getByPk(String companyCd, String splrCd, String usrCd) {
		CrdcrdBnkaccMstPK id = new CrdcrdBnkaccMstPK();
		id.setCompanyCd(companyCd);
		id.setSplrCd(splrCd);
		id.setUsrCd(usrCd);

		return em.find(CrdcrdBnkaccMst.class, id);
	}

}
