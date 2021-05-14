package jp.co.nci.iwf.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * キャッシュ制御用のHTTPヘッダを書き込むフィルター
 */
public class CacheControlFilter implements Filter {
	/** キャッシュ制御のHTTPヘッダ */
	private static final String CACHE_CONTROL = "Cache-Control";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;
		final String uri = req.getRequestURI();

		if (uri.endsWith(".html") || uri.endsWith(".jsp"))
			// HTMLへのブラウザキャッシュを許可するが、必ずサーバへアクセスして陳腐化してないかを確認させる。
			// この確認用リクエストによってHTTPファイルに対して常にFilterが効くようになる
			res.setHeader(CACHE_CONTROL, "private,max-age=0,must-revalidate");
		else if (uri.contains("/endpoint/"))
			// Endpointは毎回完全なレスポンスをダウンロード
			res.setHeader(CACHE_CONTROL, "no-store");
		else if (uri.endsWith(".js") || uri.endsWith(".css"))
			// cssとjavascriptはたまーに変更されるので毎回確認する
			res.setHeader(CACHE_CONTROL, "public,max-age=10,must-revalidate");
		else
			// その他は静的コンテンツと考えられるので、普通にキャッシュさせる
			res.setHeader(CACHE_CONTROL, "public,max-age=120");

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
