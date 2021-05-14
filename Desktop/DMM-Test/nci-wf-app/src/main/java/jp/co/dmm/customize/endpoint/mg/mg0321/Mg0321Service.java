package jp.co.dmm.customize.endpoint.mg.mg0321;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 消費税関連マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0321Service extends BasePagingService {

	@Inject private Mg0321Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0321GetResponse init(Mg0321GetRequest req) {

		// レスポンス作成
		Mg0321GetResponse res = get(req);

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

		// 消費税種類コード選択肢
		res.taxKndCdItems = repository.getSelectItemsFromLookup(targetCompanyCd, "TAX_KND_CD", false);

		// 消費税種類選択肢
		res.taxSpcItems = repository.getSelectItemsFromLookup(targetCompanyCd, "TAX_SPC", false);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		return res;
	}

	/**
	 * 消費税関連マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0321GetResponse get(Mg0321GetRequest req) {

		final Mg0321GetResponse res = createResponse(Mg0321GetResponse.class, req);
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
	public Mg0321UpdateResponse update(Mg0321UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0321UpdateResponse res = createResponse(Mg0321UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0321UpdateResponse insert(Mg0321UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0321UpdateResponse res = createResponse(Mg0321UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 消費税関連マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0321GetRequest req) {

		boolean checkFlg = false;

		// 対象のマスタ取得
		final Mg0321GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
