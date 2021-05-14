package jp.co.nci.iwf.designer.parts;

import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;

/** パーツ定義に紐付いた添付ファイル */
public class PartsAttachFileEntity {
	/** パーツ添付ファイルID */
	public Long partsAttachFileId;
	/** グループキー */
	public String groupKey;
	/** ファイル名 */
	public String fileName;
	/** ファイルサイズ */
	public Integer fileSize;
	/** コメント */
	public String comments;



	/** コンストラクタ */
	public PartsAttachFileEntity() {
	}

	/** コンストラクタ */
	public PartsAttachFileEntity(MwmPartsAttachFile f) {
		if (f != null) {
			this.partsAttachFileId = f.getPartsAttachFileId();
			this.groupKey = f.getGroupKey();
			this.fileName = f.getFileName();
			this.fileSize = f.getFileSize();
			this.comments = f.getComments();
		}
	}
}
