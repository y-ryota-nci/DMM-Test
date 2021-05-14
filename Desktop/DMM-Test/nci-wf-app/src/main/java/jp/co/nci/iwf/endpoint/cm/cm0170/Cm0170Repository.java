package jp.co.nci.iwf.endpoint.cm.cm0170;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;

/**
 * プロセス掲示板情報のリポジトリ
 */
@ApplicationScoped
public class Cm0170Repository extends BaseRepository {

	/** 採番サービス */
	@Inject private NumberingService number;

	/** プロセス掲示板情報添付ファイルをインサート */
	public MwtBbsAttachFileWf insert(MwtBbsAttachFileWf file) {
		long bbsAttachFileWfId = number.newPK(MwtBbsAttachFileWf.class);
		file.setBbsAttachFileWfId(bbsAttachFileWfId);
		em.persist(file);
		em.flush();
		return em.find(MwtBbsAttachFileWf.class, bbsAttachFileWfId);
	}

	public MwtBbsAttachFileWf getMwtBbsAttachFileWf(Long bbsAttachFileWfId) {
		return em.find(MwtBbsAttachFileWf.class, bbsAttachFileWfId);
	}

}
