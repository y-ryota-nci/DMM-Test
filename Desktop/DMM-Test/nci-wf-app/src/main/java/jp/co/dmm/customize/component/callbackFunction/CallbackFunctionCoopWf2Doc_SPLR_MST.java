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
 * 取引先(新規／変更／停止)の申請情報を文書管理側へ連携するためのコールバックファンクション.
 */
public class CallbackFunctionCoopWf2Doc_SPLR_MST extends BaseCoopWf2Doc4DmmCallbackFunction {

	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();

		// 新規の取引先申請か
		final boolean isNew = eq("1", ctx.runtimeMap.get("DDL0002").getValue());

		// 変更または停止の申請かつ申請内において文書管理側へ連携していない(＝文書IDがない)場合
		// 新規申請の際に作成されたプロセスインスタンスから文書IDを取得
		// 取得した文書IDを使って文書管理へ連携する
		// またこの場合、文書管理は"3:メジャーバージョンアップ"で連携する
		if (!isNew && process.getDocId() == null) {
			// 取引先変更の場合、過去に申請があったかどうかは会社コード(COMPANY_CD)と取引先コードから見つける
			// 会社コード(COMPANY_CD)
			String companyCd = ctx.runtimeMap.get("TXT0301").getValue();
			// 取引先コード
			String splrCd = ctx.runtimeMap.get("TXT0003").getValue();
			// 文書IDを取得
			Long docId = getDocId(companyCd, splrCd);
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
	private Long getDocId(String companyCd, String splrCd) {
		final Object[] params = { companyCd, splrCd };
		final String sql = getSql("SP0000_03");
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
