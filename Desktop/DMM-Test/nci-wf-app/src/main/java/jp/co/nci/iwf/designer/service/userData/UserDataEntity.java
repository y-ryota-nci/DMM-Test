package jp.co.nci.iwf.designer.service.userData;

import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツのユーザデータ（トランザクション）の格納クラス
 */
public class UserDataEntity {
	/** デフォルト・コンストラクタ */
	public UserDataEntity() {
		this(null, new HashMap<>());
	}

	/** コンストラクタ */
	public UserDataEntity(Map<String, Object> map) {
		this(null, map);
	}

	/** コンストラクタ */
	public UserDataEntity(String tableName, Map<String, Object> map) {
		this.tableName = tableName;
		this.values = map;
		this.runtimeId = MiscUtils.toLong(map.get("runtimeId"));
		this.parentRuntimeId = MiscUtils.toLong(map.get("parentRuntimeId"));
		this.corporationCode = MiscUtils.toStr(map.get("corporationCode"));
		this.processId = MiscUtils.toLong(map.get("processId"));
		this.sortOrder = MiscUtils.defaults(MiscUtils.toInt(map.get("sortOrder")), 0);
		this.version = MiscUtils.toLong(map.get("version"));
		this.deleteFlag = MiscUtils.toStr(map.get("deleteFlag"));
	}

	public Long runtimeId;
	public Long parentRuntimeId;
	public String corporationCode;
	public Long processId;
	public int sortOrder;
	public Long version;
	public String deleteFlag;
	public Map<String, Object> values;
	public String tableName;
}
