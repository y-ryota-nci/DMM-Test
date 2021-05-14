package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtAttachFileWf;

/**
 * ブロック：添付ファイルのリポジトリ
 */
@ApplicationScoped
public class Bl0003Repository extends BaseRepository implements CodeMaster {

	@Inject
	private NumberingService numbering;

	public List<MwtAttachFileWf> getAttachFileWfList(String corporationCode, Long processId) {
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(processId);
		return select(MwtAttachFileWf.class, getSql("VD0310_04"), params.toArray());
	}

	public MwtAttachFileWf upload(UploadFile file) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MwtAttachFileWf e = new MwtAttachFileWf();
		e.setAttachFileWfId(numbering.newPK(MwtAttachFileWf.class));
		e.setCorporationCode(null);
		e.setProcessId(null);
		e.setFileName(file.fileName);
		byte [] buffer = new byte[1024];
		while(true) {
			int len = file.stream.read(buffer);
			if (len < 0) break;
			out.write(buffer, 0, len);
		}
		byte[] fileData = out.toByteArray();
		e.setFileData(fileData);
		e.setFileSize(fileData.length);
		e.setDeleteFlag(DeleteFlag.OFF);
		em.persist(e);
		em.flush();
		return e;
	}

	public MwtAttachFileWf get(Long attachFileWfId) {
		return em.find(MwtAttachFileWf.class, attachFileWfId);
	}

	public void delete(Long attachFileWfId) {
		final List<Object> params = new ArrayList<>();
		params.add(attachFileWfId);
		execSql(getSql("VD0310_05"), params.toArray());
	}

	public void update(MwtAttachFileWf entity) {
		em.merge(entity);
		em.flush();
	}

	/**
	 * 文書管理側からワークフロー側へ添付ファイルデータのコピー処理.
	 * @param attachFileDocId 文書管理添付ファイルID
	 * @return ワークフロー添付ファイル情報
	 */
	public MwtAttachFileWf copyAttachFileDoc2Wf(Long attachFileDocId) {
		final long attachFileWfId = numbering.newPK(MwtAttachFileWf.class);
		final Object[] params = { attachFileWfId, attachFileDocId };
		execSql(getSql("VD0310_19"), params);
		return get(attachFileWfId);
	}
}
