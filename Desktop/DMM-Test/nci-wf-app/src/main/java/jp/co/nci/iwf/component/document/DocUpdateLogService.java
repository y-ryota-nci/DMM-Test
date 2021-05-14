package jp.co.nci.iwf.component.document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocUpdateType;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.UpdateVersionType;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.UpdateHistoryInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocUpdateLog;

/**
 * 文書更新履歴のサービス
 */
@BizLogic
public class DocUpdateLogService extends BaseService {

	@Inject private DocUpdateLogRepository repository;

	/**
	 * 更新履歴一覧の取得.
	 * @param docId 文書ID
	 * @return
	 */
	public List<UpdateHistoryInfo> getHistoryList(Long docId) {
		final List<UpdateHistoryInfo> results = new ArrayList<>();
		if (docId != null) {
			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			List<MwtDocUpdateLog> list = repository.getMwtDocUpdateLog(docId, corporationCode, localeCode);
			list.stream().forEach(e -> results.add(new UpdateHistoryInfo(e)));
		}
		return results;
	}

	/**
	 * 文書更新履歴の登録.
	 * @param updateType 更新区分
	 * @param docId 文書ID
	 * @param title 件名（文書ファイルの場合はファイル名）
	 * @param contentsType コンテンツ種別
	 * @param updateUserName 更新者名
	 * @param majorVersion メジャーバージョン
	 * @param minorVersion マイナーバージョン
	 * @param updateVersionType (更新時の)更新区分
	 * @param docHistoryId 文書履歴ID
	 * @param docFileHistoryId 文書ファイル履歴ID
	 */
	public void saveMwtDocUpdateLog(DocUpdateType docUpdateType, Long docId, String title, String contentsType
			,Integer majorVersion, Integer minorVersion, String updateVersionType, String folderName, Long docHistoryId, Long docFileHistoryId) {
		this.saveMwtDocUpdateLog(docUpdateType, docId, title, contentsType, null, null
				, majorVersion, minorVersion, updateVersionType, folderName, docHistoryId, docFileHistoryId);
	}

	/**
	 * 文書更新履歴の登録.
	 * @param updateType 更新区分
	 * @param docId 文書ID
	 * @param title 件名（文書ファイルの場合はファイル名）
	 * @param contentsType コンテンツ種別
	 * @param updateUserName 更新者名
	 * @param majorVersion メジャーバージョン
	 * @param minorVersion マイナーバージョン
	 * @param updateVersionType (更新時の)更新区分
	 * @param docHistoryId 文書履歴ID
	 * @param docFileHistoryId 文書ファイル履歴ID
	 */
	public void saveMwtDocUpdateLog(DocUpdateType docUpdateType, Long docId, String title, String contentsType, String updateUserName, Timestamp updateTimestamp
			,Integer majorVersion, Integer minorVersion, String updateVersionType, String folderName, Long docHistoryId, Long docFileHistoryId) {
		// 文書更新履歴の登録件数を取得
		int count = repository.countMwtDocUpdateLog(docId);

		String docUpdateLog = this.createUpdateLog(docUpdateType, title, majorVersion, minorVersion, updateVersionType, folderName);

		// 登録用のEntityを生成
		final MwtDocUpdateLog entity = new MwtDocUpdateLog();
		entity.setDocId(docId);
		entity.setSeqNo(++count);
		entity.setContentsType(contentsType);
		if (updateTimestamp != null) {
			entity.setDocUpdateTimestamp(updateTimestamp);
		} else {
			entity.setDocUpdateTimestamp(timestamp());
		}
		if (isNotEmpty(updateUserName)) {
			entity.setDocUpdateUserName(updateUserName);
		} else {
			entity.setDocUpdateUserName(sessionHolder.getLoginInfo().getUserName());
		}
		entity.setDocUpdateLog(docUpdateLog);
		entity.setDocHistoryId(docHistoryId);
		entity.setDocFileHistoryId(docFileHistoryId);
		// 登録
		repository.insert(entity);
	}

	private String createUpdateLog(DocUpdateType docUpdateType, String title, Integer majorVersion, Integer minorVersion, String updateVersionType, String folderName) {
		switch (docUpdateType) {
			case REGIST:
				// {0}が新規登録されました。(Version {1}.{2})
				return i18n.getText(MessageCd.MSG0217, title, majorVersion, minorVersion);
			case UPDATE:
				if (eq(UpdateVersionType.DO_NOT_UPDATE, updateVersionType)) {
					// {0}が上書更新されました。
					return i18n.getText(MessageCd.MSG0219, title);
				} else {
					// {0}がバージョン更新されました。(Version {1}.{2})
					return i18n.getText(MessageCd.MSG0218, title, majorVersion, minorVersion);
				}
			case DELETE:
				// {0}が削除されました。
				return i18n.getText(MessageCd.MSG0064, title);
			case COPY:
				// {0}がコピー作成されました。
				return i18n.getText(MessageCd.MSG0220, title);
			case MOVE:
				// {0}が{1}へ移動されました。
				return i18n.getText(MessageCd.MSG0221, title, folderName);
			case WF:
				// {0}をワークフローへ連携しました。
				return i18n.getText(MessageCd.MSG0228, title);
			default:
				throw new NotFoundException("更新区分に誤りがあります。docUpdateType=" + docUpdateType);
		}
	}
}
