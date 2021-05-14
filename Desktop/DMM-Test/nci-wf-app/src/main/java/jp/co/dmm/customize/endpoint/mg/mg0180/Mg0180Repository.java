package jp.co.dmm.customize.endpoint.mg.mg0180;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadRepository;
import jp.co.dmm.customize.endpoint.mg.mg0180.excel.MgExcelBookInRto;
import jp.co.dmm.customize.endpoint.mg.mg0180.excel.MgExcelEntityInRto;
import jp.co.dmm.customize.jpa.entity.mw.InRtoMst;
import jp.co.dmm.customize.jpa.entity.mw.InRtoMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;

/**
 * 社内レートマスタのリポジトリ
 */
@ApplicationScoped
public class Mg0180Repository extends MgUploadRepository<MgExcelBookInRto, MgExcelEntityInRto, Mg0180Request> {
	private static final String REPLACE = quotePattern("${REPLACE}");

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
	 * 社内レートマスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0180Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0180_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 社内レートマスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Mg0180Request req, Mg0180Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("MG0180_01").replaceFirst(REPLACE, getSql("MG0180_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Mg0180Entity.class, sql, params.toArray());
	}

	/**
	 * 社内レートマスタ削除
	 * @param companyCd
	 * @param mnyCd
	 * @param cmSqno
	 */
	public void delete(InRtoMstPK id) {
		InRtoMst entity = em.find(InRtoMst.class, id);
		entity.setDltFg(DeleteFlag.ON);
		em.merge(entity);
	}

	/**
	 * 社内レートマスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0180Request req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and RTO.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 通貨コード
		if (isNotEmpty(req.mnyCd)) {
			sql.append(" and RTO.MNY_CD = ? ");
			params.add(req.mnyCd);
		}

		// 有効期間（開始）
		if (isNotEmpty(req.vdDtS)) {
			sql.append(" and RTO.VD_DT_E >= ? ");
			params.add(req.vdDtS);
		}

		// 有効期間（終了）
		if (isNotEmpty(req.vdDtE)) {
			sql.append(" and RTO.VD_DT_S <= ? ");
			params.add(req.vdDtE);
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<String>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("RTO.DLT_FG", dltFgList.size()));
			params.addAll(dltFgList);
		}

		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	@Override
	public List<MgExcelEntityInRto> getMasterData(Mg0180Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0180_01").replaceFirst(REPLACE, getSql("MG0180_02")));

		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		// ソート
		sql.append(toSortSql(req.sortColumn, req.sortAsc));

		return select(MgExcelEntityInRto.class, sql, params.toArray());
	}

	@Override
	public void getUploadMasterCdInfo(MgExcelBookInRto book) {
		//  会社コード
		{
			String sql = "SELECT DISTINCT CORPORATION_CODE AS CODE_VALUE FROM WFM_CORPORATION WHERE DELETE_FLAG = '0' ";
			book.existCompanyCodes = select(MgMstCode.class, sql).stream().map(m -> m.codeValue).collect(Collectors.toSet());
		}

		// 通貨コード一覧
		{
			String sql = "SELECT DISTINCT COMPANY_CD || '_' || MNY_CD AS CODE_VALUE FROM MNY_MST WHERE DLT_FG = '0' ";
			book.mnyCds = select(MgMstCode.class, sql).stream().map(m -> m.codeValue).collect(Collectors.toSet());
		}
	}

	@Override
	public void uploadRegist(MgExcelBookInRto book, WfUserRole userRole) {
		for (MgExcelEntityInRto data: book.sheet.entityList) {
			if (eq("A", data.processType)) {
				insert(data, userRole);
			} else {
				final InRtoMst entity = get(data);
				if (eq("D", data.processType)) {
					delete(entity, userRole);
				}
				else if ((entity != null && eq("U", data.processType)) || eq("C", data.processType)) {
					update(entity, data, userRole);
				}
				else if (entity == null && eq("U", data.processType)) {
					insert(data, userRole);
				}
				else {

				}
			}
		}
	}

	private InRtoMst get(final MgExcelEntityInRto data) {
		final InRtoMstPK id = new InRtoMstPK();
		id.setCompanyCd(data.companyCd);
		id.setMnyCd(data.mnyCd);
		id.setSqno(data.sqno);
		return em.find(InRtoMst.class, id);
	}

	private void insert(final MgExcelEntityInRto data, WfUserRole userRole) {
		final InRtoMstPK id = new InRtoMstPK();
		id.setCompanyCd(data.companyCd);
		id.setMnyCd(data.mnyCd);
		id.setSqno(data.sqno);
		final InRtoMst entity = new InRtoMst();
		entity.setId(id);
		entity.setInRto(data.inRto);
		entity.setRtoTp(data.rtoTp);
		entity.setVdDtS(data.vdDtS);
		entity.setVdDtE(data.vdDtE);
		entity.setDltFg(data.dltFg);
		entity.setCorporationCodeCreated(userRole.getCorporationCode());
		entity.setUserCodeCreated(userRole.getUserCode());
		entity.setIpCreated(userRole.getIpAddress());
		entity.setTimestampCreated(timestamp());
		entity.setCorporationCodeUpdated(userRole.getCorporationCode());
		entity.setUserCodeUpdated(userRole.getUserCode());
		entity.setIpUpdated(userRole.getIpAddress());
		entity.setTimestampUpdated(timestamp());
		em.persist(entity);
	}

	private void update(final InRtoMst entity, final MgExcelEntityInRto data, WfUserRole userRole) {
		entity.setInRto(data.inRto);
		entity.setRtoTp(data.rtoTp);
		entity.setVdDtS(data.vdDtS);
		entity.setVdDtE(data.vdDtE);
		entity.setDltFg(data.dltFg);
		entity.setCorporationCodeUpdated(userRole.getCorporationCode());
		entity.setUserCodeUpdated(userRole.getUserCode());
		entity.setIpUpdated(userRole.getIpAddress());
		entity.setTimestampUpdated(timestamp());
		em.merge(entity);
	}

	private void delete(final InRtoMst entity, WfUserRole userRole) {
		entity.setDltFg(DeleteFlag.ON);
		entity.setCorporationCodeUpdated(userRole.getCorporationCode());
		entity.setUserCodeUpdated(userRole.getUserCode());
		entity.setIpUpdated(userRole.getIpAddress());
		entity.setTimestampUpdated(timestamp());
		em.merge(entity);
	}

	/**
	 * アップロード時の有効期間の重複チェック.
	 * 今回登録対象外のデータにおいて有効期間が重複するものがあればtrueを返す.
	 * @param companyCd 会社コード
	 * @param mnyCd 通貨コード
	 * @param sqnos 今回登録するデータの連番
	 * @param vdDtS 有効期間(開始)
	 * @param vdDtE 有効期間(終了)
	 * @return
	 */
	public boolean isOverlap(String companyCd, String mnyCd, Set<Integer> sqnos, Date vdDtS, Date vdDtE) {
		final StringBuilder sql = new StringBuilder(getSql("MG0180_03"));
		sql.append(" and SQNO not in (");
		for (int i = 0; i < sqnos.size(); i++) {
			sql.append(i == 0 ? "?" : ", ?");
		}
		sql.append(") ");
		final List<Object> params = new ArrayList<>();
		params.add(companyCd);
		params.add(mnyCd);
		params.add(vdDtE);
		params.add(vdDtS);
		params.addAll(sqnos);

		return count(sql, params.toArray()) > 0;
	}

	/**
	 * PKによるマスタ存在チェック.
	 * @param companyCd 会社コード
	 * @param mnyCd 通貨コード
	 * @param sqno 連番
	 * @return 存在する場合、true
	 */
	public boolean isExists(String companyCd, String mnyCd, Integer sqno) {
		final StringBuilder sql = new StringBuilder(getSql("MG0180_04"));
		final Object[] params = {companyCd, mnyCd, sqno};
		return count(sql, params) > 0;
	}
}
