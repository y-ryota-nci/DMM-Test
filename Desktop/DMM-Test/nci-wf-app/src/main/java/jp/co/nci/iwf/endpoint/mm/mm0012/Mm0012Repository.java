package jp.co.nci.iwf.endpoint.mm.mm0012;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップグループ更新
 */
@ApplicationScoped
public class Mm0012Repository extends BaseRepository implements CodeBook {
	/**
	 * ルックアップグループマスタ検索
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public MwmLookupGroup searchLookUpGroup(String corporationCode, String lookUpGroupId, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(corporationCode);
		params.add(lookUpGroupId);
		params.add(localeCode);
		List<MwmLookupGroup> list = select(MwmLookupGroup.class, getSql("MM001201"), params.toArray());
		return list.get(0);
	}

	/**
	 * ルックアップグマスタ検索
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmLookupEx> search(Mm0012Request req, String localeCode, boolean isPaging) {
		List<Object> params = new ArrayList<>();
		params.add(req.corporationCode);
		params.add(req.lookupGroupId);
		params.add(localeCode);

		StringBuilder replace = new StringBuilder();
		if (!CommonUtil.isEmpty(req.sortColumn)) {
			replace.append(" ORDER BY");
			String[] cols = req.sortColumn.split(",");
			int i = 0;
			for (String c : cols) {
				if (i == 0) {
					replace.append(" mlu." + c.trim());
				} else {
					replace.append(" , mlu." + c.trim());
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
		String sql = getSql("MM001202").replaceFirst("###REPLACE###", replace.toString());
		List<MwmLookupEx> list = select(MwmLookupEx.class, sql, params.toArray());
		return list;
	}

	public int count(Mm0012Request req, String localeCode) {
		List<Object> params = new ArrayList<>();
		params.add(req.corporationCode);
		params.add(req.lookupGroupId);
		params.add(localeCode);

		String sql = "SELECT COUNT(*) FROM (" + getSql("MM001202").replaceFirst("###REPLACE###", "") + ")";
		return count(sql, params.toArray());
	}

	public MwmLookup getLookup(String corporationCode, String lookUpGroupId, String lookUpId, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(corporationCode);
		params.add(lookUpGroupId);
		params.add(lookUpId);
		params.add(localeCode);
		List<MwmLookup> list = select(MwmLookup.class, getSql("MM001302"), params.toArray());
		if (CommonUtil.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public MwmLookupGroup update(final MwmLookupGroup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmLookup delete(final MwmLookup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}
}
