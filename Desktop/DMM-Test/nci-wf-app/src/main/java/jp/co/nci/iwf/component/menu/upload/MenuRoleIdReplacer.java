package jp.co.nci.iwf.component.menu.upload;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.download.BaseIdReplacer;

/**
 * メニュー定義のアップロードのID置換ロジック
 */
@ApplicationScoped
public class MenuRoleIdReplacer extends BaseIdReplacer {

	@PostConstruct
	@Override
	public void init() {
		map.put(WfmMenuRole.class, L(CORPORATION_CODE, MENU_ROLE_CODE));
		map.put(WfmMenuRoleDetail.class, L(
				CORPORATION_CODE, MENU_ROLE_CODE, SEQ_NO_MENU_ROLE_DETAIL));
	}

}
