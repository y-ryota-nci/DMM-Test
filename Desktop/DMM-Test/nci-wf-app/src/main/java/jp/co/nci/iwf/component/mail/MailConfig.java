package jp.co.nci.iwf.component.mail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;

/**
 * メール環境設定の管理クラス
 */
@ApplicationScoped
public class MailConfig extends BaseRepository implements MailCodeBook {
	@Inject
	private CacheManager cm;
	@Inject
	private Logger log;
	/** 同期用 */
	private Object sync = new Object();



	/** キャッシュ */
	private CacheHolder<String, String> cache;

	/** コンストラクタ */
	public MailConfig() {
	}

	/** 初期化 */
	@PostConstruct
	public synchronized void init() {
		if (cache == null) {
			cache = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		}

		// MWM_MAIL_CONFIGからメール設定を読み込み
		select(MwmMailConfig.class, getSql("CM0013")).forEach(mc -> {
			cache.put(mc.getConfigCode(), defaults(mc.getConfigValue(), ""));
		});

		log.debug("mail.config ---------------------------");
		for (String key : cache.keySet())
			log.debug("  {}={}", key, cache.get(key));
	}

	/** リソース解放 */
	@PreDestroy
	public void destory() {
		cache.clear();
		cm.remove(cache);
	}

	/** 文字列→メール環境Enum */
	private MailEnv toMailEnv(String string) {
		if (isEmpty(string))
			return MailEnv.DEVELOP;
		return MailEnv.valueOf(string);
	}

	/**
	 * バリデーションチェック
	 */
	public void validate() {

		// SMTP関連
		MailEnv mailEnv = getMailEnv();
		if (MailEnv.STAGING == mailEnv || MailEnv.PRODUCT == mailEnv) {
			// SMTP HOST
			if (isEmpty(getHost()))
				throwMailConfigKeyNotFoundException(PROPS_SMTP_HOST);
			// SMTP PORT
			if (isEmpty(getPort()))
				throwMailConfigKeyNotFoundException(PROPS_SMTP_PORT);
			// SMTP Timeout
			if (isEmpty(getTimeout()))
				throwMailConfigKeyNotFoundException(PROPS_TIMEOUT);
			// メールエンコード
			if (isEmpty(getEncode()))
				throwMailConfigKeyNotFoundException(PROPS_ENCODE);
			// メールContent-Transfer-Encoding
			if (isEmpty(getContentTransferEncoding()))
				throwMailConfigKeyNotFoundException(PROPS_CONTENT_TRANSFER_ENCODING);
		}
		if (MailEnv.STAGING == mailEnv) {
			// ステージング環境でのダミー送信先
			if (isEmpty(getDummySendTo())) {
				throwMailConfigKeyNotFoundException(DUMMY_SEND_TO);
			}
		}
	}

	/** 例外スロー */
	private MailException throwMailConfigKeyNotFoundException(String key) {
		throw new MailException("メール設定にキー[%s]が未指定です。", key);
	}

	/** mail.yamlから設定値を取得 */
	private String get(String key) {
		return get(key, "");
	}

	/** mail.yamlから設定値を取得（未指定ならデフォルト値） */
	private String get(String key, String defaults) {
		if (cache.isExpired()) {
			synchronized (sync) {
				if (cache.isExpired()) {
					init();
				}
			}
		}
		String value = cache.get(key);
		return (value == null ? defaults : value);
	}

	/** SMTPホスト */
	public String getHost() {
		return get(PROPS_SMTP_HOST);
	}

	/** SMTPポート */
	public String getPort() {
		return get(PROPS_SMTP_PORT);
	}

	/** SMTPサーバ接続までのタイムアウト(DNS的な) */
	public String getConnectTimeout() {
		return get(PROPS_CONNECT_TIMEOUT);
	}

	/** SMTPサーバ接続後、メール送信までのタイムアウト */
	public String getTimeout() {
		return get(PROPS_TIMEOUT);
	}

	/** 送信エラーの戻り先 */
	public String getReturnPath() {
		return get(PROPS_RETURN_PATH);
	}

