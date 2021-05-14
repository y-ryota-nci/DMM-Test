package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFolderRelationInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;

/**
 * 文書管理用のリポジトリ
 */
@ApplicationScoped
public class DocInfoRepository extends BaseRepository {

	private static final String REPLACE = "###REPLACE###";

	@Inject
	private NumberingService numbering;
	@Inject
	private DocHelper helper;

	/** 文書情報取得. */
	public MwtDocInfo getMwtDocInfoByPk(long docId) {
		return em.find(MwtDocInfo.class, docId);
	}

	/** 文書情報登録. */
	public Long insert(DocInfo inputed) {
		final MwtDocInfo entity = new MwtDocInfo();
		// 文書IDを採番
		entity.setDocId(numbering.newPK(MwtDocInfo.class));
		entity.setCorporationCode(inputed.corporationCode);
		entity.setDocNum(String.format("%020d", entity.getDocId()));
		entity.setContentsType(inputed.contentsType);
		entity.setMajorVersion(inputed.majorVersion);
		entity.setMinorVersion(inputed.minorVersion);
		entity.setTitle(inputed.title);
		entity.setComments(inputed.comments);
		entity.setPublishTimestamp(timestamp());
		entity.setPublishCorporationCode(inputed.publishCorporationCode);
		entity.setPublishCorporationName(inputed.publishCorporationName);
		entity.setPublishUserCode(inputed.publishUserCode);
		entity.setPublishUserName(inputed.publishUserName);
		entity.setOwnerCorporationCode(inputed.ownerCorporationCode);
		entity.setOwnerCorporationName(inputed.ownerCorporationName);
		entity.setOwnerUserCode(inputed.ownerUserCode);
		entity.setOwnerUserName(inputed.ownerUserName);
		entity.setPublishFlag(inputed.publishFlag);
		entity.setPublishStartDate(inputed.publishStartDate);
		entity.setPublishEndDate(inputed.publishEndDate);
		entity.setLockFlag(inputed.lockFlag);
		entity.setLockCorporationCode(inputed.lockCorporationCode);
		entity.setLockCorporationName(inputed.lockCorporationName);
		entity.setLockUserCode(inputed.lockUserCode);
		entity.setLockUserName(inputed.lockUserName);
		if (eq(CommonFlag.ON, inputed.lockFlag)) {
			entity.setLockTimestamp(timestamp());
		}
		entity.setRetentionTermType(inputed.retentionTermType);
		entity.setRetentionTerm(inputed.retentionTerm);
		entity.setDispCount(0);
		entity.setUserNameCreated(inputed.userNameCreated);
		entity.setUserNameUpdated(inputed.userNameUpdated);
		entity.setDeleteFlag(DeleteFlag.OFF);
		entity.setProcessId(inputed.processId);
		em.persist(entity);
		return entity.getDocId();
	}

	/** 文書情報更新. */
	public void update(MwtDocInfo org, DocInfo inputed) {
		org.setMajorVersion(inputed.majorVersion);
		org.setMinorVersion(inputed.minorVersion);
		org.setTitle(inputed.title);
		org.setComments(inputed.comments);
		org.setOwnerCorporationCode(inputed.ownerCorporationCode);
		org.setOwnerCorporationName(inputed.ownerCorporationName);
		org.setOwnerUserCode(inputed.ownerUserCode);
		org.setOwnerUserName(inputed.ownerUserName);
		org.setPublishFlag(inputed.publishFlag);
		org.setPublishStartDate(inputed.publishStartDate);
		org.setPublishEndDate(inputed.publishEndDate);
		org.setLockFlag(inputed.lockFlag);
		org.setLockCorporationCode(inputed.lockCorporationCode);
		org.setLockCorporationName(inputed.lockCorporationName);
		org.setLockUserCode(inputed.lockUserCode);
		org.setLockUserName(inputed.lockUserName);
		if (eq(CommonFlag.ON, inputed.lockFlag)) {
			org.setLockTimestamp(timestamp());
		}
		org.setRetentionTermType(inputed.retentionTermType);
		org.setRetentionTerm(inputed.retentionTerm);
		org.setUserNameUpdated(inputed.userNameUpdated);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setProcessId(inputed.processId);
		em.merge(org);
	}

	/** 文書管理項目更新. */
	public void updateDocBizInfo(MwtDocInfo org, Map<String, String> bizInfoMap) {
		bizInfoMap.forEach((key, value) -> setProperty(org, toCamelCase(key), value));
		em.merge(org);
	}

	/** 文書情報削除(論理削除). */
	public void delete(MwtDocInfo org, DocInfo inputed) {
		org.setLockFlag(CommonFlag.OFF);
		org.setLockCorporationCode(null);
		org.setLockUserCode(null);
		org.setLockUserName(null);
		org.setLockTimestamp(null);
		org.setUserNameUpdated(inputed.userNameUpdated);
		org.setDeleteFlag(DeleteFlag.ON);
		em.merge(org);
	}

	/** 文書-フォルダ連携情報取得. */
	public MwtDocFolderRelationInfo getMwtDocFolderRelationInfo(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_12"));
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return selectOne(MwtDocFolderRelationInfo.class, sql, params.toArray());
	}

