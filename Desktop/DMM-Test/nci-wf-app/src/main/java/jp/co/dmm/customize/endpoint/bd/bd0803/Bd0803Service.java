package jp.co.dmm.customize.endpoint.bd.bd0803;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 予算履歴メンテナンスサービス
 */
@BizLogic
public class Bd0803Service extends BaseService {
	@Inject private Bd0803Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Bd0803Request req) {
		if (isEmpty(req.organizationCodeLv2))
			throw new BadRequestException("本部が未設定です");
		if (isEmpty(req.organizationCodeLv3))
			throw new BadRequestException("部・室が未設定です");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が未設定です");
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が未設定です");

		// 予算計画履歴バージョンの抽出
		final Bd0803Response res = createResponse(Bd0803Response.class, req);
		res.results = repository.getBgtPlnHstver(req);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Bd0803Request req) {
		if (isEmpty(req.organizationCodeLv3))
			throw new BadRequestException("部・室が未設定です");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が未設定です");
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が未設定です");

		// 予算計画履歴バージョンの削除
		for (Bd0803Entity select : req.selects) {
			repository.delete(select);
		}

		// 結果表示
		final Bd0803Response res = createResponse(Bd0803Response.class, req);
		res.results = repository.getBgtPlnHstver(req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, "予算計画履歴"));
		res.success = true;
		return res;
	}

	public BaseResponse search(Bd0803Request req) {
		if (isEmpty(req.organizationCodeLv3))
			throw new BadRequestException("部・室が未設定です");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が未設定です");
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が未設定です");

		// 予算計画履歴バージョンの抽出
		final Bd0803Response res = createResponse(Bd0803Response.class, req);
		res.results = repository.getBgtPlnHstver(req);
		res.success = true;
		return res;
	}

}
