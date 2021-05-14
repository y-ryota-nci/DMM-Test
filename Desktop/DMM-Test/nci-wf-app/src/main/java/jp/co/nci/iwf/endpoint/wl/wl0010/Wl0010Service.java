package jp.co.nci.iwf.endpoint.wl.wl0010;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.tray.download.TrayConfigDownloader;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * トレイ設定一覧（管理者）サービス
 */
@ApplicationScoped
@Typed(Wl0010Service.class)
public class Wl0010Service extends BasePagingService {
	@Inject protected Wl0010Repository repository;
	@Inject private MwmLookupService mwmLookup;
	/** トレイ設定のダウンロードサービス */
	@Inject private TrayConfigDownloader downloader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Wl0010Response res = createResponse(Wl0010Response.class, req);
		res.systemFlags = mwmLookup.getOptionItems(LookupGroupId.SYSTEM_FLAG, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Wl0010Response search(Wl0010Request req) {
		final int allCount = repository.count(req);
		final Wl0010Response res = createResponse(Wl0010Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Wl0010Response delete(Wl0010Request req) {
		Wl0010Response res = createResponse(Wl0010Response.class, req);
		if (req.trayConfigIds == null || req.trayConfigIds.isEmpty()) {
			res.addAlerts(i18n.getText(MessageCd.MSG0135));
			res.success = false;
		}
		else {
			for (Long trayConfigId : req.trayConfigIds) {
				repository.delete(trayConfigId);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.trayConfig));
			res.success = true;
		}
		return res;
	}

	/**
	 * 画面定義のZIPダウンロード
	 * @param corporationCode
	 * @return
	 */
	public StreamingOutput downloadZip(String corporationCode) {
		return downloader.setup(corporationCode);
	}
}
