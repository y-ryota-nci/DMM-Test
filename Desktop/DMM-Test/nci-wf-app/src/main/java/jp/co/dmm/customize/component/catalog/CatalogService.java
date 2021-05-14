package jp.co.dmm.customize.component.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseService;

@BizLogic
public class CatalogService extends BaseService {

	@Inject
	protected CatalogRepository repository;

	public List<OptionItem> getCatalogCategories(boolean emptyLine) {

		final List<OptionItem> items = new ArrayList<>();
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}
		items.addAll(repository.getMwmCatalogCategories(sessionHolder.getLoginInfo().getCorporationCode())
				.stream()
				.map(c -> new OptionItem(c.getCatalogCategoryId(), c.getCatalogCategoryName()))
				.collect(Collectors.toList()));
		return items;
	}

	public List<OptionItem> getCatalogUnits(boolean emptyLine) {
		final List<OptionItem> items = new ArrayList<>();
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}
		items.addAll(repository.getMwmCatalogUnits(sessionHolder.getLoginInfo().getCorporationCode())
				.stream()
				.map(u -> new OptionItem(u.getCatalogUnitId(), u.getCatalogUnitName()))
				.collect(Collectors.toList()));
		return items;
	}

	public List<OptionItem> getSalesTaxTypes(boolean emptyLine) {
		final List<OptionItem> items = new ArrayList<>();
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}
		items.addAll(repository.getMwmOptionItems(sessionHolder.getLoginInfo().getCorporationCode(), sessionHolder.getLoginInfo().getLocaleCode())
				.stream()
				.map(u -> new OptionItem(u.getCode(), u.getLabel()))
				.collect(Collectors.toList()));
		return items;
	}

	public MwmCatalogImage getCatalogImage(Long catalogImageId) {
		return repository.getMwmCatalogImageByPK(catalogImageId);
	}

	public MwmCatalogImage getCatalogImageByCatalogId(Long catalogId) {
		return repository.getMwmCatalogImageByCatalogId(catalogId);
	}
}
