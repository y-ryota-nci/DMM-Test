package jp.co.dmm.customize.endpoint.mg.mg0261;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * ｸﾚｶ口座マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0261Service extends BasePagingService {

	@Inject private Mg0261Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0261GetResponse init(Mg0261GetRequest req) {

		// レスポンス作成
		Mg0261GetResponse res = get(req);

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

		res.bnkaccChrgDts = repository.getSelectItems(targetCompanyCd, "DAYS_IN_MONTH", false);

		return res;
	}

	/**
	 * ｸﾚｶ口座マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0261GetResponse get(Mg0261GetRequest req) {

		final Mg0261GetResponse res = createResponse(Mg0261GetResponse.class, req);
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
	public Mg0261UpdateResponse update(Mg0261UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0261UpdateResponse res = createResponse(Mg0261UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0261UpdateResponse insert(Mg0261UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0261UpdateResponse res = createResponse(Mg0261UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * ｸﾚｶ口座マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0261GetRequest req) {

		boolean checkFlg = false;

		// 対象の銀行マスタ取得
		final Mg0261GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
