package jp.co.nci.iwf.endpoint.mm.mm0051;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 採番形式設定レスポンス
 */
public class Mm0051Response extends BaseResponse {
	public List<OptionItem> deleteFlagList;
	public List<OptionItem> numberingFormatTypes;
	public List<OptionItem> numberingFormatDates;
	public List<OptionItem> sequenceList;
	public List<OptionItem> numberingFormatOrgs;
	public Mm0051Entity entity;

}
