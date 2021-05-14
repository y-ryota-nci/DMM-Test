package jp.co.nci.iwf.endpoint.dc.dc0020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.BinderService;
import jp.co.nci.iwf.component.document.BizDocService;
import jp.co.nci.iwf.component.document.DocAccessibleService;
import jp.co.nci.iwf.component.document.DocAttributeExService;
import jp.co.nci.iwf.component.document.DocContentsInfoService;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.component.document.DocFileOperationResponse;
import jp.co.nci.iwf.component.document.DocFileService;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.component.document.DocInfoOperationRequest;
import jp.co.nci.iwf.component.document.DocInfoOperationResponse;
import jp.co.nci.iwf.component.document.DocInfoService;
import jp.co.nci.iwf.component.document.DocUpdateLogService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100Contents;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BinderInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BizDocInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 書類一覧サービス.
 */
@BizLogic
public class Dc0020Service extends BasePagingService implements DcCodeBook {

	/** 文書フォルダサービス */
	@Inject private DocFolderService docFolderService;
	/** 文書情報サービス */
	@Inject private DocInfoService docInfoService;
	/** 文書ファイルサービス */
	@Inject private DocFileService docFileService;
	/** 文書権限サービス */
	@Inject private DocAccessibleService docAccessibleService;
	/** 文書属性(拡張)情報サービス */
	@Inject private DocAttributeExService docAttributeExService;
	/** 業務文書サービス */
	@Inject private BizDocService bizDocService;
	/** バインダーサービス */
	@Inject private BinderService binderService;
	/** 文書コンテンツ情報サービス */
	@Inject private DocContentsInfoService docContentsInfoService;
	/** 文書更新履歴サービス */
	@Inject private DocUpdateLogService docUpdateLogService;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	@Inject private Dc0020Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0020InitResponse init(Dc0020InitRequest req) {
		// 戻り値
		final Dc0020InitResponse res = createResponse(Dc0020InitResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * フォルダツリー情報取得.
	 * @param nodeId
	 * @return
	 */
	public List<Dc0020TreeItem> getTreeItems(String nodeId) {
		final List<Dc0020TreeItem> treeItems = new ArrayList<>();
		if (TreeItem.PARENT.equals(nodeId)) {
			treeItems.add(new Dc0020TreeItem(i18n.getText(MessageCd.topFolder)));
		} else {
			// 管理者であればアクセス権を無視
			final boolean isIgnoreAuth = sessionHolder.getLoginInfo().isCorpAdmin();
			final List<MwvDocFolder> list = docFolderService.getChildDocFolderList(Long.parseLong(nodeId), isIgnoreAuth);
			list.stream().map(e -> new Dc0020TreeItem(e, false)).forEach(t -> treeItems.add(t));
		}
		return treeItems;
	}

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
//			// 管理者であればアクセス権を無視
//			final boolean isIgnoreAuth = sessionHolder.getLoginInfo().isCorpAdmin();
//			treeItems.addAll(docFolderService.getTreeItems(Long.parseLong(nodeId), isIgnoreAuth));
//		}
//		return treeItems;
//	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Dc0020SearchResponse search(Dc0020SearchRequest req) {
		if (isEmpty(req.searchType)) {
			throw new BadRequestException("検索タイプが未指定です");
		} else if (!eq(SearchType.NORMAL, req.searchType) && !eq(SearchType.SIMPLE, req.searchType) ){
			throw new BadRequestException("検索タイプに不正な値が指定されています");
		}
		if (isEmpty(req.docFolderId)) {
			throw new BadRequestException("文書フォルダIDが未指定です");
		}

		final int allCount = repository.count(req);
		final Dc0020SearchResponse res = createResponse(Dc0020SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * ロック処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse lock(DocInfoOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docInfoService.lock(req);
		if (res.success) {
			// ロックされた文書情報を取得
			Dc0020Entity entity = repository.get(req.docId);
			final Dc0020SearchResponse res2 = createResponse(Dc0020SearchResponse.class, null);
			res2.results = Arrays.asList(entity);
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * ロック解除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse unlock(DocInfoOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docInfoService.unlock(req);
		if (res.success) {
			// ロック解除された文書情報を取得
			Dc0020Entity entity = repository.get(req.docId);
			final Dc0020SearchResponse res2 = createResponse(Dc0020SearchResponse.class, null);
			res2.results = Arrays.asList(entity);
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * 文書情報のコピー処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse copy(Dc0020OperationRequest req) {
		// 入力パラメータ
		if (req.targetDocId == null) {
			throw new BadRequestException("文書IDが未指定です");
		} else if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("会社コードが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		} else if (req.docFolderIdTo == null) {
			throw new BadRequestException("コピー先の文書フォルダIDが未指定です");
		} else if (isEmpty(req.newTitle)) {
			throw new BadRequestException("コピー先の文書情報の件名が未指定です");
		}

		final LoginInfo login = sessionHolder.getLoginInfo();

		final DocInfoOperationRequest req2 = new DocInfoOperationRequest();
		req2.docId = req.targetDocId;
		req2.corporationCode = req.corporationCode;
		req2.version = req.version;
		req2.docFolderIdTo = req.docFolderIdTo;
		req2.newTitle = req.newTitle;
		// 戻り値生成
		final DocInfoOperationResponse res = docInfoService.copy(req2);
		if (!res.alerts.isEmpty()) {
			return res;
		}
		final DocInfo docInfo = res.docInfo;
		final Long newDocId = docInfo.docId;
		final String contentsType = docInfo.contentsType;
		final Integer majorVersion = docInfo.majorVersion;
		final Integer minorVersion = docInfo.minorVersion;

		// 文書権限情報
		final List<DocAccessibleInfo> accessibles = docAccessibleService.getDocAccessibles(req.targetDocId, login.getLocaleCode());
		docAccessibleService.saveMwtDocAccessibleInfo(newDocId, accessibles);

		// 文書属性(拡張)の差分更新
		final List<DocAttributeExInfo> attributeExs = docAttributeExService.getDocAttributeExList(req.targetDocId, null, login.getCorporationCode());
		docAttributeExService.saveMwtDocMetaInfo(newDocId, attributeExs);

		// 業務文書情報／バインダー情報
		RuntimeContext ctx = null;
		Map<String, String> docBizInfoMap = null;
		if (eq(ContentsType.BIZ_DOC, contentsType)) {
			final BizDocInfoEntity bizDocInfoEntity = bizDocService.getBizDocInfoEntity(req.targetDocId, null);
			final Dc0100Contents contents = new Dc0100Contents();
			contents.corporationCode = bizDocInfoEntity.corporationCode;
			contents.screenDocId = bizDocInfoEntity.screenDocId;
			contents.screenDocName = bizDocInfoEntity.screenDocName;
			contents.screenId = bizDocInfoEntity.screenId;
			contents.screenName = bizDocInfoEntity.screenName;
			contents.processId = null;
			contents.docId = newDocId;
			ctx = bizDocService.createRuntimeContext(contents, null);
			// コピー元の文書内容を読込
			bizDocService.loadScreenAndUserData(ctx, bizDocInfoEntity.tranId);
			// 業務文書情報を差分更新
			docBizInfoMap = bizDocService.saveBizDocInfo(newDocId, new BizDocInfo(bizDocInfoEntity), null, ctx);

		} else if (eq(ContentsType.BINDER, contentsType)) {
			final BinderInfo binderInfo = binderService.getBinderInfo(req.targetDocId);
			binderService.saveBinderInfo(newDocId, binderInfo);
		}

		// 文書管理項目の差分更新
		docInfoService.updateDocBizInfo(newDocId, docBizInfoMap);

		// 文書ファイル情報
		final List<DocFileInfo> docFiles = docFileService.getDocFileInfoList(req.targetDocId);
		// コピー時はロックは解除
		docFileService.saveMwtDocFileInfo(newDocId, docFiles);

		// 文書コンテンツ情報の差分更新
		docContentsInfoService.saveMwtDocContentsInfo(newDocId, docInfo, attributeExs, docFiles, ctx);

		// 文書更新履歴の登録
		docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.COPY, newDocId, req.newTitle, contentsType
				,majorVersion, minorVersion, null, null, null, null);

		res.addSuccesses(i18n.getText(MessageCd.MSG0220, docInfo.title));
		res.success = true;
		return res;
	}

	/**
	 * 文書情報の移動処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse move(Dc0020OperationRequest req) {
		final DocInfoOperationRequest req2 = new DocInfoOperationRequest();
		req2.docId = req.targetDocId;
		req2.corporationCode = req.corporationCode;
		req2.version = req.version;
		req2.docFolderIdTo = req.docFolderIdTo;
		// 戻り値生成
		final BaseResponse res = docInfoService.move(req2);
		if (res.success && res.alerts.isEmpty()) {
			final Dc0020SearchResponse res2 = this.search(req);
			res2.addSuccesses(res.successes.toArray(new String[res.successes.size()]));
			return res2;
		}
		return res;
	}

	/**
	 * 文書情報の削除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Dc0020OperationRequest req) {
		final DocInfoOperationRequest req2 = new DocInfoOperationRequest();
		req2.docId = req.targetDocId;
		req2.corporationCode = req.corporationCode;
		req2.version = req.version;
		// 戻り値生成
		final BaseResponse res = docInfoService.delete(req2);
		if (res.success && res.alerts.isEmpty()) {
			final Dc0020SearchResponse res2 = this.search(req);
			res2.addSuccesses(res.successes.toArray(new String[res.successes.size()]));
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイルロック処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse lock(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.lock(req);
		if (res.success) {
			// ロックされた文書ファイル情報を取得
			final DocFileInfoEntity entity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);
			final DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, req);
			res2.docFiles = new ArrayList<>();
			res2.docFiles.add(new DocFileInfo(entity, sessionHolder.getLoginInfo()));
			res2.success = true;
			return res2;
		}
		return res;
	}

	/**
	 * 文書ファイルロック解除処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse unlock(DocFileOperationRequest req) {
		// 戻り値生成
		final BaseResponse res = docFileService.unlock(req);
		if (res.success) {
			// ロック解除された文書ファイル情報を取得
			final DocFileInfoEntity entity = docFileService.getDocFileInfoEntity(req.docId, req.docFileId);
			final DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, req);
			res2.docFiles = new ArrayList<>();
			res2.docFiles.add(new DocFileInfo(entity, sessionHolder.getLoginInfo()));
			res2.success = true;
			return res2;
		}
		return res;
	}

//	/**
//	 * 文書ファイル削除処理.
//	 * @param req
//	 * @return
//	 */
//	@Transactional
//	public BaseResponse delete(Dc0020OperationRequest req) {
//		// 削除処理
//		final BaseResponse res = docFileService.delete(req);
//		if (isEmpty(res.alerts)) {
//			// 削除対象の文書ファイル一覧から文書ファイルIDを抜き出す
//			final List<Long> deleteDocFileIds =
//					req.deleteDocFiles.stream()
//						.filter(e -> isNotEmpty(e.docFileId))
//						.map(e -> e.docFileId)
//						.collect(Collectors.toList());
//
//			// 削除対象の文書ファイルデータ一覧から文書ファイルデータIDを抜き出す
//			final List<Long> deleteDocFileDataIds =
//					req.deleteDocFiles.stream()
//						.filter(e -> isEmpty(e.docFileId))
//						.filter(e -> isNotEmpty(e.docFileDataId))
//						.map(e -> e.docFileDataId)
//						.collect(Collectors.toList());
//			// 戻り値生成
//			DocFileOperationResponse res2 = createResponse(DocFileOperationResponse.class, req);
//			res2.deleteDocFileIds = deleteDocFileIds;
//			res2.deleteDocFileDataIds = deleteDocFileDataIds;
//			res2.success = true;
//		}
//		return res;
//	}

	/**
	 * 文書ファイルデータダウンロード.
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public Response download(Long docFileDataId) {
		return docFileDataService.download(docFileDataId);
	}
}
