package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.SendBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 支払依頼の実績確認差戻し時に呼び出されるコールバックファンクション。
 * DMMの支払依頼情報テーブル(PAY_INF)のデータを削除する
 */
public class CallbackFunctionDelete_PAY_INF extends BaseCallbackFunction {

	@Override
	public void execute(
			InParamCallbackBase param
			, OutParamCallbackBase result
			, String actionType
			, Vd0310Contents contents
			, RuntimeContext ctx
			, WfvFunctionDef functionDef) {

		final SendBackActivityInstanceInParam in = (SendBackActivityInstanceInParam) param;

		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();

		// 支払取消処理
		callablePaymentCancel(in, companyCd, payNo);

		// 支払情報登録処理
		deletePayInf(companyCd, payNo);

		// 支払明細情報登録処理
		deletePaydtlInf(companyCd, payNo);

		// 前払金情報登録処理
		final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();
		if (CommonFlag.ON.equals(advpayFg)) {
			final String advpayNo = ctx.runtimeMap.get("NMB0157").getValue();
			deleteAdvpayInf(companyCd, advpayNo);
		}

	}

	/** 支払依頼情報テーブルをINSERT */
	private int deletePayInf(String companyCd, String payNo) {
		return execSql(getSql("PY0000_01"), new Object[] {companyCd, payNo});
	}

	/** 支払依頼明細情報テーブルをINSERT */
	private int deletePaydtlInf(String companyCd, String payNo) {
		return execSql(getSql("PY0000_04"), new Object[] {companyCd, payNo});
	}

	/** 前払金情報テーブルをINSERT */
	private int deleteAdvpayInf(String companyCd, String advpayNo) {
		return execSql(getSql("PY0000_06"), new Object[] {companyCd, advpayNo});

	}

	/** 支払取消処理 */
	private void callablePaymentCancel(SendBackActivityInstanceInParam in, String companyCd, String payNo) {
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		execCallable(getSql("PY0000_12"), new Object[] {companyCd, payNo, corporationCode, userCode, ipAddr});
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

}
