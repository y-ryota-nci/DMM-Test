package jp.co.nci.iwf.designer.service.download;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.download.DownloadDtoHolder;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ZipCompressor;

/**
 * 画面定義のダウンロードストリーム生成サービス
 */
@RequestScoped
public class ScreenDownloader extends MiscUtils implements StreamingOutput, CodeBook {
	@Inject private DownloadNotifyService notify;
	@Inject private ScreenDownloadService service;

	/** 企業コード */
	private String corporationCode;
	/** 画面 */
	private List<MwmScreen> screens;

	/**
	 * ダウンロードの下準備。実際の処理は ScreenDownloader.write() で行われる
	 * @param req
	 * @return
	 */
	public StreamingOutput setup(List<MwmScreen> screens) {
		this.corporationCode = screens.get(0).getCorporationCode();
		this.screens = screens;
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
			if (isEmpty(screens))
				throw new BadRequestException("画面が未指定です");

			// 画面コードに紐付く全データを抽出し、ZIPエントリ作成
			final DownloadDtoHolder<ScreenDownloadDto> holder =
					new DownloadDtoHolder<ScreenDownloadDto>().init(corporationCode);

			for (MwmScreen screen : screens) {
				final ScreenDownloadDto dto = service.createDto(screen);
				if (dto != null) {
					holder.dtoList.add(dto);
				}
			}
			// 互換性を高めるため、JSON形式でZIP化
			zip.addEntryAsJSON(holder.getClass().getSimpleName(), holder);
		}
		finally {
			// ダウンロード終了を通知
			notify.end();
		}
	}

}
