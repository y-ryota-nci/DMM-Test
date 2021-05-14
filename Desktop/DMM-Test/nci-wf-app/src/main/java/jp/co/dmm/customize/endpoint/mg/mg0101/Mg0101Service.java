package jp.co.dmm.customize.endpoint.mg.mg0101;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 銀行マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0101Service extends BasePagingService {

	@Inject private Mg0101Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0101GetResponse init(Mg0101GetRequest req) {

		// レスポンス作成

		// 対象の銀行マスタ取得
		Mg0101GetResponse res = get(req);


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
	 * 銀行マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0101GetResponse get(Mg0101GetRequest req) {

		final Mg0101GetResponse res = createResponse(Mg0101GetResponse.class, req);
		res.success = true;

		// 対象の銀行マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0101UpdateResponse update(Mg0101UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0101UpdateResponse res = createResponse(Mg0101UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0101UpdateResponse insert(Mg0101UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0101UpdateResponse res = createResponse(Mg0101UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 銀行マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0101GetRequest req) {

		boolean checkFlg = false;

		// 対象の銀行マスタ取得
		final Mg0101GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}

}
