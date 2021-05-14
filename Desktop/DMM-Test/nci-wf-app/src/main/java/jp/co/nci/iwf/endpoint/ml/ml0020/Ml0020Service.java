package jp.co.nci.iwf.endpoint.ml.ml0020;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;

/**
 * メール環境設定サービス
 */
@BizLogic
public class Ml0020Service extends BaseService {

	@Inject private Ml0020Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ml0020InitResponse init(BaseRequest req) {
		final Ml0020InitResponse res = createResponse(Ml0020InitResponse.class, req);
		res.configs = repository.getConfigs().stream()
				.map(c -> new Ml0020Entity(c))
				.collect(Collectors.toList());
		res.success = true;
		return res;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ml0020SaveResponse save(Ml0020SaveRequest req) {
		Map<Long, MwmMailConfig> currents = repository.getConfigs()
			.stream()
			.collect(Collectors.toMap(c -> c.getMailConfigId(), c -> c));

		for (Ml0020Entity input :  req.inputs) {
			final MwmMailConfig current = currents.remove(input.mailConfigId);
			if (current != null) {
				repository.update(current, input);
			}
		}

		final Ml0020SaveResponse res = createResponse(Ml0020SaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.mailConfig));
		res.success = true;
		return res;
	}

}
