package jp.co.nci.iwf.util;

import javax.servlet.http.HttpServletRequest;

import jp.co.nci.iwf.component.accesslog.ScreenInfo;

public class WebAppUtils {

	/**
	 * URIから画面IDを求める
	 * @param uriInfo
	 * @return
	 */
	public static ScreenInfo toScreenInfo(HttpServletRequest req) {
		String path = req.getPathInfo();
		if (path.indexOf('/') == 0)
			path = path.substring(1);
		String[] paths = path.split("/");
		ScreenInfo inf = new ScreenInfo();
		StringBuilder actionName = new StringBuilder(path.length());
		for (int i = 0; i < paths.length; i++) {
			if (i == 0) {
				inf.setScreenId(paths[i].toUpperCase());
			}
			else {
				actionName.append(actionName.length() == 0 ? "" : "/");
				actionName.append(paths[i]);
			}
		}
		inf.setActionName(actionName.toString());
		return inf;
	}

}
