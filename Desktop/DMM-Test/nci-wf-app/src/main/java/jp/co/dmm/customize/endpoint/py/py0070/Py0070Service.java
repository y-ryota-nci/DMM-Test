package jp.co.dmm.customize.endpoint.py.py0070;

import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jp.co.dmm.customize.endpoint.py.py0070.excel.Py0070Book;
import jp.co.dmm.customize.endpoint.py.py0070.excel.Py0070SheetPayableBal;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 買掛残高サービス
 */
@ApplicationScoped
public class Py0070Service extends BasePagingService {
	@Inject private Py0070Repository repository;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Py0070Response init(Py0070Request req) {
		final Py0070Response res = createResponse(Py0070Response.class, req);
		String companyCd = LoginInfo.get().getCorporationCode();
		res.accCds = repository.getSelectItems(companyCd, "PAYABLE_BALANCE_ACC", false);
		res.companyCds = corp.getMyCorporations(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Py0070Response search(Py0070Request req) {
		final int allCount = repository.count(req);
		final Py0070Response res = createResponse(Py0070Response.class, req, allCount);
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
	public Response download(Py0070Request req) {
		final Py0070Book book = new Py0070Book();
		{
			book.sheetPayableBal = new Py0070SheetPayableBal();
			book.sheetPayableBal.payableBals = repository.selectExcelPayableBal(req);
		}
		final String fileName = MessageFormat.format("買掛残高データ_{0}.xlsx", toStr(today(), "yyyyMMdd"));
		final Py0070StreamingOutput streaming = new Py0070StreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
