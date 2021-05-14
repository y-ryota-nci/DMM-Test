package jp.co.nci.iwf.endpoint.vd.vd0051;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascriptHistory;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * Javascript設定のリポジトリ
 */
@ApplicationScoped
public class Vd0051Repository extends BaseRepository {
	@Inject
	private NumberingService number;
	@Inject
	private Connection conn;

	/**
	 * 外部JavascriptをPKで抽出
	 * @param javascriptHistoryId
	 * @param localeCode
	 * @return
	 */
	public Vd0051Entity getEntity(Long javascriptHistoryId, String localeCode) {
		final Object[] params = { localeCode, javascriptHistoryId };
		return selectOne(Vd0051Entity.class, getSql("VD0051_01"), params);
	}

	/**
	 * 既存のJavascript履歴を論理削除
	 * @param javascriptId
	 */
	public void deleteHistory(Long javascriptId, LoginInfo login) {
		final Object[] params = {
			login.getCorporationCode(),
			login.getUserCode(),
			timestamp(),
			javascriptId
		};
		execSql(getSql("VD0051_02"), params);
		em.flush();
	}

	/** 新たなJavascript履歴をインサート */
	public long insertHistory(Vd0051Entity entity) {
		final MwmJavascriptHistory h = new MwmJavascriptHistory();
		final long javascriptHistoryId = number.newPK(MwmJavascriptHistory.class);
		final int historyNo = getNextHistoryNo(entity.javascriptId);
		h.setDeleteFlag(DeleteFlag.OFF);
		h.setHistoryNo(historyNo);
		h.setJavascriptHistoryId(javascriptHistoryId);
		h.setJavascriptId(entity.javascriptId);
		h.setScript(entity.script);
		em.persist(h);

		return javascriptHistoryId;
	}

	/** Javascript履歴Noを採番 */
	private int getNextHistoryNo(Long javascriptId) {
		if (javascriptId == null)
			return 1;
		final Object[] params = { javascriptId };
		try {
			List<Map<String, Object>> list = NativeSqlUtils.select(
					conn, getSql("VD0051_03"), params);
			if (list.isEmpty())
				return 1;

			return toInt(list.get(0).get("MAX_HISTORY_NO")) + 1;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Javascriptファイル定義をPKで抽出 */
	public MwmJavascript get(Long javascriptId) {
		if (javascriptId == null)
			return null;
		return em.find(MwmJavascript.class, javascriptId);
	}

	/** Javascriptファイル定義をインサート */
	public long insert(Vd0051Entity entity, LoginInfo login) {
		final long javascriptId = number.next("MWM_JAVASCRIPT", "JAVASCRIPT_ID");
		final MwmJavascript j = new MwmJavascript();
		j.setCorporationCode(login.getCorporationCode());
		j.setDeleteFlag(DeleteFlag.OFF);
		j.setFileName(entity.fileName);
		j.setJavascriptId(javascriptId);
		j.setRemarks(entity.remarks);
		em.persist(j);
		em.flush();
		return javascriptId;
	}

	/** Javascriptファイル定義を更新 */
	public void update(MwmJavascript j, Vd0051Entity entity) {
		j.setRemarks(entity.remarks);
		em.merge(j);
		em.flush();
	}

	public MwmJavascript getByFileName(String corporationCode, String fileName) {
		final Object[] params = { corporationCode, fileName };
		return selectOne(MwmJavascript.class, getSql("VD0051_04"), params);
	}

}
