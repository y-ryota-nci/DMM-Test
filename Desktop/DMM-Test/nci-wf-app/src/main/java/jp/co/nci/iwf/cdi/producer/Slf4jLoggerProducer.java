package jp.co.nci.iwf.cdi.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * 呼び元のクラスに対して、適切なロガーをInjectするためのProducer(Factory)
 */
@ApplicationScoped
public class Slf4jLoggerProducer {
	@Dependent
	@Produces
	public org.slf4j.Logger create(InjectionPoint ip) {
		return org.slf4j.LoggerFactory.getLogger(
				ip.getMember().getDeclaringClass());
	}
}
