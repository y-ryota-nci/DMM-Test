package jp.co.nci.iwf.endpoint.mm.mm0052;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsSequenceSpecEx;

/**
 * 通し番号一覧リポジトリ
 */
@ApplicationScoped
public class Mm0052Repository extends BaseRepository implements CodeBook {
	@Inject private SessionHolder sessionHolder;


	/**
	 * 通し番号マスタの件数
	 * @param req
	 * @return
	 */
	public int count(Mm0052Request req) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 通し番号マスタ検索
	 * @param req
	 * @return
	 */
	public List<MwmPartsSequenceSpecEx> search(Mm0052Request req) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(MwmPartsSequenceSpecEx.class, sql.toString(), params.toArray());
	}

	private void fillCondition(Mm0052Request req, StringBuilder sql, List<Object> params, boolean paging) {
		if (paging)
			sql.append(getSql("MM0052_02"));
		else
			sql.append(getSql("MM0052_01"));

		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		if (isNotEmpty(req.corporationCode)) {
			sql.append(" AND sq.corporation_code = ?");
			params.add(req.corporationCode);
		}
		if (isNotEmpty(req.partsSequenceSpecCode)) {
			sql.append(" AND sq.parts_sequence_spec_code LIKE ? escape '~'");
			params.add(escapeLikeFront(req.partsSequenceSpecCode));
		}
		if (isNotEmpty(req.partsSequenceSpecName)) {
			sql.append(" AND sq.parts_sequence_spec_name LIKE ? escape '~'");
			params.add(escapeLikeBoth(req.partsSequenceSpecName));
		}
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" AND sq.delete_flag = ?");
			params.add(req.deleteFlag);
		}

		if (paging) {
			if (isNotEmpty(req.sortColumn)) {
				sql.append(" ORDER BY");
				String[] cols = req.sortColumn.split(",");
				int i = 0;
				for (String c : cols) {
					if (i == 0) {
						sql.append(" sq." + c.trim());
					} else {
						sql.append(" , sq." + c.trim());
					}
					if (!req.sortAsc) {
						sql.append(" DESC");
					}
					i++;
				}

				// ページング
				sql.append(" offset ? rows fetch first ? rows only");
				params.add(toStartPosition(req.pageNo, req.pageSize));
				params.add(req.pageSize);
			}
		}
	}
}
