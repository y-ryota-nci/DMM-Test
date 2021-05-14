package jp.co.dmm.customize.endpoint.mg.mg0141;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 勘定科目補助マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0141Service extends BasePagingService {

	@Inject private Mg0141Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0141GetResponse init(Mg0141GetRequest req) {

		// レスポンス作成
		// 対象のマスタ取得
		Mg0141GetResponse res = get(req);

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
		return res;
	}

	/**
	 * 勘定科目補助マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0141GetResponse get(Mg0141GetRequest req) {

		final Mg0141GetResponse res = createResponse(Mg0141GetResponse.class, req);
		res.success = true;

		// 対象の勘定科目補助マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0141UpdateResponse update(Mg0141UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0141UpdateResponse res = createResponse(Mg0141UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0141UpdateResponse insert(Mg0141UpdateRequest req) {
		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0141UpdateResponse res = createResponse(Mg0141UpdateResponse.class, req);

		Mg0141Entity entity = new Mg0141Entity();
		entity.companyCd = req.companyCd;
		entity.accCd = req.accCd;
		entity.accBrkdwnCd = req.accBrkdwnCd;
		entity.sqno = (long) repository.getMaxSqno(req, false);

		res.entity = entity;
		res.success = true;
		return res;
	}

	/**
	 * 勘定科目補助マスタ存在チェック（登録用）
	 * @param req
	 * @return true:存在する / false:存在しない
	 */
	public boolean insertCheck(Mg0141UpdateRequest req) {
		return repository.countExistAcc(req) == 0 || repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 勘定科目補助マスタ存在チェック（更新用）
	 * @param req
	 * @return true:存在する / false:存在しない
	 */
	public boolean updateCheck(Mg0141UpdateRequest req) {
		return repository.countExistAcc(req) == 0 || repository.getMaxSqno(req, true) > 0;
	}
}
