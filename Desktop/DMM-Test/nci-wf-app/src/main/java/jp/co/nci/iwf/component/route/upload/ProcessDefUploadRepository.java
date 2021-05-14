package jp.co.nci.iwf.component.route.upload;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * プロセス定義のアップロードのリポジトリ
 */
@ApplicationScoped
public class ProcessDefUploadRepository extends BaseRepository {

	public void upsert(WfmNameLookup lookup) {
		em.merge(lookup);
	}
}
