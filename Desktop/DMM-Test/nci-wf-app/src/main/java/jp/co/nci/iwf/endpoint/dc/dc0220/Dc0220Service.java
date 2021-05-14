package jp.co.nci.iwf.endpoint.dc.dc0220;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.HtmlResourceService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 文書トレイ編集サービス
 */
@BizLogic
public class Dc0220Service extends BaseService {
	/** 画面ルックアップ */
	@Inject private MwmLookupService lookup;
	/** リポジトリ */
	@Inject private Dc0220Repository repository;
	/** HTMLキャッシュ */
	@Inject private HtmlResourceService htmlCache;
	/** 多言語サービス */
	@Inject private MultilingalService multi;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0220Response init(Dc0220InitRequest req) {
		if (req.docTrayConfigId != null && req.version == null)
			throw new BadRequestException("バージョンが指定されていません");
		if (req.docTrayConfigId == null && req.version != null)
			throw new BadRequestException("トレイ設定IDが指定されていません");
		if (isEmpty(req.from))
			throw new BadRequestException("遷移元画面IDが指定されていません");

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		final String corporationCode = login.getCorporationCode();
		final Dc0220Response res = createResponse(Dc0220Response.class, req);
		// コンテンツ種別（文書管理用）の選択肢
		// 「3:ファイル」は除外(「1:業務文書」「2:バインダー」のみ含める)
		res.contentsTypes = lookup.getOptionItems(LookupGroupId.CONTENTS_TYPE, true, DcCodeBook.ContentsType.BIZ_DOC, DcCodeBook.ContentsType.BINDER);
		// 公開／非公開の選択肢
		res.publishFlags = lookup.getOptionItems(LookupGroupId.PUBLISH_FLAG, true);
		// 保存期間区分の選択肢
		res.retentionTermTypes = lookup.getOptionItems(LookupGroupId.RETENTION_TERM_TYPE, true);
		// テンプレートのHTML
		res.trayTemplateHtml = htmlCache.getContents("tray-template.html");
		// 業務管理項目名の選択肢
		res.docBusinessInfoNames = getDocBusinessInfoNames(localeCode, corporationCode);
		// 検索条件一致区分の選択肢
		res.conditionMatchTypes = lookup.getOptionItems(LookupGroupId.CONDITION_MATCH_TYPE, false);
		// 表示位置揃えの選択肢
		res.alignTypeOptions = lookup.getOptionItems(LookupGroupId.RESULT_ALIGN_TYPE, false);

		// 初期表示のトレイ設定データ
		if (req.docTrayConfigId != null) {
			res.entity = repository.getEntity(req.docTrayConfigId, localeCode);
			res.conditions = repository.getConditions(req.docTrayConfigId, localeCode);
			res.results = repository.getResults(req.docTrayConfigId, localeCode);
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

	/** 文書トレイ設定の初期データを生成 */
	private Dc0220Entity createInitEntity(String localeCode, String corporationCode, String from) {
		final Dc0220Entity entity = new Dc0220Entity();
		entity.docTrayConfigId = 0L;
		entity.corporationCode = corporationCode;
		entity.systemFlag = CommonFlag.OFF;
		entity.pageSize = 10;
		entity.sortOrder = 1;
		entity.deleteFlag = DeleteFlag.OFF;

		// DC0210 文書トレイ一覧(個人用)が遷移元なら個人使用フラグを立てる
		String dc0210 = MessageCd.DC0210.toString().toLowerCase();
		entity.personalUseFlag = eq(dc0210, from) ? CommonFlag.ON : CommonFlag.OFF;

		return entity;
	}

	/** 業務管理項目名の選択肢を生成 */
	private List<MwmDocBusinessInfoName> getDocBusinessInfoNames(String localeCode, String corporationCode) {
		return repository.getDocBusinessInfos(localeCode, corporationCode);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0220Response save(Dc0220SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final String localeCode = login.getLocaleCode();
		final Dc0220Response res = createResponse(Dc0220Response.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			// 差分更新
			final long docTrayConfigId = repository.saveEntity(req, localeCode, corporationCode);
			repository.saveConditions(req, docTrayConfigId);
			repository.saveResults(req, docTrayConfigId);
			// 多言語対応
			multi.save("MWM_DOC_TRAY_CONFIG", docTrayConfigId, "DOC_TRAY_CONFIG_NAME", req.entity.docTrayConfigName);

			// 読み直し
			res.entity = repository.getEntity(docTrayConfigId, localeCode);
			res.conditions = repository.getConditions(docTrayConfigId, localeCode);
			res.results = repository.getResults(docTrayConfigId, localeCode);

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
	private String validate(Dc0220SaveRequest req) {
		// トレイ設定マスタ
		if (isEmpty(req.entity.pageSize))
			return i18n.getText(MessageCd.MSG0001, MessageCd.pageSize);
		if (isEmpty(req.entity.sortOrder))
			return i18n.getText(MessageCd.MSG0001, MessageCd.sortOrder);
		if (isEmpty(req.entity.docTrayConfigName))
			return i18n.getText(MessageCd.MSG0001, MessageCd.userDisplayName);
		if (isEmpty(req.entity.docTrayConfigCode))
			return i18n.getText(MessageCd.MSG0001, MessageCd.docTrayConfigCode);

		// トレイ設定コードはユニークであること
		if (repository.existsDocTrayCongifCode(req.entity.corporationCode, req.entity.docTrayConfigCode, req.entity.docTrayConfigId))
			return i18n.getText(MessageCd.MSG0108, MessageCd.docTrayConfigCode, req.entity.docTrayConfigCode);

		// トレイ設定検索条件マスタ
		if (req.conditions != null) {
			for (int i = 0; i< req.conditions.size(); i++) {
				final Dc0220Condition c =  req.conditions.get(i);
				if (isEmpty(c.docBusinessInfoCode))
					return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.docBusinessInfoCode), (i + 1));
				if (isEmpty(c.conditionMatchType))
					return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.conditionType), (i + 1));
			}
		}

		// 文書トレイ設定検索条件マスタ
		if (req.results == null || req.results.isEmpty())
			return i18n.getText(MessageCd.MSG0159, 1, i18n.getText(MessageCd.searchResult));

		final int MAX_COL_WIDTH = 1200;
		boolean linkFlag = false, sortFlag = false;
		for (int i = 0; i < req.results.size(); i++) {
			final Dc0220Result r = req.results.get(i);
			// 文書業務管理項目コード
			if (isEmpty(r.docBusinessInfoCode))
				return i18n.getText(MessageCd.MSG0002, i18n.getText(MessageCd.docBusinessInfoCode), (i + 1));
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
	public Dc0220Response delete(Dc0220SaveRequest req) {
		Long docTrayConfigId = req.entity.docTrayConfigId;
		String error = null;
		if (docTrayConfigId == null || docTrayConfigId < 1L)
			error = i18n.getText(MessageCd.MSG0003, MessageCd.docTrayConfig);
		else if (eq(CommonFlag.ON, req.entity.systemFlag))
			// システムデータは削除不可
			error = i18n.getText(MessageCd.MSG0160);

		final Dc0220Response res = createResponse(Dc0220Response.class, req);
		if (isEmpty(error)) {
			// 削除
			repository.removeMwmDocTrayConfig(docTrayConfigId, req.entity.version);
			repository.removeMwmDocTrayConfigCondition(docTrayConfigId);
			repository.removeMwmDocTrayConfigResult(docTrayConfigId);
			// 多言語対応
			multi.physicalDelete("MWM_DOC_TRAY_CONFIG", docTrayConfigId);

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
