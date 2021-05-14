package jp.co.nci.iwf.endpoint.dc.dc0230;

import java.util.List;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0200.Dc0200Entity;
import jp.co.nci.iwf.endpoint.dc.dc0200.Dc0200Service;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigPerson;

/**
 * 文書トレイ選択(管理者用)サービス
 */
@BizLogic
@Typed(Dc0230Service.class)
public class Dc0230Service extends Dc0200Service {
	@Inject private Dc0230Repository repository;

	/** 現在選択している文書トレイ設定個人マスタ */
	@Override
	protected List<MwmDocTrayConfigPerson> getMwmDocTrayConfigPersons(LoginInfo login) {
		return repository.getMwmDocTrayConfigPersons(login.getCorporationCode(), UserCodes.COMMON_USER_CODE);
	}

	/** アクセス可能な文書トレイ設定の選択肢を生成 */
	@Override
	protected List<MwmDocTrayConfig> getDocTrayConfigsAccessible(LoginInfo login, String trayType) {
		return repository.getDocTrayConfigsAccessible(
				login.getCorporationCode(), login.getUserCode(), login.getLocaleCode(), trayType);
	}

	/** インサート */
	protected void insert(LoginInfo login, Dc0200Entity input) {
		repository.insert(login.getCorporationCode(), UserCodes.COMMON_USER_CODE, input);
	}
}
