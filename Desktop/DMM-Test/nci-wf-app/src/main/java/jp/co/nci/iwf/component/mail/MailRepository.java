package jp.co.nci.iwf.component.mail;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;

/**
 * メールサービス用リポジトリ
 */
@ApplicationScoped
public class MailRepository extends BaseRepository {
	@Inject private LocaleService localeService;

	/** メールテンプレートのヘッダを抽出 */
	public MwmMailTemplateHeader getHeader(String corporationCode, String fileName) {
		final String localeCode = localeService.getLocaleCode();	// これは操作者に備考を見せるためなので、操作者言語コードでよい
		final Object[] params = { corporationCode, localeCode, fileName };
		return selectOne(MwmMailTemplateHeader.class, getSql("ML0000_01"), params);
	}

	/** メールテンプレートの本文を抽出 */
	public List<MwmMailTemplateBody> getBody(Long mailTemplateHeaderId, String corporationCode) {
		final Object[] params = { mailTemplateHeaderId, corporationCode };
		return select(MwmMailTemplateBody.class, getSql("ML0000_02"), params);
	}

	/** 業務管理項目名称マスタを抽出 */
	public List<MwmBusinessInfoName> getBusinessInfoName(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmBusinessInfoName.class, getSql("ML0000_03"), params);
	}
}
