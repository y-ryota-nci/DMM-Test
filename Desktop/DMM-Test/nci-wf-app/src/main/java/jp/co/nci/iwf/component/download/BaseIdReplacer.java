package jp.co.nci.iwf.component.download;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.NotImplementedException;

import jp.co.nci.integrated_workflow.model.base.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationDefEx;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadUtils;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * ID置換用サービス
 */
public abstract class BaseIdReplacer extends MiscUtils {

//	@Inject protected Connection conn;
	@Inject private WfInstanceWrapper wf;

	protected Map<Class<?>, String[]> map = new HashMap<>(32);

	protected static final String CORPORATION_CODE = WfmProcessDef.CORPORATION_CODE;
	protected static final String PROCESS_DEF_CODE = WfmProcessDef.PROCESS_DEF_CODE;
	protected static final String PROCESS_DEF_DETAIL_CODE = WfmProcessDef.PROCESS_DEF_DETAIL_CODE;
	protected static final String ACTIVITY_DEF_CODE = WfmActivityDef.ACTIVITY_DEF_CODE;
	protected static final String SEQ_NO_ASSIGNED_DEF = WfmAssignedDef.SEQ_NO_ASSIGNED_DEF;
	protected static final String SEQ_NO_ACTION_DEF = WfmActionDef.SEQ_NO_ACTION_DEF;
	protected static final String ASSIGN_ROLE_CODE = WfmAssignRole.ASSIGN_ROLE_CODE;
	protected static final String SEQ_NO_ASSIGN_ROLE_DETAIL = WfmAssignRoleDetail.SEQ_NO_ASSIGN_ROLE_DETAIL;
	protected static final String SEQ_NO_AUTH_TRANSFER = WfmAuthTransfer.SEQ_NO_AUTH_TRANSFER;
	protected static final String SEQ_NO_CONDITION_DEF = WfmConditionDef.SEQ_NO_CONDITION_DEF;
	protected static final String EXPRESSION_DEF_CODE = WfmExpressionDef.EXPRESSION_DEF_CODE;
	protected static final String FUNCTION_CODE = WfmFunction.FUNCTION_CODE;
	protected static final String SEQ_NO_FUNCTION_DEF = WfmFunctionDef.SEQ_NO_FUNCTION_DEF;
	protected static final String SEQ_NO_INFO_SHARER_DEF = WfmInformationSharerDef.SEQ_NO_INFO_SHARER_DEF;
	protected static final String MENU_ROLE_CODE = WfmMenuRole.MENU_ROLE_CODE;
	protected static final String SEQ_NO_MENU_ROLE_DETAIL = WfmMenuRoleDetail.SEQ_NO_MENU_ROLE_DETAIL;
	protected static final String VARIABLE_DEF_CODE = WfmVariableDef.VARIABLE_DEF_CODE;
	protected static final String SEQ_NO_CHANGE_DEF = WfmChangeDef.SEQ_NO_CHANGE_DEF;
	protected static final String CHANGE_ROLE_CODE = WfmChangeRole.CHANGE_ROLE_CODE;
	protected static final String SEQ_NO_CHANGE_ROLE_DETAIL = WfmChangeRoleDetail.SEQ_NO_CHANGE_ROLE_DETAIL;
	protected static final String SEQ_NO_WF_RELATION_DEF = WfmWfRelationDefEx.SEQ_NO_WF_RELATION_DEF;

	/**
	 * 初期化
	 */
	@PostConstruct
	public abstract void init();

	/** 可変長配列を配列にする */
	protected static String[] L(String...array) {
		return array;
	}

	/**
	 * 対象エンティティのプライマリキーで抽出し、そのID値を返す。レコードが存在しなければNULL
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E getExistingEntity(E entity) {
		String tableName = ProcessDefDownloadUtils.getTableName(entity);
		Class<E> clazz = (Class<E>)entity.getClass();
		if (!map.containsKey(clazz))
			throw new NotImplementedException(clazz);

		// DBから「プライマリキーが一致するレコード」のIDを取得
		String[] pkColumnNames = map.get(clazz);
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder("select ID, TIMESTAMP_UPDATED from ")
				.append(tableName)
				.append(" where ");
		for (int i = 0; i < pkColumnNames.length; i++) {
			String colName = pkColumnNames[i];
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append(colName).append(" = ?");
			String fieldName = toCamelCase(colName);
			params.add(getPropertyValue(entity, fieldName));
		}

		try {
			List<E> results = NativeSqlUtils.select(wf.getConnection(), clazz, sql, params.toArray());
			int size = results.size();
			if (size == 0)
				return null;
			else if (size == 1)
				return results.get(0);
			else
				throw new IllegalArgumentException("ユニークキーで検索したはずなのに、複数レコードが抽出されました。ユニークキーの定義を確認してください。class=" + clazz.getSimpleName() + " 件数=" + size);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * エンティティのプライマリキーを抜き出して文字列化
	 * @param entity
	 * @return
	 */
	public String toKey(Object entity) {
		Class<?> clazz = entity.getClass();
		if (!map.containsKey(clazz))
			throw new NotImplementedException(clazz);

		// DBから「プライマリキーが一致するレコード」のIDを取得
		List<String> keys = new ArrayList<>();
		String[] pkColumnNames = map.get(clazz);
		for (String colName : pkColumnNames) {
			String fieldName = toCamelCase(colName);
			Object key = getPropertyValue(entity, fieldName);
			keys.add( toStr(key) );
		}
		return String.join("~", keys);
	}
}
