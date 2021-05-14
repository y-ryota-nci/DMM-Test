package jp.co.nci.iwf.component.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.model.base.WfmUser;

/**
 * メール送信内容（宛先＋メール変数＋添付）。
 * テンプレートの差し替え要素を保持するクラス。
 */
public class MailEntry {
	/** メールの中で使用される言語コード */
	private String localeCode;
	/** 宛先リスト */
	private List<String> toList = new ArrayList<>();
	/** CCリスト */
	private List<String> ccList = new ArrayList<>();
	/** BCCリスト */
	private List<String> bccList = new ArrayList<>();
	/** 添付ファイルリスト */
	private List<MailAttachFile> attachFiles;
	/** テンプレートの置換文字列Map（Enumがキー、Stringが値） */
	private Map<String, String> variables;

	/**
	 * コンストラクタ
	 * @param localeCode 言語コード(必須)　※WFM_USER.DEFAULT_LOCALE_CODEから取得することを強く推奨。
	 * @param mailAddress 宛先メールアドレス(必須)。
	 * @param assigned アサイン情報(省略可)
	 * @param variables 置換文字列Map(省略可)
	 * @param attachFiles 添付ファイル(省略可)
	 */
	public MailEntry(WfmUser user, Map<String, String> variables, List<MailAttachFile> attachFiles) {
		this(user.getDefaultLocaleCode(), user.getMailAddress(), variables, attachFiles);
	}

	/**
	 * コンストラクタ
	 * @param localeCode 言語コード(必須)　※WFM_USER.DEFAULT_LOCALE_CODEから取得することを強く推奨。
	 * @param mailAddress 宛先メールアドレス(必須)。
	 * @param assigned アサイン情報(省略可)
	 * @param variables 置換文字列Map(省略可)
	 * @param attachFiles 添付ファイル(省略可)
	 */
	public MailEntry(String localeCode, String mailAddress, Map<String, String> variables) {
		this(localeCode, mailAddress, variables, null);
	}

	/**
	 * コンストラクタ
	 * @param localeCode 言語コード(必須)　※WFM_USER.DEFAULT_LOCALE_CODEから取得することを強く推奨。
	 * @param mailAddress 宛先メールアドレス(必須)。
	 * @param assigned アサイン情報(省略可)
	 * @param variables 置換文字列Map(省略可)
	 * @param attachFiles 添付ファイル(省略可)
	 */
	public MailEntry(String localeCode, String mailAddress, Map<String, String> variables, List<MailAttachFile> attachFiles) {
		if (StringUtils.isEmpty(localeCode))
			throw new InternalServerErrorException("メールの言語コードが未指定です");
		if (StringUtils.isEmpty(mailAddress))
			throw new InternalServerErrorException("メールの宛先が未指定です");
		this.localeCode = localeCode;
		this.toList.add(mailAddress);
		this.variables = variables;
		this.attachFiles = attachFiles;
	}

	/** 言語コード */
	public String getLocaleCode() {
		return localeCode;
	}
	/** 言語コード */
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	/** 宛先リスト */
	public List<String> getToList() {
		return toList;
	}
	/** 宛先リスト */
	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	/** CCリスト */
	public List<String> getCcList() {
		return ccList;
	}
	/** CCリスト */
	public void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}

	/** BCCリスト */
	public List<String> getBccList() {
		return bccList;
	}
	/** BCCリスト */
	public void setBccList(List<String> bccList) {
		this.bccList = bccList;
	}

	/** 添付ファイルリスト */
	public List<MailAttachFile> getAttachFiles() {
		return attachFiles;
	}
	/** 添付ファイルリスト */
	public void setAttachFiles(List<MailAttachFile> attachFiles) {
		this.attachFiles = attachFiles;
	}

	/** テンプレートの置換文字列Map（Enumがキー、Stringが値） */
	public Map<String, String> getVariables() {
		return variables;
	}
	/** テンプレートの置換文字列Map（Enumがキー、Stringが値） */
	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
}
