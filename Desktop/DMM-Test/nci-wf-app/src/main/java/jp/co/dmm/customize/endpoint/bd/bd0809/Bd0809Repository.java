package jp.co.dmm.customize.endpoint.bd.bd0809;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.component.DmmCodeBook.RcvCostPayTp;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 予算分析明細確認画面リポジトリ
 */
@ApplicationScoped
public class Bd0809Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Bd0809Request req) {
		final StringBuilder sql = new StringBuilder();
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 実データ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Bd0809Request req, BasePagingResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder();
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Bd0809Entity.class, sql.toString(), params.toArray());
	}

	/** 共通SQL条件を付与 */
	private void fillCondition(Bd0809Request req, StringBuilder sql, List<Object> params, boolean paging) {
		// ベースのSQLは費用基準か支払基準かで変わる
		String baseSql;

		if (eq(RcvCostPayTp.COST, req.rcvCostPayTp)) {
			baseSql = getSql("BD0809_01");	// 費用基準

		} else {
			baseSql = getSql("BD0809_02");	// 支払基準
		}
		// カウントするか、実データを抽出するか
		if (paging)
			sql.append(baseSql.replaceFirst(REPLACE, "ROWNUM as ID, X.*"));
		else
			sql.append(baseSql.replaceFirst(REPLACE, "count(*)"));

		// 年度と月を年月に変換して、費用計上月とする
		String cstAddYm = req.yrCd + req.mm;
		if (in(req.mm, "01", "02")) {
			cstAddYm = String.valueOf(toInt(req.yrCd) + 1) + req.mm;
		}

		// 暗黙の必須条件
		params.add(req.companyCd);
		params.add(cstAddYm);
		params.add(req.organizationCodeLv3);

		switch (req.bgtItmCd) {
		case"X9999":	//費用合計
			sql.append(" and  BGT_ITM_CD IN (SELECT BGT_ITM_CD FROM BGT_ITM_MST WHERE COMPANY_CD = ? AND DLT_FG = '0' AND BS_PL_TP = '2')" );
			params.add(req.companyCd);
		    break;
		case"X0060":	//変動費計
			sql.append(" and  BGT_ITM_CD IN (?,?) ");
			params.add("X0061");
			params.add("X0062");
		    break;
		case"X0100":	//人権費計
			sql.append(" and  BGT_ITM_CD IN (?,?) ");
			params.add("X0101");
			params.add("X0102");
		    break;
		case"X0110":	//広告宣伝費計
			sql.append(" and  BGT_ITM_CD IN (?,?) ");
			params.add("X0111");
			params.add("X0112");
		    break;
		case"X0130":	//その他販管費計
			sql.append(" and  BGT_ITM_CD IN (?,?,?,?,?,?,?,?) ");
			params.add("X0121");
			params.add("X0122");
			params.add("X0123");
			params.add("X0124");
			params.add("X0125");
			params.add("X0126");
			params.add("X0139");
			params.add("X0138");
		    break;
		case"X0140":	//固定販管費計
			sql.append(" and  BGT_ITM_CD IN (?,?,?,?,?,?,?,?,?,?,?,?) ");
			params.add("X0101");
			params.add("X0102");
			params.add("X0111");
			params.add("X0112");
			params.add("X0121");
			params.add("X0122");
			params.add("X0123");
			params.add("X0124");
			params.add("X0125");
			params.add("X0126");
			params.add("X0139");
			params.add("X0138");
		    break;
		case"X0160":	//償却費等計
			sql.append(" and  BGT_ITM_CD IN (?,?,?) ");
			params.add("X0161");
			params.add("X0162");
			params.add("X0163");
		    break;
		default:
			sql.append(" and  BGT_ITM_CD = ? ");
			params.add(req.bgtItmCd);
		}

		// 案件No
		if (isNotEmpty(req.applicationNo)) {
			sql.append(" and APPLICATION_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.applicationNo));
		}
		// 案件明細No
		if (req.applicationDtlNo != null) {
			sql.append(" and APPLICATION_DTL_NO = ?");
			params.add(req.applicationDtlNo);
		}
		// 費目
		if (isNotEmpty(req.itmexpsNm)) {
			sql.append(" and (ITMEXPS_NM1 like ? escape '~' or ITMEXPS_NM2 like ? escape '~')");
			params.add(escapeLikeBoth(req.itmexpsNm));
			params.add(escapeLikeBoth(req.itmexpsNm));
		}
		// 品目
		if (isNotEmpty(req.itmNm)) {
			sql.append(" and ITM_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.itmNm));
		}
		// 組織
		if (isNotEmpty(req.sbmtrDptNm)) {
			sql.append(" and SBMT_DPT_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.sbmtrDptNm));
		}
		// 申請者
		if (isNotEmpty(req.sbmtrNm)) {
			sql.append(" and SBMTR_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.sbmtrNm));
		}
		// 取引先
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// TODO:管理科目

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			if (paging) {
				sql.append(" offset ? rows fetch first ? rows only");
				params.add(toStartPosition(req.pageNo, req.pageSize));
				params.add(req.pageSize);
			}
		}
	}

}
