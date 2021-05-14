package jp.co.dmm.customize.endpoint.mg.mg0311;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 住所マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0311Service extends BasePagingService {

	@Inject private Mg0311Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0311GetResponse init(Mg0311GetRequest req) {

		// レスポンス作成
		Mg0311GetResponse res = get(req);

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
	 * 住所マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0311GetResponse get(Mg0311GetRequest req) {

		final Mg0311GetResponse res = createResponse(Mg0311GetResponse.class, req);
		res.success = true;

		// 対象のマスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	public Mg0311UpdateResponse insert(Mg0311UpdateRequest req) {
		// 新規登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0311UpdateResponse res = createResponse(Mg0311UpdateResponse.class, req);
		res.success = true;
		res.sqno = repository.getMaxSqnoInsert(req);
		return res;
	}

	public Mg0311UpdateResponse update(Mg0311UpdateRequest req) {
		// 更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0311UpdateResponse res = createResponse(Mg0311UpdateResponse.class, req);
		res.success = true;
		return res;
	}

	public boolean insertCheck(Mg0311UpdateRequest req) {
		return repository.getMaxSqnoInsert(req) > 0;
	}

	public boolean updateCheck(Mg0311UpdateRequest req) {
		return repository.getMaxSqnoUpdate(req) > 0;
	}

}
