package jp.co.nci.iwf.endpoint.vd.vd0111;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * パーツ条件定義一覧サービス.
 */
@BizLogic
public class Vd0111Service extends BaseService {

	@Inject private Vd0111Repository repository;
	@Inject private MwmLookupService lookup;

	/**
	 * 初期化.
	 * @param req
	 * @return
	 */
	public Vd0111Response init(Vd0111Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Vd0111Response res = createResponse(Vd0111Response.class, req);
		// 対象パーツが既にパーツ条件定義or画面パーツ条件定義の設定がないか確認
		final String errorMsg = this.validate(req.partsId, req.type, login.getLocaleCode());
		if (isNotEmpty(errorMsg)) {
			res.addAlerts(errorMsg);
			res.success = false;
		} else {
			res.templates = repository.getPartsCondDefList(login.getCorporationCode(), login.getLocaleCode());
			res.logicalOperators    = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);;
			res.parentheses         = lookup.getNameMap(LookupGroupId.PARENTHESIS);;
			res.comparisonOperators = lookup.getNameMap(LookupGroupId.COMPARISON_OPERATOR);;
			res.success = true;
		}
		return res;
	}

	/**
	 * 画面パーツ条件またはパーツ条件が設定済みか.
	 * 引数のtypeが"parts"となっていればパーツ条件定義の設定なので、画面パーツ条件定義が設定済みかどうかを調べる
	 * 上記以外は画面パーツ条件定義の設定なので、逆にパーツ条件定義が設定済みかを調べる
	 * 戻り値のlist(条件定義が設定されてある画面名称またはコンテナ名称)が取得できた場合、設定済みとしてエラーメッセージを生成して戻す
	 *
	 * @param partsId
	 * @param type
	 * @param localeCode
	 * @return エラーメッセージ
	 */
	private String validate(final Long partsId, final String type, String localeCode) {
		// このパーツに対する画面パーツ条件またはパーツ条件が既に設定済みか
		if (partsId != null) {
			if (eq("parts", type)) {
				final List<String> list = repository.isExistsScreenPartsCond(partsId, localeCode);
				if (list != null && list.size() > 0) {
					return i18n.getText(MessageCd.MSG0279, i18n.getText(MessageCd.screen), i18n.getText(MessageCd.condDef), String.join(",", list));
				}

			} else {
				final List<String> list = repository.isExistsPartsCond(partsId, localeCode);
				if (list != null && list.size() > 0) {
					return i18n.getText(MessageCd.MSG0279, i18n.getText(MessageCd.container), i18n.getText(MessageCd.condDef), String.join(",", list));
				}
			}
		}
		return null;
	}
}
