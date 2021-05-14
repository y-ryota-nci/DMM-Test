package jp.co.nci.iwf.endpoint.dc.dc0200;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocTrayType;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigPerson;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * (個人用)文書トレイ選択画面サービス
 */
@BizLogic
@Typed(Dc0200Service.class)
public class Dc0200Service extends BaseService {
	@Inject private Dc0200Repository repository;
	@Inject private MwmLookupService lookupService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0200InitResponse init(BaseRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Dc0200InitResponse res = createResponse(Dc0200InitResponse.class, req);

		// この画面で使用するトレイ種別のリスト
		final List<DocTrayType> filterList = createTrayTypeFilters(login);

		// 現在選択しているトレイ設定個人マスタ
		final Map<DocTrayType, MwmDocTrayConfigPerson> personalizes =
				getMwmDocTrayConfigPersons(login).stream()
				.collect(Collectors.toMap(p -> p.getDocTrayType(), p -> p));

		// トレイ設定の選択肢:トレイタイプごとでアクセス権のあるもののみ
		final DocTrayType[] filters = filterList.toArray(new DocTrayType[filterList.size()]);
		final List<MwmLookup> trayTypes = lookupService.get(LookupGroupId.DOC_TRAY_TYPE, filters);
		res.docTrayConfigOptions = createTrayConfigOptions(login, filters);

		// マージ
		final List<Dc0200Entity> entities = new ArrayList<>();
		for (MwmLookup t : trayTypes) {
			final Dc0200Entity entity = new Dc0200Entity();
			entity.docTrayType = DocTrayType.fromValue(t.getLookupId());
			entity.docTrayTypeName = t.getLookupName();
			final MwmDocTrayConfigPerson person = personalizes.get(entity.docTrayType);
			if (person != null) {
				entity.docTrayConfigId = person.getDocTrayConfigId();
				entity.docTrayConfigPersonalizeId = person.getDocTrayConfigPersonalizeId();
			}
			entities.add(entity);
		}
		res.entities = entities;
		res.success = true;

		return res;
	}

	/** 現在選択しているトレイ設定個人マスタ */
	protected List<MwmDocTrayConfigPerson> getMwmDocTrayConfigPersons(LoginInfo login) {
		return repository.getMwmDocTrayConfigPersons(login.getCorporationCode(), login.getUserCode());
	}

	/** この画面で使用するトレイ種別のリストを生成 */
	protected List<DocTrayType> createTrayTypeFilters(final LoginInfo login) {
		final List<DocTrayType> filterList = new ArrayList<>();
		if (login.getAccessibleScreenIds().contains(ScreenIds.DETAIL_SEARCH))
			filterList.add(DocTrayType.DETAIL_SEARCH);
		return filterList;
	}

	/** トレイタイプごとにトレイ設定の選択肢を生成 */
	private Map<DocTrayType, List<OptionItem>> createTrayConfigOptions(LoginInfo login, DocTrayType[] trayTypes) {
		final Map<DocTrayType, List<OptionItem>> map = new HashMap<>();
		for (DocTrayType trayType : trayTypes) {
			// トレイ設定の選択肢:アクセス権のあるもののみ
			List<OptionItem> options = getDocTrayConfigsAccessible(login, trayType.toString())
					.stream()
					.map(c -> {
						Long value = c.getDocTrayConfigId();
						String label = c.getDocTrayConfigName();
						if (eq(CommonFlag.ON, c.getSystemFlag()))
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
	protected List<MwmDocTrayConfig> getDocTrayConfigsAccessible(LoginInfo login, String trayType) {
		return repository.getDocTrayConfigsAccessible(
				login.getCorporationCode(), login.getUserCode(), login.getLocaleCode(), trayType);
	}

	/**
	 * 更新処理
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Dc0200SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			// 現在の選択内容
			final Map<Long, MwmDocTrayConfigPerson> currents =
					getMwmDocTrayConfigPersons(login).stream()
					.collect(Collectors.toMap(p -> p.getDocTrayConfigPersonalizeId(), p -> p));

			for (Dc0200Entity input : req.inputs) {
				if (input.docTrayConfigPersonalizeId != null && input.docTrayConfigId == null) {
					// 既存データが未選択となったら、既存データを削除
					repository.remove(input.docTrayConfigPersonalizeId);
				}
				else if (input.docTrayConfigId != null) {
					// 既存データが無ければインサート、あれば更新
					final MwmDocTrayConfigPerson current = currents.remove(input.docTrayConfigPersonalizeId);
					if (current == null)
						insert(login, input);
					else
						current.setDocTrayConfigId(input.docTrayConfigId);
				}
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.docTrayConfig));
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/** インサート */
	protected void insert(LoginInfo login, Dc0200Entity input) {
		repository.insert(login.getCorporationCode(), login.getUserCode(), input);
	}

	private String validate(Dc0200SaveRequest req) {
		for (Dc0200Entity input : req.inputs) {
			if (input.docTrayType == null)
				return i18n.getText(MessageCd.MSG0003, MessageCd.docTrayType);
		}
		return null;
	}
}
