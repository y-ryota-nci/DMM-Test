package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.component.DmmCodeBook.PurordTp;
import jp.co.dmm.customize.endpoint.po.PurordInfService;
import jp.co.dmm.customize.endpoint.py.PayInfService;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInf;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PayInf;
import jp.co.dmm.customize.jpa.entity.mw.PayInfPK;
import jp.co.dmm.customize.jpa.entity.mw.PaydtlInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInfPK;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 支払依頼の決裁時に呼び出されるコールバックファンクション。
 * WF支払依頼テーブル(MWT_PAY)からDMMの支払依頼情報テーブル(PAY_INF)へデータを作成する
 * ※新規、変更どちらからも呼ばれる(経費・前払も同様)
 */
public class CallbackFunctionCreate_PAY_INF extends BaseCallbackFunction {

	private static final String REPLACE_PARENT = quotePattern("${REPLACE_PARENT}");
	private static final String REPLACE_CHILD = quotePattern("${REPLACE_CHILD}");

	@Override
	public void execute(
			InParamCallbackBase param
			, OutParamCallbackBase result
			, String actionType
			, Vd0310Contents contents
			, RuntimeContext ctx
			, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam) param;

		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();

		// 支払情報登録処理
		insertSelectPayInf(in, ctx);

		// 支払明細情報登録処理
		final Set<RcvinspdtlInfPK> rcvKeys = insertSelectPaydtlInf(in, ctx);

		// 前払金情報登録処理
		insertSelectAdvpayInf(in, ctx);

		// 検収ステータス更新処理
		updateSts_payFixed(rcvKeys);

