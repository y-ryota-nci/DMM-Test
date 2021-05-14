package jp.co.nci.iwf.endpoint.vd.vd0112;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;

/**
 * 有効条件設定のリポジトリ.
 */
@ApplicationScoped
public class Vd0112Repository extends BaseRepository {

	/**
	 * コンテナ一覧を取得.
	 * @param corporationCode 会社コード
	 * @param localeCode ロケールコード
	 * @return コンテナ一覧
	 */
	public List<MwmContainer> getContainers(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmContainer.class, getSql("VD0112_01"), params);
	}

	/**
	 * パーツ一覧を取得.
	 * @param containerId コンテナID
	 * @param localeCode ロケールコード
	 * @return パーツ一覧
	 */
	public List<MwmPart> getParts(String containerId, String localeCode) {
		final Object[] params = { containerId, localeCode };
		return select(MwmPart.class, getSql("VD0112_02"), params);
	}

	/**
	 * パーツ定義を取得.
	 * @param partsId パーツID
	 * @return パーツ定義
	 */
	public MwmPart getPartsInfo(String partsId, String localCode) {
		final Object[] params = { partsId, localCode };
		return select(MwmPart.class, getSql("VD0112_03"), params).stream().findFirst().orElse(null);
	}
}
