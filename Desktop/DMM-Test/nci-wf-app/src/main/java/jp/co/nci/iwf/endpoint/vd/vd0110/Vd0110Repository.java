package jp.co.nci.iwf.endpoint.vd.vd0110;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;

/**
 * 画面コンテナ設定のリポジトリ
 */
@ApplicationScoped
public class Vd0110Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	/**
	 * 表示条件リストを返す
	 * @param localeCode
	 * @return
	 */
	public List<OptionItem> getDcList(long containerId, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(containerId);
		return select(MwmDc.class, getSql("VD0110_03"), params.toArray())
				.stream()
				.map(dc -> new OptionItem(dc.getDcId(), dc.getDcName()))
				.collect(Collectors.toList());
	}
}
