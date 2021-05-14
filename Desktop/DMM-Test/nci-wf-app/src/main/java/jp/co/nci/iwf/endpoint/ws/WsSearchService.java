package jp.co.nci.iwf.endpoint.ws;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmAuthTransferInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetWfmAuthTransferListInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmAuthTransferOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetWfmAuthTransferListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.base.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.base.impl.WfmAuthTransferImpl;
import jp.co.nci.integrated_workflow.model.view.WfvAuthTransfer;
import jp.co.nci.integrated_workflow.param.input.SearchWfvAuthTransferInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfvAuthTransferOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 代理設定一覧のサービス.
 */
@BizLogic
public class WsSearchService extends BasePagingService {

	@Inject protected WfInstanceWrapper wf;
	@Inject private Logger log;
	@Inject private AccessLogService accessLog;
	@Inject private WsSaveService wsSaveService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public WsSearchResponse init(WsSearchRequest req) {
		final WsSearchResponse res = createResponse(WsSearchResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public WsSearchResponse search(WsSearchRequest req) {
		// 検索条件の生成
		final SearchWfvAuthTransferInParam in = new SearchWfvAuthTransferInParam();
		in.setUserCodeFrom(req.userCode);
		in.setValid(false);
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		final SearchWfvAuthTransferOutParam out = wf.searchWfvAuthTransfer(in);
		final List<WfvAuthTransfer> list = out.getAuthTransferList();

		int allCount = out.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final WsSearchResponse res = createResponse(WsSearchResponse.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = list;
		res.success = true;
		return res;
	}

	/**
	 * 代理設定を削除
	 * @param req
	 * @return
	 */
	@Transactional
	public WsSearchResponse delete(WsSearchRequest req) {
		for (String entry : req.deleteList) {
			String[]keys = entry.split("\t");
			WfmAuthTransfer auth = wsSaveService.getWfmAuthTransfer(keys[0], toLong(keys[1]));
			final DeleteWfmAuthTransferInParam in = new DeleteWfmAuthTransferInParam();
			in.setWfmAuthTransfer(auth);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final DeleteWfmAuthTransferOutParam out = wf.deleteWfmAuthTransfer(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);

			// 代理者離任通知メールの送信
			boolean requireSendMail = corpProp.getBool(CorporationProperty.AUTH_TRANSFER_NOTIFICATION, false);
			if (requireSendMail) {
				log.info("システムプロパティで代理者通知をする設定になっているため、代理者離任通知メールを送信します。");
				wsSaveService.sendMail(auth, MailTemplateFileName.AUTH_TRANSFER_DELETE);
			} else {
				String msg = "システムプロパティで代理者通知をしない設定になっているため、代理者離任通知メールを送信しません。";
				log.info(msg);
				accessLog.appendDetail(msg);
			}
		}
		final WsSearchResponse res = createResponse(WsSearchResponse.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, "代理設定情報"));
		return res;
	}

	/**
	 * 代理設定のチェック
	 * @param req
	 * @return
	 */
	public WsSearchResponse check(WsSearchRequest req) {
		// 検索条件の生成
		final WfmAuthTransfer cond = new WfmAuthTransferImpl();
		cond.setCorporationCode(req.corporationCode);
		cond.setSeqNoAuthTransfer(req.seqNoAuthTransfer);
		cond.setDeleteFlag(DeleteFlag.OFF);
		final GetWfmAuthTransferListInParam in = new GetWfmAuthTransferListInParam();
		in.setWfmAuthTransfer(cond);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final GetWfmAuthTransferListOutParam out = wf.getWfmAuthTransferList(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
			throw new WfException(out);
		}
		// 検索結果の代理設定情報を取得
		final WfmAuthTransfer obj = out.getWfmAuthTransferList().stream().findFirst().orElse(null);
		// レスポンス生成
		final WsSearchResponse res = createResponse(WsSearchResponse.class, req);
		res.success = true;
		res.wfmAuthTransfer = obj;
		return res;
	}
}
