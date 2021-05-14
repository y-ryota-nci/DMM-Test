package jp.co.nci.iwf.endpoint.dc.dc0131;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0131.entity.ScreenDocLevelDefEntity;

public class Dc0131TreeItem implements Serializable, DcCodeBook {

	/**  */
	private static final long serialVersionUID = 1L;

	public String id;
	public String parent;
	public String text;
	public String type;
	public String icon;
	public Map<String, Object> data;
	public Map<String, Boolean> state;

	public Dc0131TreeItem(ScreenDocLevelDefEntity sdld) {
		this.id = sdld.levelCode;
		this.parent = sdld.levelCode.equals(sdld.parentLevelCode) ? TreeItem.PARENT : sdld.parentLevelCode;
		this.text = sdld.levelName;
		this.type = sdld.type;
		this.icon = Type.BOOK.equals(sdld.type) ? Icon.BOOK : Icon.FILE;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, Type.BOOK.equals(sdld.type) && CommonFlag.ON.equals(sdld.expansionFlag));
		this.data = new HashMap<>();
		if (Type.FILE.equals(sdld.type)) {
			this.data.put(DataKey.SCREEN_DOC_ID, sdld.id);
			this.data.put(DataKey.VALID_START_DATE, sdld.validStartDate);
			this.data.put(DataKey.VALID_END_DATE, sdld.validEndDate);
		}
	}
}