		// 新規・変更
		final String newFg = ctx.runtimeMap.get("TXT0113").getValue();
		if ("CHANGE".equals(newFg)) {
			// 支払訂正処理
			callablePaymentRevise(in, companyCd, payNo);
		} else {
			// 支払計上処理
			callablePaymentAdd(in, companyCd, payNo);
		}

	}

	/** 支払依頼情報テーブルをINSERT */
	private int insertSelectPayInf(MoveActivityInstanceInParam in, RuntimeContext ctx) {
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();

		// 更新前情報を取得
		final PayInf result = getPayInf(companyCd, payNo);
		if (isNotEmpty(result)) {
			execSql(getSql("PY0000_01"), new Object[] {companyCd, payNo});
		}

		// 登録処理
		final String parentTableName = ctx.root.tableName;
		final String sql = getSql("PY0000_02").replaceAll(REPLACE_PARENT, parentTableName);

		final String payhysNoRRcnt = isEmpty(result) ? null : result.getPayhysNoRRcnt();
		final String payhysNoBRcnt = isEmpty(result) ? null : result.getPayhysNoBRcnt();

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final String corporationCodeCreated = isEmpty(result) ? corporationCode : result.getCorporationCodeCreated();
		final String userCodeCreated = isEmpty(result) ? userCode : result.getUserCodeCreated();
		final String ipCreated = isEmpty(result) ? ipAddr : result.getIpCreated();
		final Timestamp timestampCreated = isEmpty(result) ? now : result.getTimestampCreated();

		final Long runtimeId = root.rows.get(0).runtimeId;
		final Object[] args = {
				payhysNoRRcnt, payhysNoBRcnt
				, corporationCodeCreated, userCodeCreated, ipCreated, timestampCreated
				, corporationCode, userCode, ipAddr, now
				, runtimeId
		};
		return execSql(sql, args);
	}

	private PayInf getPayInf(String companyCd, String payNo) {
		final EntityManager em = get(EntityManager.class);
		final PayInfPK pk = new PayInfPK();
		pk.setCompanyCd(companyCd);
		pk.setPayNo(payNo);
		return em.find(PayInf.class, pk);
	}

	/** 支払依頼明細情報テーブルをINSERT */
	private Set<RcvinspdtlInfPK> insertSelectPaydtlInf(MoveActivityInstanceInParam in, RuntimeContext ctx) {
		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();

		// 更新前情報を取得
		final List<PaydtlInf> results = getPaydtlInfs(companyCd, payNo);
		if (isNotEmpty(results)) {
			execSql(getSql("PY0000_04"), new Object[] {companyCd, payNo});
		}
		// 支払明細Noをキーとした支払明細情報Map
		final Map<Long, PaydtlInf> currents = results
				.stream()
				.collect(Collectors.toMap(e -> e.getId().getPayDtlNo(), e -> e));

		final String parentTableName = ctx.root.tableName;
		final PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0080");
		final String childTableName = ((PartsDesignGrid)ctx.designMap.get(grid.partsId)).tableName;
		final String sql = getSql("PY0000_05").replaceAll(REPLACE_PARENT, parentTableName).replaceAll(REPLACE_CHILD, childTableName);

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();
		final String purordTp = ctx.runtimeMap.get("TXT0147").getValue();

		Set<RcvinspdtlInfPK> doubleCheck = new HashSet<>();
		for (PartsContainerRow row : grid.rows) {
			final PaydtlInf result = isEmpty(currents.get(new Long(row.rowId))) ? null : currents.get(new Long(row.rowId));
			final String corporationCodeCreated = isEmpty(result) ? corporationCode : result.getCorporationCodeCreated();
			final String userCodeCreated = isEmpty(result) ? userCode : result.getUserCodeCreated();
			final String ipCreated = isEmpty(result) ? ipAddr : result.getIpCreated();
			final Timestamp timestampCreated = isEmpty(result) ? now : result.getTimestampCreated();
			final Long runtimeId = row.runtimeId;

			final Object[] args = {
					row.rowId
					, corporationCodeCreated, userCodeCreated, ipCreated, timestampCreated
					, corporationCode, userCode, ipAddr, now
					, runtimeId
			};
			int count = execSql(sql, args);

			// 検収情報のステータス対象レコードを設定
			if (count > 0 && !PurordTp.EXPENSE.equals(purordTp) && !CommonFlag.ON.equals(advpayFg)) {
				// 検収No
				String rcvinspNoId = row.children.stream()
						.filter(id -> id.endsWith("TXT0019"))
						.findFirst().orElseGet(null);
				String rcvinspNo = ctx.runtimeMap.get(rcvinspNoId).getValue();
				if (isEmpty(rcvinspNo)) {
					continue;
				}
				// 検収No
				String rcvinspDtlNoId = row.children.stream()
						.filter(id -> id.endsWith("TXT0020"))
						.findFirst().orElseGet(null);
				Long rcvinspDtlNo = Long.parseLong(ctx.runtimeMap.get(rcvinspDtlNoId).getValue());

				final RcvinspdtlInfPK rcvinspdtlInfPK = new RcvinspdtlInfPK();
				rcvinspdtlInfPK.setCompanyCd(companyCd);
				rcvinspdtlInfPK.setRcvinspNo(rcvinspNo);
				rcvinspdtlInfPK.setRcvinspDtlNo(rcvinspDtlNo);
				doubleCheck.add(rcvinspdtlInfPK);
			}
		}

		return doubleCheck;
	}

	@SuppressWarnings("unchecked")
	private List<PaydtlInf> getPaydtlInfs(String companyCd, String payNo) {
		final EntityManager em = get(EntityManager.class);
		final Query q = em.createNativeQuery(getSql("PY0000_03"), PaydtlInf.class);
		putParams(q, new Object[] {companyCd, payNo});
		return (List<PaydtlInf>)q.getResultList();
	}

	/**
	 * JPA経由でQueryへパラメータをセット
	 * @param q Query
	 * @param params パラメータList
	 */
	private void putParams(Query q, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				final Object value = params[i];
				q.setParameter(i + 1, value);
			}
		}
	}

	/** 前払金情報テーブルをINSERT */
	private int insertSelectAdvpayInf(MoveActivityInstanceInParam in, RuntimeContext ctx) {
		final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();
		if (!CommonFlag.ON.equals(advpayFg)) {
			return 0;
		}

		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();
		final String advpayNo = ctx.runtimeMap.get("NMB0157").getValue();

		// 更新前情報を取得
		final AdvpayInf result = getAdvpayInf(companyCd, advpayNo);
		if (isNotEmpty(result)) {
			execSql(getSql("PY0000_06"), new Object[] {companyCd, advpayNo});
		}
		return execSql(getSql("PY0000_07"), new Object[] {advpayNo, companyCd, payNo});

	}

	private AdvpayInf getAdvpayInf(String companyCd, String advpayNo) {
		final EntityManager em = get(EntityManager.class);
		final AdvpayInfPK pk = new AdvpayInfPK();
		pk.setCompanyCd(companyCd);
		pk.setAdvpayNo(advpayNo);;
		return em.find(AdvpayInf.class, pk);
	}

	/**
	 * 発注ステータス・検収ステータスを支払済に更新
	 * 条件：支払金額の合算が発注金額、検収金額を上回ったとき
	 * @param in
	 * @param companyCd
	 * @param rcvinspNo
	 */
	private void updateSts_payFixed(Set<RcvinspdtlInfPK> rcvKeys) {
		// 検収情報の検収ステータスを更新
		final PayInfService pyService = get(PayInfService.class);
		pyService.updateRcvinspSts(rcvKeys);

		// 発注情報の発注ステータスを更新
		final PurordInfService poService = get(PurordInfService.class);
		poService.updatePurordSts(rcvKeys);
	}

	/** 支払計上処理 */
	private void callablePaymentAdd(MoveActivityInstanceInParam in, String companyCd, String payNo) {
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		execCallable(getSql("PY0000_10"), new Object[] {companyCd, payNo, corporationCode, userCode, ipAddr});
	}

	/** 支払訂正処理 */
	private void callablePaymentRevise(MoveActivityInstanceInParam in, String companyCd, String payNo) {
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		execCallable(getSql("PY0000_11"), new Object[] {companyCd, payNo, corporationCode, userCode, ipAddr});
	}

	private void execCallable(final String sql, final Object[] params) {

		// JPAの管理するトランザクションに乗せるため、エンティティマネージャからConnectionを取得する
		EntityManager em = get(EntityManager.class);
		Connection conn = em.unwrap(Connection.class);	// これはJPA管理化のConnectionだから、勝手にクローズ禁止
		try (CallableStatement cstmt = conn.prepareCall(sql)) {
			// INパラメータの設定
			int i = 1;
			for (Object param: params) {
				cstmt.setObject(i++, param);
			}
			// OUTパラメータの設定
			int j = i;
			cstmt.registerOutParameter((j + 0), Types.NUMERIC);		//処理結果
			cstmt.registerOutParameter((j + 1), Types.VARCHAR);		//エラーメッセージ
			// プロシージャ実行
			NativeSqlUtils.debugSql(sql, params);
			cstmt.executeUpdate();
			// OUTパラメータの処理結果を取得
			final BigDecimal rtnCode = cstmt.getBigDecimal((j + 0));
			if (!eq(ReturnCode.SUCCESS, rtnCode)) {
				final String errMsg = cstmt.getString((j + 1));
				throw new InternalServerErrorException(errMsg);
			}
		} catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 正規表現のパターン文字列(検索文字列)をエスケープ  */
	private static String quotePattern(String s) {
		if (StringUtils.isEmpty(s)) return "";
		return Pattern.quote(s);
	}
}
