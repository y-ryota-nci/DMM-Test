package jp.co.nci.iwf.designer.parts;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.jpa.entity.ex.MwmPartsCondEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmScreenPartsCondEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツ条件.
 */
public class PartsCond extends MiscUtils implements Comparable<PartsCond> {

	/** デフォルトコンストラクタ */
	public PartsCond() {
	}

	/** コンストラクタ */
	public PartsCond(MwmPartsCondEx c) {
		partsCondId = c.getPartsCondId();
		partsId = c.getPartsId();
		partsConditionType = c.getPartsConditionType();
		callbackFunction = c.getCallbackFunction();
		sortOrder = c.getSortOrder();
		partsConditionTypeName = c.getPartsConditionTypeName();
	}

	/** コンストラクタ */
	public PartsCond(MwmScreenPartsCondEx c) {
		screenPartsCondId = c.getScreenPartsCondId();
		screenId = c.getScreenId();
		partsId = c.getPartsId();
		partsConditionType = c.getPartsConditionType();
		callbackFunction = c.getCallbackFunction();
		sortOrder = c.getSortOrder();
		partsConditionTypeName = c.getPartsConditionTypeName();
	}

	/** コンストラクタ. */
	public PartsCond(MwmLookup l) {
		partsConditionType = l.getLookupId();
		sortOrder = l.getSortOrder();
		partsConditionTypeName = l.getLookupName();
	}

	@Override
	public int compareTo(PartsCond o) {
		return 0;
	}

	/** パーツ条件ID */
	public Long partsCondId;
	/** 画面パーツ条件ID */
	public Long screenPartsCondId;
	/** 画面ID */
	public Long screenId;
	/** パーツID */
	public Long partsId;
	/** パーツ条件区分（1:有効条件, 2:可視条件, 3:読取専用条件） */
	public String partsConditionType;
	/** コールバック関数 */
	public String callbackFunction;
	/** 並び順 */
	public Integer sortOrder;
	/** パーツ条件項目定義一覧 */
	public List<PartsCondItem> items = new ArrayList<>();
	/** パーツ条件区分名 */
	public String partsConditionTypeName;
	/** 条件式 */
	public String expression;
}
