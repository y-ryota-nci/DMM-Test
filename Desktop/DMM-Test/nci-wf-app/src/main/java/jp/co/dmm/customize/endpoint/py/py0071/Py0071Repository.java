package jp.co.dmm.customize.endpoint.py.py0071;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.AccMst;
import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 買掛残高のリポジトリ
 */
@ApplicationScoped
public class Py0071Repository extends BaseRepository {
	@Inject private CorporationService corp;

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
	 * 取引先取得
	 * @param companyCd 会社コード
	 * @param isEmpty
	 * @return
	 */
	public List<OptionItem> getSplrList(String companyCd, boolean isEmpty) {
		String query = "select * from SPLR_MST s where s.DLT_FG='0' and trunc(sysdate) between VD_DT_S and VD_DT_E and s.COMPANY_CD = '" + companyCd + "' order by SPLR_CD";
		@SuppressWarnings("unchecked")
		List<SplrMst> splrs = em.createNativeQuery(query, SplrMst.class).getResultList();

		List<OptionItem> newItems = new ArrayList<OptionItem>();
		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}
		for (SplrMst splr : splrs) {
			newItems.add(new OptionItem(splr.getId().getSplrCd(), splr.getSplrNmKj()));
		}

		return newItems;
	}

	/**
	 * 会社名取得
	 * @param companyCd 会社コード
	 * @return 会社名
	 */
	public String getCompanyNm(String companyCd) {
		WfmCorporation entity = corp.getWfmCorporation(companyCd);
		String companyNm = "";
		if (entity!=null) {
			companyNm = entity.getCorporationName();
		}
		return companyNm;
	}

	/**
	 * 勘定科目名取得
	 * @param companyCd 会社コード
	 * @param accCd 勘定科目コード
	 * @return 勘定科目名
	 */
	public String getAccNm(String companyCd, String accCd) {
		String query = "select * from ACC_MST s where s.DLT_FG='0' and trunc(sysdate) between VD_DT_S and VD_DT_E and s.COMPANY_CD = '" + companyCd + "' and s.ACC_CD = '" + accCd + "'";
		@SuppressWarnings("unchecked")
		List<AccMst> accs = em.createNativeQuery(query, AccMst.class).getResultList();

		String accNm = "";
		if (accs!=null && accs.size()>0) {
			accNm = accs.get(0).getAccNm();
		}
		return accNm;
	}

	/**
	 * 取引先名取得
	 * @param companyCd 会社コード
	 * @param splrCd 取引先コード
	 * @return 取引先名
	 */
	public String getSplrNm(String companyCd, String splrCd) {
		String query = "select * from SPLR_MST s where s.DLT_FG='0' and trunc(sysdate) between VD_DT_S and VD_DT_E and s.COMPANY_CD = '" + companyCd + "' and s.SPLR_CD = '" + splrCd + "'";
		@SuppressWarnings("unchecked")
		List<SplrMst> splrs = em.createNativeQuery(query, SplrMst.class).getResultList();

		String splrNmKj = "";
		if (splrs!=null && splrs.size()>0) {
			splrNmKj = splrs.get(0).getSplrNmKj();
		}
		return splrNmKj;
	}

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0071Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0071_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}
	public void getTotalAmt(Py0071Request req, Py0071Response res) {
//		StringBuilder sql = new StringBuilder(
//				getSql("PY0071_01").replaceFirst(REPLACE, "1 as ID, sum(decode(JRN.DC_TP,'D',JRN.AMT_JPY,0)) as DBT_TOTAL_AMT_JPY, sum(decode(JRN.DC_TP,'C',JRN.AMT_JPY,0)) as CDT_TOTAL_AMT_JPY"));
		StringBuilder sql = new StringBuilder(
				getSql("PY0070_01").replaceFirst(REPLACE, "1 as ID, sum(PRV_BAL_AMT_JPY) as PRV_TOTAL_AMT_JPY, sum(DBT_AMT_JPY) as DBT_TOTAL_AMT_JPY, sum(CDT_AMT_JPY) as CDT_TOTAL_AMT_JPY, sum(NXT_BAL_AMT_JPY) as NXT_TOTAL_AMT_JPY"));
		List<Object> params = new ArrayList<>();

		fillCondition2(req, sql, params, false);

		List<Py0071TotalAmtEntity> list = select(Py0071TotalAmtEntity.class, sql, params.toArray(), 1, Integer.MAX_VALUE);
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
	public List<?> select(Py0071Request req, Py0071Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0071_01").replaceFirst(REPLACE, getSql("PY0071_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0071Entity.class, sql, params.toArray());
	}

	/**
	 * Excel出力用 買掛残高詳細抽出処理
	 * @param req
	 * @return
	 */
	public List<Py0071Entity> selectExcelPayableBalDtl(Py0071Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0071_01").replaceFirst(REPLACE, getSql("PY0071_02")));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		return select(Py0071Entity.class, sql, params.toArray());
	}

	private void fillCondition(Py0071Request req, StringBuilder sql, List<Object> params, boolean paging) {

		/**
		 * 固定条件
		 */
		//削除フラグ
		sql.append(" and JRN.DLT_FG = ?");
		params.add("0");
		//仕訳伝票ステータス
		sql.append(" and JRN.JRNSLP_STS = ?");
		params.add("0");

		/**
		 * 画面入力条件
		 */
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and JRN.COMPANY_CD = ?");
			params.add(req.companyCd);
		}
		//取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and JRN.SPLR_CD = ?");
			params.add(req.splrCd);
		}
		//勘定科目コード
		if (isNotEmpty(req.accCd)) {
			sql.append(" and JRN.ACC_CD = ?");
			params.add(req.accCd);
		}
		//期間(From)
		if (isNotEmpty(req.addYmS)) {
			String[] ym = req.addYmS.split("/");
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
			Date dtBgn = cal.getTime();
			sql.append(" and JRN.ADD_DT >= ? ");
			params.add(dtBgn);
		}
		//期間(To)
		if (isNotEmpty(req.addYmE)) {
			String[] ym = req.addYmE.split("/");
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Integer.valueOf(ym[0]).intValue(), Integer.valueOf(ym[1]).intValue()-1, 1);
			cal.add(Calendar.MONTH, 1);
			Date dtEnd = cal.getTime();
			sql.append(" and JRN.ADD_DT < ? ");
			params.add(dtEnd);
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

	public void fillCondition2(Py0071Request req, StringBuilder sql, List<Object> params, boolean paging) {

		/**
		 * 固定条件
		 */
		/**
		 * 画面入力条件
		 */
		//計上月
		//params.add(req.addMm);
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
		//取引先コード
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and SPLR_CD = ?");
			params.add(req.splrCd);
		}
	}
}
