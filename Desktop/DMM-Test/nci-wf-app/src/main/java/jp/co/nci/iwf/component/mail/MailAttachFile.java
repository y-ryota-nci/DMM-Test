package jp.co.nci.iwf.component.mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.util.ClassPathResource;

/**
 * 添付ファイル用リソース
 */
public class MailAttachFile {

	/** ストリーム読み込み時のバッファサイズ */
	private static int BUFFER = 8192;

	/** ファイル名 */
	private String fileName;
	/** ファイル */
	private File file;
	/** バイト配列 */
	private byte[] bytes;
	/** 入力ストリーム */
	private InputStream is;

	/**
	 * パス指定によるコンストラクタ。
	 * まずクラスパス内を探し、なければファイルシステム上から探す
	 * @param path パス
	 * @throws IOException
	 */
	public MailAttachFile(String path) throws IOException {
		// とりあえずクラスパスを探し、なければファイルシステムを探す
		final ClassPathResource cpr = new ClassPathResource(path);
		this.file = cpr.exists() ? cpr.getFile() : new File(path);
		this.fileName = file.getName();
	}

	/**
	 * ClassPathリソースによるコンストラクタ
	 * @param cpr (Springの)ClassPathリソース
	 * @throws IOException
	 */
	public MailAttachFile(File file) throws IOException {
		this.file = file;
		this.fileName = file.getName();
	}

	/**
	 * バイト配列によるコンストラクタ
	 * @param fileName リソース名称
	 * @param bytes バイト配列
	 */
	public MailAttachFile(String fileName, byte[] bytes) throws IOException {
		this.bytes = bytes;
		this.fileName = fileName;
	}

	/**
	 * バイト配列によるコンストラクタ
	 * @param name リソース名称
	 * @param bytes バイト配列
	 */
	public MailAttachFile(String fileName, InputStream is) throws IOException {
		this.is = is;
		this.fileName = fileName;
	}

	/** ファイル名 */
	public String getFileName() {
		return fileName;
	}

	/** ストリーム */
	public BufferedInputStream openInputStream() {
		try {
			if (bytes != null)
				return new BufferedInputStream(new ByteArrayInputStream(bytes), BUFFER);
			if (is != null)
				return new BufferedInputStream(is, BUFFER);
			if (file != null)
				return new BufferedInputStream(new FileInputStream(file), BUFFER);
			return null;
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