	/** 返信先 */
	public String getReplyTo() {
		return get(PROPS_REPLY_TO);
	}

	/** メーラー */
	public String getMailer() {
		return get(PROPS_MAILER);
	}

	/** 差出人メールアドレス */
	public String getFrom() {
		return get(PROPS_FROM);
	}

	/** 差出人名 */
	public String getFromPersonal() {
		return get(PROPS_FROM_PERSONAL);
	}

	/** SMTP認証 */
	public String getAuth() {
		return get(PROPS_SMTP_AUTH);
	}

	/** 認証時のユーザID */
	public String getUser() {
		return get(PROPS_SMTP_USER);
	}

	/** 認証時のパスワード */
	public String getPassword() {
		return get(PROPS_SMTP_PASSWORD);
	}

	/** プロトコル */
	public String getTransportProtocol() {
		return get(PROPS_TRANSPORT_PROTOCOL);
	}

	/** STARTTLSの有効無効 */
	public String getEnableStarttls() {
		return get(PROPS_SMTP_STARTTLS_ENABLE);
	}

	/** SSLの有効無効 */
	public String getEnableSSL() {
		return get(PROPS_SMTP_ENABLESSL_ENABLE);
	}

	/** SOCKETファクトリクラス */
	public String getSocketFactoryClass() {
		return get(PROPS_SMTP_SOCKETFACTORY_CLASS);
	}

	/** SOCKETファクトリフォールバック */
	public String getSocketFactoryFallback() {
		return get(PROPS_SMTP_SOCKETFACTORY_FALLBACK);
	}

	/** CC送信先 */
	public List<String> getDefaultCc() {
		return toList(get(PROPS_CC));
	}

	/** BCC送信先 */
	public List<String> getDefaultBcc() {
		return toList(get(PROPS_BCC));
	}

	/** ダミー送信先 */
	public String getDummySendTo() {
		return get(DUMMY_SEND_TO);
	}

	/** メールテンプレートへのパスのプレフィックス文字列 */
	public String getPathPrefix() {
		return get(PROPS_PATH_PREFIX);
	}

	/** メール環境 */
	public MailEnv getMailEnv() {
		return toMailEnv(get(PROPS_MAIL_ENV));
	}

	/** メールエンコード */
	public String getEncode() {
		return get(PROPS_ENCODE, MailCodeBook.DEFAULT_ENCODE);
	}

	/** 符号方式 */
	public String getContentTransferEncoding() {
		return get(PROPS_CONTENT_TRANSFER_ENCODING, MailCodeBook.DEFAULT_CONTENT_TRANSFER_ENCODEING);
	}

	/** 文字列をカンマ区切りでリスト化 */
	private List<String> toList(String str) {
		final List<String> list = new ArrayList<>();
		if (str != null) {
			// カンマ区切りならリスト化
			String[] astr = str.split(",\\s*");
			for (String s : astr) {
				if (isEmpty(s)) continue;
				list.add(s.trim());
			}
		}
		return list;
	}

//	/** メール件名に出力する時の「メール環境を示す文字列」 */
//	public String getEnvSubject() {
//		final MailEnv env = getMailEnv();
//		if (MailEnv.STAGING == env) {
//			return "≣≣≣ " + env.toString() + " ≣≣≣";
//		}
//		return "";
//	}
//
//	/** メール本文に出力する時の「メール環境を示す文字列」 */
//	public String getEnvBody() {
//		final MailEnv env = getMailEnv();
//		if (MailEnv.STAGING == env) {
//			return "≣≣≣≣≣≣ " + env.toString() + " ≣≣≣≣≣≣ ";
//		}
//		return "";
//	}

	/** 検証環境識別文字 */
	public String getStagingEnvString() {
		final MailEnv env = getMailEnv();
		if (MailEnv.STAGING == env) {
			return "≣≣≣≣≣≣ " + env.toString() + " ≣≣≣≣≣≣ ";
		}
		return "";
	}
}
