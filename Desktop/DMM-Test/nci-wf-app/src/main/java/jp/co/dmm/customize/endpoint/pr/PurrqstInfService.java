package jp.co.dmm.customize.endpoint.pr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.AdvpayInf;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 購入依頼用サービス
 */
@ApplicationScoped
public class PurrqstInfService extends BaseRepository {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** セッション情報 */
	@Inject private SessionHolder sessionHolder;

	public Map<String, List<UserDataEntity>> getUserDataMap(String companyCd, String purrqstNo) {
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// ヘッダー
		{
			final Object[] params = { localeCode, companyCd, purrqstNo };
			final String tableName = "MWT_PURRQST";	// ヘッダ部のコンテナのテーブル名
			final String sql = getSql("PR0011_01");
			final List<UserDataEntity> userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}
		// 明細
		{
			final Object[] params = { localeCode, companyCd, purrqstNo };
			final String tableName = "MWT_PURRQSTDTL";
			final String sql = getSql("PR0011_02");
			final List<UserDataEntity> userDataList = loader.getUserData(tableName, sql, params);
			tables.put(tableName, userDataList);
		}

		return tables;
	}

}
