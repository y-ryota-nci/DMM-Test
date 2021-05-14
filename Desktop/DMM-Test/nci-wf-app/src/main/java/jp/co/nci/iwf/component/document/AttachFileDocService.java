package jp.co.nci.iwf.component.document;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwtAttachFileDocEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtAttachFileDoc;

/**
 * 文書管理用添付ファイルサービス.
 */
@BizLogic
public class AttachFileDocService extends BaseService {

	@Inject
	private AttachFileDocRepository repository;
	/** 文書ファイルデータサービス */
	@Inject private DocFileDataService docFileDataService;

	/**
	 * 文書IDに紐づく文書添付ファイル情報一覧取得.
	 * @param docId 文書ID
	 * @return
	 */
	public List<AttachFileDocInfo> getAttachFileDocInfoList(long docId) {
		final List<MwtAttachFileDocEx> list = repository.getMwtAttachFileDocExList(docId);
		return list.stream().map(e -> new AttachFileDocInfo(e)).collect(Collectors.toList());
	}

	/**
	 * 雑多添付ファイルの差分更新.
	 * @param docId 文書ID
	 * @param attachList 添付ファイル一覧
	 */
	public void saveMwtAttachFileDoc(Long docId, List<AttachFileDocInfo> attachFiles, boolean isFromWf) {
		final List<MwtAttachFileDoc> list = repository.getMwtAttachFileDocList(docId);
		// 文書ファイルIDをKeyにMap形式に変換
		final Map<Long, MwtAttachFileDoc> map = list.stream().collect(Collectors.toMap(MwtAttachFileDoc::getAttachFileDocId, e -> e));

		// 新規登録された文書ファイルの文書ファイルデータID一覧
		final Set<Long> docFileDataIds = new HashSet<>();

		for (AttachFileDocInfo attachFile : attachFiles) {
			final MwtAttachFileDoc org = map.remove(attachFile.attachFileDocId);
			if (org == null) {
				attachFile.docId = docId;
				Long attachFileDocId = repository.insert(attachFile);
				// DMM特殊仕様
				// WFからの文書連携により添付ファイルが作成される場合、WF側の添付ファイル内容(登録者や登録日時など)を引き継ぐ
				if (isFromWf) {
					repository.updateFromWf(attachFileDocId);
				}
				docFileDataIds.add(attachFile.docFileDataId);
			}
		}

		// 残ったものは削除されたものなので一括削除(論理削除)
		final Set<Long> deleteAttachFileDocIds = map.keySet();
		repository.deleteMwtAttachFileDoc(deleteAttachFileDocIds, sessionHolder.getLoginInfo());

		// 文書ファイルデータの更新
		// 新規に文書ファイルと紐づけられたファイルデータは削除フラグを"0:未削除"に変更する
		docFileDataService.updateDeleteFlag(docFileDataIds);
	}
}
