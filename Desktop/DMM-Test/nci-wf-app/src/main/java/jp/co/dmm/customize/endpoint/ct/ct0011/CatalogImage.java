package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.io.Serializable;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;

public class CatalogImage implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	public Long catalogImageId;
	public Long version;
	public String fileName;

	/** コンストラクタ */
	public CatalogImage() {
	}

	/** コンストラクタ */
	public CatalogImage(MwmCatalogImage i) {
		this.catalogImageId = i.getCatalogImageId();
		this.fileName = i.getFileName();
		this.version = i.getVersion();
	}
}
