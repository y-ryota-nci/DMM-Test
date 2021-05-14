package jp.co.nci.iwf.endpoint.up.up0060;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.systemConfig.download.SystemConfigDownloadDto;
import jp.co.nci.iwf.component.systemConfig.upload.SystemConfigUploadService;
import jp.co.nci.iwf.endpoint.up.CommonUploadRepository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadFile;
import jp.co.nci.iwf.util.ZipExtractor;

/**
 * システム環境設定アップロード画面サービス
 */
@BizLogic
public class Up0060Service extends BaseService {
	private static final String entryName = SystemConfigDownloadDto.class.getSimpleName();

	@Inject private SystemConfigUploadService uploader;
	@Inject private CommonUploadRepository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Up0060Response init(Up0060Request req) {
		final Up0060Response res = createResponse(Up0060Response.class, req);

		// アップロードファイルIDがあれば、それを初期表示
		if (req.uploadFileId != null) {
			// アップロードファイル情報を抽出
			final MwtUploadFile up = repository.getMwtUploadFile(req.uploadFileId);
			if (up == null)
				throw new NotFoundException("アップロードファイル情報が見つかりません（uploadFileId=" + req.uploadFileId + "）");

			// バイナリデータをZIPファイルみなして、ダウンロードDTOへ復元
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(up.getFileData()))) {
				final SystemConfigDownloadDto dto = ze.extractAsJSON(entryName, SystemConfigDownloadDto.class);
				fillResponse(up, dto, res);
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		res.success = true;
		return res;
	}

	/** レスポンスにアップロード内容を反映 */
	private void fillResponse(MwtUploadFile up, SystemConfigDownloadDto dto, Up0060Response res) {
		res.uploadFileId = up.getUploadFileId();
		res.fileName = up.getFileName();
		res.corporationCode = dto.corporationCode;
		res.corporationName = dto.corporationName;
		res.timestampCreated = FORMATTER_DATETIME.get().format(dto.timestampCreated);
		res.dbDestination = dto.dbDestination + "@" + dto.dbUser;
		res.appVersion = dto.appVersion;
		res.hostIpAddr = dto.hostIpAddr;
		res.hostName = dto.hostName;
	}

	/**
	 * システム環境設定アップロード
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public Up0060Response upload(FormDataMultiPart multiPart) {
		final Up0060Response res = createResponse(Up0060Response.class, null);
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				// アップロードファイルをバイト配列化
				final byte[] data = toBytes(f.stream);

				// バイト配列をZIPファイルとみなして解凍
				try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(data))) {
					// 互換性を高めるため、JSON形式でZIP化されている
					SystemConfigDownloadDto dto = ze.extractAsJSON(entryName, SystemConfigDownloadDto.class);

					if (dto == null) {
						// エントリがない
						throw new FileNotFoundException("システム環境設定データが含まれていません");
					} else {
						// アップロードファイル情報を登録
						final MwtUploadFile up = repository.insertMwtUploadFile(dto, data, f.fileName, UploadKind.SYSTEM_ENV);

						fillResponse(up, dto, res);
						res.success = true;
					}
				}
			}
			catch (IOException e) {
				// JSONでパースできないか、クラスのシリアライズバージョンが異なるか、そもそもZIPファイルじゃない
				throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
			}
		}
		return res;
	}

	/**
	 * システム環境設定の登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Up0060Response register(Up0060Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Up0060Response res = createResponse(Up0060Response.class, req);

		if (req.uploadFileId == null)
			throw new BadRequestException("アップロードファイルIDが未指定です");

		// バリデーション
		String error = validate(req);
		if (isNotEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
		}
		else {
			// アップロードファイル情報を抽出
			final MwtUploadFile up = repository.getMwtUploadFile(req.uploadFileId);
			if (up == null)
				throw new NotFoundException("アップロードファイル情報が見つかりません（uploadFileId=" + req.uploadFileId + "）");

			// バイナリデータをZIPファイルみなして、ダウンロードDTOへ復元
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(up.getFileData()))) {
				final SystemConfigDownloadDto dto = ze.extractAsJSON(entryName, SystemConfigDownloadDto.class);

				// アップロード内容をDBへ反映
				uploader.persist(corporationCode, req, dto);

				// アップロードファイル情報を登録済みへ更新
				repository.complateToRegister(up, req);

				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.trayDisplayConfig));
				res.success = true;
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		return res;
	}

	private String validate(Up0060Request req) {
		return null;
	}
}
