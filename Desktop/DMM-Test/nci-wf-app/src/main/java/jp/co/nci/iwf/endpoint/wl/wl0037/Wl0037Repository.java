package jp.co.nci.iwf.endpoint.wl.wl0037;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class Wl0037Repository extends BaseRepository {

	public Wl0037Entity getEntity(String corporationCode, Long processId) {
		final Object[] params = { corporationCode, processId };
		return selectOne(Wl0037Entity.class, getSql("WL0037_01"), params);
	}

}