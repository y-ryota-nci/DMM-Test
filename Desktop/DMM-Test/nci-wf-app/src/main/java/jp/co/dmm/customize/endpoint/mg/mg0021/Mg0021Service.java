package jp.co.dmm.customize.endpoint.mg.mg0021;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 費目マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0021Service extends BasePagingService {

	@Inject private Mg0021Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0021GetResponse init(Mg0021GetRequest req) {

		// レスポンス作成
		// 対象の取引先マスタ取得
		Mg0021GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 削除フラグ選択肢
		res.dltFgNm = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 費目マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0021GetResponse get(Mg0021GetRequest req) {

		final Mg0021GetResponse res = createResponse(Mg0021GetResponse.class, req);
		res.success = true;

		// 対象の費目マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0021UpdateResponse update(Mg0021UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0021UpdateResponse res = createResponse(Mg0021UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0021UpdateResponse insert(Mg0021UpdateRequest req) {

		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0021UpdateResponse res = createResponse(Mg0021UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 費目マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0021GetRequest req) {

		boolean checkFlg = false;

		// 対象の費目マスタ取得
		final Mg0021GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
