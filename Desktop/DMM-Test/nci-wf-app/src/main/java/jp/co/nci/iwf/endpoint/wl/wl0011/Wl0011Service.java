package jp.co.nci.iwf.endpoint.wl.wl0011;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.HtmlResourceService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.BaseTrayService;
import jp.co.nci.iwf.endpoint.wl.wl0030.Wl0030Service;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;

/**
 * トレイ編集サービス
 */
@BizLogic
public class Wl0011Service extends BaseTrayService {
	/** 画面ルックアップ */
	@Inject private MwmLookupService mwmLookup;
	/** WFMルックアップ */
	@Inject private WfmLookupService wfmLookup;
	/** リポジトリ */
	@Inject private Wl0011Repository repository;
	/** HTMLキャッシュ */
	@Inject private HtmlResourceService htmlCache;
	/** 多言語サービス */
	@Inject private MultilingalService multi;
	/** トレイサービス */
	@Inject private Wl0030Service trayService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wl0011Response init(Wl0011InitRequest req) {
		if (req.trayConfigId != null && req.version == null)
			throw new BadRequestException("バージョンが指定されていません");
		if (req.trayConfigId == null && req.version != null)
			throw new BadRequestException("トレイ設定IDが指定されていません");
		if (isEmpty(req.from))
			throw new BadRequestException("遷移元画面IDが指定されていません");

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		final String corporationCode = login.getCorporationCode();
		final Wl0011Response res = createResponse(Wl0011Response.class, req);
		// テンプレートのHTML
		res.trayTemplateHtml = htmlCache.getContents("tray-template.html");
		// プロセス定義の選択肢
		res.processDefOptions = getProcessDefOptions(corporationCode);
		// 業務状態の選択肢
		res.businessStatusOptions = wfmLookup.getOptionItems(true, LookupTypeCode.BUSINESS_PROCESS_STATUS);
		// プロセス状態の選択肢
		res.processStatusOptions = wfmLookup.getOptionItems(true, LookupTypeCode.PROCESS_STATUS);
		// 業務管理項目名の選択肢
		res.businessInfoNames = getBusinessInfoNames(localeCode, corporationCode);
		// 検索条件一致区分の選択肢
		res.conditionMatchTypes = mwmLookup.getOptionItems(LookupGroupId.CONDITION_MATCH_TYPE, false);
		// 表示位置揃えの選択肢
		res.alignTypeOptions = mwmLookup.getOptionItems(LookupGroupId.RESULT_ALIGN_TYPE, false);
		// 画面の選択肢
		res.screens = trayService.getScreenCodes(corporationCode, localeCode);
		// 画面プロセスの選択肢
		res.screenProcesses = trayService.getScreenProcesses(corporationCode, localeCode);

		// 初期表示のトレイ設定データ
		if (req.trayConfigId != null) {
			res.entity = repository.getEntity(req.trayConfigId, localeCode);
			res.conditions = repository.getConditions(req.trayConfigId, localeCode);
			res.results = repository.getResults(req.trayConfigId, localeCode);
			// 排他チェック
			if (!eq(req.version, res.entity.version))
				throw new AlreadyUpdatedException();
		}
		else {
			res.entity = createInitEntity(localeCode, corporationCode, req.from);
		}

		res.success = true;
		return res;
	}

	/** トレイ設定の初期データを生成 */
	private Wl0011Entity createInitEntity(String localeCode, String corporationCode, String from) {
		final Wl0011Entity entity = new Wl0011Entity();
		entity.trayConfigId = 0L;
		entity.corporationCode = corporationCode;
		entity.systemFlag = CommonFlag.OFF;
		entity.pageSize = 10;
		entity.sortOrder = 1;
		entity.deleteFlag = DeleteFlag.OFF;

		// WL0015 トレイ設定一覧(個人用)が遷移元なら個人使用フラグを立てる
		String wl0015 = MessageCd.WL0015.toString().toLowerCase();
		entity.personalUseFlag = eq(wl0015, from) ? CommonFlag.ON : CommonFlag.OFF;

		return entity;
	}

