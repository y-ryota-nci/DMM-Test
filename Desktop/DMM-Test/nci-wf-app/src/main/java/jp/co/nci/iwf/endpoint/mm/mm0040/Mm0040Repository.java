package jp.co.nci.iwf.endpoint.mm.mm0040;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 文書管理項目名称設定のリポジトリ
 */
@ApplicationScoped
public class Mm0040Repository extends BaseRepository {

	/** 文書管理項目名称一覧を返す */
	public List<MwmDocBusinessInfoName> getBusinessInfoNames(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmDocBusinessInfoName.class, getSql("MM0040_01"), params);
	}

	/** 文書管理項目名称を更新. */
	public void update(MwmDocBusinessInfoName org, MwmDocBusinessInfoName input) {
		org.setDocBusinessInfoName(input.getDocBusinessInfoName());
		org.setValidFlag(input.getValidFlag());
		org.setScreenPartsInputFlag(input.getScreenPartsInputFlag());
		org.setDataType(input.getDataType());
		org.setSortOrder(input.getSortOrder());
		em.merge(org);
	}

}
