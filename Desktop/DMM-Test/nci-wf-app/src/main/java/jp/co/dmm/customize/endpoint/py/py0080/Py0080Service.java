package jp.co.dmm.customize.endpoint.py.py0080;

import java.text.MessageFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jp.co.dmm.customize.endpoint.py.py0080.excel.Py0080Book;
import jp.co.dmm.customize.endpoint.py.py0080.excel.Py0080Sheet;
import jp.co.dmm.customize.endpoint.py.py0080.excel.Py0080StreamingOutput;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 前払残高一覧サービス
 */
@ApplicationScoped
public class Py0080Service extends BasePagingService {
	@Inject private Py0080Repository repository;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Py0080Response res = createResponse(Py0080Response.class, req);
		res.companyCds = corp.getMyCorporations(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Py0080SearchRequest req) {
		final int allCount = repository.count(req);
		final Py0080SearchResponse res = createResponse(Py0080SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * ダウンロード
	 * @param req
	 * @return
	 */
	public Response download(Py0080SearchRequest req) {
		final Py0080Book book = new Py0080Book();
		{
			book.sheet = new Py0080Sheet();
			book.sheet.results = repository.selectExcel(req);
		}
		final String fileName = MessageFormat.format("前払残高データ_{0}.xlsx", toStr(today(), "yyyyMMdd"));
		final Py0080StreamingOutput streaming = new Py0080StreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
