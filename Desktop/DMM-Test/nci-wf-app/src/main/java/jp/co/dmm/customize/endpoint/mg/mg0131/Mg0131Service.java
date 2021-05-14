package jp.co.dmm.customize.endpoint.mg.mg0131;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 勘定科目マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0131Service extends BasePagingService {

	@Inject private Mg0131Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0131GetResponse init(Mg0131GetRequest req) {

		// レスポンス作成
		// 対象の勘定科目マスタ取得
		Mg0131GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 貸借区分選択肢
		res.dcTpNm = repository.getSelectItems(targetCompanyCd, "kbnDcTp", false);
		// 勘定科目補助区分選択肢
		res.accBrkdwnTpNm = repository.getSelectItems(targetCompanyCd, "kbnAccBrkdwnTp", false);
		// 税入力区分選択肢
		res.taxIptTpNm = repository.getSelectItems(targetCompanyCd, "kbnTaxIptTp", false);
		// 削除フラグ選択肢
		res.dltFgNm = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		return res;
	}

	/**
	 * 勘定科目マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0131GetResponse get(Mg0131GetRequest req) {

		final Mg0131GetResponse res = createResponse(Mg0131GetResponse.class, req);
		res.success = true;

		// 対象の勘定科目マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0131UpdateResponse update(Mg0131UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0131UpdateResponse res = createResponse(Mg0131UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0131UpdateResponse insert(Mg0131UpdateRequest req) {
		//登録
		repository.insert(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0131UpdateResponse res = createResponse(Mg0131UpdateResponse.class, req);

		// 採番された連番を取得
		Mg0131Entity entity = new Mg0131Entity();
		entity.companyCd = req.companyCd;
		entity.accCd = req.accCd;
		entity.sqno = (long) repository.getMaxSqno(req, false);

		res.entity = entity;
		res.success = true;
		return res;
	}

	/**
	 * 勘定科目マスタ存在チェック（登録用）
	 * @param req
	 * @return
	 */
	public boolean insertCheck(Mg0131UpdateRequest req) {
		return repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 勘定科目マスタ存在チェック（更新用）
	 * @param req
	 * @return
	 */
	public boolean updateCheck(Mg0131UpdateRequest req) {
		return repository.getMaxSqno(req, true) > 0;
	}
}
