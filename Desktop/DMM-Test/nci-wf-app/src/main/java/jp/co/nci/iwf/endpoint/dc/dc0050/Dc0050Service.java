package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.document.DocAccessibleService;
import jp.co.nci.iwf.component.document.DocAttributeExService;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderHierarchyInfo;

/**
 * 文書フォルダ設定サービス.
 */
@BizLogic
public class Dc0050Service extends BaseService implements DcCodeBook {

	@Inject
	private Dc0050Repository repository;
	/** 文書フォルダサービス */
	@Inject
	private DocFolderService docFolderService;
	/** 文書権限情報サービス */
	@Inject
	private DocAccessibleService docAccessibleService;
	/** 文書属性(拡張)サービス */
	@Inject
	private DocAttributeExService docAttributeExService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0050InitResponse init(Dc0050InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 戻り値
		final Dc0050InitResponse res = createResponse(Dc0050InitResponse.class, req);
		res.treeItems = this.getTreeItems(corporationCode, localeCode);
		// メタテンプレートの選択肢
		res.metaTemplates = docAttributeExService.getMetaTemplateOptionList(corporationCode);
		// 権限設定用ロールの選択肢
		res.roles = docAccessibleService.getRoleOptions(corporationCode, localeCode);
		res.success = true;

		return res;
	}

	/**
	 * 文書フォルダを階層順で並べた一覧情報を取得.
	 * @param corporationCode 企業コード
	 * @param localeCode ロケールコード
	 * @return 文書フォルダ一覧（階層順）
	 */
	private List<Dc0050TreeItem> getTreeItems(final String corporationCode, final String localeCode) {
		// 文書フォルダ情報一覧を取得
		// [トップ]フォルダから取得するので第3引数には"0"を渡す
		final List<MwvDocFolder> list = repository.getMwmDocFolderList(corporationCode, localeCode, 0);
		List<Dc0050TreeItem> treeItems = list.stream().map(l -> new Dc0050TreeItem(l)).collect(Collectors.toList());
		// ツリー情報の最初はトップフォルダを配置
		treeItems.add(0, new Dc0050TreeItem(i18n.getText(MessageCd.topFolder)));

		return treeItems;
	}

//	/**
//	 * 文書フォルダを階層順で並べた一覧情報を取得.
//	 * @param corporationCode 企業コード
//	 * @param localeCode ロケールコード
//	 * @return 文書フォルダ一覧（階層順）
//	 */
//	private List<DocFolderTreeItem> getTreeItems(final String corporationCode, final String localeCode) {
//		// 文書フォルダ情報一覧を取得
//		// [トップ]フォルダから取得するので第3引数には"0"を渡す
//		final List<MwvDocFolder> list = repository.getMwmDocFolderList(corporationCode, localeCode, 0);
//		List<DocFolderTreeItem> treeItems = list.stream().map(l -> new DocFolderTreeItem(l)).collect(Collectors.toList());
//		// ツリー情報の最初はトップフォルダを配置
//		treeItems.add(0, new DocFolderTreeItem(i18n.getText(MessageCd.topFolder)));
//
//		return treeItems;
//	}

//	/**
//	 * フォルダツリー情報取得.
//	 * @param nodeId
//	 * @return
//	 */
//	public List<DocFolderTreeItem> getTreeItems(String nodeId) {
//		List<DocFolderTreeItem> treeItems = new ArrayList<>();
//		if (TreeItem.PARENT.equals(nodeId)) {
//			treeItems.add(new DocFolderTreeItem(i18n.getText(MessageCd.topFolder)));
//		} else {
//			treeItems.addAll(docFolderService.getTreeItems(Long.parseLong(nodeId), true));
//		}
//		return treeItems;
//	}

