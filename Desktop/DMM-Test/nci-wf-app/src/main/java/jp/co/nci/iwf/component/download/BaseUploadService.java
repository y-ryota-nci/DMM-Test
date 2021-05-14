package jp.co.nci.iwf.component.download;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamBase;
import jp.co.nci.integrated_workflow.api.param.OutParamBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

public class BaseUploadService extends BaseRepository {

	@Inject protected WfInstanceWrapper wf;
	@Inject protected SessionHolder sessionHolder;

	@PostConstruct
	public void init(){
	}

	/**
	 * インサート用IWF APIを呼び出す
	 * @param entity
	 * @return
	 */
	protected Long execInsertAPI(Object entity) {
		// INパラメータ
		String className = entity.getClass().getSimpleName();
		String propName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
		InParamBase in = newInstance("jp.co.nci.integrated_workflow.api.param.input.Insert" + className + "InParam");
		setPropertyValue(in, propName, entity);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// WF APIコール
		OutParamBase out = invokeMethod(wf, "insert" + className, in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		// 更新結果エンティティからIDを抜き出し
		Object result = getPropertyValue(out, propName);
		return invokeMethod(result, "getId");
	}

	/**
	 * アップデート用IWF APIを呼び出す
	 * @param entity
	 * @return
	 */
	protected Long execUpdateAPI(Object entity) {
		// INパラメータ
		String className = entity.getClass().getSimpleName();
		String propName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
		InParamBase in = newInstance("jp.co.nci.integrated_workflow.api.param.input.Update" + className + "InParam");
		setPropertyValue(in, propName, entity);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// WF APIコール
		OutParamBase out = invokeMethod(wf, "update" + className, in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		// 更新結果エンティティからIDを抜き出し
		Object result = getPropertyValue(out, propName);
		return invokeMethod(result, "getId");
	}

	/**
	 * 削除用IWF APIを呼び出す
	 * @param entity
	 * @return
	 */
	protected void execDeleteAPI(Object entity) {
		// INパラメータ
		if (!(entity instanceof WfmNameLookup)) {
			String className = entity.getClass().getSimpleName();
			String propName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
			InParamBase in = newInstance("jp.co.nci.integrated_workflow.api.param.input.Delete" + className + "InParam");
			setPropertyValue(in, propName, entity);
			in.setWfUserRole(sessionHolder.getWfUserRole());

			// WF APIコール
			OutParamBase out = invokeMethod(wf, "delete" + className, in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
	}

	/**
	 * リフレクションでメソッド実行
	 * @param entity 対象エンティティ
	 * @param methodName 実行するメソッド名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T invokeMethod(Object entity, String methodName) {
		Method method;
		try {
			method = entity.getClass().getMethod(methodName);
			return (T)method.invoke(entity);
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * リフレクションでメソッド実行
	 * @param entity 対象エンティティ
	 * @param methodName 実行するメソッド名
	 * @param value メソッドの引数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T invokeMethod(Object entity, String methodName, Object value) {
		Method method;
		try {
			method = entity.getClass().getMethod(methodName, value.getClass());
			return (T)method.invoke(entity, value);
		}
		catch (InvocationTargetException e) {
			// Reflection先で発生したエラーは元のエラーに書き戻す
			Throwable t = e;
			while (t.getCause() != null) {
				t = t.getCause();
			}
			if (t instanceof RuntimeException)
				throw (RuntimeException)t;
			else
				throw new InternalServerErrorException(e);
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * エンティティのプロパティ値＝指定値であれば、新しい値で置換
	 * @param entity エンティティ
	 * @param propName 置換するプロパティ名
	 * @param oldValue 指定値
	 * @param newValue (上書き後の)新しい値
	 */
	protected void setPropertyValueIfSame(Object entity, String propName, Object oldValue, Object newValue) {
		if (eq(oldValue, getPropertyValue(entity, propName))) {
			setPropertyValue(entity, propName, newValue);
		}
	}
}
