package jp.co.nci.iwf.endpoint.mm.mm0030;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;

/**
 * 業務管理項目名称設定のリポジトリ
 */
@ApplicationScoped
public class Mm0030Repository extends BaseRepository {

	/** 業務管理項目名称一覧を返す */
	public List<MwmBusinessInfoName> getBusinessInfoNames(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmBusinessInfoName.class, getSql("MM0030_01"), params);
	}

	/** 業務管理項目名称をインサート */
	public void insert(MwmBusinessInfoName input) {
		em.persist(input);

		final List<String> params = new ArrayList<>();
		params.add(input.getDataType());
		params.add(input.getCorporationCode());
		params.add(input.getBusinessInfoCode());

		execSql(getSql("MM0030_02"), params.toArray());
	}

	/** パーツ表示条件を更新 */
	public void update(MwmBusinessInfoName input) {
		em.merge(input);

		final List<String> params = new ArrayList<>();
		params.add(input.getDataType());
		params.add(input.getCorporationCode());
		params.add(input.getBusinessInfoCode());

		execSql(getSql("MM0030_02"), params.toArray());
	}
}
