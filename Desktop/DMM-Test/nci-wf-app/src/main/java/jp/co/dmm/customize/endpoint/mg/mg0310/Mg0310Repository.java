package jp.co.dmm.customize.endpoint.mg.mg0310;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgExcelBookZip;
import jp.co.dmm.customize.endpoint.mg.mg0310.excel.MgExcelEntityZip;
import jp.co.dmm.customize.jpa.entity.mw.ZipMst;
import jp.co.dmm.customize.jpa.entity.mw.ZipMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 住所マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0310Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	private final String DMM_COMPANY_CD = "00020";

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
	 * 住所マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0310SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("MG0310_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 住所マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> select(Mg0310SearchRequest req, Mg0310SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

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
			"Z.DLT_FG," +
			"opi1.LABEL AS DLT_FG_NM";

		StringBuilder sql = new StringBuilder(getSql("MG0310_01").replaceFirst(REPLACE, col));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params.toArray());

		return convertEntity(query.getResultList());
	}

	/**
	 * エンティティへの変換処理
	 * @param results
	 * @return
	 */
	private List<Mg0310Entity> convertEntity(List<Object[]> results) {

		List<Mg0310Entity> list = new ArrayList<Mg0310Entity>();

		for (Object[] cols : results) {
			Mg0310Entity entity = new Mg0310Entity();

			entity.companyCd = (String) cols[0];
			entity.zipCd = (String) cols[1];
			entity.sqno = (BigDecimal) cols[2];
			entity.adrPrfCd = (String) cols[3];
			entity.adrPrf = (String) cols[4];
			entity.adrPrfKn = (String) cols[5];
			entity.adr1 = (String) cols[6];
			entity.adr1Kn = (String) cols[7];
			entity.adr2 = (String) cols[8];
			entity.adr2Kn = (String) cols[9];


			entity.dltFg = (String)cols[10];
			entity.dltFgNm = (String)cols[11];

			list.add(entity);
		}
		return list;
	}

	/**
	 * 住所マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0310RemoveRequest req) {
		int delCnt = 0;
		String[] targetArray = req.deleteTarget.split(",");

		for (String target : targetArray) {
			if (StringUtils.isNotEmpty(target)) {
				StringBuilder sql = new StringBuilder(getSql("MG0310_02"));
				delCnt += execSql(sql.toString(), target.split("\\|"));
			}
		}

		return delCnt;
	}

	/**
	 * 住所マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0310SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		// 会社コード：「00020」を固定に設定
		req.companyCd = DMM_COMPANY_CD;
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and Z.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 郵便番号
		if (isNotEmpty(req.zipCd)) {
			sql.append(" and Z.ZIP_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.zipCd.replaceAll("-", "")));
		}

		// 住所名称
		if (isNotEmpty(req.adr)) {
			sql.append(" and Z.ADR_PRF || Z.ADR1|| Z.ADR2 like ? escape '~'");
			params.add(escapeLikeBoth(req.adr));
		}


		// 削除フラグ
		List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("Z.DLT_FG", dltFgList.size()));
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

	/**
	 * アップロードされたファイルをＤＢに登録
	 * @param book
	 */
	public void uploadRegist (MgExcelBookZip book, WfUserRole userRole) {
		for (MgExcelEntityZip entity : book.sheet.entityList) {
			if (in(entity.processType, "C", "D")) {
				updateOrDelete(entity, userRole);
			} else {
				insert(entity, userRole);
			}
		}
	}

	/**
	 * 住所情報更新（アップロード用）
	 * @param entity
	 * @param userRole
	 */
	private void updateOrDelete(MgExcelEntityZip entity, WfUserRole userRole) {
		// 更新情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		ZipMstPK id = new ZipMstPK();
		id.setCompanyCd(DMM_COMPANY_CD);
		id.setZipCd(entity.zipCd);
		id.setSqno(Long.parseLong(entity.sqno));

		ZipMst dbEntity = em.find(ZipMst.class, id);

		if (dbEntity != null) {
			if (eq("D", entity.processType)) {
				dbEntity.setDltFg(DeleteFlag.ON);
			} else {
				// 都道府県
				dbEntity.setAdrPrfCd(entity.adrPrfCd);
				dbEntity.setAdrPrf(entity.adrPrf);
				dbEntity.setAdrPrfKn(entity.adrPrfKn);

				// 市区町村
				dbEntity.setAdr1(entity.adr1);
				dbEntity.setAdr1Kn(entity.adr1Kn);

				// 町名地区
				dbEntity.setAdr2(entity.adr2);
				dbEntity.setAdr2Kn(entity.adr2Kn);

				dbEntity.setDltFg(entity.dltFg);

				dbEntity.setCorporationCodeUpdated(corporationCode);
				dbEntity.setIpUpdated(ipAddr);
				dbEntity.setTimestampUpdated(now);
				dbEntity.setUserCodeUpdated(userCode);
			}

			em.merge(dbEntity);
		}

	}

	/**
	 * 住所情報新規登録（アップロード用）
	 * @param entity
	 * @param userRole
	 */
	private void insert(MgExcelEntityZip entity, WfUserRole userRole) {
		// 登録情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		ZipMst dbEntity = new ZipMst();

		ZipMstPK id = new ZipMstPK();
		id.setCompanyCd(DMM_COMPANY_CD);
		id.setZipCd(entity.zipCd);
		id.setSqno(Long.parseLong(entity.sqno));
		dbEntity.setId(id);

		// 都道府県
		dbEntity.setAdrPrfCd(entity.adrPrfCd);
		dbEntity.setAdrPrf(entity.adrPrf);
		dbEntity.setAdrPrfKn(entity.adrPrfKn);

		// 市区町村
		dbEntity.setAdr1(entity.adr1);
		dbEntity.setAdr1Kn(entity.adr1Kn);

		// 町名地区
		dbEntity.setAdr2(entity.adr2);
		dbEntity.setAdr2Kn(entity.adr2Kn);

		dbEntity.setDltFg(entity.dltFg);

		dbEntity.setCorporationCodeCreated(corporationCode);
		dbEntity.setIpCreated(ipAddr);
		dbEntity.setTimestampCreated(now);
		dbEntity.setUserCodeCreated(userCode);

		dbEntity.setCorporationCodeUpdated(corporationCode);
		dbEntity.setIpUpdated(ipAddr);
		dbEntity.setTimestampUpdated(now);
		dbEntity.setUserCodeUpdated(userCode);

		em.persist(dbEntity);
	}

	public boolean checkByPk(String zipCd, String sqno) {
		StringBuilder sql = new StringBuilder("select count(1) from ZIP_MST ");
		sql.append("where COMPANY_CD = ? ");
		sql.append("and ZIP_CD = ? ");
		sql.append("and SQNO = ? ");

		List<Object> params = new ArrayList<>();
		params.add(DMM_COMPANY_CD);
		params.add(zipCd);
		params.add(sqno);

		Integer results = count(sql, params.toArray());

		return results != null && results.intValue() > 0;
	}

	public List<MgExcelEntityZip> getMasterData(Mg0310SearchRequest req) {
		StringBuilder sql = new StringBuilder("select ZIP_CD, ");
		sql.append("SQNO, ");
		sql.append("ADR_PRF_CD, ");
		sql.append("ADR_PRF, ");
		sql.append("ADR_PRF_KN, ");
		sql.append("ADR1, ");
		sql.append("ADR1_KN, ");
		sql.append("ADR2, ");
		sql.append("ADR2_KN, ");
		sql.append("DLT_FG, ");
		sql.append("ROWNUM as ID, ");
		sql.append("NULL as PROCESS_TYPE ");
		sql.append("from ZIP_MST Z ");
		sql.append("where 1 = 1 ");

		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return select(MgExcelEntityZip.class, sql, params.toArray());
	}
}