	/** 文書-フォルダ連携情報登録. */
	public void insert(MwtDocFolderRelationInfo entity) {
		entity.setDocFolderRelationId(numbering.newPK(MwtDocFolderRelationInfo.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
	}

	/** 文書-フォルダ連携情報更新. */
	public void update(MwtDocFolderRelationInfo entity) {
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.merge(entity);
	}

	/**
	 * 文書情報のロック／ロック解除.
	 * @param docId 文書ID
	 * @param login ログイン者情報
	 * @param isLock ロックするか trueならロック
	 */
	public void lockOrUnlockDocInfo(long docId, LoginInfo login, boolean isLock) {
		final List<Object> params = new ArrayList<>();
		params.add( isLock ? CommonFlag.ON : CommonFlag.OFF);
		params.add( isLock ? timestamp() : null );
		params.add( isLock ? login.getCorporationCode() : null );
		params.add( isLock ? login.getCorporationName() : null );
		params.add( isLock ? login.getUserCode() : null );
		params.add( isLock ? login.getUserName() : null );
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp());
		params.add(docId);
		final StringBuilder sql = new StringBuilder(getSql("DC0003"));
		execSql(sql, params.toArray());
	}

	/**
	 * 文書ファイル情報のロック／ロック解除.
	 * @param docId 文書ID
	 * @param login ログイン者情報
	 * @param isLock ロックするか trueならロック
	 */
	public void lockOrUnlockDocFileInfo(long docId, LoginInfo login, boolean isLock) {
		final List<Object> params = new ArrayList<>();
		params.add( isLock ? CommonFlag.ON : CommonFlag.OFF);
		params.add( isLock ? timestamp() : null );
		params.add( isLock ? login.getCorporationCode() : null );
		params.add( isLock ? login.getCorporationName() : null );
		params.add( isLock ? login.getUserCode() : null );
		params.add( isLock ? login.getUserName() : null );
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp());
		final StringBuilder sql = new StringBuilder(getSql("DC0004"));
		sql.append(" and DOC_ID = ? ");
		params.add(docId);
		execSql(sql, params.toArray());
	}

	/** アクセス可能な文書情報一覧件数取得. */
	public int countDocInfoEntityList(DocInfoSearchRequest req, LoginInfo login, boolean isIgnoreAuth) {
		// 条件
		final List<Object> params = new ArrayList<>();

		// 文書条件
		final StringBuilder sqlDoc = isIgnoreAuth ? new StringBuilder(getSql("DC0017")) :new StringBuilder(getSql("DC0016"));
		fillConditionDoc(req, sqlDoc, params, login, isIgnoreAuth);

		final StringBuilder sql = new StringBuilder(getSql("DC0014").replaceFirst(REPLACE, sqlDoc.toString()));
		fillCondition(req, login, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** アクセス可能な文書情報一覧取得. */
	public List<DocInfoEntity> getDocInfoEntityList(DocInfoSearchRequest req, LoginInfo login, boolean isIgnoreAuth) {
		final List<Object> params = new ArrayList<>();

		// 文書条件
		final StringBuilder sqlDoc = isIgnoreAuth ? new StringBuilder(getSql("DC0017")) :new StringBuilder(getSql("DC0016"));
		fillConditionDoc(req, sqlDoc, params, login, isIgnoreAuth);

		final StringBuilder sql = new StringBuilder(getSql("DC0015").replaceFirst(REPLACE, sqlDoc.toString()));
		fillCondition(req, login, sql, params, true);

		return select(DocInfoEntity.class, sql.toString(), params.toArray());
	}

	/** 文書情報Entity取得. */
	public DocInfoEntity getDocInfoEntity(DocInfoSearchRequest req, LoginInfo login, boolean isIgnoreAuth) {
		final List<Object> params = new ArrayList<>();

		final StringBuilder sqlDoc = isIgnoreAuth ? new StringBuilder(getSql("DC0017")) :new StringBuilder(getSql("DC0016"));
		fillConditionDoc(req, sqlDoc, params, login, isIgnoreAuth);

		final StringBuilder sql = new StringBuilder(getSql("DC0015").replaceFirst(REPLACE, sqlDoc.toString()));
		fillCondition(req, login, sql, params, false);

		final DocInfoEntity docInfoEntity = selectOne(DocInfoEntity.class, sql, params.toArray());
		if (docInfoEntity != null) {
			em.detach(docInfoEntity);
		}
		return docInfoEntity;
	}

	/** SELECT/COUNTでの共通SQLを追記(文書) */
	private void fillConditionDoc(DocInfoSearchRequest req, StringBuilder sql, List<Object> params, LoginInfo login, boolean isIgnoreAuth) {
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());

		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		if (!isIgnoreAuth) {
			final Set<String> hashValues = helper.toHashValues(login);
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}
			int start = sql.indexOf(REPLACE);
			int end = start + REPLACE.length();
			sql.replace(start, end, replace.toString());
		}

		params.add(login.getLocaleCode());

		if (req.docId != null) {
			sql.append(" and MDI.DOC_ID = ?");
			params.add(req.docId);
		}
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and MDI.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		if (isNotEmpty(req.title)) {
			sql.append(" and MDI.TITLE LIKE ? escape '~'");
			params.add(escapeLikeBoth(req.title));
		}
		if (isNotEmpty(req.contentsType)) {
			sql.append(" and MDI.CONTENTS_TYPE = ?");
			params.add(req.contentsType);
		}
		if (isNotEmpty(req.excludeDocId)) {
			sql.append(" AND MDI.DOC_ID <> ?");
			params.add(req.excludeDocId);
		}
		// 削除済みを対象としない場合
		if (!eq(CommonFlag.ON, req.includeDeleted)) {
			sql.append(" and MDI.DELETE_FLAG = '0'");
		}
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(DocInfoSearchRequest req, LoginInfo login, StringBuilder sql, List<Object> params, boolean paging) {
		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}

		// ページング
		if (paging) {
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}
}
