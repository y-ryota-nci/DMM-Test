package jp.co.nci.iwf.jersey.provider;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

//@Provider
//@ApplicationScoped
public class MOXyConfig implements ContextResolver<JAXBContext> {
	private JAXBContext config;

	public MOXyConfig() {
		try {
			config = JAXBContext.newInstance(getClass().getPackage().getName());
		} catch (JAXBException e) {
			throw new InternalServerErrorException(e);
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		return config;
	}

}
