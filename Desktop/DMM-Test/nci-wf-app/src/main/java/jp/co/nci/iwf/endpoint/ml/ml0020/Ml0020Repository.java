package jp.co.nci.iwf.endpoint.ml.ml0020;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;

/**
 * メール環境設定のリポジトリ
 */
@ApplicationScoped
public class Ml0020Repository extends BaseRepository {
	/** メール環境マスタを抽出 */
	public List<MwmMailConfig> getConfigs() {
		return select(MwmMailConfig.class, getSql("ML0020_01"));
	}

	public void update(MwmMailConfig current, Ml0020Entity input) {
		current.setConfigValue(input.configValue);
		current.setVersion(input.version);
		em.flush();
	}
}
