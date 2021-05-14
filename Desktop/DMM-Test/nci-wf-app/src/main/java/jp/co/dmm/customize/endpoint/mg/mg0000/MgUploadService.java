package jp.co.dmm.customize.endpoint.mg.mg0000;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BasePagingService;

public abstract class MgUploadService extends BasePagingService {

	public abstract Response upload(FormDataMultiPart multiPart);

	public abstract Response download(BasePagingRequest req);

	public abstract Response register(MgUploadSaveRequest req);
}
