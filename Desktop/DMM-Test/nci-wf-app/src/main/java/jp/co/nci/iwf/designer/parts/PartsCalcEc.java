package jp.co.nci.iwf.designer.parts;

import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;

/**
 * 計算式有効条件.
 */
public class PartsCalcEc extends PartsCondItem {

	/** デフォルトコンストラクタ */
	public PartsCalcEc() {
	}

	/** コンストラクタ */
	public PartsCalcEc(MwmPartsCalcEc c) {
		partsCalcEcId = c.getPartsCalcEcId();
		partsCalcId = c.getPartsCalcId();
//		ecClass = c.getEcClass();
//		ecType = c.getEcType();
//		ecOperator = c.getEcOperator();
		itemClass = c.getEcClass();
		condType = c.getEcType();
		operator = c.getEcOperator();
		targetLiteralVal = c.getTargetLiteralVal();
		targetPartsId = c.getTargetPartsId();
		numericFlag = c.getNumericFlag();
		sortOrder = c.getSortOrder();
		identifyKey = c.getIdentifyKey();
	}

	/** コンストラクタ */
	public PartsCalcEc(MwmScreenCalcEc c) {
		screenCalcEcId = c.getScreenCalcEcId();
		screenCalcId = c.getScreenCalcId();
//		ecClass = c.getEcClass();
//		ecType = c.getEcType();
//		ecOperator = c.getEcOperator();
		itemClass = c.getEcClass();
		condType = c.getEcType();
		operator = c.getEcOperator();
		targetLiteralVal = c.getTargetLiteralVal();
		targetPartsId = c.getTargetPartsId();
		numericFlag = c.getNumericFlag();
		sortOrder = c.getSortOrder();
		identifyKey = c.getIdentifyKey();
	}

	/** パーツ計算式有効条件ID */
	public Long partsCalcEcId;
	/** パーツ計算式ID */
	public Long partsCalcId;
	/** 画面計算式有効条件ID */
	public Long screenCalcEcId;
	/** 画面計算式ID */
	public Long screenCalcId;
	/** 同定キー */
	public String identifyKey;
}
