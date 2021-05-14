package jp.co.nci.iwf.component.mail.download;

import java.io.Serializable;
import java.util.List;

import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;

/**
 * メールテンプレート定義ダウンロードDTO
 */
public class MailTemplateDownloadDto extends BaseDownloadDto implements Serializable{

	/** メールテンプレート定義ファイルリスト */
	public List<MwmMailTemplateFile> fileList;
	/** メールテンプレートヘッダーリスト */
	public List<MwmMailTemplateHeader> headerList;
	/** メールテンプレート本文リスト */
	public List<MwmMailTemplateBody> bodyList;
	/** 業務管理項目リスト */
	public List<MwmBusinessInfoName> businessNameList;
	/** 多言語マスタリスト */
	public List<MwmMultilingual> multilingualList;
	/** メール変数マスタ(MWM_MAIL_VARIABLE) */
	public List<MwmMailVariable> variableList;

	/**
	 * コンストラクタ
	 * @param corporationCode
	 */
	public MailTemplateDownloadDto(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	/**
	 * コンストラクタ
	 */
	public MailTemplateDownloadDto() {}
}
