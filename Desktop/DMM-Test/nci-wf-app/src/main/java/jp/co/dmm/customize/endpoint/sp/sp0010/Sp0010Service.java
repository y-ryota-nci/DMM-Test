package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.sp.ZipMstEntity;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * 取引先一覧サービス
 */
@ApplicationScoped
public class Sp0010Service extends BasePagingService {

	@Inject private Sp0010Repository repository;

	@Inject
	protected MwmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Sp0010SearchResponse init(Sp0010SearchRequest req) {
		final Sp0010SearchResponse res = createResponse(Sp0010SearchResponse.class, req);
		res.success = true;
		res.adrPrfCds = repository.getSelectItems(sessionHolder.getWfUserRole().getCorporationCode(), "kbnPrefectures");
		res.hdFlag = "00053".equals(sessionHolder.getWfUserRole().getCorporationCode()) ? "1" : "0";

		LoginInfo login = sessionHolder.getLoginInfo();
		res.companyCd = login.getCorporationCode();
		res.companyNm = login.getCorporationName();
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Sp0010SearchResponse search(Sp0010SearchRequest req) {
		final int allCount = repository.count(req);
		final Sp0010SearchResponse res = createResponse(Sp0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 取引先口座情報チェック
	 * @param req
	 * @return
	 */
	public Sp0010AccountCheckResponse accountCheck(Sp0010AccountCheckRequest req) {
		final Sp0010AccountCheckResponse res = createResponse(Sp0010AccountCheckResponse.class, req);

		if (!repository.accountCheck(req)) {
			res.checkResults = "1";
		}
		return res;
	}

	/**
	 * 住所取得
	 * @param companyCd 会社コード
	 * @param adrPrfCd 郵便番号
	 * @return
	 */
	public Sp0010GetAddressResponse getAddressInfo(Sp0010GetAddressRequest req) {
		final Sp0010GetAddressResponse res = createResponse(Sp0010GetAddressResponse.class, req);

		List<ZipMstEntity> results = repository.getAddressInfo(req.companyCd, req.zipCd);
		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * 会社情報取得
	 * @param req
	 * @return
	 */
	public Sp0010GetcompanyResponse getCompany(Sp0010GetcompanyRequest req) {
		final Sp0010GetcompanyResponse res = createResponse(Sp0010GetcompanyResponse.class, req);

		LoginInfo loginInfo = sessionHolder.getLoginInfo();
		List<String> companyList = repository.getCompany(loginInfo.getCorporationCode(), loginInfo.getLocaleCode());

		res.companyList = companyList;

		if (isNotEmpty(req.splrCd)) {
			res.companyCds = repository.getDGHDCompanys(req.splrCd);
		}

		res.success = true;

		return res;
	}


	/**
	 * 変更申請バリデーション
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public Sp0010GetScreenProcessIdResponse validate(Sp0010GetScreenProcessIdRequest req) {
		final Sp0010GetScreenProcessIdResponse res = createResponse(Sp0010GetScreenProcessIdResponse.class, req);

		int countSplr = repository.countSplr(req);
		if (countSplr > 0) {
			res.success = false;
			throw new InvalidUserInputException("会社コード[" + req.companyCd + "]、取引先コード[" + req.splrCd + "]はまだ変更申請中です。");
		}

		res.screenProcessId = repository.getScreenProcessId(req.companyCd, sessionHolder.getLoginInfo().getCorporationCode());
		res.companyCd = req.companyCd;
		res.success = true;
		return res;
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public Sp0010GetScreenProcessIdResponse getScreenProcessId(Sp0010GetScreenProcessIdRequest req) {
		final Sp0010GetScreenProcessIdResponse res = createResponse(Sp0010GetScreenProcessIdResponse.class, req);
		res.screenProcessId = repository.getScreenProcessId(req.companyCd, sessionHolder.getLoginInfo().getCorporationCode());
		res.companyCd = req.companyCd;
		res.success = true;
		return res;
	}


	/**
	 * 取引先コード＆名寄せチェック
	 * @param req
	 * @return
	 */
	public Sp0010ResearchCheckResponse researchCheck(Sp0010ResearchCheckRequest req) {
		final Sp0010ResearchCheckResponse res = createResponse(Sp0010ResearchCheckResponse.class, req);
		res.checkResults = "0";
		boolean errorFlag = false;

		// すでに取引先コードがある場合
		if (isNotEmpty(req.splrCd)) {
			String existsCompanyCds = repository.researchSplrCdCheck(req);

			if (isNotEmpty(existsCompanyCds)) {
				res.existsCompanyCds = existsCompanyCds;
				res.checkResults = "1";
				errorFlag = true;
			}
		}

		// 2019/04/02 名寄せチェック除外
//		if (!errorFlag && isEmpty(req.splrCd)) {
//			res.results = repository.researchCheck(req, res);
//			res.checkResults = res.results.size() != 0 ? "2" : "0";
//		}

		res.success = true;
		return res;
	}

	public Sp0010GetDefaultBnkaccResponse getDefaultBnkacc(Sp0010GetDefaultBnkaccRequest req) {
		final Sp0010GetDefaultBnkaccResponse res = createResponse(Sp0010GetDefaultBnkaccResponse.class, req);
		MwmLookup lookup = lookupService.get(LookupGroupId.DEFAULT_BNKACC, req.companyCd);
		String bnkaccNm = lookup != null ? repository.getDefaultBnkacc(req.companyCd, lookup.getLookupName()) : null;

		if (!isEmpty(bnkaccNm)) {
			res.companyCd = req.companyCd;
			res.bnkaccCd = lookup.getLookupName();
			res.bnkaccNm = bnkaccNm;
			res.success = true;
		} else {
			res.success = false;
		}

		return res;
	}

}
