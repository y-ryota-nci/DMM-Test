package jp.co.nci.iwf.component.document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.DocFileInfoHistoryEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocInfo;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 文書ファイルサービス.
 */
@BizLogic
public class DocFileService extends BaseService implements DcCodeBook {

	@Inject
	private DocFileRepository repository;
	@Inject
	private DocHelper helper;
	/** 文書更新履歴サービス */
	@Inject private DocUpdateLogService docUpdateLogService;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	/**
	 * 文書ファイル情報の差分更新.
	 * @param docId 文書ID
	 * @param docFiles 文書ファイル一覧
	 */
	public void saveMwtDocFileInfo(Long docId, List<DocFileInfo> docFiles) {
		this.saveMwtDocFileInfo(docId, docFiles, false, false, false);
	}

	/**
	 * 文書ファイル情報の差分更新.
	 * @param docId 文書ID
	 * @param docFiles 文書ファイル一覧
	 * @param isLock ロックするか trueならロックする(=解除しない)
	 * @param isAddHistory 文書更新履歴を登録するか trueなら登録
	 * @param isFromWf WFからの文書連携か
	 */
	public void saveMwtDocFileInfo(Long docId, List<DocFileInfo> docFiles, boolean isLock, boolean isAddHistory, boolean isFromWf) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final List<MwtDocFileInfo> list = this.getMwtDocFileInfoList(docId);
		// 文書ファイルIDをKeyにMap形式に変換
		final Map<Long, MwtDocFileInfo> map = list.stream().collect(Collectors.toMap(MwtDocFileInfo::getDocFileId, e -> e));

		// 新規登録された文書ファイルの文書ファイルデータID一覧
		final Set<Long> docFileDataIds = new HashSet<>();

