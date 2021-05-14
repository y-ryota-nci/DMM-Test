package jp.co.dmm.customize.endpoint.py.py0070;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 買掛残高のリポジトリ
 */
@ApplicationScoped
public class Py0070Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0070Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0070_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	public void getTotalAmt(Py0070Request req, Py0070Response res) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0070_01").replaceFirst(REPLACE, "1 as ID, sum(PRV_BAL_AMT_JPY) as PRV_TOTAL_AMT_JPY, sum(DBT_AMT_JPY) as DBT_TOTAL_AMT_JPY, sum(CDT_AMT_JPY) as CDT_TOTAL_AMT_JPY, sum(NXT_BAL_AMT_JPY) as NXT_TOTAL_AMT_JPY"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		List<Py0070TotalAmtEntity> list = select(Py0070TotalAmtEntity.class, sql, params.toArray(), 1, Integer.MAX_VALUE);
		res.prvTotalAmtJpy = BigDecimal.ZERO;
		res.dbtTotalAmtJpy = BigDecimal.ZERO;
		res.cdtTotalAmtJpy = BigDecimal.ZERO;
		res.nxtTotalAmtJpy = BigDecimal.ZERO;
		if (list != null && list.size()>0) {
			res.prvTotalAmtJpy = list.get(0).prvTotalAmtJpy;
			res.dbtTotalAmtJpy = list.get(0).dbtTotalAmtJpy;
			res.cdtTotalAmtJpy = list.get(0).cdtTotalAmtJpy;
			res.nxtTotalAmtJpy = list.get(0).nxtTotalAmtJpy;
		}

		return;
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Py0070Request req, Py0070Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0070_01").replaceFirst(REPLACE, getSql("PY0070_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0070Entity.class, sql, params.toArray());
	}

	/**
	 * Excel出力用 買掛残高抽出処理
	 * @param req
	 * @return
	 */
	public List<Py0070Entity> selectExcelPayableBal(Py0070Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0070_01").replaceFirst(REPLACE, getSql("PY0070_02")));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		return select(Py0070Entity.class, sql, params.toArray());
	}

	public void fillCondition(Py0070Request req, StringBuilder sql, List<Object> params, boolean paging) {

		/**
		 * 固定条件
		 */
		/**
		 * 画面入力条件
		 */
		//期間
		params.add(req.addYmS);
		params.add(req.addYmE);
		//勘定科目コード
		params.add(req.accCd);
		//勘定科目コード
		params.add(req.accCd);
		//計上月
		String[] ym = req.addYmS.split("/");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
		Date dtBgn = cal.getTime();
		ym = req.addYmE.split("/");
		cal.clear();
		cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
		cal.add(Calendar.MONTH, 1);
		Date dtEnd = cal.getTime();
		params.add(dtBgn);
		params.add(dtEnd);
		//計上月の前月
		Date dtPrv = addMonth(dtBgn, -1);
		String prvYm = toStr(dtPrv, "yyyyMM");
		params.add(prvYm);
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and COMPANY_CD = ?");
			params.add(req.companyCd);
		}
		//金額が０で且つ仕訳件数も０の取引先を除く
		if (req.excZeroAmt) {
			sql.append(" and (PRV_BAL_AMT_JPY <> 0 or DBT_AMT_JPY <> 0 or CDT_AMT_JPY <> 0 or JRN_CNT <> 0)");
		}
		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}
		// ページング
		if (paging) {
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
/*
		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
*/
	}

}
