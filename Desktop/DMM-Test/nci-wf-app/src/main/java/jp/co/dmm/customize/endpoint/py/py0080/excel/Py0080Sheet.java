package jp.co.dmm.customize.endpoint.py.py0080.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.py.py0080.Py0080Entity;

public class Py0080Sheet implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 前払残高 */
	@XlsHorizontalRecords(tableLabel="前払残高")
	public List<Py0080Entity> results = new ArrayList<>();

}
