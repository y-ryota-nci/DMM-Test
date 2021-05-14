package jp.co.dmm.customize.endpoint.bd.bd0801;

import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.jpa.entity.mw.BgtPln;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 予算入力画面サービス
 */
@BizLogic
public class Bd0801Service extends BasePagingService {
	@Inject private MwmLookupService lookup;
	@Inject private Bd0801Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Bd0801InitResponse init(Bd0801InitRequest req) {
		final Bd0801InitResponse res = createResponse(Bd0801InitResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();

		res.years = repository.getYearList();
		res.orgLv2s = repository.getOrgLv2List(login.getCorporationCode());
		res.organizationCodeLv2 = req.organizationCodeLv2;
		res.orgLv3s = repository.getOrgLv3List(login.getCorporationCode(), req.organizationCodeLv2);
		res.organizationCodeLv3 = req.organizationCodeLv3;
		res.rcvCostPayTps = lookup.getOptionItems(LookupGroupId.RCVINSP_PAY_TP, true);
		res.bsplTp = lookup.getOptionItems(LookupGroupId.BS_PL_TP, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Bd0801SearchResponse search(Bd0801SearchRequest req) {
		if (isEmpty(req.organizationCodeLv3)) throw new BadRequestException("部・室が未指定です");
		if (isEmpty(req.rcvCostPayTp)) throw new BadRequestException("検収基準／支払基準が未指定です");
		if (isEmpty(req.yrCd)) throw new BadRequestException("年度が未指定です");
		if (isEmpty(req.bsplTp)) throw new BadRequestException("BS/PL区分が未指定です");

		final Bd0801SearchResponse res = createResponse(Bd0801SearchResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();
		req.companyCd = login.getCorporationCode();

		res.results = repository.select(req);
		res.success = true;
		return res;
	}

	/**
	 * 本部変更時
	 * @param req
	 * @return
	 */
	public Bd0801InitResponse changeOrgLevel2(Bd0801InitRequest req) {
		if (isEmpty(req.organizationCodeLv2)) throw new BadRequestException("本部が未指定です");

		final Bd0801InitResponse res = createResponse(Bd0801InitResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();
		res.orgLv3s = repository.getOrgLv3List(login.getCorporationCode(), req.organizationCodeLv2);
		res.success = true;
		return res;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Bd0801SearchResponse save(Bd0801SearchRequest req) {
		// 既存の予算計画を抽出し、予算科目コードをキーにMap化

		final LoginInfo login = sessionHolder.getLoginInfo();
		req.companyCd = login.getCorporationCode();
		final Map<String, BgtPln> currents = repository.getBgtPln(req);

		// 消し込めれば更新、消し込めなければ新規インサート
		for (Bd0801Entity input : req.inputs) {
			input.companyCd = login.getCorporationCode();

			BgtPln current = currents.remove(input.bgtItmCd);
			if (current == null)
				repository.insert(input);
			else
				repository.update(current, input);
		}

		final Bd0801SearchResponse res = createResponse(Bd0801SearchResponse.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, "予算計画"));
		res.results = repository.select(req);
		return res;
	}

}
