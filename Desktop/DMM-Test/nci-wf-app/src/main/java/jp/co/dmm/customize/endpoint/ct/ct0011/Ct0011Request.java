package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalog;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogUsedDepartment;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * カタログ設定リクエスト
 */
public class Ct0011Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** カタログ */
	public MwmCatalog catalog;
	/** 画像 */
	public MwmCatalogImage catalogImage;

	/** 利用部門 */
	public List<MwmCatalogUsedDepartment> catalogUsedDepartments;
	public List<MwmCatalogUsedDepartment> removeCatalogUsedDepartments;
}
