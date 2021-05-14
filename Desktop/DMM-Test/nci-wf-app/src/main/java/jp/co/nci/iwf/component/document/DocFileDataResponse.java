package jp.co.nci.iwf.component.document;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;

public class DocFileDataResponse extends BaseResponse {

	public List<MwtDocFileData> fileDatas;
}
