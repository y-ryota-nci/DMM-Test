package jp.co.nci.iwf.endpoint.na.na0002;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.endpoint.na.NaCodeBook;
import jp.co.nci.iwf.endpoint.na.na0002.entity.ScreenProcessLevelDefEntity;

public class Na0002TreeItem implements Serializable, NaCodeBook {

	/**  */
	private static final long serialVersionUID = 1L;

	public String id;
	public String parent;
	public String text;
	public String type;
	public String icon;
	public Map<String, Object> data;
	public Map<String, Boolean> state;

	public Na0002TreeItem(ScreenProcessLevelDefEntity spld) {
		this.id = spld.levelCode;
		this.parent = spld.levelCode.equals(spld.parentLevelCode) ? TreeItem.PARENT : spld.parentLevelCode;
		this.text = spld.levelName;
		this.type = spld.type;
		this.icon = Type.BOOK.equals(spld.type) ? Icon.BOOK : Icon.FILE;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, Type.BOOK.equals(spld.type) && CommonFlag.ON.equals(spld.expansionFlag));
		this.data = new HashMap<>();
		if (Type.FILE.equals(spld.type)) {
			this.data.put(DataKey.SCREEN_PROCESS_ID, spld.id);
			this.data.put(DataKey.VALID_START_DATE, spld.validStartDate);
			this.data.put(DataKey.VALID_END_DATE, spld.validEndDate);
			this.data.put(DataKey.NEW_APPLY_DISPLAY_FLAG, CommonFlag.OFF);
		}
	}
}
