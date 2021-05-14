package jp.co.dmm.customize.endpoint.mg.mg0111;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 銀行支店マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0111Service extends BasePagingService {

	@Inject private Mg0111Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0111GetResponse init(Mg0111GetRequest req) {

		// レスポンス作成

		// 対象の銀行支店マスタ取得
		Mg0111GetResponse res = get(req);


		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 削除フラグ選択肢
		res.dltFg = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 銀行支店マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0111GetResponse get(Mg0111GetRequest req) {

		final Mg0111GetResponse res = createResponse(Mg0111GetResponse.class, req);
		res.success = true;

		// 対象の銀行支店マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 銀行支店更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0111UpdateResponse update(Mg0111UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0111UpdateResponse res = createResponse(Mg0111UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 銀行支店登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0111UpdateResponse insert(Mg0111UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0111UpdateResponse res = createResponse(Mg0111UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 銀行支店マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0111GetRequest req) {

		boolean checkFlg = false;

		// 対象の取引先マスタ取得
		final Mg0111GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}

}
