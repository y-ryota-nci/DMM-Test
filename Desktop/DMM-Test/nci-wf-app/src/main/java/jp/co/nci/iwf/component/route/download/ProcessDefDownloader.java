package jp.co.nci.iwf.component.route.download;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.download.DownloadDtoHolder;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ZipCompressor;

/**
 * プロセス定義のダウンロードストリーム生成サービス
 */
@ApplicationScoped
public class ProcessDefDownloader extends MiscUtils implements StreamingOutput, CodeBook {
	@Inject private DownloadNotifyService notify;
	@Inject private ProcessDefDownloadService service;

	/** プロセス定義リスト */
	private List<WfmProcessDef> procList;


	/**
	 * ダウンロードの下準備。実際の処理は ScreenDownloader.write() で行われる
	 * @param req
	 * @return
	 */
	public StreamingOutput setup(List<WfmProcessDef> procList) {
		this.procList = procList;
		return this;
	}

	/**
	 * ZIPダウンロード処理
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロード開始を通知
		notify.begin();

        // プロセス定義コードに紐付く全データを抽出し、ZIPエントリ作成
		try (ZipCompressor zip = new ZipCompressor(output, MS932)) {
			final DownloadDtoHolder<ProcessDefDownloadDto> holder =
					new DownloadDtoHolder<ProcessDefDownloadDto>().init(procList.get(0).getCorporationCode());
			for (WfmProcessDef proc : procList) {
				if (isEmpty(proc.getCorporationCode()))
					throw new BadRequestException("企業コードが未指定です");
				if (isEmpty(proc.getProcessDefCode()))
					throw new BadRequestException("プロセス定義コードが未指定です");
				if (isEmpty(proc.getProcessDefDetailCode()))
					throw new BadRequestException("プロセス定義明細コードが未指定です");

				final ProcessDefDownloadDto dto = service.createDto(
						proc.getCorporationCode(), proc.getProcessDefCode(), proc.getProcessDefDetailCode());
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
