package jp.co.dmm.customize.endpoint.po.po0040;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.StreamingReader.Builder;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * kintoneデータアップロードのEXCELファイルのリーダー.
 */
@ApplicationScoped
public class Po0040ExcelReader extends PoiUtils {

	/** EXCELファイルを読み込んで、ブックを生成 */
	public Po0040Book parse(InputStream stream) throws IOException {
		// ストリーミングとしてEXCELを読み込む
		final Builder builder = StreamingReader.builder()
				.rowCacheSize(100)
				.bufferSize(4096);
		try (Workbook workbook = builder.open(stream)) {
			final Po0040Book book = new Po0040Book();
			// kintone取込では最初のシートしか使用しない(他のシートは無視)
			final List<Po0040KntnInf> list = read(workbook.getSheetAt(0));
			book.kntnInfs = list;
			return book;
		}
	}

	/** シートを読み込み、kintoneデータを生成. */
	private List<Po0040KntnInf> read(Sheet sheet) {
		final List<Po0040KntnInf> list = new ArrayList<>();
	    int r = 0;
	    int sqno = 1;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			// A列：レコード番号が未入力なら終わり
			int i = 0;
			String recNo = getCellStringValue(row, i++);
			if (isEmpty(recNo)) {
				break;
			}
			final Po0040KntnInf bean = new Po0040KntnInf();
			bean.sqno = sqno++;
			bean.recNo = recNo;
			bean.evntNo = getCellStringValue(row, i++);
			bean.kntnSts = getCellStringValue(row, i++);
			bean.exhbDt = getCellStringValue(row, i++);
			bean.evntMngNo = getCellStringValue(row, i++);
			bean.evntCont = getCellStringValue(row, i++);
			bean.kntnHllId = getCellStringValue(row, i++);
			bean.hllNm = getCellStringValue(row, i++);
			bean.prdctId = getCellStringValue(row, i++);
			bean.tlntNm = getCellStringValue(row, i++);
			bean.splrCd = getCellStringValue(row, i++);
			bean.prdctNm = getCellStringValue(row, i++);
			bean.baseAmt = getCellLongValue(row, i++);
			bean.adjBaseAmt = getCellLongValue(row, i++);
			bean.invAmt = getCellLongValue(row, i++);
			bean.trnspExpAmt = getCellLongValue(row, i++);
			bean.adjTrnspExpAmt = getCellLongValue(row, i++);
			bean.mnscrExpAmt = getCellLongValue(row, i++);
			bean.bumonCd = getCellStringValue(row, i++);
			bean.anlysCd = getCellStringValue(row, i++);
			bean.smry = getCellStringValue(row, i++);
//			bean.taxCd = getCellStringValue(row, i++);
			bean.taxFgChg = getCellStringValue(row, i++);
			bean.taxUnt = getCellStringValue(row, i++);
			bean.itmExpsCd1 = getCellStringValue(row, i++);
			bean.itmExpsCd2 = getCellStringValue(row, i++);
			list.add(bean);
		}
		return list;
	}

}
