package jp.co.nci.iwf.component.systemConfig.upload;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.download.BaseSyncIdService;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;

/**
 * システム環境設定アップロードでの採番値同期サービス
 */
@ApplicationScoped
public class SystemConfigUploadSyncIdService extends BaseSyncIdService {
	/** 初期化 */
	@PostConstruct
	public void init() {
		propertyCodes.put(WfmCorporationProperty.class, "WFM_CORPORATION_PROPERTY_ID");
		propertyCodes.put(WfmCorpPropMaster.class, "WFM_CORP_PROP_MASTER_ID");
	}
}
