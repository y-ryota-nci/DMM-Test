package jp.co.nci.iwf.endpoint.dc.dc0101;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocFileDataResponse;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.component.document.DocFileOperationResponse;
import jp.co.nci.iwf.component.document.DocFileService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoHistoryEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 文書ファイル更新画面サービス.
 */
@BizLogic
public class Dc0101Service extends BaseService implements CodeMaster, DcCodeBook {

	/** 文書ファイルサービス */
	@Inject protected DocFileService docFileService;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0101InitResponse init(Dc0101InitRequest req) {

		// 起動パラメータのバリデーション
		validateInitParams(req);

		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書ファイル情報を取得
		DocFileInfoEntity docFileInfoEntity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);
		validateDocInfoEntity(req, docFileInfoEntity);
		// 文書ファイル更新履歴一覧を取得
		List<DocFileInfoHistoryEntity> histories = docFileService.getDocFileInfoHistoryEntityList(req.docFileId);
		// バージョン更新用の選択肢を生成
		final List<OptionItem> updateVersionTypes = new ArrayList<>();
		updateVersionTypes.add(new OptionItem(UpdateVersionType.DO_NOT_UPDATE, i18n.getText(MessageCd.MSG0214, docFileInfoEntity.majorVersion, docFileInfoEntity.minorVersion)));
		updateVersionTypes.add(new OptionItem(UpdateVersionType.MINOR_VERSION_UP, i18n.getText(MessageCd.MSG0215, docFileInfoEntity.majorVersion, docFileInfoEntity.minorVersion+1)));
		updateVersionTypes.add(new OptionItem(UpdateVersionType.MAJOR_VERSION_UP, i18n.getText(MessageCd.MSG0216, docFileInfoEntity.majorVersion+1, 0)));

		// 戻り値生成
		final Dc0101InitResponse res = createResponse(Dc0101InitResponse.class, req);
		res.docFileInfo = new DocFileInfo(docFileInfoEntity, login);
		res.histories = histories.stream().map(e -> new DocFileInfo(e)).collect(Collectors.toList());
		res.updateVersionTypes = updateVersionTypes;
		res.success = true;

		return res;
	}

	/** 起動パラメータのバリデーション */
	private void validateInitParams(Dc0101InitRequest req) {
		if (req.docFileId == null) {
			throw new BadRequestException("文書ファイルIDが未指定です");
		}
	}

	/** 文書ファイル情報の整合性チェック */
	private void validateDocInfoEntity(Dc0101InitRequest req, DocFileInfoEntity docFileInfoEntity) {
		if (docFileInfoEntity == null)
			throw new NotFoundException("文書ファイル情報が見つかりません ->"
					+ " docFileId=" + req.docFileId);
	}

	/**
	 * 文書ファイルデータ登録.
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public BaseResponse upload(FormDataMultiPart multiPart) {
		if (multiPart != null && multiPart.getBodyParts().size() > 1) {
			throw new InvalidUserInputException(MessageCd.MSG0208, 1);
		}
		final DocFileDataResponse res = docFileDataService.upload(multiPart, true);
		if (res.success && isEmpty(res.alerts)) {
			final DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, null);
			res2.docFiles = res.fileDatas.stream().map(d -> new DocFileInfo(d)).collect(Collectors.toList());
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイルデータダウンロード.
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public Response download(Long docFileDataId) {
		return docFileDataService.download(docFileDataId);
	}

	/**
	 * 文書ファイルロック処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse lock(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.lock(req);
		if (res.success) {
			// 再描画するためinitを呼び出す
			Dc0101InitRequest req2 = new Dc0101InitRequest();
			req2.docId = req.docId;
			req2.docFileId = req.docFileId;
			final Dc0101InitResponse res2 = init(req2);
			res2.addSuccesses(i18n.getText(MessageCd.MSG0226, res2.docFileInfo.fileName));
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイルロック解除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse unlock(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.unlock(req);
		if (res.success) {
			// 再描画するためinitを呼び出す
			Dc0101InitRequest req2 = new Dc0101InitRequest();
			req2.docId = req.docId;
			req2.docFileId = req.docFileId;
			final Dc0101InitResponse res2 = init(req2);
			res2.addSuccesses(i18n.getText(MessageCd.MSG0227, res2.docFileInfo.fileName));
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイル更新処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse update(Dc0101ExecuteRequest req) {
		// 入力パラメータのバリデーション
		this.validateExecuteParams(req);

		BaseResponse res = docFileService.update(req.docFileInfo, req.updateVersionType);
		if (res != null && res.success) {
			// 再描画するためinitを呼び出す
			Dc0101InitRequest req2 = new Dc0101InitRequest();
			req2.docId = req.docFileInfo.docId;
			req2.docFileId = req.docFileInfo.docFileId;
			final Dc0101InitResponse res2 = init(req2);
			res2.addSuccesses(i18n.getText(MessageCd.MSG0067, res2.docFileInfo.fileName));
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイル削除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(DocFileOperationRequest req) {
		// 文書ファイル情報の削除処理
		final BaseResponse res = docFileService.delete(req);
		// エラーでなければファイルデータの削除処理
		if (isEmpty(res.alerts)) {
			final Set<Long> deleteDocFileDataIds = req.deleteDocFiles.stream()
					.map(e -> e.docFileDataId)
					.collect(Collectors.toSet());
			docFileDataService.deleteMwtDocFileDataList(deleteDocFileDataIds);
		}
		return res;
	}

	/** 更新パラメータのバリデーション. */
	private void validateExecuteParams(Dc0101ExecuteRequest req) {
		if (req.docFileInfo == null)
			throw new BadRequestException("文書ファイル情報がありません。");
		if (req.docFileInfo.docId == null || req.docFileInfo.docFileId == null || req.docFileInfo.version == null)
			throw new BadRequestException("文書IDまたは文書ファイルIDまたはバージョンが未指定です");
		if (isEmpty(req.updateVersionType))
			throw new BadRequestException("バージョン更新区分が未指定です");
	}
}
