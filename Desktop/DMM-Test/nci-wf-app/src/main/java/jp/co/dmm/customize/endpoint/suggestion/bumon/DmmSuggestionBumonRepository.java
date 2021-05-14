package jp.co.dmm.customize.endpoint.suggestion.bumon;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.sandbox.SandboxSuggestionResponse;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * DMM_Suggestionのリポジトリ
 */
@ApplicationScoped
public class DmmSuggestionBumonRepository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(DmmSuggestionBumonRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("SG_BUMON_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(DmmSuggestionBumonRequest req, SandboxSuggestionResponse res) {
		StringBuilder sql = new StringBuilder(
				getSql("SG_BUMON_01").replaceFirst(REPLACE, getSql("SG_BUMON_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(DmmSuggestionBumonMstEntity.class, sql, params.toArray());
	}

	private void fillCondition(DmmSuggestionBumonRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		if (isEmpty(req.corporationCode)) {
			params.add(LoginInfo.get().getCorporationCode());
		} else {
			params.add(req.corporationCode);
		}

		// 部門コード
		if (isNotEmpty(req.bumonCd)) {
			sql.append(" and BUMON_CD like ? escape '~'");
			params.add(escapeLikeFront(req.bumonCd));
		}
		// 部門名
		if (isNotEmpty(req.bumonNm)) {
			sql.append(" and BUMON_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.bumonNm));
		}
		// ページング
		if (paging) {
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}
}
