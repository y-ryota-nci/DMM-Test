package jp.co.nci.iwf.endpoint.dc.dc0062;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.vd.vd0114.Vd0114Repository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 拡張項目登録サービス.
 */
@BizLogic
public class Dc0062Service extends BaseService {

	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;
	/** ルックアップサービス */
	@Inject private WfmLookupService wfmLookup;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;
	/** 拡張項目登録リポジトリ */
	@Inject private Dc0062Repository repository;
	/** パーツプロパティ設定画面リポジトリ */
	@Inject private Vd0114Repository repository2;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0062Response init(Dc0062Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final Dc0062Response res = createResponse(Dc0062Response.class, req);
		if (req.metaId != null) {
			final MwmMetaItem entity = repository.getByPk(req.metaId);
			if (entity == null) {
				res.addAlerts(i18n.getText(MessageCd.noRecord));
			} else if (!eq(entity.getVersion(), req.version)) {
				throw new AlreadyUpdatedException();
			} else {
				res.entity = entity;
			}
		} else {
			final MwmMetaItem entity = new MwmMetaItem();
			entity.setCorporationCode(login.getCorporationCode());
			entity.setDeleteFlag(DeleteFlag.OFF);
			res.entity = entity;
		}
		// メタ入力タイプの選択肢
		res.inputTypes = lookup.getOptionItems(LookupGroupId.META_INPUT_TYPE, true);
		// 選択肢マスタの選択肢
		res.options = repository2.getMwmOptions(res.entity.getCorporationCode(), login.getLocaleCode())
				.stream()
				.map(o -> new OptionItem(o.getOptionId(), o.getOptionName()))
				.collect(Collectors.toList());
		// 削除区分の選択肢
		res.deleteFlags = wfmLookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		// 選択肢マスタの項目一覧の選択肢
		if (isNotEmpty(res.entity.getOptionId())) {
			res.items = this.changeOption(res.entity.getCorporationCode(), res.entity.getOptionId());
		}

		res.success = true;
		return res;
	}

	/**
	 * 選択肢プロパティ変更時のデフォルト値変更処理
	 * @param req
	 * @return
	 */
	public List<OptionItem> changeOption(String corporationCode, Long optionId) {
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

	/**
	 * 登録・更新処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0062Response save(Dc0062SaveRequest req) {
		final Dc0062Response res = createResponse(Dc0062Response.class, req);
		if (!validate(req, res)) {
			return res;
		}
		final MwmMetaItem org = repository.getByPk(req.entity.getMetaId());
		MwmMetaItem updated = null;
		if (org == null) {
			updated = repository.insert(req.entity);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.extendedItem));
		} else {
			updated = repository.update(org, req.entity);
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.extendedItem));
		}
		// 多言語対応(メタ名称)
		multi.save("MWM_META_ITEM", updated.getMetaId(), "META_NAME", updated.getMetaName());
		res.entity = updated;
		res.success = true;
		return res;
	}

	private boolean validate(Dc0062SaveRequest req, Dc0062Response res) {
		final MwmMetaItem entity = repository.get(req.entity.getCorporationCode(), req.entity.getMetaCode());
		if (entity != null) {
			if (!eq(entity.getMetaId(), req.entity.getMetaId())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0049));
			} else if (!eq(entity.getVersion(), req.entity.getVersion())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			}
		}
		return res.alerts.isEmpty();
	}
}
