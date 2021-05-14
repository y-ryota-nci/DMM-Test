package jp.co.nci.iwf.endpoint.dc.dc0060;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaItemEx;

/**
 * 拡張項目一覧リポジトリ.
 */
@ApplicationScoped
public class Dc0060Repository extends BaseRepository {

	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Dc0060Request req) {
		StringBuilder sql = new StringBuilder(getSql("DC0060_02"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		final StringBuilder countSql = new StringBuilder(getSql("DC0060_01").replaceFirst("###DC0060_02###", sql.toString()));

		return count(countSql.toString(), params.toArray());
	}

	/**
	 * 検索エンティティ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<MwmMetaItemEx> select(Dc0060Request req, Dc0060Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("DC0060_02"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, true);

		return select(MwmMetaItemEx.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0060Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(LoginInfo.get().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and A.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// メタコード
		if (isNotEmpty(req.metaCode)) {
			sql.append(" and A.META_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.metaCode));
		}
		// メタ名称
		if (isNotEmpty(req.metaName)) {
			sql.append(" and A.META_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.metaName));
		}
		// 入力タイプ
		if (isNotEmpty(req.inputType)) {
			sql.append(" and A.INPUT_TYPE = ?");
			params.add(req.inputType);
		}
		// 必須フラグ
		if (req.requiredFlag) {
			sql.append(" and A.REQUIRED_FLAG = ?");
			params.add(CommonFlag.ON);
		}
		// 削除区分
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" and A.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}

		// ページングおよびソート
		if (paging) {
			// ソート
			if (isNotEmpty(req.sortColumn)) {
				sql.append(toSortSql(req.sortColumn, req.sortAsc));
			}
			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}
}
