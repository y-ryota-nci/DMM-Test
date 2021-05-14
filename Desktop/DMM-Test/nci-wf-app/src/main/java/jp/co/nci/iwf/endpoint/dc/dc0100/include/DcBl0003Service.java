package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書・バインダー)ブロック：文書属性のサービス
 */
@BizLogic
public class DcBl0003Service extends BaseService implements CodeMaster {

	/** 文書フォルダファイルサービス */
	@Inject protected DocFolderService docFolderService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		// 文書フォルダの選択はドロップダウンからポップアップ形式に変更したので下記処理は不要
//		// フォルダ一覧の取得
//		final List<OptionItem> folders = docFolderService.getFolderOptionList(null, null, sessionHolder.getLoginInfo().isCorpAdmin());
//		folders.add(0, OptionItem.EMPTY);
//		res.contents.folders = folders;
	}

}
