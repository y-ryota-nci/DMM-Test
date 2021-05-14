package jp.co.nci.iwf.component.systemConfig.download;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;

/**
 * システム環境設定ダウンロード用サービス
 */
@ApplicationScoped
public class SystemConfigDownloadService extends BaseDownloadService {
	@Inject private SystemConfigDownloadRepository repository;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private CorporationService corp;

	/**
	 * 画面コードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param screenCode 画面コード
	 * @return
	 */
	public SystemConfigDownloadDto createDto(String corporationCode) {
		final SystemConfigDownloadDto dto = new SystemConfigDownloadDto();
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

		// 企業プロパティマスタ (WFM_CORPORATION_PROPERTY)
		dto.corporationPropertyList = repository.getWfmCorporationProperty(corporationCode);
		// プロパティマスタ(WFM_CORP_PROP_MASTER)
		dto.corpPropMasterList = repository.getWfmCorpPropMaster();
		// メール環境設定マスタ(MWM_MAIL_CONFIG)
		dto.mailConfigList = repository.getMwmMailConfig();
		// 名称ルックアップ
		dto.nameLookupList = repository.getWfmNameLookup("WFM_CORP_PROP_MASTER", "MWM_MAIL_CONFIG");

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}
}
