package jp.co.dmm.customize.endpoint.sp.sp0011;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.sp.PayeeBnkaccMstEntity;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 取引先登録画面サービス
 */
@ApplicationScoped
public class Sp0011Service extends BasePagingService {

	@Inject private Sp0011Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Sp0011GetResponse init(Sp0011GetRequest req) {

		// レスポンス作成

		// 対象の取引先マスタ取得
		Sp0011GetResponse res = get(req);

		// 選択肢設定（ログインユーザの会社コードで取得）
		String targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();

		// 法人・個人区分
		res.crpPrsTps = repository.getSelectItems(targetCompanyCd, "KbnSplrNominal", false);
		// 国内・海外区分
		res.dmsAbrTps = repository.getSelectItems(targetCompanyCd, "KbnGrobal", false);
		// 都道府県
		res.adrPrfCds = repository.getSelectItems(targetCompanyCd, "kbnPrefectures", true);
		// 取引状況区分
		res.trdStsTps = repository.getSelectItems(targetCompanyCd, "KbnSplrDealStMst", false);
		// 振込手数料負担区分
		res.payCmmOblTps = repository.getSelectItems(targetCompanyCd, "KbnSplrFeeCharge", false);
		// 休日処理区分
		res.hldTrtTps = repository.getSelectItems(targetCompanyCd, "KbnSplrHoliday", false);
		// 振込先口座種別
		res.bnkaccTps = repository.getSelectItems(targetCompanyCd, "KbnSplrAccountTp", false);
		// 関係会社区分
		res.affcmpTps = repository.getSelectItems(targetCompanyCd, "KbnAffcmp", false);
		// 振込手数料負担区分
		res.payCmmOblTps = repository.getSelectItems(targetCompanyCd, "KbnSplrFeeCharge", false);
		// 休日処理区分
		res.hldTrtTps = repository.getSelectItems(targetCompanyCd, "KbnSplrHoliday", false);
		return res;
	}

	/**
	 * 取引先情報取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Sp0011GetResponse get(Sp0011GetRequest req) {

		final Sp0011GetResponse res = createResponse(Sp0011GetResponse.class, req);
		res.success = true;

		// 対象の取引先マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Sp0011GetResponse getAccountList(Sp0011GetRequest req) {
		int allCount = repository.getAccountCount(req);
		final Sp0011GetResponse res = createResponse(Sp0011GetResponse.class, req, allCount);

		// 対象の取引先口座マスタ取得
		res.accList = new ArrayList<PayeeBnkaccMstEntity>();
		if ("00053".equals(LoginInfo.get().getCorporationCode())
			|| (!"00053".equals(LoginInfo.get().getCorporationCode()) && LoginInfo.get().getCorporationCode().equals(req.companyCd))){
			if (isNotEmpty(req.companyCd)) {
				res.accList = repository.getAccountList(req);
			}
		}

		res.success = true;
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Sp0011UpdateResponse update(Sp0011UpdateRequest req) {

		//更新（UPDATE+INSERT）
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Sp0011UpdateResponse res = createResponse(Sp0011UpdateResponse.class, req);
		res.success = true;
		return res;
	}
}
