package jp.co.nci.iwf.component.menu.upload;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.download.BaseSyncIdService;

/**
 * メニューロール定義のアップロードのID置換ロジック
 */
@ApplicationScoped
public class MenuRoleUploadSyncIdService extends BaseSyncIdService {

	/** 初期化 */
	@PostConstruct
	@Override
	public void init() {
		propertyCodes.put(WfmMenuRoleDetail.class, "WFM_MENU_ROLE_DETAIL_ID");
		propertyCodes.put(WfmMenuRole.class, "WFM_MENU_ROLE_ID");
	}

}
