package jp.co.nci.iwf.component.menu.download;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ZipCompressor;

/**
 * メニューロール定義のダウンロードストリーム生成サービス
 */
@ApplicationScoped
public class MenuRoleDownloader extends MiscUtils implements StreamingOutput, CodeBook {
	@Inject private DownloadNotifyService notify;
	@Inject private MenuRoleDownloadService service;
	@Inject private CorporationService corp;

	/** ダウンロード時の企業コード */
	private String corporationCode;
	/** ダウンロード時の企業名 */
	private String corporationName;
	/** ダウンロード時のメニューロール区分 */
	private String menuRoleType;
	/** ダウンロード時のメニューロールコード */
	private Set<String> menuRoleCodes;

	/**
	 * ダウンロードの下準備。実際の処理は ScreenDownloader.write() で行われる
	 * @param corporationCode
	 * @param menuRoleType
	 * @param menuRoleCodes
	 * @return
	 */
	public StreamingOutput setup(String corporationCode, String menuRoleType, Set<String> menuRoleCodes) {
		WfmCorporation c = corp.getWfmCorporation(corporationCode);
		this.corporationCode = corporationCode;
		this.corporationName = c.getCorporationName();
		this.menuRoleType = menuRoleType;
		this.menuRoleCodes = menuRoleCodes;
		return this;
	}

	/**
	 * ZIPダウンロード処理
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロード開始を通知
		notify.begin();

		if (isEmpty(corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		try (ZipCompressor zip = new ZipCompressor(output, MS932)) {
			// 選択されたメニューロール全データを抽出し、ZIPエントリ作成
			final MenuRoleDownloadDto dto = service.createDto(
					corporationCode, corporationName, menuRoleType, menuRoleCodes);
			if (dto != null) {
				// 互換性を高めるため、JSON形式でZIP化
				String entryName = dto.getClass().getSimpleName();
				zip.addEntryAsJSON(entryName, dto);
			}
		}
		finally {
			// ダウンロード終了を通知
			notify.end();
		}
	}
}
