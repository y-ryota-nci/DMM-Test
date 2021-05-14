package jp.co.nci.iwf.endpoint.up;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtUploadRegistered;

/**
 * 画面定義アップロード画面リポジトリ
 */
@ApplicationScoped
public class CommonUploadRepository extends BaseRepository {
	@Inject private NumberingService numbering;
	@Inject private SessionHolder sessionHolder;

	/**
	 * アップロードファイル情報をインサート
	 * @param holder ダウンロード用DTO
	 * @param bytes ファイルデータ
	 * @param f ファイル情報
	 * @param login 操作者情報
	 */
	public MwtUploadFile insertMwtUploadFile(BaseDownloadDto holder, byte[] bytes, String fileName, String uploadKind) {
		final long uploadFileId = numbering.newPK(MwtUploadFile.class);
		final MwtUploadFile up = new MwtUploadFile();
		up.setDeleteFlag(DeleteFlag.OFF);
		up.setFileAppVersion(holder.appVersion);
		up.setFileCorporationCode(holder.corporationCode);
		up.setFileData(bytes);
		up.setFileDbString(holder.dbDestination);
		up.setFileHostName(holder.hostName);
		up.setFileHostIpAddr(holder.hostIpAddr);
		up.setFileName(fileName);
		up.setFileSize(bytes.length);
		up.setFileTimestamp(holder.timestampCreated);
		up.setRegisteredFlag(CommonFlag.OFF);
		up.setUploadCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		up.setUploadDatetime(timestamp());
		up.setUploadFileId(uploadFileId);
		up.setUploadKind(uploadKind);
		up.setVersion(1L);
		em.persist(up);
		em.flush();
		return up;
	}

	/** アップロードファイル情報を抽出 */
	public MwtUploadFile getMwtUploadFile(long uploadFileId) {
		return em.find(MwtUploadFile.class, uploadFileId);
	}

	/**
	 * アップロードファイル情報を登録済みへ更新
	 * @param up アップロードファイル情報
	 * @param configs アップロードする際の設定内容
	 */
	public void complateToRegister(MwtUploadFile up, Object configs) {
		// アップロードファイル情報を設定済へ更新
		up.setRegisteredFlag(CommonFlag.ON);
		em.merge(up);

		// 既存のアップロードファイル登録情報の最新フラグを落とす
		final Object[] params = { up.getUploadFileId() };
		execSql(getSql("UP0000_01"), params);

		// アップロードファイル情報を新規に登録
		final String json = toJsonFromObj(configs);
		final long uploadFileRegisteredId = numbering.newPK(MwtUploadRegistered.class);
		final MwtUploadRegistered r = new MwtUploadRegistered();
		r.setDeleteFlag(DeleteFlag.OFF);
		r.setLatestFlag(CommonFlag.ON);
		r.setRegisteredConfig(json);
		r.setRegisteredCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		r.setRegisteredDatetime(timestamp());
		r.setRegisteredUserCode(sessionHolder.getLoginInfo().getUserCode());
		r.setUploadFileId(up.getUploadFileId());
		r.setUploadFileRegisteredId(uploadFileRegisteredId);
		r.setVersion(1L);
		em.persist(r);
	}

}
