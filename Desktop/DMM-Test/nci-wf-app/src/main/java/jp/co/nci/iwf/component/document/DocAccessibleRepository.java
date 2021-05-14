package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAccessibleInfo;

@ApplicationScoped
public class DocAccessibleRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/**
	 * 文書権限情報一覧取得.
	 * @param docFolderId 文書フォルダID
	 * @param localeCode ロケールコード
	 * @return 文書フォルダ権限情報一覧
	 */
	public <E extends BaseJpaEntity> List<E> getDocAccessbileList(Class<E> resultClass, long docId, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(docId);
		return select(resultClass, getSql("DC0024"), params.toArray());
	}

	/**
	 * 文書権限情報を文書権限IDをKeyにMapとして抽出
	 * @param docId 文書ID
	 * @param localeCode ロケールコード
	 * @return
	 */
	public Map<Long, MwtDocAccessibleInfo> getMwtDocAccessibleMap(long docId, String localeCode) {
		final List<MwtDocAccessibleInfo> list = this.getDocAccessbileList(MwtDocAccessibleInfo.class, docId, localeCode);
		return list.stream().collect(Collectors.toMap(MwtDocAccessibleInfo::getDocAccessibleId, e -> e));
	}

	/** 文書権限情報登録. */
	public void insert(DocAccessibleInfo inputed) {
		final MwtDocAccessibleInfo entity = new MwtDocAccessibleInfo();
		entity.setDocId(inputed.docId);
		entity.setSeqNo(inputed.seqNo);
		entity.setAuthRefer(inputed.authRefer);
		entity.setAuthDownload(inputed.authDownload);
		entity.setAuthEdit(inputed.authEdit);
		entity.setAuthDelete(inputed.authDelete);
		entity.setAuthCopy(inputed.authCopy);
		entity.setAuthMove(inputed.authMove);
		entity.setAuthPrint(inputed.authPrint);
		entity.setBelongType(inputed.belongType);
		entity.setCorporationCode(inputed.corporationCode);
		entity.setAssignRoleCode(inputed.roleCode);
		entity.setUserCode(inputed.userCode);
		entity.setHashValue(inputed.hashValue);
		entity.setDeleteFlag(DeleteFlag.OFF);
		// 文書権限IDを採番
		entity.setDocAccessibleId(numbering.newPK(MwtDocAccessibleInfo.class));
		em.persist(entity);
	}

	/** 文書権限情報更新. */
	public void update(MwtDocAccessibleInfo org, DocAccessibleInfo inputed) {
		org.setSeqNo(inputed.seqNo);
		org.setAuthRefer(inputed.authRefer);
		org.setAuthDownload(inputed.authDownload);
		org.setAuthEdit(inputed.authEdit);
		org.setAuthDelete(inputed.authDelete);
		org.setAuthCopy(inputed.authCopy);
		org.setAuthMove(inputed.authMove);
		org.setAuthPrint(inputed.authPrint);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 文書権限情報の一括削除. */
	public void deleteMwtDocAccessibleInfo(Set<Long> docAccessibleIds) {
		if (!docAccessibleIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(docAccessibleIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("DC0025"));
			sql.append(toInListSql("DOC_ACCESSIBLE_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/**
	 * 文書フォルダ権限情報一覧取得.
	 * @param docFolderId 文書フォルダID
	 * @param localeCode ロケールコード
	 * @return 文書フォルダ権限情報一覧
	 */
	public <E extends BaseJpaEntity> List<E> getDocFolderAccessbileList(Class<E> resultClass, long docFolderId, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(docFolderId);
		return select(resultClass, getSql("DC0022"), params.toArray());
	}

	/**
	 * 文書フォルダ権限情報を文書フォルダ権限IDをKeyにMapとして抽出
	 * @param docFolderId 文書フォルダID
	 * @param localeCode ロケールコード
	 * @return
	 */
	public Map<Long, MwmDocFolderAccessibleInfo> getMwmDocFolderAccessibleInfoMap(long docFolderId, String localeCode) {
		final List<MwmDocFolderAccessibleInfo> list = this.getDocFolderAccessbileList(MwmDocFolderAccessibleInfo.class, docFolderId, localeCode);
		return list.stream().collect(Collectors.toMap(MwmDocFolderAccessibleInfo::getDocFolderAccessibleId, e -> e));
	}

	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の登録処理. */
	public void insert(MwmDocFolderAccessibleInfo entity) {
		entity.setDocFolderAccessibleId(numbering.newPK(MwmDocFolderAccessibleInfo.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
	}

	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の更新処理. */
	public void update(MwmDocFolderAccessibleInfo entity) {
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.merge(entity);
		em.flush();
	}

	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の削除録処理. */
	public void delete(MwmDocFolderAccessibleInfo entity) {
		em.remove(entity);
		em.flush();
	}

	/** 文書フォルダ権限IDを指定してMWM_DOC_FOLDER_ACCESSIBLE_INFOを削除 */
	public void deleteMwmDocFolderAccessibleInfo(Set<Long> deleteIds) {
		if (!deleteIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("DC0023"));
			sql.append(toInListSql("DOC_FOLDER_ACCESSIBLE_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}
}
