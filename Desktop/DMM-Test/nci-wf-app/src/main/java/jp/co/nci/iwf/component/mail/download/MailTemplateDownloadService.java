package jp.co.nci.iwf.component.mail.download;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;

/**
 * メールテンプレート定義ダウンロード用サービス
 */
@ApplicationScoped
public class MailTemplateDownloadService extends BaseDownloadService {

	@Inject private MailTemplateDownloadRepository repository;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private CorporationService corp;

	/**
	 * 画面コードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param corporationCode 企業コード
	 * @return
	 */
	public MailTemplateDownloadDto createDto(String corporationCode) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final MailTemplateDownloadDto dto = new MailTemplateDownloadDto(corporationCode);
		dto.corporationCode = corporationCode;
		WfmCorporation c = corp.getWfmCorporation(corporationCode);
		if (c != null) {
			dto.corporationName = c.getCorporationName();
		}

		//------------------------------------------
		// ダウンロード時のAPPバージョンとDB接続先情報
		//------------------------------------------
		dto.appVersion = manifest.getVersion();
		dto.dbDestination = destination.getUrl();
		dto.dbUser = destination.getUser();
		dto.timestampCreated = timestamp();
		dto.hostIpAddr = hsr.getLocalAddr();
		dto.hostName = hsr.getLocalName();

		//------------------------------------------
		// メール関連
		//------------------------------------------
		{
			// メールテンプレートヘッダーマスタ(MWM_MAIL_TEMPLATE_HEADER)
			dto.headerList = repository.getMwmMailTemplateHeader(corporationCode);
			// メールテンプレート本文マスタ(MWM_MAIL_TEMPLATE_BODY)
			dto.bodyList = repository.getMwmMailTemplateBody(corporationCode);
			// メールテンプレートファイルマスタ(MWM_MAIL_TEMPLATE_FILE)
			dto.fileList = repository.getMwmMailTemplateFile();
			// メール変数マスタ(MWM_MAIL_VARIABLE)
			dto.variableList = repository.getMwmMailValiable(corporationCode, localeCode);
		}
		//------------------------------------------
		// 業務管理項目マスタ
		//------------------------------------------
		{
			dto.businessNameList = repository.getMwmBusinessInfoNames(corporationCode);
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語対応マスタ（MWM_MULTILINGUAL）
			dto.multilingualList = new ArrayList<>(256);
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_MAIL_TEMPLATE_FILE", "MWM_MAIL_TEMPLATE_HEADER", "MAIL_TEMPLATE_FILE_ID", corporationCode));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_BUSINESS_INFO_NAME", "MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID", corporationCode));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_MAIL_VARIABLE", "MWM_MAIL_VARIABLE", "MAIL_VARIABLE_ID", corporationCode));
		}

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}
}
