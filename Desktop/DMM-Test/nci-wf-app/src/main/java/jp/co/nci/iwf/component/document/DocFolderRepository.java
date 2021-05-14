package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderHierarchyInfo;

@ApplicationScoped
public class DocFolderRepository extends BaseRepository {

	private static final String REPLACE = "###REPLACE###";

	@Inject
	private NumberingService numbering;
	@Inject
	private DocHelper helper;

	/**
	 * アクセス可能な文書フォルダ件数取得.
	 * @param req
	 * @param login ログイン者情報
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 * @return アクセス可能な文書フォルダ件数
	 */
	public int count(DocFolderSearchRequest req, LoginInfo login, boolean isIgnoreAuth) {
		final List<Object> params = new ArrayList<>();
		params.add(login.getLocaleCode());

		final StringBuilder baseSql = this.createBaseSql(login, isIgnoreAuth, params);
		fillCondition(req, baseSql, params);

		final StringBuilder sql = new StringBuilder(getSql("DC0001_02").replaceFirst(REPLACE, baseSql.toString()));
		return count(sql.toString(), params.toArray());
	}

	/**
	 * アクセス可能な文書フォルダ一覧取得.
	 * @param req
	 * @param login ログイン者情報
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 * @param isPaging ページングをするか trueならページング
	 * @return アクセス可能な文書フォルダ一覧
	 */
	public List<MwvDocFolder> search(DocFolderSearchRequest req, LoginInfo login, boolean isIgnoreAuth, boolean isPaging) {
		final List<Object> params = new ArrayList<>();
		params.add(login.getLocaleCode());

		final StringBuilder baseSql = this.createBaseSql(login, isIgnoreAuth, params);
		fillCondition(req, baseSql, params);

		final StringBuilder sql = new StringBuilder(getSql("DC0001_03").replaceFirst(REPLACE, baseSql.toString()));
		// ソートおよびページング
		if (isPaging) {
			// ソート
			if (isNotEmpty(req.sortColumn)) {
				sql.append(toSortSql(req.sortColumn, req.sortAsc));
			} else {
				sql.append(" order by X.FOLDER_PATH, X.LEVEL_DEPTH, X.SORT_ORDER ");
			}
			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}

		return select(MwvDocFolder.class, sql.toString(), params.toArray());
	}

