package jp.co.nci.iwf.endpoint.vd.vd0062;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 選択肢マスタ登録サービス.
 */
@BizLogic
public class Vd0062Service extends BaseService {

	@Inject private Vd0062Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0062Response init(Vd0062InitRequest req) {
		if (req.optionId != null && req.version == null)
			throw new BadRequestException("versionが未指定です");

		// パーツ選択肢マスタの取得
		MwmOption entity = null;
		if (req.optionId != null) {
			entity = repository.get(req.optionId);
		}
		// 排他判定
		if (entity == null || !eq(entity.getVersion(), req.version)) {
			throw new AlreadyUpdatedException("OPTION_ID=" + req.optionId + " VERSION=" + req.version);
		}
		// 問題なければ選択肢項目マスタ一覧を取得
		List<MwmOptionItem> items = repository.getMwmOptionItems(req.optionId, sessionHolder.getLoginInfo().getLocaleCode());

		final Vd0062Response res = createResponse(Vd0062Response.class, req);
		res.entity = entity;
		res.items  = items;
		res.success = true;
		return res;
	}

	/**
	 * パーツ選択肢マスタの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Vd0062Response update(Vd0062SaveRequest req) {
		final Vd0062Response res = createResponse(Vd0062Response.class, req);

		// 入力内容
		MwmOption inputed = req.entity;
		// DBからパーツ選択肢マスタを再取得
		MwmOption org = repository.get(inputed.getOptionId());

		// バリデーション
		final List<String> errors = validate(inputed, org);
		if (isEmpty(errors)) {
			// 選択肢マスタの更新
			repository.update(inputed, org);
			// 多言語対応
			multi.save("MWM_OPTION", inputed.getOptionId(), "OPTION_NAME", inputed.getOptionName());

			// 読み直し
			res.entity = repository.get(inputed.getOptionId());
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.optionSetting));
			res.success = true;
		}
		else {
			res.alerts = errors;
			res.success = false;
		}

		return res;
	}

	/**
	 * パーツ選択肢項目マスタ一覧の登録.
	 * @param req
	 * @return
	 */
	@Transactional
	public Vd0062Response regist(Vd0062SaveRequest req) {
		final Vd0062Response res = createResponse(Vd0062Response.class, req);

		// パーツ選択肢マスタ情報
		final MwmOption option = req.entity;
		// DBからパーツ選択肢マスタを再取得
		final MwmOption entity = repository.get(option.getOptionId());
		// 排他判定
		if (entity == null || !eq(option.getVersion(), entity.getVersion())) {
			throw new AlreadyUpdatedException("OPTION_ID=" + option.getOptionId() + " VERSION=" + option.getVersion());
		}

		// バリデーション
		final List<String> errors = validate(req.items);
		if (isEmpty(errors)) {
			final String localcCode = sessionHolder.getLoginInfo().getLocaleCode();
			final Map<Long, MwmOptionItem> map = repository.getMwmOptionItemMap(option.getOptionId(), localcCode);

			for (MwmOptionItem item : req.items) {
				Long optionItemId = item.getOptionItemId();
				final MwmOptionItem org = map.remove(optionItemId);
				// 選択肢項目マスタの登録／更新

				if (org == null) {
					optionItemId = repository.insert(item, option.getOptionId());
				} else {
					repository.update(item, org);
				}
				// 多言語対応
				multi.save("MWM_OPTION_ITEM", optionItemId, "LABEL", item.getLabel());
			}

			// mapに残ったものは削除
			final Set<Long> deleteOptionItemIds = map.keySet();

			// 削除対象パーツで有効条件の比較対象パーツとして設定されてあるパーツ有効条件
			// および不要となったパーツ有効条件を一括削除
			repository.delete(deleteOptionItemIds);
			// 多言語対応している「ラベル」を一つずつ削除
			for (Long optionItemId : deleteOptionItemIds) {
				multi.physicalDelete("MWM_OPTION_ITEM", optionItemId, "LABEL");
			}

			// パーツ選択肢項目マスタの読み直し
			res.items = repository.getMwmOptionItems(option.getOptionId(), localcCode);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.optionItem));
			res.success = true;
		}
		else {
			res.alerts = errors;
			res.success = false;
		}

		return res;
	}

	/** バリデーション */
	private List<String> validate(MwmOption inputed, MwmOption org) {
		List<String> errors = new ArrayList<>();

		// パーツ選択肢コードは必須
		if (isEmpty(inputed.getOptionCode())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.optionCode) );
		}
		// パーツ選択肢名は必須
		if (isEmpty(inputed.getOptionName())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.optionName) );
		}
		// 企業コードは必須
		if (isEmpty(inputed.getCorporationCode())) {
			errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.corporationCode) );
		}
		// 排他判定
		if (org == null || !eq(inputed.getVersion(), org.getVersion())) {
			errors.add( i18n.getText(MessageCd.MSG0050) );
		}

		return errors;
	}

	/** バリデーション */
	private List<String> validate(List<MwmOptionItem> items) {
		List<String> errors = new ArrayList<>();
		// コードの重複チェック用
		Set<String> duplicate = new HashSet<>();

		for (MwmOptionItem item : items) {
			// コードは必須
//			if (isEmpty(item.getCode())) {
//				errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.universalCode) );
//			}
			// ラベルは必須
			if (isEmpty(item.getLabel())) {
				errors.add( i18n.getText(MessageCd.MSG0001, MessageCd.label) );
			}
			// コードの重複は許さない
			if (duplicate.contains(item.getCode())) {
				errors.add( "コードが重複しています。" );
			}
			// エラーがあればチェックを終える
			if (errors.size() > 0) {
				break;
			}
			duplicate.add(item.getCode());
		}

		return errors;
	}
}
