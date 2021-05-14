package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.ex.MwmScreenPartsCondEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;

/**
 * 画面定義関連の読込リポジトリ
 */
@ApplicationScoped
public class ScreenLoadRepository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	/** 画面定義 */
	public MwmScreen get(long screenId, String localeCode) {
		final Object[] params = { localeCode, screenId };
		return selectOne(MwmScreen.class, getSql("VD0031_05"), params);
	}

	/**
	 * 画面定義に紐付く画面パーツ条件定義一覧を抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwmScreenPartsCond> getMwmScreenPartsCondList(long screenId) {
		return getMwmScreenPartsCondList(screenId, MwmScreenPartsCond.class);
	}

	/**
	 * 画面定義に紐づく画面パーツ条件定義をパーツIDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, List<MwmScreenPartsCondEx>> getMwmScreenPartsCondMap(long screenId) {
		return getMwmScreenPartsCondList(screenId, MwmScreenPartsCondEx.class)
				.stream()
				.collect(Collectors.groupingBy(MwmScreenPartsCondEx::getPartsId));
	}

	private <E extends BaseJpaEntity> List<E> getMwmScreenPartsCondList(long screenId, Class<E> clazz) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(screenId);
		return select(clazz, getSql("VD0031_06"), params.toArray());
	}

	/**
	 * 画面定義に紐づく画面パーツ条件項目定義一覧を抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwmScreenPartsCondItem> getMwmScreenPartsCondItemList(long screenId) {
		final List<Object> params = new ArrayList<>();
		params.add(screenId);
		return select(MwmScreenPartsCondItem.class, getSql("VD0031_07"), params.toArray());
	}

	/**
	 * 画面定義に紐づく画面パーツ条件項目定義を画面パーツ条件IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, List<MwmScreenPartsCondItem>> getMwmScreenPartsCondItemMap(long screenId) {
		return getMwmScreenPartsCondItemList(screenId)
				.stream()
				.collect(Collectors.groupingBy(MwmScreenPartsCondItem::getScreenPartsCondId));
	}

	/**
	 * 画面定義に紐づく画面パーツ条件項目定義を画面パーツ条件項目IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, MwmScreenPartsCondItem> getMwmScreenPartsCondItemMapByItemId(long screenId) {
		return getMwmScreenPartsCondItemList(screenId)
				.stream()
				.collect(Collectors.toMap(MwmScreenPartsCondItem::getScreenPartsCondItemId, e -> e));
	}

	/**
	 * 画面定義に紐付く画面計算式定義一覧を抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwmScreenCalc> getMwmScreenCalcList(long screenId) {
		final List<Object> params = new ArrayList<>();
		params.add(screenId);
		return select(MwmScreenCalc.class, getSql("VD0031_08"), params.toArray());
	}

	/**
	 * 画面定義に紐づく画面計算式定義をパーツIDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, List<MwmScreenCalc>> getMwmScreenCalcMap(long screenId) {
		return getMwmScreenCalcList(screenId)
				.stream()
				.collect(Collectors.groupingBy(MwmScreenCalc::getPartsId));
	}

	/**
	 * 画面定義に紐付く画面計算式定義を画面計算式IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, MwmScreenCalc> getMwmScreenCalcMapByCalcId(long screenId) {
		return getMwmScreenCalcList(screenId)
				.stream()
				.collect(Collectors.toMap(MwmScreenCalc::getScreenCalcId, e -> e));
	}

	/**
	 * 画面定義に紐づく画面計算式項目定義一覧を抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwmScreenCalcItem> getMwmScreenCalcItemList(long screenId) {
		final List<Object> params = new ArrayList<>();
		params.add(screenId);
		return select(MwmScreenCalcItem.class, getSql("VD0031_10"), params.toArray());
	}

	/**
	 * 画面定義に紐づく画面計算式項目定義を画面計算式IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, List<MwmScreenCalcItem>> getMwmScreenCalcItemMap(long screenId) {
		return getMwmScreenCalcItemList(screenId)
				.stream()
				.collect(Collectors.groupingBy(MwmScreenCalcItem::getScreenCalcId));
	}

	/**
	 * 画面定義に紐づく画面計算式項目定義を画面計算項目IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, MwmScreenCalcItem> getMwmScreenCalcItemMapByItemId(long screenId) {
		return getMwmScreenCalcItemList(screenId)
				.stream()
				.collect(Collectors.toMap(MwmScreenCalcItem::getScreenCalcItemId, e -> e));
	}

	/**
	 * 画面定義に紐づく画面計算式有効条件定義一覧を抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public List<MwmScreenCalcEc> getMwmScreenCalcEcList(long screenId) {
		final List<Object> params = new ArrayList<>();
		params.add(screenId);
		return select(MwmScreenCalcEc.class, getSql("VD0031_12"), params.toArray());
	}

	/**
	 * 画面定義に紐づく画面ツ計算式有効条件定義を画面計算式IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, List<MwmScreenCalcEc>> getMwmScreenCalcEcMap(long screenId) {
		return getMwmScreenCalcEcList(screenId)
				.stream()
				.collect(Collectors.groupingBy(MwmScreenCalcEc::getScreenCalcId));
	}

	/**
	 * 画面定義に紐づく画面計算式有効条件定義を画面計算式有効条件IDをKeyにMapとして抽出
	 * @param screenId 画面ID
	 * @return
	 */
	public Map<Long, MwmScreenCalcEc> getMwmScreenCalcEcMapByEcId(long screenId) {
		return getMwmScreenCalcEcList(screenId)
				.stream()
				.collect(Collectors.toMap(MwmScreenCalcEc::getScreenCalcEcId, e -> e));
	}
}
