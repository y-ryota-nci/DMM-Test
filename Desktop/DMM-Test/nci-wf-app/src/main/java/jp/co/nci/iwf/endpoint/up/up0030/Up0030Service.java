package jp.co.nci.iwf.endpoint.up.up0030;

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
import jp.co.nci.iwf.component.menu.download.MenuRoleDownloadDto;
import jp.co.nci.iwf.component.menu.upload.MenuRoleUploadService;
import jp.co.nci.iwf.endpoint.up.CommonUploadRepository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadFile;
import jp.co.nci.iwf.util.ZipExtractor;

/**
 * メニューロール定義アップロード画面サービス
 */
@BizLogic
public class Up0030Service extends BaseService {

	private static final String entryName = MenuRoleDownloadDto.class.getSimpleName();

	@Inject private MenuRoleUploadService uploader;
	@Inject private CommonUploadRepository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Up0030Response init(Up0030Request req) {
		final Up0030Response res = createResponse(Up0030Response.class, req);

		// アップロードファイルIDがあれば、それを初期表示
		if (req.uploadFileId != null) {
			// アップロードファイル情報を抽出
			final MwtUploadFile up = repository.getMwtUploadFile(req.uploadFileId);
			if (up == null)
				throw new NotFoundException("アップロードファイル情報が見つかりません（uploadFileId=" + req.uploadFileId + "）");

			// バイナリデータをZIPファイルみなして、ダウンロードDTOへ復元
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(up.getFileData()))) {
				final MenuRoleDownloadDto dto = ze.extractAsJSON(entryName, MenuRoleDownloadDto.class);
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
	private void fillResponse(MwtUploadFile up, MenuRoleDownloadDto dto, Up0030Response res) {
		res.uploadFileId = up.getUploadFileId();
		res.fileName = up.getFileName();
		res.corporationCode = dto.corporationCode;
		res.corporationName = dto.corporationName;
		res.menuRoles = dto.menuRoleList;
		res.timestampCreated = FORMATTER_DATETIME.get().format(dto.timestampCreated);
		res.dbDestination = dto.dbDestination + "@" + dto.dbUser;
		res.appVersion = dto.appVersion;
		res.hostIpAddr = dto.hostIpAddr;
		res.hostName = dto.hostName;
	}

	/**
	 * メニューロール定義のアップロード
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public Up0030Response upload(FormDataMultiPart multiPart) {
		final Up0030Response res = createResponse(Up0030Response.class, null);
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				// アップロードファイルをバイト配列化
				final byte[] data = toBytes(f.stream);

				// バイト配列をZIPファイルとみなして解凍
				try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(data))) {
					// 互換性を高めるため、JSON形式でZIP化されている
					MenuRoleDownloadDto dto = ze.extractAsJSON(entryName, MenuRoleDownloadDto.class);

					if (dto == null) {
						// エントリがない
						throw new FileNotFoundException("メニューロール定義データが含まれていません");
					} else {
						// アップロードファイル情報を登録
						final MwtUploadFile up = repository.insertMwtUploadFile(dto, data, f.fileName, UploadKind.MENU_ROLE);

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
	 * メニューロール定義の登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Up0030Response register(Up0030Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Up0030Response res = createResponse(Up0030Response.class, req);

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
				final MenuRoleDownloadDto dto = ze.extractAsJSON(entryName, MenuRoleDownloadDto.class);

				// アップロード内容をDBへ反映
				uploader.persist(corporationCode, req, dto);

				// アップロードファイル情報を登録済みへ更新
				repository.complateToRegister(up, req);

				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.menuRole));
				res.success = true;
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		return res;
	}

	/** バリデーション */
	private String validate(Up0030Request req) {
		return null;
	}
}
