package jp.co.dmm.customize.endpoint.py.py0071.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.py.py0071.Py0071Entity;

public class Py0071SheetPayableBalDtl implements Serializable {
	/** 買掛残高詳細 */
	@XlsHorizontalRecords(tableLabel="買掛残高詳細")
	public List<Py0071Entity> payableBalDtls = new ArrayList<>();
}
