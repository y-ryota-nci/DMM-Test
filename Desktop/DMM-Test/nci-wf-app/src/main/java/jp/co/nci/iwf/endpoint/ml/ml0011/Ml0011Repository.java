package jp.co.nci.iwf.endpoint.ml.ml0011;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;

/**
 * メールテンプレート編集のリポジトリ
 */
@ApplicationScoped
public class Ml0011Repository extends BaseRepository {
	@Inject private NumberingService numbering;


	/** メールテンプレートヘッダを抽出 */
	public Ml0011EntityHeader getHeader(Long fileId, String corporationCode, String localeCode) {
		final Object[] params = { corporationCode, localeCode, fileId };
		Ml0011EntityHeader header = selectOne(Ml0011EntityHeader.class, getSql("ML0011_01"), params);
		em.detach(header);
		return header;
	}

	/** メールテンプレートヘッダを本文を抽出 */
	public List<Ml0011EntityBody> getBody(Long headerId) {
		if (headerId == null)
			return new ArrayList<>();

		final Object[] params = { headerId };
		List<Ml0011EntityBody> list = select(Ml0011EntityBody.class, getSql("ML0011_02"), params);
		for (Ml0011EntityBody body : list)
			em.detach(body);
		return list;
	}

	/** 業務管理項目名称マスタを抽出 */
	public List<MwmBusinessInfoName> getMwmBusinessInfoName(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmBusinessInfoName.class, getSql("ML0011_03"), params);
	}

	/** メールテンプレートヘッダマスタを抽出 */
	public MwmMailTemplateHeader getMwmMailTemplateHeader(Long headerId) {
		if (headerId == null)
			return null;
		return em.find(MwmMailTemplateHeader.class, headerId);
	}

	/** メールテンプレート本文マスタを抽出 */
	public List<MwmMailTemplateBody> getMwmMailTemplateBody(Long headerId) {
		final Object[] params = { headerId };
		return select(MwmMailTemplateBody.class, getSql("ML0011_04"), params);
	}

	/** メールテンプレート本文をインサート */
	public void insert(Long headerId, Ml0011EntityBody input) {
		final long mailTemplateBodyId = numbering.newPK(MwmMailTemplateBody.class);
		final MwmMailTemplateBody body = new MwmMailTemplateBody();
		body.setDeleteFlag(DeleteFlag.OFF);
		body.setLocaleCode(input.localeCode);
		body.setMailBody(input.mailBody);
		body.setMailSubject(input.mailSubject);
		body.setMailTemplateBodyId(mailTemplateBodyId);
		body.setMailTemplateHeaderId(headerId);
		em.persist(body);
	}

	/** メールテンプレート本文をアップデート */
	public void update(MwmMailTemplateBody body, Ml0011EntityBody input) {
		body.setDeleteFlag(DeleteFlag.OFF);
		body.setMailBody(input.mailBody);
		body.setMailSubject(input.mailSubject);
		em.merge(body);
	}

	/** メールテンプレート本文を削除 */
	public void delete(MwmMailTemplateBody body) {
		em.remove(body);
	}

	/** メールテンプレートヘッダをインサート */
	public Long insert(Ml0011EntityHeader input) {
		final long mailTemplateHeaderId = numbering.newPK(MwmMailTemplateHeader.class);
		final MwmMailTemplateHeader h = new MwmMailTemplateHeader();
		h.setCorporationCode(input.corporationCode);
		h.setDeleteFlag(DeleteFlag.OFF);
		h.setMailTemplateFileId(input.mailTemplateFileId);
		h.setMailTemplateHeaderId(mailTemplateHeaderId);
		h.setReturnTo(input.returnTo);
		h.setSendBcc(input.sendBcc);
		h.setSendCc(input.sendCc);
		h.setSendFrom(input.sendFrom);
		h.setSendFromPersonal(input.sendFromPersonal);
		h.setSendTo(input.sendTo);
		em.persist(h);
		return mailTemplateHeaderId;
	}

	/** メールテンプレートヘッダをアップデート */
	public void update(MwmMailTemplateHeader h, Ml0011EntityHeader input) {
		h.setDeleteFlag(DeleteFlag.OFF);
		h.setReturnTo(input.returnTo);
		h.setSendBcc(input.sendBcc);
		h.setSendCc(input.sendCc);
		h.setSendFrom(input.sendFrom);
		h.setSendFromPersonal(input.sendFromPersonal);
		h.setSendTo(input.sendTo);
		em.merge(h);
	}
}
