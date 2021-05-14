package jp.co.dmm.customize.endpoint.md.md0011;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 取引先登録画面サービス
 */
@ApplicationScoped
public class Md0011Service extends BasePagingService {

	@Inject private Md0011Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Md0011GetResponse init(Md0011GetRequest req) {

		// レスポンス作成

		// 対象の取引先マスタ取得
		Md0011GetResponse res = get(req);

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
	public Md0011GetResponse get(Md0011GetRequest req) {

		final Md0011GetResponse res = createResponse(Md0011GetResponse.class, req);
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
	public Md0011GetResponse getAccountList(Md0011GetRequest req) {
		int allCount = repository.getAccountCount(req);
		final Md0011GetResponse res = createResponse(Md0011GetResponse.class, req, allCount);

		// 対象の取引先口座マスタ取得
		res.accList = repository.getAccountList(req);
		res.success = true;
		return res;
	}
}
