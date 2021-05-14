package jp.co.nci.iwf.endpoint.dc.dc0022;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.DocTrayResponse;
import jp.co.nci.iwf.component.tray.DocTrayService;
import jp.co.nci.iwf.component.tray.TrayConditionDef;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocTrayType;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 詳細検索サービス.
 */
@BizLogic
public class Dc0022Service extends DocTrayService {

	/** 詳細検索リポジトリ */
	@Inject private Dc0022Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public TrayInitResponse init(BaseRequest req) {
		final TrayInitResponse res = createTrayInitResponse(req, DocTrayType.DETAIL_SEARCH.toString());
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public DocTrayResponse search(TraySearchRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Long trayConfigId = ((Number)req.get("trayConfigId")).longValue();
		final Map<String, TrayConditionDef> conditionDefs =
				getTrayConditions(trayConfigId).stream()
				.collect(Collectors.toMap(tcd -> tcd.businessInfoCode, tcd -> tcd));

		// 件数のカウント
		final int allCount = repository.count(req, conditionDefs, login, login.isCorpAdmin());

		// 文書管理トレイのリクエストはMapなので基底クラスの初期化関数が使えるようダミーリクエストを生成
		final BasePagingRequest dummy = new BasePagingRequest();
		dummy.pageNo = (Integer)req.get("pageNo");
		dummy.pageSize = (Integer)req.get("pageSize");

		final DocTrayResponse res = createResponse(DocTrayResponse.class, dummy, allCount);
		if (res.allCount == 0) {
			res.results = new ArrayList<>();
		} else {
			res.results = repository.select(req, conditionDefs, login, login.isCorpAdmin());
		}
		res.success = true;
		return res;
	}
}
