package jp.co.nci.iwf.component.mail.download;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;

/**
 * メールテンプレート定義ダウンロードのリポジトリ
 */
@ApplicationScoped
public class MailTemplateDownloadRepository extends BaseRepository {
	private static final String TABLE_NAME = quotePattern("{TABLE_NAME}");
	private static final String FILTER_TABLE_NAME = quotePattern("{FILTER_TABLE_NAME}");
	private static final String FILTER_BY_PK = quotePattern("{FILTER_BY_PK}");
	private static final String PK_COL_NAME = quotePattern("{PK_COL_NAME}");

	/** メールテンプレートヘッダの抽出 */
	public List<MwmMailTemplateHeader> getMwmMailTemplateHeader(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmMailTemplateHeader.class, getSql("ML0010_03"), params);
	}

	/** メールテンプレート本文の抽出 */
	public List<MwmMailTemplateBody> getMwmMailTemplateBody(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmMailTemplateBody.class, getSql("ML0010_04"), params);
	}

	/** メールテンプレートファイルの抽出 */
	public List<MwmMailTemplateFile> getMwmMailTemplateFile() {
		return select(MwmMailTemplateFile.class, getSql("ML0010_05"));
	}

	/** 業務管理項目マスタ */
	public List<MwmBusinessInfoName> getMwmBusinessInfoNames(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmBusinessInfoName.class, getSql("ML0010_06"), params);
	}

	/** 多言語対応マスタを抽出 */
	public List<MwmMultilingual> getMwmMultilingual(String tableName, String filterTableName, String pkColName, String corporationCode) {
		final Object[] params = { tableName, corporationCode };
		final String sql = getSql("ML0010_07")
				.replaceAll(TABLE_NAME, tableName)
				.replaceAll(FILTER_TABLE_NAME, filterTableName)
				.replaceFirst(PK_COL_NAME, pkColName)
				.replaceFirst(FILTER_BY_PK, "CORPORATION_CODE = ? ");
		return select(MwmMultilingual.class, sql, params);

	}

	/** メール変数マスタ */
	public List<MwmMailVariable> getMwmMailValiable(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmMailVariable.class, getSql("ML0010_08"), params);
	}
}
