package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocUpdateLog;

@ApplicationScoped
public class DocUpdateLogRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/** 文書更新履歴の件数取得. */
	public int countMwtDocUpdateLog(long docVersionId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_10"));
		final List<Object> params = new ArrayList<>();
		params.add(docVersionId);
		return count(sql.toString(), params.toArray());
	}

	/** 文書更新履歴一覧取得. */
	public List<MwtDocUpdateLog> getMwtDocUpdateLog(long docId, String corporationCode, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);
		params.add(docId);
		return select(MwtDocUpdateLog.class, getSql("DC0100_13"), params.toArray());
	}

	/** 文書更新履歴登録. */
	public void insert(MwtDocUpdateLog entity) {
		// 文書更新履歴IDを採番
		entity.setDocUpdateLogId(numbering.newPK(MwtDocUpdateLog.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
	}
}
