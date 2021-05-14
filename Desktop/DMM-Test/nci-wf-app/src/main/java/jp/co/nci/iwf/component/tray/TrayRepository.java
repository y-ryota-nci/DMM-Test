package jp.co.nci.iwf.component.tray;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * トレイ設定用のリポジトリ
 */
@ApplicationScoped
public class TrayRepository extends BaseRepository {

	/** 操作者のアクセス可能なトレイ設定 */
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
		return selectOne(TrayConfig.class, getSql("WL0000_01"), params);
	}

	/** トレイ設定検索条件マスタを抽出 */
	public List<TrayConditionDef> getTrayConditions(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		return select(TrayConditionDef.class, getSql("WL0000_03"), params);
	}

	/** トレイ設定検索結果マスタを抽出 */
	public List<TrayResultDef> getTrayResults(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		return select(TrayResultDef.class, getSql("WL0000_04"), params);
	}

	/** 画面一覧を抽出 */
	public List<MwvScreen> getMwmScreens(String corporationCode, String localeCode) {
		final String sql = getSql("WL0000_05");
		final Object[] params = { corporationCode, localeCode };
		return select(MwvScreen.class, sql, params);
	}

	/** 画面プロセス一覧を抽出 */
	public List<MwvScreenProcessDef> getMwmScreenProcess(String corporationCode, String localeCode) {
		final String sql = getSql("WL0000_06");
		final Object[] params = { corporationCode, localeCode };
		return select(MwvScreenProcessDef.class, sql, params);
	}

	/** 画面プロセスIDから画面プロセス定義を抽出 */
	public MwvScreenProcessDef getMwvScreenProcessDef(String corporationCode, String screenProcessCode, String localeCode) {
		final String sql = getSql("WL0000_07");
		final Object[] params = { corporationCode, screenProcessCode, localeCode };
		return selectOne(MwvScreenProcessDef.class, sql, params);
	}

	/** 画面コードから画面情報を抽出 */
	public MwmScreen getMwmScreen(String corporationCode, String screenCode) {
		final String sql = getSql("WL0000_08");
		final Object[] params = { corporationCode, screenCode };
		return selectOne(MwmScreen.class, sql, params);
	}
}
