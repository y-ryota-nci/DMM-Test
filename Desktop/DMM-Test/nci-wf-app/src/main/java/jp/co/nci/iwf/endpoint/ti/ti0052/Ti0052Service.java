package jp.co.nci.iwf.endpoint.ti.ti0052;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

/**
 *  汎用テーブル検索条件カラム設定サービス
 */
@BizLogic
public class Ti0052Service extends BaseService {
	@Inject private MwmLookupService lookup;
	@Inject private Ti0052Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0052Response init(BaseRequest req) {
		Ti0052Response res = createResponse(Ti0052Response.class, req);

		// 選択肢
		res.searchColumnTypes = lookup.getOptionItems(LookupGroupId.SEARCH_COLUMN_TYPE, true);
		res.conditionDisplayTypes = lookup.getOptionItems(LookupGroupId.CONDITION_DISPLAY_TYPE, true);
		res.conditionInitTypes = lookup.getOptionItems(LookupGroupId.CONDITION_INIT_TYPE, true);
		res.conditionMatchTypes = lookup.getOptionItems(LookupGroupId.CONDITION_MATCH_TYPE, true);
		res.conditionOptions = getConditionOptions();
		res.conditionBlankTypes = lookup.getOptionItems(LookupGroupId.CONDITION_BLANK_TYPE, true);
		res.conditionKanaConvertTypes = lookup.getOptionItems(LookupGroupId.CONDITION_KANA_CONVERT_TYPE, true);
		res.conditionTrimFlags = lookup.getOptionItems(LookupGroupId.CONDITION_TRIM_FLAG, true);
		res.resultDisplayTypes = lookup.getOptionItems(LookupGroupId.RESULT_DISPLAY_TYPE, true);
		res.resultAlignTypes = lookup.getOptionItems(LookupGroupId.RESULT_ALIGN_TYPE, true);
		res.resultOrderByDirections = lookup.getOptionItems(LookupGroupId.RESULT_ORDER_BY_DIRECTION, true);
		res.positions = getPositions();
		res.colWidths = getColWidths();

		res.success = true;
		return res;
	}

	private List<OptionItem> getColWidths() {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		for (int i = 1; i <= 12; i++)  {
			String val = String.valueOf(i);
			items.add(new OptionItem(val, val));
		}
		return items;
	}

	private List<OptionItem> getPositions() {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		for (int i = 1; i <= 10; i++)  {
			String val = String.valueOf(i);
			items.add(new OptionItem(val, val));
		}
		return items;
	}

	private List<OptionItem> getConditionOptions() {
		List<MwmOption> list = repository.getConditionOptions();
		return list.stream()
				.map(o -> new OptionItem(o.getOptionId(), o.getOptionName()))
				.collect(Collectors.toList());
	}

}
