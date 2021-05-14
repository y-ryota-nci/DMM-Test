package jp.co.nci.iwf.designer.parts;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 計算式項目.
 */
public class PartsCalcItem extends MiscUtils {

	/** デフォルトコンストラクタ. */
	public PartsCalcItem() {
	}

	/**
	 * コンストラクタ
	 */
	public PartsCalcItem(MwmPartsCalcItem i) {
		partsCalcItemId = i.getPartsCalcItemId();
		partsCalcId = i.getPartsCalcId();
		calcItemType = i.getCalcItemType();
		calcItemValue = i.getCalcItemValue();
		sortOrder = i.getSortOrder();
		identifyKey = i.getIdentifyKey();
		forceCalcFlag = MiscUtils.eq(CommonFlag.ON, i.getForceCalcFlag());
	}

	/**
	 * コンストラクタ
	 */
	public PartsCalcItem(MwmScreenCalcItem i) {
		screenCalcItemId = i.getScreenCalcItemId();
		screenCalcId = i.getScreenCalcId();
		calcItemType = i.getCalcItemType();
		calcItemValue = i.getCalcItemValue();
		sortOrder = i.getSortOrder();
		identifyKey = i.getIdentifyKey();
		forceCalcFlag = MiscUtils.eq(CommonFlag.ON, i.getForceCalcFlag());
	}

	/** パーツ計算項目ID */
	public Long partsCalcItemId;
	/** 画面計算項目ID */
	public Long screenCalcItemId;
	/** パーツ計算式ID */
	public Long partsCalcId;
	/** 画面計算式ID */
	public Long screenCalcId;
	/** 計算項目区分 */
	public String calcItemType;
	/** 計算項目値 */
	public String calcItemValue;
	/** 並び順 */
	public Integer sortOrder;
	/** 同定キー */
	public String identifyKey;
	/** 強制計算フラグ（ブランクでもゼロとして強制計算） */
	public boolean forceCalcFlag;
}
