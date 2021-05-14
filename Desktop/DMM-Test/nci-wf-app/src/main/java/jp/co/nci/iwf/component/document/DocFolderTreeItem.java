package jp.co.nci.iwf.component.document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書フォルダ階層設定画面のツリーデータ
 */
public class DocFolderTreeItem implements Serializable, DcCodeBook {

	/**  */
	private static final long serialVersionUID = 1L;

	public String id;
	public String parent;
	public String text;
	public String icon;
	public String type;
	public boolean children;
	public Map<String, Object> data;
	public Map<String, Boolean> state;
	public Map<String, String> a_attr;

	public DocFolderTreeItem(String text) {
		this(text, true);
	}

	public DocFolderTreeItem(MwvDocFolder dfe) {
		this(dfe, true);
	}

	public DocFolderTreeItem(String text, boolean opened) {
		this.id = TreeItem.TOP;
		this.parent = TreeItem.PARENT;
		this.text = text;
		this.icon = Icon.HOME;
		this.type = Type.HOME;
		this.children = true;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, opened);
		this.data = new HashMap<>();
		this.data.put(DataKey.FOLDER_PATH, "");
	}

	public DocFolderTreeItem(MwvDocFolder dfe, boolean opened) {
		this.id = String.valueOf(dfe.getDocFolderId());
		this.parent = MiscUtils.eq(0l, dfe.getParentDocFolderId()) ? TreeItem.TOP : String.valueOf(dfe.getParentDocFolderId());
		this.text = dfe.getFolderName();
		this.icon = Icon.BOOK;
		this.type = Type.BOOK;
		this.children = true;
		this.state = new HashMap<>();
		this.state.put(State.OPENED, opened);
		this.data = new HashMap<>();
		this.data.put(DataKey.VERSION, dfe.getVersion());
		this.data.put(DataKey.HIERARCHY_ID, dfe.getDocFolderHierarchyId());
		this.data.put(DataKey.SORTORDER, dfe.getSortOrder());
		this.data.put(DataKey.FOLDER_PATH, "/" + dfe.getFolderPath());
	}

}