	/** ベースとなるSQL生成. */
	private StringBuilder createBaseSql(LoginInfo login, boolean isIgnoreAuth, List<Object> params) {
		StringBuilder replace = new StringBuilder();
		if (!isIgnoreAuth) {
			StringBuilder addWhere = new StringBuilder();
			// ログイン者のログイン情報より文書権限のハッシュ値を取得
			final Set<String> hashValues = helper.toHashValues(login);
			for (String hashValue: hashValues) {
				addWhere.append(addWhere.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}
			replace.append(getSql("DC0001_01").replaceFirst("###REPLACE###", addWhere.toString()));
		}
		final StringBuilder sql = new StringBuilder(getSql("DC0001").replaceFirst("###REPLACE_DC0001_01###", replace.toString()));
		return sql;
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(DocFolderSearchRequest req, StringBuilder sql, List<Object> params) {
		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and DF.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// 親文書フォルダID
		if (isNotEmpty(req.parentDocFolderId)) {
			sql.append(" and DF.PARENT_DOC_FOLDER_ID = ?");
			params.add(req.parentDocFolderId);
		}
		// 文書フォルダID
		if (isNotEmpty(req.docFolderId)) {
			sql.append(" and DF.DOC_FOLDER_ID = ?");
			params.add(req.docFolderId);
		}
		// フォルダコード
		if (isNotEmpty(req.folderCode)) {
			sql.append(" and DF.FOLDER_CODE = ?");
			params.add(req.folderCode);
		}
		// 文書フォルダ名
		if (isNotEmpty(req.folderName)) {
			sql.append(" and DF.FOLDER_NAME LIKE ? escape '~'");
			params.add(escapeLikeBoth(req.folderName));
		}
		// 除外する文書フォルダID
		if (isNotEmpty(req.excludeDocFolderId)) {
			sql.append(" AND DF.DOC_FOLDER_ID <> ?");
			params.add(req.excludeDocFolderId);
		}
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）のPrimaryKeyによる取得処理. */
	public MwmDocFolder getMwmDocFolderByPk(Long docFolderId) {
		if (docFolderId == null) {
			return null;
		}
		return em.find(MwmDocFolder.class, docFolderId);
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）の登録処理. */
	public Long insert(MwvDocFolder inputed) {
		final MwmDocFolder entity = new MwmDocFolder();
		entity.setCorporationCode(inputed.getCorporationCode());
		entity.setFolderName(inputed.getFolderName());
		entity.setValidStartDate(inputed.getValidStartDate());
		entity.setValidEndDate(inputed.getValidEndDate());
		entity.setMetaTemplateId(inputed.getMetaTemplateId());
		entity.setSortOrder(inputed.getSortOrder());
		entity.setDeleteFlag(DeleteFlag.OFF);
		entity.setDocFolderId(numbering.newPK(MwmDocFolder.class));
		entity.setFolderCode(String.format("%010d", entity.getDocFolderId()));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
		return entity.getDocFolderId();
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）の更新処理. */
	public void update(MwmDocFolder org, MwvDocFolder inputed) {
		org.setFolderName(inputed.getFolderName());
		org.setValidStartDate(inputed.getValidStartDate());
		org.setValidEndDate(inputed.getValidEndDate());
		org.setMetaTemplateId(inputed.getMetaTemplateId());
		org.setSortOrder(inputed.getSortOrder());
		this.update(org);
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）の更新処理. */
	public void update(MwmDocFolder org) {
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
		em.flush();
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）の削除処理. */
	public void delete(MwmDocFolder mdf) {
		em.remove(mdf);
		em.flush();
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の取得処理. */
	public MwmDocFolderHierarchyInfo getMwmDocFolderHierarchyInfo(Long docFolderHierarchyId) {
		if (docFolderHierarchyId == null) {
			return null;
		}
		return em.find(MwmDocFolderHierarchyInfo.class, docFolderHierarchyId);
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の登録処理. */
	public void insert(MwmDocFolderHierarchyInfo entity) {
		entity.setDocFolderHierarchyId(numbering.newPK(MwmDocFolderHierarchyInfo.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の更新処理. */
	public void update(MwmDocFolderHierarchyInfo entity) {
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.merge(entity);
		em.flush();
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の削除処理. */
	public void delete(MwmDocFolderHierarchyInfo entity) {
		em.remove(entity);
		em.flush();
	}

//	/**
//	 * 文書フォルダ権限情報一覧取得.
//	 * @param docFolderId 文書フォルダID
//	 * @param localeCode ロケールコード
//	 * @return 文書フォルダ権限情報一覧
//	 */
//	public <E extends BaseJpaEntity> List<E> getDocFolderAccessbileList(Class<E> resultClass, long docFolderId, String localeCode) {
//		final List<Object> params = new ArrayList<>();
//		params.add(localeCode);
//		params.add(docFolderId);
//		return select(resultClass, getSql("DC0022"), params.toArray());
//	}
//
//	/**
//	 * 文書フォルダ権限情報を文書フォルダ権限IDをKeyにMapとして抽出
//	 * @param docFolderId コンテナID
//	 * @return
//	 */
//	public Map<Long, MwmDocFolderAccessibleInfo> getMwmDocFolderAccessibleInfoMap(long docFolderId, String localeCode) {
//		final List<MwmDocFolderAccessibleInfo> list = this.getDocFolderAccessbileList(MwmDocFolderAccessibleInfo.class, docFolderId, localeCode);
//		return list.stream().collect(Collectors.toMap(MwmDocFolderAccessibleInfo::getDocFolderAccessibleId, e -> e));
//	}
//
//	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の登録処理. */
//	public void insert(MwmDocFolderAccessibleInfo entity) {
//		entity.setDocFolderAccessibleId(numbering.newPK(MwmDocFolderAccessibleInfo.class));
//		entity.setDeleteFlag(DeleteFlag.OFF);
//		em.persist(entity);
//		em.flush();
//	}
//
//	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の更新処理. */
//	public void update(MwmDocFolderAccessibleInfo entity) {
//		entity.setDeleteFlag(DeleteFlag.OFF);
//		em.merge(entity);
//		em.flush();
//	}
//
//	/** 文書フォルダ権限情報（MWM_DOC_FOLDER_ACCESSIBLE_INFO）の削除録処理. */
//	public void delete(MwmDocFolderAccessibleInfo entity) {
//		em.remove(entity);
//		em.flush();
//	}
//
//	/** 文書フォルダ権限IDを指定してMWM_DOC_FOLDER_ACCESSIBLE_INFOを削除 */
//	public void deleteMwmDocFolderAccessibleInfo(Set<Long> deleteIds) {
//		if (!deleteIds.isEmpty()) {
//			final List<Object> params = new ArrayList<>(deleteIds);
//			final StringBuilder sql = new StringBuilder();
//			sql.append(getSql("DC0023"));
//			sql.append(toInListSql("DOC_FOLDER_ACCESSIBLE_ID", params.size()));
//			execSql(sql.toString(), params.toArray());
//		}
//	}
}
