package jp.co.nci.iwf.component.tray.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ZipCompressor;

/**
 * トレイ設定ダウンロード用ストリーム書き込みクラス
 */
@ApplicationScoped
public class TrayConfigDownloader extends MiscUtils implements StreamingOutput, CodeBook {
	@Inject private DownloadNotifyService notify;
	@Inject private TrayConfigDownloadService service;

	/** 企業コード */
	private String corporationCode;

	/**
	 * ダウンロードの下準備。実際の処理は ScreenDownloader.write() で行われる
	 * @param corporationCode 企業コード
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

		try (ZipCompressor zip = new ZipCompressor(output, MS932)) {
			// 画面コードに紐付く全データを抽出し、ZIPエントリ作成
			final TrayConfigDownloadDto dto = service.createDto(corporationCode);
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
