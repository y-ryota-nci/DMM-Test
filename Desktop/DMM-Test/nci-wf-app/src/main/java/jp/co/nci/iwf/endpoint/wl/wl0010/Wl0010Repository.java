package jp.co.nci.iwf.endpoint.wl.wl0010;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;

/**
 * トレイ設定一覧（管理者）リポジトリ
 */
@ApplicationScoped
public class Wl0010Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Wl0010Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("WL0010_01").replaceFirst(REPLACE, " count(*) "));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Wl0010Request req, StringBuilder sql, List<Object> params, boolean paging) {
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
			sql.append(" and TC.TRAY_CONFIG_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.trayConfigCode));
		}
		// トレイ設定名
		if (isNotEmpty(req.trayConfigName)) {
			sql.append(" and TC.TRAY_CONFIG_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.trayConfigName));
		}
		// システムフラグ
		if (isNotEmpty(req.systemFlag)) {
			sql.append(" and TC.SYSTEM_FLAG = ?");
			params.add(req.systemFlag);
		}
		// 削除区分
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" and TC.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
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
	public List<TrayConfig> select(Wl0010Request req, Wl0010Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(
				getSql("WL0010_01").replaceFirst(REPLACE, getSql("WL0010_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(TrayConfig.class, sql.toString(), params.toArray());
	}

	/** トレイ設定を削除 */
	public void delete(Long trayConfigId) {
		MwmTrayConfig entity = em.find(MwmTrayConfig.class, trayConfigId);
		if (entity != null && eq(entity.getSystemFlag(), CommonFlag.ON))
			throw new BadRequestException("システムデータを削除できません");

		em.remove(entity);
	}
}
