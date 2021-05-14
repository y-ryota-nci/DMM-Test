package jp.co.nci.iwf.endpoint.vd.vd0161;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.service.ContainerLoadRepository;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;

/**
 * 背景HTML設定画面のサービス
 */
@BizLogic
public class Vd0161Service extends BaseService {

	@Inject
	private ContainerLoadRepository loadRepository;
	@Inject
	private Vd0161Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 保存(Ctrl+S)
	 * @param req
	 * @return
	 */
	@Transactional
	public Vd0161InitResponse save(Vd0161SaveRequest req) {
		final Vd0161InitResponse res = createResponse(Vd0161InitResponse.class, req);

		// コンテナIDがDBに存在するとき、コンテナ定義を更新
		MwmContainer container = repository.get(req.entity.containerId);

		if (container == null) {
			// DBに登録済か判定
			res.addAlerts(i18n.getText(MessageCd.MSG0156, MessageCd.containerId));
		} else if (!eq(container.getVersion(), req.entity.getVersion())){
			// 排他判定
			throw new AlreadyUpdatedException();
		} else {
			repository.update(container, req.entity);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.bgHtml));

			res.entity = new Vd0161Entity();
			final MwmContainer srcCntr = loadRepository.getMwmContainer(req.entity.containerId);
			res.entity.setVersion(srcCntr.getVersion());
			res.success = true;
		}

		return res;
	}
}
