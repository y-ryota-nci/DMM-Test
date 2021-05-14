package jp.co.nci.iwf.endpoint.up.up0010;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.component.download.DownloadDtoHolder;
import jp.co.nci.iwf.designer.service.download.ScreenDownloadDto;
import jp.co.nci.iwf.designer.service.upload.ScreenUploadService;
import jp.co.nci.iwf.endpoint.up.CommonUploadRepository;
import jp.co.nci.iwf.endpoint.vd.vd0030.Up0010SaveConfig;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadFile;
import jp.co.nci.iwf.util.ZipExtractor;

/**
 * 画面定義アップロード画面サービス
 */
@BizLogic
public class Up0010Service extends BaseService {
	@Inject private ScreenUploadService uploader;
	@Inject private CommonUploadRepository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Up0010Response init(Up0010Request req) {
		final Up0010Response res = createResponse(Up0010Response.class, req);

		// アップロードファイルIDがあれば、それを初期表示
		if (req.uploadFileId != null) {
			final MwtUploadFile up = repository.getMwtUploadFile(req.uploadFileId);
			if (up == null)
				throw new NotFoundException("アップロードファイル情報が見つかりません（uploadFileId=" + req.uploadFileId + "）");

			// バイナリデータをZIPファイルみなして、ダウンロードDTOへ復元
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(up.getFileData()))) {
				final DownloadDtoHolder<ScreenDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
				fillResponse(up, holder, res);
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		res.success = true;
		return res;
	}

	/** レスポンスにアップロード内容を反映 */
	private void fillResponse(MwtUploadFile up, DownloadDtoHolder<ScreenDownloadDto> holder, Up0010Response res) {
		// レスポンス
		res.uploadFileId = up.getUploadFileId();
		res.fileName = up.getFileName();
		res.corporationCode = holder.corporationCode;
		res.corporationName = holder.corporationName;
		res.timestampCreated = FORMATTER_DATETIME.get().format(holder.timestampCreated);
		res.dbDestination = holder.dbDestination + "@" + holder.dbUser;
		res.appVersion = holder.appVersion;
		res.hostIpAddr = holder.hostIpAddr;
		res.hostName = holder.hostName;
		res.results = toResults(holder);
		res.allCount = res.results.size();
		res.count = res.allCount;
	}

