package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BinderInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BinderInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtBinderInfo;

@ApplicationScoped
public class BinderRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/**
	 * バインダー情報Entity取得.
	 * @param docId 文書ID
	 */
	public BinderInfoEntity getBinderInfoEntity(Long docId) {
		if (docId != null) {
			final List<Object> params = new ArrayList<>();
			params.add(docId);
			return selectOne(BinderInfoEntity.class, getSql("DC0021"), params.toArray());
		} else {
			final BinderInfoEntity entity = new BinderInfoEntity();
			return entity;
		}
	}

	/** バインダー情報取得. */
	public MwtBinderInfo getMwtBinderInfo(long docId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return selectOne(MwtBinderInfo.class, getSql("DC0021"), params.toArray());
	}

	/** バインダー情報登録. */
	public void insert(BinderInfo inputed) {
		final MwtBinderInfo entity = new MwtBinderInfo();
		entity.setBinderId(numbering.newPK(MwtBinderInfo.class));
		entity.setDocId(inputed.docId);
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
	}

	/** バインダー情報更新. */
	public void update(MwtBinderInfo org, BinderInfo inputed) {
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}
}
