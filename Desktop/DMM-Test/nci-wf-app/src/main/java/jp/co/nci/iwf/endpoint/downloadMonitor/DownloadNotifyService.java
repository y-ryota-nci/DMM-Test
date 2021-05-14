package jp.co.nci.iwf.endpoint.downloadMonitor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ダウンロード開始と終了の通知サービス
 */
@BizLogic
public class DownloadNotifyService extends BaseService {
	@Context private HttpServletRequest req;

	/**
	 * DownloadMonitorへの開始通知
	 * @param token DownloadMonitorトークン
	 */
	public void begin() {
		// DownloadMonitorキューを取得、なかったら生成
		final String token = getToken();
		if (StringUtils.isNotEmpty(token)) {
			// トークンをキューに登録し、DownloadMonitorで監視できるようにする
			sessionHolder.getDownloadingQueues().add(token);
		}
	}

	/**
	 * DownloadMonitorへの終了通知
	 * @param token DownloadMonitorトークン
	 */
	public void end() {
		final String token = getToken();
		if (StringUtils.isNotEmpty(token)) {
			// 完了したトークンをキューから削除
			sessionHolder.getDownloadingQueues().remove(token);

			// 完了したトークンを履歴に登録
			sessionHolder.getDownloadedHistory().add(token);
		}
	}

	/**
	 * ダウンロードモニターのトークンを返す
	 * @return
	 */
	private String getToken() {
		return req.getParameter("___token");
	}
}
