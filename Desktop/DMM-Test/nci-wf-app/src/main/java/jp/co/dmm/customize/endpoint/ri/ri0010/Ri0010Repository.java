package jp.co.dmm.customize.endpoint.ri.ri0010;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.component.DmmCodeBook.PurordSts;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 検収対象選択のリポジトリ
 */
@ApplicationScoped
public class Ri0010Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Ri0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("RI0010_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Ri0010SearchRequest req, Ri0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("RI0010_01").replaceFirst(REPLACE, getSql("RI0010_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Ri0010Entity.class, sql, params.toArray());
	}

	private void fillCondition(Ri0010SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(req.corporationCode);
		params.add(req.corporationCode);
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
		// 取引先CD
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and O.SPLR_CD = ?");
			params.add(req.splrCd);
		}
		// 取引先名称
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and O.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 通貨
		if (isNotEmpty(req.mnyCd)) {
			sql.append(" and O.MNY_CD = ?");
			params.add(req.mnyCd);
		}
		// 発注依頼日FromTo
		if (req.purordRqstDtFrom != null) {
			sql.append(" and ? <= PURORD_RQST_DT ");
			params.add(req.purordRqstDtFrom);
		}
		if (req.purordRqstDtTo != null) {
			sql.append(" and PURORD_RQST_DT < ? + 1");
			params.add(req.purordRqstDtTo);
		}
		// 検収予定日From/To
		if (req.inspCompDtFrom != null) {
			sql.append(" and ? <= INSP_COMP_DT");
			params.add(req.inspCompDtFrom);
		}
		if (req.inspCompDtTo != null) {
			sql.append(" and INSP_COMP_DT < ? + 1");
			params.add(req.inspCompDtTo);
		}
		// 支払予定日From/To
		if (req.payPlnDtFrom != null) {
			sql.append(" and ? <= PAY_PLN_DT");
			params.add(req.payPlnDtFrom);
		}
		if (req.payPlnDtTo != null) {
			sql.append(" and PAY_PLN_DT < ? + 1");
			params.add(req.payPlnDtTo);
		}
		// 品目CD
		if (isNotEmpty(req.itmCd)) {
			sql.append(" and D.ITM_CD = ?");
			params.add(req.itmCd);
		}
		// 発注申請者
		if (isNotEmpty(req.sbmtrCd)) {
			sql.append(" and O.SBMTR_CD = ?");
			params.add(req.sbmtrCd);
		}
		if (isNotEmpty(req.sbmtrNm)) {
			sql.append(" and U.USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.sbmtrNm));
		}
		// ステータス
		List<String> purordStsList = new ArrayList<>();
		if (req.purordStsPurordFixed) purordStsList.add(PurordSts.PURORD_FIXED);
		if (req.purordStsRcvinspFixed) purordStsList.add(PurordSts.RCVINSP_FIXED);
		if (req.purordStsPayFixed) purordStsList.add(PurordSts.PAY_FIXED);
		if (req.purordStsCancel) purordStsList.add(PurordSts.CANCEL);
		if (!purordStsList.isEmpty()) {
			sql.append(" and " + toInListSql("O.PURORD_STS", purordStsList.size()));
			params.addAll(purordStsList);
		}
		// 第三階層組織CD(部・室)
		if (isEmpty(req.orgnzCd)) {
			sql.append(" and 1 = 0 ");
		} else {
			sql.append(" and O.ORGNZ_CD = ? ");
			params.add(req.orgnzCd);
		}
		// 検索結果から除外する「発注No＋発注明細No」
		if (req.excludePurOrdNoList != null && !req.excludePurOrdNoList.isEmpty()) {
			sql.append(" and (");
			for (int i = 0; i < req.excludePurOrdNoList.size(); i++) {
				final String row = req.excludePurOrdNoList.get(i);
				if (isNotEmpty(row)) {
					final String[] values = row.split("-");
					final String purordNo = values[0];
					final int purordDtlNo = Integer.valueOf(values[1]);
					sql.append(i == 0 ? "" : " and ");
					sql.append("not (D.PURORD_NO = ? and D.PURORD_DTL_NO = ?)");
					params.add(purordNo);
					params.add(purordDtlNo);
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

	/** 通貨マスタ抽出 */
	public List<MnyMst> getMnyMst(String corporationCode) {
		Object[] params = { corporationCode };
		String sql = getSql("RI0010_03");
		return select(MnyMst.class, sql, params);
	}

}
