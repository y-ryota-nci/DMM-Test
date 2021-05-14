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
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 支払申請の実績確認時に呼び出されるコールバックファンクション。
 */
public class CallbackFunctionExecute_PAY_INF_COMPLETE extends BaseCallbackFunction {

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

		// 支払完了処理
		callablePaymentComplete(in, companyCd, payNo);

	}

	/** 支払完了処理 */
	private void callablePaymentComplete(MoveActivityInstanceInParam in, String companyCd, String payNo) {
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		execCallable(getSql("PY0000_18"), new Object[] {companyCd, payNo, corporationCode, userCode, ipAddr});
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
