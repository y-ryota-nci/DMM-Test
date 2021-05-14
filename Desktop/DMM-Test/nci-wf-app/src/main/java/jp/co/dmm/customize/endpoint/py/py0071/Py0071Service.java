package jp.co.dmm.customize.endpoint.py.py0071;

import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jp.co.dmm.customize.endpoint.py.py0071.excel.Py0071Book;
import jp.co.dmm.customize.endpoint.py.py0071.excel.Py0071SheetPayableBalDtl;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 買掛残高サービス
 */
@ApplicationScoped
public class Py0071Service extends BasePagingService {
	@Inject private Py0071Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Py0071Response init(Py0071Request req) {
		final Py0071Response res = createResponse(Py0071Response.class, req);
		res.companyNm = repository.getCompanyNm(req.companyCd);
		res.accNm = repository.getAccNm(req.companyCd, req.accCd);
		res.splrNmKj = repository.getSplrNm(req.companyCd, req.splrCd);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Py0071Response search(Py0071Request req) {
		final int allCount = repository.count(req);
		final Py0071Response res = createResponse(Py0071Response.class, req, allCount);
		res.results = repository.select(req, res);
		repository.getTotalAmt(req, res);
		res.success = true;
		return res;
	}

	/**
	 * ダウンロード
	 * @param req
	 * @return
	 */
	public Response download(Py0071Request req) {
		final Py0071Book book = new Py0071Book();
		{
			book.sheetPayableBalDtl = new Py0071SheetPayableBalDtl();
			book.sheetPayableBalDtl.payableBalDtls = repository.selectExcelPayableBalDtl(req);
		}
		final String fileName = MessageFormat.format("買掛残高詳細データ_{0}.xlsx", toStr(today(), "yyyyMMdd"));
		final Py0071StreamingOutput streaming = new Py0071StreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
