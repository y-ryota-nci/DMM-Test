package jp.co.nci.iwf.designer.parts;

import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツ条件項目.
 */
public class PartsCondItem extends MiscUtils implements Comparable<PartsCondItem> {

	/** デフォルトコンストラクタ */
	public PartsCondItem() {
	}

	/** コンストラクタ */
	public PartsCondItem(MwmPartsCondItem c) {
		partsCondItemId = c.getPartsCondItemId();
		partsCondId = c.getPartsCondId();
		itemClass = c.getItemClass();
		condType = c.getCondType();
		operator = c.getOperator();
		targetLiteralVal = c.getTargetLiteralVal();
		targetPartsId = c.getTargetPartsId();
		numericFlag = c.getNumericFlag();
		sortOrder = c.getSortOrder();
		identifyKey = c.getIdentifyKey();
	}

	/** コンストラクタ */
	public PartsCondItem(MwmScreenPartsCondItem c) {
		screenPartsCondItemId = c.getScreenPartsCondItemId();
		screenPartsCondId = c.getScreenPartsCondId();
		itemClass = c.getItemClass();
		condType = c.getCondType();
		operator = c.getOperator();
		targetLiteralVal = c.getTargetLiteralVal();
		targetPartsId = c.getTargetPartsId();
		numericFlag = c.getNumericFlag();
		sortOrder = c.getSortOrder();
		identifyKey = c.getIdentifyKey();
	}

	@Override
	public int compareTo(PartsCondItem o) {
		return compareTo(sortOrder, o.sortOrder);
	}

	/** パーツ条件項目ID */
	public Long partsCondItemId;
	/** パーツ条件ID */
	public Long partsCondId;
	/** 画面パーツ条件項目ID */
	public Long screenPartsCondItemId;
	/** 画面パーツ条件ID */
	public Long screenPartsCondId;
	/** 項目クラス */
	public String itemClass;
	/** 条件区分 */
	public String condType;
	/** 比較演算子 */
	public String operator;
	/** 比較先リテラル値 */
	public String targetLiteralVal;
	/** 比較先パーツID */
	public Long targetPartsId;
	/** 数値化フラグ */
	public String numericFlag;
	/** 並び順 */
	public Integer sortOrder;
	/** 同定キー */
	public String identifyKey;

}
