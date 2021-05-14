package jp.co.dmm.customize.endpoint.ss.ss0010;

import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010Book;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfHd;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSAPSndInfPd;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInf;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInfDt;
import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010SheetSSGLSndInfHd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * SS連携データService
 */
@ApplicationScoped
public class Ss0010Service extends BasePagingService {
	@Inject private Ss0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ss0010Response init(Ss0010Request req) {
		final Ss0010Response res = createResponse(Ss0010Response.class, req);
		String companyCd = LoginInfo.get().getCorporationCode();
		res.companyCd = companyCd;
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ss0010Response search(Ss0010Request req) {
		final int allCount = repository.count(req);
		final Ss0010Response res = createResponse(Ss0010Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	public Response download(Ss0010Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Ss0010Book book = new Ss0010Book();
		{
			if (req.datTpGL) {
				//SS-GL送信情報
				book.sheetGLSndInf = new Ss0010SheetSSGLSndInf();
				//SS-GL送信情報(ヘッダー)
				book.sheetGLSndInf.ssglSndInfs = repository.selectSSGLSndInfList(req);
				book.sheetGLSndInfHd = new Ss0010SheetSSGLSndInfHd();
				book.sheetGLSndInfHd.ssglSndInfHds = repository.selectSSGLSndInfHdList(req);
				//SS-GL送信情報(明細)
				book.sheetGLSndInfDt = new Ss0010SheetSSGLSndInfDt();
				book.sheetGLSndInfDt.ssglSndInfDts = repository.selectSSGLSndInfDtList(req);
			}
			if (req.datTpAP) {
				//SS-AP送信情報
				book.sheetAPSndInf = new Ss0010SheetSSAPSndInf();
				book.sheetAPSndInf.ssapSndInfs = repository.selectSSAPSndInfList(req);
				//SS-AP送信情報(ヘッダー)
				book.sheetAPSndInfHd = new Ss0010SheetSSAPSndInfHd();
				book.sheetAPSndInfHd.ssapSndInfHds = repository.selectSSAPSndInfHdList(req);
				//SS-AP送信情報(支払明細)
				book.sheetAPSndInfPd = new Ss0010SheetSSAPSndInfPd();
				book.sheetAPSndInfPd.ssapSndInfPds = repository.selectSSAPSndInfPdList(req);
				//SS-AP送信情報(明細)
				book.sheetAPSndInfDt = new Ss0010SheetSSAPSndInfDt();
				book.sheetAPSndInfDt.ssapSndInfDts = repository.selectSSAPSndInfDtList(req);
			}
		}
		final String fileName = MessageFormat.format("SS連携データ_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));
		final Ss0010StreamingOutput streaming = new Ss0010StreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
