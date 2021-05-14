package jp.co.nci.iwf.cdi.producer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jp.co.nci.integrated_workflow.api.param.InParamBase;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;

/**
 * WfInstanceWrapperにConnectionをDIするためのProxy
 */
public class WfInstanceProxy {
	private WfInstanceWrapper wf;
	private Object proxy;

	private WfInstanceProxy(WfInstanceWrapper wf) {
		this.wf = wf;
		this.proxy = Proxy.newProxyInstance(
				WfInstanceWrapper.class.getClassLoader(),
				new Class[] { WfInstanceWrapper.class },
				new WfInstanceHandler());
	}

    /**
     * 外部からはこのメソッドを通じてProxyを取得します.
     */
    public static WfInstanceWrapper createProxy(WfInstanceWrapper some) {
    	WfInstanceProxy obj = new WfInstanceProxy(some);
        return WfInstanceWrapper.class.cast(obj.proxy);
    }

    /**
     * Proxyでの割り込みハンドラー
     */
	private class WfInstanceHandler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// WfInstanceWrapperのメソッド呼び出しに割り込んで、wfInstanceとconnectionを設定
			boolean target =
					args != null
					&& args.length > 0
					&& (args[0] instanceof InParamBase);
			if (target) {
				// CDIからEntityManagerを取得し、そのConnectionを求める
				// このときアクティブなトランザクションがないとConnectionがnullであるため、
				// 改めてトランザクションを開始する。CloseはTransactionalInterceptorが自動で行う
				EntityManager em = getEntityManager();
				EntityTransaction tx = em.getTransaction();
				if (tx != null && !tx.isActive()) {
					tx.begin();
				}
				final Connection conn = em.unwrap(Connection.class);
				wf.createWfInstance(conn);
			}

			Object r = method.invoke(wf, args);

			return r;
		}
	}

	public void dispose() {
		EntityManager em = getEntityManager();
		if (em != null) {
			EntityTransaction tx = em.getTransaction();
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		wf.dispose();
		wf = null;
		proxy = null;
	}

	private EntityManager getEntityManager() {
		return CDI.current().select(EntityManager.class).get();
	}
}
