package jp.co.nci.iwf.cdi.intercepter;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.slf4j.Logger;

/**
 * @Transactional をTOMCAT環境で実現するためのインターセプター。
 * TOMCATでは必要だけれど、Glassfish／WebLogic／Wildfly／JBOSSなどのJ2EEサーバでは不要である、
 * というよりJ2EEの機能と被るので、その場合は当インターセプターは殺しておく必要がある。
 */
@Transactional
@Interceptor
@Dependent
@Priority(Interceptor.Priority.PLATFORM_BEFORE+200) // javax.transaction.Transactionalのコメントに従って、優先度は「PLATFORM_BEFORE+200」固定
public class TransactionInterceptor {

	@Inject
	private Logger log;

	@AroundInvoke
	public Object doTransaction(InvocationContext ic) throws Exception {
		final EntityManager em = CDI.current().select(EntityManager.class).get();
		final EntityTransaction tx = em.getTransaction();
		final String classAndMethod = ic.getMethod().getDeclaringClass().getSimpleName()
				+ "." + ic.getMethod().getName() + "()";
		boolean isBeginTrans = false;
		try {
			// @TransactionalのTxTypeを判定してないので、正しく動作しないケースあり
			if (!tx.isActive()) {
				long start = System.currentTimeMillis();
				log.info("=== begin transaction === {}", classAndMethod);
				tx.begin();
				isBeginTrans = true;
				long now = System.currentTimeMillis();
				if (now - start > 1000L)
					log.info("★★★ 'begin transaction'の完了までに{}ミリ秒が掛かりました", now - start);
			}

			final Object result = ic.proceed();

			// このインターセプターで始めたトランザクションはここでコミット
			if (isBeginTrans && tx.isActive()) {
				if (tx.getRollbackOnly()) {
					log.info("=== rollback === {}", classAndMethod);
					tx.rollback();
				}
				else {
					log.info("=== commit === {}", classAndMethod);
					tx.commit();
				}
			}
			return result;
		}
		catch (Exception e) {
			try {
				// このインターセプターで始めたトランザクションはここで、
				// それ以外は開始元でロールバックさせるためにフラグだけセット
				if (tx.isActive()) {
					if (isBeginTrans) {
						log.info("=== rollback === {}", classAndMethod);
						tx.rollback();
					}
					else {
						tx.setRollbackOnly();
					}
				}
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			throw e;
		}
	}
}
