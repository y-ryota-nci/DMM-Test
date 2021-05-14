package jp.co.nci.iwf.endpoint.wl.wl0021;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.endpoint.wl.wl0020.Wl0020Entity;
import jp.co.nci.iwf.endpoint.wl.wl0020.Wl0020Repository;
import jp.co.nci.iwf.endpoint.wl.wl0020.Wl0020Service;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;

/**
 * 標準トレイ選択画面サービス
 */
@ApplicationScoped
@Typed(Wl0021Service.class)
public class Wl0021Service extends Wl0020Service {
	@Inject private Wl0020Repository repository;

	/** 現在選択しているトレイ設定個人マスタ */
	@Override
	protected List<MwmTrayConfigPerson> getMwmTrayConfigPersons(LoginInfo login) {
		return repository.getMwmTrayConfigPersons(login.getCorporationCode(), UserCodes.COMMON_USER_CODE);
	}

	/** この画面で使用するトレイ種別のリストを生成 */
	@Override
	protected List<TrayType> createTrayTypeFilters(final LoginInfo login) {
		return asList(TrayType.WORKLIST, TrayType.FORCE, TrayType.OWN, TrayType.ALL, TrayType.BATCH);
	}

	/** アクセス可能なトレイ設定の選択肢を生成 */
	protected List<TrayConfig> getTrayConfigsAccessible(LoginInfo login, String trayType) {
		return repository.getTrayConfigsAccessible(
				login.getCorporationCode(), UserCodes.COMMON_USER_CODE, login.getLocaleCode(), trayType);
	}

	/** インサート */
	protected void insert(LoginInfo login, Wl0020Entity input) {
		repository.insert(login.getCorporationCode(), UserCodes.COMMON_USER_CODE, input);
	}
}
