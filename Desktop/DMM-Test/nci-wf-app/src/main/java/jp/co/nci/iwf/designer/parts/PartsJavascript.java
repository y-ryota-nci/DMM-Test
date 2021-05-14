package jp.co.nci.iwf.designer.parts;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;

/**
 * パーツ定義で参照しているJavascript
 */
public class PartsJavascript extends BaseJpaEntity {
	/** コンストラクタ */
	public PartsJavascript(MwmScreenJavascript sj) {
		javascriptId = sj.getJavascriptId();
		sortOrder = sj.getSortOrder();
	}

	/** コンストラクタ */
	public PartsJavascript(MwmContainerJavascript cj) {
		javascriptId = cj.getJavascriptId();
		sortOrder = cj.getSortOrder();
	}

	/** コンストラクタ */
	public PartsJavascript() {

	}

	/** Javascript ID */
	public Long javascriptId;
	/** 並び順 */
	public Integer sortOrder;
}
