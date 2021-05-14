package jp.co.dmm.customize.component.catalog;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogCategory;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogUnit;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

@ApplicationScoped
public class CatalogRepository extends BaseRepository {

	public List<MwmCatalogCategory> getMwmCatalogCategories(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmCatalogCategory.class, getSql("CT0000_01"), params);
	}

	public List<MwmCatalogUnit> getMwmCatalogUnits(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmCatalogUnit.class, getSql("CT0000_02"), params);
	}

	public List<MwmOptionItem> getMwmOptionItems(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmOptionItem.class, getSql("CT0000_03"), params);
	}

	public MwmCatalogImage getMwmCatalogImageByCatalogId(Long catalogId) {
		if (catalogId == null) {
			return null;
		}
		final Object[] params = { catalogId };
		return selectOne(MwmCatalogImage.class, getSql("CT0000_04"), params);
	}

	public MwmCatalogImage getMwmCatalogImageByPK(Long catalogImageId) {
		if (catalogImageId == null) {
			return null;
		}
		return em.find(MwmCatalogImage.class, catalogImageId);
	}

}
