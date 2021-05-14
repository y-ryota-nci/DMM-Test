package jp.co.nci.iwf.endpoint.mm.mm0012;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * ルックアップグループ設定リクエスト
 */
public class Mm0012Request extends BasePagingRequest {
	public String corporationCode;
	public String lookupGroupId;
	public Long timestampUpdated;

	public List<MwmLookup> deleteLookups;
}
