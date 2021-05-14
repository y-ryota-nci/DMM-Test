package jp.co.nci.iwf.component.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.nci.iwf.util.ClassPathResource;

/**
 * メールテンプレートクラス。
 * 言語ごとのメールテンプレートを読み込んで、件名と本文を保持する
 */
public class MailTemplateFile {


	/** 改行コード */
	protected static final String LF = "\n";

	/** メールの件名 */
	private Map<String, String> subject = new HashMap<>();
	/** 本文 */
	private Map<String, String> contents = new HashMap<>();
	/** 差出人アドレス */
	private Map<String, String> from = new HashMap<>();
	/** テンプレートファイル名 */
	private String fileName;

	/**
	 * コンストラクタ.
	 * テンプレートファイルのパスは [prefix]/[localeCode]/fileName で決まる
	 * @param localeCodes テンプレートファイルへのパスの一部である言語コード
	 * @param fileName テンプレートファイル名
	 * @param encode テンプレートファイルのエンコード
	 * @throws IOException
	 */
	public MailTemplateFile(Collection<String> localeCodes, String fileName, String encode) {
		this.fileName = fileName;

		// 言語コード毎にテンプレートを読み込む
		final Charset charset = Charset.forName(encode);
		for (String localeCode : localeCodes) {
			final String path = getPath(localeCode, fileName);
			final ClassPathResource cpr = new ClassPathResource(path);
			if (!cpr.exists()) {
				continue;
			}
			try (BufferedReader bf = Files.newBufferedReader(cpr.getFile().toPath(), charset)) {
				final StringBuilder body = new StringBuilder(256);
				final AtomicInteger i = new AtomicInteger();

				bf.lines().forEach(line -> {
					final int lineCnt = i.incrementAndGet();
					if (lineCnt == 1) {
						// 一行目が件名
						subject.put(localeCode, line);
					}
					else if (lineCnt == 2) {
						// 二行目は差出人(From)
						from.put(localeCode, line);
					}
					else if (lineCnt > 2) {
						// 三行目以降が本文
						body.append(line).append(LF);
					}
				});
				contents.put(localeCode, body.toString());
			}
			catch (IOException e) {
				throw new MailException(e);
			}
		}
	}

	/** 非公開コンストラクタ */
	@SuppressWarnings("unused")
	private MailTemplateFile() {}

	/** テンプレートファイル名 */
	public String getFileName() {
		return fileName;
	}

	private String getPath(String localeCode, String fileName) {
		final String path = String.format("mail/%s/%s", localeCode, fileName);
		return path;
	}

	/**
	 * メールの件名
	 * @param localeCode 言語コード
	 * @return
	 * @throws IOException メールテンプレートが存在しないときにスロー
	 */
	public String getSubject(String localeCode) {
		return subject.get(localeCode);
	}

	/**
	 * 本文
	 * @param localeCode 言語コード
	 * @return
	 * @throws IOException
	 */
	public String getContents(String localeCode) {
		return contents.get(localeCode);
	}

	/**
	 * 差出人（FROM）
	 * @param localeCode
	 * @return
	 * @throws IOException
	 */
	public String getFrom(String localeCode) {
		// 環境設定からデフォルト値を設定するので差出人はテンプレートになくてもよい
		return from.get(localeCode);
	}

	/**
	 * 対象言語コードのテンプレートを読み込んでいるか
	 * @param localeCode
	 * @return
	 */
	public boolean isSupport(String localeCode, String fileName) {
		// 対象ファイルが存在していればOK
		final String path = getPath(localeCode, fileName);
		return new ClassPathResource(path).exists();
	}

	/**
	 * テンプレートのエントリが存在するか
	 * @return
	 */
	public boolean isEmpty() {
		return subject.isEmpty() && contents.isEmpty();
	}
}
