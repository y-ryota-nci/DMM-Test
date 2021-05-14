package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsCondEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsChildHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;

/**
 * コンテナ単位のパーツ定義読み込みリポジトリ
 */
@ApplicationScoped
public class ContainerLoadRepository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	/** コンテナIDに紐付くコンテナ定義を抽出 */
	public MwmContainer getMwmContainer(Long containerId) {
		if (containerId == null)
			return null;
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(containerId);
		List<MwmContainer> list = select(MwmContainer.class, getSql("VD0110_01"), params.toArray());
		return (list.isEmpty() ? null : list.get(0));
	}

	/** コンテナIDに紐付くパーツ定義を抽出 */
	public List<MwmPart> getMwmPartsList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(containerId);
		return select(MwmPart.class, getSql("VD0110_02"), params.toArray());
	}

	/** パーツに紐付くパーツ子要素定義を抽出 */
	public MwmPartsChildHolder getMwmPartsChild(long partsId) {
		final List<Object> params = new ArrayList<>();
		params.add(partsId);
		final List<MwmPartsChildHolder> list = select(MwmPartsChildHolder.class, getSql("VD0110_04"), params.toArray());
		return (list.isEmpty() ? null : list.get(0));
	}

	/** コンテナIDに紐付くパーツ子要素定義をMapとして抽出 */
	public Map<Long, MwmPartsChildHolder> getMwmPartsChildMap(Long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsChildHolder.class, getSql("VD0110_05"), params.toArray())
				.stream()
				.collect(Collectors.toMap(MwmPartsChildHolder::getPartsId, ch -> ch));
	}

	/**
	 * コンテナに紐付くカラム定義をMapとして抽出
	 * @param containerId
	 * @return
	 */
	public List<MwmPartsColumn> getMwmPartsColumnList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsColumn.class, getSql("VD0110_06"), params.toArray());
	}

	/**
	 * コンテナに紐付くカラム定義をMapとして抽出
	 * @param containerId
	 * @return
	 */
	public Map<Long, List<MwmPartsColumn>> getMwmPartsColumnMap(long containerId) {
		return getMwmPartsColumnList(containerId)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsColumn::getPartsId, Collectors.toList()));
	}

	/** パーツカラム定義を返す */
	public MwmPartsColumn getMwmPartsColumn(Long containerId, String columnName) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		params.add(columnName);
		return select(MwmPartsColumn.class, getSql("VD0110_09"), params.toArray())
				.stream()
				.findFirst().orElse(null);
	}

	/** コンテナの循環参照チェック */
	public List<ContainerEndlessLoopEntity> getCircularReference(Long childContainerId) {
		final List<Object> params = new ArrayList<>();
		params.add(childContainerId);
		return select(ContainerEndlessLoopEntity.class, getSql("VD0110_10"), params.toArray());
	}

	/** コンテナIDをキーにパーツ関連定義を抽出し、パーツIDをキーにMpa化 */
	public Map<Long, List<MwmPartsRelation>> getMwmPartsRelationMap(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsRelation.class, getSql("VD0110_13"), params.toArray())
				.stream()
				.collect(Collectors.groupingBy(MwmPartsRelation::getPartsId));
	}

	/**
	 * コンテナに紐付くパーツ計算式定義一覧を抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public List<MwmPartsCalc> getMwmPartsCalcList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsCalc.class, getSql("VD0110_15"), params.toArray());
	}

	/**
	 * コンテナに紐づくパーツ計算式定義をパーツIDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmPartsCalc>> getMwmPartsCalcMap(long containerId) {
		return getMwmPartsCalcList(containerId)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsCalc::getPartsId));
	}

	/**
	 * コンテナに紐付くパーツ計算式定義を計算式IDをKeyにMapとして抽出
	 *
	 */
	public Map<Long, MwmPartsCalc> getMwmPartsCalcMapByCalcId(long containerId) {
		return getMwmPartsCalcList(containerId)
				.stream()
				.collect(Collectors.toMap(MwmPartsCalc::getPartsCalcId, e -> e));
	}

	/**
	 * コンテナに紐づくパーツ計算式項目定義一覧を抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public List<MwmPartsCalcItem> getMwmPartsCalcItemList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsCalcItem.class, getSql("VD0110_17"), params.toArray());
	}

	/**
	 * コンテナに紐づくパーツ計算式項目定義をパーツ計算式IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmPartsCalcItem>> getMwmPartsCalcItemMap(long containerId) {
		return getMwmPartsCalcItemList(containerId)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsCalcItem::getPartsCalcId));
	}

	/**
	 * コンテナに紐づくパーツ計算式項目定義をパーツ計算項目IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, MwmPartsCalcItem> getMwmPartsCalcItemMapByItemId(long containerId) {
		return getMwmPartsCalcItemList(containerId)
				.stream()
				.collect(Collectors.toMap(MwmPartsCalcItem::getPartsCalcItemId, e -> e));
	}

	/**
	 * コンテナに紐づくパーツ計算式有効条件定義一覧を抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public List<MwmPartsCalcEc> getMwmPartsCalcEcList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsCalcEc.class, getSql("VD0110_19"), params.toArray());
	}

	/**
	 * コンテナに紐づくパーツ計算式有効条件定義をパーツ計算式IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmPartsCalcEc>> getMwmPartsCalcEcMap(long containerId) {
		return getMwmPartsCalcEcList(containerId)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsCalcEc::getPartsCalcId));
	}

	/**
	 * コンテナに紐づくパーツ計算式有効条件定義をパーツ計算式有効条件IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, MwmPartsCalcEc> getMwmPartsCalcEcMapByEcId(long containerId) {
		return getMwmPartsCalcEcList(containerId)
				.stream()
				.collect(Collectors.toMap(MwmPartsCalcEc::getPartsCalcEcId, e -> e));
	}

	/**
	 * パーツIDに紐付くパーツ計算式定義の計算式IDを抽出
	 */
	public Set<Long> getPartsCalcIdListByPartsId(Set<Long> partsIds) {
		final List<Object> params = new ArrayList<>();
		params.addAll(partsIds);
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("VD0110_21"));
		sql.append(toInListSql("PARTS_ID", partsIds.size()));
		return select(MwmPartsCalc.class, sql.toString(), params.toArray())
				.stream()
				.map(MwmPartsCalc::getPartsCalcId)
				.collect(Collectors.toSet());
	}

	/**
	 * コンテナに紐付くコンテナJavascript定義を抽出
	 * @param containerId
	 * @return
	 */
	public List<MwmContainerJavascript> getMwmContainerJavascript(long containerId) {
		final Object[] params = { containerId };
		return select(MwmContainerJavascript.class, getSql("VD0110_22"), params);
	}

	/**
	 * コンテナに紐付くパーツ選択肢定義をパーツIDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, MwmPartsOption> getMwmPartsOptionMap(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsOption.class, getSql("VD0110_23"), params.toArray())
				.stream()
				.collect(Collectors.toMap(MwmPartsOption::getPartsId, o -> o));
	}

	/**
	 * コンテナに紐付くパーツ選択肢定義の選択肢IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmOptionItem>> getMwmOptionItemMap(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(containerId);
		return select(MwmOptionItem.class, getSql("VD0110_24"), params.toArray())
				.stream()
				.collect(Collectors.groupingBy(MwmOptionItem::getOptionId));
	}

	/**
	 * コンテナに紐付くパーツ汎用テーブル情報を抽出
	 * @param containerId
	 * @return
	 */
	public Map<Long, MwmPartsTableInfo> getMwmPartsTableInfo(long containerId) {
		final Object[] params = { containerId };
		return select(MwmPartsTableInfo.class, getSql("VD0110_25"), params)
				.stream()
				.collect(Collectors.toMap(ti -> ti.getPartsId(), ti -> ti));
	}

	/**
	 * コンテナに紐付く添付ファイル情報
	 * @param containerId
	 * @return
	 */
	public Map<Long, List<MwmPartsAttachFile>> getMwmPartsAttachFile(Long containerId) {
		final Object[] params = { containerId };
		return select(MwmPartsAttachFile.class, getSql("VD0110_26"), params)
				.stream()
				.collect(Collectors.groupingBy(ti -> ti.getPartsId()));
	}

	/**
	 * パーツイベント定義
	 * @param containerId
	 * @return
	 */
	public Map<Long, List<MwmPartsEvent>> getMwmPartsEvent(long containerId) {
		final Object[] params = { containerId };
		return select(MwmPartsEvent.class, getSql("VD0110_27"), params)
				.stream()
				.collect(Collectors.groupingBy(ti -> ti.getPartsId()));
	}

	/**
	 * コンテナに紐付くパーツ条件定義一覧を抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public List<MwmPartsCond> getMwmPartsCondList(long containerId) {
		return getMwmPartsCondList(containerId, MwmPartsCond.class);
	}

	/**
	 * コンテナに紐づくパーツ条件定義をパーツIDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmPartsCondEx>> getMwmPartsCondMap(long containerId) {
		return getMwmPartsCondList(containerId, MwmPartsCondEx.class)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsCondEx::getPartsId));
	}

	private <E extends BaseJpaEntity> List<E> getMwmPartsCondList(long containerId, Class<E> clazz) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(containerId);
		return select(clazz, getSql("VD0110_29"), params.toArray());
	}

	/**
	 * コンテナに紐づくパーツ条件項目定義一覧を抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public List<MwmPartsCondItem> getMwmPartsCondItemList(long containerId) {
		final List<Object> params = new ArrayList<>();
		params.add(containerId);
		return select(MwmPartsCondItem.class, getSql("VD0110_31"), params.toArray());
	}

	/**
	 * コンテナに紐づくパーツ条件項目定義をパーツ条件IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, List<MwmPartsCondItem>> getMwmPartsCondItemMap(long containerId) {
		return getMwmPartsCondItemList(containerId)
				.stream()
				.collect(Collectors.groupingBy(MwmPartsCondItem::getPartsCondId));
	}

	/**
	 * コンテナに紐づくパーツ条件項目定義をパーツ条件項目IDをKeyにMapとして抽出
	 * @param containerId コンテナID
	 * @return
	 */
	public Map<Long, MwmPartsCondItem> getMwmPartsCondItemMapByItemId(long containerId) {
		return getMwmPartsCondItemList(containerId)
				.stream()
				.collect(Collectors.toMap(MwmPartsCondItem::getPartsCondItemId, e -> e));
	}

	/**
	 * パーツIDに紐付くパーツ条件定義のパーツ条件IDを抽出
	 */
	public Set<Long> getPartsCondIdListByPartsId(Set<Long> partsIds) {
		final List<Object> params = new ArrayList<>();
		params.addAll(partsIds);
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("VD0110_33"));
		sql.append(toInListSql("PARTS_ID", partsIds.size()));
		return select(MwmPartsCond.class, sql.toString(), params.toArray())
				.stream()
				.map(MwmPartsCond::getPartsCondId)
				.collect(Collectors.toSet());
	}
}
