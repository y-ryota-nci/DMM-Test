package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderHierarchyInfo;

@BizLogic
public class DocFolderService extends BasePagingService {

	@Inject
	private DocFolderRepository repository;
	@Inject
	private MultilingalService multi;

	/**
	 * 文書フォルダ一覧検索(ページングあり).
	 * @param req
	 * @param isIgnoreAuth
	 * @return
	 */
	public DocFolderSearchResponse search(DocFolderSearchRequest req, boolean isIgnoreAuth) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final int allCount = repository.count(req, login, isIgnoreAuth);
		final DocFolderSearchResponse res = createResponse(DocFolderSearchResponse.class, req, allCount);
		if (res.allCount == 0) {
			res.results = new ArrayList<>();
		} else {
			res.results = this.getDocFolderList(req, isIgnoreAuth, true);
		}
		res.success = true;
		return res;
	}

	/**
	 * 親文書フォルダ内にある文書フォルダ情報一覧取得.
	 *
	 * @param parentDocFolderId 親文書フォルダID
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 * @return 文書フォルダ一覧
	 */
	public List<MwvDocFolder> getChildDocFolderList(long parentDocFolderId, boolean isIgnoreAuth) {
		final DocFolderSearchRequest req = new DocFolderSearchRequest();
		req.parentDocFolderId = parentDocFolderId;
		return this.getDocFolderList(req, isIgnoreAuth, false);
	}

