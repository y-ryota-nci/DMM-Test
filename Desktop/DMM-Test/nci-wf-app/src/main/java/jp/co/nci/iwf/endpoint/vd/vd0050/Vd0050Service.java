package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 外部Javascript一覧のサービス
 */
@BizLogic
public class Vd0050Service extends BasePagingService {
	/** 企業サービス */
	@Inject private CorporationService corp;
	@Inject private Vd0050Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0050InitResponse init(BaseRequest req) {
		final Vd0050InitResponse res = createResponse(Vd0050InitResponse.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0050SearchResponse search(Vd0050SearchRequest req) {
		Map<String, String> conatinerJsMap = repository.getContainerJs(req.corporationCode)
				.stream().collect(Collectors.groupingBy(
						cj -> cj.fileName,
						Collectors.mapping(cj -> cj.containerCode + " " + cj.containerName, Collectors.joining(", "))));
		Map<String, String> screenJsMap = repository.getScreenJs(req.corporationCode)
				.stream().collect(Collectors.groupingBy(
						sj -> sj.fileName,
						Collectors.mapping(sj -> sj.screenCode + " " + sj.screenName, Collectors.joining(", "))));

		final int allCount = repository.count(req);
		final Vd0050SearchResponse res = createResponse(Vd0050SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.results.forEach(r -> {
			// Javascriptを参照している画面とコンテナを設定
			final Vd0050Entity e = (Vd0050Entity)r;
			e.screenInUse = screenJsMap.get(e.fileName);
			e.containerInUse = conatinerJsMap.get(e.fileName);
		});
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Vd0050DeleteRequest req) {
		for (Long javascriptId : req.javascriptIds) {
			repository.delete(javascriptId);
			multi.physicalDelete("MWM_JAVASCRIPT", javascriptId);
		}
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, "Javascript"));
		res.success = true;
		return res;
	}
}
