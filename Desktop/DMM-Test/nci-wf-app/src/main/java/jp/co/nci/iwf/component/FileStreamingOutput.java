package jp.co.nci.iwf.component;

import javax.ws.rs.core.StreamingOutput;

/**
 * ファイル名とコンテンツタイプを指定できる StreamingOutput
 */
public abstract class FileStreamingOutput implements StreamingOutput {
	/** 出力ファイル名 */
	protected String fileName;
	/** コンテンツタイプ */
	protected String contentType;

	/** コンストラクタ */
	public FileStreamingOutput(String fileName) {
		this.fileName = fileName;
	}

	/** 出力ファイル名 */
	public String getFileName() {
		return fileName;
	}
	/** 出力ファイル名 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** コンテンツタイプ */
	public String getContentType() {
		return contentType;
	}
	/** コンテンツタイプ */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
