package jp.co.nci.iwf.endpoint.ml.ml0010;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.mail.download.MailTemplateDownloader;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メールテンプレート一覧のサービス
 */
@BizLogic
public class Ml0010Service extends BasePagingService {
	/** コンテナ一覧リポジトリ */
	@Inject private Ml0010Repository repository;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** 画面定義のダウンロードサービス */
	@Inject private MailTemplateDownloader downloader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ml0010InitResponse init(BaseRequest req) {
		final Ml0010InitResponse res = createResponse(Ml0010InitResponse.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ml0010Response search(Ml0010Request req) {
		final int allCount = repository.count(req);
		final Ml0010Response res = createResponse(Ml0010Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * メールテンプレート定義をZIPとしてダウンロード
	 * @return
	 */
	public StreamingOutput download(String corporationCode) {
		if (isEmpty(corporationCode))
			throw new BadRequestException("企業コードが指定されていません");

		return downloader.setup(corporationCode);
	}


	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Ml0010DeleteRequest req) {
		if (req.mailTemplateFileId == null || req.mailTemplateFileId == 0L)
			throw new NotFoundException("削除対象のIDが指定されていません");

		final String filename = repository.getFilename(req.mailTemplateFileId);
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		if (repository.delete(req.mailTemplateFileId, corporationCode))
			// メールテンプレートファイルが削除された
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.mailTemplate));
		else
			// 他企業で使用しているため、メールテンプレート "{0}" を削除できませんでした（当社分のヘッダと本文は削除されています）。
			res.addWarns(i18n.getText(MessageCd.MSG0172, filename));

		res.success = true;
		return res;
	}
}
