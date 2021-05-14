package jp.co.dmm.customize.endpoint.ss.ss0010.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;

import jp.co.dmm.customize.endpoint.ss.ss0010.entity.Ss0010SsglSndInfDt;

public class Ss0010SheetSSGLSndInfDt implements Serializable {
	/** SS-GL送信情報(明細) */
	@XlsHorizontalRecords(tableLabel="SS-GL送信情報(明細)")
	public List<Ss0010SsglSndInfDt> ssglSndInfDts = new ArrayList<>();
}
