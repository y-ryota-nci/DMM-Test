package jp.co.nci.iwf.jersey.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.ScreenAuthorityService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;

@Provider
//@PreMatching	/* これがあるとEndpointの特定前のため、フィルターが全リクエストに対して掛かってしまう */
@Priority(Priorities.AUTHORIZATION)
@RequiredLogin	// これが付与されたEndpointに対して当フィルターを適用
@ApplicationScoped
public class EndpointAuthorityFilter extends MiscUtils implements ContainerRequestFilter {
	@Inject private ScreenAuthorityService service;
	@Inject private SessionHolder sessionHolder;
	@Inject private Logger log;
	@Context private HttpServletRequest req;

	@Override
	public void filter(ContainerRequestContext crc) throws IOException {

		// 対象URIにアクセス権があるか
		final String uri = crc.getUriInfo().getPath();
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (login != null && !service.isAuthorized(login, req)) {
			String msg = String.format("対象URIへのアクセス権がありません。 path=%s corporationCode=%s userAddedInfo=%s",
					uri, login.getCorporationCode(), login.getUserAddedInfo());
			log.error(msg);
			throw new ForbiddenException(msg);
		}
	}

}
