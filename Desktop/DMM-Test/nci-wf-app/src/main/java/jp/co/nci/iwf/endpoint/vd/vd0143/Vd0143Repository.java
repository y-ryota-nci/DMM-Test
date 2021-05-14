package jp.co.nci.iwf.endpoint.vd.vd0143;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;

/**
 * 計算式定義一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0143Repository extends BaseRepository {

	/**
	 * パーツ計算式定義を行うパーツが画面定義側で画面計算式定義が設定されているか.
	 * 設定されている場合はその画面名称を戻す
	 * @param partsId
	 * @return 画面計算式定義がされてある画面名称一覧
	 */
	public List<String> isExistsScreenPartsCond(final Long partsId, final String localeCode) {
		if (partsId == null) {
			return null;
		}
		final Object[] params = { localeCode, partsId };
		final List<MwvScreen> list = select(MwvScreen.class, getSql("VD0143_01"), params);
		return list.stream().map(e -> e.screenName).collect(Collectors.toList());
	}

	/**
	 * 画面計算式定義を行うパーツがコンテナ側で既にパーツ計算式定義が設定されているか.
	 * 設定されている場合はそのコンテナ名称を戻す
	 * @param partsId
	 * @return パーツ計算式定義がされてあるコンテナ名称一覧
	 */
	public List<String> isExistsPartsCond(final Long partsId, final String localeCode) {
		if (partsId == null) {
			return null;
		}
		final Object[] params = { localeCode, partsId };
		final List<MwvContainer> list = select(MwvContainer.class, getSql("VD0143_02"), params);
		return list.stream().map(e -> e.containerName).collect(Collectors.toList());
	}
}
