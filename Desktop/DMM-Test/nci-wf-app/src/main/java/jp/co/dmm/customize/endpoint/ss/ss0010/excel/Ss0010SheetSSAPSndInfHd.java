package jp.co.dmm.customize.endpoint.ss.ss0010.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsapSndInfHd;

public class Ss0010SheetSSAPSndInfHd implements Serializable {
	/** SS-AP送信情報(ヘッダー) */
	@XlsHorizontalRecords(tableLabel="SS-AP送信情報(ヘッダー)")
	public List<Ss0010SsapSndInfHd> ssapSndInfHds = new ArrayList<>();
}
