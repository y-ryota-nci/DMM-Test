package jp.co.nci.iwf.endpoint.ml.ml0030;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * メールテンプレートファイル設定サービス
 */
@BizLogic
public class Ml0030Service extends BaseService {
	@Inject private Ml0030Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ml0030InitResponse init(Ml0030InitRequest req) {
		final Ml0030InitResponse res = createResponse(Ml0030InitResponse.class, req);
		final Long id = req.mailTemplateFileId;
		if (id == null) {
			// 新規
			res.file = new MwmMailTemplateFile();
		}
		else if (req.version == null) {
			// 既存レコードなのにバージョンが未指定
			throw new BadRequestException("Versionが未指定です");
		}
		else {
			final MwmMailTemplateFile f =
					repository.getMwmMailTemplateFile(id);
			if (f == null) {
				throw new NotFoundException(
						"メールテンプレートファイルマスタが見つかりません。mailTemplateFileId=" + id);
			}
			// 排他
			if (!eq(f.getVersion(), req.version)) {
				res.success = false;
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			}
			res.file = f;
		}
		res.success = true;
		return res;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Ml0030SaveRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final String error = validate(req);
		if (isEmpty(error)) {
			if (req.file.getMailTemplateFileId() == 0L) {
				repository.insert(req.file);
			} else {
				repository.update(req.file);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.mailTemplate));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	private String validate(Ml0030SaveRequest req) {
		long fileId = req.file.getMailTemplateFileId();
		String fileName = req.file.getMailTemplateFilename();
		if (isEmpty(fileName))
			return i18n.getText(MessageCd.MSG0001, MessageCd.fileName);

		if (!ValidatorUtils.isFilename(fileName))
			return i18n.getText(MessageCd.MSG0103, MessageCd.fileName);

		// ファイル名が他レコードで使われていないか
		if (repository.isExists(fileName, fileId))
			return i18n.getText(MessageCd.MSG0130, MessageCd.fileName);

		final String remarks = req.file.getRemarks();
		if (isEmpty(remarks))
			return i18n.getText(MessageCd.MSG0001, MessageCd.remarks);

		return null;
	}
}
