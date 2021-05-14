package jp.co.nci.iwf.endpoint.wl.wl0020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;

/**
 * 個人用トレイ選択画面のサービス
 */
@BizLogic
public class Wl0020Service extends BaseService {
	@Inject private Wl0020Repository repository;
	@Inject private MwmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wl0020InitResponse init(BaseRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Wl0020InitResponse res = createResponse(Wl0020InitResponse.class, req);

		// この画面で使用するトレイ種別のリスト
		final List<TrayType> filterList = createTrayTypeFilters(login);

		// 現在選択しているトレイ設定個人マスタ
		final Map<TrayType, MwmTrayConfigPerson> personalizes =
				getMwmTrayConfigPersons(login).stream()
				.collect(Collectors.toMap(p -> p.getTrayType(), p -> p));

		// トレイ設定の選択肢:トレイタイプごとでアクセス権のあるもののみ
		final TrayType[] filters = filterList.toArray(new TrayType[filterList.size()]);
		final List<MwmLookup> trayTypes = lookupService.get(LookupGroupId.TRAY_TYPE, filters);
		res.trayConfigOptions = createTrayConfigOptions(login, filters);

		// マージ
		final List<Wl0020Entity> entities = new ArrayList<>();
		for (MwmLookup t : trayTypes) {
			final Wl0020Entity entity = new Wl0020Entity();
			entity.trayType = TrayType.fromValue(t.getLookupId());
			entity.trayTypeName = t.getLookupName();
			final MwmTrayConfigPerson person = personalizes.get(entity.trayType);
			if (person != null) {
				entity.trayConfigId = person.getTrayConfigId();
				entity.trayConfigPersonalizeId = person.getTrayConfigPersonalizeId();
			}
			entities.add(entity);
		}
		res.entities = entities;
		res.success = true;

		return res;
	}

	/** 現在選択しているトレイ設定個人マスタ */
	protected List<MwmTrayConfigPerson> getMwmTrayConfigPersons(LoginInfo login) {
		return repository.getMwmTrayConfigPersons(login.getCorporationCode(), login.getUserCode());
	}

	/** この画面で使用するトレイ種別のリストを生成 */
	protected List<TrayType> createTrayTypeFilters(final LoginInfo login) {
		final List<TrayType> filterList = new ArrayList<>();
		if (login.getAccessibleScreenIds().contains(ScreenIds.WORKLIST))
			filterList.add(TrayType.WORKLIST);
		if (login.getAccessibleScreenIds().contains(ScreenIds.FORCE))
			filterList.add(TrayType.FORCE);
		if (login.getAccessibleScreenIds().contains(ScreenIds.OWN))
			filterList.add(TrayType.OWN);
		if (login.getAccessibleScreenIds().contains(ScreenIds.ALL))
			filterList.add(TrayType.ALL);
		if (login.getAccessibleScreenIds().contains(ScreenIds.BATCH))
			filterList.add(TrayType.BATCH);
		return filterList;
	}

	/** トレイタイプごとにトレイ設定の選択肢を生成 */
	private Map<TrayType, List<OptionItem>> createTrayConfigOptions(LoginInfo login, TrayType[] trayTypes) {
		final Map<TrayType, List<OptionItem>> map = new HashMap<>();
		for (TrayType trayType : trayTypes) {
			// トレイ設定の選択肢:アクセス権のあるもののみ
			List<OptionItem> options = getTrayConfigsAccessible(login, trayType.toString())
					.stream()
					.map(c -> {
						Long value = c.trayConfigId;
						String label = c.trayConfigName;
						if (eq(CommonFlag.ON, c.systemFlag))
								label += " **";
						return new OptionItem(value, label);
					})
					.collect(Collectors.toList());
			options.add(0, OptionItem.EMPTY);
			map.put(trayType, options);
		}
		return map;
	}

	/** アクセス可能なトレイ設定の選択肢を生成 */
	protected List<TrayConfig> getTrayConfigsAccessible(LoginInfo login, String trayType) {
		return repository.getTrayConfigsAccessible(
				login.getCorporationCode(), login.getUserCode(), login.getLocaleCode(), trayType);
	}

	/**
	 * 更新処理
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Wl0020SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			// 現在の選択内容
			final Map<Long, MwmTrayConfigPerson> currents =
					getMwmTrayConfigPersons(login).stream()
					.collect(Collectors.toMap(p -> p.getTrayConfigPersonalizeId(), p -> p));

			for (Wl0020Entity input : req.inputs) {
				if (input.trayConfigPersonalizeId != null && input.trayConfigId == null) {
					// 既存データが未選択となったら、既存データを削除
					repository.remove(input.trayConfigPersonalizeId);
				}
				else if (input.trayConfigId != null) {
					// 既存データが無ければインサート、あれば更新
					final MwmTrayConfigPerson current = currents.remove(input.trayConfigPersonalizeId);
					if (current == null)
						insert(login, input);
					else
						current.setTrayConfigId(input.trayConfigId);
				}
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.trayConfig));
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/** インサート */
	protected void insert(LoginInfo login, Wl0020Entity input) {
		repository.insert(login.getCorporationCode(), login.getUserCode(), input);
	}

	private String validate(Wl0020SaveRequest req) {
		for (Wl0020Entity input : req.inputs) {
			if (input.trayType == null)
				return i18n.getText(MessageCd.MSG0003, MessageCd.trayType);
		}
		return null;
	}


}
