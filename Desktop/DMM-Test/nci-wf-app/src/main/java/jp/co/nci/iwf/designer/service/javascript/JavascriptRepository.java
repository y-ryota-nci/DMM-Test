package jp.co.nci.iwf.designer.service.javascript;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmJavascriptEntityEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 外部Javascriptサービス
 */
@ApplicationScoped
public class JavascriptRepository extends BaseRepository {
	private static final String KEY = MiscUtils.quotePattern("${JAVASCRIPT_IDS}");

	/** JavascriptIDをキーに外部Javascriptのスクリプト内容を抽出 */
	public List<MwmJavascriptEntityEx> get(List<Long> javascriptIds) {
		String replacement = javascriptIds.stream()
				.map(id -> id.toString())
				.collect(Collectors.joining(", "));

		String sql = getSql("VD0310_15").replaceFirst(KEY, replacement);
		return select(MwmJavascriptEntityEx.class, sql, javascriptIds.toArray());
	}

	/** 画面IDをキーに画面Javascriptを抽出 */
	public List<MwmScreenJavascript> getScreenJavascript(Long screenId) {
		final Object[] params = { screenId };
		return select(MwmScreenJavascript.class, getSql("VD0310_16"), params);
	}
}
