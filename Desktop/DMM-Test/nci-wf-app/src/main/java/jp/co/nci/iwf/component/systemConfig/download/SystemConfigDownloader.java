package jp.co.nci.iwf.component.systemConfig.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ZipCompressor;

/**
 * システム環境設定ダウンローダー
 */
@ApplicationScoped
public class SystemConfigDownloader extends MiscUtils implements StreamingOutput, CodeBook {
	@Inject private DownloadNotifyService notify;
	@Inject private SystemConfigDownloadService service;

	private String corporationCode;

	/**
	 * ダウンロードの下準備。実際の処理は ScreenDownloader.write() で行われる
	 * @param req
	 * @return
	 */
	public StreamingOutput setup(String corporationCode) {
		this.corporationCode = corporationCode;
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
			// 画面コードに紐付く全データを抽出し、ZIPエントリ作成
			final SystemConfigDownloadDto dto = service.createDto(corporationCode);
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
