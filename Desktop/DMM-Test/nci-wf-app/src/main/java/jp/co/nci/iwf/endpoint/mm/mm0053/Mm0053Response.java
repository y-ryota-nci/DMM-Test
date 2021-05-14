package jp.co.nci.iwf.endpoint.mm.mm0053;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsSequenceSpecEx;

/**
 * 通し番号設定レスポンス
 */
public class Mm0053Response extends BaseResponse {
	public MwmPartsSequenceSpecEx sequence;

	public List<OptionItem> deleteFlagList;
	public List<OptionItem> sequenceLengthList;
	public List<OptionItem> resetTypeList;

}
