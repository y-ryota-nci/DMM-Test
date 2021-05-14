package jp.co.dmm.customize.endpoint.mg.mg0251;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 部門関連マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0251Service extends BasePagingService {

	@Inject private Mg0251Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0251GetResponse init(Mg0251GetRequest req) {

		// レスポンス作成
		Mg0251GetResponse res = get(req);

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
	 * 部門関連マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0251GetResponse get(Mg0251GetRequest req) {

		final Mg0251GetResponse res = createResponse(Mg0251GetResponse.class, req);
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
	public Mg0251UpdateResponse update(Mg0251UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0251UpdateResponse res = createResponse(Mg0251UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0251UpdateResponse insert(Mg0251UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0251UpdateResponse res = createResponse(Mg0251UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 部門関連マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0251GetRequest req) {

		boolean checkFlg = false;

		// 対象の銀行マスタ取得
		final Mg0251GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