//	/**
//	 * 親文書フォルダ内にある文書フォルダ情報のツリー情報を取得.
//	 *
//	 * @param parentDocFolderId 親文書フォルダID
//	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
//	 * @return 文書フォルダ一覧
//	 */
//	public List<DocFolderTreeItem> getTreeItems(long parentDocFolderId, boolean isIgnoreAuth) {
//		// 親文書フォルダに紐づく文書フォルダ情報一覧を取得
//		final List<MwvDocFolder> list = this.getChildDocFolderList(parentDocFolderId, isIgnoreAuth);
//		List<DocFolderTreeItem> treeItems = list.stream().map(l -> new DocFolderTreeItem(l, false)).collect(Collectors.toList());
//		return treeItems;
//	}

	/**
	 * アクセス可能な文書フォルダの選択肢一覧取得.
	 * @param folderName フォルダ名(絞込み条件)
	 * @param exclusionDocFolderId 除外する文書フォルダID
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 */
	public List<OptionItem> getFolderOptionList(String folderName, Long excludeDocFolderId, boolean isIgnoreAuth) {
		final DocFolderSearchRequest req = new DocFolderSearchRequest();
		req.folderName = folderName;
		req.excludeDocFolderId = excludeDocFolderId;
		// アクセス可能な文書フォルダ一覧を取得し、選択肢に追加
		final List<MwvDocFolder> list = this.getDocFolderList(req, isIgnoreAuth, false);
		final List<OptionItem> folders = new ArrayList<>();
		list.stream().forEach(e -> folders.add(new OptionItem(e.getDocFolderId(), e.getFolderPath())));
		return folders;
	}

	/**
	 * 文書フォルダ情報取得.
	 * 企業管理者であればアクセス権限を無視してフォルダ情報を取得する
	 * @param corporationCode 企業コード
	 * @param docFolderId 文書フォルダID
	 * @return
	 */
	public MwvDocFolder getMwvDocFolder(String corporationCode, Long docFolderId) {
		if (docFolderId == null) {
			return null;
		}
		final DocFolderSearchRequest req = new DocFolderSearchRequest();
		req.corporationCode = corporationCode;
		req.docFolderId = docFolderId;
		final List<MwvDocFolder> list = this.getDocFolderList(req, sessionHolder.getLoginInfo().isCorpAdmin(), false);
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * 文書フォルダ情報取得.
	 * 文書フォルダに対するアクセス権限は無視します
	 * @param corporationCode 企業コード
	 * @param folderCode フォルダコード
	 * @return
	 */
	public MwvDocFolder getMwvDocFolderByFolderCode(String corporationCode, String folderCode) {
		if (isEmpty(corporationCode) || isEmpty(folderCode)) {
			return null;
		}
		final DocFolderSearchRequest req = new DocFolderSearchRequest();
		req.corporationCode = corporationCode;
		req.folderCode = folderCode;
		final List<MwvDocFolder> list = this.getDocFolderList(req, true, false);
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * 文書フォルダ情報取得.
	 * 文書フォルダ名にて検索
	 * 文書フォルダに対するアクセス権限は無視します
	 * @param corporationCode 企業コード
	 * @param folderName フォルダ名
	 * @return
	 */
	public MwvDocFolder getMwvDocFolderByFolderName(String corporationCode, String folderName, Long parentDocFolderId) {
		if (isEmpty(corporationCode) || isEmpty(folderName)) {
			return null;
		}
		final DocFolderSearchRequest req = new DocFolderSearchRequest();
		req.corporationCode = corporationCode;
		req.folderName = folderName;
		req.parentDocFolderId = parentDocFolderId;
		final List<MwvDocFolder> list = this.getDocFolderList(req, true, false);
		return list.stream().filter(e -> eq(folderName, e.getFolderName())).findFirst().orElse(null);
	}

	/** 文書フォルダの差分更新. */
	public Long saveMwmDocFolder(MwvDocFolder inputed) {
		// 文書フォルダ情報を取得
		final MwmDocFolder folder = repository.getMwmDocFolderByPk(inputed.getDocFolderId());
		if (folder == null && eq(DeleteFlag.ON, inputed.getDeleteFlag())) {
			throw new AlreadyUpdatedException(i18n.getText(MessageCd.MSG0048));
		} else if (folder != null && !eq(folder.getVersion(), inputed.getVersion())) {
			throw new AlreadyUpdatedException();
		}

		Long docFolderId = folder != null ? folder.getDocFolderId() : null;
		if (eq(DeleteFlag.ON, inputed.getDeleteFlag())) {
			repository.delete(folder);
			multi.physicalDelete("MWM_DOC_FOLDER", folder.getDocFolderId(), "FOLDER_NAME");

			// 文書フォルダ階層情報の削除
			final MwmDocFolderHierarchyInfo hierarchy = repository.getMwmDocFolderHierarchyInfo(inputed.getDocFolderHierarchyId());
			repository.delete(hierarchy);
		} else {
			// 有効期間の設定（未設定の場合、有効開始日はシステム日付、有効終了日は最大値を設定）
			inputed.setValidStartDate(inputed.getValidStartDate() != null ? inputed.getValidStartDate() : today());
			inputed.setValidEndDate(inputed.getValidEndDate() != null ? inputed.getValidEndDate() : ENDDATE);
			if (folder == null) {
				docFolderId = repository.insert(inputed);
			} else {
				repository.update(folder, inputed);
			}
			// フォルダ名を多言語用を更新
			multi.save("MWM_DOC_FOLDER", docFolderId, "FOLDER_NAME", inputed.getFolderName());
		}
		return docFolderId;
	}

	/** 文書フォルダ階層情報の差分更新. */
	public void saveMwmDocFolderHierarchy(Long docFolderId, MwvDocFolder inputed) {
		final MwmDocFolderHierarchyInfo org = repository.getMwmDocFolderHierarchyInfo(inputed.getDocFolderHierarchyId());
		if (org == null) {
			// 次に文書フォルダ階層情報を登録
			final MwmDocFolderHierarchyInfo entity = new MwmDocFolderHierarchyInfo();
			entity.setParentDocFolderId(inputed.getParentDocFolderId());
			entity.setDocFolderId(docFolderId);
			repository.insert(entity);
		} else if (eq(DeleteFlag.ON, inputed.getDeleteFlag())) {
			repository.delete(org);
		} else {
			org.setParentDocFolderId(inputed.getParentDocFolderId());
			repository.update(org);
		}
	}

	/**
	 * 文書フォルダ検索.
	 * @param req
	 * @param isIgnoreAuth
	 * @param isPaging trueならページングおよびソートを行う
	 * @return
	 */
	private List<MwvDocFolder> getDocFolderList(DocFolderSearchRequest req, boolean isIgnoreAuth, boolean isPaging) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		// 企業コードが未指定の場合、操作者の企業コードで絞り込む
		if (isEmpty(req.corporationCode)) {
			req.corporationCode = login.getCorporationCode();
		}
		return repository.search(req, login, isIgnoreAuth, isPaging);
	}

}
