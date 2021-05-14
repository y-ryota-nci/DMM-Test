package jp.co.dmm.customize.endpoint.bd.bd0808;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.RcvCostPayTp;
import jp.co.dmm.customize.jpa.entity.mw.BgtItmMst;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHstver;
import jp.co.dmm.customize.jpa.entity.mw.VOrganizationLevel;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 特定組織指定バージョン予算／実績分析画面リポジトリ
 */
@ApplicationScoped
public class Bd0808Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**  履歴バージョンの選択肢を抽出 */
	public List<OptionItem> getHstVersions(Bd0808InitRequest req) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(new OptionItem("", "最新バージョン"));

		if (isEmpty(isEmpty(req.yrCd) || isEmpty(req.organizationCodeLv3) || isEmpty(req.rcvCostPayTp))) {
			return items;
		}

		LoginInfo login = sessionHolder.getLoginInfo();
		final Object[] params = {
				login.getCorporationCode(), req.yrCd, req.organizationCodeLv3, req.rcvCostPayTp
		};
		final String sql = getSql("BD0808_01");
		final List<BgtPlnHstver> list = select(BgtPlnHstver.class, sql, params);

		for (BgtPlnHstver e : list) {
			items.add(new OptionItem(e.getId().getHstVersion(), toStr(e.getHstDt(), "yyyy/MM/dd") + " : " + e.getHstNm()));
		}

		return items;
	}

	/** 抽出 */
	public List<Bd0808Result> select(Bd0808SearchRequest req) {
		// 予算
		List<Bd0808Entity> budgets;
		if (isEmpty(req.hstVersion))
			budgets = getBudgets(req);
		else
			budgets = getBudgetVersions(req);

		List<Bd0808Entity> performance = getPerformance(req);

		// マージして１つの表に整形（ここで差異も求める）
		List<Bd0808Result> results = merge(budgets, performance, req);
		return results;
	}

	/** 予算と実績から差異を生成し、それらをつかって単一の表を生成 */
	private List<Bd0808Result> merge(List<Bd0808Entity> budgets, List<Bd0808Entity> performance, Bd0808SearchRequest req) {
		Map<String, Bd0808Entity> performanceMap = performance.stream().collect(Collectors.toMap(e -> e.bgtItmCd, e -> e));

		List<Bd0808Result> results = new ArrayList<>();
		List<BigDecimal> bsTotal_1 = new ArrayList<>();
		List<BigDecimal> bsTotal_2 = new ArrayList<>();
		List<BigDecimal> bsTotal_3 = new ArrayList<>();
		Bd0808Entity advPayPerform = null;
		Bd0808Entity accPayPerform = null;


		List<Bd0808Entity> advPayBal = getAdvPay_Bal(req);
		List<Bd0808Entity> advPayInc = getAdvPay_Inc(req);
		List<Bd0808Entity> advPayDec = getAdvPay_Dec(req);
		List<Bd0808Entity> advPayPrvBal = getAdvPay_Prev_Bal(req);
		advPayPerform = makePerform(advPayBal,advPayInc,advPayDec,advPayPrvBal);


		List<Bd0808Entity> accPayBal = getAccPay_Bal(req);
		List<Bd0808Entity> accPayInc = getAccPay_Inc(req);
		List<Bd0808Entity> accPayDec = getAccPay_Dec(req);
		List<Bd0808Entity> accPayPrvBal = getAccPay_Prev_Bal(req);
		accPayPerform = makePerform(accPayBal,accPayInc,accPayDec,accPayPrvBal);

		for(int i = 0; i < 12; i++) {
			bsTotal_1.add(new BigDecimal(0));
			bsTotal_2.add(new BigDecimal(0));
			bsTotal_3.add(new BigDecimal(0));
		}
		bsTotal_1.set(0, bsTotal_1.get(0).add(advPayPerform.bgtAmt03));
		bsTotal_1.set(0, bsTotal_1.get(0).add(accPayPerform.bgtAmt03));
		bsTotal_1.set(1, bsTotal_1.get(1).add(advPayPerform.bgtAmt04));
		bsTotal_1.set(1, bsTotal_1.get(1).add(accPayPerform.bgtAmt04));
		bsTotal_1.set(2, bsTotal_1.get(2).add(advPayPerform.bgtAmt05));
		bsTotal_1.set(2, bsTotal_1.get(2).add(accPayPerform.bgtAmt05));
		bsTotal_1.set(3, bsTotal_1.get(3).add(advPayPerform.bgtAmt06));
		bsTotal_1.set(3, bsTotal_1.get(3).add(accPayPerform.bgtAmt06));
		bsTotal_1.set(4, bsTotal_1.get(4).add(advPayPerform.bgtAmt07));
		bsTotal_1.set(4, bsTotal_1.get(4).add(accPayPerform.bgtAmt07));
		bsTotal_1.set(5, bsTotal_1.get(5).add(advPayPerform.bgtAmt08));
		bsTotal_1.set(5, bsTotal_1.get(5).add(accPayPerform.bgtAmt08));
		bsTotal_1.set(6, bsTotal_1.get(6).add(advPayPerform.bgtAmt09));
		bsTotal_1.set(6, bsTotal_1.get(6).add(accPayPerform.bgtAmt09));
		bsTotal_1.set(7, bsTotal_1.get(7).add(advPayPerform.bgtAmt10));
		bsTotal_1.set(7, bsTotal_1.get(7).add(accPayPerform.bgtAmt10));
		bsTotal_1.set(8, bsTotal_1.get(8).add(advPayPerform.bgtAmt11));
		bsTotal_1.set(8, bsTotal_1.get(8).add(accPayPerform.bgtAmt11));
		bsTotal_1.set(9, bsTotal_1.get(9).add(advPayPerform.bgtAmt12));
		bsTotal_1.set(9, bsTotal_1.get(9).add(accPayPerform.bgtAmt12));
		bsTotal_1.set(10, bsTotal_1.get(10).add(advPayPerform.bgtAmt01));
		bsTotal_1.set(10, bsTotal_1.get(10).add(accPayPerform.bgtAmt01));
		bsTotal_1.set(11, bsTotal_1.get(11).add(advPayPerform.bgtAmt02));
		bsTotal_1.set(11, bsTotal_1.get(11).add(accPayPerform.bgtAmt02));

		bsTotal_3.set(0, bsTotal_1.get(0).add(bsTotal_2.get(0)));
		bsTotal_3.set(1, bsTotal_1.get(1).add(bsTotal_2.get(1)));
		bsTotal_3.set(2, bsTotal_1.get(2).add(bsTotal_2.get(2)));
		bsTotal_3.set(3, bsTotal_1.get(3).add(bsTotal_2.get(3)));
		bsTotal_3.set(4, bsTotal_1.get(4).add(bsTotal_2.get(4)));
		bsTotal_3.set(5, bsTotal_1.get(5).add(bsTotal_2.get(5)));
		bsTotal_3.set(6, bsTotal_1.get(6).add(bsTotal_2.get(6)));
		bsTotal_3.set(7, bsTotal_1.get(7).add(bsTotal_2.get(7)));
		bsTotal_3.set(8, bsTotal_1.get(8).add(bsTotal_2.get(8)));
		bsTotal_3.set(9, bsTotal_1.get(9).add(bsTotal_2.get(9)));
		bsTotal_3.set(10, bsTotal_1.get(10).add(bsTotal_2.get(10)));
		bsTotal_3.set(11, bsTotal_1.get(11).add(bsTotal_2.get(11)));

		for (Bd0808Entity budget : budgets) {
			// 実績
			Bd0808Entity perform = performanceMap.get(budget.bgtItmCd);
			perform.totalLineFlag = "0";

			List<Object> bgtItmCdList = null;

			//変動費計
			if("X0060".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList( "X0061", "X0062");
				perform.totalLineFlag = "1";
			}
			//人権費計
			if("X0100".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList( "X0101", "X0102");
				perform.totalLineFlag = "1";
			}
			//広告宣伝費計
			if("X0110".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList( "X0111", "X0112");
				perform.totalLineFlag = "1";
			}
			//その他販管費計
			if("X0130".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList( "X0121", "X0122", "X0123", "X0124", "X0125", "X0126", "X0139", "X0138");
				perform.totalLineFlag = "1";
			}
			//固定販管費計
			if("X0140".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList("X0101", "X0102", "X0111", "X0112", "X0121", "X0122", "X0123", "X0124", "X0125", "X0126", "X0139", "X0138");
				perform.totalLineFlag = "1";
			}
			//償却費等計
			if("X0160".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList( "X0161", "X0162", "X0163");
				perform.totalLineFlag = "1";
			}
			//費用合計
			if("X9999".equals(budget.bgtItmCd)) {
				bgtItmCdList = asList("ALL");
				perform.totalLineFlag = "1";
			}

			if (bgtItmCdList != null) {
				List<Bd0808Entity> ttlPerform = getResult_ttl(req, bgtItmCdList);
				if(ttlPerform != null && perform != null) {
					perform.bgtAmt01 = ttlPerform.get(0).bgtAmt01;
					perform.bgtAmt02 = ttlPerform.get(0).bgtAmt02;
					perform.bgtAmt03 = ttlPerform.get(0).bgtAmt03;
					perform.bgtAmt04 = ttlPerform.get(0).bgtAmt04;
					perform.bgtAmt05 = ttlPerform.get(0).bgtAmt05;
					perform.bgtAmt06 = ttlPerform.get(0).bgtAmt06;
					perform.bgtAmt07 = ttlPerform.get(0).bgtAmt07;
					perform.bgtAmt08 = ttlPerform.get(0).bgtAmt08;
					perform.bgtAmt09 = ttlPerform.get(0).bgtAmt09;
					perform.bgtAmt10 = ttlPerform.get(0).bgtAmt10;
					perform.bgtAmt11 = ttlPerform.get(0).bgtAmt11;
					perform.bgtAmt12 = ttlPerform.get(0).bgtAmt12;
				}
			}

			// 前払金残高
			if("B1000".equals(budget.bgtItmCd)) {
				if(advPayPerform != null && perform != null) {
					perform.bgtAmt03 = advPayPerform.bgtAmt03;
					perform.bgtAmt04 = advPayPerform.bgtAmt04;
					perform.bgtAmt05 = advPayPerform.bgtAmt05;
					perform.bgtAmt06 = advPayPerform.bgtAmt06;
					perform.bgtAmt07 = advPayPerform.bgtAmt07;
					perform.bgtAmt08 = advPayPerform.bgtAmt08;
					perform.bgtAmt09 = advPayPerform.bgtAmt09;
					perform.bgtAmt10 = advPayPerform.bgtAmt10;
					perform.bgtAmt11 = advPayPerform.bgtAmt11;
					perform.bgtAmt12 = advPayPerform.bgtAmt12;
					perform.bgtAmt01 = advPayPerform.bgtAmt01;
					perform.bgtAmt02 = advPayPerform.bgtAmt02;
				}
			}

			// 未払・買掛・買掛仮の残高
			if("B1030".equals(budget.bgtItmCd)) {
				if(accPayPerform != null && perform != null) {
					perform.bgtAmt03 = accPayPerform.bgtAmt03;
					perform.bgtAmt04 = accPayPerform.bgtAmt04;
					perform.bgtAmt05 = accPayPerform.bgtAmt05;
					perform.bgtAmt06 = accPayPerform.bgtAmt06;
					perform.bgtAmt07 = accPayPerform.bgtAmt07;
					perform.bgtAmt08 = accPayPerform.bgtAmt08;
					perform.bgtAmt09 = accPayPerform.bgtAmt09;
					perform.bgtAmt10 = accPayPerform.bgtAmt10;
					perform.bgtAmt11 = accPayPerform.bgtAmt11;
					perform.bgtAmt12 = accPayPerform.bgtAmt12;
					perform.bgtAmt01 = accPayPerform.bgtAmt01;
					perform.bgtAmt02 = accPayPerform.bgtAmt02;
				}
			}

			if("B9997".equals(budget.bgtItmCd)) {
				perform.bgtAmt03 = bsTotal_1.get(0);
				perform.bgtAmt04 = bsTotal_1.get(1);
				perform.bgtAmt05 = bsTotal_1.get(2);
				perform.bgtAmt06 = bsTotal_1.get(3);
				perform.bgtAmt07 = bsTotal_1.get(4);
				perform.bgtAmt08 = bsTotal_1.get(5);
				perform.bgtAmt09 = bsTotal_1.get(6);
				perform.bgtAmt10 = bsTotal_1.get(7);
				perform.bgtAmt11 = bsTotal_1.get(8);
				perform.bgtAmt12 = bsTotal_1.get(9);
				perform.bgtAmt01 = bsTotal_1.get(10);
				perform.bgtAmt02 = bsTotal_1.get(11);
				perform.totalLineFlag = "1";
			}
			if("B9998".equals(budget.bgtItmCd)) {
				perform.bgtAmt03 = bsTotal_2.get(0);
				perform.bgtAmt04 = bsTotal_2.get(1);
				perform.bgtAmt05 = bsTotal_2.get(2);
				perform.bgtAmt06 = bsTotal_2.get(3);
				perform.bgtAmt07 = bsTotal_2.get(4);
				perform.bgtAmt08 = bsTotal_2.get(5);
				perform.bgtAmt09 = bsTotal_2.get(6);
				perform.bgtAmt10 = bsTotal_2.get(7);
				perform.bgtAmt11 = bsTotal_2.get(8);
				perform.bgtAmt12 = bsTotal_2.get(9);
				perform.bgtAmt01 = bsTotal_2.get(10);
				perform.bgtAmt02 = bsTotal_2.get(11);
				perform.totalLineFlag = "1";
			}
			if("B9999".equals(budget.bgtItmCd)) {
				perform.bgtAmt03 = bsTotal_3.get(0);
				perform.bgtAmt04 = bsTotal_3.get(1);
				perform.bgtAmt05 = bsTotal_3.get(2);
				perform.bgtAmt06 = bsTotal_3.get(3);
				perform.bgtAmt07 = bsTotal_3.get(4);
				perform.bgtAmt08 = bsTotal_3.get(5);
				perform.bgtAmt09 = bsTotal_3.get(6);
				perform.bgtAmt10 = bsTotal_3.get(7);
				perform.bgtAmt11 = bsTotal_3.get(8);
				perform.bgtAmt12 = bsTotal_3.get(9);
				perform.bgtAmt01 = bsTotal_3.get(10);
				perform.bgtAmt02 = bsTotal_3.get(11);
				perform.totalLineFlag = "1";
			}

			// 予算と実績(COM, DCM)から、最終的な検索結果を生成
			Bd0808Result r = new Bd0808Result(budget, perform);
			results.add(r);
		}
		return results;
	}

	/** 実績 */
	private List<Bd0808Entity> getPerformance(Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd, req.rcvCostPayTp);

		String sql = "";
		// 実績(COM)
		if (eq(RcvCostPayTp.COST, req.rcvCostPayTp)) {
			//費用実績
			sql = getSql("BD0808_09");
		} else {
			//支払実績
			sql = getSql("BD0808_05");
		}
		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		if (isNotEmpty(req.bgtItmCd)) {
			replaceStr = replaceStr + " and BGT_ITM_CD = ?";
			params.add(req.bgtItmCd);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);

		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 実績合計行 */
	private List<Bd0808Entity> getResult_ttl( Bd0808SearchRequest req, List<Object>bgtItmCdList) {
		List<Object> params = asList(req.companyCd, req.yrCd, req.rcvCostPayTp);
		String sql = "";

		if("1".equals(req.rcvCostPayTp)) {
			sql = getSql("BD0808_12");
		} else {
			sql = getSql("BD0808_14");
		}

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}

		String whereBgtItmCd = "";
		if("ALL".equals(bgtItmCdList.get(0))) {
			whereBgtItmCd = " and  BGT_ITM_CD IN (SELECT BGT_ITM_CD FROM BGT_ITM_MST WHERE COMPANY_CD = ? AND DLT_FG = '0' AND BS_PL_TP = '2')";
			params.add(req.companyCd);
		} else {
			whereBgtItmCd = " and  BGT_ITM_CD IN (";
			for (Object bgtItmCd : bgtItmCdList) {
				whereBgtItmCd = whereBgtItmCd + "?,";
				params.add(bgtItmCd);
			}
			whereBgtItmCd = whereBgtItmCd + "'')";
		}

		replaceStr = replaceStr + whereBgtItmCd;
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 前払増加 */
	private List<Bd0808Entity> getAdvPay_Inc( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_15");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 前払減少 */
	private List<Bd0808Entity> getAdvPay_Dec( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_16");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 前払残高 */
	private List<Bd0808Entity> getAdvPay_Bal( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_17");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 前月前払残高 */
	private List<Bd0808Entity> getAdvPay_Prev_Bal( Bd0808SearchRequest req) {
		String cstAddYm = req.yrCd + "02";
		List<Object> params = asList(req.companyCd, req.companyCd, cstAddYm,req.companyCd, req.companyCd, cstAddYm, cstAddYm,req.companyCd, req.companyCd, cstAddYm, cstAddYm );

		String sql = "";
		sql = getSql("BD0808_18");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}


	/** 未払・買掛・買掛仮の増加 */
	private List<Bd0808Entity> getAccPay_Inc( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_19");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 未払・買掛・買掛仮の減少 */
	private List<Bd0808Entity> getAccPay_Dec( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_20");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 未払・買掛・買掛仮の残高 */
	private List<Bd0808Entity> getAccPay_Bal( Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd);
		String sql = "";
		sql = getSql("BD0808_21");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 未払・買掛・買掛仮の前月残高 */
	private List<Bd0808Entity> getAccPay_Prev_Bal( Bd0808SearchRequest req) {
		String cstAddYm = req.yrCd + "02";
		List<Object> params = asList(req.companyCd, req.companyCd, cstAddYm,req.companyCd, req.companyCd, cstAddYm, cstAddYm,req.companyCd, req.companyCd, cstAddYm, cstAddYm );

		String sql = "";
		sql = getSql("BD0808_22");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 予算計画を抽出 */
	private List<Bd0808Entity> getBudgets(Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd, req.rcvCostPayTp);
		String sql = getSql("BD0808_02");

		String replaceStr = "";
		if(isEmpty(req.organizationCodeLv3)) {
			replaceStr = replaceStr + " and ORGANIZATION_CODE_UP = ?";
			params.add(req.organizationCodeLv2);
		} else {
			replaceStr = replaceStr + " and ORGANIZATION_CODE = ?";
			params.add(req.organizationCodeLv3);
		}
		if (isNotEmpty(req.bgtItmCd)) {
			replaceStr = replaceStr + " and BGT_ITM_CD = ?";
			params.add(req.bgtItmCd);
		}
		sql = sql.replaceFirst(REPLACE, replaceStr);

		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 予算計画履歴を抽出 */
	private List<Bd0808Entity> getBudgetVersions(Bd0808SearchRequest req) {
		List<Object> params = asList(req.companyCd, req.yrCd, req.rcvCostPayTp, req.organizationCodeLv3, req.hstVersion);
		String sql = getSql("BD0808_08");
		if (isNotEmpty(req.bgtItmCd)) {
			sql = sql.replaceFirst(REPLACE, " and BGT_ITM_CD = ?");
			params.add(req.bgtItmCd);
		}
		else {
			sql = sql.replaceFirst(REPLACE, "");
		}
		List<Bd0808Entity> list = select(Bd0808Entity.class, sql, params.toArray());
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 予算科目の選択肢を生成 */
	public List<OptionItem> getBgtItmCds(String companyCd) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(new OptionItem("", "*** すべて ***"));
		final Object[] params = { companyCd };
		final String sql = getSql("BD0808_07");
		items.addAll(select(BgtItmMst.class, sql, params).stream()
				.map(e -> new OptionItem(e.getId().getBgtItmCd(), e.getBgtItmNm()))
				.collect(Collectors.toList()));
		return items;
	}

	/** 予算科目の選択肢を生成 */
	public List<OptionItem> getBgtItmCdsBsPl(Bd0808SearchRequest req) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(new OptionItem("", "*** すべて ***"));

		final Object[] params = { req.companyCd};
		final String sql = getSql("BD0808_11");
		items.addAll(select(BgtItmMst.class, sql, params).stream()
				.map(e -> new OptionItem(e.getId().getBgtItmCd(), e.getBgtItmNm()))
				.collect(Collectors.toList()));
		return items;
	}
	/** 操作者の所属している組織の組織階層ビューを抽出 */
	public List<VOrganizationLevel> getMyOrganizationLevel() {
		LoginInfo login = sessionHolder.getLoginInfo();
		final List<Object> params = asList(login.getCorporationCode());
		params.add(login.getOrganizationCode());

		final String sql = getSql("BD0801_04")
				.replaceFirst(REPLACE, toInListSql(" and ORGANIZATION_CODE", login.getOrganizationCodes().size()));
		return select(VOrganizationLevel.class, sql, params.toArray());
	}

	private Bd0808Entity makePerform(List<Bd0808Entity> balAmt, List<Bd0808Entity> incAmt, List<Bd0808Entity> decAmt, List<Bd0808Entity> prvBalAmt) {
		Bd0808Entity perform = new Bd0808Entity();

		perform.bgtAmt03 = balAmt.get(0).bgtAmt03;
		if(balAmt.get(0).bgtAmt03 == null || balAmt.get(0).bgtAmt03.compareTo(new BigDecimal(0)) == 0){
			if(prvBalAmt != null &&  !prvBalAmt.isEmpty() && prvBalAmt.get(0).bgtAmt02 != null) {
				perform.bgtAmt03 = prvBalAmt.get(0).bgtAmt02;
			}
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt03 != null) {
				perform.bgtAmt03 = perform.bgtAmt03.add(incAmt.get(0).bgtAmt03);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt03 != null) {
				perform.bgtAmt03 = perform.bgtAmt03.subtract(decAmt.get(0).bgtAmt03);
			}
		}

		perform.bgtAmt04 = balAmt.get(0).bgtAmt04;
		if(balAmt.get(0).bgtAmt04 == null || balAmt.get(0).bgtAmt04.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt04 = perform.bgtAmt03;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt04 != null) {
				perform.bgtAmt04 = perform.bgtAmt04.add(incAmt.get(0).bgtAmt04);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt04 != null) {
				perform.bgtAmt04 = perform.bgtAmt04.subtract(decAmt.get(0).bgtAmt04);
			}
		}

		perform.bgtAmt05 = balAmt.get(0).bgtAmt05;
		if(balAmt.get(0).bgtAmt05 == null || balAmt.get(0).bgtAmt05.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt05 = perform.bgtAmt04;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt05 != null) {
				perform.bgtAmt05 = perform.bgtAmt05.add(incAmt.get(0).bgtAmt05);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt05 != null) {
				perform.bgtAmt05 = perform.bgtAmt05.subtract(decAmt.get(0).bgtAmt05);
			}
		}

		perform.bgtAmt06 = balAmt.get(0).bgtAmt06;
		if(balAmt.get(0).bgtAmt06 == null || balAmt.get(0).bgtAmt06.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt06 = perform.bgtAmt05;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt06 != null) {
				perform.bgtAmt06 = perform.bgtAmt06.add(incAmt.get(0).bgtAmt06);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt06 != null) {
				perform.bgtAmt06 = perform.bgtAmt06.subtract(decAmt.get(0).bgtAmt06);
			}
		}

		perform.bgtAmt07 = balAmt.get(0).bgtAmt07;
		if(balAmt.get(0).bgtAmt07 == null || balAmt.get(0).bgtAmt07.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt07 = perform.bgtAmt06;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt07 != null) {
				perform.bgtAmt07 = perform.bgtAmt07.add(incAmt.get(0).bgtAmt07);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt07 != null) {
				perform.bgtAmt07 = perform.bgtAmt07.subtract(decAmt.get(0).bgtAmt07);
			}
		}

		perform.bgtAmt08 = balAmt.get(0).bgtAmt08;
		if(balAmt.get(0).bgtAmt08 == null || balAmt.get(0).bgtAmt08.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt08 = perform.bgtAmt07;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt08 != null) {
				perform.bgtAmt08 = perform.bgtAmt08.add(incAmt.get(0).bgtAmt08);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt08 != null) {
				perform.bgtAmt08 = perform.bgtAmt08.subtract(decAmt.get(0).bgtAmt08);
			}
		}

		perform.bgtAmt09 = balAmt.get(0).bgtAmt09;
		if(balAmt.get(0).bgtAmt09 == null || balAmt.get(0).bgtAmt09.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt09 = perform.bgtAmt08;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt09 != null) {
				perform.bgtAmt09 = perform.bgtAmt09.add(incAmt.get(0).bgtAmt09);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt09 != null) {
				perform.bgtAmt09 = perform.bgtAmt09.subtract(decAmt.get(0).bgtAmt09);
			}
		}

		perform.bgtAmt10 = balAmt.get(0).bgtAmt10;
		if(balAmt.get(0).bgtAmt10 == null || balAmt.get(0).bgtAmt10.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt10 = perform.bgtAmt09;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt10 != null) {
				perform.bgtAmt10 = perform.bgtAmt10.add(incAmt.get(0).bgtAmt10);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt10 != null) {
				perform.bgtAmt10 = perform.bgtAmt10.subtract(decAmt.get(0).bgtAmt10);
			}
		}

		perform.bgtAmt11 = balAmt.get(0).bgtAmt11;
		if(balAmt.get(0).bgtAmt11 == null || balAmt.get(0).bgtAmt11.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt11 = perform.bgtAmt10;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt11 != null) {
				perform.bgtAmt11 = perform.bgtAmt11.add(incAmt.get(0).bgtAmt11);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt11 != null) {
				perform.bgtAmt11 = perform.bgtAmt11.subtract(decAmt.get(0).bgtAmt11);
			}
		}

		perform.bgtAmt12 = balAmt.get(0).bgtAmt12;
		if(balAmt.get(0).bgtAmt12 == null || balAmt.get(0).bgtAmt12.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt12 = perform.bgtAmt11;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt12 != null) {
				perform.bgtAmt12 = perform.bgtAmt12.add(incAmt.get(0).bgtAmt12);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt12 != null) {
				perform.bgtAmt12 = perform.bgtAmt12.subtract(decAmt.get(0).bgtAmt12);
			}
		}

		perform.bgtAmt01 = balAmt.get(0).bgtAmt01;
		if(balAmt.get(0).bgtAmt01 == null || balAmt.get(0).bgtAmt01.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt01 = perform.bgtAmt12;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt01 != null) {
				perform.bgtAmt01 = perform.bgtAmt01.add(incAmt.get(0).bgtAmt01);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt01 != null) {
				perform.bgtAmt01 = perform.bgtAmt01.subtract(decAmt.get(0).bgtAmt01);
			}
		}

		perform.bgtAmt02 = balAmt.get(0).bgtAmt02;
		if(balAmt.get(0).bgtAmt02 == null || balAmt.get(0).bgtAmt02.compareTo(new BigDecimal(0)) == 0){
			perform.bgtAmt02 = perform.bgtAmt01;
			if(incAmt != null && !incAmt.isEmpty() && incAmt.get(0).bgtAmt02 != null) {
				perform.bgtAmt02 = perform.bgtAmt02.add(incAmt.get(0).bgtAmt02);
			}
			if(decAmt != null && !decAmt.isEmpty() && decAmt.get(0).bgtAmt02 != null) {
				perform.bgtAmt02 = perform.bgtAmt02.subtract(decAmt.get(0).bgtAmt02);
			}
		}

		return(perform);
	}
}
