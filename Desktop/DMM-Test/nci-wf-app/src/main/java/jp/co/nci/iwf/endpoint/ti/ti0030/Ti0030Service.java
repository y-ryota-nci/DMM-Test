package jp.co.nci.iwf.endpoint.ti.ti0030;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;

/**
 * 汎用テーブル設定一覧サービス
 */
@BizLogic
public class Ti0030Service extends BasePagingService {
	@Inject private Ti0030Repository repository;
	@Inject private MwmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0030InitResponse init(BaseRequest req) {
		final Ti0030InitResponse res = createResponse(Ti0030InitResponse.class, req);
		res.entityTypes = lookup.getOptionItems(LookupGroupId.ENTITY_TYPE, true);
		res.categories = getCategoryOptionItems();
		res.success = true;
		return res;
	}

	private List<OptionItem> getCategoryOptionItems() {
		final String corporationCode = LoginInfo.get().getCorporationCode();
		final String localeCode = LoginInfo.get().getLocaleCode();
		List<MwmCategory> list = repository.getCategories(corporationCode, localeCode);
		List<OptionItem> items = list.stream()
				.map(c -> new OptionItem(c.getCategoryId(), c.getCategoryName()))
				.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ti0030Response search(Ti0030Request req) {
		final int allCount = repository.count(req);
		final Ti0030Response res = createResponse(Ti0030Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
