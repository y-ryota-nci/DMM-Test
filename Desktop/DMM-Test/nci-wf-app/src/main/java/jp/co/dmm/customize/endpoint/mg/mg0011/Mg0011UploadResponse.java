package jp.co.dmm.customize.endpoint.mg.mg0011;

import java.io.Serializable;

import jp.co.dmm.customize.jpa.entity.mw.ItmImgMst;

public class Mg0011UploadResponse implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	public Long itmImgId;
	public Long version;
	public String fileName;

	/** コンストラクタ */
	public Mg0011UploadResponse() {
	}

	/** コンストラクタ */
	public Mg0011UploadResponse(ItmImgMst i) {
		this.itmImgId = i.getId().getItmImgId();
		this.fileName = i.getFileNm();
		this.version = i.getVrsn().longValue();
	}
}
