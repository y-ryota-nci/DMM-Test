package jp.co.nci.iwf.endpoint.dc.dc0062;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaItem;

/**
 * 拡張項目登録リポジトリ.
 */
@ApplicationScoped
public class Dc0062Repository extends BaseRepository {

	@Inject private NumberingService numbering;

	/** メタ項目取得. */
	public MwmMetaItem getByPk(long metaId) {
		return em.find(MwmMetaItem.class, metaId);
	}

	/** メタ項目取得. */
	public MwmMetaItem get(String corporationCode, String metaCode) {
		final Object[] params = { corporationCode, metaCode };
		return selectOne(MwmMetaItem.class, getSql("DC0062_01"), params);
	}

	/**
	 * メタ項目登録.
	 * @param inputed
	 */
	public MwmMetaItem insert(MwmMetaItem inputed) {
		inputed.setMetaId(numbering.newPK(MwmMetaItem.class));
		inputed.setRequiredFlag(isEmpty(inputed.getRequiredFlag()) ? CommonFlag.OFF : CommonFlag.ON);
		inputed.setDeleteFlag(DeleteFlag.OFF);
		em.persist(inputed);
		em.flush();
		return inputed;
	}

	/**
	 * メタ項目更新.
	 * @param org
	 * @param inputed
	 */
	public MwmMetaItem update(MwmMetaItem org, MwmMetaItem inputed) {
		org.setMetaName(inputed.getMetaName());
		org.setRequiredFlag(isEmpty(inputed.getRequiredFlag()) ? CommonFlag.OFF : CommonFlag.ON);
		org.setMaxLengths(inputed.getMaxLengths());
		org.setInitialValue1(inputed.getInitialValue1());
		org.setInitialValue2(inputed.getInitialValue2());
		org.setInitialValue3(inputed.getInitialValue3());
		org.setInitialValue4(inputed.getInitialValue4());
		org.setInitialValue5(inputed.getInitialValue5());
		org.setDeleteFlag(inputed.getDeleteFlag());
		em.merge(org);
		em.flush();
		return org;
	}
}
