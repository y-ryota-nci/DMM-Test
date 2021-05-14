package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoHistoryEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileInfoHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocUpdateLog;

/**
 * 文書ファイル情報用のリポジトリ
 */
@ApplicationScoped
public class DocFileRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/** 文書情報取得. */
	public MwtDocInfo getMwtDocInfoByPk(long docId) {
		return em.find(MwtDocInfo.class, docId);
	}

	/** 文書ファイル情報取得. */
	public MwtDocFileInfo getMwtDocFileInfoByPk(long docFileId) {
		return em.find(MwtDocFileInfo.class, docFileId);
	}

	/** 文書IDによる文書ファイル情報一覧取得. */
	public List<MwtDocFileInfo> getMwtDocFileList(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0002"));
		sql.append(" DOC_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return select(MwtDocFileInfo.class, sql, params.toArray());
	}

	/** 文書ファイルIDによる文書ファイル情報一覧取得. */
	public List<MwtDocFileInfo> getMwtDocFileList(List<Long> docFileIds) {
		final StringBuilder sql = new StringBuilder(getSql("DC0002"));
		sql.append(toInListSql("DOC_FILE_ID", docFileIds.size()));
		final List<Object> params = new ArrayList<>();
		params.add(docFileIds);
		return select(MwtDocFileInfo.class, sql, params.toArray());
	}

	/**
	 * 引数の文書IDに紐づく文書ファイル情報Entity一覧取得.
	 * @param docId 文書ID
	 * @param docFileId 文書ファイルID
	 * @param hashValues ログイン者が保持している文書権限情報のハッシュ値一覧
	 * @param isAdmin 企業管理者か
	 * @return
	 */
	public List<DocFileInfoEntity> getDocFileInfoEntityList(Long docId, Long docFileId, Collection<String> hashValues, boolean isAdmin) {
		final List<Object> params = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		// 管理者と管理者以外で結合するテーブルを切り替える
		String joinTable = null;
		if (isAdmin) {
			joinTable = getSql("DC0005_01");
			params.add(docId);
		} else {
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}
			joinTable = getSql("DC0005_02").replaceFirst("###REPLACE###", replace.toString());
		}
		sql.append(getSql("DC0006").replaceFirst("###JOIN_TABLE###", joinTable));
		if (docId != null) {
			sql.append(" and MDFI.DOC_ID = ? ");
			params.add(docId);
		}
		if (docFileId != null) {
			sql.append(" and MDFI.DOC_FILE_ID = ? ");
			params.add(docFileId);
		}
		// ソート順を設定
		sql.append( toSortSql("MDFI.DOC_FILE_ID", true) );

		final List<DocFileInfoEntity> list = select(DocFileInfoEntity.class, sql, params.toArray());
		if (list != null) {
			list.stream().forEach(e -> em.detach(e));
		}
		return list;
	}

	/**
	 * 引数の文書ファイルIDに紐づく文書ファイル情報履歴Entity一覧取得.
	 * @param docFileId 文書ファイルID
	 * @return
	 */
	public List<DocFileInfoHistoryEntity> getDocFileInfoHistoryEntityList(Long docFileId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0009"));
		final List<Object> params = new ArrayList<>();
		params.add(docFileId);
		return select(DocFileInfoHistoryEntity.class, sql, params.toArray());
	}

	/**
	 * 文書ファイル情報のロック／ロック解除.
	 * @param docFileId 文書ファイルID
	 * @param login ログイン者情報
	 * @param isLock ロックするか trueならロック
	 */
	public void lockOrUnlockDocFileInfo(long docFileId, LoginInfo login, boolean isLock) {
		final List<Object> params = new ArrayList<>();
		params.add(isLock ? CommonFlag.ON : CommonFlag.OFF);
		params.add(isLock ? timestamp() : null );
		params.add(isLock ? login.getCorporationCode() : null );
		params.add(isLock ? login.getCorporationName() : null );
		params.add(isLock ? login.getUserCode() : null );
		params.add(isLock ? login.getUserName() : null );
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp());
		final StringBuilder sql = new StringBuilder(getSql("DC0004"));
		sql.append(" and DOC_FILE_ID =? ");
		params.add(docFileId);
		execSql(sql, params.toArray());
	}

	/**
	 * 文書ファイル情報の一括削除
	 */
	public void deleteMwtDocFileInfo(List<Long> docFileIds, LoginInfo login) {
		if (!docFileIds.isEmpty()) {
			final StringBuilder sql = new StringBuilder(getSql("DC0007"));
			sql.append(toInListSql("DOC_FILE_ID", docFileIds.size()));
			final List<Object> params = new ArrayList<>();
			params.add(login.getCorporationCode());
			params.add(login.getUserCode());
			params.add(timestamp());
			params.addAll(docFileIds);
			execSql(sql, params.toArray());
		}
	}

	/** 文書更新履歴の件数取得. */
	public int countMwtDocUpdateLog(long docId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0100_10"));
		final List<Object> params = new ArrayList<>();
		params.add(docId);
		return count(sql.toString(), params.toArray());
	}

	/** 文書更新履歴登録. */
	public void insert(MwtDocUpdateLog entity) {
		// 文書更新履歴IDを採番
		entity.setDocUpdateLogId(numbering.newPK(MwtDocUpdateLog.class));
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.persist(entity);
		em.flush();
	}

	/** 履歴の履歴連番更新. */
	public void updateHistorySeqNo(long docFileId) {
		StringBuilder sql = new StringBuilder(getSql("DC0100_17").replaceFirst("###TABLE_NAME###", "MWT_DOC_FILE_INFO_HISTORY"));
		sql.append("DOC_FILE_ID = ? ");
		final List<Object> params = new ArrayList<>();
		params.add(docFileId);
		execSql(sql, params.toArray());
	}

	/**
	 * 文書ファイル情報履歴登録
	 * @param docFileId 文書ファイルID
	 * @return 文書ファイル履歴ID
	 */
	public Long insertMwtDocFileInfoHistory(long docFileId) {
		final MwtDocFileInfo org = em.find(MwtDocFileInfo.class, docFileId);
		final MwtDocFileInfoHistory entity = new MwtDocFileInfoHistory();
		copyProperties(org, entity);
		// 履歴連番には必ず"0"を設定
		entity.setHistorySeqNo(0);
		// PK採番
		entity.setDocFileHistoryId(numbering.newPK(MwtDocFileInfoHistory.class));
		em.persist(entity);
		return entity.getDocFileHistoryId();
	}

	/** 文書ファイル情報登録. */
	public Long insert(DocFileInfo inputed, LoginInfo login, boolean isFromWf) {
		final MwtDocFileInfo entity = new MwtDocFileInfo();
		entity.setDocFileId(numbering.newPK(MwtDocFileInfo.class));
		entity.setCorporationCode(inputed.corporationCode);
		entity.setDocFileNum(String.format("%020d", entity.getDocFileId()));
		entity.setDocId(inputed.docId);
		entity.setMajorVersion(inputed.majorVersion);
		entity.setMinorVersion(inputed.minorVersion);
		entity.setComments(inputed.comments);
		entity.setDocFileDataId(inputed.docFileDataId);
		entity.setLockFlag(inputed.lockFlag);
		entity.setLockCorporationCode(inputed.lockCorporationCode);
		entity.setLockCorporationName(inputed.lockCorporationName);
		entity.setLockUserCode(inputed.lockUserCode);
		entity.setLockUserName(inputed.lockUserName);
		if (eq(CommonFlag.ON, inputed.lockFlag)) {
			entity.setLockTimestamp(timestamp());
		}
		entity.setDeleteFlag(DeleteFlag.OFF);
		// DMM特殊仕様
		// WFからの文書連携により文書ファイルが作成される場合、WF側の文書ファイル内容(登録者や登録日時など)を引き継ぐ
		if (isFromWf) {
			entity.setUserNameCreated(inputed.userNameCreated);
			entity.setUserNameUpdated(inputed.userNameUpdated);
			entity.setDocFileWfId(inputed.docFileWfId);
		}
		// 文書連携でない場合はログイン者の名前等を設定する
		else {
			entity.setUserNameCreated(login.getUserName());
			entity.setUserNameUpdated(login.getUserName());
		}
		// OCR対象ﾌﾗｸﾞ設定(0：未対応 1：読込済 9：対象外)
		String fileNm = inputed.fileName.toLowerCase();
		if(fileNm.indexOf(".pdf") != -1 || fileNm.indexOf(".png") != -1 || fileNm.indexOf(".gif") != -1){
			entity.setOcrFlag("0");
		}
		else{
			entity.setOcrFlag("9");
		}
		em.persist(entity);
		em.flush();
		return entity.getDocFileId();
	}

	/**
	 * 文書ファイル情報更新.
	 * @param org
	 * @param docFileInfo
	 */
	public void update(MwtDocFileInfo org, DocFileInfo inputed) {
		org.setMajorVersion(inputed.majorVersion);
		org.setMinorVersion(inputed.minorVersion);
		org.setComments(inputed.comments);
		org.setDocFileDataId(inputed.docFileDataId);
		org.setUserNameUpdated(inputed.userNameUpdated);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
		em.flush();
	}

	public void update(MwtDocFileInfo entity) {
		em.merge(entity);
		em.flush();
	}

	/**
	 * WF側からの文書連携にて登録された文書ファイル情報の作成者、作成日時等の更新処理.
	 * 文書ファイルデータから値をコピーする
	 * @param docFileId 文書ファイルID
	 */
	public void updateFromWf(long docFileId) {
		// 更新実行
		execSql(getSql("DC0033"), new Object[]{docFileId});
	}
}
