package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 業務文書メニュー編集画面のツリーデータ
 */
public class Dc0120TreeItem implements Serializable, DcCodeBook {

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

	public Dc0120TreeItem(String text) {
		this.id = TreeItem.ID;
		this.parent = TreeItem.PARENT;
		this.text = text;
		this.icon = Icon.HOME;
		this.type = Type.HOME;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, true);
	}

	public Dc0120TreeItem(MwmScreenDocLevel sdl) {
		this.id = sdl.getLevelCode();
		this.parent = sdl.getLevelCode().equals(sdl.getParentLevelCode()) ? TreeItem.ID : sdl.getParentLevelCode();
		this.text = sdl.getLevelName();
		this.icon = Icon.BOOK;
		this.type = Type.BOOK;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, CommonFlag.ON.equals(sdl.getExpansionFlag()));
		this.data = new HashMap<>();
		this.data.put(DataKey.SCREEN_DOC_LEVEL_ID, sdl.getScreenDocLevelId());
		this.data.put(DataKey.EXPANSION_FLAG, sdl.getExpansionFlag());
		this.data.put(DataKey.SORTORDER, sdl.getSortOrder());
	}

	public Dc0120TreeItem(MwmScreenDocDef sdd, String parent) {
		this.id = sdd.getScreenDocCode();
		this.parent = parent == null ? TreeItem.ID : parent;
		this.text = sdd.getScreenDocName();
		this.icon = Icon.FILE;
		this.type = Type.FILE;
		this.state = new HashMap<>();
		this.data = new HashMap<>();
		this.data.put(DataKey.SCREEN_DOC_ID, sdd.getScreenDocId());
		this.data.put(DataKey.SORTORDER, sdd.getSortOrder());
		this.a_attr = new HashMap<String, String>();
		this.a_attr.put("data-type", Type.FILE);
	}

}
