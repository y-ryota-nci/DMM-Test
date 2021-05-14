package jp.co.dmm.customize.endpoint.mg.mg0271;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 予算科目マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0271Service extends BasePagingService {

	@Inject private Mg0271Repository repository;
	@Inject private MwmLookupService lookup;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0271GetResponse init(Mg0271GetRequest req) {

		// レスポンス作成
		Mg0271GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// BS/PL選択肢
		res.bsPlTps = lookup.getOptionItems(LookupGroupId.BS_PL_TP, false);

		// 削除フラグ選択肢
		res.dltFg = repository.getSelectItems(targetCompanyCd, "dltFg", false);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		return res;
	}

	/**
	 * 予算科目マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0271GetResponse get(Mg0271GetRequest req) {

		final Mg0271GetResponse res = createResponse(Mg0271GetResponse.class, req);
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
	public Mg0271UpdateResponse update(Mg0271UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0271UpdateResponse res = createResponse(Mg0271UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0271UpdateResponse insert(Mg0271UpdateRequest req) {

		//更新
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0271UpdateResponse res = createResponse(Mg0271UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 予算科目マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0271GetRequest req) {

		boolean checkFlg = false;

		// 対象のマスタ取得
		final Mg0271GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
