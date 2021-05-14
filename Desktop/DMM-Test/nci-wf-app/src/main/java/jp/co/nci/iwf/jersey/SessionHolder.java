package jp.co.nci.iwf.jersey;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * セッション情報の格納クラス
 */
@SessionScoped
@Named("sessionHolder")
public class SessionHolder implements Serializable {
	/** ログイン者情報 */
	private LoginInfo loginInfo;
	/** WF API操作時のユーザ情報 */
	private WfUserRole wfUserRole;
	/** put()後、一度だけget()できるMap。get()は値が削除される */
	private transient FlushScope flushScope = new FlushScope();
	/** ダウンロードモニターの処理済みトークン履歴 */
	private final Set<String> downloadedHistory = Collections.synchronizedSet(new HashSet<String>());
	/** 処理中のトークンを格納しているキュー */
	private final Set<String> downloadingQueues = Collections.synchronizedSet(new HashSet<String>());

	private boolean openIdAuthentication;

	@PostConstruct
	public void init() {
		if (flushScope == null) {
			flushScope = new FlushScope();
		}
	}

	@PreDestroy
	public void preDestory() {
		loginInfo = null;
		wfUserRole = null;
		if (flushScope != null) flushScope.clear();
	}

	/** ログイン者情報 */
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	/** ログイン者情報 */
	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	/** WF API操作時のユーザ情報 */
	public WfUserRole getWfUserRole() {
		return wfUserRole;
	}
	/** WF API操作時のユーザ情報 */
	public void setWfUserRole(WfUserRole wfUserRole) {
		this.wfUserRole = wfUserRole;
	}

	/** put()後、一度だけget()できるMap。get()は値が削除される */
	public FlushScope getFlushScope() {
		if (flushScope == null) {
			flushScope = new FlushScope();
		}
		return flushScope;
	}

	/** ダウンロード済みトークン履歴 */
	public Set<String> getDownloadedHistory() {
		return downloadedHistory;
	}

	/** ダウンロード中のトークンを格納しているキュー */
	public Set<String> getDownloadingQueues() {
		return downloadingQueues;
	}

	/** ユーザ認証されているか */
	public boolean isAuthenticated() {
		LoginInfo loginInfo = getLoginInfo();
		return loginInfo != null
				&& MiscUtils.isNotEmpty(loginInfo.getCorporationCode())
				&& MiscUtils.isNotEmpty(loginInfo.getUserAddedInfo())
				&& loginInfo.getMenuRoleCodes() != null
				&& !loginInfo.getMenuRoleCodes().isEmpty();
	}

	public boolean isOpenIdAuthentication() {
		return openIdAuthentication;
	}

	public void setOpenIdAuthentication(boolean openIdAuthentication) {
		this.openIdAuthentication = openIdAuthentication;
	}
}
