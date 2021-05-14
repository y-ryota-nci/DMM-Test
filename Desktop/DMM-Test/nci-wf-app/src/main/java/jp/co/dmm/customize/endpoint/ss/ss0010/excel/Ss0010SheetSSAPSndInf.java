package jp.co.dmm.customize.endpoint.ss.ss0010.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInf;

public class Ss0010SheetSSAPSndInf implements Serializable {
	/** SS-AP送信情報 */
	@XlsHorizontalRecords(tableLabel="SS-AP送信情報")
	public List<Ss0010SsapSndInf> ssapSndInfs = new ArrayList<>();
}
