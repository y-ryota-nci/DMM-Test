package jp.co.nci.iwf.component.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;

/**
 * メール変数サービス
 */
@ApplicationScoped
public class MailVariableService extends BaseRepository {

	/** メール変数マスタの選択肢を生成 */
	public List<OptionItem> getOptionItems(String corporationCode, String localeCode, boolean emptyLine) {
		List<OptionItem> items = new ArrayList<>();
		if (emptyLine)
			items.add(OptionItem.EMPTY);

		items.addAll(getMwmMailVariables(corporationCode, localeCode)
				.stream()
				.map(v -> new OptionItem(v.getMailVariableCode(), v.getMailVariableLabel()))
				.collect(Collectors.toList()));

		return items;
	}

	/** メール変数マスタを抽出 */
	public List<MwmMailVariable> getMwmMailVariables(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		final String sql = getSql("ML0010_08");
		return select(MwmMailVariable.class, sql, params);
	}
}
