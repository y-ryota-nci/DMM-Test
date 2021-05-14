package jp.co.nci.iwf.component.i18n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfmLocaleV;

/**
 * 言語のリポジトリ
 */
@ApplicationScoped
public class LocaleRepository extends BaseRepository {
	/**
	 * ロケールマスタ一覧
	 * @param localeCodeLang 言語コード(何語でロケール名を表示するかの判定用)
	 * @return
	 */
	public List<WfmLocaleV> getAll(String localeCodeLang) {
		final List<String> params = new ArrayList<>();
		params.add(localeCodeLang);
		return select(WfmLocaleV.class, getSql("AU0003"), params.toArray());
	}
}
