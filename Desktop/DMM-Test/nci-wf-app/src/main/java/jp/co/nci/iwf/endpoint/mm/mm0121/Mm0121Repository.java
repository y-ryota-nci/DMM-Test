package jp.co.nci.iwf.endpoint.mm.mm0121;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;
import jp.co.nci.iwf.jpa.entity.mw.MwmAnnouncement;

/**
 * お知らせ設定画面リポジトリ
 */
@ApplicationScoped
public class Mm0121Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** お知らせIDをキーに全言語コードのお知らせ情報を抽出 */
	public List<MwvAnnouncement> getMwvAnnouncement(Long announcementId) {
		if (announcementId == null) {
			return new ArrayList<>();
		}
		final Object[] params = { announcementId };
		return select(MwvAnnouncement.class, getSql("MM0121_01"), params);
	}

	/** PKでお知らせマスタを抽出 */
	public MwmAnnouncement get(Long announcementId) {
		if (announcementId == null) {
			return null;
		}
		return em.find(MwmAnnouncement.class, announcementId);
	}

	/** お知らせマスタをインサート */
	public long insert(Mm0121Entity input) {
		final long announcementId = numbering.newPK(MwmAnnouncement.class);
		final MwmAnnouncement dest = new MwmAnnouncement();
		copyFieldsAndProperties(input, dest);
		dest.setAnnouncementId(announcementId);
		dest.setCorporationCode(input.corporationCode);
		dest.setTimestampStart(toTimestamp(input.ymdStart, input.hhmmStart));
		dest.setTimestampEnd(toTimestamp(input.ymdEnd, input.hhmmEnd));
		dest.setDeleteFlag(DeleteFlag.OFF);

		final Mm0121EntityDetail srcDetail = input.locales.get(0);
		copyFieldsAndProperties(srcDetail, dest);

		em.persist(dest);
		em.flush();

		return announcementId;
	}

	private Timestamp toTimestamp(Date ymd, String hhmm) {
		Calendar c = Calendar.getInstance();
		c.setTime(ymd);

		if (eq("23:59", hhmm)) {
			c.add(Calendar.DATE, 1);
			c.add(Calendar.MILLISECOND, -1);
		}
		else {
			String[] times = hhmm.split(":");
			c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
			c.set(Calendar.MINUTE, Integer.valueOf(times[1]));
		}
		return new Timestamp(c.getTimeInMillis());
	}

	/** お知らせマスタを更新 */
	public void update(MwmAnnouncement current, Mm0121Entity input) {
		current.setCorporationCode(input.corporationCode);
		current.setTimestampStart(toTimestamp(input.ymdStart, input.hhmmStart));
		current.setTimestampEnd(toTimestamp(input.ymdEnd, input.hhmmEnd));
		current.setDeleteFlag(DeleteFlag.OFF);

		final Mm0121EntityDetail srcDetail = input.locales.get(0);
		copyFieldsAndProperties(srcDetail, current);

		em.flush();
	}

}
