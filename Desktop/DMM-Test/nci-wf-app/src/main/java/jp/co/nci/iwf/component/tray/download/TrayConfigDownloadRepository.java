package jp.co.nci.iwf.component.tray.download;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;

/**
 * トレイ設定ダウンロード用リポジトリ
 */
@ApplicationScoped
public class TrayConfigDownloadRepository extends BaseRepository {
	private static final String TABLE_NAME = quotePattern("{TABLE_NAME}");
	private static final String FILTER_BY_PK = quotePattern("{FILTER_BY_PK}");
	private static final String PK_COL_NAME = quotePattern("{PK_COL_NAME}");

	/** トレイ設定を抽出 */
	public List<MwmTrayConfig> getMwmTrayConfig(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmTrayConfig.class, getSql("WL0011_13"), params);
	}

	/** トレイ設定個人マスタを抽出 */
	public List<MwmTrayConfigPerson> getMwmTrayConfigPersons(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmTrayConfigPerson.class, getSql("WL0011_14"), params);
	}

	/** トレイ設定検索条件マスタを抽出  */
	public List<MwmTrayConfigCondition> getMwmTrayConfigConditions(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmTrayConfigCondition.class, getSql("WL0011_16"), params);
	}

	/** トレイ設定検索結果マスタを抽出  */
	public List<MwmTrayConfigResult> getMwmTrayConfigResults(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmTrayConfigResult.class, getSql("WL0011_17"), params);
	}

	/** 多言語対応マスタを抽出 */
	public List<MwmMultilingual> getMwmMultilingual(String tableName, String pkColName, String corporationCode) {
		final Object[] params = { tableName, corporationCode };
		final String sql = getSql("WL0011_18")
				.replaceAll(TABLE_NAME, tableName)
				.replaceFirst(PK_COL_NAME, pkColName)
				.replaceFirst(FILTER_BY_PK, "CORPORATION_CODE = ? ");
		return select(MwmMultilingual.class, sql, params);

	}

	/** 業務管理項目マスタ */
	public List<MwmBusinessInfoName> getMwmBusinessInfoNames(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmBusinessInfoName.class, getSql("WL0011_19"), params);
	}

}
