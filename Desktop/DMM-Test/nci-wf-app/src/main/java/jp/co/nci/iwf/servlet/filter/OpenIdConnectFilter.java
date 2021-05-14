package jp.co.nci.iwf.servlet.filter;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.nci.iwf.component.CodeBook.AppURL;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.openid.OpenIdConnect;

/**
 * OpenIDConnectフィルター
 */
public class OpenIdConnectFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;

		String[] path = req.getRequestURI().split(req.getContextPath());
		if (path.length > 1) {
			final AuthenticateService auth = AuthenticateService.get();
			OpenIdConnect openIdConnect = CDI.current().select(OpenIdConnect.class).get();
			if ("/oidc/.well-known/openid-configuration".equals(path[1])) {
				openIdConnect.wellKnownResponse(req, res);
				return;
			} else if ("/oidc/auth".equals(path[1])) {
				authorize(req, res, auth.isAuthenticated());
				return;
			} else if ("/oidc/token".equals(path[1])) {
				if (openIdConnect.isTokenRequest(req)) {
					openIdConnect.tokenResponse(req, res);
					return;
				}
			} else if ("/oidc/certs".equals(path[1])) {
				openIdConnect.certsResponse(req, res);
				return;
			} else {
				if (auth.isAuthenticated() && !openIdConnect.getSessionHolder().isOpenIdAuthentication()) {
					Map<String, String> openIdConnectParam = openIdConnect.getSessionHolder().getFlushScope().get(OpenIdConnect.OPEN_ID_CONNECT_PARAM);
					if (openIdConnect != null && openIdConnect.isAuthenticationRequest(openIdConnectParam)) {
						LoginInfo login = LoginInfo.get();
						openIdConnect.AuthenticationResponse(openIdConnectParam, login.getUserAddedInfo(), openIdConnect.isHttps(req), res);
						openIdConnect.getSessionHolder().setOpenIdAuthentication(true);
						return;
					}
				}
			}
		}

		chain.doFilter(request, response);
	}



	/**
	 * 認証
	 * @param req
	 * @param res
	 * @param auth
	 * @return
	 */
	private void authorize(final HttpServletRequest req, final HttpServletResponse res, final boolean authenticated)
			throws IOException {
		OpenIdConnect openIdConnect = CDI.current().select(OpenIdConnect.class).get();
		boolean https = openIdConnect.isHttps(req);
		if (!authenticated) {
			openIdConnect.getSessionHolder().getFlushScope().put(OpenIdConnect.OPEN_ID_CONNECT_PARAM, openIdConnect.getOpenIdConnectParam(req));
			String url = req.getRequestURL().toString().split(req.getContextPath())[0] + req.getContextPath() + "/page/" + AppURL.LOGIN;
			if (https) {
				url = url.replace("http://", "https://");
			}
			res.sendRedirect(url);
		} else {
			Map<String, String> openIdConnectParam = openIdConnect.getOpenIdConnectParam(req);
			if (openIdConnect.isAuthenticationRequest(openIdConnectParam)) {
				openIdConnect.getSessionHolder().getFlushScope().put(OpenIdConnect.OPEN_ID_CONNECT_PARAM, openIdConnectParam);
				LoginInfo login = LoginInfo.get();
				openIdConnect.AuthenticationResponse(openIdConnectParam, login.getUserAddedInfo(), https, res);
			}
		}
	}

	@Override
	public void destroy() {
	}
}