		for (DocFileInfo docFile : docFiles) {
			final MwtDocFileInfo org = map.remove(docFile.docFileId);
			if (org == null) {
				docFile.corporationCode = login.getCorporationCode();
				docFile.docId = docId;
				// ロック情報を設定
				docFile.lockFlag = isLock ? CommonFlag.ON : CommonFlag.OFF;
				docFile.lockCorporationCode = isLock ? login.getCorporationCode() : null;
				docFile.lockCorporationName = isLock ? login.getCorporationName() : null;
				docFile.lockUserCode = isLock ? login.getUserCode() : null;
				docFile.lockUserName = isLock ? login.getUserName() : null;
				// 登録実行
				final Long docFileId = repository.insert(docFile, login, isFromWf);
				// DMM特殊仕様
				// WFからの文書連携により文書ファイルが作成される場合、WF側の文書ファイル内容(登録者や登録日時など)を引き継ぐ
				if (isFromWf) {
					repository.updateFromWf(docFileId);
				}
				// 履歴情報を作成
				if (isAddHistory) {
					// WFからの文書連携の場合、文書更新日時／文書更新者名は文書ファイルの作成時点の時間と名前をセット
					final Timestamp timestampUpdated = isFromWf ? MiscUtils.parseAsTimestamp(docFile.timestampCreated) : null;
					final String updateUserName = isFromWf ? docFile.userNameCreated : null;
					// 履歴情報の登録
					docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.REGIST, docId, docFile.fileName, ContentsType.FILE, updateUserName, timestampUpdated
							,docFile.majorVersion, docFile.minorVersion, UpdateVersionType.DO_NOT_UPDATE, null, null, null);
				}
				docFileDataIds.add(docFile.docFileDataId);
			} else {
				// 既に登録済みの場合、ロックフラグ等だけを更新
				repository.lockOrUnlockDocFileInfo(org.getDocFileId(), login, isLock);
			}
		}

		// 文書ファイルデータの更新
		// 文書ファイルと紐づけられたファイルデータは削除フラグを"0:未削除"に変更する
		docFileDataService.updateDeleteFlag(docFileDataIds);
	}

	/**
	 * 文書ファイル情報のロック処理.
	 * 　紐づく文書情報が(他のユーザによって)ロック済みのものがあればロックはできない
	 * @param req
	 * @return res
	 */
	public BaseResponse lock(DocFileOperationRequest req) {
		final BaseResponse res = this.lockOrUnlock(req, true);
		return res;
	}

	/**
	 * 文書ファイル情報のロック解除処理.
	 * 　紐づく文書情報が(他のユーザによって)ロック済みのものがあればロック解除はできない
	 * @param req
	 * @return res
	 */
	public BaseResponse unlock(DocFileOperationRequest req) {
		final BaseResponse res = this.lockOrUnlock(req, false);
		return res;
	}

	/**
	 * 文書ファイル情報の移動処理.
	 * 移動先の文書情報が(他のユーザによって)ロック済みであれば移動はできない
	 * @param req
	 * @return
	 */
	public BaseResponse move(DocFileOperationRequest req) {
		// 入力パラメータのバリデーション
		if (req.docFileId == null) {
			throw new BadRequestException("文書ファイルIDが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		} else if (req.toDocId == null) {
			throw new BadRequestException("移動先の文書IDが未指定です");
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書ファイル情報に対するロックチェック
		final DocFileInfoEntity docFileInfoEntity = this.getDocFileInfoEntity(req.docId, req.docFileId);
		String error1 = this.validateDocFileInfo(docFileInfoEntity, req.version, true, Arrays.asList(DocAuth.MOVE), login);
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 移動先文書情報に対するロックチェック
		// 別のユーザがロックしていたらロックはできない
		final MwtDocInfo docInfo = repository.getMwtDocInfoByPk(req.toDocId);
		if (docInfo == null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0223, MessageCd.docInfo));
			return res;
		} else if (eq(CommonFlag.ON, docInfo.getLockFlag())
				&& !(eq(login.getCorporationCode(), docInfo.getLockCorporationCode()) && eq(login.getUserCode(), docInfo.getLockUserCode()))) {
			// 『{0}』は他のユーザが編集中です。
			res.addAlerts(i18n.getText(MessageCd.MSG0222, docInfo.getTitle()));
			return res;
		}
		// 問題なければ文書ファイルを移動処理を実行
		final MwtDocFileInfo entity = repository.getMwtDocFileInfoByPk(docFileInfoEntity.docFileId);
		entity.setDocId(req.toDocId);
		repository.update(entity);
		// 文書更新履歴の登録
		docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.MOVE, docFileInfoEntity.docId, docFileInfoEntity.fileName, ContentsType.FILE
				,docFileInfoEntity.majorVersion, docFileInfoEntity.minorVersion, UpdateVersionType.DO_NOT_UPDATE, docInfo.getTitle(), null, null);

		res.addSuccesses(i18n.getText(MessageCd.MSG0065, docFileInfoEntity.fileName));
		res.success = true;
		return res;
	}

	/**
	 * 文書ファイル情報のコピー処理.
	 * 移動先の文書情報が(他のユーザによって)ロック済みであればコピーはできない
	 * @param req
	 * @return
	 */
	public BaseResponse copy(DocFileOperationRequest req) {
		// 入力パラメータのバリデーション
		if (req.docFileId == null) {
			throw new BadRequestException("文書ファイルIDが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		} else if (req.toDocId == null) {
			throw new BadRequestException("コピー先の文書IDが未指定です");
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書ファイル情報に対するロックチェック
		final DocFileInfoEntity docFileInfoEntity = this.getDocFileInfoEntity(req.docId, req.docFileId);
		String error1 = this.validateDocFileInfo(docFileInfoEntity, req.version, false, Arrays.asList(DocAuth.COPY), login);
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 移動先文書情報に対するロックチェック
		// 別のユーザがロックしていたらロックはできない
		final MwtDocInfo docInfo = repository.getMwtDocInfoByPk(req.toDocId);
		if (docInfo == null) {
			res.addAlerts(i18n.getText(MessageCd.MSG0223, MessageCd.docInfo));
			return res;
		} else if (eq(CommonFlag.ON, docInfo.getLockFlag())
				&& !(eq(login.getCorporationCode(), docInfo.getLockCorporationCode()) && eq(login.getUserCode(), docInfo.getLockUserCode()))) {
			// 『{0}』は他のユーザが編集中です。
			res.addAlerts(i18n.getText(MessageCd.MSG0222, docInfo.getTitle()));
			return res;
		}
		// 問題なければ文書ファイルをコピー処理を実行
		final MwtDocFileInfo org = repository.getMwtDocFileInfoByPk(docFileInfoEntity.docFileId);
		final DocFileInfo inputed = new DocFileInfo();
		inputed.corporationCode = org.getCorporationCode();
		inputed.docId = req.toDocId;
		inputed.majorVersion = org.getMajorVersion();
		inputed.minorVersion = org.getMinorVersion();
		// バージョンをコピーしない場合は"1.0"で作成する
		if (eq(CommonFlag.ON, req.notVersionCopy)) {
			inputed.majorVersion = 1;
			inputed.minorVersion = 0;
		}
		inputed.comments = org.getComments();
		inputed.docFileDataId = org.getDocFileDataId();
		inputed.lockFlag = CommonFlag.OFF;
		repository.insert(inputed, login, false);
		// 文書更新履歴の登録
		docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.COPY, inputed.docId, docFileInfoEntity.fileName, ContentsType.FILE
				,docFileInfoEntity.majorVersion, docFileInfoEntity.minorVersion, UpdateVersionType.DO_NOT_UPDATE, docInfo.getTitle(), null, null);

		// {0}がコピー作成されました。
		res.addSuccesses(i18n.getText(MessageCd.MSG0220, docFileInfoEntity.fileName));

		res.success = true;
		return res;
	}

	/**
	 * 文書ファイルの削除処理.
	 * @return
	 */
	public BaseResponse delete(DocFileOperationRequest req) {

		final BaseResponse res = createResponse(BaseResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		final List<DocFileInfoEntity> deleteDocFiles = new ArrayList<>();
		final List<String> alerts = new ArrayList<>();
		for (DocFileInfo docFileInfo : req.deleteDocFiles) {
			if (isNotEmpty(docFileInfo.docFileId)) {
				final DocFileInfoEntity docFileInfoEntity = this.getDocFileInfoEntity(req.docId, docFileInfo.docFileId);
				String error = this.validateDocFileInfo(docFileInfoEntity, docFileInfo.version, true, Arrays.asList(DocAuth.EDIT, DocAuth.DELETE), login);
				if (isEmpty(error)) {
					deleteDocFiles.add(docFileInfoEntity);
				} else {
					alerts.add(error);
				}
			}
		}

		if (alerts.isEmpty()) {
			// ここまできたら文書ファイルを削除していく
			// 文書ファイル情報の削除(論理削除)
			final List<Long> deleteDocFileIds = deleteDocFiles.stream().map(e -> e.docFileId).collect(Collectors.toList());
			repository.deleteMwtDocFileInfo(deleteDocFileIds, login);
			// さらに文書更新履歴に文書ファイル削除の履歴を追加
			deleteDocFiles.stream().forEach(e -> {
				docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.DELETE, e.docId, e.fileName, ContentsType.FILE
						,e.majorVersion, e.minorVersion, null, null, null, null);
			});
			res.success = true;
		} else {
			res.addAlerts(alerts.toArray(new String[alerts.size()]));
		}

		return res;
	}

	/**
	 * 文書ファイル情報に対する権限判定.
	 * @param docInfoEntity
	 * @param auth
	 * @return trueなら権限あり
	 */
	public boolean hasAuth(final DocFileInfoEntity docFileInfoEntity, DocAuth auth) {
		switch (auth) {
		case REFER:
			return eq(Auth.ON, docFileInfoEntity.authRefer);
		case DOWNLOAD:
			return eq(Auth.ON, docFileInfoEntity.authDownload);
		case EDIT:
			return eq(Auth.ON, docFileInfoEntity.authEdit);
		case DELETE:
			return eq(Auth.ON, docFileInfoEntity.authDelete);
		case COPY:
			return eq(Auth.ON, docFileInfoEntity.authCopy);
		case MOVE:
			return eq(Auth.ON, docFileInfoEntity.authMove);
		case PRINT:
			return eq(Auth.ON, docFileInfoEntity.authPrint);
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
	 * 文書ファイル情報一覧取得
	 * @param docId 文書ID
	 * @return
	 */
	public List<MwtDocFileInfo> getMwtDocFileInfoList(long docId) {
		return repository.getMwtDocFileList(docId);
	}

	/**
	 * 文書ファイル情報Entity取得.
	 * @param docFileId 文書ファイルID
	 * @return 文書情報Entity
	 */
	public DocFileInfoEntity getDocFileInfoEntity(long docId, long docFileId) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		final Set<String> hashValues = helper.toHashValues(login);
		final List<DocFileInfoEntity> list = repository.getDocFileInfoEntityList(docId, docFileId, hashValues, login.isCorpAdmin());
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * 文書IDに紐づく文書ファイル情報Entity一覧取得.
	 * @param docId 文書ID
	 * @return 文書情報Entity
	 */
	public List<DocFileInfoEntity> getDocFileInfoEntityList(long docId) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		final Set<String> hashValues = helper.toHashValues(login);
		return repository.getDocFileInfoEntityList(docId, null, hashValues, login.isCorpAdmin());
	}

	/**
	 * 文書IDに紐づく文書ファイル情報一覧取得.
	 * @param docId 文書ID
	 * @return
	 */
	public List<DocFileInfo> getDocFileInfoList(long docId) {
		final List<DocFileInfoEntity> list = this.getDocFileInfoEntityList(docId);
		final LoginInfo login = sessionHolder.getLoginInfo();
		return list.stream().map(e -> new DocFileInfo(e, login)).collect(Collectors.toList());
	}

	/**
	 * 文書ファイル履歴Entity一覧取得.
	 * @param docFileId 文書ファイルID
	 */
	public List<DocFileInfoHistoryEntity> getDocFileInfoHistoryEntityList(long docFileId) {
		return repository.getDocFileInfoHistoryEntityList(docFileId);
	}

	/**
	 * 文書ファイル情報の履歴情報の登録.
	 * @param docFileId 文書ファイルID
	 * @return 文書ファイル履歴ID
	 */
	private Long saveDocFileHistory(Long docFileId) {
		if (docFileId == null) {
			return null;
		}
		// まず現在の各履歴に登録されている履歴連番をインクリメント
		// こうすることで常に最新の履歴は履歴連番"0"で作成される
		repository.updateHistorySeqNo(docFileId);

		// 文書ファイル情報履歴の登録
		// 登録後、文書ファイル履歴IDを取得
		Long docFileHistoryId = repository.insertMwtDocFileInfoHistory(docFileId);

		return docFileHistoryId;
	}

	public List<String> validateDocFileInfos(final Long docId) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final List<DocFileInfoEntity> docFiles = this.getDocFileInfoEntityList(docId);
		final List<String> errors = new ArrayList<>();
		for (DocFileInfoEntity docFileInfoEntity: docFiles) {
			String error = this.validateDocFileInfo(docFileInfoEntity, null, true, null, login);
			if (isNotEmpty(error)) {
				errors.add(error);
			}
		}
		return errors;
	}

	/**
	 * ロックまたはロック解除処理.
	 * @param req
	 * @param isLock ロックするか trueならロック falseならロック解除
	 * @return
	 */
	private BaseResponse lockOrUnlock(DocFileOperationRequest req, boolean isLock) {
		// 入力パラメータのバリデーション
		if (req.docId == null || req.docFileId == null) {
			throw new BadRequestException("文書ID、文書ファイルIDが未指定です");
		} else if (req.version == null) {
			throw new BadRequestException("バージョンが未指定です");
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書ファイル情報に対するロックチェック
		final DocFileInfoEntity docFileInfoEntity = this.getDocFileInfoEntity(req.docId, req.docFileId);
		String error1 = this.validateDocFileInfo(docFileInfoEntity, req.version, true, Arrays.asList(DocAuth.EDIT), login);
		if (isNotEmpty(error1)) {
			res.addAlerts(error1);
			return res;
		}
		// 文書情報に対するロックチェック
		// 別のユーザがロックしていたらロックはできない
		final MwtDocInfo docInfo = repository.getMwtDocInfoByPk(docFileInfoEntity.docId);
		if (eq(CommonFlag.ON, docInfo.getLockFlag())
				&& !(eq(login.getCorporationCode(), docInfo.getLockCorporationCode()) && eq(login.getUserCode(), docInfo.getLockUserCode()))) {
			// 『{0}』は他のユーザが編集中です。
			res.addAlerts(i18n.getText(MessageCd.MSG0222, docInfo.getTitle()));
			return res;
		}
		// 問題なければロック／ロック解除を実行
		repository.lockOrUnlockDocFileInfo(req.docFileId, login, isLock);

		res.success = true;
		return res;
	}

	/**
	 * 文書ファイル情報の操作時のチェック処理.
	 * @param docInfo 文書情報Entity
	 * @param version バージョン
	 * @param locked 操作者がロック中である必要がある場合、trueを指定
	 * @param authes チェック対象となる権限一覧
	 * @return ロックされている場合、エラーメッセージを返す
	 */
	private String validateDocFileInfo(DocFileInfoEntity docFileInfoEntity, Long version, boolean locked, List<DocAuth> authes, final LoginInfo login) {
		if (docFileInfoEntity == null) {
			// 他のユーザによって削除済みです。
			return i18n.getText(MessageCd.MSG0048);
		}
		if (version != null && !eq(docFileInfoEntity.version, version)) {
			// 他のユーザによって更新済みです。
			return i18n.getText(MessageCd.MSG0050);
		}
		if (locked) {
			if (eq(CommonFlag.ON, docFileInfoEntity.lockFlag)
					&& !(eq(login.getCorporationCode(), docFileInfoEntity.lockCorporationCode) && eq(login.getUserCode(), docFileInfoEntity.lockUserCode))) {
				// 『{0}』は他のユーザが編集中です。
				return i18n.getText(MessageCd.MSG0222, docFileInfoEntity.fileName);
			}
		}
		if (authes != null) {
			for (DocAuth auth: authes) {
				if (!hasAuth(docFileInfoEntity, auth)) {
					return getAuthErrorMessage(auth);
				}
			}
		}
		return null;
	}

	/**
	 * 文書ファイル情報更新.
	 * @param docFileInfo 文書ファイル情報
	 * @param updateVersionType バージョン更新区分
	 */
	public BaseResponse update(DocFileInfo docFileInfo, String updateVersionType) {
		final BaseResponse res = createResponse(BaseResponse.class, null);
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 文書ファイル情報に対するロック等をチェック
		final DocFileInfoEntity docFileInfoEntity = this.getDocFileInfoEntity(docFileInfo.docId, docFileInfo.docFileId);
		String error = this.validateDocFileInfo(docFileInfoEntity, docFileInfo.version, true, Arrays.asList(DocAuth.EDIT), login);
		if (isNotEmpty(error)) {
			res.addAlerts(error);
			return res;
		}

		// ここまできたら更新実行
		// バージョン情報が更新される場合、現在の文書ファイル情報を履歴テーブルへ移す
		Long docFileHistoryId = null;
		if (!eq(UpdateVersionType.DO_NOT_UPDATE, updateVersionType)) {
			// 履歴情報の登録
			docFileHistoryId = this.saveDocFileHistory(docFileInfo.docFileId);
		}

		// 現在の文書ファイル情報を取得
		final MwtDocFileInfo org = repository.getMwtDocFileInfoByPk(docFileInfo.docFileId);
		// 次にバージョン更新区分におうじてバージョンを書き換える
		if (eq(UpdateVersionType.MINOR_VERSION_UP, updateVersionType)) {
			// メジャーバージョンはそのまま
			// マイナーバージョンを1つあげる
			docFileInfo.majorVersion = org.getMajorVersion();
			docFileInfo.minorVersion = org.getMinorVersion() + 1;
		} else if (eq(UpdateVersionType.MAJOR_VERSION_UP, updateVersionType)) {
			// メジャーバージョンを1つあげる
			// マイナーバージョンは0固定
			docFileInfo.majorVersion = org.getMajorVersion() + 1;
			docFileInfo.minorVersion = 0;
		} else {
			docFileInfo.majorVersion = org.getMajorVersion();
			docFileInfo.minorVersion = org.getMinorVersion();
		}
		// 文書ファイル情報の作成者名、更新者名を設定
		docFileInfo.userNameCreated = login.getUserName();
		docFileInfo.userNameUpdated = login.getUserName();

		// 文書ファイル情報更新
		repository.update(org, docFileInfo);

		// 文書ファイルデータの更新
		// 文書ファイルと紐づけられたファイルデータは削除フラグを"0:未削除"に変更する
		final Set<Long> docFileDataIds = new HashSet<>();
		docFileDataIds.add(docFileInfo.docFileDataId);
		docFileDataService.updateDeleteFlag(docFileDataIds);

		// 最後に文書更新履歴を追加
		docUpdateLogService.saveMwtDocUpdateLog(DocUpdateType.UPDATE, docFileInfo.docId, docFileInfo.fileName, ContentsType.FILE
				,docFileInfo.majorVersion, docFileInfo.minorVersion, updateVersionType, null, null, docFileHistoryId);

		res.success = true;
		return res;
	}

}
