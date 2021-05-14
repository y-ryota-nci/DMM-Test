package jp.co.nci.iwf.endpoint.dc.dc0111;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;

/**
 * 画面文書定義設定のサービス
 */
@BizLogic
public class Dc0111Service extends BaseService {
	/** 画面文書定義設定のリポジトリ */
	@Inject private Dc0111Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @return
	 */
	public Dc0111InitResponse init(Dc0111InitRequest req) {
		final Dc0111InitResponse res = createResponse(Dc0111InitResponse.class, req);
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 画面文書定義の抽出
		if (req.screenDocId == null) {
			res.entity = new MwvScreenDocDef();
			res.entity.corporationCode = corporationCode;
			res.entity.validStartDate = today();
			res.entity.validEndDate = ENDDATE;
		}
		else {
			res.entity = repository.get(req.screenDocId, localeCode);

			// 排他判定
			if (!eq(res.entity.version, req.version))
				throw new AlreadyUpdatedException();
		}

		// 画面の選択肢
		res.screenIds = createScreens(corporationCode, localeCode);
		// WF連携先の選択肢
		res.screenProcessCodes = createScreenProcessDefs(res.entity.screenId, res.entity.corporationCode, localeCode);

		res.success = true;
		return res;
	}

	/** 画面の選択肢を生成 */
	private List<OptionItem> createScreens(String corporationCode, String localeCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll( repository.getScreens(corporationCode, localeCode)
				.stream()
				.map(s -> new OptionItem(s.screenId, s.screenCode + " " + s.screenName))
				.collect(Collectors.toList()));
		return items;
	}

	/** WF連携先の選択肢を生成 */
	private List<OptionItem> createScreenProcessDefs(Long screenId, String corporationCode, String localeCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll( repository.getScreenProcessDefs(screenId, corporationCode, localeCode)
				.stream()
				.map(s -> new OptionItem(s.screenProcessCode, s.screenProcessCode + " " + s.screenProcessName))
				.collect(Collectors.toList()));
		return items;
	}

	/**
	 * 画面変更時処理.
	 * @param req
	 * @return
	 */
	public Dc0111ChangeResponse change(Dc0111ChangeRequest req) {
		final Dc0111ChangeResponse res = createResponse(Dc0111ChangeResponse.class, req);
		final Long screenId = req.screenId;
		final String corporationCode = req.corporationCode;
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		// WF連携先の選択肢
		res.screenProcessCodes = createScreenProcessDefs(screenId, corporationCode, localeCode);

		res.success = true;
		return res;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Dc0111SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final BaseResponse res = createResponse(BaseResponse.class, req);

		// エラー
		List<String> errors = validate(req);
		if (!errors.isEmpty()) {
			res.addAlerts(errors.toArray(new String[errors.size()]));
			res.success = false;
		}
		else {
			// 差分更新
			MwmScreenDocDef input = req.entity;
			MwmScreenDocDef org = repository.get(input.getScreenDocId());
			long screenDocId;
			if (org == null)
				screenDocId = repository.insert(input, corporationCode);
			else
				screenDocId = repository.update(input, org);

			multi.save("MWM_SCREEN_DOC_DEF", screenDocId, "SCREEN_DOC_NAME", input.getScreenDocName());
			multi.save("MWM_SCREEN_DOC_DEF", screenDocId, "DESCRIPTION", input.getDescription());

			res.success = true;
		}

		return res;
	}

	/** バリデーション */
	private List<String> validate(Dc0111SaveRequest req) {
		final List<String> errors = new ArrayList<>();

		// 画面が設定されていること
		final Long screenId = req.entity.getScreenId();
		if (screenId == null)
			errors.add(i18n.getText(MessageCd.MSG0001, MessageCd.screen));

		// 画面が存在すること
		if (!repository.existScreen(screenId))
			errors.add(i18n.getText(MessageCd.MSG0129, MessageCd.screen));

		// 会社コード＋画面文書コードの組み合わせが存在しないこと
		if (repository.existScreenDocDef(req.entity.getScreenDocId(), req.entity.getCorporationCode(), req.entity.getScreenDocCode()))
			errors.add(i18n.getText(MessageCd.MSG0130, MessageCd.screenDocCode));

		return errors;
	}

}
