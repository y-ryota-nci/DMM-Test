package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;

import jp.co.dmm.customize.endpoint.po.PurordInfService;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInfPK;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 検収の承認後に呼び出されるコールバックファンクション。
 * WF検収テーブル(MWT_RCVINSP)からDMMの検収情報テーブル(RCVINSP_INF)へデータを吸い出す。
 * 【検収変更は対象ではない】
 */
public class CallbackFunctionCreate_RCVINSP_INF extends BaseCallbackFunction {
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// INSERT行数
		int count = 0;
		final String companyCd = ctx.runtimeMap.get("TXT0068").getValue();
		// 検収No
		String rcvinspNo = ctx.runtimeMap.get("NMB0047").getValue();

		// MWT_RCVINSPからRCVINSP_INFへ転写
		Long runtimeId = root.rows.get(0).runtimeId;
		count = insertSelectRCVINSP(in, runtimeId);
		if (count == 0) {
			throw new InternalServerErrorException("実インサート行数が0でした。");
		}

		// 検収明細は既存データと同定するのが困難なので、DELETE&INSERT
		deleteRcvinspDtl(companyCd, rcvinspNo);

		// MWT_RCVINSP_DETAILからRCVINSPDTL_INFへ転写
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0059");	// 検収明細（円貨）のリピーター
		Set<RcvinspdtlInfPK> rcvKeys = new HashSet<>();
		BigDecimal payadvAmt = BigDecimal.ZERO;		// 前払充当額(合計)
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			long rcvinspDtlNo = row.rowId;

			// 前払充当額を合算
			String s = ctx.runtimeMap.get(prefix + "TXT0084").getValue();
			if (isNotEmpty(s)) {
				payadvAmt = payadvAmt.add(toBigDecimal(s));
			}

			// 費目が設定済みなら入力済み行として検収明細情報を作成
			String itmexpsCd1 = ctx.runtimeMap.get(prefix + "TXT0041").getValue();
			if (isNotEmpty(itmexpsCd1)) {
				count = insertSelectRCVINSPDTL(in, row.runtimeId, rcvinspNo, row.rowId);
				if (count == 0) {
					throw new InternalServerErrorException("実インサート行数が0でした。");
				}

				// 発注ステータス更新用に検収明細のキーを記録
				final RcvinspdtlInfPK key = new RcvinspdtlInfPK();
				key.setCompanyCd(companyCd);
				key.setRcvinspNo(rcvinspNo);
				key.setRcvinspDtlNo(rcvinspDtlNo);
				rcvKeys.add(key);
			}
		}

		// 支払金額が0円なら、検収ステータスを「支払済」にする。源泉徴収額は考慮不要とのこと。
		// 支払金額＝検収金額(税込)－前払充当額
		final BigDecimal rcvinspAmt = toBigDecimal(ctx.runtimeMap.get("TXT0112").getValue());	// 検収金額(税込)
		final BigDecimal payAmt = rcvinspAmt.subtract(payadvAmt);
		if (BigDecimal.ZERO.compareTo(payAmt) == 0) {
			updateRcvinspSts(companyCd, rcvinspNo);
		}

		// 発注情報.発注ステータスを検収済に更新
		final PurordInfService poService = get(PurordInfService.class);
		poService.updatePurordSts(rcvKeys);

		// 検収計上／検収訂正処理
		String newFlag = ctx.runtimeMap.get("TXT0134").getValue();	// 新規／変更区分
		callableReceiveInspection(in, companyCd, rcvinspNo, newFlag);
	}

	/** 検収情報を支払済へ更新 */
	private void updateRcvinspSts(String companyCd, String rcvinspNo) {
		final Object[] params = { companyCd, rcvinspNo };
		final String sql = getSql("RI0000_17");
		execSql(sql, params);
	}

	/** 検収明細情報を削除 */
	private void deleteRcvinspDtl(String companyCd, String rcvinspNo) {
		final Object[] params = { companyCd, rcvinspNo };
		execSql(getSql("RI0000_10"), params);
	}

	/** 検収明細情報テーブルをINSERT
	 * @param purordAmtRemain
	 * @param purordAmt */
	private int insertSelectRCVINSPDTL(
			MoveActivityInstanceInParam in,
			Long runtimeId,
			String rcvinspNo,
			int dtlNo
	) {
		final String sql = getSql("RI0000_02");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				rcvinspNo, dtlNo,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 検収情報テーブルをINSERT */
	private int insertSelectRCVINSP(MoveActivityInstanceInParam in, Long runtimeId) {
		final String sql = getSql("RI0000_01");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 検収計上／検収訂正処理 */
	private void callableReceiveInspection(MoveActivityInstanceInParam in, String companyCd, String rcvinspNo, String newFlag) {
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		if ("CHANGE".equals(newFlag))
			// 変更＿検収申請→検収訂正処理
			execCallable(getSql("RI0000_16"), new Object[] {companyCd, rcvinspNo, corporationCode, userCode, ipAddr});
		else
			// 新規＿検収申請→検収計上処理
			execCallable(getSql("RI0000_09"), new Object[] {companyCd, rcvinspNo, corporationCode, userCode, ipAddr});
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

	private static BigDecimal toBigDecimal(String s) {
		if (isEmpty(s)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(s);
	}
}
