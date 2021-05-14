package jp.co.dmm.customize.endpoint.mg.mg0330;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.mg.mg0330.excel.MgExcelBookLnd;
import jp.co.dmm.customize.endpoint.mg.mg0330.excel.MgExcelEntityLnd;
import jp.co.dmm.customize.jpa.entity.mw.LndMst;
import jp.co.dmm.customize.jpa.entity.mw.LndMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 国マスタのリポジトリ
 */
@ApplicationScoped
public class Mg0330Repository extends BaseRepository {

	private static final String REPLACE = quotePattern("${REPLACE}");
	/** ログイン者情報 */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 国マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Mg0330Request req) {
		final StringBuilder sql = new StringBuilder(getSql("MG0330_01").replaceFirst(REPLACE, "count(*)"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		return count(sql.toString(), params.toArray());
	}

	/**
	 * 国マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Mg0330Request req, Mg0330Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		final StringBuilder replace = new StringBuilder();
		replace.append("  L.COMPANY_CD");
		replace.append(", L.LND_CD");
		replace.append(", L.LND_NM");
		replace.append(", L.LND_CD_DJII");
		replace.append(", L.DLT_FG");
		replace.append(", O.LABEL DLT_FG_NM ");

		final StringBuilder sql = new StringBuilder(getSql("MG0330_01").replaceFirst(REPLACE, replace.toString()));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);
		return select(Mg0330Entity.class, sql.toString(), params.toArray());
	}

	/**
	 * 国マスタ情報削除
	 * @param req
	 * @return
	 */
	public int delete(Mg0330RemoveRequest req, WfUserRole userRole) {
		int delCnt = 0;
		final String[] targetArray = StringUtils.split(req.deleteTarget, ",");
		// 更新情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		for (String lndCd : targetArray) {
			final LndMstPK pk = new LndMstPK();
			pk.setCompanyCd(CorporationCodes.DMM_COM);
			pk.setLndCd(lndCd);
			final LndMst entity = em.find(LndMst.class, pk);
			if (isNotEmpty(entity)) {
				entity.setDltFg(DeleteFlag.ON);
				entity.setCorporationCodeUpdated(corporationCode);
				entity.setIpUpdated(ipAddr);
				entity.setTimestampUpdated(now);
				entity.setUserCodeUpdated(userCode);
				em.merge(entity);
				delCnt++;
			}
		}

		return delCnt;
	}

	/**
	 * 国マスタ検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Mg0330Request req, StringBuilder sql, List<Object> params, boolean paging) {

		// ロケールコード
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 会社コード：「00020」を固定に設定
		params.add(CorporationCodes.DMM_COM);

		// 国コード
		if (isNotEmpty(req.lndCd)) {
			sql.append(" and L.LND_CD like ? escape '~'");
			params.add(escapeLikeBoth(req.lndCd));
		}

		// 国名
		if (isNotEmpty(req.lndNm)) {
			sql.append(" and L.LND_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.lndNm));
		}

		// 削除フラグ
		final List<String> dltFgList = new ArrayList<>();

		if (req.dltFgOff) dltFgList.add(DeleteFlag.OFF);
		if (req.dltFgOn) dltFgList.add(DeleteFlag.ON);

		if (dltFgList.size() != 0) {
			sql.append(" and " + toInListSql("L.DLT_FG", dltFgList.size()));
			params.addAll(dltFgList);
		}

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}

		// ページング
		if (paging) {
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/**
	 * アップロードされたファイルをＤＢに登録
	 * @param book
	 */
	public void uploadRegist (MgExcelBookLnd book, WfUserRole userRole) {
		for (MgExcelEntityLnd entity : book.sheet.entityList) {
			if (in(entity.processType, "C", "D")) {
				updateOrDelete(entity, userRole);
			} else {
				insert(entity, userRole);
			}
		}
	}

	/**
	 * 国情報更新（アップロード用）
	 * @param entity
	 * @param userRole
	 */
	private void updateOrDelete(MgExcelEntityLnd entity, WfUserRole userRole) {
		// 更新情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final LndMstPK pk = new LndMstPK();
		pk.setCompanyCd(CorporationCodes.DMM_COM);
		pk.setLndCd(entity.lndCd);

		final LndMst dbEntity = em.find(LndMst.class, pk);

		if (isNotEmpty(dbEntity)) {
			if (eq("D", entity.processType)) {
				dbEntity.setDltFg(DeleteFlag.ON);
			} else {
				// 国名
				dbEntity.setLndNm(entity.lndNm);
				// 国コード（DJII）
				dbEntity.setLndCdDjii(entity.lndCdDjii);
				// ソート順
				dbEntity.setSortOrder(Integer.parseInt(entity.sortOrder));
				dbEntity.setDltFg(entity.dltFg);
			}
			dbEntity.setCorporationCodeUpdated(corporationCode);
			dbEntity.setIpUpdated(ipAddr);
			dbEntity.setTimestampUpdated(now);
			dbEntity.setUserCodeUpdated(userCode);

			em.merge(dbEntity);
		}
	}

	/**
	 * 国情報新規登録（アップロード用）
	 * @param entity
	 * @param userRole
	 */
	private void insert(MgExcelEntityLnd entity, WfUserRole userRole) {
		// 登録情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final LndMst dbEntity = new LndMst();
		final LndMstPK pk = new LndMstPK();
		pk.setCompanyCd(CorporationCodes.DMM_COM);
		pk.setLndCd(entity.lndCd);
		dbEntity.setId(pk);

		// 国名
		dbEntity.setLndNm(entity.lndNm);
		// 国コード（DJII）
		dbEntity.setLndCdDjii(entity.lndCdDjii);
		// ソート順
		dbEntity.setSortOrder(Integer.parseInt(entity.sortOrder));

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

	public boolean checkByPk(String lndCd) {
		final LndMstPK pk = new LndMstPK();
		pk.setCompanyCd(CorporationCodes.DMM_COM);
		pk.setLndCd(lndCd);
		return isNotEmpty(em.find(LndMst.class, pk));
	}

	public List<MgExcelEntityLnd> getMasterData(Mg0330Request req) {
		final StringBuilder replace = new StringBuilder();
		replace.append("  L.LND_CD");
		replace.append(", L.LND_NM");
		replace.append(", L.LND_CD_DJII");
		replace.append(", L.SORT_ORDER");
		replace.append(", L.DLT_FG ");

		final StringBuilder sql = new StringBuilder(getSql("MG0330_01").replaceFirst(REPLACE, replace.toString()));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);
		return select(MgExcelEntityLnd.class, sql, params.toArray());
	}
}
