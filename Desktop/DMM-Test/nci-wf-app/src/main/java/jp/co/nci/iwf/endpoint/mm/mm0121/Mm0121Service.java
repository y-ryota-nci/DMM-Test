package jp.co.nci.iwf.endpoint.mm.mm0121;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.endpoint.mm.mm0120.Mm0120Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;
import jp.co.nci.iwf.jpa.entity.mw.MwmAnnouncement;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * お知らせ設定画面サービス
 */
@BizLogic
public class Mm0121Service extends BaseService {
	@Inject private Mm0121Repository repository;
	@Inject private LocaleService locale;
	@Inject private MultilingalService multi;
	@Inject private Mm0120Service mm0120Service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0121Response init(Mm0121InitRequest req) {
		final Mm0121Response res = createResponse(Mm0121Response.class, req);

		if (req.announcementId != null && req.version != null) {
			res.entity = getEntity(req.announcementId);

			if (!eq(req.version, res.entity.version)) {
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			}
		}
		else if (req.announcementId == null && req.version == null)
			res.entity = createNewEntity();
		else
			throw new BadRequestException("バージョンが未指定です");

		res.corporations = mm0120Service.createCorporations();
		res.success = res.alerts.isEmpty();
		return res;
	}

	private Mm0121Entity getEntity(Long announcementId) {
		List<MwvAnnouncement> list = repository.getMwvAnnouncement(announcementId);
		if (list.isEmpty())
			throw new NotFoundException("お知らせ情報が存在しません。お知らせID=" + announcementId);

		final Mm0121Entity entity = new Mm0121Entity();
		final MwvAnnouncement ann = list.get(0);
		copyFieldsAndProperties(ann, entity);
		entity.ymdStart = toYmd(ann.timestampStart);
		entity.hhmmStart = toHHMM(ann.timestampStart);
		entity.ymdEnd = toYmd(ann.timestampEnd);
		entity.hhmmEnd = toHHMM(ann.timestampEnd);
		entity.locales = new ArrayList<>();


		Map<String, MwvAnnouncement> map = list.stream()
				.collect(Collectors.toMap(e -> e.localeCode, e -> e));

		// ベースはロケールマスタの言語数。
		for (OptionItem item : locale.getSelectableLocaleCodeOptions()) {
			final String localeCode = item.getValue(), localeName = item.getLabel();
			final MwvAnnouncement src = map.get(localeCode);
			final Mm0121EntityDetail d = new Mm0121EntityDetail();
			d.localeCode = localeCode;
			d.localeName = localeName;
			if (src != null) {
				d.subject = src.subject;
				d.contents = src.contents;
				d.linkTitle = src.linkTitle;
				d.linkUrl = src.linkUrl;
			}
			entity.locales.add(d);
		}
		return entity;
	}

	private String toHHMM(Timestamp t) {
		return new SimpleDateFormat("HH:mm").format(t);
	}

	private Date toYmd(Timestamp t) {
		return new java.sql.Date(t.getTime());
	}

	/** 新しくお知らせ情報を生成 */
	private Mm0121Entity createNewEntity() {
		final Mm0121Entity entity = new Mm0121Entity();
		entity.corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		entity.ymdStart = today();
		entity.hhmmStart = "00:00";
		entity.ymdEnd = ENDDATE;
		entity.hhmmEnd = "23:59";
		entity.locales = new ArrayList<>();
		for (OptionItem item : locale.getSelectableLocaleCodeOptions()) {
			final Mm0121EntityDetail d = new Mm0121EntityDetail();
			d.localeCode = item.getValue();
			d.localeName = item.getLabel();
			entity.locales.add(d);
		}
		return entity;
	}

	/**
	 * 保存処理
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Mm0121Request req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);

		String error = validate(req);
		if (isEmpty(error)) {
			Long announcementId = req.entity.announcementId;
			MwmAnnouncement current = repository.get(announcementId);
			if (current == null)
				announcementId = repository.insert(req.entity);
			else
				repository.update(current, req.entity);

			for (Mm0121EntityDetail d : req.entity.locales) {
				multi.save("MWM_ANNOUNCEMENT", announcementId, "SUBJECT", d.localeCode, d.subject);
				multi.save("MWM_ANNOUNCEMENT", announcementId, "CONTENTS", d.localeCode, d.contents);
				multi.save("MWM_ANNOUNCEMENT", announcementId, "LINK_TITLE", d.localeCode, d.linkTitle);
				multi.save("MWM_ANNOUNCEMENT", announcementId, "LINK_URL", d.localeCode, d.linkUrl);
			}

			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.informationSetting));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}

		return res;
	}

	/** バリデーション */
	private String validate(Mm0121Request req) {
		Mm0121Entity e = req.entity;
		if (e == null)
			throw new BadRequestException();
		if (e.locales == null || e.locales.isEmpty())
			throw new BadRequestException();

		// 有効期間は必須
		if (e.ymdStart == null || isEmpty(e.hhmmStart) || e.ymdEnd == null || isEmpty(e.hhmmEnd))
			return i18n.getText(MessageCd.MSG0001, MessageCd.validTerm);
		// 有効期間の時刻
		if (!ValidatorUtils.isTime(e.hhmmStart) || !ValidatorUtils.isTime(e.hhmmEnd))
			return i18n.getText(MessageCd.MSG0001, MessageCd.validTerm);



		StringBuilder subject = new StringBuilder(), contents = new StringBuilder();
		for (Mm0121EntityDetail d : e.locales) {
			if (isNotEmpty(d.subject))
				subject.append(d.subject);
			if (isNotEmpty(d.contents))
				contents.append(d.contents);
		}
		// 件名
		if (subject.length() == 0)
			return i18n.getText(MessageCd.MSG0001, MessageCd.title);
		// 内容
		if (contents.length() == 0)
			return i18n.getText(MessageCd.MSG0001, MessageCd.contents);

		return null;
	}

}
