package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocWfRelation;

/**
 * 文書情報-WF連携用のリポジトリ
 */
@ApplicationScoped
public class DocWfRelationRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/**
	 * 文書IDをキーに文書WF連携情報一覧取得.
	 * @param docId 文書ID
	 * @return 文書WF連携情報一覧
	 */
	public List<MwtDocWfRelation> getMwtDocWfRelationList(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_24"));
		sql.append(" A.DOC_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		// ソート順
		// 文書WF連携IDの降順にてソートし、最新のWF連携情報が上位にくるようにする
		sql.append(toSortSql("DOC_WF_RELATION_ID", false));

		return select(MwtDocWfRelation.class, sql, params.toArray());
	}

	/** 文書WF連携情報登録. */
	public void insertMwtDocWfRelation(String corporationCode, Long processId, Long docId, String relationType) {
		final MwtDocWfRelation entity = new MwtDocWfRelation();
		entity.setDocWfRelationId(numbering.newPK(MwtDocWfRelation.class));
		entity.setCorporationCode(corporationCode);
		entity.setProcessId(processId);
		entity.setDocId(docId);
		entity.setRelationType(relationType);
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
	}

	/** WF連携で使用する画面プロセス定義取得. */
	public MwmScreenProcessDef getMwmScreenProcessDef(String corporationCode, String screenProcessCode) {
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(screenProcessCode);
		return select(MwmScreenProcessDef.class, getSql("DC0100_22"), params.toArray()).stream().findFirst().orElse(null);
	}
}
