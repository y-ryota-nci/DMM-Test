package jp.co.nci.iwf.component.systemConfig.download;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;

/**
 * システム環境設定ダウンロード用リポジトリ
 */
@ApplicationScoped
public class SystemConfigDownloadRepository extends BaseRepository {
	/** 企業プロパティマスタ抽出 */
	public List<WfmCorporationProperty> getWfmCorporationProperty(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(WfmCorporationProperty.class, getSql("MM0110_03"), params);
	}

	/** プロパティマスタ抽出 */
	public List<WfmCorpPropMaster> getWfmCorpPropMaster() {
		return select(WfmCorpPropMaster.class, getSql("MM0110_02"));
	}

	/** メール環境設定マスタ抽出 */
	public List<MwmMailConfig> getMwmMailConfig() {
		return select(MwmMailConfig.class, getSql("MM0110_04"));
	}

	/** 名称ルックアップマスタ抽出 */
	public List<WfmNameLookup> getWfmNameLookup(String t1, String t2) {
		final Object[] params = { t1, t2 };
		return select(WfmNameLookup.class, getSql("MM0110_05"), params);
	}
}
