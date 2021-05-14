package jp.co.nci.iwf.endpoint.ti.ti0052;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

/**
 * 汎用テーブル検索条件カラム設定リポジトリ
 */
@ApplicationScoped
public class Ti0052Repository extends BaseRepository {


	public List<MwmOption> getConditionOptions() {
		final LoginInfo login = LoginInfo.get();
		final Object[] params = { login.getLocaleCode(), login.getCorporationCode() };
		return select(MwmOption.class, getSql("TI0052_01"), params);
	}
}
