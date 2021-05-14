package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import jp.co.dmm.customize.component.DmmCodeBook.PrdPurordTp;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 発注の承認後に呼び出されるコールバックファンクション。
 * WF発注テーブル(MWT_PURCHASE_ORDER)からDMMの発注情報テーブル(PURORD_INF)へデータを吸い出す。
 * 【発注変更は対象ではない】
 */
public class CallbackFunctionCreate_ORD_INF extends BaseCallbackFunction {

	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {
		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 会社コード
		String companyCd = in.getCorporationCode();
		// 発注No
		String purordNo = ctx.runtimeMap.get("NMB0004").getValue();
		// 通貨
		String mnyCd = ctx.runtimeMap.get("MST0159").getValue();

		// 定期発注なら定期発注Noを採番
		final String prdPurordTp = ctx.runtimeMap.get("TXT0137").getValue();
		Long prdPurOrdNo = null;
		if (eq(prdPurordTp, PrdPurordTp.ROUTINE_ADS)) {
			prdPurOrdNo = get(NumberingService.class).next("PRD_PURORD_MST", "PRD_PURORD_NO");
		}

		// MWT_PURCHASE_ORDERからORD_INFへ転写
		final Long runtimeId = root.rows.get(0).runtimeId;
		int count = insertSelectORDINF(in, runtimeId, prdPurOrdNo);
		if (count == 0) {
			throw new InternalServerErrorException("実インサート行数が0でした。");
		}

		// ORDDTL_INFは既存データ同定するのが困難なので、DELETE&INSERTとする
		deletePurOrdDtl(companyCd, purordNo);

		// MWT_PURCHASE_ORDER_DETAILからORDDTL_INFへ転写
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0074");	// 発注明細グリッド

		int purordDtlNo = 0;
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.sortOrder + "_";

			// 発注金額
			BigDecimal purOrdAmt = null, purOrdAmtInctax = null;
			String price = ctx.runtimeMap.get(prefix + "TXT0017").getValue();
			String inctax = ctx.runtimeMap.get(prefix + "TXT0045").getValue();
			if (isNotEmpty(price)) {
				purOrdAmt = new BigDecimal(price);
				purOrdAmtInctax = new BigDecimal(inctax);
			}

			count = insertSelectORDDTL(in, row.runtimeId, purordNo, ++purordDtlNo, mnyCd, purOrdAmt, purOrdAmtInctax);
			if (count == 0)
				throw new InternalServerErrorException("実インサート行数が0でした。");
		}

		// 定期発注の追加処理
		if (eq(prdPurordTp, PrdPurordTp.ROUTINE_ADS)) {
			// 同一発注Noで生きている定期発注マスタは１つだけ。
			// よって既存の定期発注マスタを論理削除する
			deletePrdPurordInf(in, purordNo);

			// 定期発注マスタ作成
			insertSelect_PRD_PURORD_MST(in, prdPurOrdNo);

			// 定期発注予定マスタ
			insertSelect_PRD_PURORD_PLN_MST(in, prdPurOrdNo);

			// 採番した定期発注Noをトランザクションに書き戻し
			updatePrdPurOrdNo(in, prdPurOrdNo);
		}
	}

	/** 既存の定期発注マスタを論理削除 */
	private void deletePrdPurordInf(MoveActivityInstanceInParam in, String purordNo) {
		final String companyCd = in.getCorporationCode();
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] params = {
				corporationCode, userCode, ipAddr,
				companyCd, purordNo
		};
		final String sql = getSql("PO0000_09");
		execSql(sql, params);
	}

	/** 明細行を削除 */
	private void deletePurOrdDtl(String compayCd, String purordNo) {
		final Object[] args = { compayCd, purordNo };
		execSql(getSql("PO0000_08"), args);
	}

	/** 発注明細情報テーブルをINSERT */
	private int insertSelectORDDTL(
			MoveActivityInstanceInParam in, Long runtimeId, String purOrdNo, int purordDtlNo, String mnyCd, BigDecimal purOrdAmt, BigDecimal purOrdAmtInctax
	) {
		// 円貨・外貨を通貨コードで判定
		final BigDecimal purOrdAmtJpy = ("JPY".equals(mnyCd) ? purOrdAmt : null);
		final BigDecimal purOrdAmtJpyInctax = ("JPY".equals(mnyCd) ? purOrdAmtInctax : null);
		final BigDecimal purOrdAmtFc = ("JPY".equals(mnyCd) ? null : purOrdAmt);

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				purOrdNo, purordDtlNo,
				purOrdAmtJpy, purOrdAmtJpyInctax, mnyCd, purOrdAmtFc,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				runtimeId
		};
		final String sql = getSql("PO0000_02");
		return execSql(sql, args);
	}

	/** 発注情報テーブルをINSERT */
	private int insertSelectORDINF(
			MoveActivityInstanceInParam in, Long runtimeId, Long prdPurOrdNo
	) {
		final String sql = getSql("PO0000_01");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				prdPurOrdNo,
				corporationCode, userCode, ipAddr,
				corporationCode, userCode, ipAddr,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 定期発注Noをトランザクションデータへ反映 */
	private int updatePrdPurOrdNo(MoveActivityInstanceInParam in, long prdPurOrdNo) {
		final String sql = getSql("PO0000_06");
		final String corporationCode = in.getCorporationCode();
		final long processId = in.getProcessId();
		final String corporationCodeUpdated = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final Object[] args = {
				prdPurOrdNo,
				corporationCodeUpdated, userCode,
				corporationCode, processId
		};
		return execSql(sql, args);
	}

	/** 定期発注予定マスタの登録 */
	private int insertSelect_PRD_PURORD_PLN_MST(MoveActivityInstanceInParam in, long prdPurOrdNo) {
		final String sql = getSql("PO0000_05");
		final String corporationCode = in.getCorporationCode();
		final long processId = in.getProcessId();
		final String corporationCodeUpdated = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				prdPurOrdNo,
				corporationCodeUpdated, userCode, ipAddr, corporationCodeUpdated, userCode, ipAddr,
				corporationCode, processId
		};
		return execSql(sql, args);
	}

	/** 定期発注マスタテーブル登録 */
	private int insertSelect_PRD_PURORD_MST(MoveActivityInstanceInParam in, long prdPurOrdNo) {
		final String sql = getSql("PO0000_03");
		final String corporationCode = in.getCorporationCode();
		final long processId = in.getProcessId();
		final String corporationCodeUpdated = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {
				prdPurOrdNo,
				corporationCodeUpdated, userCode, ipAddr, corporationCodeUpdated, userCode, ipAddr,
				corporationCode, processId
		};
		return execSql(sql, args);
	}
}
