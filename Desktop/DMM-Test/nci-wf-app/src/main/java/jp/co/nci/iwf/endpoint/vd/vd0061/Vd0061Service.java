package jp.co.nci.iwf.endpoint.vd.vd0061;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;

/**
 * 選択肢マスタ登録サービス.
 */
@BizLogic
public class Vd0061Service extends BaseService {

	@Inject private Vd0061Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0061InitResponse init(BaseRequest req) {
		final Vd0061InitResponse res = createResponse(Vd0061InitResponse.class, req);
		final MwmOption entity = new MwmOption();

		res.entity = entity;
		res.success = true;
		return res;
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Vd0061InsertResponse regist(Vd0061InsertRequest req) {
		final Vd0061InsertResponse res = createResponse(Vd0061InsertResponse.class, req);
		final MwmOption e = req.entity;
		e.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());

		// バリデーション
		final List<String> errors = validate(e);
		if (isEmpty(errors)) {
			// 選択肢マスタの登録
			long optionId = repository.insert(e);
			// 多言語対応
			multi.save("MWM_OPTION", optionId, "OPTION_NAME", e.getOptionName());

			// 読み直し
			res.entity = repository.get(optionId);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.optionSetting));
			res.success = true;
		}
		else {
			res.alerts = errors;
			res.success = false;
		}

		return res;
	}

	/** バリデーション */
	private List<String> validate(final MwmOption e) {
		List<String> errors = new ArrayList<>();

		// パーツ選択肢コードは必須
		if (isEmpty(e.getOptionCode())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.optionCode) );
		}
		// パーツ選択肢コードが重複しているか
		else if (repository.isDuplicate(e.getOptionCode(), e.getCorporationCode())) {
			errors.add( i18n.getText(MessageCd.MSG0130, MessageCd.optionCode) );
		}
		// パーツ選択肢名は必須
		if (isEmpty(e.getOptionName())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.optionName) );
		}
		// 企業コードは必須
		if (isEmpty(e.getCorporationCode())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.corporationCode) );
		}

		return errors;
	}
}
