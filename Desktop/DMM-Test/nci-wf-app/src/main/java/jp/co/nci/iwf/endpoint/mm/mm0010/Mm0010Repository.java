package jp.co.nci.iwf.endpoint.mm.mm0010;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupGroupEx;

/**
 * ルックアップグループ一覧リポジトリ
 */
@ApplicationScoped
public class Mm0010Repository extends BaseRepository implements CodeBook {

	/**
	 * ルックアップグループマスタ検索
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public int count(Mm0010Request req, String localeCode) {
		List<Object> params = new ArrayList<>();

		StringBuilder replace = new StringBuilder();

		if (!CommonUtil.isEmpty(req.corporationCode)) {
			replace.append(" AND mlg.corporation_code = ?");
			params.add(req.corporationCode);
		}

		if (!CommonUtil.isEmpty(req.lookupGroupId)) {
			replace.append(" AND mlg.lookup_group_id LIKE ? escape '~'");
			params.add(escapeLikeFront(req.lookupGroupId));
		}
		if (!CommonUtil.isEmpty(req.lookupGroupName)) {
			replace.append(" AND mlg.lookup_group_name LIKE ? escape '~'");
			params.add(escapeLikeBoth(req.lookupGroupName));
		}
		if (!CommonUtil.isEmpty(req.deleteFlag)) {
			replace.append(" AND mlg.delete_flag = ?");
			params.add(req.deleteFlag);
		}
		replace.append(" AND mlg.locale_code = ?");
		params.add(localeCode);

		String sql = getSql("MM001001").replaceFirst("###REPLACE###", replace.toString());

		return count("SELECT COUNT(*) FROM (" + sql.toString() + ")", params.toArray());
	}

	/**
	 * ルックアップグループマスタ検索
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmLookupGroupEx> search(Mm0010Request req, String localeCode, boolean isPaging) {
		List<Object> params = new ArrayList<>();

		StringBuilder replace = new StringBuilder();

		if (!CommonUtil.isEmpty(req.corporationCode)) {
			replace.append(" AND mlg.corporation_code = ?");
			params.add(req.corporationCode);
		}

		if (!CommonUtil.isEmpty(req.lookupGroupId)) {
			replace.append(" AND mlg.lookup_group_id LIKE ? escape '~'");
			params.add(escapeLikeFront(req.lookupGroupId));
		}
		if (!CommonUtil.isEmpty(req.lookupGroupName)) {
			replace.append(" AND mlg.lookup_group_name LIKE ? escape '~'");
			params.add(escapeLikeBoth(req.lookupGroupName));
		}
		if (!CommonUtil.isEmpty(req.deleteFlag)) {
			replace.append(" AND mlg.delete_flag = ?");
			params.add(req.deleteFlag);
		}
		replace.append(" AND mlg.locale_code = ?");
		params.add(localeCode);

		if (!CommonUtil.isEmpty(req.sortColumn)) {
			replace.append(" ORDER BY");
			String[] cols = req.sortColumn.split(",");
			int i = 0;
			for (String c : cols) {
				if (i == 0) {
					replace.append(" mlg." + c.trim());
				} else {
					replace.append(" , mlg." + c.trim());
				}
				if (!req.sortAsc) {
					replace.append(" DESC");
				}
				i++;
			}
		}
		// ページング
		if (isPaging) {
			replace.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}

		String sql = getSql("MM001001").replaceFirst("###REPLACE###", replace.toString());
		List<MwmLookupGroupEx> list = select(MwmLookupGroupEx.class, sql, params.toArray());
		return list;
	}
}
