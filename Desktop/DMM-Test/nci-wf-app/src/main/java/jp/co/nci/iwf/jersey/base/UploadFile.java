package jp.co.nci.iwf.jersey.base;

import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.ContentDisposition;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードしたファイル内容を格納する構造体
 */
public class UploadFile implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public String fileName;
	public long size;
	public InputStream stream;
	public java.util.Date createDate;
	public java.util.Date modificationDate;

	/**
	 * コンストラクタ
	 * @param bodyPart
	 */
	public UploadFile(BodyPart bodyPart) {
		ContentDisposition cd = null;
		final String scd = bodyPart.getHeaders().getFirst("Content-Disposition");
		if (scd != null) {
			try {
				cd = new ContentDisposition(scd, true);
			} catch (final ParseException ex) {
				throw new IllegalArgumentException("Error parsing content disposition: " + scd, ex);
			}
		}
		if (cd == null) {
			cd = bodyPart.getContentDisposition();
		}

		this.fileName = MiscUtils.iso8859ToUtf8(cd.getFileName());	// 文字化け対策
		this.size = cd.getSize();
		this.createDate = cd.getCreationDate();
		this.modificationDate = cd.getModificationDate();

		final BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
		this.stream = bodyPartEntity.getInputStream();
	}

}
