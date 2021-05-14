package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocInfoEntity;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFolderRelationInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;

@BizLogic
public class DocInfoService extends BasePagingService implements DcCodeBook {

	@Inject private DocInfoRepository repository;

	/** 文書ファイルサービス */
	@Inject private DocFileService docFileService;
	/** 文書更新履歴サービス */
	@Inject private DocUpdateLogService docUpdateLogService;

	public DocInfoSearchResponse search(DocInfoSearchRequest req, boolean isIgnoreAuth) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final int allCount = repository.countDocInfoEntityList(req, login, isIgnoreAuth);
		final DocInfoSearchResponse res = createResponse(DocInfoSearchResponse.class, req, allCount);
		if (res.allCount == 0) {
			res.results = new ArrayList<>();
		} else {
			res.results = this.getDocInfoList(req, isIgnoreAuth);
		}
		res.success = true;
		return res;
	}

	/**
	 * アクセス可能な文書情報一覧取得.
	 * @param folderName フォルダ名(絞込み条件)
	 * @param exculteDocFolederId 除外する文書フォルダID
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 */
	public List<DocInfoEntity> getDocInfoList(DocInfoSearchRequest req, boolean isIgnoreAuth) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		return repository.getDocInfoEntityList(req, login, isIgnoreAuth);
	}

	/**
	 * アクセス可能な文書情報の選択肢一覧取得.
	 * @param folderName フォルダ名(絞込み条件)
	 * @param exclusionDocFolderId 除外する文書フォルダID
	 * @param isIgnoreAuth フォルダアクセス権限を無視するか trueなら無視
	 */
	public List<OptionItem> getDocInfoOptionList(DocInfoSearchRequest req, boolean isIgnoreAuth) {
		// アクセス可能な文書情報一覧を取得し、選択肢に追加
		List<DocInfoEntity> list = this.getDocInfoList(req, isIgnoreAuth);
		final List<OptionItem> docInfos = new ArrayList<>();
		list.stream().forEach(e -> docInfos.add(new OptionItem(e.docId, e.title)));
		return docInfos;
	}

	/**
	 * 文書情報の差分更新.
	 * @param docId 文書ID NULLが渡された場合、新規登録
	 * @param docInfo 画面上で入力された文書情報
	 * @return 文書ID
	 */
	public Long saveMwtDocInfo(Long docId, DocInfo docInfo) {
		if (docId == null) {
			docId = repository.insert(docInfo);
		} else {
			final MwtDocInfo org = repository.getMwtDocInfoByPk(docId);
			repository.update(org, docInfo);
		}

		// 文書-フォルダ連携情報の差分更新
		saveMwtDocFolderRelationInfo(docId, docInfo.docFolderId);

		return docId;
	}

	/**
	 * 文書管理項目の差分更新.
	 * @param docId 文書ID
	 * @param bizInfoMap 文書管理項目Map
	 */
	public void updateDocBizInfo(Long docId, Map<String, String> bizInfoMap) {
		if (isNotEmpty(bizInfoMap)) {
			final MwtDocInfo org = repository.getMwtDocInfoByPk(docId);
			repository.updateDocBizInfo(org, bizInfoMap);
		}
	}

	/**
	 * 文書-フォルダ連携情報の差分更新.
	 * @param docId 文書ID
	 * @param docFolderId 文書フォルダID
	 */
	public void saveMwtDocFolderRelationInfo(Long docId, Long docFolderId) {
		final MwtDocFolderRelationInfo org = repository.getMwtDocFolderRelationInfo(docId);
		if (org == null) {
			MwtDocFolderRelationInfo entity = new MwtDocFolderRelationInfo();
			entity.setDocFolderId(docFolderId);
			entity.setDocId(docId);
			repository.insert(entity);
		} else {
			org.setDocFolderId(docFolderId);
			repository.update(org);
		}
	}

	/**
	 * 文書情報のロック処理.
	 * 　文書情報をロックする場合、紐づく文書ファイルも一緒にロックする
	 * 　逆に紐づく文書ファイルで1つでも(他のユーザによって)ロック済みのものがあればロックはできない
	 * @param req
	 * @return res
	 */
	public BaseResponse lock(DocInfoOperationRequest req) {
		final BaseResponse res = this.lockOrUnlock(req, true);
		return res;
	}

	/**
	 * 文書情報のロック解除処理.
	 * 　文書情報をロック解除する場合、紐づく文書ファイルも一緒にロック解除する
	 * 　逆に紐づく文書ファイルが1つでも(他のユーザによって)ロック済みのものがあればロック解除はできない
	 * @param req
	 * @return res
	 */
	public BaseResponse unlock(DocInfoOperationRequest req) {
		final BaseResponse res = this.lockOrUnlock(req, false);
		return res;
	}

	/**
	 * 文書情報の移動処理.
	 * @param req
	 * @return res
	 */
	public BaseResponse move(DocInfoOperationRequest req) {
		// 入力パラメータ
		if (req.docId == null) {
			throw new BadRequestException("文書IDが未指定です");
		} else if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("会社コードが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		} else if (req.docFolderIdTo == null) {
			throw new BadRequestException("移動先の文書フォルダIDが未指定です");
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		// 文書情報に対するチェック
		final DocInfoEntity docInfoEntity = this.getDocInfoEntity(req.docId, req.corporationCode);
		String error1 = this.validateDocInfo(docInfoEntity, req.version, true, Arrays.asList(DocAuth.EDIT, DocAuth.MOVE));
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 問題なければ文書情報の移動を実行
		final MwtDocFolderRelationInfo entity = repository.getMwtDocFolderRelationInfo(req.docId);
		entity.setDocFolderId(req.docFolderIdTo);
		repository.update(entity);
		res.addSuccesses(i18n.getText(MessageCd.MSG0065, docInfoEntity.title));
		res.success = true;
		return res;
	}

	/**
	 * 文書情報のコピー処理.
	 * @param req
	 * @return res
	 */
	public DocInfoOperationResponse copy(DocInfoOperationRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final DocInfoOperationResponse res = createResponse(DocInfoOperationResponse.class, req);
		// 文書情報に対するチェック
		final DocInfoEntity docInfoEntity = this.getDocInfoEntity(req.docId, req.corporationCode);
		String error1 = this.validateDocInfo(docInfoEntity, req.version, false, Arrays.asList(DocAuth.COPY));
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 問題なければコピーを実行
		// 文書情報
		final DocInfo docInfo = new DocInfo(docInfoEntity);
		// コピー対象外の項目を再設定
		// コピー先の件名、文書フォルダは移動先のフォルダIDを設定
		// またロックは解除した状態にしておく
		docInfo.title = req.newTitle;
		docInfo.docFolderId = req.docFolderIdTo;
		docInfo.dispCount = 0;
		docInfo.publishCorporationCode = login.getCorporationCode();
		docInfo.publishCorporationName = login.getCorporationName();
		docInfo.publishUserCode = login.getUserCode();
		docInfo.publishUserName = login.getUserName();
		docInfo.lockFlag = CommonFlag.OFF;
		docInfo.lockCorporationCode = null;
		docInfo.lockCorporationName = null;
		docInfo.lockUserCode = null;
		docInfo.lockUserName = null;
		docInfo.userNameCreated = login.getUserName();
		docInfo.userNameUpdated = login.getUserName();
		// 文書情報のコピー（新規登録）
		final Long newDocId = saveMwtDocInfo(null, docInfo);
		docInfo.docId = newDocId;
		res.docInfo = docInfo;
		res.success = true;
		return res;
	}

//	/**
//	 * 文書情報のコピー処理.
//	 * @param req
//	 * @return res
//	 */
//	public BaseResponse copy(DocInfoOperationRequest req) {
//		// 入力パラメータ
//		if (req.docId == null) {
//			throw new BadRequestException("文書IDが未指定です");
//		} else if (isEmpty(req.corporationCode)) {
//			throw new BadRequestException("会社コードが未指定です");
//		} else if (req.version == null) {
//			throw new BadRequestException("バージョンが未指定です");
//		} else if (req.docFolderIdTo == null) {
//			throw new BadRequestException("コピー先の文書フォルダIDが未指定です");
//		} else if (isEmpty(req.newTitle)) {
//			throw new BadRequestException("コピー先の文書情報の件名が未指定です");
//		}
//
//		final LoginInfo login = sessionHolder.getLoginInfo();
//
//		final BaseResponse res = createResponse(BaseResponse.class, req);
//		// 文書情報に対するチェック
//		final DocInfoEntity docInfoEntity = this.getDocInfoEntity(req.docId, req.corporationCode);
//		String error1 = this.validateDocInfo(docInfoEntity, req.version, false, Arrays.asList(DocAuth.COPY));
//		if (isNotEmpty(error1)) {
//			res.addAlerts(error1);
//			return res;
//		}
//		// 問題なければコピーを実行
//		// 文書情報
//		final DocInfo docInfo = new DocInfo(docInfoEntity);
//		// コピー対象外の項目を再設定
//		// またロックは解除した状態にしておく
//		docInfo.title = req.newTitle;
//		docInfo.dispCount = 0;
//		docInfo.publishCorporationCode = login.getCorporationCode();
//		docInfo.publishUserCode = login.getUserCode();
//		docInfo.publishUserName = login.getUserName();
//		docInfo.lockFlag = CommonFlag.OFF;
//		docInfo.lockCorporationCode = null;
//		docInfo.lockUserCode = null;
//		docInfo.lockUserName = null;
//		docInfo.userNameCreated = login.getUserName();
//		docInfo.userNameUpdated = login.getUserName();
//		final Long newDocId = saveMwtDocInfo(null, docInfo);
//
//		// 文書-フォルダ連携情報
//		saveMwtDocFolderRelationInfo(newDocId, req.docFolderIdTo);
//
//		// 文書権限情報
//		final List<DocAccessibleInfo> accessibles = docAccessibleService.getDocAccessibles(req.docId, login.getLocaleCode());
//		docAccessibleService.saveMwtDocAccessibleInfo(newDocId, accessibles);
//
//		// 文書属性(拡張)の差分更新
//		final List<DocAttributeExInfo> attributeExs = docAttributeExService.getDocAttributeExList(req.docId, null, login.getCorporationCode());
//		docAttributeExService.saveMwtDocMetaInfo(newDocId, attributeExs);
//
//		// 業務文書情報／バインダー情報
//		RuntimeContext ctx = null;
//		if (eq(ContentsType.BIZ_DOC, docInfoEntity.contentsType)) {
//			final BizDocInfoEntity bizDocInfoEntity = bizDocService.getBizDocInfoEntity(req.docId, null);
//			final Dc0100Contents contents = new Dc0100Contents();
//			contents.corporationCode = bizDocInfoEntity.corporationCode;
//			contents.screenDocId = bizDocInfoEntity.screenDocId;
//			contents.screenDocName = bizDocInfoEntity.screenDocName;
//			contents.screenId = bizDocInfoEntity.screenId;
//			contents.screenName = bizDocInfoEntity.screenName;
//			contents.processId = null;
//			ctx = bizDocService.createRuntimeContext(contents, null);
//			// コピー元の文書内容を読込
//			bizDocService.load(ctx, bizDocInfoEntity.tranId);
//			// 業務文書情報を差分更新
//			bizDocService.saveBizDocInfo(newDocId, new BizDocInfo(bizDocInfoEntity), null, ctx);
//
//		} else if (eq(ContentsType.BINDER, docInfoEntity.contentsType)) {
//
//		}
//
//		// 文書ファイル情報
//		final List<DocFileInfo> docFiles = docFileService.getDocFileInfoList(req.docId);
//		// コピー時はロックは解除
//		docFileService.saveMwtDocFileInfo(newDocId, docFiles, false, false);
//
//		// 文書コンテンツ情報の差分更新
//		docContentsInfoService.saveMwtDocContentsInfo(newDocId, docInfo, attributeExs, docFiles, ctx);
//
//		// 文書更新履歴の登録
//		docUpdateLogService.saveMwtDocUpdateLog(newDocId, req.newTitle, docInfoEntity.contentsType, docInfoEntity.majorVersion, docInfoEntity.minorVersion,
//				null, false, false, true, false, null, null, null);
//
//		res.addSuccesses(i18n.getText(MessageCd.MSG0220, docInfoEntity.title));
//		res.success = true;
//		return res;
//	}

	/**
	 * 文書情報の削除処理.
	 * @param req
	 * @return res
	 */
	public BaseResponse delete(DocInfoOperationRequest req) {
		// 入力パラメータ
		if (req.docId == null) {
			throw new BadRequestException("文書IDが未指定です");
		} else if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("会社コードが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		}

		final LoginInfo login = sessionHolder.getLoginInfo();

		final BaseResponse res = createResponse(BaseResponse.class, req);
		// 文書情報に対するチェック
		final DocInfoEntity docInfoEntity = this.getDocInfoEntity(req.docId, req.corporationCode);
		String error1 = this.validateDocInfo(docInfoEntity, req.version, false, Arrays.asList(DocAuth.EDIT, DocAuth.DELETE));
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 問題なければ削除を実行
		final MwtDocInfo entity = repository.getMwtDocInfoByPk(docInfoEntity.docId);
		final DocInfo docInfo = new DocInfo();
		docInfo.userNameUpdated = login.getUserName();
		repository.delete(entity, docInfo);

		// 文書更新履歴の登録
		docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.DELETE, docInfoEntity.docId, docInfoEntity.title, docInfoEntity.contentsType
				,docInfoEntity.majorVersion, docInfoEntity.minorVersion, null, null, null, null);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, docInfoEntity.title));
		res.success = true;
		return res;
	}

	/**
	 * 文書情報Entity取得.
	 * @param docId 文書ID
	 * @return 文書情報Entity
	 */
	public DocInfoEntity getDocInfoEntity(long docId, String corporationCode) {
		return this.getDocInfoEntity(docId, corporationCode, null);
	}

	/**
	 * 文書情報Entity取得.
	 * @param docId 文書ID
	 * @param corporationCode 企業コード
	 * @param fromCoopWfFlag WFからの文書連携か 1:文書連携
	 * @return 文書情報Entity
	 */
	public DocInfoEntity getDocInfoEntity(long docId, String corporationCode, String fromCoopWfFlag) {
		final DocInfoSearchRequest req = new DocInfoSearchRequest();
		req.docId = docId;
		req.corporationCode = corporationCode;

		final LoginInfo login = sessionHolder.getLoginInfo();
		final boolean isIgnoreAuth = (eq(CommonFlag.ON, fromCoopWfFlag) || login.isCorpAdmin());
		final DocInfoEntity entity = repository.getDocInfoEntity(req, login, isIgnoreAuth);
		return entity;
	}

	/**
	 * 新規の文書情報Entity生成.
	 */
	public DocInfoEntity createNewDocInfoEntity(Long copyDocId, String corporationCode, Long screenDocId) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final DocInfoEntity docInfoEntity = new DocInfoEntity();
		if (copyDocId != null) {
			final DocInfoEntity src = getDocInfoEntity(copyDocId, corporationCode);
			docInfoEntity.title = src.title;
			docInfoEntity.comments = src.comments;
			docInfoEntity.docFolderId = src.docFolderId;
			docInfoEntity.contentsType = src.contentsType;
		}
		// 画面文書IDがあれば業務文書
		else if (screenDocId != null) {
			docInfoEntity.contentsType = ContentsType.BIZ_DOC;
		}
		// なければバインダー
		else {
			docInfoEntity.contentsType = ContentsType.BINDER;
		}
		docInfoEntity.corporationCode = defaults(corporationCode, login.getCorporationCode());
		docInfoEntity.majorVersion = 1;
		docInfoEntity.minorVersion = 0;
		docInfoEntity.publishFlag = CommonFlag.OFF;
		docInfoEntity.publishCorporationCode = login.getCorporationCode();
		docInfoEntity.publishUserCode = login.getUserCode();
		docInfoEntity.publishUserName = login.getUserName();
		docInfoEntity.ownerCorporationCode = login.getCorporationCode();
		docInfoEntity.ownerUserCode = login.getUserCode();
		docInfoEntity.ownerUserName = login.getUserName();
		docInfoEntity.authRefer = Auth.ON;
		docInfoEntity.authDownload = Auth.ON;
		docInfoEntity.authEdit = Auth.ON;
		docInfoEntity.authDelete = Auth.ON;
		docInfoEntity.authCopy = Auth.ON;
		docInfoEntity.authMove = Auth.ON;
		docInfoEntity.authPrint = Auth.OFF;
		docInfoEntity.wfApplying = CommonFlag.OFF;
		return docInfoEntity;
	}

	/**
	 * 文書情報に対する権限判定.
	 * @param docInfoEntity
	 * @param auth
	 * @return trueなら権限あり
	 */
	public boolean hasAuth(final DocInfoEntity docInfoEntity, DocAuth auth) {
		switch (auth) {
		case REFER:
			return eq(Auth.ON, docInfoEntity.authRefer);
		case DOWNLOAD:
			return eq(Auth.ON, docInfoEntity.authDownload);
		case EDIT:
			return eq(Auth.ON, docInfoEntity.authEdit);
		case DELETE:
			return eq(Auth.ON, docInfoEntity.authDelete);
		case COPY:
			return eq(Auth.ON, docInfoEntity.authCopy);
		case MOVE:
			return eq(Auth.ON, docInfoEntity.authMove);
		case PRINT:
			return eq(Auth.ON, docInfoEntity.authPrint);
		default:
			return false;
		}
	}

	public String getAuthErrorMessage(DocAuth auth) {
		switch (auth) {
		case REFER:
			// 参照権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authRefer);
		case DOWNLOAD:
			// ダウンロード権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authDownload);
		case EDIT:
			// 編集権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authEdit);
		case DELETE:
			// 削除権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authDelete);
		case COPY:
			// コピー権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authCopy);
		case MOVE:
			// 移動権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authMove);
		case PRINT:
			// 印刷権限がありません。
			return i18n.getText(MessageCd.MSG0223, MessageCd.authPrint);
		default:
			return null;
		}
	}

	/**
	 * 文書情報の操作時のチェック処理.
	 * @param docInfo 文書情報Entity
	 * @param version バージョン
	 * @param locked 操作者がロック中である必要がある場合、trueを指定
	 * @param authes チェック対象となる権限一覧
	 * @return ロックされている場合、エラーメッセージを返す
	 */
	private String validateDocInfo(DocInfoEntity docInfoEntity, Long version, boolean locked, List<DocAuth> authes) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		if (docInfoEntity == null) {
			throw new NotFoundException("文書情報が見つかりません");
		}
		if (version != null && !eq(docInfoEntity.version, version)) {
			// 他のユーザによって更新済みです。
			return i18n.getText(MessageCd.MSG0050);
		}
		if (locked) {
			if (eq(CommonFlag.ON, docInfoEntity.lockFlag)
					&& !(eq(login.getCorporationCode(), docInfoEntity.lockCorporationCode) && eq(login.getUserCode(), docInfoEntity.lockUserCode))) {
				// 『{0}』は他のユーザが編集中です。
				return i18n.getText(MessageCd.MSG0222, docInfoEntity.title);
			}
		}
		if (authes != null) {
			for (DocAuth auth: authes) {
				if (!hasAuth(docInfoEntity, auth)) {
					return getAuthErrorMessage(auth);
				}
			}
		}
		return null;
	}

	/**
	 * 文書ファイル情報がロックされているかチェック処理.
	 * @param docId 文書ID
	 * @return ロックされている場合、エラーメッセージを返す
	 */
	private List<String> validateLockedDocFileInfo(final Long docId) {
		final List<String> errors = docFileService.validateDocFileInfos(docId);
		return errors;
	}

	/**
	 * ロックまたはロック解除処理.
	 * @param req
	 * @param isLock ロックするか trueならロック falseならロック解除
	 * @return
	 */
	private BaseResponse lockOrUnlock(DocInfoOperationRequest req, boolean isLock) {
		// 入力パラメータ
		if (req.docId == null) {
			throw new BadRequestException("文書IDが未指定です");
		} else if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("会社コードが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書情報に対するロックチェック
		final DocInfoEntity docInfo = this.getDocInfoEntity(req.docId, req.corporationCode);
		String error1 = this.validateDocInfo(docInfo, req.version, true, Arrays.asList(DocAuth.EDIT));
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 文書ファイル情報に対するロックチェック
		List<String> errors = this.validateLockedDocFileInfo(req.docId);
		if (isNotEmpty(errors)) {
			res.addAlerts(errors.toArray(new String[errors.size()]));
			return res;
		}
		// 問題なければロック／ロック解除を実行
		// 文書情報のロックorロック解除
		repository.lockOrUnlockDocInfo(req.docId, login, isLock);
		// 文書ファイル情報のロックorロック解除
		repository.lockOrUnlockDocFileInfo(req.docId, login, isLock);
		res.addSuccesses(i18n.getText(MessageCd.MSG0050));
		res.success = true;

		return res;
	}
}
