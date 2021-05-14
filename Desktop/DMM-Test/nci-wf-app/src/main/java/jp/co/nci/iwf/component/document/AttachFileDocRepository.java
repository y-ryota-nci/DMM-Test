package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwtAttachFileDocEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtAttachFileDoc;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;

/**
 * 添付ファイル情報用のリポジトリ
 */
@ApplicationScoped
public class AttachFileDocRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/** 文書添付ファイル取得. */
	public MwtAttachFileDoc getMwtAttachFileDocByPk(long attachFileDocId) {
		return em.find(MwtAttachFileDoc.class, attachFileDocId);
	}

	/** 文書IDによる文書添付ファイル一覧取得. */
	public List<MwtAttachFileDoc> getMwtAttachFileDocList(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0018"));
		sql.append(" A.DOC_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		// ソート順
		sql.append(toSortSql("A.ATTACH_FILE_DOC_ID", true));
		return select(MwtAttachFileDoc.class, sql, params.toArray());
	}

	/** 文書IDによる文書添付ファイル一覧取得. */
	public List<MwtAttachFileDocEx> getMwtAttachFileDocExList(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0018"));
		sql.append(" A.DOC_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		// ソート順
		sql.append(toSortSql("A.ATTACH_FILE_DOC_ID", true));
		return select(MwtAttachFileDocEx.class, sql, params.toArray());
	}

	/**
	 * 文書ファイルデータ取得.
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public MwtDocFileData getMwtDocFileDataByPk(Long docFileDataId) {
		if (docFileDataId == null) {
			return null;
		}
		return em.find(MwtDocFileData.class, docFileDataId);
	}

	/** 文書添付ファイル情報登録. */
	public Long insert(AttachFileDocInfo inputed) {
		final MwtAttachFileDoc entity = new MwtAttachFileDoc();
		entity.setAttachFileDocId(numbering.newPK(MwtAttachFileDoc.class));
		entity.setDocId(inputed.docId);
		entity.setComments(inputed.comments);
		entity.setDocFileDataId(inputed.docFileDataId);
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		return entity.getAttachFileDocId();
	}

	/**
	 * 文書添付ファイル情報更新.
	 * @param org
	 * @param docFileInfo
	 */
	public void update(MwtAttachFileDoc org, AttachFileDocInfo inputed) {
		org.setComments(inputed.comments);
		org.setDocFileDataId(inputed.docFileDataId);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/**
	 * 文書添付ファイルの一括削除
	 * @param attachFileDocIds 削除対象の文書添付ファイルID一覧
	 * @param login
	 */
	public void deleteMwtAttachFileDoc(Set<Long> attachFileDocIds, LoginInfo login) {
		if (!attachFileDocIds.isEmpty()) {
			final StringBuilder sql = new StringBuilder(getSql("DC0019"));
			sql.append(toInListSql("ATTACH_FILE_DOC_ID", attachFileDocIds.size()));
			final List<Object> params = new ArrayList<>();
			params.add(login.getCorporationCode());
			params.add(login.getUserCode());
			params.add(timestamp());
			params.addAll(attachFileDocIds);
			execSql(sql, params.toArray());
		}
	}

	/**
	 * WF側からの文書連携にて登録された文書添付ファイルの作成者、作成日時等の更新処理.
	 * 文書ファイルデータから値をコピーする
	 * @param attachFileDocId 文書添付ファイルID
	 */
	public void updateFromWf(long attachFileDocId) {
		// 更新実行
		execSql(getSql("DC0034"), new Object[]{attachFileDocId});
	}
}
