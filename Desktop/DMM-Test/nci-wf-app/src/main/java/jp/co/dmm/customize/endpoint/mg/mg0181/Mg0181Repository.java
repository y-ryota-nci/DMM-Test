package jp.co.dmm.customize.endpoint.mg.mg0181;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.jpa.entity.mw.InRtoMst;
import jp.co.dmm.customize.jpa.entity.mw.InRtoMstPK;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 社内レートマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0181Repository extends BaseRepository {

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
	 * 通貨コード取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getMnyCdItems(String companyCd) {
		String query = "select m.* from MNY_MST m where m.COMPANY_CD = ? and m.DLT_FG = '0' order by m.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(companyCd);
		List<OptionItem> newItems = new ArrayList<OptionItem>();
		newItems.add(new OptionItem("", "--"));

		List<MnyMst> mnyMstList = select(MnyMst.class, query, params.toArray());

		for (MnyMst mst : mnyMstList) {
			newItems.add(new OptionItem(mst.getId().getMnyCd(), mst.getMnyNm()));
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
	public Mg0181Entity get(Mg0181Request req) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0181_01"));
		/**
		 * パラメータ生成
		 */
		final List<Object> params = new ArrayList<>();
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and RTO.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}
		//通貨コード
		if (isNotEmpty(req.mnyCd)) {
			sql.append(" and RTO.MNY_CD = ? ");
			params.add(req.mnyCd);
		}
		//連番
		if (isNotEmpty(req.sqno) && req.sqno>0) {
			sql.append(" and RTO.SQNO = ? ");
			params.add(req.sqno);
		}
		List<Mg0181Entity> results = new ArrayList<>();
		Mg0181Entity entity = null;
		//検索条件が存在する場合のみ検索(存在しない場合は新規追加)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = query.getResultList();

			results = select(Mg0181Entity.class, sql, params.toArray());
			if (results!=null && !results.isEmpty()) {
				entity = results.get(0);
			} else {
				entity = new Mg0181Entity();
			}
		} else {
			entity = new Mg0181Entity();
		}

		return entity;
	}

	/**
	 * 社内レートの存在チェック
	 * @return
	 */
	public int getMaxSqno(Mg0181Request req, boolean updateCheck) {
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0181_02"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.mnyCd);

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

	/**
	 * 社内レートマスタ取得
	 * @param companyCd
	 * @param mnyCd
	 * @param cmSqno
	 * @return
	 */
	public InRtoMst getInRtoMst(InRtoMstPK id) {
		InRtoMst entity = em.find(InRtoMst.class, id);
		return entity;
	}

	/**
	 * 社内レートマスタ登録
	 * @param inputed
	 */
	public void insert(Mg0181Entity inputed) {

		//連番値取得
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0181_03"));

		List<Object> params = new ArrayList<>();
		params.add(inputed.companyCd);
		params.add(inputed.mnyCd);

		Integer results = count(sql, params.toArray()) + 1;
		inputed.sqno = results.longValue();

		InRtoMst entity = new InRtoMst();
		InRtoMstPK id = new InRtoMstPK();
		id.setCompanyCd(inputed.companyCd);
		id.setMnyCd(inputed.mnyCd);
		id.setSqno(inputed.sqno);

		entity.setId(id);
		entity.setInRto(inputed.inRto);
		entity.setRtoTp(inputed.rtoTp);
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
		em.persist(entity);
	}

	/**
	 * 社内レートマスタ更新
	 * @param inputed
	 */
	public long update(Mg0181Entity inputed, WfUserRole userRole) {
		InRtoMstPK id = new InRtoMstPK();
		id.setCompanyCd(inputed.companyCd);
		id.setMnyCd(inputed.mnyCd);
		id.setSqno(inputed.sqno);
		InRtoMst orgData = getInRtoMst(id);

		orgData.setInRto(inputed.inRto);
		orgData.setRtoTp(inputed.rtoTp);
		orgData.setVdDtS(inputed.vdDtS);
		orgData.setVdDtE(inputed.vdDtE);
		orgData.setDltFg(inputed.dltFg);

		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		orgData.setCorporationCodeUpdated(corporationCode);
		orgData.setUserCodeUpdated(userCode);
		orgData.setIpUpdated(ipAddr);
		orgData.setTimestampUpdated(now);

		em.merge(orgData);

		return inputed.sqno;
	}
}
