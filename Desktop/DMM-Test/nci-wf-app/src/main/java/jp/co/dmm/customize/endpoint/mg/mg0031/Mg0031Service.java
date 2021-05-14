package jp.co.dmm.customize.endpoint.mg.mg0031;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 品目マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0031Service extends BasePagingService {

	@Inject private Mg0031Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0031GetResponse init(Mg0031GetRequest req) {

		// レスポンス作成
		// 対象の取引先マスタ取得
		Mg0031GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 資産区分選択肢
		res.asstTps = repository.getSelectItems(targetCompanyCd, "kbnAsst", false);

		// 経費区分選択肢
		res.cstTps = repository.getSelectItems(targetCompanyCd, "cstTp", false);


		// 削除フラグ選択肢
		res.dltFgNm = repository.getSelectItems(targetCompanyCd, "dltFg", false);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		// 課税対象区分
		res.taxSbjTps = repository.getSelectItemsFromLookup(targetCompanyCd, "TAX_SBJ_TP", false);

		return res;
	}

	/**
	 * 品目マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0031GetResponse get(Mg0031GetRequest req) {

		final Mg0031GetResponse res = createResponse(Mg0031GetResponse.class, req);
		res.success = true;

		// 対象の品目マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0031UpdateResponse update(Mg0031UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0031UpdateResponse res = createResponse(Mg0031UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0031UpdateResponse insert(Mg0031UpdateRequest req) {

		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0031UpdateResponse res = createResponse(Mg0031UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 品目マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0031GetRequest req) {
		return repository.isExist(req);
	}
}
