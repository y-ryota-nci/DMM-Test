package jp.co.nci.iwf.component.document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.Auth;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.AuthBelongType;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmDocFolderAccessibleInfoEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocAccessibleInfoEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAccessibleInfo;

/**
 * 文書権限および文書フォルダ権限情報のサービス
 */
@BizLogic
public class DocAccessibleService extends BaseService {

	@Inject private WfInstanceWrapper wf;
	@Inject private DocAccessibleRepository repository;
	@Inject private DocHelper helper;

	/**
	 * 文書権限一覧の取得.
	 * @param docId 文書ID
	 * @param localeCode ロケールコード
	 */
	public List<DocAccessibleInfo> getDocAccessibles(long docId, String localeCode) {
		final List<MwtDocAccessibleInfoEx> list = repository.getDocAccessbileList(MwtDocAccessibleInfoEx.class, docId, localeCode);
		return list.stream().map(e -> new DocAccessibleInfo(e)).collect(Collectors.toList());
	}

	/**
	 * 文書フォルダ編集時の権限設定可能ロール選択肢取得
	 * @param corporationCode 企業コード
	 * @param localeCode ロケールコード
	 * @return
	 */
	public List<OptionItem> getRoleOptions(final String corporationCode, final String localeCode) {
		final SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(corporationCode);
		inParam.setMenuRoleType(MenuRoleType.NORMAL);
		inParam.setValidStartDate(today());
		inParam.setDeleteFlag(DeleteFlag.OFF);
		final List<WfmMenuRole> roles = wf.searchWfmMenuRole(inParam).getMenuRoles();
		return roles.stream().map(r -> new OptionItem(r.getCorporationCode() + "|" + r.getMenuRoleCode(), r.getMenuRoleName())).collect(Collectors.toList());
	}

	/** 文書権限情報の差分更新. */
	public void saveMwtDocAccessibleInfo(Long docId, List<DocAccessibleInfo> accessibles) {
		final Map<Long, MwtDocAccessibleInfo> map = repository.getMwtDocAccessibleMap(docId, sessionHolder.getLoginInfo().getLocaleCode());

		int seqNo = 1;
		for (DocAccessibleInfo accessible : accessibles) {
			final MwtDocAccessibleInfo org = map.remove(accessible.docAccessibleId);
			if (org == null) {
				accessible.docId = docId;
				accessible.seqNo = seqNo;
				// 各権限において権限なしの場合、"0"を設定してやる
				accessible.authRefer = StringUtils.defaultIfEmpty(accessible.authRefer, Auth.OFF);
				accessible.authDownload = StringUtils.defaultIfEmpty(accessible.authDownload, Auth.OFF);
				accessible.authEdit = StringUtils.defaultIfEmpty(accessible.authEdit, Auth.OFF);
				accessible.authDelete = StringUtils.defaultIfEmpty(accessible.authDelete, Auth.OFF);
				accessible.authCopy = StringUtils.defaultIfEmpty(accessible.authCopy, Auth.OFF);
				accessible.authMove = StringUtils.defaultIfEmpty(accessible.authMove, Auth.OFF);
				// 印刷権限には常に"0"を設定
				accessible.authPrint = Auth.OFF;
				// 所属区分はロールコードがある場合は"R"、ユーザコードがある場合は"U"に設定
				if (isNotEmpty(accessible.roleCode)) {
					accessible.belongType = AuthBelongType.ROLE;
					accessible.userCode = null;
				} else if (isNotEmpty(accessible.userCode)) {
					accessible.belongType = AuthBelongType.USER;
					accessible.roleCode = null;
				}
				// ハッシュ値を計算
				accessible.hashValue = helper.toHashValue(accessible.belongType, accessible.corporationCode, accessible.roleCode, accessible.userCode);
				// 登録実行
				repository.insert(accessible);
			} else {
				accessible.seqNo = seqNo;
				// 各権限において権限なしの場合、"0"を設定してやる
				accessible.authRefer = StringUtils.defaultIfEmpty(accessible.authRefer, Auth.OFF);
				accessible.authDownload = StringUtils.defaultIfEmpty(accessible.authDownload, Auth.OFF);
				accessible.authEdit = StringUtils.defaultIfEmpty(accessible.authEdit, Auth.OFF);
				accessible.authDelete = StringUtils.defaultIfEmpty(accessible.authDelete, Auth.OFF);
				accessible.authCopy = StringUtils.defaultIfEmpty(accessible.authCopy, Auth.OFF);
				accessible.authMove = StringUtils.defaultIfEmpty(accessible.authMove, Auth.OFF);
				// 印刷権限には常に"0"を設定
				accessible.authPrint = Auth.OFF;
				// 更新実行
				repository.update(org, accessible);
			}
			seqNo++;
		}
		// 残余は削除されたものなので一括削除
		repository.deleteMwtDocAccessibleInfo(map.keySet());
	}

