package jp.co.nci.iwf.endpoint.mm.mm0002;

import java.sql.Date;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 組織編集画面のリポジトリ
 */
@ApplicationScoped
public class Mm0002Repository extends BaseRepository {
	/**
	 * 基準日以降で指定組織に所属しているユーザ数をカウント
	 * @param corporationCode 企業コード
	 * @param organizationCode 組織コード
	 * @param baseDate 基準日
	 * @return ユーザ数
	 */
	public int countUser(String corporationCode, String organizationCode, Date baseDate) {
		final Object[] params = { baseDate, baseDate, corporationCode, organizationCode };
		return count(getSql("MM0002_01"), params);
	}

}
