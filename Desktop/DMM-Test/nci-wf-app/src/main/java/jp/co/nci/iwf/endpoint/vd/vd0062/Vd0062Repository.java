package jp.co.nci.iwf.endpoint.vd.vd0062;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 選択肢マスタ登録リポジトリ.
 */
@ApplicationScoped
public class Vd0062Repository extends BaseRepository {
	@Inject
	private NumberingService numbering;

	/** 選択肢マスタをPKで抽出 */
	public MwmOption get(Long optionId) {
		if (optionId == null)
			return null;
		return em.find(MwmOption.class, optionId);
	}

	/** 選択肢IDをキーに選択肢項目マスタの抽出 */
	public List<MwmOptionItem> getMwmOptionItems(Long optionId, String localCode) {
		final Object[] params = { localCode, optionId };
		return select(MwmOptionItem.class, getSql("VD0062_01"), params);
	}

	/** 選択肢項目マスタを選択肢要素IDをキーにMapで抽出. */
	public Map<Long, MwmOptionItem> getMwmOptionItemMap(Long optionId, String localCode) {
		return getMwmOptionItems(optionId, localCode)
				.stream()
				.collect(Collectors.toMap(MwmOptionItem::getOptionItemId, i -> i));
	}

	/** 選択肢マスタの更新 */
	public void update(MwmOption inputed, MwmOption org) {
		org.setOptionName(inputed.getOptionName());
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/**
	 * 選択肢項目マスタの登録.
	 * @param inputed
	 * @param optionId
	 * @return 選択肢項目ID
	 */
	public long insert(MwmOptionItem inputed, Long optionId) {
		// 選択肢項目IDの採番
		long optionItemId = numbering.newPK(MwmOptionItem.class);

		final MwmOptionItem org = new MwmOptionItem();
		org.setOptionItemId(optionItemId);
		org.setOptionId(optionId);
		org.setCode(inputed.getCode());
		org.setLabel(inputed.getLabel());
		org.setSortOrder(inputed.getSortOrder());
		org.setDeleteFlag(DeleteFlag.OFF);
		em.persist(org);

		return optionItemId;
	}

	/**
	 * 選択肢項目マスタの更新.
	 * @param inputed
	 * @param org
	 */
	public void update(MwmOptionItem inputed, MwmOptionItem org) {
		org.setCode(inputed.getCode());
		org.setLabel(inputed.getLabel());
		org.setSortOrder(inputed.getSortOrder());
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 削除されたMWM_OPTION_ITEMを一括削除 */
	public void delete(Set<Long> deleteOptionItemIds) {
		// パーツ計算式ID単位で削除
		if (!deleteOptionItemIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deleteOptionItemIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0062_02"));
			sql.append(toInListSql("OPTION_ITEM_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 選択肢を抽出 */
	public MwmOptionItem getMwmOptionItem(Long optionId, String code) {
		List<MwmOptionItem> items = getMwmOptionItems(optionId, LoginInfo.get().getLocaleCode());
		return items.stream()
			.filter(item -> eq(item.getCode(), code))
			.findFirst()
			.orElse(null);
	}
}
