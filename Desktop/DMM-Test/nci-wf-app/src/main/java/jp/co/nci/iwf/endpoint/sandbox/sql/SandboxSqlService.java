package jp.co.nci.iwf.endpoint.sandbox.sql;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * サンドボックスSQLサービス
 */
@BizLogic
public class SandboxSqlService extends BaseService implements CodeBook {
	@Inject private SandboxSqlRepository repository;
	@Inject private DownloadNotifyService notify;
	@Inject private Logger log;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * SQL実行
	 * @param req
	 * @return
	 */
	public BasePagingResponse executeSql(SandboSqlRequest req) {
		BasePagingResponse res = createResponse(BasePagingResponse.class, req);
		try {
			res.results = repository.search(req);
			res.success = true;
		}
		catch (SQLException e) {
			if (eq("99999", e.getSQLState()) && e.getErrorCode() == 17021) {
				res.addAlerts(i18n.getText(MessageCd.MSG0278));
			} else {
				res.addAlerts(e.getMessage());
			}
			log.error(e.getMessage(), e);
			res.success = true;
		}
		catch (Exception e) {
			throw new InternalServerErrorException();
		}
		return res;
	}

	/** CSVダウンロード */
	public StreamingOutput downloadCsv(SandboSqlRequest req) {
		return new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				// ダウンロードモニターへ開始を通知
				notify.begin();

				// CSVPrinterはCSV形式でストリームへの書き込みを出来るようにする機能を提供する
				AtomicInteger count = new AtomicInteger();
				try (CSVPrinter csv = NciCsvFormat.print(new OutputStreamWriter(output, MS932))) {
					// CSVボディ
					// RowHandlerを使って抽出処理を行うと、一定行数をフェッチして少量ずつ逐次処理を行うため、メモリを食わない。
					// よってRowHandlerを使ってストリームへ逐次書き込みをすれば、1000万行のCSVダウンロードでも OutOfMemoryを誘発しない。
					repository.selectAll(req.sql, map -> {
						// ヘッダ行
						List<String> names = new ArrayList<>(map.keySet());
						if (count.incrementAndGet() == 1) {
							csv.printRecord(names);
						}
						// ボディ部
						List<String> line = new ArrayList<>(names.size());
						for (String name : names) {
							line.add(toStr(map.get(name)));
						}
						csv.printRecord(line);
						return true;
					});
				}
				catch (SQLException e) {
					if (eq("99999", e.getSQLState()) && e.getErrorCode() == 17021) {
						throw new InvalidUserInputException(i18n.getText(MessageCd.MSG0278));
					} else {
						throw new InvalidUserInputException(e.getMessage());
					}
				}
				catch (Exception e) {
					throw new InternalServerErrorException(e);
				}
				finally {
					// ダウンロードモニターへ終了を通知
					notify.end();
				}
			}
		};
	}

}
