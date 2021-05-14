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
 * 変更契約申請、変更契約申請（経常支払）の承認後に呼び出されるコールバックファンクション(文書管理連携のみ)。
 * 【変更契約申請のみ対象】
 */
public class CallbackFunctionUpdate_CNTRCT_INF_DOC extends BaseCoopWf2Doc4DmmCallbackFunction {
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();

		if (process.getDocId() == null) {
			// 契約No
			String cntrctNo = ctx.runtimeMap.get("NMB0003").getValue();

			//対象の文書IDを取得
			String docSql = getSql("CO0020_11");
			final Object[] args = {cntrctNo};

			EntityManager em = get(EntityManager.class);
			Query q = em.createNativeQuery(docSql);
			putParams(q, args);

			List<Object[]> list = (List<Object[]>) q.getResultList();

			if (list.size() != 0) {
				Long docId = ((BigDecimal)list.get(0)[0]).longValue();
				if (docId != null) {
					// 文書管理は"3:メジャーバージョンアップ"で連携
					functionDef.setFunctionParameter01("3");
					// 連携用のDOC_IDを設定
					contents.relatedDocId = ((BigDecimal)list.get(0)[0]).longValue();
				}
			}
		}

		// WF→文書管理への連携処理を実行
		super.execute(param, result, actionType, contents, ctx, functionDef);

	}


	/**
	 * JPA経由でQueryへパラメータをセット
	 * @param q Query
	 * @param params パラメータList
	 */
	protected void putParams(Query q, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				final Object value = params[i];
				q.setParameter(i + 1, value);
			}
		}
	}

}
