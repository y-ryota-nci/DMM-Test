package jp.co.dmm.customize.endpoint.mg.mg0241;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 支払サイトマスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0241Service extends BasePagingService {

	@Inject private Mg0241Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0241GetResponse init(Mg0241GetRequest req) {

		// レスポンス作成
		Mg0241GetResponse res = get(req);

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

		// 支払サイト（月）の選択
		res.paySiteMOpts = repository.getSelectItems(targetCompanyCd, "paySiteMOpts", true);

		// 支払サイト（日）の選択
		res.paySiteNOpts = repository.getSelectItems(targetCompanyCd, "paySiteNOpts", true);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		return res;
	}

	/**
	 * 支払サイトマスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0241GetResponse get(Mg0241GetRequest req) {

		final Mg0241GetResponse res = createResponse(Mg0241GetResponse.class, req);
		res.success = true;

		// 対象のマスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0241UpdateResponse update(Mg0241UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0241UpdateResponse res = createResponse(Mg0241UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0241UpdateResponse insert(Mg0241UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0241UpdateResponse res = createResponse(Mg0241UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 支払サイトマスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0241GetRequest req) {

		boolean checkFlg = false;

		// 対象の銀行マスタ取得
		final Mg0241GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
