package jp.co.nci.iwf.component.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 文書ファイルデータサービス.
 */
@BizLogic
public class DocFileDataService extends BaseService {

	@Inject
	private DocFileDataRepository repository;
	@Inject
	private DownloadNotifyService notify;

	/**
	 * 文書ファイルデータ登録.
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public DocFileDataResponse upload(FormDataMultiPart multiPart) {
		return this.upload(multiPart, false);
	}

	/**
	 * 文書ファイルデータ登録.
	 * @param multiPart
	 * @param isDocFile 文書ファイルか
	 * @return
	 */
	@Transactional
	public DocFileDataResponse upload(FormDataMultiPart multiPart, boolean isDocFile) {
		DocFileDataResponse res = createResponse(DocFileDataResponse.class, null);
		res.fileDatas = new ArrayList<>();

		try {
			for (BodyPart bodyPart : multiPart.getBodyParts()) {
				final UploadFile file = new UploadFile(bodyPart);
				res.fileDatas.add(repository.insert(file, isDocFile));
			}
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}

		res.success = true;
		return res;
	}

	/**
	 * 文書ファイルデータの削除フラグ更新.
	 * 削除フラグが"1:削除済"のファイルデータを"0:未削除"へと更新する
	 * @param docFileDataIds 文書ファイルデータID一覧
	 */
	public void updateDeleteFlag(Set<Long> docFileDataIds) {
		repository.update(docFileDataIds, sessionHolder.getLoginInfo());
	}

//	/**
//	 * 文書ファイルデータ削除.
//	 * @param docFileDataId 文書ファイルデータID
//	 * @return
//	 */
//	public void deleteMwtDocFileData(long docFileDataId) {
//		List<Long> deleteDocFileDataIds = new ArrayList<>();
//		deleteDocFileDataIds.add(docFileDataId);
//		this.deleteMwtDocFileDataList(deleteDocFileDataIds);
//	}

	/**
	 * 文書ファイルデータの一括削除(物理削除).
	 * なお削除されるデータは削除フラグが"1:削除済"のもの
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public void deleteMwtDocFileDataList(Set<Long> deleteDocFileDataIds) {
		repository.deleteMwtDocFileData(deleteDocFileDataIds);
	}

	/**
	 * 文書ファイルデータダウンロード.
	 * @param docFileDataId 文書ファイルID
	 * @return
	 */
	public Response download(Long docFileDataId) {
		notify.begin();
		try {
			MwtDocFileData d = repository.getMwtDocFileDataByPk(docFileDataId);
			if (d == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return DownloadUtils.download(d.getFileName(), d.getFileData());
		} finally {
			notify.end();
		}
	}

	/**
	 * ワークフロー側から文書管理側へ文書ファイルデータのコピー処理.
	 * @param docFileWfId ワークフロー文書ファイルID
	 * @return 文書ファイルデータID
	 */
	public List<DocFileInfo> copyDocFileWf2Doc(List<DocFileWfInfo> files) {
		final List<DocFileInfo> list = new ArrayList<>();
		for (DocFileWfInfo file : files) {
			DocFileInfo bean = new DocFileInfo(repository.copyDocFileWf2Doc(file.docFileWfId));
			bean.comments = file.comments;
			bean.docFileWfId = file.docFileWfId;
			list.add(bean);
		}
		return list;
	}

	/**
	 * ワークフロー側から文書管理側へ添付ファイルデータのコピー処理.
	 * @param attachFileWfId ワークフロー添付ファイルID
	 * @return 文書ファイルデータID
	 */
	public List<AttachFileDocInfo> copyAttachFileWf2Doc(List<AttachFileWfInfo> files) {
		final List<AttachFileDocInfo> list = new ArrayList<>();
		for (AttachFileWfInfo file : files) {
			AttachFileDocInfo bean = new AttachFileDocInfo(repository.copyAttachFileWf2Doc(file.attachFileWfId));
			bean.comments = file.comments;
			list.add(bean);
		}
		return list;
	}
}
