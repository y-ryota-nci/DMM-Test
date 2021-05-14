package jp.co.nci.iwf.component.systemConfig.download;

import java.io.Serializable;
import java.util.List;

import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;

/**
 * システム環境設定ダウンロードDTO
 */
public class SystemConfigDownloadDto extends BaseDownloadDto implements Serializable {
	/** 会社別プロパティマスタ */
	public List<WfmCorporationProperty> corporationPropertyList;
	/** プロパティマスタ */
	public List<WfmCorpPropMaster> corpPropMasterList;
	/** メール環境設定マスタ */
	public List<MwmMailConfig> mailConfigList;
	/** 名称ルックアップマスタ */
	public List<WfmNameLookup> nameLookupList;
}
