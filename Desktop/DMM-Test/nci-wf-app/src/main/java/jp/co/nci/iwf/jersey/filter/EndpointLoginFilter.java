package jp.co.nci.iwf.jersey.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * jerseyリクエスト用のログインフィルター。
 * アノテーション「@LoginRequired」が付与されているEndpointに対して、ログイン認証確認処理を実施する
 */
@Provider
//@PreMatching	/* これがあるとEndpointの特定前のため、フィルターが全リクエストに対して掛かってしまう */
@Priority(Priorities.AUTHENTICATION)
@RequiredLogin	// これが付与されたEndpointに対して当フィルターを適用
@ApplicationScoped
public class EndpointLoginFilter extends MiscUtils implements ContainerRequestFilter {
	@Inject private Logger log;
	@Context private HttpServletRequest req;

	@Override
	public void filter(ContainerRequestContext crc) throws IOException {

		// ユーザ認証済か
		final AuthenticateService auth = AuthenticateService.get();
		final String uri = crc.getUriInfo().getPath();
		if (!auth.isAuthenticated()) {
			String msg = String.format("認証されていません。ログインが必要です %s", uri);
			log.error(msg);
			throw new NotAuthorizedException(msg);
		}
	}
}