	/**
	 * 画面定義のアップロード
	 * @param multiPart
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Up0010Response upload(FormDataMultiPart multiPart) {
		final Up0010Response res = createResponse(Up0010Response.class, null);
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			// アップロードファイルをバイト配列化
			final UploadFile f = new UploadFile(bodyPart);
			final byte[] data = toBytes(f.stream);

			// バイト配列をZIPファイルとみなして解凍
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(data))) {
				// 互換性を高めるため、JSON形式でZIP化されている
				DownloadDtoHolder<ScreenDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
				if (holder == null || holder.dtoList == null || holder.dtoList.isEmpty()) {
					// エントリがない
					throw new FileNotFoundException("画面定義データが含まれていません");
				}
				BaseDownloadDto dto = holder.dtoList.get(0);
				if (dto == null)
					throw new FileNotFoundException("DTOがNULLです");
				if (!(dto instanceof ScreenDownloadDto))
					throw new FileNotFoundException("DTOが画面定義データではありません");

				// アップロードファイル情報を登録
				MwtUploadFile up = repository.insertMwtUploadFile(holder, data, f.fileName, UploadKind.SCREEN);

				// レスポンス
				fillResponse(up, holder, res);
				res.success = true;
			}
			catch (IOException e) {
				// JSONでパースできないか、クラスのシリアライズバージョンが異なるか、そもそもZIPファイルじゃない
				throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
			}
			break;
		}
		return res;
	}

	/** 画面リストの中にコンテナリストを内包するように構造化 */
	private List<Up0010Entity> toResults(DownloadDtoHolder<ScreenDownloadDto> holder) {
		final List<Up0010Entity> results = new ArrayList<>();
		for (ScreenDownloadDto dto : holder.dtoList) {
			dto.screenList.sort(new Comparator<MwmScreen>() {
				@Override
				public int compare(MwmScreen s1, MwmScreen s2) {
					return compareTo(s1.getScreenCode(), s2.getScreenCode());
				}
			});
			for (MwmScreen screen : dto.screenList) {
				final Up0010Entity r = new Up0010Entity();
				r.corporationCode = screen.getCorporationCode();
				r.screenCode = screen.getScreenCode();
				r.screenName = screen.getScreenName();
				r.containerCodes = new ArrayList<>();
				for (MwmContainer c : dto.containerList) {
					Map<String, String> map = new HashMap<>();
					map.put("containerCode", c.getContainerCode());
					r.containerCodes.add(map);
				}
				results.add(r);
			}
		}
		return results;
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Up0010Response register(Up0010Request req) {
		final Up0010Response res = createResponse(Up0010Response.class, req);

		if (req.uploadFileId == null)
			throw new BadRequestException("アップロードファイルIDが未指定です");
		if (req.configs == null || req.configs.isEmpty())
			throw new BadRequestException("アップロード設定リストが未指定です");

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
				final DownloadDtoHolder<ScreenDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
				final Map<String, Up0010SaveConfig> configs = req.configs.stream()
						.collect(Collectors.toMap(c -> c.screenCode, c -> c));
				for (ScreenDownloadDto dto : holder.dtoList) {
					// アップロード設定があれば、その画面定義をアップロードしてよい
					for (MwmScreen screen : dto.screenList) {
						final Up0010SaveConfig config = configs.get(screen.getScreenCode());
						if (config == null) {
							continue;
						}
						// アップロード内容をDBへ反映
						uploader.persist(config, dto);

						// 処理結果
						String name = i18n.getText(MessageCd.screenDifinition);
						if (!dto.screenList.isEmpty()) {
							name += String.format("[%s %s]", screen.getScreenCode(), screen.getScreenName());
						}
						res.addSuccesses(i18n.getText(MessageCd.MSG0151, name));
					}
				}
				// アップロードファイル情報を登録済みへ更新
				repository.complateToRegister(up, req.configs);

				res.success = true;
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}
		}
		return res;
	}

	/** バリデーション */
	private String validate(Up0010Request req) {
		for (Up0010SaveConfig c : req.configs) {
			// 画面コード
			if (isEmpty(c.newScreenCode))
				return i18n.getText(MessageCd.MSG0001, MessageCd.screenCode);

			// コンテナコード
			if (c.newContainerCodes == null || c.newContainerCodes.isEmpty())
				return i18n.getText(MessageCd.MSG0001, MessageCd.containerCode);

			for (Map.Entry<String, String> entry : c.newContainerCodes.entrySet()) {
				// コンテナコード
				if (isEmpty(entry.getKey()) || isEmpty(entry.getValue()))
					return i18n.getText(MessageCd.MSG0001, MessageCd.containerCode);
			}
		}
		return null;
	}

	/**
	 * 画面定義の上書き登録確認
	 * @param req
	 * @return
	 */
	public Up0010Response confirm(Up0010Request req) {
		final Up0010Response res = createResponse(Up0010Response.class, req);

		for (Up0010SaveConfig c : req.configs) {
			// 新旧で差異がある場合のみ、既存の画面コードがあるかをチェック
			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			if (!eq(c.screenCode, c.newScreenCode) && uploader.existScreenCode(corporationCode, c.newScreenCode))
				res.confirmMessage = i18n.getText(MessageCd.MSG0154, MessageCd.screenCode);

			// 新旧で差異がある場合のみ、既存のコンテナコードがあるかをチェック
			for (Map.Entry<String, String> entry : c.newContainerCodes.entrySet()) {
				String containerCode = entry.getKey();
				String newContainerCode = entry.getValue();
				if (!eq(containerCode, newContainerCode) && uploader.existsContainerCode(corporationCode, newContainerCode)) {
					res.confirmMessage = i18n.getText(MessageCd.MSG0154, MessageCd.containerCode);
					break;
				}
			}
		}
		res.success = true;
		return res;
	}

}
