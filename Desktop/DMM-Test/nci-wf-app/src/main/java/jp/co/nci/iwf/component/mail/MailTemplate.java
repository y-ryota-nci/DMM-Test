package jp.co.nci.iwf.component.mail;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;

/**
 * メールテンプレートクラス。
 * 言語ごとのメールテンプレートを読み込んで、件名と本文を保持する
 */
@BizLogic
public class MailTemplate implements MailCodeBook {

	/** 改行コード */
	protected static final String LF = "\n";

	/** メールの件名 */
	private Map<String, String> subject = new HashMap<>();
	/** 本文 */
	private Map<String, String> contents = new HashMap<>();
	/** 差出人アドレス */
	private Map<String, String> from = new HashMap<>();
	/** 差出人名 */
	private Map<String, String> fromPersonal = new HashMap<>();
	/** CC */
	private Map<String, String> cc = new HashMap<>();
	/** BCC */
	private Map<String, String> bcc = new HashMap<>();
	/** 宛先 */
	private Map<String, String> to = new HashMap<>();

	/** テンプレートファイル名 */
	private String fileName;
	/** テンプレートの企業コード */
	private String corporationCode;

	/**
	 * コンストラクタ.
	 * テンプレートファイルのパスは [prefix]/[localeCode]/fileName で決まる
	 * @param fileName テンプレートファイル名
	 * @throws IOException
	 */
	public MailTemplate(String fileName, String corporationCode) {
		this.fileName = fileName;
		this.corporationCode = corporationCode;

		// メールテンプレートヘッダマスタを抽出
		final MailRepository repository = CDI.current().select(MailRepository.class).get();
		final MwmMailTemplateHeader header = repository.getHeader(corporationCode, fileName);

		// メールテンプレート本文マスタを抽出
		final Map<String, MwmMailTemplateBody> bodies;
		if (header == null) {
			bodies = new HashMap<>();
		} else {
			bodies = repository.getBody(header.getMailTemplateHeaderId(), corporationCode)
				.stream()
				.collect(Collectors.toMap(b -> b.getLocaleCode(), b -> b));
		}
		LocaleService localeService = CDI.current().select(LocaleService.class).get();
		Set<String> localeCodes = localeService.getSelectableLocaleCodes();

		// ファイルテンプレート
		MailTemplateFile fileTemplate = new MailTemplateFile(localeCodes, fileName, TEMPLATE_CHARSET);

		for (String localeCode : localeCodes) {
			MwmMailTemplateBody body = bodies.get(localeCode);
			if (body != null) {
				// メールテンプレート本文が登録されていればそれを使う
				subject.put(localeCode, body.getMailSubject());
				contents.put(localeCode, body.getMailBody());
				from.put(localeCode, header.getSendFrom());
				fromPersonal.put(localeCode, header.getSendFromPersonal());
				cc.put(localeCode, header.getSendCc());
				bcc.put(localeCode, header.getSendBcc());
				to.put(localeCode, header.getSendTo());
			}
			else if (fileTemplate.isSupport(localeCode, fileName)) {
				// メールテンプレート本文がなくても、ファイルテンプレートがあればそれを使う
				subject.put(localeCode, fileTemplate.getSubject(localeCode));
				contents.put(localeCode, fileTemplate.getContents(localeCode));
				from.put(localeCode, fileTemplate.getFrom(localeCode));
			}
		}
	}

	@SuppressWarnings("unused")
	private MailTemplate() {}

	/** テンプレートファイル名 */
	public String getFileName() {
		return fileName;
	}

	/** テンプレートの企業コード */
	public String getCorporationCode() {
		return corporationCode;
	}

	/**
	 * メールの件名
	 * @param localeCode 言語コード
	 * @return
	 * @throws IOException メールテンプレートが存在しないときにスロー
	 */
	public String getSubject(String localeCode) throws FileNotFoundException {
		if (!subject.containsKey(localeCode)) {
			throw new FileNotFoundException("メールテンプレートが存在しません(localeCode=" + localeCode + " path=" + fileName + ")");
		}
		return subject.get(localeCode);
	}

	/**
	 * 本文
	 * @param localeCode 言語コード
	 * @return
	 * @throws IOException
	 */
	public String getContents(String localeCode) throws FileNotFoundException {
		if (!contents.containsKey(localeCode)) {
			throw new FileNotFoundException("メールテンプレートが存在しません(localeCode=" + localeCode + " path=" + fileName + ")");
		}
		return contents.get(localeCode);
	}

	/**
	 * 差出人アドレス（FROM）
	 * @param localeCode
	 * @return
	 */
	public String getFrom(String localeCode) {
		// 環境設定からデフォルト値を設定するので差出人はテンプレートになくてもよい
		return from.get(localeCode);
	}

	/**
	 * 差出人名（FROM）
	 * @param localeCode
	 * @return
	 */
	public String getFromPersonal(String localeCode) {
		// 環境設定からデフォルト値を設定するので差出人はテンプレートになくてもよい
		return fromPersonal.get(localeCode);
	}

	/**
	 * CC
	 * @param localeCode
	 * @return
	 */
	public String[] getCc(String localeCode) {
		String str = cc.get(localeCode);
		if (str == null)
			return null;
		return str.split("[,\\s]+");
	}

	/**
	 * BCC
	 * @param localeCode
	 * @return
	 */
	public String[] getBcc(String localeCode) {
		String str = bcc.get(localeCode);
		if (str == null)
			return null;
		return str.split("[,\\s]+");
	}

	/**
	 * 宛先(送信先)
	 * @param localeCode
	 * @return
	 */
	public String[] getTo(String localeCode) {
		String str = to.get(localeCode);
		if (str == null || str.length() == 0)
			return null;
		return str.split("[,\\s]+");
	}

	/**
	 * 対象言語コードのテンプレートを読み込んでいるか
	 * @param localeCode
	 * @return
	 */
	public boolean isSupport(String localeCode) {
		return subject.containsKey(localeCode) && contents.containsKey(localeCode);
	}

	/**
	 * テンプレートのエントリが存在するか
	 * @return
	 */
	public boolean isEmpty() {
		return subject.isEmpty() && contents.isEmpty();
	}
}
