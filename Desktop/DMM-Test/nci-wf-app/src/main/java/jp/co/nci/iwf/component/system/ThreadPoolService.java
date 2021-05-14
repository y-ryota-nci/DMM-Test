package jp.co.nci.iwf.component.system;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * スレッドプールサービス
 */
@ApplicationScoped
public class ThreadPoolService {
	/** スレッドプール */
	private ExecutorService pool = Executors.newFixedThreadPool(32);
	/** ロガー */
	private Logger log = LoggerFactory.getLogger(ThreadPoolService.class);
	/** 終了待ちタイムアウトミリ秒 */
	private static final long awaitTime = TimeUnit.SECONDS.toMillis(60);

	/**
	 * タスクの実行
	 * @param task
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return pool.submit(task);
	}

	/**
	 * タスクの実行
	 * @param task
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Future<?> submit(Runnable task) {
		return pool.submit(task);
	}

	/**
	 * スレッドプールの終了処理
	 */
	@PreDestroy
	public void dispose() {
		try {
			// 各プールへの終了通知
			pool.shutdown();

			// 一定時間、待機
			if(!pool.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)){
				// タイムアウトした場合、全てのスレッドを中断(interrupted)してスレッドプールを破棄する。
				pool.shutdownNow();
			}
		}
		catch (InterruptedException e) {
			log.warn("awaitTermination interrupted: ", e);
			pool.shutdownNow();
		}
	}
}
