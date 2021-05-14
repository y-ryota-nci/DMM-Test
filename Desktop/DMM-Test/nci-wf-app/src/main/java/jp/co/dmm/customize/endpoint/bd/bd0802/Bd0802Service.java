package jp.co.dmm.customize.endpoint.bd.bd0802;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Entity;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnHstver;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 予算計画履歴作成画面サービス
 */
@BizLogic
public class Bd0802Service extends BaseService {
	@Inject private Bd0802Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Bd0802InitRequest req) {
		if (isEmpty(req.organizationCodeLv2))
			throw new BadRequestException("本部が未設定です");
		if (isEmpty(req.organizationCodeLv3))
			throw new BadRequestException("部・室が未設定です");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が未設定です");
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が未設定です");
		if (isEmpty(req.bsplTp))
			throw new BadRequestException("B/S/PL区分が未設定です");

		final Bd0802InitResponse res = createResponse(Bd0802InitResponse.class, req);
		res.results = repository.getBgtPln(req);
		final Bd0801Entity e = res.results.get(0);
		res.yrCd = e.yrCd;
		res.yrNm = e.yrNm;
		res.rcvCostPayTp = e.rcvCostPayTp;
		res.organizationNameLv2 = e.organizationNameUp;
		res.organizationCodeLv3 = e.organizationCode;
		res.organizationNameLv3 = e.organizationName;
		res.rcvCostPayTpNm = e.rcvCostPayTpNm;
		res.bsplTp = e.bsplTp;
		res.bsplTpNm = e.bsplTpNm;
		res.success = true;
		return res;
	}

	/**
	 * 履歴作成
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse create(Bd0802CreateRequest req) {
		if (isEmpty(req.organizationCodeLv3))
			throw new BadRequestException("部・室が未設定です");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が未設定です");
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が未設定です");
		if (isEmpty(req.hstNm))
			throw new BadRequestException("履歴名称が未設定です");

		// 予算履歴バージョン
		final LoginInfo login = sessionHolder.getLoginInfo();
		req.companyCd = login.getCorporationCode();
		final BgtPlnHstver ver = repository.insertBgtPlnHstver(req);
		// 予算履歴
		repository.insertBgtPlnHst(req, ver);

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, "予算計画履歴"));
		return res;
	}

}
