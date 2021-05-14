package jp.co.nci.iwf.designer.parts.design;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsOptionItem;

/**
 * 【デザイン時】選択肢があるパーツの既定クラス
 */
public abstract class PartsDesignOption extends PartsDesign {

	/** 選択肢 */
	public Long optionId;
	/** 選択肢項目 */
	public List<PartsOptionItem> optionItems;

}
