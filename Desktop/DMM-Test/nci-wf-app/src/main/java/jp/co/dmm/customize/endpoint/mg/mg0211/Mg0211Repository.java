package jp.co.dmm.customize.endpoint.mg.mg0211;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.jpa.entity.mw.HldtaxMst;
import jp.co.dmm.customize.jpa.entity.mw.HldtaxMstPK;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 社内レートマスタ登録リポジトリ
 */
@ApplicationScoped
public class Mg0211Repository extends BaseRepository {

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

	@SuppressWarnings("unchecked")
	public Mg0211Entity get(Mg0211Request req) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0211_01"));
		/**
		 * パラメータ生成
		 */
		final List<Object> params = new ArrayList<>();
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and HLD.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}
		//源泉税区分
		if (isNotEmpty(req.hldtaxTp)) {
			sql.append(" and HLD.HLDTAX_TP = ? ");
			params.add(req.hldtaxTp);
		}
		List<Mg0211Entity> results = new ArrayList<>();
		Mg0211Entity entity = null;
		//検索条件が存在する場合のみ検索(存在しない場合は新規追加)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = query.getResultList();

			results = select(Mg0211Entity.class, sql, params.toArray());
			if (results!=null && !results.isEmpty()) {
				entity = results.get(0);
			} else {
				entity = new Mg0211Entity();
			}
		} else {
			entity = new Mg0211Entity();
		}

		return entity;
	}

	/**
	 * 源泉税区分マスタ取得
	 * @param id
	 * @return
	 */
	public HldtaxMst getHldtaxMst(HldtaxMstPK id) {
		HldtaxMst entity = em.find(HldtaxMst.class, id);
		return entity;
	}

	/**
	 * 社内レートマスタ登録
	 * @param inputed
	 */
	public void insert(Mg0211Entity inputed) {
		HldtaxMst entity = new HldtaxMst();
		HldtaxMstPK id = new HldtaxMstPK();
		id.setCompanyCd(inputed.companyCd);
		id.setHldtaxTp(inputed.hldtaxTp);
		entity.setId(id);
		entity.setHldtaxNm(inputed.hldtaxNm);
		entity.setHldtaxRto1(inputed.hldtaxRto1);
		entity.setHldtaxRto2(inputed.hldtaxRto2);
		entity.setAccCd(inputed.accCd);
		entity.setAccBrkdwnCd(inputed.accBrkdwnCd);
		entity.setVdDtS(inputed.vdDtS);
		entity.setVdDtE(inputed.vdDtE);
		entity.setDltFg(inputed.dltFg);
		entity.setCorporationCodeCreated(inputed.corporationCodeCreated);
		entity.setUserCodeCreated(inputed.userCodeCreated);
		entity.setIpCreated(inputed.ipCreated);
		entity.setTimestampCreated(inputed.timestampCreated);
		entity.setCorporationCodeUpdated(inputed.corporationCodeUpdated);
		entity.setUserCodeUpdated(inputed.userCodeUpdated);
		entity.setIpUpdated(inputed.ipUpdated);
		entity.setTimestampUpdated(inputed.timestampUpdated);
		entity.setSortOrder(inputed.sortOrder);
		em.persist(entity);
	}

	/**
	 * 社内レートマスタ更新
	 * @param inputed
	 */
	public void update(Mg0211Entity inputed) {
		HldtaxMstPK id = new HldtaxMstPK();
		id.setCompanyCd(inputed.companyCd);
		id.setHldtaxTp(inputed.hldtaxTp);
		HldtaxMst orgData = getHldtaxMst(id);
		orgData.setHldtaxNm(inputed.hldtaxNm);
		orgData.setHldtaxRto1(inputed.hldtaxRto1);
		orgData.setHldtaxRto2(inputed.hldtaxRto2);
		orgData.setAccCd(inputed.accCd);
		orgData.setAccBrkdwnCd(inputed.accBrkdwnCd);
		orgData.setVdDtS(inputed.vdDtS);
		orgData.setVdDtE(inputed.vdDtE);
		orgData.setDltFg(inputed.dltFg);
		orgData.setCorporationCodeUpdated(inputed.corporationCodeUpdated);
		orgData.setUserCodeUpdated(inputed.userCodeUpdated);
		orgData.setIpUpdated(inputed.ipUpdated);
		orgData.setTimestampUpdated(inputed.timestampUpdated);
		orgData.setSortOrder(inputed.sortOrder);
		em.merge(orgData);
	}

}
