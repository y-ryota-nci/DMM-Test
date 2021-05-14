package jp.co.nci.iwf.component.download;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 複数のダウンロード用DTOを束ねておくためのホルダークラス
 *
 * @param <DTO> ダウンロード用DTO
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public class DownloadDtoHolder<DTO extends BaseDownloadDto> extends BaseDownloadDto implements Serializable {
	/** DTOリスト */
	public List<DTO> dtoList = new ArrayList<>();

	/**
	 * 初期化
	 * @param hsr HTTPサーブレットリクエスト
	 * @return
	 */
	public DownloadDtoHolder<DTO> init(String corporationCode) {
		final DestinationDatabaseService destination =
				CDI.current().select(DestinationDatabaseService.class).get();
		final ManifestService manifest =
				CDI.current().select(ManifestService.class).get();
		final CorporationService corp =
				CDI.current().select(CorporationService.class).get();
		final HttpServletRequest hsr =
				CDI.current().select(HttpServletRequest.class).get();

		this.corporationCode = corporationCode;
		this.corporationName = corp.getWfmCorporation(corporationCode).getCorporationName();
		this.appVersion = manifest.getVersion();
		this.dbDestination = destination.getUrl();
		this.dbUser = destination.getUser();
		this.timestampCreated = MiscUtils.timestamp();
		this.hostIpAddr = hsr.getLocalAddr();
		this.hostName = hsr.getLocalName();

		return this;
	}
}
