package jp.co.nci.iwf.endpoint.dc.dc0200;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigPerson;

/**
 * 文書トレイ選択(個人用)リポジトリ
 */
@ApplicationScoped
@Typed(Dc0200Repository.class)
public class Dc0200Repository extends BaseRepository {
	@Inject private NumberingService numbering;
	@Inject private Logger log;

	/** 指定ユーザの現在の文書トレイ設定個人マスタ */
	public List<MwmDocTrayConfigPerson> getMwmDocTrayConfigPersons(String corporationCode, String userCode) {
		final Object[] params = { corporationCode, userCode };
		return select(MwmDocTrayConfigPerson.class, getSql("DC0220_01"), params);
	}

	/** 操作者の選択可能な文書トレイ設定 */
	public List<MwmDocTrayConfig> getDocTrayConfigsAccessible(
			String corporationCode, String userCode, String localeCode, String trayType) {
		final Object[] params = {
				localeCode,
				corporationCode,
				userCode,
		};
		return select(MwmDocTrayConfig.class, getSql("DC0220_02"), params);
	}

	/** 文書トレイ設定個人マスタをインサート */
	public void insert(String corporationCode, String userCode, Dc0200Entity input) {
		final long docTrayConfigPersonalizeId = numbering.newPK(MwmDocTrayConfigPerson.class);
		final MwmDocTrayConfigPerson p = new MwmDocTrayConfigPerson();
		p.setDocTrayConfigPersonalizeId(docTrayConfigPersonalizeId);
		p.setCorporationCode(corporationCode);
		p.setUserCode(userCode);
		p.setDocTrayType(input.docTrayType);
		p.setDocTrayConfigId(input.docTrayConfigId);
		p.setDeleteFlag(DeleteFlag.OFF);

		log.debug("docTrayConfigPersonalizeId={} corporationCode={} userCode={} docTrayType={} docTrayConfigId={}",
				p.getDocTrayConfigPersonalizeId(), p.getCorporationCode(), p.getUserCode(), p.getDocTrayType(), p.getDocTrayConfigId());
		em.persist(p);
	}

	/** 文書トレイ設定個人マスタを削除 */
	public void remove(Long docTrayConfigPersonalizeId) {
		final MwmDocTrayConfigPerson p = em.find(MwmDocTrayConfigPerson.class, docTrayConfigPersonalizeId);
		em.remove(p);
	}
}
