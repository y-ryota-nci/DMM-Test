package jp.co.nci.iwf.endpoint.vd.vd0061;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

/**
 * 選択肢マスタ登録リポジトリ.
 */
@ApplicationScoped
public class Vd0061Repository extends BaseRepository {
	@Inject
	private NumberingService number;

	/** 選択肢マスタをインサート */
	public long insert(MwmOption o) {
		// 選択肢IDを採番
		final long optionId = number.newPK(MwmOption.class);
		final MwmOption entity = new MwmOption();
		entity.setOptionId(optionId);
		entity.setOptionCode(o.getOptionCode());
		entity.setOptionName(o.getOptionName());
		entity.setCorporationCode(o.getCorporationCode());
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
		return optionId;
	}

	/** 選択肢マスタをPKで抽出 */
	public MwmOption get(Long optionId) {
		if (optionId == null)
			return null;
		return em.find(MwmOption.class, optionId);
	}

	/** 選択肢マスタに同一の選択肢コードがあるか. */
	public boolean isDuplicate(String optionCode, String corporationCode) {
		final Object[] params = { optionCode, corporationCode };
		return (select(MwmOption.class, getSql("VD0061_01"), params).size() > 0);
	}
}
