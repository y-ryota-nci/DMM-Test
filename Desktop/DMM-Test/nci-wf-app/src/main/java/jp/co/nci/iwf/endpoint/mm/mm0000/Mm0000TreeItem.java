package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * プロファイル管理画面のツリーデータ
 */
public class Mm0000TreeItem implements Serializable, Mm0000CodeBook {
	public String id;
	public String text;
	public String icon;
	public Map<String, Boolean> state;
	public Map<String, String> li_attr = new HashMap<>();
	public boolean children;

	/** 企業ベースのコンストラクタ */
	public Mm0000TreeItem(WfmCorporation c) {
		id = String.join("@",
				c.getCorporationCode(),
				String.valueOf(c.getTimestampUpdated().getTime()));
		if (MiscUtils.isNotEmpty(c.getCorporationGroupName()))
			text = "[" + c.getCorporationGroupName() + "] " + c.getCorporationName();
		else
			text = c.getCorporationName();
		icon = "fa fa-institution";
		children = true;
		state = new HashMap<>();
		state.put(State.OPENED, true);
		if (DeleteFlag.ON.equals(c.getDeleteFlag())) {
			li_attr.put("data-type", "deleted");
		} else {
			li_attr.put("data-type", "corporation");
		}
	}

	/** 組織ベースのコンストラクタ */
	public Mm0000TreeItem(WfmOrganization o) {
		id = String.join("@",
				o.getCorporationCode(),
				o.getOrganizationCode(),
				String.valueOf(o.getTimestampUpdated().getTime()));
		text = o.getOrganizationAddedInfo() + " " + o.getOrganizationName();
		icon = "fa fa-sitemap";
		children = true;
		if (DeleteFlag.ON.equals(o.getDeleteFlag())) {
			li_attr.put("data-type", "deleted");
		} else {
			li_attr.put("data-type", "organization");
		}
	}

	/** ユーザベースのコンストラクタ */
	public Mm0000TreeItem( WfvUserBelongImpl ub) {
		id = String.join("@",
				ub.getCorporationCode(),
				ub.getOrganizationCode(),
				ub.getUserCode(),
				String.valueOf(ub.getTimestampUpdatedUser().getTime()));
		text = String.format("%s %s [%s]",
				ub.getUserAddedInfo(), ub.getUserName(),
				MiscUtils.defaults(ub.getPostName(), "---"));
		icon = "fa fa-user";
		if (DeleteFlag.ON.equals(ub.getDeleteFlagUser())) {
			li_attr.put("data-type", "deleted");
		} else {
			li_attr.put("data-type", "user");
		}
	}
}
