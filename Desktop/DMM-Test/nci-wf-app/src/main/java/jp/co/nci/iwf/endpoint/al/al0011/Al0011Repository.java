package jp.co.nci.iwf.endpoint.al.al0011;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.endpoint.al.al0010.Al0010Entity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLogDetail;

/**
 * アクセスログ詳細リポジトリ
 */
@ApplicationScoped
public class Al0011Repository extends BaseRepository {
	/** アクセスログを単独で抽出 */
	public Al0010Entity get(long accessLogId, String localeCode) {
		final Object[] params = { accessLogId, localeCode };
		return selectOne(Al0010Entity.class, getSql("AL0010_03"), params);
	}

	/** アクセスログ明細を抽出 */
	public List<MwtAccessLogDetail> getDetails(long accessLogId, String localeCode) {
		// 抽出
		final Object[] params = { accessLogId, localeCode };
		return select(MwtAccessLogDetail.class, getSql("AL0010_04"), params);
	}
}
