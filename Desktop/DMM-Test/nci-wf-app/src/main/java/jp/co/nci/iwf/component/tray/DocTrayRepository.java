package jp.co.nci.iwf.component.tray;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 文書管理トレイ情報のリポジトリ
 */
@ApplicationScoped
public class DocTrayRepository extends BaseRepository {
	/** 操作者の使用する文書管理トレイ設定を抽出 */
	public TrayConfig getTrayConfig(String trayType, LoginInfo login) {
		final Object[] params = {
				// priority=1
				login.getCorporationCode(),
				login.getUserCode(),
				trayType,
				// priority=2
				login.getCorporationCode(),
				UserCodes.COMMON_USER_CODE,
				trayType,
				// priority=3
				login.getCorporationCode(),
				// locale
				login.getLocaleCode()
		};
		// union all で抽出するので、優先順位の一番高い一件を使う
		return selectOne(TrayConfig.class, getSql("DC0022_01"), params);
	}

	/** 文書トレイ設定検索条件 */
	public List<TrayConditionDef> getTrayConditions(long docTrayConfigId, String localeCode) {
		final Object[] params = { localeCode, docTrayConfigId };
		return select(TrayConditionDef.class, getSql("DC0022_02"), params);
	}

	/** 文書トレイ設定検索結果 */
	public List<TrayResultDef> getTrayResults(long docTrayConfigId, String localeCode) {
		final Object[] params = { localeCode, docTrayConfigId };
		return select(TrayResultDef.class, getSql("DC0022_03"), params);
	}

}
