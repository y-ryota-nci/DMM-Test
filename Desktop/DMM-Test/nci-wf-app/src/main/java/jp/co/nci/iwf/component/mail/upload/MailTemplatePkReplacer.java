package jp.co.nci.iwf.component.mail.upload;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.mail.download.MailTemplateDownloadDto;
import jp.co.nci.iwf.component.upload.BasePkReplacer;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.endpoint.up.up0050.Up0050Request;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;

/**
 * メールテンプレート定義アップロードのID置換ロジック
 */
@ApplicationScoped
public class MailTemplatePkReplacer extends BasePkReplacer {

	/**
	 * DTO内の全エンティティに対して、各エンティティのユニークキーからプライマリキーの置換要否を調べ、
	 * すでに使われているのであればプライマリキーを置き換える。
	 * @param req
	 * @param dto
	 */
	public ChangedPKsMap replaceAllPK(Up0050Request req, MailTemplateDownloadDto dto) {
		log.debug("START replaceAllPK()");

		// プライマリーキー置換対象
		final ChangedPKsMap changedPKsMap = new ChangedPKsMap();

		//------------------------------------------
		// 業務管理項目マスタ
		//------------------------------------------
		{
			final ChangedPKs<MwmBusinessInfoName> changes = replacePK(dto.businessNameList, MwmBusinessInfoName.class);
			changedPKsMap.put(changes);
		}
		//------------------------------------------
		// メールテンプレート関係
		//------------------------------------------
		// トレイ設定マスタ
		{
			// メールテンプレートファイルマスタ(MWM_MAIL_TEMPLATE_FILE)
			final ChangedPKs<MwmMailTemplateFile> changes = replacePK(dto.fileList, MwmMailTemplateFile.class);
			changedPKsMap.put(changes);
			// メールテンプレートファイルマスタ(MWM_MAIL_TEMPLATE_HEADER)
			copyPK("mailTemplateFileId", changes, dto.headerList);
		}
		// メールテンプレートファイルマスタ(MWM_MAIL_TEMPLATE_HEADER)
		{
			final ChangedPKs<MwmMailTemplateHeader> changes = replacePK(dto.headerList, MwmMailTemplateHeader.class);
			changedPKsMap.put(changes);
			// メールテンプレート本文マスタ(MWM_MAIL_TEMPLATE_BODY)
			copyPK("mailTemplateHeaderId", changes, dto.bodyList);
		}
		// メールテンプレート本文マスタ(MWM_MAIL_TEMPLATE_BODY)
		{
			final ChangedPKs<MwmMailTemplateBody> changes = replacePK(dto.bodyList, MwmMailTemplateBody.class);
			changedPKsMap.put(changes);
		}
		// メール変数マスタ(MWM_MAIL_VARIABLE)
		{
			final ChangedPKs<MwmMailVariable> changes = replacePK(dto.variableList, MwmMailVariable.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語はDelete＆Insertするから差分不要
//			final ChangedPKs<MwmMultilingual> changes = replacePK(dto.multilingualList, changedPKsMap);
//			changedPKsMap.put(changes);
		}

		log.debug("END replaceAllPK()");

		return changedPKsMap;
	}

}
