package jp.co.nci.iwf.endpoint.mm.mm0011;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップグループ登録リポジトリ
 */
@ApplicationScoped
public class Mm0011Repository extends BaseRepository implements CodeBook {

	/** インサート */
	public MwmLookupGroup insert(final MwmLookupGroup entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public MwmLookupGroup searchLookUpGroup(String corporationCode, String lookUpGroupId, String localeCode) {
		List<String> params = new ArrayList<String>();
		params.add(corporationCode);
		params.add(lookUpGroupId);
		params.add(localeCode);
		List<MwmLookupGroup> list = select(MwmLookupGroup.class, getSql("MM001201"), params.toArray());
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
}
