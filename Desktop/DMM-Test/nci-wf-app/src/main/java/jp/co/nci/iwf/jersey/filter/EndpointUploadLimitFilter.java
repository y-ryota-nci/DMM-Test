package jp.co.nci.iwf.jersey.filter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードリクエストでの、コンテンツサイズ制限を行うフィルター
 */
@Provider
//@PreMatching	/* これがあるとEndpointの特定前のため、フィルターが全リクエストに対して掛かってしまう */
@Priority(Priorities.HEADER_DECORATOR)
@RequiredLogin	// これが付与されたEndpointに対して当フィルターを適用
@ApplicationScoped
public class EndpointUploadLimitFilter extends MiscUtils implements ContainerRequestFilter {
	/** プロパティサービス */
	@Inject private CorporationPropertyService prop;

	/** byte -> MB変換用の係数 */
	private static final int UNIT_MB = 1024 * 1024;

	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		// Multipartであればアップロードとみなす
		if (MediaType.MULTIPART_FORM_DATA_TYPE.isCompatible(crc.getMediaType())) {
			final int length = crc.getLength();
			final int max = prop.getInt(CorporationProperty.MAX_FILE_SIZE_PER_UPLOAD, 20) * UNIT_MB;
			if (max > 0 && length > max) {
				throw new InvalidUserInputException(MessageCd.MSG0275, toMB(length), toMB(max));
			}
		}
	}

	/** バイトをMBへ変換 */
	private String toMB(int size) {
		return new BigDecimal(size / UNIT_MB).setScale(1, RoundingMode.UP).toString();
	}
}
