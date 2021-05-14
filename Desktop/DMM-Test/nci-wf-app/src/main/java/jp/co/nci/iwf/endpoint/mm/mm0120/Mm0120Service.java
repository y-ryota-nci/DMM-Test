package jp.co.nci.iwf.endpoint.mm.mm0120;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * お知らせ一覧画面サービス
 */
@BizLogic
public class Mm0120Service extends BasePagingService {
	@Inject private Mm0120Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0120InitResponse init(BaseRequest req) {
		final Mm0120InitResponse res = createResponse(Mm0120InitResponse.class, req);
		res.ymd = today();
		res.hhmm = "00:00";
		res.corporations = createCorporations();
		res.success = true;
		return res;
	}

	/** 公開先の企業の選択肢を生成 */
	public List<OptionItem> createCorporations() {
		// 企業コードがASPなら、全会社へのブロードキャストとみなす
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final List<OptionItem> items = new ArrayList<>();
		items.add(new OptionItem(CorporationCode.ASP, i18n.getText(MessageCd.allCorporations)));
		if (!eq(CorporationCode.ASP, corporationCode)) {
			items.add(new OptionItem(corporationCode, i18n.getText(MessageCd.myCorporation)));
		}
		return items;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0120SearchResponse search(Mm0120SearchRequest req) {
		final int allCount = repository.count(req);
		final Mm0120SearchResponse res = createResponse(Mm0120SearchResponse.class, req, allCount);
		res.results = modify(repository.select(req, res));
		res.success = true;
		return res;
	}

	private List<Mm0120Entity> modify(List<Mm0120Entity> list) {
		list.forEach(e -> {
			if (eq(e.corporationCode, CorporationCode.ASP))
				e.corporation = i18n.getText(MessageCd.allCorporations);
			else
				e.corporation = i18n.getText(MessageCd.myCorporation);
		});
		return list;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Mm0120SearchRequest req) {
		if (req.announcementIds == null || req.announcementIds.isEmpty())
			throw new BadRequestException("削除対象のお知らせIDが未指定です");

		for (Long announcementId : req.announcementIds)
			repository.delete(announcementId);

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.informationSetting));
		res.success = true;
		return res;
	}

}
