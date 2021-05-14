package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtBinderInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtBinderInfoHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwtBizDocInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtBizDocInfoHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAccessibleHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAppendedInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfoHistory;

/**
 * 業務文書リポジトリ.
 */
@ApplicationScoped
public class Dc0100Repository extends BaseRepository {

	@Inject private NumberingService numbering;

	/** 文書管理(業務文書・バインダー)のおけるブロック一覧の取得. */
	public List<BlockDisplayEntity> getBlockDisplayList(String corporationCode, LookupGroupId lookupGroupId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_05"));
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(lookupGroupId.toString());
		params.add(localeCode);
		List<BlockDisplayEntity> result = select(BlockDisplayEntity.class, sql.toString(), params.toArray());
		if (result == null) {
			result = new ArrayList<>();
		}
		result.stream().forEach(e -> em.detach(e));
		return result;
	}

	/** 文書情報取得. */
	public MwtDocInfo getMwtDocInfo(Long docId) {
		if (docId == null) {
			return null;
		}
		return em.find(MwtDocInfo.class, docId);
	}

	/** 文書メモ一覧取得. */
	public List<MwtDocAppendedInfo> getMwtDocAppendedInfoList(long docId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return select(MwtDocAppendedInfo.class, getSql("DC0100_14"), params.toArray());
	}

	/** 文書メモ情報登録. */
	public void insert(MwtDocAppendedInfo entity) {
		// 文書メモIDを採番
		entity.setDocAppendedId(numbering.newPK(MwtDocAppendedInfo.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
	}

	/** 引数の文書IDに紐づく文書メモ情報の連番の最大値取得. */
	public int getMwtDocAppendedMaxSeq(long docId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return count(getSql("DC0100_15"), params.toArray());
	}

	/** 業務文書情報取得. */
	public MwtBizDocInfo getMwtBizDocInfo(long docId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return select(MwtBizDocInfo.class, getSql("DC0100_09"), params.toArray()).stream().findFirst().orElse(null);
	}

	/** 履歴の履歴連番更新. */
	public void updateHistorySeqNo(String tableName, long docId) {
		StringBuilder sql = new StringBuilder(getSql("DC0100_17").replaceFirst("###TABLE_NAME###", tableName));
		sql.append("DOC_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		execSql(sql, params.toArray());
	}

	/**
	 * 文書情報履歴登録.
	 * @param docId 文書ID
	 * @return 文書履歴ID
	 */
	public Long insertMwtDocInfoHistory(long docId) {
		final MwtDocInfo org = em.find(MwtDocInfo.class, docId);
		final MwtDocInfoHistory entity = new MwtDocInfoHistory();
		copyProperties(org, entity);
		// 履歴連番には必ず"0"を設定
		entity.setHistorySeqNo(0);
		// PK採番
		entity.setDocHistoryId(numbering.newPK(MwtDocInfoHistory.class));
		em.persist(entity);
		return entity.getDocHistoryId();
	}

	/**
	 * 文書権限情報履歴登録.
	 * @param docId 文書ID
	 * @param docHistoryId 文書履歴ID
	 */
	public void insertMwtDocAccessibleHistory(long docId, long docHistoryId) {
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		List<MwtDocAccessibleInfo> orgs = select(MwtDocAccessibleInfo.class, getSql("DC0100_18"), params.toArray());
		MwtDocAccessibleHistory entity = null;
		for (MwtDocAccessibleInfo org: orgs) {
			entity = new MwtDocAccessibleHistory();
			copyProperties(org, entity);
			// 履歴連番には必ず"0"を設定
			entity.setHistorySeqNo(0);
			// 文書履歴IDを設定
			entity.setDocHistoryId(docHistoryId);
			// PK採番
			entity.setDocAccessibleHistoryId(numbering.newPK(MwtDocAccessibleHistory.class));
			em.persist(entity);
		}
	}

	/**
	 * 業務文書情報履歴登録.
	 * @param docId 文書ID
	 * @return 文書履歴ID
	 */
	public void insertMwtBizDocInfoHistory(long docId, long docHistoryId) {
		final MwtBizDocInfo org = this.getMwtBizDocInfo(docId);
		if (org != null) {
			final MwtBizDocInfoHistory entity = new MwtBizDocInfoHistory();
			copyProperties(org, entity);
			// 履歴連番には必ず"0"を設定
			entity.setHistorySeqNo(0);
			// 文書履歴IDを設定
			entity.setDocHistoryId(docHistoryId);
			// PK採番
			entity.setBizDocHistoryId(numbering.newPK(MwtBizDocInfoHistory.class));
			em.persist(entity);
		}
	}

	/**
	 * バインダー情報履歴登録.
	 * @param docId 文書ID
	 * @return 文書履歴ID
	 */
	public void insertMwtBinderInfoHistory(long docId, long docHistoryId) {
		final MwtBinderInfo org = em.find(MwtBinderInfo.class, docId);
		if (org != null) {
			final MwtBinderInfoHistory entity = new MwtBinderInfoHistory();
			copyProperties(org, entity);
			// 履歴連番には必ず"0"を設定
			entity.setHistorySeqNo(0);
			// 文書履歴IDを設定
			entity.setDocHistoryId(docHistoryId);
			// PK採番
			entity.setBinderHistoryId(numbering.newPK(MwtBinderInfoHistory.class));
			em.persist(entity);
		}
	}

}
