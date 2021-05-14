package jp.co.dmm.customize.endpoint.py.py0070.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.py.py0070.Py0070Entity;

public class Py0070SheetPayableBal implements Serializable {
	/** 買掛残高 */
	@XlsHorizontalRecords(tableLabel="買掛残高")
	public List<Py0070Entity> payableBals = new ArrayList<>();
}
