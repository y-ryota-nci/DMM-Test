package jp.co.nci.iwf.designer.service.upload;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.upload.BaseUploadRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面定義アップロードのリポジトリ
 */
@ApplicationScoped
public class ScreenUploadRepository extends BaseUploadRepository {

	public Object getByPrimaryKey(Class<?> clazz, String pkJavaField, Object entity) {
		Long id = getPropertyValue(entity, pkJavaField);
		return em.find(clazz, id);
	}

	public Object getByPrimaryKey(Class<?> clazz, Object id) {
		return em.find(clazz, id);
	}

	/** 画面コードが存在するか */
	public boolean existScreenCode(String corporationCode, String screenCode) {
		final Object[] params = { corporationCode, screenCode };
		return count(getSql("UP0001_15"), params) > 0;
	}

	/** コンテナコードが存在するか */
	public boolean existsContainerCode(String corporationCode, String containerCode) {
		final Object[] params = { corporationCode, containerCode };
		return count(getSql("UP0001_16"), params) > 0;
	}

	/** 対象カラムの最大値を返す */
	public long getCurrentMaxValue(String tblName, String pkColName) {
		String sql = new StringBuilder()
				.append("select nvl(max(").append(pkColName).append("), 0) from ")
				.append(tblName)
				.toString();
		return count(sql);
	}

	/** 画面マスタ.アップロード日時の更新 */
	public void updateUploadDatetime(MwmScreen s) {
		final Object[] params = { s.getScreenId() };
		execSql(getSql("UP0001_23"), params);
	}
}
