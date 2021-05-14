package jp.co.dmm.customize.endpoint.ri.ri0010;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 検収対象選択サービス
 */
@ApplicationScoped
public class Ri0010Service extends BasePagingService {
	@Inject private Ri0010Repository repository;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ri0010SearchResponse init(BaseRequest req) {
		final Ri0010SearchResponse res = createResponse(Ri0010SearchResponse.class, req);
		res.success = true;
		res.mnyCds = createMnyCds(sessionHolder.getLoginInfo().getCorporationCode());
		return res;
	}

	/** 通貨の選択肢を生成 */
	private List<OptionItem> createMnyCds(String corporationCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(repository.getMnyMst(corporationCode)
				.stream()
				.map(e -> new OptionItem(e.getId().getMnyCd(), e.getMnyNm()))
				.collect(Collectors.toList()));
		return items;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Ri0010SearchRequest req) {
		final int allCount = repository.count(req);
		final Ri0010SearchResponse res = createResponse(Ri0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
