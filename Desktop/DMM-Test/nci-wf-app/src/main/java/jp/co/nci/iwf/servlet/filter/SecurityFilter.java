package jp.co.nci.iwf.servlet.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * セキュリティ関連のパラメータをヘッダへ埋め込むためのフィルター
 */
public class SecurityFilter implements Filter {
	/** X-FRAME-OPTIONSのキー */
	private static final String X_FRAME_OPTIONS = "X-FRAME-OPTIONS";
	/** X-Content-Type-Optionsのキー */
	private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	/** X-XSS-Protectionのキー*/
	private static final String X_XSS_PROTECTION = "X-XSS-Protection";

	/** X-FRAME-OPTIONS の設定値 */
	private static String xFrameOption = "DENY";
	/** X-Content-Type-Options の設定値 */
	private static String xContentTypeOption = "nosniff";
	/** X-XSS-Protectionの設定値 */
	private static String xssProtection = " 1; mode=block";

	/**
	 * IEドキュメントモード定数
	 */
	interface Compatible {
		/** IE互換モード用HTTPヘッダ名 */
		public static final String HTTP_HEADER_NAME = "X-UA-Compatible";
		/** IE6 */
		public static final String IE6 = "IE=6";
		/** IE6 互換 */
		public static final String IE6_Emulate = "IE=EmulateIE6";
		/** IE7 */
		public static final String IE7 = "IE=7";
		/** IE7 互換 */
		public static final String IE7_Emulate = "IE=EmulateIE7";
		/** IE8 */
		public static final String IE8 = "IE=8";
		/** IE8互換 */
		public static final String IE8_Emulate = "IE=EmulateIE8";
		/** IE9 */
		public static final String IE9 = "IE=9";
		/** IE9互換 */
		public static final String IE9_Emulate = "IE=EmulateIE9";
		/** IE10 */
		public static final String IE10 = "IE=10";
		/** IE10互換 */
		public static final String IE10_Emulate = "IE=EmulateIE10";
		/** クライアントのIEが解釈可能な最新バージョン */
		public static final String EDGE = "IE=edge";
	}

	/** init-param：IE互換モードで描画するURI */
	private static final String COMPATIBLE_IE_URI = "Compatible-IE-URI";
	/** init-param：「IE互換モードで描画するURI 」に該当した場合に適用するIEドキュメントモード */
	private static final String COMPATIBLE_IE_DOCUMENT_MODE = "Compatible-Mode";

	private static String compatibleDocMode;
	private static Set<String> compatibleUri = new HashSet<String>();

	/**
	 * Default constructor.
	 */
	public SecurityFilter() {
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// 他ドメインのコンテンツをIFRAMEに読み込めるか
		String s = config.getInitParameter(X_FRAME_OPTIONS);
		if (s != null && s.length() > 0) {
			xFrameOption = s;
		}

		// IE互換モードで描画するURI
		s = config.getInitParameter(COMPATIBLE_IE_URI);
		if (s != null && s.length() > 0) {
			final String astr[] = s.split(",\\s*");
			for (final String uri : astr) {
				if (StringUtils.isNotEmpty(uri)) {
					compatibleUri.add(uri);
				}
			}
		}
		// 「IE互換モードで描画するURI 」に該当した場合に適用するIEドキュメントモード
		compatibleDocMode = StringUtils.defaultIfEmpty(
				config.getInitParameter(COMPATIBLE_IE_DOCUMENT_MODE),
				Compatible.EDGE);	// エミュレート指定がなければEdge（最新）モード

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		xFrameOption = null;
		compatibleDocMode = null;
		compatibleUri.clear();
		compatibleUri = null;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;

		// 他ドメインのコンテンツをIFRAMEに読み込めるか
		// （クリックジャッキング対策）
		res.addHeader(X_FRAME_OPTIONS, xFrameOption);

		// IEに Content-Typeだけでファイルタイプを決定するよう強制
		res.addHeader(X_CONTENT_TYPE_OPTIONS, xContentTypeOption);

		// ブラウザへのXSSプロテクションを強制的に有効化
		res.addHeader(X_XSS_PROTECTION, xssProtection);

		// IEの互換モード（X-UA-Compatible）ヘッダをセット
		// 「IE互換モードで描画するURI 」に該当するなら指定のIE互換モード
		final String uri = req.getServletPath();
		if (uri != null && uri.length() > 0 && compatibleUri.contains(uri))
			res.setHeader(Compatible.HTTP_HEADER_NAME, compatibleDocMode);
		else
			res.setHeader(Compatible.HTTP_HEADER_NAME, Compatible.EDGE);

		// 他のフィルターを実行
		chain.doFilter(request, response);
	}
}
