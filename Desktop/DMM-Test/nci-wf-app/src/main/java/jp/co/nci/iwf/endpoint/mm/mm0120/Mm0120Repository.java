package jp.co.nci.iwf.endpoint.mm.mm0120;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.ibm.icu.util.Calendar;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmAnnouncement;

/**
 * お知らせ一覧画面リポジトリ
 */
@ApplicationScoped
public class Mm0120Repository extends BaseRepository {
	/** 件数のカウント */
	public int count(Mm0120SearchRequest req) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** 絞込条件を付与 */
	private void fillCondition(Mm0120SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		sql.append(getSql("MM0120_01"));
		params.add(LoginInfo.get().getLocaleCode());

		// 置換文字列（SELECT句）
		{
			final String FIND = "${FIELDS}";
			final int start = sql.indexOf(FIND);
			final int end = start + FIND.length();
			final String replacement = (paging ? getSql("MM0120_02") : "count(*)");
			sql.replace(start, end, replacement);
		}

		// お知らせID
		if (req.announcementId != null) {
			sql.append(" and A.ANNOUNCEMENT_ID = ?");
			params.add(req.announcementId);
		}
		// 企業コード
		{
			// 企業コードにASPが設定されたお知らせは、全企業へのブロードキャストとみなす
			sql.append(" and A.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// 件名
		if (isNotEmpty(req.subject)) {
			sql.append(" and A.SUBJECT like ? escape '~'");
			params.add(escapeLikeBoth(req.subject));
		}
		// 内容
		if (isNotEmpty(req.contents)) {
			sql.append(" and A.CONTENTS like ? escape '~'");
			params.add(escapeLikeBoth(req.contents));
		}
		// 掲載期間
		final Timestamp period = toTimestamp(req.ymd, req.hhmm);
		if (period != null) {
			sql.append(" and A.TIMESTAMP_START <= ? and ? <= A.TIMESTAMP_END");
			params.add(period);
			params.add(period);
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

	/** Timestampへ変換 */
	private Timestamp toTimestamp(java.util.Date ymd, String time) {
		if (ymd != null) {
			final Calendar c = Calendar.getInstance();
			c.setTime(ymd);

			if (isNotEmpty(time)) {
				String[] times = time.split(":");
				c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
				c.set(Calendar.MINUTE, Integer.valueOf(times[1]));
			}
			return new Timestamp(c.getTimeInMillis());
		}
		return null;
	}

	/** ページングして検索 */
	public List<Mm0120Entity> select(Mm0120SearchRequest req, Mm0120SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder();
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Mm0120Entity.class, sql.toString(), params.toArray());
	}

	public void delete(Long announcementId) {
		if (announcementId == null)
			throw new NullPointerException();

		MwmAnnouncement e = em.find(MwmAnnouncement.class, announcementId);
		if (e != null)
			e.setDeleteFlag(DeleteFlag.ON);
	}

}
