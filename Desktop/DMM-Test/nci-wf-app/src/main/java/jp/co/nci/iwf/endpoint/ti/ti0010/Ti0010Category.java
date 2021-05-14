package jp.co.nci.iwf.endpoint.ti.ti0010;

import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;

/**
 * マスタ取込設定のカテゴリ情報
 */
public class Ti0010Category {
	public String corporationCode;
	public String categoryName;
	public String categoryCode;
	public Long categoryId;

	public Ti0010Category() {
	}

	public Ti0010Category(MwmCategory c) {
		corporationCode = c.getCorporationCode();
		categoryCode = c.getCategoryCode();
		categoryName = c.getCategoryName();
		categoryId = c.getCategoryId();
	}
}