	/**
	 * 追加・編集
	 * @param req
	 * @return
	 */
	public Dc0050EditResponse edit(Dc0050EditRequest req) {
		if (req.docFolderId == null && req.parentDocFolderId == null) {
			throw new BadRequestException("文書フォルダID、親文書フォルダIDが未指定です");
		}

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		// 戻り値
		final Dc0050EditResponse res = createResponse(Dc0050EditResponse.class, req);

		// 文書フォルダIDがあれば編集とみて対象の文書フォルダ情報を取得
		if (req.docFolderId != null) {
			final MwvDocFolder folder = docFolderService.getMwvDocFolder(corporationCode, req.docFolderId);
			if (folder == null) {
				throw new AlreadyUpdatedException();
			}
			res.folder = folder;
			// 文書フォルダ権限情報一覧の取得
			res.accessibles = docAccessibleService.getDocFolderAccessibleList(folder.getDocFolderId());
		}
		// なければ新規登録用の文書フォルダ情報を生成
		else {
			// 同一の親フォルダ内にいる文書フォルダ情報のソート順の最大値取得
			int sortOrder = repository.getMaxSortOrder(req.parentDocFolderId, localeCode);

			final MwvDocFolder folder = new MwvDocFolder();
			folder.setCorporationCode(corporationCode);
			folder.setParentDocFolderId(req.parentDocFolderId);
			folder.setSortOrder(++sortOrder);
			res.folder = folder;
			// 文書フォルダ権限情報一覧の取得
			// 新規の場合は親フォルダの権限情報一覧を取得する
			res.accessibles = docAccessibleService.getDocFolderAccessibleList(req.parentDocFolderId);
		}

		res.success = true;
		return res;
	}

	/**
	 * 文書フォルダの登録・更新・削除.
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0050SaveResponse save(Dc0050SaveRequest req) {
		// リクエストからフォルダ情報、権限情報を取得
		final MwvDocFolder inputed = req.folder;
		final List<MwmDocFolderAccessibleInfo> accessibles = req.accessibles;
		final boolean isDelete = eq(DeleteFlag.ON, inputed.getDeleteFlag());

		// 文書フォルダの差分更新
		final Long docFolderId = docFolderService.saveMwmDocFolder(inputed);

		// 文書フォルダ階層情報の差分更新
		docFolderService.saveMwmDocFolderHierarchy(docFolderId, inputed);

		// 文書フォルダ権限情報の差分更新
		docAccessibleService.saveMwmDocFolderAccessible(docFolderId, accessibles, isDelete);

		// 戻り値
		final Dc0050SaveResponse res = createResponse(Dc0050SaveResponse.class, req);
		if (!isDelete) {
			res.treeItem = new Dc0050TreeItem( docFolderService.getMwvDocFolder(inputed.getCorporationCode(), docFolderId) );
		}
		res.success = true;
		return res;
	}

	/**
	 * 移動
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0050MoveResponse move(Dc0050MoveRequest req) {
		// 戻り値
		final Dc0050MoveResponse res = createResponse(Dc0050MoveResponse.class, req);
		res.treeItems = new ArrayList<>();

		// 同一の親フォルダ内にいる文書フォルダ情報のソート順の最大値取得
		int sortOrder = repository.getMaxSortOrder(req.parentDocFolderId, sessionHolder.getLoginInfo().getLocaleCode());

		// 対象フォルダを順次更新
		for (MwvDocFolder target: req.folders) {
			// 文書フォルダ情報取得
			final MwmDocFolder folder = repository.getMwmDocFolderByPk(target.getDocFolderId());
			if (folder == null) {
				throw new NotFoundException("文書フォルダ情報が取得できませんでした -> docFolderId=" + target.getDocFolderId());
			} else if (!eq(folder.getVersion(), target.getVersion())) {
				throw new AlreadyUpdatedException();
			}

			// 文書フォルダ階層情報
			final MwmDocFolderHierarchyInfo hierarchy = repository.getMwmDocFolderHierarchyInfo(target.getDocFolderHierarchyId());
			// 移動先のフォルダが同じ場合、同一フォルダ内での並び替えなので入力値の並び順をセット
			if (eq(hierarchy.getParentDocFolderId(), req.parentDocFolderId)) {
				folder.setSortOrder(target.getSortOrder());
			}
			// 移動先のフォルダが異なる場合、移動先のフォルダの一番最後にくっつける
			else {
				folder.setSortOrder(++sortOrder);
				hierarchy.setParentDocFolderId(req.parentDocFolderId);
			}
			// 文書フォルダ更新
			repository.update(folder);
			// 文書フォルダ階層更新
			repository.update(hierarchy);

			res.treeItems.add( new Dc0050TreeItem(docFolderService.getMwvDocFolder(folder.getCorporationCode(), folder.getDocFolderId())) );
		}

		res.success = true;
		return res;
	}

}
