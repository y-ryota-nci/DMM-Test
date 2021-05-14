package jp.co.nci.iwf.endpoint.ml.ml0040;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.mail.MailVariableService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;

/**
 * メール変数設定サービス
 */
@BizLogic
public class Ml0040Service extends BaseService {
	@Inject private MailVariableService variableService;
	@Inject private MultilingalService multi;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ml0040Response init(BaseRequest req) {
		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		Ml0040Response res = createResponse(Ml0040Response.class, req);
		res.results = variableService.getMwmMailVariables(corporationCode, localeCode);
		res.success = true;
		return res;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ml0040Response save(Ml0040Request req) {
		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 現在値を抽出
		Map<String, MwmMailVariable> currents = variableService
				.getMwmMailVariables(corporationCode, localeCode)
				.stream()
				.collect(Collectors.toMap(v -> v.getMailVariableCode(), v -> v));

		// 入力値を反映
		for (MwmMailVariable input : req.inputs) {
			MwmMailVariable current = currents.get(input.getMailVariableCode());
			if (current == null)
				throw new NotFoundException("対象レコードが見つかりません MAIL_VARIABLE_CODE=" + input.getMailVariableCode());

			current.setMailVariableLabel(input.getMailVariableLabel());
			current.setMailVariableValue(input.getMailVariableValue());

			// 多言語
			multi.save("MWM_MAIL_VARIABLE", current.getMailVariableId(), "MAIL_VARIABLE_LABEL", current.getMailVariableLabel());
			multi.save("MWM_MAIL_VARIABLE", current.getMailVariableId(), "MAIL_VARIABLE_VALUE", current.getMailVariableValue());
		}

		// 読み直し
		Ml0040Response res = createResponse(Ml0040Response.class, req);
		res.results = variableService.getMwmMailVariables(corporationCode, localeCode);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.mailVariable));
		res.success = true;
		return res;
	}
}
