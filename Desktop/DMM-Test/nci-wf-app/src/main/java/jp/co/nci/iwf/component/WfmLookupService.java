package jp.co.nci.iwf.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * WFルックアップサービス
 */
@BizLogic
public class WfmLookupService extends BaseService {
	@Inject
	private WfInstanceWrapper wf;

	/**
	 * WFルックアップを検索し、その結果をOptionItem化
	 * @param emptyLine 先頭に空行を追加するならtrue
	 * @param lookupTypeCode ルックアップ種別コード
	 * @return
	 */
	public List<OptionItem> getOptionItems(boolean emptyLine, String lookupTypeCode) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = localeService.getLocaleCode();
		final List<WfmLookup> list = getLookup(corporationCode, lookupTypeCode, null, DeleteFlag.OFF, localeCode);
		return toOptionItemList(emptyLine, list);
	}

	/**
	 * WFルックアップを検索し、その結果をOptionItem化
	 * @param emptyLine 先頭に空行を追加するならtrue
	 * @param corporationCode 企業コード
	 * @param lookupTypeCode ルックアップ種別コード
	 * @param lookupCodes ルックアップコードの絞り込み用配列
	 * @return
	 */
	public List<OptionItem> getOptionItems(boolean emptyLine, String corporationCode, String lookupTypeCode, String...lookupCodes) {
		final String localeCode = localeService.getLocaleCode();
		final List<WfmLookup> list = getLookup(corporationCode, lookupTypeCode, lookupCodes, DeleteFlag.OFF, localeCode);
		return toOptionItemList(emptyLine, list);
	}

	/**
	 * 名称を取得
	 * @param lookupTypeCode
	 * @param lookupCode
	 * @return
	 */
	public String getName(String lookupTypeCode, String lookupCode) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = localeService.getLocaleCode();
		final String[] lookupCodes = { lookupCode };
		final List<WfmLookup> list = getLookup(corporationCode, lookupTypeCode, lookupCodes, DeleteFlag.OFF, localeCode);
		for (WfmLookup e : list) {
			if (eq(lookupCode, e.getLookupCode())) {
				return e.getLookupName();
			}
		}
		return null;
	}

	/**
	 * WFルックアップリストをOptionItemリストへ
	 * @param list
	 * @return
	 */
	private List<OptionItem> toOptionItemList(boolean emptyLine, List<WfmLookup> list) {
		final List<OptionItem> items = new ArrayList<>(list.size() + 1);
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}
		for (WfmLookup e : list) {
			items.add(new OptionItem(e.getValue(), e.getLookupName()));
		}
		return items;
	}

	/**
	 * WFルックアップを抽出
	 * @param corporationCode
	 * @param lookupTypeCode
	 * @param lookupCodes
	 * @param deleteFlag
	 * @param localeCode
	 * @return
	 */
	private List<WfmLookup> getLookup(String corporationCode, String lookupTypeCode, String[] lookupCodes, String deleteFlag, String localeCode) {
		final SearchWfmLookupInParam in = new SearchWfmLookupInParam();
		in.setCorporationCode(corporationCode);
		in.setLookupTypeCode(lookupTypeCode);
		in.setDeleteFlag(deleteFlag);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmLookup.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmLookup.LOOKUP_TYPE_CODE),
				new OrderBy(true, "A." + WfmLookup.SORT_ORDER),
				new OrderBy(true, "A." + WfmLookup.LOOKUP_CODE),
				new OrderBy(true, "A." + WfmLookup.LOCALE_CODE),
		});
		wf.setLocale(localeCode);
		final List<WfmLookup> list = wf.searchWfmLookup(in).getWfmLookups();

		if (lookupCodes == null || lookupCodes.length == 0)
			return list;

		Set<String> filter = new HashSet<>(Arrays.asList(lookupCodes));
		return list.stream()
			.filter(e -> filter.contains(e.getLookupCode()))
			.collect(Collectors.toList());
	}
}
