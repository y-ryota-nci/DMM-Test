package jp.co.nci.iwf.component.accesslog;

import java.util.Queue;
import java.util.concurrent.Callable;

import javax.enterprise.inject.spi.CDI;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.component.CodeBook.AccessLogResultType;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLog;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLogDetail;

/**
 * アクセスログの登録を非同期実行させるためのタスク。
 */
public class AccessLogTask implements Callable<Long> {
	/** 登録内容のキュー */
	private Queue<Object> queue;

	/**
	 * コンストラクタ
	 * @param queue キュー
	 */
	public AccessLogTask(Queue<Object> queue) {
		this.queue = queue;
	}

	/**
	 * キューの内容をアクセスログへ書き込み
	 */
	@Transactional
	@Override
	public Long call() throws Exception {
		AccessLogRepository repository = CDI.current().select(AccessLogRepository.class).get();

		// キューにある限り実行
		Object obj = null;
		while (queue != null && (obj = queue.poll()) != null) {
			if (obj instanceof MwtAccessLog) {
				MwtAccessLog header = (MwtAccessLog)obj;
				if (AccessLogResultType.UNKNOWN.equals(header.getAccessLogResultType())) {
					// 新規は必ずアクセスログ処理結果が「不明」
					repository.insert(header);
				}
				else if (StringUtils.isNotEmpty(header.getAccessLogResultType())){
					// アクセスログ処理結果の更新
					repository.update(header.getAccessLogId(), header.getAccessLogResultType());
				}
			}
			else if (obj instanceof MwtAccessLogDetail) {
				// 明細の登録
				final MwtAccessLogDetail detail = (MwtAccessLogDetail)obj;
				repository.insert(detail);
			}
		}
		return -1L;
	}
}
