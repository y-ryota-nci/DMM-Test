package jp.co.nci.iwf.endpoint.vd.vd0111;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * 条件定義一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0111Repository extends BaseRepository {

	/**
	 * 設定可能な条件定義一覧を取得.
	 * @param localeCode ロケールコード
	 * @return 条件定義一覧
	 */
	public List<PartsCond> getPartsCondDefList(final String corporationCode, final String localeCode) {
		final Object[] params = { corporationCode, localeCode };
		final List<MwmLookup> list = select(MwmLookup.class, getSql("VD0111_01"), params);
		return list.stream().map(c -> new PartsCond(c)).collect(Collectors.toList());
	}

	/**
	 * 条件定義を行うパーツが画面定義側で条件定義が設定されているか.
	 * 設定されている場合はその画面名称を戻す
	 * @param partsId
	 * @return 条件定義がされてある画面名称一覧
	 */
	public List<String> isExistsScreenPartsCond(final Long partsId, final String localeCode) {
		if (partsId == null) {
			return null;
		}
		final Object[] params = { localeCode, partsId };
		final List<MwvScreen> list = select(MwvScreen.class, getSql("VD0111_02"), params);
		return list.stream().map(e -> e.screenName).collect(Collectors.toList());
	}

	/**
	 * 画面パーツ条件定義を行うパーツがコンテナ側で既に条件定義が設定されているか.
	 * 設定されている場合はそのコンテナ名称を戻す
	 * @param partsId
	 * @return 条件定義がされてあるコンテナ名称一覧
	 */
	public List<String> isExistsPartsCond(final Long partsId, final String localeCode) {
		if (partsId == null) {
			return null;
		}
		final Object[] params = { localeCode, partsId };
		final List<MwvContainer> list = select(MwvContainer.class, getSql("VD0111_03"), params);
		return list.stream().map(e -> e.containerName).collect(Collectors.toList());
	}
}
