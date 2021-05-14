package jp.co.nci.iwf.component.tray.download;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;

/**
 * トレイ設定ダウンロード用サービス
 */
@ApplicationScoped
public class TrayConfigDownloadService extends BaseDownloadService {

	@Inject private TrayConfigDownloadRepository repository;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private CorporationService corp;

	/**
	 * 画面コードをもとに関連データをすべて抽出、DTOへセットして返す
	 * @param corporationCode 企業コード
	 * @return
	 */
	public TrayConfigDownloadDto createDto(String corporationCode) {
		WfmCorporation c = corp.getWfmCorporation(corporationCode);
		final TrayConfigDownloadDto dto = new TrayConfigDownloadDto(corporationCode, c.getCorporationName());

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
		// トレイ設定関係
		//------------------------------------------
		{
			// トレイ設定（MWM_TRAY_CONFIG）
			dto.configList = repository.getMwmTrayConfig(corporationCode);
			// トレイ設定個人マスタ(MWM_TRAY_CONFIG_PERSON)
			dto.personList = repository.getMwmTrayConfigPersons(corporationCode);
			// トレイ設定検索条件マスタ(MWM_TRAY_CONFIG_CONDITION)
			dto.conditionList = repository.getMwmTrayConfigConditions(corporationCode);
			// パーツ子要素定義（MWM_PARTS_CHILD_HOLDER）
			dto.resultList = repository.getMwmTrayConfigResults(corporationCode);
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
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_TRAY_CONFIG", "TRAY_CONFIG_ID", corporationCode));
			dto.multilingualList.addAll(repository.getMwmMultilingual("MWM_BUSINESS_INFO_NAME", "BUSINESS_INFO_NAME_ID", corporationCode));
		}

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}

	/** ダウンロード対象のエンティティリストをリフレクションで扱いやすいようフラットに配列化 */
	public List<?>[] toAllEntities(TrayConfigDownloadDto dto) {
		// ダウンロード対象のエンティティをフラットに扱うため、配列化
		final List<?>[] allEntities = {
			dto.configList,
			dto.personList,
			dto.conditionList,
			dto.resultList,
			dto.multilingualList,
		};
		return allEntities;
	}
}
