package jp.co.nci.iwf.endpoint.mm.mm0013;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップ登録リポジトリ
 */
@ApplicationScoped
public class Mm0013Repository extends BaseRepository implements CodeBook {

	@Transactional
	public MwmLookup insert(final MwmLookup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	@Transactional
	public MwmLookup update(final MwmLookup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	@Transactional
	public MwmLookup delete(final MwmLookup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmLookup getLookup(String corporationCode, String lookUpGroupId, String lookUpId, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(corporationCode);
		params.add(lookUpGroupId);
		params.add(lookUpId);
		params.add(localeCode);
		List<MwmLookup> list = select(MwmLookup.class, getSql("MM001302"), params.toArray());
		if (CommonUtil.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public MwmLookupGroup getLookupGroup(String corporationCode, String lookUpGroupId, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(corporationCode);
		params.add(lookUpGroupId);
		params.add(localeCode);
		List<MwmLookupGroup> list = select(MwmLookupGroup.class, getSql("MM001301"), params.toArray());
		if (CommonUtil.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

}