	/** 業務管理項目名の選択肢を生成 */
	private List<MwmBusinessInfoName> getBusinessInfoNames(String localeCode, String corporationCode) {
		return repository.getBusinessInfos(localeCode, corporationCode);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Wl0011Response save(Wl0011SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final String localeCode = login.getLocaleCode();
		final Wl0011Response res = createResponse(Wl0011Response.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			// 差分更新
			final long trayConfigId = repository.saveEntity(req, localeCode, corporationCode);
			repository.saveConditions(req, trayConfigId);
			repository.saveResults(req, trayConfigId);
			// 多言語対応
			multi.save("MWM_TRAY_CONFIG", trayConfigId, "TRAY_CONFIG_NAME", req.entity.trayConfigName);

			// 読み直し
			res.entity = repository.getEntity(trayConfigId, localeCode);
			res.conditions = repository.getConditions(trayConfigId, localeCode);
			res.results = repository.getResults(trayConfigId, localeCode);

			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.trayDisplayConfig));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}

		return res;
	}



	/** エラーチェック */
	private String validate(Wl0011SaveRequest req) {
		// トレイ設定マスタ
		if (isEmpty(req.entity.pageSize))
			return i18n.getText(MessageCd.MSG0001, MessageCd.pageSize);
		if (isEmpty(req.entity.sortOrder))
			return i18n.getText(MessageCd.MSG0001, MessageCd.sortOrder);
		if (isEmpty(req.entity.trayConfigName))
			return i18n.getText(MessageCd.MSG0001, MessageCd.userDisplayName);
		if (isEmpty(req.entity.trayConfigCode))
			return i18n.getText(MessageCd.MSG0001, MessageCd.trayConfigCode);

		// トレイ設定コードはユニークであること
		if (repository.existsTrayCongifCode(req.entity.corporationCode, req.entity.trayConfigCode, req.entity.trayConfigId))
			return i18n.getText(MessageCd.MSG0108, MessageCd.trayConfigCode, req.entity.trayConfigCode);

		// トレイ設定検索条件マスタ
		if (req.conditions != null) {
			for (int i = 0; i< req.conditions.size(); i++) {
				final Wl0011Condition c =  req.conditions.get(i);
				if (isEmpty(c.businessInfoCode))
					return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.businessInfoCode), (i + 1));
				if (isEmpty(c.conditionMatchType))
					return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.conditionType), (i + 1));
			}
		}

		// トレイ設定検索条件マスタ
		if (req.results == null || req.results.isEmpty())
			return i18n.getText(MessageCd.MSG0159, 1, i18n.getText(MessageCd.searchResult));

		final int MAX_COL_WIDTH = 1200;
		boolean linkFlag = false, sortFlag = false;
		for (int i = 0; i < req.results.size(); i++) {
			final Wl0011Result r = req.results.get(i);
			// 業務管理項目コード
			if (isEmpty(r.businessInfoCode))
				return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.businessInfoCode), (i + 1));
			// 列幅
			if (r.colWidth == null)
				return i18n.getText(MessageCd.MSG0002, MessageCd.columnWidth, (i + 1));
			if (r.colWidth > MAX_COL_WIDTH)
				return i18n.getText(MessageCd.MSG0006, MessageCd.columnWidth, 1, MAX_COL_WIDTH, (i + 1));
			// リンク
			linkFlag |= eq(CommonFlag.ON, r.linkFlag);
			// 初期ソートフラグ
			sortFlag |= eq(CommonFlag.ON, r.initialSortFlag);
		}

		// リンクは最低１つ必要
		if (!linkFlag)
			return i18n.getText(MessageCd.MSG0159, 1, MessageCd.linkForDocument);
		// 初期ソートが最低一つは必要
		if (!sortFlag)
			return i18n.getText(MessageCd.MSG0003, MessageCd.initSort);

		return null;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Wl0011Response delete(Wl0011SaveRequest req) {
		Long trayConfigId = req.entity.trayConfigId;
		String error = null;
		if (trayConfigId == null || trayConfigId < 1L)
			error = i18n.getText(MessageCd.MSG0003, MessageCd.trayConfig);
		else if (eq(CommonFlag.ON, req.entity.systemFlag))
			// システムデータは削除不可
			error = i18n.getText(MessageCd.MSG0160);

		final Wl0011Response res = createResponse(Wl0011Response.class, req);
		if (isEmpty(error)) {
			// 削除
			repository.removeMwmTrayConfig(trayConfigId, req.entity.version);
			repository.removeMwmTrayConfigCondition(trayConfigId);
			repository.removeMwmTrayConfigResult(trayConfigId);
			// 多言語対応
			multi.physicalDelete("MWM_TRAY_CONFIG", trayConfigId);

			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.trayDisplayConfig));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}
}
