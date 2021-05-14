package jp.co.nci.iwf.component.document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocContentsInfo;

@ApplicationScoped
public class DocContentsInfoRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/** 文書コンテンツ情報取得. */
	public MwtDocContentsInfo getMwtDocContent(long docId) {
		final Object[] params = {docId};
		return selectOne(MwtDocContentsInfo.class, getSql("DC0013"), params);
	}

	/** 文書コンテンツ情報登録. */
	public void insert(MwtDocContentsInfo entity) {
		// 文書コンテンｔヌIDを採番
		entity.setDocContentsId(numbering.newPK(MwtDocContentsInfo.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
	}

	/** 文書コンテンツ情報更新. */
	public void update(MwtDocContentsInfo org) {
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}
}
