package jp.co.nci.iwf.component.document;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BinderInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BinderInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtBinderInfo;

/**
 * バインダーサービス
 */
@BizLogic
public class BinderService extends BaseService {

	@Inject private BinderRepository repository;

	/**
	 * バインダー情報Entity取得.
	 * @param docId 文書ID
	 * @return
	 */
	public BinderInfoEntity getBinderInfoEntity(Long docId) {
		return repository.getBinderInfoEntity(docId);
	}

	public BinderInfo getBinderInfo(Long docId) {
		return new BinderInfo(this.getBinderInfoEntity(docId));
	}

	/**
	 * バインダー情報の差分更新
	 * @param docId
	 * @param title
	 * @param bizDocInfo
	 * @param versionInfo
	 * @param ctx
	 */
	public void saveBinderInfo(Long docId, BinderInfo binderInfo) {
		final MwtBinderInfo org = repository.getMwtBinderInfo(docId);
		if (org == null) {
			binderInfo.docId = docId;
			repository.insert(binderInfo);
		} else {
			repository.update(org, binderInfo);
		}
	}
}
