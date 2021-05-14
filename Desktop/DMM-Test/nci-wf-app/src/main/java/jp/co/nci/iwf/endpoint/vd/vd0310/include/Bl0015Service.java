package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileWf;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * ブロック：ワークフロー文書ファイルのサービス
 */
@BizLogic
public class Bl0015Service extends BaseService implements CodeMaster {

	@Inject
	protected Bl0015Repository repository;

	@Inject
	private DownloadNotifyService notify;

	public List<DocFileWfInfo> getDocFileWfList(String corporationCode, Long processId) {
		List<DocFileWfInfo> results = new ArrayList<>();
		List<MwtDocFileWf> l = repository.getDocFileWfList(corporationCode, processId);
		for (int i = 0; i < l.size(); i++) {
			DocFileWfInfo e = new DocFileWfInfo(l.get(i));
			e.rowNo = (i + 1L);
			results.add(e);
		}
		return results;
	}

	@Transactional
	public Bl0015Response upload(FormDataMultiPart multiPart) {
		Bl0015Response res = createResponse(Bl0015Response.class, null);
		res.docFileWfList = new ArrayList<>();

		try {
			for (BodyPart bodyPart : multiPart.getBodyParts()) {
				final UploadFile file = new UploadFile(bodyPart);
				res.docFileWfList.add(new DocFileWfInfo(repository.upload(file)));
			}
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}
		res.success = true;
		return res;
	}

	@Transactional
	public Bl0015Response delete(Long docFileWfId) {
		Bl0015Response res = createResponse(Bl0015Response.class, null);
		res.docFileWfList = new ArrayList<>();
		repository.delete(docFileWfId);

		MwtDocFileWf e = new MwtDocFileWf();
		e.setDocFileWfId(docFileWfId);
		res.docFileWfList.add(new DocFileWfInfo(e));
		res.success = true;
		return res;
	}

	public Response download(Long docFileWfId) {
		notify.begin();
		try {
			MwtDocFileWf e = repository.get(docFileWfId);
			if (e == null) {
				throw new WebApplicationException(Response.Status.BAD_REQUEST);
			}
			return DownloadUtils.download(e.getFileName(), e.getFileData());
		} finally {
			notify.end();
		}
	}

	public void execute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 追加処理
		create(req);
		// 削除処理
		remove(req);
	}

	private void create(Vd0310ExecuteRequest req) {
		if (isEmpty(req.additionDocFileWfList)) {
			return;
		}
		for (DocFileWfInfo input : req.additionDocFileWfList) {
			MwtDocFileWf e = repository.get(input.docFileWfId);
			if (e != null) {
				e.setCorporationCode(req.contents.corporationCode);
				e.setProcessId(req.contents.processId);
				e.setComments(input.comments);
				e.setFileSize(input.fileSize);
				repository.update(e);
			}
		}
	}

	public void remove(Vd0310ExecuteRequest req) {
		if (isEmpty(req.removeDocFileWfIdList)) {
			return;
		}
		for (Long docFileWfId : req.removeDocFileWfIdList) {
			MwtDocFileWf e = repository.get(docFileWfId);
			e.setDeleteFlag(DeleteFlag.ON);
			repository.update(e);
		}
	}

	/**
	 * 文書管理側からワークフロー側へ文書ファイルデータのコピー処理.
	 * @param src 文書ファイル情報一覧
	 * @return ワークフロー文書ファイル情報一覧
	 */
	public List<DocFileWfInfo> copyDocFileDoc2Wf(List<DocFileInfo> files) {
		final List<DocFileWfInfo> list = new ArrayList<>();
		for (DocFileInfo file : files) {
			list.add(new DocFileWfInfo( repository.copyDocFileDoc2Wf(file.docFileId) ));
		}
		return list;
	}
}
