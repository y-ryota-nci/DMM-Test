package jp.co.nci.iwf.component.callbackFunction;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.callback.AbstractCallbackFunction;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CallbackParamKey;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * WFコールバックファンクションの基底クラス
 */
public abstract class BaseCallbackFunction extends AbstractCallbackFunction implements CodeBook, ICallbackFunction {


	/** CDIからBeanをDIして返す */
	protected static <E> E get(Class<E> clazz) {
		return CDI.current().select(clazz).get();
	}

	/** CDIからBeanをDIして返す */
	protected static <E> E get(Class<E> clazz, Annotation...selectors) {
		return CDI.current().select(clazz, selectors).get();
	}

	/** CDIからBeanをDIして返す */
	protected static <E> E get(Class<E> clazz, AnnotationLiteral<?>...selectors) {
		return CDI.current().select(clazz, selectors).get();
	}

	/** WF API取得 */
	protected WfInstanceWrapper getWfInstanceWrapper() {
		return CDI.current().select(WfInstanceWrapper.class).get();
	}

	/** 等価か */
	protected static <T> boolean eq(T t1, T t2) {
		return MiscUtils.eq(t1, t2);
	}

	/** 文字列化 */
	protected static String toStr(Object o) {
		return MiscUtils.toStr(o);
	}

	/** 空か（文字列のみ、空文字列も空として扱う） */
	protected static boolean isEmpty(Object obj) {
		return MiscUtils.isEmpty(obj);
	}

	/** 空でないか（文字列のみ、空文字列も空として扱う） */
	protected static boolean isNotEmpty(Object obj) {
		return MiscUtils.isNotEmpty(obj);
	}

	/** *.yamlファイルからIDをキーにSQL文を返す */
	protected static String getSql(String sqlId) {
		final SqlService service = get(SqlService.class);
		return service.get(sqlId);
	}

	/** SQL実行、トランザクションは@Transactionalに従う */
	protected int execSql(String sql, Object[] params) {
		// JPAの管理するトランザクションに乗せるため、エンティティマネージャからConnectionを取得する
		EntityManager em = get(EntityManager.class);
		Connection conn = em.unwrap(Connection.class);	// これはJPA管理化のConnectionだから、勝手にクローズ禁止
		try {
			return NativeSqlUtils.execSql(conn, sql, params);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException("コールバックファンクションの実行中にエラーが発生しました", e);
		}
	}

	/**
	 * 業務機能処理実行.
	 *
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 */
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result) throws Exception {
		final Map<String, Object> handOverParam = param.getHandOverParam();
		final Vd0310Contents contents = (Vd0310Contents)handOverParam.get(Vd0310Contents.class.getSimpleName());
		final RuntimeContext ctx = (RuntimeContext)handOverParam.get(RuntimeContext.class.getSimpleName());
		final String actionType = (String)handOverParam.get(ActionType.class.getSimpleName());
		final WfvFunctionDef functionDef = (WfvFunctionDef)param.getParam(CallbackParamKey.WFV_FUNCTION_DEF);
		execute(param, result, actionType, contents, ctx, functionDef);
	}

	/**
	 * 業務機能処理実行.
	 *
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 */
	public abstract void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType, Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef);


	/** 引き継ぎパラメータの取得 */
	@SuppressWarnings("unchecked")
	protected <T> T getHandOverParam(InParamCallbackBase param, String key) {
		return (T)param.getHandOverParam().get(key);
	}

	/** 引き継ぎパラメータのセット */
	protected Object setHandOverparam(InParamCallbackBase param, String key, Object value) {
		return param.getHandOverParam().put(key, value);
	}
}
