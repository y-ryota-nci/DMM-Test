package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.dmm.customize.component.DmmCodeBook.PurordTp;
import jp.co.dmm.customize.endpoint.py.PayInfService;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInf;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.StopProcessInstanceInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * WF支払申請から前払情報／前払消込情報への反映処理
 */
public class CallbackFunctionCreate_PAY_INF_ADVPAY extends BaseCallbackFunction {

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		// 前払申請の場合は処理を行わない
		final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();
		if (CommonFlag.ON.equals(advpayFg)) {
			return;
		}

		final PayInfService service = get(PayInfService.class);
		final String companyCd = ctx.runtimeMap.get("TXT0089").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0037").getValue();
		final String purordTp = ctx.runtimeMap.get("TXT0147").getValue();
		final String mnyCd = ctx.runtimeMap.get("MST0149").getValue();
		final String addRto = ctx.runtimeMap.get( "TXT0170").getValue();

		// 支払Noに紐付く前払消込情報
		final List<AdvpayMatInf> currentList = service.getAdvpayMatInfByPayNo(companyCd, payNo);
		// 支払Noに紐付く前払金Noリスト
		final Set<String> advpayNoList = currentList.stream().map(e -> e.getId().getAdvpayNo()).collect(Collectors.toSet());
		// 支払明細Noをキーとした前払消込情報Map
		final Map<BigDecimal, AdvpayMatInf> currents = currentList
				.stream()
				.collect(Collectors.toMap(e -> e.getPayDtlNo(), e -> e));

		final PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0080");	// 支払明細のリピーター
		for (PartsContainerRow row : grid.rows) {
			final String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 前払No
			final String advpayNo = ctx.runtimeMap.get(prefix + "TXT0049").getValue();

			if (isNotEmpty(advpayNo)) {
				// プロセス取消時は前払消込情報をクリアするため、currentsから削除させない
				if (!(param instanceof StopProcessInstanceInParam)) {
					// 前払充当額
					final BigDecimal matAmt = new BigDecimal(ctx.runtimeMap.get(prefix + "TXT0050").getValue());
					// 支払明細Noをキーに既存の有無をチェック。削除できれば既存データあり
					final BigDecimal payDtlNo = new BigDecimal(row.rowId);
					final AdvpayMatInf current = currents.remove(payDtlNo);
					if (PurordTp.EXPENSE.equals(purordTp)) {
						if (current == null)
							service.insertAdvpayMat(companyCd, payNo, payDtlNo, advpayNo, matAmt, mnyCd, addRto);
						else
							service.updateAdvpayMat(current, payNo, payDtlNo, advpayNo, matAmt, mnyCd, addRto);
					} else if (current == null) {
						final String rcvinspNo = ctx.runtimeMap.get(prefix + "TXT0019").getValue();
						final BigDecimal rcvinspDtlNo = new BigDecimal(ctx.runtimeMap.get(prefix + "TXT0020").getValue());
						service.updateAdvpayMat(companyCd, rcvinspNo, rcvinspDtlNo, payNo, payDtlNo);
					}
					// 今回申請分の分前払金Noを追記
					advpayNoList.add(advpayNo);
				}
			}
		}

		// 残余は使わなくなったものなので消込対象から削除
		for (AdvpayMatInf advpayMat : currents.values()) {
			if (PurordTp.EXPENSE.equals(purordTp))
				service.delete(advpayMat);
			else
				service.update(advpayMat);
		}
		// 処理前の前払Noと申請した前払Noの両方に対して、前払情報の残高と前払ステータスの更新
		service.updateAdvpayInf(companyCd, advpayNoList, mnyCd);
	}
}
