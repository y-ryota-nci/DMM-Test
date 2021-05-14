package jp.co.nci.iwf.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.co.nci.iwf.component.CodeBook;

/**
 * ZIP圧縮クラス。
 */
public final class ZipCompressor extends MiscUtils implements CodeBook, java.io.Closeable {
	/** バッファサイズ */
	private static final int BUF_SIZE = 8 * 1024;
	/** ZIPストリーム */
	private ZipOutputStream zos = null;

	/**
	 * コンストラクタ。エントリ名のエンコードはShift-JIS
	 * @param out ZIPの書き込み先ストリーム
	 */
	public ZipCompressor(OutputStream out) {
		zos = new ZipOutputStream(out, MS932);
	}

	/**
	 * コンストラクタ
	 * @param out ZIPの書き込み先ストリーム
	 * @param encode エントリ名のエンコード（これが解凍時のファイル名のエンコードになる）
	 */
	public ZipCompressor(OutputStream out, Charset encode) {
		zos = new ZipOutputStream(out, encode);
	}

	/**
	 * ZIPエントリを追加。
	 * @param entryName エントリ名
	 * @param in 入力ストリーム
	 * @throws IOException
	 */
	public void addEntry(String entryName, InputStream in) throws IOException {
		// エントリ作成
		final ZipEntry ze = new ZipEntry(entryName);
		zos.putNextEntry(ze);

		if (entryName.endsWith("/")) {
			// フォルダは無視
		}
		else {
			// 実体の書き込み
			try (InputStream is = new BufferedInputStream(in)) {
				byte[] buf = new byte[BUF_SIZE];
				for (;;) {
					int len = is.read(buf);
					if (len < 0) break;
					zos.write(buf, 0, len);
				}
			}
		}
		zos.closeEntry();
	}

	/**
	 * ZIPエントリを追加
	 * @param entryName エントリ名
	 * @param bytes バイト配列
	 * @throws IOException
	 */
	public void addEntry(String entryName, byte[] bytes) throws IOException {
		// エントリ作成
		final ZipEntry ze = new ZipEntry(entryName);
		zos.putNextEntry(ze);

		if (entryName.endsWith("/") || bytes == null || bytes.length == 0){
			// フォルダ or 実体がないのは無視
		}
		else {
			// 実体の書き込み
			zos.write(bytes);
		}
		zos.closeEntry();
	}

	/**
	 * ZIPエントリを追加
	 * @param entryName エントリ名
	 * @param obj 圧縮対象のエレメント
	 * @throws IOException
	 */
	public void addEntry(String entryName, Serializable obj) throws IOException {
		addEntry(entryName, toBytes(obj));
	}

	/**
	 * JSONエンコードしてZIPエントリを追加。
	 * オブジェクトをJSON文字列化してZIP圧縮するので、ファイルサイズは100倍ぐらい大きくなる可能性があるが、復元したときの互換性が高い
	 * @param obj 圧縮対象のエレメント
	 * @throws IOException
	 */
	public void addEntryAsJSON(String entryName, Object obj) throws IOException {
		if (obj == null)
			return;

		// オブジェクトをJSON文字列化
		String json = toJsonFromObj(obj);
		if (MiscUtils.isEmpty(json))
			return;

		addEntry(entryName, json.getBytes(UTF8));
	}

	/**
	 * リソースをクローズして、ZIPファイルへ書き込み
	 */
	@Override
	public void close() throws IOException {
		zos.flush();
		zos.close();
	}
}
