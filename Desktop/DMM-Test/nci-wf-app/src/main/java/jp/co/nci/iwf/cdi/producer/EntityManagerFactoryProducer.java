package jp.co.nci.iwf.cdi.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.slf4j.Logger;

/**
 * JPAのEntityManagerFactoryをInject出来るようにするためのProducer(Factory).
 * Tomcatでは @PersistenceUnit や @PersistenceContext が使えないので
 * 通常のCDI管理BeanとしてProduceしてやる。
 */
@ApplicationScoped
public class EntityManagerFactoryProducer {
	@Inject
	private Logger log;

	/**
	 * デフォルトのEntityManagerをインスタンス化
	 * @return
	 */
	@Produces
	@RequestScoped
	public EntityManager createPrimary() {
		EntityManagerFactory fac = CDI.current().select(EntityManagerFactory.class).get();
		EntityManager em = fac.createEntityManager();
		log.trace("=== create EntityManager({}) === ", em.hashCode());
		return em;
	}

	/**
	 * EntityManagerのリソース解放
	 * @param em
	 */
	protected void disposeEntityManager(@Disposes EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		if (tx != null && tx.isActive()) {
			log.trace("=== rollback transaction({}) ===", tx.hashCode());
			tx.rollback();
		}
		if (em.isOpen()) {
			log.trace("=== close EntityManager({}) ===", em.hashCode());
			em.close();
		}
	}

	/**
	 * デフォルトのEntityManagerFactoryをインスタンス化
	 * @return
	 */
	@Produces
	@ApplicationScoped
	public EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("primary");
	}

	/**
	 * EntityManagerFactoryのリソース解放
	 * @param fac
	 */
	public void disposeEntityManagerFactory(@Disposes EntityManagerFactory fac) {
		if (fac != null && fac.isOpen())
			fac.close();
	}
}
