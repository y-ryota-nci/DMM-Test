package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.document.DocContentsInfoService;
import jp.co.nci.iwf.component.document.DocInfoOperationRequest;
import jp.co.nci.iwf.component.document.DocInfoService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書・バインダー)ブロック：ボタンのサービス
 */
@BizLogic
public class DcBl0000Service extends BaseService implements CodeMaster, DcCodeBook {

	/** 文書管理サービス */
	@Inject private DocInfoService docService;
	/** 文書コンテンツ情報サービス */
	@Inject private DocContentsInfoService docContentsInfoService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		if (res.contents.docId != null) {
			final Integer majorVersion = res.contents.docInfo.majorVersion;
			final Integer minorVersion = res.contents.docInfo.minorVersion;
			final List<OptionItem> updateVersionTypes = new ArrayList<>();
			updateVersionTypes.add(new OptionItem(UpdateVersionType.DO_NOT_UPDATE, i18n.getText(MessageCd.MSG0214, majorVersion, minorVersion)));
			updateVersionTypes.add(new OptionItem(UpdateVersionType.MINOR_VERSION_UP, i18n.getText(MessageCd.MSG0215, majorVersion, minorVersion+1)));
			updateVersionTypes.add(new OptionItem(UpdateVersionType.MAJOR_VERSION_UP, i18n.getText(MessageCd.MSG0216, majorVersion+1, 0)));
			res.contents.updateVersionTypes = updateVersionTypes;
		}
	}

	/**
	 * 文書情報ロック処理.
	 * @param req
	 * @return res
	 */
	public BaseResponse lock(DocInfoOperationRequest req) {
		return docService.lock(req);
	}

	/**
	 * 文書情報アンロック処理.
	 * @param req
	 * @return res
	 */
	public BaseResponse unlock(DocInfoOperationRequest req) {
		return docService.unlock(req);
	}

	/**
	 * 文書情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// 文書情報の差分更新
		final Long docId = docService.saveMwtDocInfo(req.contents.docId, req.docInfo);
		res.docId = docId;
		if (res.ctx != null) {
			res.ctx.docId = docId;
		}
	}

	/**
	 * 文書管理項目の差分更新.
	 * @param req
	 * @param res
	 */
	public void saveDocBizInfo(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		docService.updateDocBizInfo(docId, res.docBizInfos);
	}

	/**
	 * 文書コンテンツ情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void saveContents(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		docContentsInfoService.saveMwtDocContentsInfo(res.docId, req.docInfo, req.attributeExs, req.docFiles, res.ctx);
	}
}
