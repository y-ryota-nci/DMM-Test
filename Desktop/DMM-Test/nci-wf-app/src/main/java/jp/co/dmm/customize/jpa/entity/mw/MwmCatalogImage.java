package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_CATALOG_IMAGE database table.
 *
 */
@Entity
@Table(name="MWM_CATALOG_IMAGE")
@NamedQuery(name="MwmCatalogImage.findAll", query="SELECT m FROM MwmCatalogImage m")
public class MwmCatalogImage extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_IMAGE_ID")
	private long catalogImageId;
	@Column(name="CATALOG_ID")
	private long catalogId;
	@Column(name="FILE_NAME")
	private String fileName;
	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	public MwmCatalogImage() {
	}

	public long getCatalogImageId() {
		return catalogImageId;
	}
	public void setCatalogImageId(long catalogImageId) {
		this.catalogImageId = catalogImageId;
	}

	public long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(long catalogId) {
		this.catalogId = catalogId;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

}