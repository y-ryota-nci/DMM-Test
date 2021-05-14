package jp.co.nci.iwf.endpoint.mm.mm0510;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * バージョン情報画面サービス
 */
@BizLogic
public class Mm0510Service extends BaseService {

	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0510InitResponse init(BaseRequest req) {
		final Mm0510InitResponse res = createResponse(Mm0510InitResponse.class, req);

		// マニフェスト情報
		res.groupId = manifest.getGroupId();
		res.artifactId = manifest.getArtifactId();
		res.version = manifest.getVersion();
		res.buildBy = manifest.getBuildBy();
		res.buildTimestamp = manifest.getBuildTimestamp();
		res.buildJdk = manifest.getBuildJdk();

		// DB接続先
		res.databaseURL = destination.getUrl();
		res.databaseUser = destination.getUser();
		res.databaseName = destination.getDatabaseName();

		res.success = true;
		return res;
	}

}
