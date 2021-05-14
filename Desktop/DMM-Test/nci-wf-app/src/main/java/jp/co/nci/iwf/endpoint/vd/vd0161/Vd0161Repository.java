package jp.co.nci.iwf.endpoint.vd.vd0161;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;

/**
 * 背景HTML設定のリポジトリ
 */
@ApplicationScoped
public class Vd0161Repository extends BaseRepository {

	/** コンテナ定義をPKで抽出 */
	public MwmContainer get(Long containerId) {
		if (containerId == null)
			return null;
		return em.find(MwmContainer.class, containerId);
	}

	/** コンテナ定義を更新 */
	public void update(MwmContainer c, Vd0161Entity entity) {
		c.setBgHtml(entity.bgHtml);
		c.setCustomCssStyle(entity.customCssStyle);
	}
}
