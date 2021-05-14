package jp.co.nci.iwf.designer.parts.runtime;

import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOption;

/**
 * 【実行時】選択肢を使うパーツの基底クラス
 *
 * @param <D>
 */
public abstract class PartsOption<D extends PartsDesignOption> extends PartsBase<D> {
	/** 値に対応したラベルを返す */
	public String getLabel(D d) {
		String value = getValue();
		for (PartsOptionItem item : d.optionItems) {
			if (same(item.value, value))
				return item.label;
		}
		return null;
	}
}
