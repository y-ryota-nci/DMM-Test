package jp.co.dmm.customize.endpoint.ss.ss0010;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfHd;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfPd;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInfHd;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * SS連携データRepository
 */
@ApplicationScoped
public class Ss0010Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(
				getSql("SS0010_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Ss0010Request req, Ss0010Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("SS0010_01").replaceFirst(REPLACE, getSql("SS0010_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Ss0010Entity.class, sql, params.toArray());
	}

	/**
	 * 検索条件設定
	 * @param req
	 * @param sql
	 * @param params
	 * @param paging
	 */
	private void fillCondition(Ss0010Request req, StringBuilder sql, List<Object> params, boolean paging) {

		/**
		 * 画面入力条件
		 */
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and SND.COMPANY_CD = ?");
			params.add(LoginInfo.get().getCorporationCode());
		}
		//作成日(開始)
		if (isNotEmpty(req.makDtS)) {
			java.sql.Date dt = toDate(req.makDtS, FORMAT_DATE);
			sql.append(" and SND.MAK_DT >= ? ");
			params.add(dt);
		}
		//作成日(終了)
		if (isNotEmpty(req.makDtE)) {
			java.sql.Date dt = toDate(req.makDtE, FORMAT_DATE);
			dt = addDay(dt, 1);
			sql.append(" and SND.MAK_DT < ? ");
			params.add(dt);
		}
		//送信状況
		List<String> sndStsList = new ArrayList<String>();

		if (req.sndStsNon) sndStsList.add("0");
		if (req.sndStsSnd) sndStsList.add("1");
		if (req.sndStsHld) sndStsList.add("9");

		if (sndStsList.size() != 0) {
			sql.append(" and " + toInListSql("SND.SND_STS", sndStsList.size()));
			params.addAll(sndStsList);
		}
		//出力対象
		List<String> datTpList = new ArrayList<String>();

		if (req.datTpGL) datTpList.add("1");
		if (req.datTpAP) datTpList.add("2");

		if (datTpList.size() != 0) {
			sql.append(" and " + toInListSql("SND.DAT_TP", datTpList.size()));
			params.addAll(datTpList);
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
	 * SS-GL送信情報検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsglSndInf> selectSSGLSndInfList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_GL_01"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "SND.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "SND.SSGL_SND_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsglSndInf.class, sql, params.toArray());
	}

	/**
	 * SS-GL送信情報(ヘッダー)検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsglSndInfHd> selectSSGLSndInfHdList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_GL_02"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "HED.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "HED.SSGL_SND_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsglSndInfHd.class, sql, params.toArray());
	}

	/**
	 * SS-GL送信情報(明細)検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsglSndInfDt> selectSSGLSndInfDtList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_GL_03"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSGL_SND_NO"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSGL_SND_DTL_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsglSndInfDt.class, sql, params.toArray());
	}

	/**
	 * SS-AP送信情報検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsapSndInf> selectSSAPSndInfList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_AP_01"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "SND.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "SND.SSAP_SND_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsapSndInf.class, sql, params.toArray());
	}

	/**
	 * SS-AP送信情報(ヘッダー)検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsapSndInfHd> selectSSAPSndInfHdList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_AP_02"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "HED.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "HED.SSAP_SND_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsapSndInfHd.class, sql, params.toArray());
	}

	/**
	 * SS-AP送信情報(支払明細)検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsapSndInfPd> selectSSAPSndInfPdList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_AP_03"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSAP_SND_NO"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSAP_SND_DTL_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsapSndInfPd.class, sql, params.toArray());
	}

	/**
	 * SS-AP送信情報(明細)検索処理
	 * @param req
	 * @return
	 */
	public List<Ss0010SsapSndInfDt> selectSSAPSndInfDtList(Ss0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("SS0010_AP_04"));
		final List<Object> params = new ArrayList<>();

		final List<OrderBy> orderBys = new ArrayList<>();
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.COMPANY_CD"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSAP_SND_NO"));
		orderBys.add(new OrderBy(OrderBy.ASC, "DTL.SSAP_SND_DTL_NO"));

		fillConditionSndInf(req, sql, params, orderBys);

		return select(Ss0010SsapSndInfDt.class, sql, params.toArray());
	}

	private void fillConditionSndInf(Ss0010Request req, StringBuilder sql, List<Object> params, List<OrderBy> orderBys) {

		/**
		 * 画面入力条件
		 */
		//会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and SND.COMPANY_CD = ?");
			params.add(LoginInfo.get().getCorporationCode());
		}
		//作成日(開始)
		if (isNotEmpty(req.makDtS)) {
			java.sql.Date dt = toDate(req.makDtS, FORMAT_DATE);
			sql.append(" and SND.MAK_DT >= ? ");
			params.add(dt);
		}
		//作成日(終了)
		if (isNotEmpty(req.makDtE)) {
			java.sql.Date dt = toDate(req.makDtE, FORMAT_DATE);
			dt = addDay(dt, 1);
			sql.append(" and SND.MAK_DT < ? ");
			params.add(dt);
		}
		//送信状況
		List<String> sndStsList = new ArrayList<String>();

		if (req.sndStsNon) sndStsList.add("0");
		if (req.sndStsSnd) sndStsList.add("1");
		if (req.sndStsHld) sndStsList.add("9");

		if (sndStsList.size() != 0) {
			sql.append(" and " + toInListSql("SND.SND_STS", sndStsList.size()));
			params.addAll(sndStsList);
		}
		// ソート
		if (orderBys.size()>0) {
			sql.append("order by ");
			for (int i=0; i<orderBys.size(); i++) {
				if (i>0) {
					sql.append(",");
				}
				OrderBy order = orderBys.get(i);
				sql.append(order.getColumn()+" "+(order.getSortType()==OrderBy.DESC ? "DESC" : ""));
			}
		}
	}
}
