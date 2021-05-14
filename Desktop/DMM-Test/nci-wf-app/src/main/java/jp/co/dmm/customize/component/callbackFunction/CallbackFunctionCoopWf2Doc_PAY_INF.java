package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 支払(新規／変更)の申請情報を文書管理側へ連携するためのコールバックファンクション.
 */
public class CallbackFunctionCoopWf2Doc_PAY_INF extends BaseCoopWf2Doc4DmmCallbackFunction {

	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();

		// 新規の検収申請か
		final boolean isNew = eq("NEW", ctx.runtimeMap.get("TXT0113").getValue());
		// 支払(前払)か
		final boolean isAdvpay = eq("1", ctx.runtimeMap.get("TXT0155").getValue());
		// 支払(経費)か
		final boolean isExpense = eq("4", ctx.runtimeMap.get("TXT0147").getValue());

		// 支払(前払)、支払(経費)の場合はアクション機能定義の第2パラメータに設定する画面文書定義コードを変更
		if (isAdvpay) {
			functionDef.setFunctionParameter02("0000000014");
		} else if (isExpense) {
			functionDef.setFunctionParameter02("0000000015");
		}

		// 変更申請かつ申請内において文書管理側へ連携していない(＝文書IDがない)場合、新規検収の際に作成されたプロセスインスタンスから文書IDを取得
		// 取得した文書IDを使って文書管理へ連携する
		// またこの場合、文書管理は"3:メジャーバージョンアップ"で連携する
		if (!isNew && process.getDocId() == null) {
			// 支払No(＝申請番号)
			String payNo = ctx.runtimeMap.get("NMB0037").getValue();
			// 文書IDを取得
			Long docId = getDocId(process.getCorporationCode(), payNo);
			if (docId != null) {
				// 文書管理は"3:メジャーバージョンアップ"で連携
				functionDef.setFunctionParameter01("3");
				// 連携する文書IDを指定
				contents.relatedDocId = docId;
			}
		}

		// WF→文書管理への連携処理を実行
		super.execute(param, result, actionType, contents, ctx, functionDef);
	}

	/** 文書IDの取得. */
	@SuppressWarnings("unchecked")
	private Long getDocId(String corporationCode, String payNo) {
		final Object[] params = { corporationCode, payNo };
		final String sql = getSql("PY0000_17");
		EntityManager em = get(EntityManager.class);
		Query q = em.createNativeQuery(sql);
		putParams(q, params);
		List<Object> list = (List<Object>) q.getResultList();
		if (list != null && list.size() > 0) {
			return ((BigDecimal)list.get(0)).longValue();
		}
		return null;
	}

	private void putParams(Query q, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				final Object value = params[i];
				q.setParameter(i + 1, value);
			}
		}
	}
}
