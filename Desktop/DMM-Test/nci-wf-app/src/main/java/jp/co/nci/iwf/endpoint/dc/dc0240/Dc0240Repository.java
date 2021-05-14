package jp.co.nci.iwf.endpoint.dc.dc0240;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;

/**
 * 文書トレイ一覧(管理者用)リポジトリ
 */
@ApplicationScoped
public class Dc0240Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Dc0240Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("DC0200_01").replaceFirst(REPLACE, " count(*) "));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0240Request req, StringBuilder sql, List<Object> params, boolean paging) {
		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		params.add(localeCode);
		params.add(corporationCode);

		// 所有者ユーザコード
		if (isEmpty(req.ownerUserCode)) {
			params.add(CommonFlag.OFF);	// PERSONAL_USE_FLAG
		} else {
			params.add(CommonFlag.ON);	// PERSONAL_USE_FLAG

			sql.append(" and TC.USER_CODE_CREATED = ?");
			params.add(req.ownerUserCode);
		}

		// トレイ設定コード
		if (isNotEmpty(req.trayConfigCode)) {
			sql.append(" and TC.DOC_TRAY_CONFIG_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.trayConfigCode));
		}
		// トレイ設定名
		if (isNotEmpty(req.trayConfigName)) {
			sql.append(" and TC.DOC_TRAY_CONFIG_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.trayConfigName));
		}
		// システムフラグ
		if (isNotEmpty(req.systemFlag)) {
			sql.append(" and TC.SYSTEM_FLAG = ?");
			params.add(req.systemFlag);
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
	 * ページ制御付で検索
	 * @param req
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<TrayConfig> select(Dc0240Request req, BasePagingResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(
				getSql("DC0200_01").replaceFirst(REPLACE, getSql("DC0200_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(TrayConfig.class, sql.toString(), params.toArray());
	}

	/** トレイ設定を削除 */
	public void delete(Long trayConfigId) {
		MwmDocTrayConfig entity = em.find(MwmDocTrayConfig.class, trayConfigId);
		if (entity != null && eq(entity.getSystemFlag(), CommonFlag.ON))
			throw new BadRequestException("システムデータは削除できません");

		em.remove(entity);
	}
}
