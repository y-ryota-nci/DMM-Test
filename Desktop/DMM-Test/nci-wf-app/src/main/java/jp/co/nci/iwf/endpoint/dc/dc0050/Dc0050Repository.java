package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderHierarchyInfo;

/**
 * 文書フォルダ設定画面リポジトリ
 */
@ApplicationScoped
public class Dc0050Repository extends BaseRepository {

	/**
	 * 文書フォルダ一覧取得.
	 * @param corporationCode
	 * @param localeCode
	 * @param parentDocFolderId
	 * @return
	 */
	public List<MwvDocFolder> getMwmDocFolderList(String corporationCode, String localeCode, long parentDocFolderId) {
		final StringBuilder sql = new StringBuilder(getSql("DC0050_01"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(parentDocFolderId);
		params.add(corporationCode);
		// ソート順
		sql.append( toSortSql("D.LEVEL_DEPTH, D.PARENT_DOC_FOLDER_ID, D.SORT_ORDER", true));

		return select(MwvDocFolder.class, sql.toString(), params.toArray());
	}

	/**
	 * 文書フォルダ内に文書フォルダないし文書情報が存在するか
	 * @param docFolderId 文書フォルダID
	 * @return trueなら存在する
	 */
	public boolean isExistsChildren(Long docFolderId) {
		final List<Object> params = new ArrayList<>();
		params.add(docFolderId);
		final int count = count(getSql("DC0050_02"), params.toArray());
		return (count > 0);
	}

	/**
	 * 同一フォルダ内にある文書フォルダの並び順の最大値取得.
	 * @param parentDocFolderId 親文書フォルダID
	 * @return 並び順の最大値
	 */
	public int getMaxSortOrder(long parentDocFolderId, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(parentDocFolderId);
		return count(getSql("DC0050_03"), params.toArray());
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）のPrimaryKeyによる取得処理. */
	public MwmDocFolder getMwmDocFolderByPk(Long docFolderId) {
		if (docFolderId == null) {
			return null;
		}
		return em.find(MwmDocFolder.class, docFolderId);
	}

	/** 文書フォルダ（MWM_DOC_FOLDER）の更新処理. */
	public void update(MwmDocFolder org) {
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
		em.flush();
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の取得処理. */
	public MwmDocFolderHierarchyInfo getMwmDocFolderHierarchyInfo(Long docFolderHierarchyId) {
		if (docFolderHierarchyId == null) {
			return null;
		}
		return em.find(MwmDocFolderHierarchyInfo.class, docFolderHierarchyId);
	}

	/** 文書フォルダ階層情報（MWM_DOC_FOLDER_HIERARCHY_INFO）の更新処理. */
	public void update(MwmDocFolderHierarchyInfo entity) {
		entity.setDeleteFlag(DeleteFlag.OFF);
		em.merge(entity);
		em.flush();
	}

}