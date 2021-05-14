package jp.co.dmm.customize.endpoint.mg.mg0020;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgMstCode;
import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadRepository;
import jp.co.dmm.customize.endpoint.mg.mg0020.excel.MgExcelBookItmexps;
import jp.co.dmm.customize.endpoint.mg.mg0020.excel.MgExcelEntityItmexps;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmexpsMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 費目マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0020Repository extends MgUploadRepository<MgExcelBookItmexps, MgExcelEntityItmexps, Mg0020SearchRequest> {
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
	 * 費目マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0020SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0020_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 費目マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0020SearchRequest req, Mg0020SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("MG0020_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());
		return convertEntity(query.getResultList());
	}

	private List<Mg0020Entity> convertEntity(List<Object[]> results) {

		List<Mg0020Entity> list = new ArrayList<Mg0020Entity>();

		for (Object[] cols : results) {
			Mg0020Entity entity = new Mg0020Entity();

			entity.companyCd = (String)cols[0];
			entity.itmexpsCd = (String)cols[1];
			entity.itmexpsNm = (String)cols[2];
			entity.itmexpsNmS = (String)cols[3];
			entity.itmexpsLevel = cols[4] != null ? ((java.math.BigDecimal)cols[4]).longValue() : null;
			entity.dltFg = (String)cols[5];
			entity.dltFgNm = (String)cols[6];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 費目情報削除
	 * @param req
	 * @return
	 */
	public void delete(Mg0020Request req) {

		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0020_03"));
				final List<Object> params = new ArrayList<>();
				params.add(target.substring(0, target.indexOf("|")));
				params.add(target.substring(target.indexOf("|")+1));

				execSql(sql.toString(), params.toArray());
			}
		}
	}

	/**
	 * 費目マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0020SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and IM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 費目コード
		if (isNotEmpty(req.itmexpsCd)) {
			sql.append(" and IM.ITMEXPS_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.itmexpsCd));
		}

		// 費目名称
		if (isNotEmpty(req.itmexpsNm)) {
			sql.append(" and IM.ITMEXPS_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.itmexpsNm));
		}

		// 費目略称
		if (isNotEmpty(req.itmexpsNmS)) {
			sql.append(" and IM.ITMEXPS_NM_S like ? escape '~'");
			params.add(escapeLikeBoth(req.itmexpsNmS));
		}

		// 費目階層
		if (isNotEmpty(req.itmexpsLevel)) {
			sql.append(" and IM.ITMEXPS_LEVEL like ? escape '~'");
			params.add(escapeLikeBoth(req.itmexpsLevel));
		}

		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("IM.DLT_FG", dltFgList.size()));
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
	public void getUploadMasterCdInfo(MgExcelBookItmexps book) {

		// 会社コード
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CORPORATION_CODE AS CODE_VALUE FROM WFM_CORPORATION WHERE DELETE_FLAG = ? ");
		List<Object> params = new ArrayList<>();
		params.add(CommonFlag.OFF);

		List<MgMstCode> results = select(MgMstCode.class, sql, params.toArray());
		book.existCompanyCodes = new HashSet<String>();
		for (MgMstCode code : results) {
			book.existCompanyCodes.add(code.codeValue);
		}
	}

	@Override
	public List<MgExcelEntityItmexps> getMasterData(Mg0020SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("MG0020_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityItmexps.class, sql, params.toArray());
	}

	/**
	 * アップロードされたファイルをＤＢに登録
	 * @param book Excel Book
	 */
	public void uploadRegist(MgExcelBookItmexps book, WfUserRole userRole) {
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		StringBuilder deleteSql = new StringBuilder(getSql("MG0020_03"));
		StringBuilder updateSql = new StringBuilder(getSql("MG0021_01"));
		StringBuilder insertSql = new StringBuilder(getSql("MG0021_02"));

		for (MgExcelEntityItmexps entity : book.sheet.entityList) {
			if (eq("D", entity.processType)) {
				List<Object> params = new ArrayList<>();
				params.add(entity.companyCd);
				params.add(entity.itmexpsCd);

				Query query = em.createNativeQuery(deleteSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();

			} else if (eq("C", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.itmexpsNm);
				params.add(entity.itmexpsNmS);
				params.add(entity.itmexpsLevel);
				params.add(entity.dltFg);

				params.add(corporationCode);
				params.add(userCode);
				params.add(ipAddr);
				params.add(now);

				params.add(entity.companyCd);
				params.add(entity.itmexpsCd);

				Query query = em.createNativeQuery(updateSql.toString());
				putParams(query, params.toArray());
				query.executeUpdate();
			} else if (eq("A", entity.processType)) {
				List<Object> params = new ArrayList<>();

				params.add(entity.companyCd);
				params.add(entity.itmexpsCd);

				params.add(entity.itmexpsNm);
				params.add(entity.itmexpsNmS);
				params.add(entity.itmexpsLevel);

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
		}

	}

	public ItmexpsMst getByPk(String companyCd, String itmexpsCd) {
		ItmexpsMstPK id = new ItmexpsMstPK();
		id.setCompanyCd(companyCd);
		id.setItmexpsCd(itmexpsCd);

		return em.find(ItmexpsMst.class, id);
	}
}
