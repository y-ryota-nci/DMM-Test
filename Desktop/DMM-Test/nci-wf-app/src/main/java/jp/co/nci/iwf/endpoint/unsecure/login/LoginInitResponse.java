package jp.co.nci.iwf.endpoint.unsecure.login;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;

/**
 * ログイン画面の初期化レスポンス
 */
public class LoginInitResponse extends BaseResponse {


	/** お知らせ情報 */
	private List<MwvAnnouncement> announcements;

	/** お知らせ情報 */
	public List<MwvAnnouncement> getAnnouncements() {
		return announcements;
	}
	/** お知らせ情報 */
	public void setAnnouncements(List<MwvAnnouncement> announcements) {
		this.announcements = announcements;
	}
}
