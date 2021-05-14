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
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtAttachFileWf;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * ブロック：添付ファイルのサービス
 */
@BizLogic
public class Bl0003Service extends BaseService implements CodeMaster {

	@Inject
	protected Bl0003Repository repository;

	@Inject
	private DownloadNotifyService notify;

	public List<AttachFileWfInfo> getAttachFileWfList(String corporationCode, Long processId) {
		List<AttachFileWfInfo> results = new ArrayList<>();
		List<MwtAttachFileWf> l = repository.getAttachFileWfList(corporationCode, processId);
		for (int i = 0; i < l.size(); i++) {
			AttachFileWfInfo e = new AttachFileWfInfo(l.get(i));
			e.rowNo = (i + 1L);
			results.add(e);
		}
		return results;
	}

	@Transactional
	public Bl0003Response upload(FormDataMultiPart multiPart) {
		Bl0003Response res = createResponse(Bl0003Response.class, null);
		res.attachFileWfList = new ArrayList<>();

		try {
			for (BodyPart bodyPart : multiPart.getBodyParts()) {
				final UploadFile file = new UploadFile(bodyPart);
				res.attachFileWfList.add(new AttachFileWfInfo(repository.upload(file)));
			}
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}
		res.success = true;
		return res;
	}

	@Transactional
	public Bl0003Response delete(Long attachFileWfId) {
		Bl0003Response res = createResponse(Bl0003Response.class, null);
		res.attachFileWfList = new ArrayList<>();
		repository.delete(attachFileWfId);

		MwtAttachFileWf e = new MwtAttachFileWf();
		e.setAttachFileWfId(attachFileWfId);
		res.attachFileWfList.add(new AttachFileWfInfo(e));
		res.success = true;
		return res;
	}

	public Response download(Long attachFileWfId) {
		notify.begin();
		try {
			MwtAttachFileWf e = repository.get(attachFileWfId);
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
		if (isEmpty(req.additionAttachFileWfList)) {
			return;
		}
		for (AttachFileWfInfo input : req.additionAttachFileWfList) {
			MwtAttachFileWf e = repository.get(input.attachFileWfId);
			if (e != null) {
				e.setCorporationCode(req.contents.corporationCode);
				e.setProcessId(req.contents.processId);
				e.setComments(input.comments);
				repository.update(e);
			}
		}
	}

	public void remove(Vd0310ExecuteRequest req) {
		if (isEmpty(req.removeAttachFileWfIdList)) {
			return;
		}
		for (Long attachFileWfId : req.removeAttachFileWfIdList) {
			MwtAttachFileWf e = repository.get(attachFileWfId);
			e.setDeleteFlag(DeleteFlag.ON);
			repository.update(e);
		}
	}

	/**
	 * 文書管理側からワークフロー側へ添付ファイルデータのコピー処理.
	 * @param src 文書管理添付ファイル情報一覧
	 * @return ワークフロー添付ファイル情報一覧
	 */
	public List<AttachFileWfInfo> copyAttachFileDoc2Wf(List<AttachFileDocInfo> files) {
		final List<AttachFileWfInfo> list = new ArrayList<>();
		for (AttachFileDocInfo file : files) {
			list.add(new AttachFileWfInfo( repository.copyAttachFileDoc2Wf(file.attachFileDocId) ));
		}
		return list;
	}
}
