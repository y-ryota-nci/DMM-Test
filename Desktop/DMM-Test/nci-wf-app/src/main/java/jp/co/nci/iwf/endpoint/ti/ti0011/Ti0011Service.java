package jp.co.nci.iwf.endpoint.ti.ti0011;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.endpoint.ti.ti0010.Ti0010Category;
import jp.co.nci.iwf.endpoint.ti.ti0010.Ti0010Repository;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;

/**
 * カテゴリ編集画面サービス
 */
@BizLogic
public class Ti0011Service extends BaseService {
	@Inject private Ti0010Repository ti0010repository;
	@Inject private Ti0011Repository repository;
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0011Response init(BaseRequest req) {
		final Ti0011Response res = createResponse(Ti0011Response.class, req);

		// カテゴリ一覧
		res.categories = getCategories();
		res.success = true;
		return res;
	}

	/** カテゴリ一覧 */
	private List<Ti0010Category> getCategories() {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		return ti0010repository.getCategories(corporationCode, localeCode)
				.stream()
				.map(c -> new Ti0010Category(c))
				.collect(Collectors.toList());
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ti0011Response save(Ti0011SaveRequest req) {
		final Ti0011Response res = createResponse(Ti0011Response.class, req);

		// バリデーション
		final String error = validateUpdate(req);
		if (isEmpty(error)) {
			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			final Map<Long, MwmCategory> currents = ti0010repository.getCategories(corporationCode, localeCode)
					.stream()
					.collect(Collectors.toMap(c -> c.getCategoryId(), c -> c));

			// 差分更新
			for (Ti0010Category input : req.inputs) {
				MwmCategory current = currents.remove(input.categoryId);
				Long categoryId;
				if (current == null)
					categoryId = repository.insert(input);
				else
					categoryId = repository.update(current, input);
				multi.save("MWM_CATEGORY", categoryId, "CATEGORY_NAME", input.categoryName);
			}
			for (MwmCategory current : currents.values()) {
				repository.delete(current);
				multi.physicalDelete("MWM_CATEGORY", current.getCategoryId());
			}

			// 成功→再読み込み
			res.success = true;
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.category));
			res.categories = getCategories();
		}
		else {
			res.success = false;
			res.addAlerts(error);
		}
		return res;
	}

	private String validateUpdate(Ti0011SaveRequest req) {
		for (Ti0010Category input : req.inputs) {
			// 企業コード
			if (isEmpty(input.corporationCode))
				if (isEmpty(input.categoryCode))
					return i18n.getText(MessageCd.MSG0001, MessageCd.corporationCode);
			// カテゴリコード
			if (isEmpty(input.categoryCode))
				return i18n.getText(MessageCd.MSG0001, MessageCd.categoryCode);
			// カテゴリ名
			if (isEmpty(input.categoryName))
				return i18n.getText(MessageCd.MSG0001, MessageCd.categoryName);
		}
		return null;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Ti0011Response delete(Ti0011SaveRequest req) {
		final Ti0011Response res = createResponse(Ti0011Response.class, req);
		// バリデーション
		final String error = validateDelete(req);
		if (isEmpty(error)) {
			for (Ti0010Category c : req.inputs) {
				if (c.categoryId != null)
					repository.delete(c.categoryId);
			}
			res.success = true;
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.category));
			res.categories = getCategories();
		}
		else {
			res.success = false;
			res.addAlerts(error);
		}
		return res;
	}

	/** 削除用のバリデーション */
	private String validateDelete(Ti0011SaveRequest req) {
		for (Ti0010Category c : req.inputs) {
			// カテゴリ配下のテーブルがいないこと
			if (c.categoryId != null
					&& repository.countTableUnderCategory(c.corporationCode, c.categoryId) > 0) {
				String code = i18n.getText(MessageCd.categoryCode) + "=" + c.categoryCode;
				String master = i18n.getText(MessageCd.universalMaster);
				return i18n.getText(MessageCd.MSG0139, code, master);
			}
		}
		return null;
	}
}
