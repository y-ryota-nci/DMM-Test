package jp.co.dmm.customize.endpoint.py.py0070.excel;

import java.io.Serializable;

public class Py0070Book implements Serializable {
	/** 区切り文字 */
	public final String SEPARATOR = "\t";

	/** シート「買掛残高」 */
	public Py0070SheetPayableBal sheetPayableBal;
}
