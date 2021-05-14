package jp.co.nci.iwf.component.system;

import java.sql.Timestamp;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;

/**
 * お知らせ情報リポジトリ
 */
@ApplicationScoped
public class AnnouncementReposiroty extends BaseRepository {
	/**
	 * 全社共通のお知らせのうち、指定日時で有効なお知らせを古いものからmax件を抽出
	 * @param baseDate 基準日
	 * @param localeCode 言語コード
	 * @param maxCount 最大件数
	 * @return
	 */
	public List<MwvAnnouncement> getLoginAnnouncement(Timestamp baseDate, String localeCode, int maxCount) {
		final Object[] params = {
				localeCode,
				CorporationCode.ASP,	// 企業コード＝ASPなら全社共通のお知らせとする
				baseDate,
				maxCount
		};
		return select(MwvAnnouncement.class, getSql("CM0018"), params);
	}

	/**
	 * 特定企業用のお知らせのうち、指定日時で有効なお知らせを古いものからmax件を抽出
	 * @param corporationCode
	 * @param baseDate
	 * @param localeCode
	 * @param maxCount
	 * @return
	 */
	public List<MwvAnnouncement> getTopAnnouncement(String corporationCode, Timestamp baseDate, String localeCode,
			int maxCount) {
		final Object[] params = {
				localeCode,
				corporationCode,
				baseDate,
				maxCount
		};
		return select(MwvAnnouncement.class, getSql("CM0019"), params);
	}
}
