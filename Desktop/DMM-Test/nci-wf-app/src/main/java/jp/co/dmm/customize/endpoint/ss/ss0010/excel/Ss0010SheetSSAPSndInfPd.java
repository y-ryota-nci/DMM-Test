package jp.co.dmm.customize.endpoint.ss.ss0010.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfPd;

public class Ss0010SheetSSAPSndInfPd implements Serializable {
	/** SS-AP送信情報(支払明細) */
	@XlsHorizontalRecords(tableLabel="SS-AP送信情報(支払明細)")
	public List<Ss0010SsapSndInfPd> ssapSndInfPds = new ArrayList<>();
}
