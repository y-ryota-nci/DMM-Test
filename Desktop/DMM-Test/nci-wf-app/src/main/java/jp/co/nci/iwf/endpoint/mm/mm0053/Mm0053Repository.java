package jp.co.nci.iwf.endpoint.mm.mm0053;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;

/**
 * 通し番号登録リポジトリ
 */
@ApplicationScoped
public class Mm0053Repository extends BaseRepository implements CodeBook {

	public MwmPartsSequenceSpec insert(final MwmPartsSequenceSpec entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmPartsSequenceSpec update(final MwmPartsSequenceSpec entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmPartsSequenceSpec delete(final MwmPartsSequenceSpec entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmPartsSequenceSpec getSequence(String corporationCode, String partsSequenceSpecCode, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(partsSequenceSpecCode);
		List<MwmPartsSequenceSpec> list = select(MwmPartsSequenceSpec.class, getSql("MM0053_01"), params.toArray());
		if (CommonUtil.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

}
