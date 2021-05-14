package jp.co.nci.iwf.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.MiscUtils;

@ApplicationScoped
public class HtmlResourceService extends MiscUtils implements CodeBook {
	/** キャッシュマネージャー */
	@Inject private CacheManager cm;

	/** HTMLファイル名をキー、HTML内容を値としたキャッシュ */
	private CacheHolder<String, String> cache;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cache = cm.newInstance(CacheInterval.FROM_DATABASE);
	}

	/** 指定されたHTMLファイルを クラスパス「/html/」配下から探し、その内容を返す */
	public String getContents(final String fileName) {
		// キャッシュにあるか？
		String html = cache.get(fileName);
		if (isEmpty(html)) {
			synchronized (cache) {
				html = cache.get(fileName);
				if (isEmpty(html)) {
					final ClassPathResource cpr = new ClassPathResource("html/" + fileName);
					if (!cpr.exists())
						throw new NotFoundException("HTMLファイルが見つかりません。 path=" + cpr.getPath());

					// HTMLを読み込み
					try (BufferedReader br = new BufferedReader(new InputStreamReader(cpr.getInputStream(), UTF8))) {
						html = br.lines().collect(Collectors.joining("\r\n"));
						cache.put(fileName, html);
					}
					catch (IOException e) {
						throw new InternalServerErrorException("HTMLの読み込みに失敗しました path=" + cpr.getPath(), e);
					}
				}
			}
		}
		return html;
	}
}
