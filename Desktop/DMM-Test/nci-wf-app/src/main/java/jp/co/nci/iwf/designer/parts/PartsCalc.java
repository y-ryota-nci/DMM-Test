package jp.co.nci.iwf.designer.parts;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 計算式.
 */
public class PartsCalc extends MiscUtils {

	/** デフォルトコンストラクタ */
	public PartsCalc() {
	}

	/**
	 * コンストラクタ.
	 */
	public PartsCalc(MwmPartsCalc c) {
		partsCalcId = c.getPartsCalcId();
		partsId = c.getPartsId();
		partsCalcName = c.getPartsCalcName();
		sortOrder = c.getSortOrder();
		defaultFlag = c.getDefaultFlag();
		identifyKey = c.getIdentifyKey();
		callbackFunction = c.getCallbackFunction();
	}

	/**
	 * コンストラクタ.
	 */
	public PartsCalc(MwmScreenCalc c) {
		screenCalcId = c.getScreenCalcId();
		screenId = c.getScreenId();
		partsId = c.getPartsId();
		partsCalcName = c.getPartsCalcName();
		sortOrder = c.getSortOrder();
		defaultFlag = c.getDefaultFlag();
		identifyKey = c.getIdentifyKey();
		callbackFunction = c.getCallbackFunction();
	}

	/** パーツ計算式ID */
	public Long partsCalcId;
	/** 画面計算式ID */
	public Long screenCalcId;
	/** 画面ID */
	public Long screenId;
	/** パーツID */
	public Long partsId;
	/** パーツ計算式名 */
	public String partsCalcName;
	/** 並び順 */
	public Integer sortOrder;
	/** デフォルトフラグ（1:デフォルト 0:デフォルト外） */
	public String defaultFlag;
	/** パーツ計算項目定義一覧 */
	public List<PartsCalcItem> items = new ArrayList<>();
	/** パーツ計算式有効条件定義一覧 */
	public List<PartsCalcEc> ecs = new ArrayList<>();
	/** 同定キー */
	public String identifyKey;
	/** コールバック関数 */
	public String callbackFunction;
}
