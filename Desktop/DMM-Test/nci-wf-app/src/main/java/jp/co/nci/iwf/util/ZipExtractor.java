package jp.co.nci.iwf.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.provider.JacksonConfig;

/**
 * ZIP解凍
 */
public class ZipExtractor extends MiscUtils implements Closeable, CodeBook {
	/** ZIPストリーム */
	private ZipInputStream zis;

	/**
	 * エントリがMS932でエンコードされているものとして初期化
	 * @param in
	 * @throws IOException
	 */
	public ZipExtractor(InputStream in) throws IOException {
		zis = new ZipInputStream(in, MS932);
	}

	/**
	 * エントリが指定エンコードでエンコードされているものとして初期化
	 * @param in
	 * @param charset
	 * @throws IOException
	 */
	public ZipExtractor(InputStream in, Charset charset) throws IOException {
		zis = new ZipInputStream(in, charset);
	}

	/**
	 * ZIPを解凍して、エントリ名をキー、エントリ値をJavaインスタンスとして、Map化して返す。
	 * オブジェクトをダイレクトにバイト配列化してある前提である。
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Map<String, Object> extractAsObject() throws IOException, ClassNotFoundException {
		Map<String, Object> map = new HashMap<>();
		ZipEntry entry;
		while (null != (entry = zis.getNextEntry())) {
			if (!entry.isDirectory()) {
				ObjectInputStream ois = new ObjectInputStream(zis);
				map.put(entry.getName(), ois.readObject());
			}
			zis.closeEntry();
		}
		return map;
	}

	/**
	 * エントリ名とJSONマッピング先クラスを定義したMapに従い、ZIPを解凍してMap化して返す。
	 * 各エントリはJSON文字列である前提である。
	 * @param mapping エントリ名とJSONマッピング先クラスを定義したMap
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> extractAsJSON(Map<String, Class<?>> mapping) throws IOException {
		Map<String, Object> map = new HashMap<>();
		ZipEntry entry;
		while (null != (entry = zis.getNextEntry())) {
			String entryName = entry.getName();
			if (mapping.containsKey(entryName) && !entry.isDirectory()) {
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = zis.read(buffer)) != -1) {
					    baos.write(buffer, 0, length);
					}
					String json = baos.toString("UTF-8");
					Class<?> clazz = mapping.get(entryName);
					Object obj = JacksonConfig.getObjectMapper().readValue(json, clazz);
					map.put(entryName, obj);
				}
			}
			zis.closeEntry();
		}
		return map;
	}

	/**
	 * ZIPを解凍して、該当エントリのオブジェクトをJavaインスタンスとして返す。
	 * 各エントリはJSON文字列である前提である。
	 * @param entryName エントリ名
	 * @param clazz JSONマッピング先クラス
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <E> E extractAsJSON(String entryName, Class<E> clazz) throws IOException {
		Object obj = null;
		ZipEntry entry;
		while (null != (entry = zis.getNextEntry())) {
			if (eq(entryName, entry.getName()) && !entry.isDirectory()) {
				obj = toJson(zis, clazz);
			}
			zis.closeEntry();
		}
		return (E)obj;
	}

	/**
	 * ZIPを解凍して、次のエントリを指定された型のインスタンスとして返す。
	 * 各エントリはJSON文字列である前提である。
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <E> E extractNextAsJSON(Class<E> clazz) throws IOException {
		Object obj = null;
		ZipEntry entry = null;
		if (null != (entry = zis.getNextEntry())) {
			if (!entry.isDirectory()) {
				obj = toJson(zis, clazz);
			}
			zis.closeEntry();
		}
		return (E)obj;
	}

	/** 次のエントリを指定された型のインスタンスとして返す */
	private <E> E toJson(InputStream in, Class<E> clazz) throws IOException {
		// エントリを文字列のまま扱うと解凍結果が巨大な場合にOutOfMemoryを招くので、いったんファイル化し、
		// そのストリームをJSON変換することで、必要メモリを最小限に抑える
		final File temp = File.createTempFile("extract", ".tmp");
		Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
		try (BufferedReader br = Files.newBufferedReader(temp.toPath(), UTF8)) {
			return JacksonConfig.getObjectMapper().readValue(br, clazz);
		}
		finally {
			Files.delete(temp.toPath());
		}
	}

	/**
	 * ストリームを閉じる
	 */
	@Override
	public void close() throws IOException {
		if (zis != null) {
			zis.close();
		}
	}
}
