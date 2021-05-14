package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.dmm.customize.endpoint.ri.RcvinspInfService;
import jp.co.dmm.customize.jpa.entity.mw.AdvpayMatInf;
import jp.co.dmm.customize.jpa.entity.mw.RcvinspdtlInf;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.StopProcessInstanceInParam;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * WF検収申請から前払情報／前払消込情報への反映処理
 */
public class CallbackFunctionCreate_RCVINSP_INF_ADVPAY extends BaseCallbackFunction {

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {
		final RcvinspInfService service = get(RcvinspInfService.class);
		final String companyCd = ctx.runtimeMap.get("TXT0068").getValue();
		final String rcvinspNo = ctx.runtimeMap.get("NMB0047").getValue();
		final String mnyCd = ctx.runtimeMap.get("MST0104").getValue();
		final String addRto = ctx.runtimeMap.get( "TXT0123").getValue();

		// 検収Noに紐付く前払消込情報
		final List<AdvpayMatInf> currentList = service.getAdvpayMatInfByRcvinspNo(companyCd, rcvinspNo);
		// 検収Noに紐付く前払金Noリスト
		final Set<String> advpayNoList = currentList.stream().map(e -> e.getId().getAdvpayNo()).collect(Collectors.toSet());
		// 検収明細Noをキーとした前払消込情報Map
		final Map<BigDecimal, AdvpayMatInf> currents = currentList
				.stream()
				.collect(Collectors.toMap(e -> e.getRcvinspDtlNo(), e -> e));

		final PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0059");	// 検収明細のリピーター
		for (PartsContainerRow row : grid.rows) {
			final String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 前払No
			final String advpayNo = ctx.runtimeMap.get(prefix + "TXT0085").getValue();

			if (isNotEmpty(advpayNo)) {
				// プロセス取消時は前払消込情報をクリアするため、currentsから削除させない
				if (!(param instanceof StopProcessInstanceInParam)) {
					// 前払充当額
					final BigDecimal matAmt = new BigDecimal(ctx.runtimeMap.get(prefix + "TXT0084").getValue());
					// 既存データがあろうとなかろうとinsert&delete updateは行わない
					final Integer rcvinspDtlNo = row.rowId;
					service.insertAdvpayMat(companyCd, rcvinspNo, rcvinspDtlNo, advpayNo, matAmt, mnyCd, addRto);

					// 今回申請分の分前払金Noを追記
					advpayNoList.add(advpayNo);
				}
			}
		}

		// 残余は使わなくなったものなので消込対象から削除
		for (AdvpayMatInf advpayMat : currents.values()) {
			service.delete(advpayMat);
		}

		// 変更申請で取り消した場合(決裁後は差戻不可のためRCVINSP_INFテーブルにデータがある状態で取り消しが押された場合は必ず変更申請)
		if(param instanceof StopProcessInstanceInParam && service.getAdvpayToRmnCount(companyCd, rcvinspNo) > 0) {
			List<RcvinspdtlInf> rcvinspdtlInfList = service.getAdvpayToRmn(companyCd, rcvinspNo);
			for (Iterator<RcvinspdtlInf> iterator = rcvinspdtlInfList.iterator(); iterator.hasNext();) {
				RcvinspdtlInf rcvinspdtlInf = (RcvinspdtlInf) iterator.next();
				final Integer rcvinspDtlNo = (int)rcvinspdtlInf.getId().getRcvinspDtlNo();
				final String advpayNo = rcvinspdtlInf.getAdvpayNo();
				final String mnyCddtl = rcvinspdtlInf.getMnyCd();
				BigDecimal matAmt = null;
				if(MiscUtils.eq("JPY" ,mnyCddtl) ) {
					matAmt = rcvinspdtlInf.getAdvpayAplyAmtJpy();
				}else {
					matAmt = rcvinspdtlInf.getAdvpayAplyAmtFc();
				}
				Object addRtodtl = rcvinspdtlInf.getAddRto();
				if(MiscUtils.isEmpty(addRtodtl)) {
					addRtodtl = addRto;
				}
				service.insertAdvpayMat(companyCd, rcvinspNo, rcvinspDtlNo, advpayNo, matAmt, mnyCddtl, addRtodtl.toString());
				if(!advpayNoList.contains(advpayNo)) {
					advpayNoList.add(advpayNo);
				}
			}
		}

		// 処理前の前払Noと申請した前払Noの両方に対して、前払情報の残高と前払ステータスの更新
		service.updateAdvpayInf(companyCd, advpayNoList, mnyCd);
	}

}


