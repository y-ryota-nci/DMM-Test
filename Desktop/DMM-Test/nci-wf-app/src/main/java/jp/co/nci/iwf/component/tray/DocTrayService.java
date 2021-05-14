package jp.co.nci.iwf.component.tray;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書管理トレイ情報のサービス
 */
public abstract class DocTrayService extends TrayService {
	@Inject private DocTrayRepository repository;
	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * トレイ系画面の初期化レスポンスを生成
	 * @param req リクエスト
	 * @param trayType トレイタイプ
	 * @return
	 */
	@Override
	protected TrayInitResponse createTrayInitResponse(BaseRequest req, String trayType) {
		if (trayType == null)
			throw new BadRequestException("trayTypeが未指定です");

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final TrayInitResponse res = createResponse(TrayInitResponse.class, req);
		res.trayType = trayType.toString();

		// 文書種別の選択肢
		res.processDefCodes = getProcessDefOptions(corporationCode);
		// コンテンツ種別（文書管理用）の選択肢
		// 「3:ファイル」は除外(「1:業務文書」「2:バインダー」のみ含める)
		res.contentsTypes = lookup.getOptionItems(LookupGroupId.CONTENTS_TYPE, true, DcCodeBook.ContentsType.BIZ_DOC, DcCodeBook.ContentsType.BINDER);
		// 公開／非公開の選択肢
		res.publishFlags = lookup.getOptionItems(LookupGroupId.PUBLISH_FLAG, true);
		// 保存期間区分の選択肢
		res.retentionTermTypes = lookup.getOptionItems(LookupGroupId.RETENTION_TERM_TYPE, true);
		// トレイのテンプレート用HTML
		res.trayTemplateHtml = htmlCache.getContents("tray-template.html");

		// ログイン者の参照可能なトレイ設定を求める
		final TrayConfig config = getAccessibleTrayConfig(trayType);
		if (config == null)
			throw new InternalServerErrorException("トレイ設定が1つもありません");

		final long trayConfigId = config.trayConfigId;
		res.config = config;
		res.configConditions = getTrayConditions(trayConfigId);
		res.configResults = getTrayResultDefs(trayConfigId);

		// 画面タイトルにトレイ設定名を付与
		if (isNotEmpty(config.trayConfigName))
			res.title += " : [" + config.trayConfigName + "]";

		return res;
	}

	/** トレイ設定検索条件マスタをキャッシュから求めて返す */
	@Override
	protected List<TrayConditionDef> getTrayConditions(long docTrayConfigId) {
		// null判定を2回しているのは、synchronizedの遅延を最小にするため
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<TrayConditionDef> conditions = cacheConditions.get(localeCode, docTrayConfigId);
		if (conditions == null) {
			synchronized (cacheConditions) {
				conditions = cacheConditions.get(localeCode, docTrayConfigId);
				if (conditions == null) {
					conditions = repository.getTrayConditions(docTrayConfigId, localeCode);
					if (conditions.isEmpty()) {
						log.warn("トレイ設定検索条件が０件です。docTrayConfigId={}", docTrayConfigId);
					}
					cacheConditions.put(localeCode, docTrayConfigId, conditions);
				}
			}
		}
		return conditions;
	}

	/** トレイ設定検索結果マスタをキャッシュから求めて返す */
	@Override
	protected List<TrayResultDef> getTrayResultDefs(long docTrayConfigId) {
		// null判定を2回しているのは、synchronizedの遅延を最小にするため
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<TrayResultDef> results = cacheResults.get(localeCode, docTrayConfigId);
		if (results == null) {
			synchronized (cacheResults) {
				results = cacheResults.get(localeCode, docTrayConfigId);
				if (results == null) {
					results = repository.getTrayResults(docTrayConfigId, localeCode);
					if (results.isEmpty()) {
						log.warn("トレイ設定検索結果が０件です。docTrayConfigId={}", docTrayConfigId);
					}
					cacheResults.put(localeCode, docTrayConfigId, results);
				}
			}
		}
		return results;
	}

	/** ログイン者の参照可能なトレイ設定を求める */
	@Override
	protected TrayConfig getAccessibleTrayConfig(String trayType) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final TrayConfig config = repository.getTrayConfig(trayType, login);
		return config;
	}
}
