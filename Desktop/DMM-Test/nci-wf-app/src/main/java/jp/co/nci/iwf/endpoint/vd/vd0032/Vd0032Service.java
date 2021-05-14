package jp.co.nci.iwf.endpoint.vd.vd0032;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.service.ContainerLoadService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 画面定義のパーツツリー画面サービス.
 */
@BizLogic
public class Vd0032Service extends BaseService {

	@Inject
	private ContainerLoadService partsLoadService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0032InitResponse init(Vd0032InitRequest req) {
		// デザイナーコンテキストの生成
		final DesignerContext ctx = DesignerContext.designInstance(req.viewWidth);
		partsLoadService.loadRootDesign(req.containerId, ctx);

		final Vd0032InitResponse res = createResponse(Vd0032InitResponse.class, req);
		res.ctx = ctx;
		res.success = true;
		return res;
	}
}
