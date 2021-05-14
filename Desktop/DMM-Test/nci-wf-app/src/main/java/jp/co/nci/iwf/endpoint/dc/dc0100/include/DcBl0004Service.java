package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.DocAccessibleService;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書(業務文書・バインダー)ブロック：権限設定のサービス
 */
@BizLogic
public class DcBl0004Service extends BaseService implements CodeMaster {

	/** 文書フォルダサービス */
	@Inject private DocFolderService docFolderService;
	/** 文書権限情報のサービス */
	@Inject private DocAccessibleService docAccessibleService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 本業務文書に紐づいている権限一覧の取得
		if (res.contents.docId != null) {
			res.contents.accessibles = docAccessibleService.getDocAccessibles(res.contents.docId, localeCode);
		} else if (req.copyDocId != null) {
			res.contents.accessibles = docAccessibleService.getDocAccessibles(req.copyDocId, localeCode);
			res.contents.accessibles.forEach(e -> {
				e.docAccessibleId = null;
				e.docId = null;
			});
		}
		// 設定可能な権限ロール一覧の取得
		res.contents.roles = docAccessibleService.getRoleOptions(corporationCode, localeCode);
	}

	/**
	 * 文書フォルダに紐づく権限情報を返す.
	 * @param req
	 * @return
	 */
	public DcBl0004Response getAccessiblesByFolderId(DcBl0004Request req) {
		final DcBl0004Response res = createResponse(DcBl0004Response.class, req);
		if (req.docFolderId == null) {
			throw new BadRequestException("文書フォルダIDが未指定です。");
		}
		// 選択した文書フォルダ情報を取得
		final MwvDocFolder folder = docFolderService.getMwvDocFolder(req.corporationCode, req.docFolderId);
		if (folder == null) {
			throw new NotFoundException("文書フォルダが見つかりません ->"
					+ " corporationCode=" + req.corporationCode
					+ " docFolderId=" + req.docFolderId);
		}
		res.folder = folder;
		// 選択した文書フォルダに紐づく権限情報一覧を取得
		res.accessibles = docAccessibleService.getFolderAccessibles(req.docFolderId);

		res.success = true;
		return res;
	}

	/**
	 * 文書権限情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		docAccessibleService.saveMwtDocAccessibleInfo(docId, req.accessibles);
	}
}
