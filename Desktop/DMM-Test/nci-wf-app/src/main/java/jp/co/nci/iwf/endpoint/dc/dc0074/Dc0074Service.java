package jp.co.nci.iwf.endpoint.dc.dc0074;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.vd.vd0114.Vd0114Repository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaItemEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDetail;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * テンプレート明細登録サービス.
 */
@BizLogic
public class Dc0074Service extends BaseService {

	/** テンプレート明細登録リポジトリ */
	@Inject private Dc0074Repository repository;
	/** パーツプロパティ設定画面リポジトリ */
	@Inject private Vd0114Repository repository2;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0074Response init(Dc0074Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();

		final Dc0074Response res = createResponse(Dc0074Response.class, req);
		// 並び順の選択肢
		final List<OptionItem> sortOrders = repository.getSortOrders(req.metaTemplateId, localeCode);
		if (req.metaTemplateDetailId != null) {
			final MwmMetaTemplateDetailEx entity = repository.getMwmMetaTemplateDetailEx(req.metaTemplateId, req.metaTemplateDetailId, localeCode);
			if (entity == null) {
				res.addAlerts(i18n.getText(MessageCd.noRecord));
			} else if (!eq(entity.getVersion(), req.version)) {
				throw new AlreadyUpdatedException();
			} else {
				res.entity = entity;
			}
		} else {
			final MwmMetaTemplateDetailEx entity = new MwmMetaTemplateDetailEx();
			entity.setCorporationCode(login.getCorporationCode());
			entity.setMetaTemplateId(req.metaTemplateId);
			// 新規登録時、並び順は一番最後に設定
			entity.setSortOrder(sortOrders.size() + 1);
			entity.setDeleteFlag(DeleteFlag.OFF);
			res.entity = entity;
			// 並び順の選択肢に追加
			sortOrders.add(new OptionItem(sortOrders.size() + 1, toStr(sortOrders.size() + 1)));
		}
		// 拡張項目の選択肢
		res.metaItems = repository.getMwmMetaItemList(res.entity.getCorporationCode(), null, localeCode)
				.stream()
				.map(o -> new OptionItem(o.getMetaId(), o.getMetaName()))
				.collect(Collectors.toList());
//		// 削除区分の選択肢
//		res.deleteFlags = wfmLookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		// 並び順の選択肢
		res.sortOrders = sortOrders;
		// 選択肢マスタの項目一覧の選択肢
		res.optionItems = this.getOptionItems(res.entity.getCorporationCode(), res.entity.getOptionId());

		res.success = true;
		return res;
	}

	/**
	 * 拡張項目変更.
	 * @param req
	 * @return
	 */
	public Dc0074Response change(String corporationCode, Long metaId) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final Dc0074Response res = createResponse(Dc0074Response.class, null);
		// 拡張項目取得
		final MwmMetaItemEx metaItem = repository.getMwmMetaItemList(corporationCode, metaId, localeCode).stream().findAny().orElse(null);
		if (metaItem == null || eq(DeleteFlag.ON, metaItem.getDeleteFlag())) {
			res.addAlerts(i18n.getText(MessageCd.noRecord));
			return res;
		}
		res.metaItem = metaItem;
		// 選択肢マスタの項目一覧の選択肢
		res.optionItems = this.getOptionItems(res.metaItem.getCorporationCode(), res.metaItem.getOptionId());

		res.success = true;
		return res;
	}

	/**
	 * 登録・更新処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0074Response save(Dc0074SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final Dc0074Response res = createResponse(Dc0074Response.class, req);

		// 更新対象の明細レコード取得
		final MwmMetaTemplateDetail org = repository.get(req.entity.getMetaTemplateId(), req.entity.getMetaId());
		// 今回設定した並び順によって既存データの並び順を一括更新
		int orgSortOrder = (org != null? org.getSortOrder() : 9999);
		repository.updateSortOrder(req.entity.getMetaTemplateId(), req.entity.getMetaTemplateDetailId(), orgSortOrder, req.entity.getSortOrder(), login);

		// 対象データ登録／更新
		if (org != null) {
			orgSortOrder = org.getSortOrder();
			if (!eq(org.getMetaTemplateDetailId(), req.entity.getMetaTemplateDetailId())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0049));
			} else if (!eq(org.getVersion(), req.entity.getVersion())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			} else {
				repository.update(org, req.entity);
				res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.extendedItem));
			}
		} else {
			Long metaTemplateDetailId = repository.insert(req.entity);
			req.entity.setMetaTemplateDetailId(metaTemplateDetailId);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.extendedItem));
		}

		// 登録／更新したレコードを再取得
		final MwmMetaTemplateDetailEx entity = repository.getMwmMetaTemplateDetailEx(req.entity.getMetaTemplateId(), req.entity.getMetaTemplateDetailId(), login.getLocaleCode());
		res.entity = entity;
		res.success = true;
		return res;
	}

	private List<OptionItem> getOptionItems(String corporationCode, Long optionId) {
		if (optionId == null) {
			return null;
		}
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		// デフォルト値の選択肢項目一覧
		final List<MwmOptionItem> list = repository2.getMwmOptionItems(corporationCode, optionId, localeCode);
		final List<OptionItem> items = list.stream().map(i -> new OptionItem(i.getCode(), i.getLabel())).collect(Collectors.toList());
		// デフォルト値なしを入力させるため、ブランク行がなければ追加する
		if (items.isEmpty() || !isEmpty(items.get(0).getValue())) {
			items.add(0, OptionItem.EMPTY);
		}
		return items;
	}
}
