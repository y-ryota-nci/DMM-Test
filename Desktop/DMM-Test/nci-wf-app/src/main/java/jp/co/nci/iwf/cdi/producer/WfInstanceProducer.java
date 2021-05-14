package jp.co.nci.iwf.cdi.producer;

import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.integrated_workflow.wrapper.impl.WfInstanceWrapperImpl;
import jp.co.nci.iwf.component.i18n.LocaleService;

/**
 * WF APIのインスタンス取得するためののProducer(Factory)
 */
@ApplicationScoped
public class WfInstanceProducer {
	@Inject
	private LocaleService localeService;


	/**
	 * WF APIのインスタンス生成
	 * @return
	 */
	@RequestScoped
	@Produces
	public WfInstanceWrapper createWfInstance() {
		final Locale locale = localeService.getLocale();

		// オリジナル版では Hibernate+Springで SessionFactoryをDIしていたが、
		// Restfull版ではJPA+CDIなのでSessionFactoryがない。
		// よってWfInstanceWrapperへのProxyを作成し、そのinitialize()に割り込んで
		// Connectionをセットしてやる。

		// またこの時のトランザクションは deltaspikeのTransactionalInterceptorによって
		// JTAで管理される。これにはbeans.xmlでBeanManagedUserTransactionStrategyが
		// 登録されていることを確認せよ
		final WfInstanceWrapper wf = new WfInstanceWrapperImpl();
		final WfInstanceWrapper proxy = WfInstanceProxy.createProxy(wf);
		proxy.setLocale(locale);
		return proxy;
	}

	/**
	 * WF APIのインスタンスからリソース解放
	 * @param wf
	 */
	public void disposeWfInstance(@Disposes WfInstanceWrapper wf) {
		wf.dispose();
	}
}