	/**
	 * 文書フォルダ権限情報一覧取得処理.
	 * @param docFolderId 文書フォルダID
	 * @return
	 */
	public List<MwmDocFolderAccessibleInfoEx> getDocFolderAccessibleList(Long docFolderId) {
		return repository.getDocFolderAccessbileList(MwmDocFolderAccessibleInfoEx.class, docFolderId, sessionHolder.getLoginInfo().getLocaleCode());
	}

	/**
	 * 文書フォルダ権限一覧の取得.
	 * @param docFolderId 文書フォルダID
	 */
	public List<DocAccessibleInfo> getFolderAccessibles(long docFolderId) {
		final List<MwmDocFolderAccessibleInfoEx> list = this.getDocFolderAccessibleList(docFolderId);
		return list.stream().map(e -> new DocAccessibleInfo(e)).collect(Collectors.toList());
	}

	/** 文書フォルダ権限情報の差分更新. */
	public void saveMwmDocFolderAccessible(Long docFolderId, List<MwmDocFolderAccessibleInfo> accessibles, boolean isDelete) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Map<Long, MwmDocFolderAccessibleInfo> map = repository.getMwmDocFolderAccessibleInfoMap(docFolderId, login.getLocaleCode());
		if (!isDelete) {
			// 差分更新
			int seqNo = 1;
			for (MwmDocFolderAccessibleInfo accessible : accessibles) {
				final MwmDocFolderAccessibleInfo org = map.remove(accessible.getDocFolderAccessibleId());
				if (org == null) {
					accessible.setDocFolderId(docFolderId);
					accessible.setSeqNo(seqNo);
					accessible.setAuthRefer(StringUtils.defaultIfEmpty(accessible.getAuthRefer(), Auth.OFF));
					accessible.setAuthDownload(StringUtils.defaultIfEmpty(accessible.getAuthDownload(), Auth.OFF));
					accessible.setAuthEdit(StringUtils.defaultIfEmpty(accessible.getAuthEdit(), Auth.OFF));
					accessible.setAuthDelete(StringUtils.defaultIfEmpty(accessible.getAuthDelete(), Auth.OFF));
					accessible.setAuthCopy(StringUtils.defaultIfEmpty(accessible.getAuthCopy(), Auth.OFF));
					accessible.setAuthMove(StringUtils.defaultIfEmpty(accessible.getAuthMove(), Auth.OFF));
					// 印刷権限には常に"0"を設定
					accessible.setAuthPrint(Auth.OFF);
					// 所属区分はロールコードがある場合は"R"、ユーザコードがある場合は"U"に設定
					if (isNotEmpty(accessible.getAssignRoleCode())) {
						accessible.setBelongType(AuthBelongType.ROLE);
						accessible.setUserCode(null);
					} else if (isNotEmpty(accessible.getUserCode())) {
						accessible.setBelongType(AuthBelongType.USER);
						accessible.setAssignRoleCode(null);
					}
					// ハッシュ値を計算
					accessible.setHashValue( helper.toHashValue(accessible) );
					// 登録実行
					repository.insert(accessible);
				} else {
					org.setSeqNo(seqNo);
					org.setAuthRefer(StringUtils.defaultIfEmpty(accessible.getAuthRefer(), Auth.OFF));
					org.setAuthDownload(StringUtils.defaultIfEmpty(accessible.getAuthDownload(), Auth.OFF));
					org.setAuthEdit(StringUtils.defaultIfEmpty(accessible.getAuthEdit(), Auth.OFF));
					org.setAuthDelete(StringUtils.defaultIfEmpty(accessible.getAuthDelete(), Auth.OFF));
					org.setAuthCopy(StringUtils.defaultIfEmpty(accessible.getAuthCopy(), Auth.OFF));
					org.setAuthMove(StringUtils.defaultIfEmpty(accessible.getAuthMove(), Auth.OFF));
					// 印刷権限には常に"0"を設定
					org.setAuthPrint(Auth.OFF);
					// ハッシュ値を計算
					org.setHashValue( helper.toHashValue(org) );
					// 更新実行
					repository.update(org);
				}
				seqNo++;
			}
		}
		// 残余は削除されたものなので一括削除
		repository.deleteMwmDocFolderAccessibleInfo(map.keySet());
	}
}
