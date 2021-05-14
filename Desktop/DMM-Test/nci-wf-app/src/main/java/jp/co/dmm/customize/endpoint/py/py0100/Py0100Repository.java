package jp.co.dmm.customize.endpoint.py.py0100;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.RcvinspSts;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 支払依頼対象選択画面リポジトリ
 */
@ApplicationScoped
public class Py0100Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0100Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0100_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Py0100Request req, Py0100Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0100_01").replaceFirst(REPLACE, getSql("PY0100_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0100Entity.class, sql, params.toArray());
	}

	private void fillCondition(Py0100Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(req.corporationCode);
		// 第三階層組織CD(部・室)
		if (isEmpty(req.orgnzCd)) {
			sql.append(" and 1 = 0 ");
		} else {
			sql.append(" and P.ORGNZ_CD = ? ");
			params.add(req.orgnzCd);
		}
		// 発注No
		if (isNotEmpty(req.purordNo)) {
			sql.append(" and O.PURORD_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.purordNo));
		}
		// 発注件名
		if (isNotEmpty(req.purordNm)) {
			sql.append(" and O.PURORD_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.purordNm));
		}
		// 検収No
		if (isNotEmpty(req.rcvinspNo)) {
			sql.append(" and P.RCVINSP_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.rcvinspNo));
		}
		// 検収件名
		if (isNotEmpty(req.rcvinspNm)) {
			sql.append(" and P.RCVINSP_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.rcvinspNm));
		}
		// 取引先
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and P.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 発注申請者
		if (isNotEmpty(req.purordSbmtrNm)) {
			sql.append(" and U1.USER_NAME like ? escape '~'");
			params.add(escapeLikeFront(req.purordSbmtrNm));
		}
		// 検収申請者
		if (isNotEmpty(req.rcvinspSbmtrNm)) {
			sql.append(" and U2.USER_NAME like ? escape '~'");
			params.add(escapeLikeFront(req.rcvinspSbmtrNm));
		}

		// 検収完了日
		if (req.rcvinspDtFrom != null) {
			sql.append(" and P.RCVINSP_DT >= ?");
			params.add(req.rcvinspDtFrom);
		}
		if (req.rcvinspDtTo != null) {
			sql.append(" and P.RCVINSP_DT <= ?");
			params.add(req.rcvinspDtTo);
		}
		// 支払予定日
		if (req.payPlnDtFrom != null) {
			sql.append(" and P.PAY_PLN_DT >= ?");
			params.add(req.payPlnDtFrom);
		}
		if (req.payPlnDtTo != null) {
			sql.append(" and P.PAY_PLN_DT <= ?");
			params.add(req.payPlnDtTo);
		}

		// 支払依頼状況
		List<String> rcvinspStsList = new ArrayList<>();
		if (req.rcvinspStsNotYet) {
			rcvinspStsList.add(RcvinspSts.RCVINSP_FIXED);
			rcvinspStsList.add(RcvinspSts.CANCEL);
		}
		if (req.rcvinspStsDone) {
			rcvinspStsList.add(RcvinspSts.PAY_FIXED);
		}
		if (!rcvinspStsList.isEmpty()) {
			sql.append(" and " + toInListSql("P.RCVINSP_STS", rcvinspStsList.size()));
			params.addAll(rcvinspStsList);
		}

		// 検索結果から除外する「発注No＋発注明細No」
		if (req.excludeRcvinspNoList != null && !req.excludeRcvinspNoList.isEmpty()) {
			sql.append(" and (");
			for (int i = 0; i < req.excludeRcvinspNoList.size(); i++) {
				final String row = req.excludeRcvinspNoList.get(i);
				if (isNotEmpty(row)) {
					final String[] values = row.split("-");
					final String rcvinspNo = values[0];
					final int rcvinspDtlNo = Integer.valueOf(values[1]);
					sql.append(i == 0 ? "" : " and ");
					sql.append("not (D.RCVINSP_NO = ? and D.RCVINSP_DTL_NO = ?)");
					params.add(rcvinspNo);
					params.add(rcvinspDtlNo);
				}
			}
			sql.append(")");
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
	 * 選択データ取得
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> getSelectedDataList(Py0100Request req, Py0100Response res) {
		if (isEmpty(req.selectedRcvinspNoList)) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0100_03").replaceFirst(REPLACE, getSql("PY0100_04")));

		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		// 検索結果から選択された「検収No＋検収明細No」で絞込み条件を追加
		sql.append(" (D.COMPANY_CD, D.RCVINSP_NO, D.RCVINSP_DTL_NO) in (");
		for (int i = 0; i < req.selectedRcvinspNoList.size(); i++) {
			final String row = req.selectedRcvinspNoList.get(i);
			if (isNotEmpty(row)) {
				final String[] values = row.split("-");
				final String rcvinspNo = values[0];
				final int rcvinspDtlNo = Integer.valueOf(values[1]);
				sql.append(i == 0 ? "" : ", ");
				sql.append("(?, ?, ?)");
				params.add(req.corporationCode);
				params.add(rcvinspNo);
				params.add(rcvinspDtlNo);
			}
		}
		sql.append(")");
		// ソート
		sql.append(toSortSql("D.COMPANY_CD, D.RCVINSP_NO, D.RCVINSP_DTL_NO", true));
		return select(Py0100Entity.class, sql, params.toArray());
	}
}
