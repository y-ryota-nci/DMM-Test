package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.DocFileDataResponse;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.component.document.DocFileOperationResponse;
import jp.co.nci.iwf.component.document.DocFileService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書ファイルブロック：文書ファイルのサービス
 */
@BizLogic
public class DcBl0006Service extends BaseService implements CodeMaster {

	/** 文書ファイルサービス */
	@Inject private DocFileService docFileService;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		if (res.contents.docId != null) {
			res.contents.docFiles = docFileService.getDocFileInfoList(res.contents.docId);
		}
		// コピー対象の文書IDがある場合、コピー対象の文書情報に紐づいている文書ファイル一覧を取得
		else if (req.copyDocId != null) {
			res.contents.docFiles = docFileService.getDocFileInfoList(req.copyDocId);
			// コピーなので文書ファイルID、文書添付ファイルIDや文書ID等は消しておく
			res.contents.docFiles.forEach(e -> {
				e.docFileId = null;
				e.version = null;
				e.corporationCode = null;
				e.docFileNum = null;
				e.docId = null;
			});
		}
	}

	/**
	 * 文書情報に紐づく文書ファイル一覧取得.
	 * @param req
	 * @return
	 */
	public DcBl0006Response getDocFiles(DcBl0006Request req) {
		if (req.docId == null) {
			throw new BadRequestException("文書IDが未指定です");
		}
		final DcBl0006Response res = createResponse(DcBl0006Response.class, req);
		res.docFiles = docFileService.getDocFileInfoList(req.docId);
		res.success = true;
		return res;
	}

	/**
	 * 文書ファイル(1件)取得
	 * @param req
	 * @return
	 */
	public DocFileOperationResponse getDocFile(DocFileOperationRequest req) {
		if (req.docId == null || req.docFileId == null) {
			throw new BadRequestException("文書IDまたは文書ファイルIDが未指定です");
		}
		// 文書ファイル情報取得
		final DocFileInfoEntity docFileInfoEntity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);

		final DocFileOperationResponse res = createResponse(DocFileOperationResponse.class, req);
		res.docFiles = new ArrayList<DocFileInfo>();
		res.docFiles.add(new DocFileInfo(docFileInfoEntity, sessionHolder.getLoginInfo()));
		res.success = true;
		return res;
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
			// ロックされた文書ファイル情報を取得
			final DocFileInfoEntity entity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);
			final DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, req);
			res2.docFiles = new ArrayList<>();
			res2.docFiles.add(new DocFileInfo(entity, sessionHolder.getLoginInfo()));
			res2.success = true;
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
			// ロック解除された文書ファイル情報を取得
			final DocFileInfoEntity entity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);
			final DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, req);
			res2.docFiles = new ArrayList<>();
			res2.docFiles.add(new DocFileInfo(entity, sessionHolder.getLoginInfo()));
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイル移動処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse move(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.move(req);
		return res;
	}

	/**
	 * 文書ファイルコピー処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse copy(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.copy(req);
		return res;
	}

	/**
	 * 文書ファイル削除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(DocFileOperationRequest req) {
		// 文書ファイルの削除処理
		final BaseResponse res = docFileService.delete(req);
		// エラーでなければファイルデータの削除処理
		// 削除フラグが"1:削除済"のファイルデータだけが削除される
		if (isEmpty(res.alerts)) {
			final Set<Long> deleteDocFileDataIds = req.deleteDocFiles.stream()
					.filter(e -> isEmpty(e.docFileId))
					.map(e -> e.docFileDataId)
					.collect(Collectors.toSet());
			docFileDataService.deleteMwtDocFileDataList(deleteDocFileDataIds);
		}
		return res;
	}

	/**
	 * 文書ファイルデータ登録.
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public BaseResponse upload(FormDataMultiPart multiPart) {
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
	 * 文書ファイル情報およびファイルデータの登録・更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		// ロックフラグ trueであればロックする
		// unlockFlagは「0：ロック解除しない、1：ロック解除する」なので気をつけて
		final boolean isLock = eq(CommonFlag.OFF, req.versionInfo.unlockFlag);
		// WFからの文書連携か
		final boolean isFromWf = eq(CommonFlag.ON, req.fromCoopWfFlag);
		// 文書ファイル情報の差分更新
		docFileService.saveMwtDocFileInfo(docId, req.docFiles, isLock, true, isFromWf);
	}
}
