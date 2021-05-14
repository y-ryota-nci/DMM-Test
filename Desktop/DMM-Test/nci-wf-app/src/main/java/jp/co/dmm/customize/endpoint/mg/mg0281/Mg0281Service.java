package jp.co.dmm.customize.endpoint.mg.mg0281;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * メディアマスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0281Service extends BasePagingService {

	@Inject private Mg0281Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0281GetResponse init(Mg0281GetRequest req) {

		// レスポンス作成
		Mg0281GetResponse res = get(req);

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
	 * メディアマスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0281GetResponse get(Mg0281GetRequest req) {

		final Mg0281GetResponse res = createResponse(Mg0281GetResponse.class, req);
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
	public Mg0281UpdateResponse update(Mg0281UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0281UpdateResponse res = createResponse(Mg0281UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0281UpdateResponse insert(Mg0281UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0281UpdateResponse res = createResponse(Mg0281UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * メディアマスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0281GetRequest req) {

		boolean checkFlg = false;

		// 対象のマスタ取得
		final Mg0281GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
