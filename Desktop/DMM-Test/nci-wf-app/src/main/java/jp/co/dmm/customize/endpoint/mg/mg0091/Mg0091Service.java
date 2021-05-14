package jp.co.dmm.customize.endpoint.mg.mg0091;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 銀行口座マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0091Service extends BasePagingService {

	@Inject private Mg0091Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0091GetResponse init(Mg0091GetRequest req) {

		// レスポンス作成
		// 対象の取引先マスタ取得
		Mg0091GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 銀行口座種別選択肢
		res.bnkaccTpNm = repository.getSelectItems(targetCompanyCd, "KbnBnkaccTp", false);
		// 削除フラグ選択肢
		res.dltFgNm = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 銀行口座マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0091GetResponse get(Mg0091GetRequest req) {

		final Mg0091GetResponse res = createResponse(Mg0091GetResponse.class, req);
		res.success = true;

		// 対象の銀行口座マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 登録チェック
	 * @param req
	 * @return
	 */
	public boolean insertCheck(Mg0091UpdateRequest req) {
		return repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 更新チェック
	 * @param req
	 * @return
	 */
	public boolean updateCheck(Mg0091UpdateRequest req) {
		return repository.getMaxSqno(req, true) > 0;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0091UpdateResponse update(Mg0091UpdateRequest req) {

		//更新(論理削除)
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0091UpdateResponse res = createResponse(Mg0091UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0091UpdateResponse insert(Mg0091UpdateRequest req) {

		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0091UpdateResponse res = createResponse(Mg0091UpdateResponse.class, req);

		Mg0091Entity entity = new Mg0091Entity();
		entity.companyCd = req.companyCd;
		entity.bnkaccCd = req.bnkaccCd;
		entity.sqno = (long) repository.getMaxSqno(req, false);

		res.entity = entity;
		res.success = true;
		return res;
	}
}
