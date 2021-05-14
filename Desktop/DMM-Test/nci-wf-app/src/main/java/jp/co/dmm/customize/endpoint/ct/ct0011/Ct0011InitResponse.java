package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalog;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * カタログ登録画面の初期化レスポンス
 */
public class Ct0011InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public boolean editable;
	public MwmCatalog catalog;
	public CatalogImage catalogImage;
	public List<CatalogUsedDepartment> catalogUsedDepartments;

	/** カタログカテゴリーの選択肢 */
	public List<OptionItem> catalogCategories;
	/** カタログ単位の選択肢 */
	public List<OptionItem> catalogUnits;
	/** 消費税区分の選択肢 */
	public List<OptionItem> salesTaxTypes;

}
