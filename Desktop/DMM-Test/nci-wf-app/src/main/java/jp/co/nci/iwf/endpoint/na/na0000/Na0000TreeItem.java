package jp.co.nci.iwf.endpoint.na.na0000;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.endpoint.na.NaCodeBook;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請階層設定画面のツリーデータ
 */
public class Na0000TreeItem implements Serializable, NaCodeBook {

	/**  */
	private static final long serialVersionUID = 1L;

	public String id;
	public String parent;
	public String text;
	public String icon;
	public String type;
	public Map<String, Object> data;
	public Map<String, Boolean> state;
	public Map<String, String> a_attr;

	public Na0000TreeItem(String text) {
		this.id = TreeItem.ID;
		this.parent = TreeItem.PARENT;
		this.text = text;
		this.icon = Icon.HOME;
		this.type = Type.HOME;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, true);
	}

	public Na0000TreeItem(MwmScreenProcessLevel spl) {
		this.id = spl.getLevelCode();
		this.parent = spl.getLevelCode().equals(spl.getParentLevelCode()) ? TreeItem.ID : spl.getParentLevelCode();
		this.text = spl.getLevelName();
		this.icon = Icon.BOOK;
		this.type = Type.BOOK;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, CommonFlag.ON.equals(spl.getExpansionFlag()));
		this.data = new HashMap<>();
		this.data.put(DataKey.SCREEN_PROCESS_LEVEL_ID, spl.getScreenProcessLevelId());
		this.data.put(DataKey.EXPANSION_FLAG, spl.getExpansionFlag());
		this.data.put(DataKey.SORT_ORDER, spl.getSortOrder());
	}

	public Na0000TreeItem(MwmScreenProcessDef spd, String parent) {
		this.id = spd.getScreenProcessCode();
		this.parent = parent == null ? TreeItem.ID : parent;
		this.text = spd.getScreenProcessName();
		this.icon = Icon.FILE;
		this.type = Type.FILE;
		this.state = new HashMap<>();
		this.data = new HashMap<>();
		this.data.put(DataKey.SCREEN_PROCESS_ID, spd.getScreenProcessId());
		this.data.put(DataKey.SORT_ORDER, spd.getSortOrder());
		this.a_attr = new HashMap<String, String>();
		this.a_attr.put("data-type", Type.FILE);
	}

}
