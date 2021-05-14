package jp.co.dmm.customize.endpoint.mg.mg0191;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 部門マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0191Service extends BasePagingService {

	@Inject private Mg0191Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0191GetResponse init(Mg0191GetRequest req) {

		// レスポンス作成
		// 対象の取引先マスタ取得
		Mg0191GetResponse res = get(req);

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

		// サイトコード選択肢
		res.siteCdItems = repository.getSelectItems(targetCompanyCd, "DpmtSite", true);

		// 分類コード選択肢
		res.tpCdItems = repository.getSelectItems(targetCompanyCd, "DpmtType", true);

		// 消費税種類コード選択肢
		res.taxKndCdItems = repository.getSelectItemsFromLookup(targetCompanyCd, "TAX_KND_CD", false);

		return res;
	}

	/**
	 * 部門マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0191GetResponse get(Mg0191GetRequest req) {

		final Mg0191GetResponse res = createResponse(Mg0191GetResponse.class, req);
		res.success = true;

		// 対象の部門マスタ取得
		res.entity =  repository.get(req, sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0191UpdateResponse update(Mg0191UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0191UpdateResponse res = createResponse(Mg0191UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0191UpdateResponse insert(Mg0191UpdateRequest req) {

		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0191UpdateResponse res = createResponse(Mg0191UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 部門マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0191GetRequest req) {

		boolean checkFlg = false;

		// 対象の部門マスタ取得
		final Mg0191GetResponse res = get(req);

		// 対象が存在した場合は登録不可
		if(res.entity.companyCd != null) {
			checkFlg = true;
		}
		return checkFlg;
	}
}
