package jp.co.nci.iwf.endpoint.vd.vd0090;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;

/**
 * 新規申請メニュー割当一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0090Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Vd0090Request req) {
		final StringBuilder sql = new StringBuilder(
				getSql("VD0090_01").replaceFirst(REPLACE, " count(*) "));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページ制御付で検索
	 * @param req
	 * @param res
	 * @return
	 */
	public List<MwvScreenProcessMenu> select(Vd0090Request req, Vd0090Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(
				getSql("VD0090_01").replaceFirst(REPLACE, " ROWNUM as ROWNUM_ID, SPM.* "));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(MwvScreenProcessMenu.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0090Request req, StringBuilder sql, List<Object> params, boolean paging) {
		final LoginInfo login = LoginInfo.get();
		params.add(login.getCorporationCode());
		params.add(login.getLocaleCode());

		// メニューID
		if (isNotEmpty(req.menuId)) {
			sql.append(" and SPM.MENU_ID = ?");
			params.add(req.menuId);
		}
		// メニュー名称
		if (isNotEmpty(req.menuName)) {
			sql.append(" and SPM.MENU_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.menuName));
		}
		// 画面プロセスコード
		if (isNotEmpty(req.screenProcessCode)) {
			sql.append(" and SPM.SCREEN_PROCESS_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.screenProcessCode));
		}
		// 画面プロセス名
		if (isNotEmpty(req.screenProcessName)) {
			sql.append(" and SPM.SCREEN_PROCESS_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenProcessName));
		}
		// 画面コード
		if (isNotEmpty(req.screenCode)) {
			sql.append(" and SPM.SCREEN_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.screenCode));
		}
		// 画面名
		if (isNotEmpty(req.screenName)) {
			sql.append(" and SPM.SCREEN_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenName));
		}
		// プロセス定義コード
		if (isNotEmpty(req.processDefCode)) {
			sql.append(" and SPM.PROCESS_DEF_CODE = ?");
			params.add(req.processDefCode);
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

	/** 物理削除 */
	public int delete(Long screenProcessMenuId) {
		MwmScreenProcessMenu entity = em.find(MwmScreenProcessMenu.class, screenProcessMenuId);
		if (entity != null) {
			em.remove(entity);
			return 1;
		}
		return 0;
	}
}
