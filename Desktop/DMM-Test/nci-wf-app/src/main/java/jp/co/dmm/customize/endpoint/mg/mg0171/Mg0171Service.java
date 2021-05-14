package jp.co.dmm.customize.endpoint.mg.mg0171;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 通貨マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0171Service extends BasePagingService {

	@Inject private Mg0171Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0171GetResponse init(Mg0171GetRequest req) {

		// レスポンス作成
		Mg0171GetResponse res = get(req);

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
	 * 通貨マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0171GetResponse get(Mg0171GetRequest req) {

		final Mg0171GetResponse res = createResponse(Mg0171GetResponse.class, req);
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
	public Mg0171UpdateResponse update(Mg0171UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0171UpdateResponse res = createResponse(Mg0171UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0171UpdateResponse insert(Mg0171UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0171UpdateResponse res = createResponse(Mg0171UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 通貨マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0171GetRequest req) {

		boolean checkFlg = false;

		// 対象の銀行マスタ取得
		final Mg0171GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
