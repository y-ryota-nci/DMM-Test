package jp.co.nci.iwf.endpoint.dc.dc0230;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import jp.co.nci.iwf.endpoint.dc.dc0200.Dc0200Repository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;

/**
 * 文書トレイ選択(管理者用)リポジトリ
 */
@ApplicationScoped
@Typed(Dc0230Repository.class)
public class Dc0230Repository extends Dc0200Repository {

	/** 操作者の選択可能な文書トレイ設定 */
	@Override
	public List<MwmDocTrayConfig> getDocTrayConfigsAccessible(
			String corporationCode, String userCode, String localeCode, String trayType) {
		final Object[] params = {
				localeCode,
				corporationCode,
		};
		return select(MwmDocTrayConfig.class, getSql("DC0220_03"), params);
	}
}
