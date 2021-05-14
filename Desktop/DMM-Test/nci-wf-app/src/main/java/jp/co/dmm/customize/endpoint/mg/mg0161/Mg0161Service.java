package jp.co.dmm.customize.endpoint.mg.mg0161;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 消費税マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0161Service extends BasePagingService {

	@Inject private Mg0161Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0161GetResponse init(Mg0161GetRequest req) {

		// レスポンス作成
		Mg0161GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 税処理区分選択肢
		res.taxTps = repository.getSelectItems(targetCompanyCd, "TAX", false);

		// 端数処理区分選択肢
		res.frcTps = repository.getSelectItems(targetCompanyCd, "FRC_TP", false);

		// 正残区分選択肢
		res.dcTps = repository.getSelectItems(targetCompanyCd, "DC_TP", false);

		// 削除フラグ選択肢
		res.dltFg = repository.getSelectItems(targetCompanyCd, "dltFg", false);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 消費税マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0161GetResponse get(Mg0161GetRequest req) {

		final Mg0161GetResponse res = createResponse(Mg0161GetResponse.class, req);
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
	public Mg0161UpdateResponse update(Mg0161UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0161UpdateResponse res = createResponse(Mg0161UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0161UpdateResponse insert(Mg0161UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0161UpdateResponse res = createResponse(Mg0161UpdateResponse.class, req);

		Mg0161Entity entity = new Mg0161Entity();
		entity.companyCd = req.companyCd;
		entity.taxCd = req.taxCd;
		entity.sqno = (long) repository.getMaxSqno(req, false);

		res.entity = entity;
		res.success = true;
		return res;
	}

	/**
	 * 消費税マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0161UpdateRequest req) {

		return repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 消費税マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean updateCheck(Mg0161UpdateRequest req) {

		return repository.getMaxSqno(req, true) > 0;
	}
}
