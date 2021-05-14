package jp.co.nci.iwf.endpoint.up.up0020;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.component.download.DownloadDtoHolder;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadDto;
import jp.co.nci.iwf.component.route.upload.ProcessDefUploadService;
import jp.co.nci.iwf.endpoint.up.CommonUploadRepository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadFile;
import jp.co.nci.iwf.util.ZipExtractor;

/**
 * プロセス定義アップロード画面サービス
 */
@BizLogic
public class Up0020Service extends BaseService {
    @Inject private CommonUploadRepository repository;
    @Inject private WfInstanceWrapper wf;
	@Inject private ProcessDefUploadService uploader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Up0020Response init(Up0020Request req) {
		final Up0020Response res = createResponse(Up0020Response.class, req);

		if (req.uploadFileId != null) {
			// アップロードファイル情報を抽出
			final MwtUploadFile up = repository.getMwtUploadFile(req.uploadFileId);
			if (up == null)
				throw new NotFoundException("アップロードファイル情報が見つかりません（uploadFileId=" + req.uploadFileId + "）");

			// バイナリデータをZIPファイルみなして、ダウンロードDTOへ復元
			try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(up.getFileData()))) {
				final DownloadDtoHolder<ProcessDefDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
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
	private void fillResponse(MwtUploadFile up, DownloadDtoHolder<ProcessDefDownloadDto> holder, Up0020Response res) {
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
	 * プロセス定義のアップロード
	 * @param multiPart
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Up0020Response upload(FormDataMultiPart multiPart) {
		final Up0020Response res = createResponse(Up0020Response.class, null);
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				// アップロードファイルをバイト配列化
				final byte[] data = toBytes(f.stream);

				// バイト配列をZIPファイルとみなして解凍
				try (ZipExtractor ze = new ZipExtractor(new ByteArrayInputStream(data))) {
					// 互換性を高めるため、JSON形式でZIP化されている
					DownloadDtoHolder<ProcessDefDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
					if (holder == null || holder.dtoList == null || holder.dtoList.isEmpty()) {
						// エントリがない
						throw new FileNotFoundException("画面定義データが含まれていません");
					}
					BaseDownloadDto dto = holder.dtoList.get(0);
					if (dto == null)
						throw new FileNotFoundException("DTOがNULLです");
					if (!(dto instanceof ProcessDefDownloadDto))
						throw new FileNotFoundException("DTOが画面定義データではありません");

					// アップロードファイル情報を登録
					MwtUploadFile up = repository.insertMwtUploadFile(holder, data, f.fileName, UploadKind.PROCESS);

					// レスポンス
					fillResponse(up, holder, res);
					res.success = true;
				}
	        }
			catch (IOException e) {
				// JSONでパースできないか、クラスのシリアライズバージョンが異なるか、そもそもZIPファイルじゃない
				throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
			}
		}
		return res;
	}

	/** アップロードファイルの概要をエンティティ化 */
	private List<Up0020Config> toResults(DownloadDtoHolder<ProcessDefDownloadDto> holder) {
		return holder.dtoList.stream()
				.flatMap(dto -> dto.processDefList.stream())
				.map(proc -> new Up0020Config(proc))
				.sorted((r1, r2) -> {
					int compare = compareTo(r1.corporationCode, r2.corporationCode);
					if (compare != 0)
						return compare;
					compare = compareTo(r1.processDefCode, r2.processDefCode);
					if (compare != 0)
						return compare;
					return compareTo(r1.processDefDetailCode, r2.processDefDetailCode);
				})
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Up0020Response register(Up0020Request req) {
		final String newCorporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Up0020Response res = createResponse(Up0020Response.class, req);

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
				final DownloadDtoHolder<ProcessDefDownloadDto> holder = ze.extractNextAsJSON(DownloadDtoHolder.class);
				final Map<String, Up0020Config> configs = req.configs.stream()
						.collect(Collectors.toMap(c -> toKey(c.corporationCode, c.processDefCode, c.processDefDetailCode), c -> c));

				boolean doneAssignRole = false, doneAssignRoleDetail = false;
				boolean doneChangeAssignRole = false, doneChangeRoleDetail = false;
				for (ProcessDefDownloadDto dto : holder.dtoList) {
					// アップロード設定があれば、その画面定義をアップロードしてよい
					for (WfmProcessDef proc : dto.processDefList) {
						final String key = toKey(proc.getCorporationCode(), proc.getProcessDefCode(), proc.getProcessDefDetailCode());
						final Up0020Config config = configs.get(key);
						if (config == null) {
							continue;
						}
						// 参加者ロールと参加者変更ロールはどんなときでも必ずアップロードされなければならないが
						// 各DTOは同時にダウンロードされているので、参加者ロールと参加者変更ロールはすべてのDTOで同一である。
						// よって参加者ロールと参加者変更ロールは未実施なら1回だけアップロード
						config.isAssignRole = !doneAssignRole;
						config.isChangeRole = !doneChangeAssignRole;

						// 参加者ロール構成と参加者変更ロール構成は、アップロード指定されていて未実施なら１回だけアップロード
						config.isAssignRoleDetail = (!doneAssignRoleDetail && req.isAssignRoleDetail);
						config.isChangeRoleDetail = (!doneChangeRoleDetail && req.isChangeRoleDetail);

						// アップロード内容をDBへ反映
						uploader.persist(newCorporationCode, config, dto);

						// 処理結果
						String name = i18n.getText(MessageCd.processDef);
						name += String.format("[%s %s]", config.newProcessDefCode, proc.getProcessDefName());
						res.addSuccesses(i18n.getText(MessageCd.MSG0151, name));

						// 参加者ロールと参加者ロール構成、参加者変更ロールと参加者変更ロール構成のアップロード状態を記録
						if (config.isAssignRole) doneAssignRole = true;
						if (config.isAssignRoleDetail) doneAssignRoleDetail = true;
						if (config.isChangeRole) doneChangeAssignRole = true;
						if (config.isChangeRoleDetail) doneChangeRoleDetail = true;
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

	private static String toKey(String corporationCode, String processDefCode, String processDefDetailCode) {
		return new StringBuilder(32)
				.append(corporationCode)
				.append("_")
				.append(processDefCode)
				.append("_")
				.append(processDefDetailCode)
				.toString();
	}

	/** バリデーション */
	private String validate(Up0020Request req) {
		for (Up0020Config c : req.configs) {
			if (isEmpty(c.newProcessDefCode))
				return i18n.getText(MessageCd.MSG0001, MessageCd.processDefCode);
			if (isEmpty(c.newProcessDefDetailCode))
				return i18n.getText(MessageCd.MSG0001, MessageCd.processDefDetailCode);
		}
		return null;
	}

	/**
	 * プロセス定義の上書き登録確認
	 * @param req
	 * @return
	 */
	public Up0020Response confirm(Up0020Request req) {
		final Up0020Response res = createResponse(Up0020Response.class, req);

		// 新旧で差異がある場合のみ、既存の画面コードがあるかをチェック
		for (Up0020Config c : req.configs) {
			if (!eq(c.processDefCode, c.newProcessDefCode)
					|| !eq(c.processDefDetailCode, c.newProcessDefDetailCode)
			) {
				final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
				in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
				in.setProcessDefCode(c.newProcessDefCode);
				in.setProcessDefDetailCode(c.newProcessDefDetailCode);
				if (!wf.searchWfmProcessDef(in).getProcessDefs().isEmpty()) {
					res.confirmMessage = i18n.getText(MessageCd.MSG0154, MessageCd.processDef);
					break;
				}
			}
		}
		res.success = true;
		return res;
	}

}
