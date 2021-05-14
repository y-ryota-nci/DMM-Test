package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.AttachFileDocRequest;
import jp.co.nci.iwf.component.document.AttachFileDocResponse;
import jp.co.nci.iwf.component.document.AttachFileDocService;
import jp.co.nci.iwf.component.document.DocFileDataResponse;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 添付ファイルブロック：添付ファイルのサービス
 */
@BizLogic
public class DcBl0010Service extends BaseService implements CodeMaster {

	/** 文書管理用添付ファイルサービス */
	@Inject private AttachFileDocService attachFileDocService;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		if (res.contents.docId != null) {
			res.contents.attachFileDocs = attachFileDocService.getAttachFileDocInfoList(res.contents.docId);
		}
		// コピー対象の文書IDがある場合、コピー対象の文書情報に紐づいている文書添付ファイル一覧を取得
		else if (req.copyDocId != null) {
			res.contents.attachFileDocs = attachFileDocService.getAttachFileDocInfoList(req.copyDocId);
			// コピーなので文書添付ファイルIDや文書IDは消しておく
			res.contents.attachFileDocs.forEach(e -> {
				e.attachFileDocId = null;
				e.version = null;
				e.docId = null;
			});
		}
	}

	/**
	 * 添付ファイルデータ登録.
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public BaseResponse upload(FormDataMultiPart multiPart) {
		final DocFileDataResponse res = docFileDataService.upload(multiPart);
		if (res.success && isEmpty(res.alerts)) {
			final AttachFileDocResponse res2 = createResponse(AttachFileDocResponse.class, null);
			res2.attachFiles = res.fileDatas.stream().map(d -> new AttachFileDocInfo(d)).collect(Collectors.toList());
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * 添付ファイルデータダウンロード.
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public Response download(Long docFileDataId) {
		return docFileDataService.download(docFileDataId);
	}

	/**
	 * 添付ファイルデータ削除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(AttachFileDocRequest req) {
		// 文書ファイルデータの削除(物理削除)
		final Set<Long> deleteDocFileDataIds = req.deleteAttachFiles.stream()
				.filter(e -> isEmpty(e.attachFileDocId))
				.map(e -> e.docFileDataId)
				.collect(Collectors.toSet());
		docFileDataService.deleteMwtDocFileDataList(deleteDocFileDataIds);

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 文書添付ファイルおよびファイルデータの登録・更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		// WFからの文書連携か
		final boolean isFromWf = eq(CommonFlag.ON, req.fromCoopWfFlag);
		// 文書添付ファイルデータの差分更新
		attachFileDocService.saveMwtAttachFileDoc(docId, req.attachFileDocs, isFromWf);
	}
}
