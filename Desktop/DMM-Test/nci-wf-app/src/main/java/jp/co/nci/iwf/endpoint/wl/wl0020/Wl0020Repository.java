package jp.co.nci.iwf.endpoint.wl.wl0020;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;

/**
 * 個人用トレイ選択画面のリポジトリ
 */
@ApplicationScoped
public class Wl0020Repository extends BaseRepository {
	@Inject private NumberingService numbering;
	@Inject private Logger log;

	/** 指定ユーザの現在のトレイ設定個人マスタ */
	public List<MwmTrayConfigPerson> getMwmTrayConfigPersons(String corporationCode, String userCode) {
		final Object[] params = { corporationCode, userCode };
		return select(MwmTrayConfigPerson.class, getSql("WL0020_01"), params);
	}

	/** 操作者の選択可能なトレイ設定 */
	public List<TrayConfig> getTrayConfigsAccessible(String corporationCode, String userCode, String localeCode, String trayType) {
		final Object[] params = {
				localeCode,
				corporationCode,
				userCode,
		};
		return select(TrayConfig.class, getSql("WL0020_02"), params);
	}

	/** トレイ設定個人マスタをインサート */
	public void insert(String corporationCode, String userCode, Wl0020Entity input) {
		final long trayConfigPersonalizeId = numbering.newPK(MwmTrayConfigPerson.class);
		final MwmTrayConfigPerson p = new MwmTrayConfigPerson();
		p.setTrayConfigPersonalizeId(trayConfigPersonalizeId);
		p.setCorporationCode(corporationCode);
		p.setUserCode(userCode);
		p.setTrayType(input.trayType);
		p.setTrayConfigId(input.trayConfigId);
		p.setDeleteFlag(DeleteFlag.OFF);

		log.debug("trayConfigPersonalizeId={} corporationCode={} userCode={} trayType={} trayConfigId={}",
				p.getTrayConfigPersonalizeId(), p.getCorporationCode(), p.getUserCode(), p.getTrayType(), p.getTrayConfigId());
		em.persist(p);
	}

	/** トレイ設定個人マスタを削除 */
	public void remove(Long trayConfigPersonalizeId) {
		final MwmTrayConfigPerson p = em.find(MwmTrayConfigPerson.class, trayConfigPersonalizeId);
		em.remove(p);
	}
}
