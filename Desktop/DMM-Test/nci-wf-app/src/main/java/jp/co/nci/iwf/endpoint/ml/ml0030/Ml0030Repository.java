package jp.co.nci.iwf.endpoint.ml.ml0030;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;

/**
 * メールテンプレートファイル設定リポジトリ
 */
@ApplicationScoped
public class Ml0030Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** メールテンプレートファイルマスタを抽出 */
	public MwmMailTemplateFile getMwmMailTemplateFile(Long mailTemplateFileId) {
		return em.find(MwmMailTemplateFile.class, mailTemplateFileId);
	}

	/** メールテンプレートファイルマスタをインサート */
	public long insert(MwmMailTemplateFile input) {
		long id = numbering.newPK(MwmMailTemplateFile.class);
		input.setMailTemplateFileId(id);
		input.setDeleteFlag(DeleteFlag.OFF);
		em.persist(input);
		return id;
	}

	/** メールテンプレートファイルマスタをアップデート */
	public void update(MwmMailTemplateFile input) {
		MwmMailTemplateFile f = getMwmMailTemplateFile(input.getMailTemplateFileId());
		if (f == null)
			throw new AlreadyUpdatedException();

		f.setMailTemplateFilename(input.getMailTemplateFilename());
		f.setRemarks(input.getRemarks());
		f.setVersion(input.getVersion());
	}

	/**
	 * メールテンプレートファイルがすでに存在しているか
	 * @param fileName メールテンプレートのファイル名
	 * @param excludeFileId 除外するFileId（＝自分自身）
	 * @return
	 */
	public boolean isExists(String fileName, long excludeFileId) {
		final Object[] params = { fileName, excludeFileId };
		int count = count(getSql("ML0030_03"), params);
		return count > 0;
	}
}
