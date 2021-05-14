package jp.co.nci.iwf.component.menu.download;

import java.io.Serializable;
import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * メニューダウンロード用DTO
 */
public class MenuRoleDownloadDto extends BaseDownloadDto implements Serializable {
	/** ダウンロード時のメニューロール区分 */
	public String menuRoleType;

	/** メニューロール(WFM_MENU_ROLE) */
	public List<WfmMenuRole> menuRoleList;
	/** メニューロール構成(WFM_MENU_ROLE_DETAIL) */
	public List<WfmMenuRoleDetail> menuRoleDetailList;
	/** 名称ルックアップリスト(WFM_NAME_LOOKUP) */
	public List<WfmNameLookup> nameLookupList;



	/**
	 * コンストラクタ
	 */
	public MenuRoleDownloadDto() {
	}

	/**
	 * コンストラクタ
	 * @param corporationCode
	 */
	public MenuRoleDownloadDto(String corporationCode, String corporationName, String menuRoleType) {
		super(corporationCode, corporationName);
		this.menuRoleType = menuRoleType;
	}
}
