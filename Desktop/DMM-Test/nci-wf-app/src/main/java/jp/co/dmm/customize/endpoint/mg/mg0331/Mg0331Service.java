package jp.co.dmm.customize.endpoint.mg.mg0331;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 国マスタ編集画面サービス
 */
@ApplicationScoped
public class Mg0331Service extends BasePagingService {

	@Inject private Mg0331Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0331Response init(Mg0331Request req) {

		final Mg0331Response res = createResponse(Mg0331Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		if (isEmpty(req.lndCd)) {
			res.entity = new Mg0331Entity();
			res.entity.dltFg = DeleteFlag.OFF;
		} else {
			res.entity = repository.get(req, localeCode);
		}
		if (isEmpty(res.entity)) {
			throw new BadRequestException("国情報が存在しません。");
		}

		// 削除フラグ選択肢
		res.dltFgItems = repository.getSelectItems(false, localeCode);

		res.success = true;
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0331SaveResponse update(Mg0331SaveRequest req) {
		// レスポンス作成
		final Mg0331SaveResponse res = createResponse(Mg0331SaveResponse.class, req);
		//更新
		repository.update(req, sessionHolder.getWfUserRole());
		res.success = true;
		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0331SaveResponse insert(Mg0331SaveRequest req) {
		// レスポンス作成
		final Mg0331SaveResponse res = createResponse(Mg0331SaveResponse.class, req);
		//更新
		repository.insert(req, sessionHolder.getWfUserRole());
		res.success = true;
		return res;
	}

	/**
	 * 国マスタタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0331Request req) {
		// 対象の銀行マスタ取得
		boolean checkFlg = false;

		final Mg0331Response res = createResponse(Mg0331Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.entity = repository.get(req, localeCode);

		// 対象が存在した場合は登録不可
		if(isNotEmpty(res.entity) && isNotEmpty(res.entity.lndCd)) {
			checkFlg = true;
		}
		return checkFlg;
	}

}
